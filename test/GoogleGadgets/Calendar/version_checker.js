/*
Copyright (C) 2008 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

function Version() {
  this.major = null;
  this.minor = null;
  this.build = null;
  this.revision = null;
}

Version.prototype.isValid = function() {
  return !isNaN(this.major) &&
    !isNaN(this.minor) &&
    !isNaN(this.build) &&
    !isNaN(this.revision);
};

Version.prototype.toString = function() {
  return this.major + '.' +
      this.minor + '.' +
      this.build + '.' +
      this.revision;
};

Version.prototype.isGreater = function(other) {
  if (this.major > other.major) {
    return true;
  }
  if (this.minor > other.minor) {
    return true;
  }
  if (this.build > other.build) {
    return true;
  }
  if (this.revision > other.revision) {
    return true;
  }

  return false;
};

Version.parse = function(versionString) {
  var versionParts = versionString.split('.');

  if (versionParts.length != 4) {
    return null;
  }

  var version = new Version();
  version.major = parseInt(versionParts[0]);
  version.minor = parseInt(versionParts[1]);
  version.build = parseInt(versionParts[2]);
  version.revision = parseInt(versionParts[3]);

  if (version.isValid()) {
    return version;
  }

  return null;
};

function UpgradeInfo() {
  this.mandatoryVersion = null;
  this.downloadUrl = '';
  this.reason  = '';
  this.infoUrl = '';
}

UpgradeInfo.DGC_PATH = 'http://desktop.google.com/plugins/';

UpgradeInfo.isValidUrl = function(url) {
  return url.indexOf(UpgradeInfo.DGC_PATH) === 0;
};

UpgradeInfo.parse = function(response) {
  var parts = response.split('\n');
  var info = new UpgradeInfo();

  if (parts.length < 4) {
    return null;
  }

  info.mandatoryVersion = Version.parse(parts[0]);

  if (!info.mandatoryVersion) {
    return null;
  }

  info.downloadUrl = parts[1];
  info.reason = parts[2];
  info.infoUrl = parts[3];

  if (!UpgradeInfo.isValidUrl(info.downloadUrl)) {
    debug.error('Invalid downloadUrl.');
    return null;
  }

  if (!UpgradeInfo.isValidUrl(info.infoUrl)) {
    debug.error('Invalid infoUrl.');
    return null;
  }

  return info;
};

UpgradeInfo.prototype.toString = function() {
  return this.mandatoryVersion + '\n' +
      this.downloadUrl + '\n' +
      this.reason + '\n' +
      this.infoUrl;
};

VersionChecker.CHECK_INTERVAL = 60 * 60 * 1000;
VersionChecker.LAST_PING_ON_KEY = 'version_checker_last_ping_on';

function VersionChecker(currentVersionString, url, onUpgradeNeeded) {
  this.isActive = true;
  this.currentVersion = Version.parse(currentVersionString);
  this.url = url;
  this.onUpgradeNeeded = onUpgradeNeeded;

  if (!this.currentVersion) {
    debug.error('Cannot initialize VersionChecker. Invalid current version string.');
    return;
  }

  this.check();
  this.checkTimer = view.setInterval(this.makeCheck(),
      VersionChecker.CHECK_INTERVAL);
}

VersionChecker.prototype.getLastPingOn = function() {
  var d = options.getValue(VersionChecker.LAST_PING_ON_KEY);

  if (d) {
    return new Date(d);
  }

  return null;
};

VersionChecker.prototype.setLastPingOn = function(d) {
  if (d) {
    options.putValue(VersionChecker.LAST_PING_ON_KEY, d.getTime());
  } else {
    options.putValue(VersionChecker.LAST_PING_ON_KEY, '');
  }
};

VersionChecker.BASE_FUZZ = 15 * 60 * 1000;

VersionChecker.prototype.makeFuzz = function() {
  var fuzz = VersionChecker.BASE_FUZZ;
  fuzz *= Math.random();
  fuzz = Math.floor(fuzz);

  return fuzz;
};

VersionChecker.prototype.makeCheck = function() {
  var me = this;

  return function() {
    me.check();
  };
};

VersionChecker.prototype.makePing = function() {
  var me = this;

  return function() {
    me.ping();
  };
};

VersionChecker.prototype.schedulePing = function() {
  var interval = this.makeFuzz();

  debug.error('Scheduling version check in ' + interval);
  view.setTimeout(this.makePing(), interval);
};

VersionChecker.prototype.makeOnReadyStateChange = function(request) {
  var me = this;

  return function() {
    me.onReadyStateChange(request);
  };
};

VersionChecker.prototype.check = function() {
  if (!this.isActive) {
    return;
  }

  var now = new Date();

  var lastPingOn = this.getLastPingOn();

  if (lastPingOn) {
    if (now.getMonth() == lastPingOn.getMonth() &&
        now.getDate() == lastPingOn.getDate()) {
      debug.trace('Already attempted a ping today.');
      return;
    }
  }

  this.setLastPingOn(now);
  this.schedulePing();
};

VersionChecker.prototype.ping = function() {
  var request = new XMLHttpRequest();
  request.open('GET', this.url, true);
  request.onreadystatechange = this.makeOnReadyStateChange(request);
  request.send();
};

VersionChecker.prototype.onReadyStateChange = function(request) {
  if (request.readyState != 4) {
    return;
  }

  if (request.status == 200) {
    var upgradeInfo = UpgradeInfo.parse(request.responseText);

    if (!upgradeInfo) {
      debug.error('Could not parse version info.');
      return;
    }

    if (upgradeInfo.mandatoryVersion.isGreater(this.currentVersion)) {
      this.isActive = false;
      // No more need to check.
      view.clearTimeout(this.checkTimer);
      this.onUpgradeNeeded(upgradeInfo);
    }
  }
};
