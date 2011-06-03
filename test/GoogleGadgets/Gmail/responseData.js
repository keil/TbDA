// Copyright 2007 Google Inc.
// All Rights Reserved.

// @fileoverview Functions for parsing and handling a server response

function ResponseData(binData) {
  try {
    this.dataArr = this.convertToArray(binData);
    this.arrayIndex = 0;
    debug.trace('All data has been parsed.');
    debug.trace('');
    debug.trace('----------------------------------------');
    debug.trace('');
    imgLoading.visible = false;
  } catch(e) {
    // rare occurance, if the details view is closing when receiving data
    return;
  }
}



ResponseData.prototype.getNextLine = function() {
  var line = this.dataArr[this.arrayIndex++];
  // since BLANK_LINE is a normal character, if it appears twice, the first one
  // isn't actualy part of the response line
  if (line.charAt(1) == BLANK_LINE && line.charAt(0) == BLANK_LINE) {
    line = line.substr(1);
  }
  return line;
};



ResponseData.prototype.decPtr = function() {
  --this.arrayIndex;
};



ResponseData.prototype.skipLines = function(lines) {
  this.arrayIndex += lines;
};



/**
 * This function converts from utf8 to unicode
 */
ResponseData.prototype.fromUtf8ToUnicode = function(arr, left, right) {
  var temp;
  var ret = '';
  var flag = 0;
  var tmp;
  for (var i = left; i < right; i++) {
    temp = arr[i];
    if (flag === 0) {
      if ((temp & 0xe0) == 0xe0) {
        flag = 2;
        tmp = (temp & 0x0f) << 12;
      } else if ((temp & 0xc0) == 0xc0) {
        flag = 1;
        tmp = (temp & 0x1f) << 6;
      } else if ((temp & 0x80) === 0) {
        ret += String.fromCharCode(arr[i]);
      } else {
        flag = 0;
      }
    } else if (flag == 1) {
      flag = 0;
      ret += String.fromCharCode(tmp | (temp & 0x3f));
    } else if (flag == 2) {
      flag = 3;
      tmp |= (temp & 0x3f) << 6;
    } else if (flag == 3) {
      flag = 0;
      ret += String.fromCharCode(tmp | (temp & 0x3f));
    } else {
      flag = 0;
    }
  }
  return ret;
};



ResponseData.prototype.convertToArray = function(binData) {
  debug.trace('Parsing of data has begun...');

  // This workaround needed typecasting binary data to a string
  // we do this by assigning it to a label
  // this does implicit typecasting
  try {
    label.innerText = binData;
  } catch(e) {
    // rare occurance, if the details view is closing when receiving data
    return '';
  }
  var temp = label.innerText;
  // clear the label innerText, otherwise it can slowdown the entire sidebar
  // as it's drawn every time
  // TODO: why was drawString getting repeatedly called on a hidden element?
  label.innerText = '';
  var initArr = new Array(2 * temp.length);
  var dataArr = new Array();

  debug.trace('Binary data first conversion through label (' +
              temp.length/1024 + ' KB)');
  debug.trace('Starting MSB - LSB swap...');
  var i;
  for (i = 0; i < temp.length; ++i) {
    // The binary format in which we recieve the text has MSB and LSB swapped
    // Hence the modulus and division by 256
    // it is basically little endian big endian incompatibility
    var c = temp.charCodeAt(i);
    initArr[2 * i + 1] = (c - (initArr[2 * i] = c % 256)) / 256;
  }

  for (i = 0; i < initArr.length;) {
    var numBytes = parseInt(initArr[i] * 256 + initArr[i + 1], 10);
    i += 2;
    dataArr.push(this.fromUtf8ToUnicode(initArr, i, i + numBytes));
    i += numBytes;
  }

  debug.trace('Data has been converted from text to array...');
  return dataArr;
};
