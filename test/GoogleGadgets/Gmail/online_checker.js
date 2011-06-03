OnlineChecker.PING_URL = 'http://clients2.google.com';
OnlineChecker.CHECK_INTERVAL_MS = 10 * 60 * 1000;
OnlineChecker.PING_TIMEOUT_MS = 30 * 1000;
OnlineChecker.USE_FRAMEWORK_API = true;

function OnlineChecker() {
  // Assume online at first.
  this.isPingSucceeded = true;
  this.timeoutTimer = null;
  this.pingTimer = null;
  this.ping();
}

OnlineChecker.prototype.isOnline = function() {
  if (OnlineChecker.USE_FRAMEWORK_API) {
    if (framework.system.network.online) {
      this.isPingSucceeded = true;

      return true;
    }
  }

  return this.isPingSucceeded;
};

OnlineChecker.prototype.ping = function() {
  var interval = OnlineChecker.CHECK_INTERVAL_MS;
  var fuzz = interval / 5;
  fuzz *= Math.random();
  fuzz = Math.floor(fuzz);
  interval += fuzz;
  this.pingTimer = view.setTimeout(this.makePing(), interval);

  if (OnlineChecker.USE_FRAMEWORK_API) {
    if (framework.system.network.online) {
      this.isPingSucceeded = true;

      return;
    }
  }

  var request = new XMLHttpRequest();
  request.open('GET', OnlineChecker.PING_URL, true);
  this.timeoutTimer = view.setTimeout(
      this.makeOnTimeout(),
      OnlineChecker.PING_TIMEOUT_MS);
  request.onreadystatechange = this.makeOnReadyStateChange(request);

  debug.info('Sending ping.');
  request.send();
};

OnlineChecker.prototype.onTimeout = function() {
  debug.info('Ping timed out.');
  this.isPingSucceeded = false;
};

OnlineChecker.prototype.onReadyStateChange = function(request) {
  if (request.readyState != 4) {
    return;
  }

  if (request.status == 200) {
    if (this.timeoutTimer) {
      debug.info('Clearing ping timer.');
      view.clearTimeout(this.timeoutTimer);
      this.timeoutTimer = null;
    }

    debug.info('Ping succeeded.');
    this.isPingSucceeded = true;
  }
};

OnlineChecker.prototype.makeOnTimeout = function() {
  var me = this;

  return function() {
    me.onTimeout();
  };
};

OnlineChecker.prototype.makePing = function() {
  var me = this;

  return function() {
    me.ping();
  };
};

OnlineChecker.prototype.makeOnReadyStateChange = function(request) {
  var me = this;

  return function() {
    me.onReadyStateChange(request);
  };
};
