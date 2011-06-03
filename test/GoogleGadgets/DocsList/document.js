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

function Document() {
  this.title = '';
  this.link = '';
  this.type = '';
  this.updated = null;
  this.date = '';
  this.starred = false;
}

Document.prototype.getIcon = function() {
  return 'images/icon-' + this.type + '.gif';
};

Document.UNKNOWN = 'unknown';
Document.DOCUMENT = 'document';
Document.SPREADSHEET = 'spreadsheet';
Document.PRESENTATION = 'presentation';
Document.FORM = 'form';

Document.buildNewDocumentUrl = function(type, appsDomain) {
  if (appsDomain) {
    appsDomain = 'a/' + appsDomain + '/';
  }

  return Document.NEW_DOC_MAP[type].replace('[![APPS_PATH]!]', appsDomain);
};

Document.NEW_DOC_MAP = {};
Document.NEW_DOC_MAP[Document.DOCUMENT] =
    'http://docs.google.com/[![APPS_PATH]!]MiscCommands?command=newdoc';
Document.NEW_DOC_MAP[Document.SPREADSHEET] =
    'http://spreadsheets.google.com/[![APPS_PATH]!]ccc?new';
Document.NEW_DOC_MAP[Document.PRESENTATION] =
    'http://docs.google.com/[![APPS_PATH]!]DocAction?action=new_presentation';
Document.NEW_DOC_MAP[Document.FORM] =
     'http://spreadsheets.google.com/[![APPS_PATH]!]newform';

var FILE_EXTENSIONS = {
  'csv': {
    mime: 'text/csv',
    type: Document.SPREADSHEET },
  'doc': {
    mime: 'application/msword',
    type: Document.DOCUMENT },
  'htm': {
    mime: 'text/html',
    type: Document.DOCUMENT },
  'html': {
    mime: 'text/html',
    type: Document.DOCUMENT },
  'ods': {
    mime: 'application/x-vnd.oasis.opendocument.spreadsheet',
    type: Document.SPREADSHEET },
  'odt': {
    mime: 'application/vnd.oasis.opendocument.text',
    type: Document.DOCUMENT },
  'pdf': {
    mime: 'application/pdf',
    type: Document.DOCUMENT },
  'pps': {
    mime: 'application/vnd.ms-powerpoint',
    type: Document.PRESENTATION },
  'ppt': {
    mime: 'application/vnd.ms-powerpoint',
    type: Document.PRESENTATION },
  'rtf': {
    mime: 'application/rtf',
    type: Document.DOCUMENT },
  'sxw': {
    mime: 'application/vnd.sun.xml.writer',
    type: Document.DOCUMENT },
  'tab': {
    mime: 'text/tab-separated-values',
    type: Document.SPREADSHEET },
  'txt': {
    mime: 'text/plain',
    type: Document.DOCUMENT },
  'tsv': {
    mime: 'text/tab-separated-values',
    type: Document.SPREADSHEET },
  'xls': {
    mime: 'application/vnd.ms-excel',
    type: Document.SPREADSHEET }
};

