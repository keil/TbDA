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

UploadFile.NEW_STATE = '';
UploadFile.PENDING_STATE = 'loading';
UploadFile.SUCCESS_STATE = 'success';
UploadFile.ERROR_STATE = 'error';

function UploadFile(filename) {
  this.filename = filename;
  this.title = this.filename.replace(/(.*?)\\([^\\]+?)(\.(\w+))?$/, '$2');
  this.state = UploadFile.NEW_STATE;
  this.errorCode = null;
  this.extension = filename.replace(/(.*?)\.(\w+)$/, '$2').toLowerCase();

  this.type = Document.UNKNOWN;
  this.mime = 'text/plain';

  this.link = '';

  var extensionDesc = FILE_EXTENSIONS[this.extension];

  if (extensionDesc) {
    this.type = extensionDesc.type;
    this.mime = extensionDesc.mime;
  }
}

UploadFile.prototype.isNew = function() {
  return this.state == UploadFile.NEW_STATE;
};

UploadFile.prototype.isPending = function() {
  return this.state == UploadFile.PENDING_STATE;
};

UploadFile.prototype.isSuccess = function() {
  return this.state == UploadFile.SUCCESS_STATE;
};

UploadFile.prototype.isError = function() {
  return this.state == UploadFile.ERROR_STATE;
};

UploadFile.prototype.isDone = function() {
  return this.isSuccess() || this.isError();
};

UploadFile.prototype.isUnknownType = function() {
  return this.type == Document.UNKNOWN;
};

UploadFile.prototype.getIcon = function() {
  return 'images/icon-' + this.type + '.gif';
};

UploadFile.prototype.getStatusIcon = function() {
  return 'images/icon-upload-' + this.state + '.gif';
};

UploadFile.CODE_415 = 415;
// These are local errors and should not be HTTP response codes.
UploadFile.CODE_CANCELLED = 1;

UploadFile.prototype.getError = function() {
  if (this.errorCode == UploadFile.CODE_CANCELLED) {
    return strings.ERROR_UPLOAD_CANCELLED;
  } else if (this.errorCode == UploadFile.CODE_415) {
    return strings.ERROR_UPLOAD_TYPE;
  } else if (this.errorCode == 401) {
    return strings.ERROR_UPLOAD_EXPIRED;
  } else if (this.errorCode === 0) {
    return strings.ERROR_UPLOAD_NETWORK;
  } else {
    return strings.ERROR_UPLOAD_CORRUPT;
  }
};
