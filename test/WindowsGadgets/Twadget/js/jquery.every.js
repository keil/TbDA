//source: http://www.mail-archive.com/discuss@jquery.com/msg05594.html
jQuery.fn.extend({
    every: function(interval,id,fn) {
        return jQuery.timer.add.apply(this,[false].concat(jQuery.map(arguments,"a")));
    },
    doin: function(interval,id,fn) {
        return jQuery.timer.add.apply(this,[true].concat(jQuery.map(arguments,"a")));
    },
    stop: function(id) {
        return jQuery.timer.remove.apply(this,[id]);
    }
});

jQuery.extend({
    timer: {
        add: function(oneOff,interval,id,fn) {
            var args = jQuery.map(arguments,"a"), slice = 4;
            return this.each(function() {
                var self = this, counter = 0;
                interval = jQuery.speed(interval)['duration'];
                if (fn == undefined || id.constructor == Function) {
                    fn = id;
                    id = interval;
                    slice = 3;
                }
                if (!self.timers) self.timers = {};
                if (!self.timers[id]) self.timers[id] = [];
                self.timers[id].push(window.setInterval(function() {
                    if (oneOff && counter++ >= 1) return jQuery(self).stop(id);
                    fn.apply(self,args.slice(slice));
                },interval));
            });
        },
        remove: function(id) {
            return this.each(function() {
                if (!this.timers) return;
                jQuery.each(id == undefined ? this.timers : [this.timers[id]], function(i) {
                    jQuery.each(this,function(j) {
                        window.clearInterval(this);
                    });
                });
            });
        }
    }
});

