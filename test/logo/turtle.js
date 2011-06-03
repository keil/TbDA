//
// Turtle Graphics in Javascript
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

/*global CanvasTextFunctions */

//----------------------------------------------------------------------
function CanvasTurtle(element, width, height)
//----------------------------------------------------------------------
{
    function deg2rad(d) { return d / 180 * Math.PI; }
    function rad2deg(r) { return r * 180 / Math.PI; }

    var self = this;
    self.display = element;
    self.backbuffer = element.cloneNode(true);
    self.context = null;

    self.x = width / 2;
    self.y = height / 2;
    self.r = Math.PI / 2;
    self.down = true;
    self.color = '#000000';
    self.fontsize = 14;
    self.width = 1;
    self.mode = 'paint';
    self.visible = true;

    /*private*/function moveto(x, y) {
        if (self.down) {
            self.context.strokeStyle = self.color;
            self.context.lineWidth = self.width;
            self.context.globalCompositeOperation =
                (self.mode === 'erase') ? 'destination-out' :
                (self.mode === 'reverse') ? 'xor' :
                'source-over';
            self.context.beginPath();
            self.context.moveTo(self.x, self.y);
            self.context.lineTo(x, y);
            self.context.stroke();
        }

        self.x = x;
        self.y = y;
    }

    this.move = function(distance) {
        var x = self.x + distance * Math.cos(self.r);
        var y = self.y - distance * Math.sin(self.r);
        moveto(x, y);
    };

    this.turn = function(angle) {
        self.r -= deg2rad(angle);
    };

    this.penup = function() { self.down = false; };
    this.pendown = function() { self.down = true; };

    this.setpenmode = function(mode) { this.mode = mode; };
    this.getpenmode = function() { return this.mode; };

    this.ispendown = function() { return self.down; };

    var STANDARD_COLORS = {
        0: "black",     1: "blue",      2: "lime",     3: "cyan",
        4: "red",       5: "magenta",   6: "yellow",   7: "white",
        8: "brown",     9: "tan",      10: "green",   11: "aquamarine",
        12: "salmon",  13: "purple",   14: "orange",  15: "gray"
    };

    this.setcolor = function(color) {
        if (STANDARD_COLORS[color] !== undefined) {
            self.color = STANDARD_COLORS[color];
        }
        else {
            self.color = color;
        }
    };
    this.getcolor = function() { return self.color; };

    this.setwidth = function(width) { self.width = width; };
    this.getwidth = function() { return self.width; };
    
    this.setfontsize = function(size) { self.fontsize = size; };
    this.getfontsize = function() { return self.fontsize; };

    this.setposition = function(x, y) {
        x = (x === undefined) ? self.x : x + (width / 2);
        y = (y === undefined) ? self.y : -y + (height / 2);

        moveto(x, y);
    };

    this.towards = function(x, y) {
        x = x + (width / 2);
        y = -y + (height / 2);

        return 90 - rad2deg(Math.atan2(self.y - y, x - self.x));
    };

    this.setheading = function(angle) {
        self.r = deg2rad(90 - angle);
    };

    this.clearscreen = function() {
        self.home();
        self.clear();
    };

    this.clear = function() {
        self.context.clearRect(0, 0, width, height);
    };

    this.home = function() {
        moveto(width / 2, height / 2);
        self.r = deg2rad(90);
    };

    this.showturtle = function() {
        self.visible = true;
    };

    this.hideturtle = function() {
        self.visible = false;
    };

    this.isturtlevisible = function() {
        return self.visible;
    };

    this.getheading = function() {
        return 90 - rad2deg(self.r);
    };

    this.getxy = function() {
        return [self.x - (width / 2), -self.y + (height / 2)];
    };

    this.drawtext = function(text) {
        if (self.context.fillText) {
            self.context.font = self.fontsize + 'px sans-serif';
            self.context.fillStyle = self.color;
            self.context.strokeStyle = self.color;
            self.context.fillText(text, self.x, self.y);
        }
    };

    /*private*/function drawturtle() {
        if (self.visible) {
            self.context.strokeStyle = "green";
            self.context.lineWidth = 2;
            self.context.beginPath();
            self.context.moveTo(self.x + Math.cos(self.r) * 20, self.y - Math.sin(self.r) * 20);
            self.context.lineTo(self.x + Math.cos(self.r - Math.PI * 2 / 3) * 10, self.y - Math.sin(self.r - Math.PI * 2 / 3) * 10);
            self.context.lineTo(self.x + Math.cos(self.r + Math.PI * 2 / 3) * 10, self.y - Math.sin(self.r + Math.PI * 2 / 3) * 10);
            self.context.lineTo(self.x + Math.cos(self.r) * 20, self.y - Math.sin(self.r) * 20);
            self.context.stroke();
        }
    }

    //----------------------------------------------------------------------
    this.begin = function() {
        //----------------------------------------------------------------------
        // Render to a backbuffer
        self.context = self.backbuffer.getContext("2d");

        self.context.lineCap = 'round';

        // Monkey patch in Hershey Font-based text from
        // c/o http://jim.studt.net/canvastext/
        if (!self.context.fillText && CanvasTextFunctions) {
            CanvasTextFunctions.enable(self.context);
            self.context.strokeText = function(string, x, y) {
                var size_font = this.font.split(/ /);
                var size = parseFloat(size_font[0]);
                var font = size_font[1];

                if (this.textAlign === "right" || this.textAlign === "end") {
                    this.drawTextRight(font, size, x, y, string);
                }
                else if (this.textAlign === "center") {
                    this.drawTextCenter(font, size, x, y, string);
                }
                else {
                    this.drawText(font, size, x, y, string);
                }
            };
            self.context.fillText = function(string, x, y) {
                var oldStroke = this.strokeStyle;
                this.strokeStyle = this.fillStyle;
                this.strokeText(string, x, y);
                this.strokeStyle = oldStroke;
            };
            self.context.measureText = function(string) {
                var size_font = this.font.split(/ /);
                var size = parseFloat(size_font[0]);
                var font = size_font[1];
                return CanvasTextFunctions.measure(font, size, string);
            };
        }
    };

    //----------------------------------------------------------------------
    this.end = function() {
        //----------------------------------------------------------------------
        // Flip backbuffer into primary
        var newDisplay = self.backbuffer.cloneNode(true);
        self.context = newDisplay.getContext("2d");
        if (self.context.drawImage) { self.context.drawImage(self.backbuffer, 0, 0); } // Clone itself doesn't copy image data
        drawturtle();

        self.display.parentNode.replaceChild(newDisplay, self.display);
        self.display = newDisplay;

        // Guard against rogue drawing
        self.context = null;
    };

    self.begin();
    self.end();

} // CanvasTurtle
