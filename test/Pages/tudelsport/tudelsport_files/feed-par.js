(function($) {
	$.feedParser = function(doc) {
		return new $.feedParser.instance(doc);
	};
	
	$.feedParser.instance = function(doc, options) {
		this.doc = doc;
        // If a custom regular expression has been set, grab it from the regx object
	};
	
	$.extend($.feedParser.instance.prototype, {
		
		parse: function(options) {
			return { items: $(this.parseItems(options)) };
		},
		
		parseItems: function(options) {
			var opts = $.extend({ sort: 'asc' }, options)
			var parsed_items = $.map($(this.doc).find('item'), function(itm, index) {
				
				// Get date title data
				var title = $(itm).find("title").text();
				var title_parts = $.map(title.split(':'), function(str, index) {
					return $.trim(str);
				});
				
				var category, display_title;
				if(title_parts.length == 2) {
					category = title_parts[0];
					display_title = title_parts[1];
				} else {
					category = "";
					display_title = title_parts[0];
				}
				
				// Get the pubdate
				var pubDateString = $(itm).find("pubDate").text()
				var pubDate = new Date(pubDateString);
				
				if($(itm).find("startDatum").length > 0) {
					// Get start date data
					var startDateComponents = $(itm).find("startDatum").text().split('-');
					var startDate = new Date();
					startDate.setFullYear(startDateComponents[0], parseFloat(startDateComponents[1])-1, parseFloat(startDateComponents[2]));					
				} else {
					startDate = pubDate;
				}
	
				
				// Set datums
				var displayPubDate = $.format.date(pubDate, "dd-MM-yyyy, HH:mm");
				
				// Get permalink
				permalink = '/live/' + $(itm).find('link').text();

				// permalink = $(itm).find("link:first").text()
				// for(e in $(itm).find("link")) {
				// 	permalink += (e + " ")
				// }
				// var permalink = 
				
				// first line of the desciption
				var first_line = $(itm).find('description').text().split("\n")[0];
				var meta_parts = $.map(first_line.split("|"), function(str, ind) {
					return $.trim(str);
				});
				// Get the elements
				var all_items = $(itm).find('description').text().split('|')
				// get the body tekst
				var body = all_items[all_items.length - 2];
					
				var price;
				if(meta_parts.length == 4) {
					var price = meta_parts[2];
					// var body = meta_parts[3]
				} else {
					var price = "";
					// var body = meta_parts[2]
				}
				
				return {
					pubDate: pubDate,
					pubDateString: pubDateString,
					displayPubDate: displayPubDate,
					fullTitle: title,
					category: category,
					title: display_title,
					startDate: startDate,
					displayDate: meta_parts[0],
					displayTime: meta_parts[1],
					displayPrice: price,
					bodyText: body,
					permalink: permalink
				};
			});
			
			
			if(opts['sort'] == 'desc') {
				parsed_items.sort(function(a,b) {
					var retval = 0;
					if(a.startDate.getTime() < b.startDate.getTime()) {
						retval = 1;
					} else if(a.startDate.getTime() > b.startDate.getTime()) {
						retval = -1;
					}
					return retval;
				});				
			} else {
				parsed_items.sort(function(a,b) {
					var retval = 0;
					if(a.startDate.getTime() > b.startDate.getTime()) {
						retval = 1;
					} else if(a.startDate.getTime() < b.startDate.getTime()) {
						retval = -1;
					}
					return retval;
				});
			}
			
			return parsed_items;
		}	
	});
})(jQuery);