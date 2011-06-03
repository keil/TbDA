//
// Atom to HTML - fetch a feed, inject it as dl/dt/dd
//

// Copyright 2009 Joshua Bell
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/*extern ActiveXObject */ // For jslint.com

function atomToHtml(uri, element) {

    var READYSTATE_UNINITIALIZED = 0;
    var READYSTATE_LOADING = 1;
    var READYSTATE_LOADED = 2;
    var READYSTATE_INTERACTIVE = 3;
    var READYSTATE_COMPLETE = 4;

    var xhr;
    try { if (!xhr && window.XMLHttpRequest) { xhr = new XMLHttpRequest(); } } catch (e) { }
    try { if (!xhr && window.ActiveXObject) { xhr = new ActiveXObject('Msxml2.XMLHTTP'); } } catch (e) { }
    try { if (!xhr && window.ActiveXObject) { xhr = new ActiveXObject('Msxml3.XMLHTTP'); } } catch (e) { }
    try { if (!xhr && window.ActiveXObject) { xhr = new ActiveXObject('Microsoft.XMLHTTP'); } } catch (e) { }
    if (!xhr) {
        element.innerHTML = '<em>Unable to load feed</em>';
        return;
    }

    xhr.open("GET", uri, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === READYSTATE_COMPLETE) {
            if (200 <= xhr.status && xhr.status < 400) {
                var doc = xhr.responseXML;
                var entries = doc.getElementsByTagName('entry');
                var html = [];

                html.push('<dl>');

                for (var i = 0; i < entries.length; i += 1) {
                    var entry = entries[i];

                    try {
                        var entryHTML = [];
                        entryHTML.push('<dt>', entry.getElementsByTagName('title')[0].childNodes[0].nodeValue, '</dt>');
                        entryHTML.push('<dd>', entry.getElementsByTagName('content')[0].childNodes[0].nodeValue, '</dd>');
                        html.push(entryHTML.join(''));
                    }
                    catch (e) {
                        if (console && console.log) { console.log("Error:", e); }
                    }

                }

                html.push('</dl>');

                element.innerHTML = html.join('');
            }
            else {
                element.innerHTML = '<em>Unable to load feed</em>';
            }
        }
    };
    xhr.send(null);
}

