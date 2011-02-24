/*
 * jQuery jcountdown
 *
 * Copyright (c) 2011 Marek Nowicki <http://my.opera.com/faramir2>
 */
(function($) {
    $.fn.jcountdown = function(options) {
        var version = '1.0.0';

        // options
        var opts = $.extend({}, $.fn.jcountdown.defaults, options);

        return this.each(function() {
            $this = $(this);
            $this.timerID = null;
            $this.running = false;

            var o = $.meta ? $.extend({}, opts, $this.data()) : opts;

            $this.utc = o.utc;
            $this.utc_offset = o.utc_offset;
            $this.path = o.path;

            $this.css({
                fontFamily: o.fontFamily,
                fontSize: o.fontSize,
                backgroundColor: o.background,
                color: o.foreground
            });

            $this.pobrane = false;
            $this.roznica = 0;

            var end = new Date(parseInt(o.endDate));
            if (isNaN(end)) end = new Date(o.endDate);
            if (isNaN(end)) end = new Date();
            
            $this.endDate = end;
            $this.endString = o.endString;

            $.fn.jcountdown.startClock($this);

        });
    };

    $.fn.jcountdown.startClock = function(el) {
        $.fn.jcountdown.stopClock(el);
        el.pobrane = false;
        el.roznica = 0;
        el.stop = false;
        sendRequest("get", el.path+"/Time",
            function(el) {
                return function(http) {
                    if(http.readyState == 4 && http.status == 200){
                        el.roznica = new Date().getTime() - Number(http.responseText);
                        el.pobrane = true;
                        $.fn.jcountdown.displayTime(el);
                    }
                };
            }(el)
            );

    }

    $.fn.jcountdown.stopClock = function(el) {
        if(el.running) {
            clearTimeout(el.timerID);
        }
        el.running = false;
    }

    $.fn.jcountdown.displayTime = function(el) {
        var time = $.fn.jcountdown.getTime(el);
        el.html(time);
        if (el.stop==false) {
            el.timerID = setTimeout(function(){
                $.fn.jcountdown.displayTime(el)
            },1000);
        }
    }

    $.fn.jcountdown.getTime = function(el) {
        now = new Date(new Date().getTime() - el.roznica);

        if(el.utc == true) {
            var localTime = now.getTime();
            var localOffset = now.getTimezoneOffset() * 60000;
            var utc = localTime + localOffset;
            var utcTime = utc + (3600000 * el.utc_offset);

            now = new Date(utcTime);
        }

        var timeNow = "";
        diff = Math.floor((el.endDate.getTime() - now.getTime())/1000);
        if (diff<0) {
            timeNow = el.endString;
            el.stop = true;
        } else {
            sec = diff%60;
            diff = Math.floor(diff/60);
            min = diff%60;
            diff = Math.floor(diff/60);
            hour = diff%24;
            diff = Math.floor(diff/24);
            days = diff;
            if (days>0) timeNow = days+'d ';
            timeNow = timeNow + hour+':'+(min<10?'0':'')+min+':'+(sec<10?'0':'')+sec;
        }
        return timeNow;
    };

    $.fn.jcountdown.defaults = {
        utc_offset: 0,
        utc: false,
        fontFamily: '',
        fontSize: '',
        foreground: '',
        background: '',
        path: ''
    };

})(jQuery);
