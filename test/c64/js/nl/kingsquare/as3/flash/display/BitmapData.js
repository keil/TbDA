/*
 * Copyright notice
 *
 * (c) 2009 Tim de Koning - Kingsquare Information Services.  All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

nl.kingsquare.as3.flash.display.BitmapData = Class.extend({
    init: function(context) {
        this.context = context;
    },

    fillRect: function(rect/*:Rectangle*/, color/*:uint*/) {
    	this.context.clearRect(rect.x, rect.y, rect.width, rect.height);
    	this.context.fillStyle = 'rgba('+((color >> 16) & 0xFF) +
    			','+((color >> 8) & 0xFF)+','+((color >> 0) & 0xFF)+
    			', '+((color >> 24) & 0xFF)/255+')';
        this.context.fillRect(rect.x, rect.y, rect.width, rect.height);
    },

    setPixel32: function(x/*:int*/, y/*:int*/, color/*:uint*/) {
    	var pixelData;

    	if (typeof color == 'undefined') color = 0;
    	if (typeof x == 'undefined') x = 0;
    	if (typeof y == 'undefined') y = 0;

		// Not all browsers implement createImageData. On such browsers we obtain the
		// ImageData object using the getImageData method. The worst-case scenario is
		// to create an object *similar* to the ImageData object and hope for the best
		// luck.
		if (this.context.createImageData) {
			pixelData = this.context.createImageData(1, 1);
		} else if (this.context.getImageData) {
			pixelData = this.context.getImageData(x, y, 1, 1);
		} else {
			pixelData = {'width' : 1, 'height' : 1, 'data' : new Array(4)};
		}

		pixelData.data[0] = (color >> 16) & 0xFF //R
		pixelData.data[1] = (color >> 8) & 0xFF //G
		pixelData.data[2] = (color) & 0xFF //B
		pixelData.data[3] = (color >> 24) & 0xFF //A

		// Draw the ImageData object.
		this.context.putImageData(pixelData, x, y);
	}
});