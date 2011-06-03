(function($){
	
 $.fn.renderItems = function(doc, options) {
	var opts = $.extend({ 
						items_sel: '.vevent', 
						tpl: $.template('<strong>NO TEMPLATE</strong>'), 
						inject: 'prepend',
						preloader_sel: '.feed_loader',
						sort: 'asc'
						}, options);

	// Remove old items
	$(this).find(opts.items_sel).each(function() { $(this).remove(); });
	
	
	// Create new items
	var container = $(this);
	
	// Remove feed loaders 
	container.find(opts.preloader_sel).each(function() {
		$(this).remove();
	});

	$.each($.feedParser(doc).parse({sort: opts['sort']}).items, function(index, item) {
		if(opts.max && opts.max <= index) {
			return;
		}
		switch(opts.inject) {
			case 'after':
				container.find(opts.inject_sel).after(opts.tpl, item);
				break;				
			case 'prepend':
			default:		
				container.append(opts.tpl, item);
				break;
		}
	});
	return $(this);
 }
	
 $.fn.renderAgenda = function(doc, options) {
	var tpl = $.template('<a href="http://www.tudelft.nl${permalink}" class="vevent small_agenda_items">\
		<span class="divided">\
			<strong class="summary"><span>${category}</span> ${title}</strong>\
			<em class="dtstart">${displayDate} <span>${displayTime} uur</span></em>\
		</span>\
	</a>');
	return $(this).renderItems(doc, $.extend({ items_sel: '.vevent', tpl: tpl}, options));	
 };

 $.fn.renderNews = function(doc, options) {
	var tpl = $.template('<div class="box vevent news_item">\
		<img src="http://www.tudelft.nl${permalink}&binary=/img/foto.jpg" width="66" height="64" alt="image description" />\
		<h3 class="summary"><a href="http://www.tudelft.nl${permalink}">${fullTitle}</a></h3>\
		<em class="date dtstart">${displayDate} ${displayTime}</em>\
		<a href="http://www.tudelft.nl${permalink}" class="link">Meer info</a>\
	</div>');

	return $(this).renderItems(doc, $.extend({ items_sel: '.box.vevent', tpl: tpl, inject: 'after', inject_sel: '.box.intro', sort: 'desk'}, options));	
 };

<!-- fullnews toegevoegd / kopie van bovenstaande met andere naam--> 

$.fn.renderFullnews = function(doc, options) {
	var tpl = $.template('<div class="box vevent news_item">\
		<img src="http://www.tudelft.nl${permalink}&binary=/img/foto.jpg" width="66" height="64" alt="image description" />\
		<h3 class="summary"><a href="http://www.tudelft.nl${permalink}">${fullTitle}</a></h3>\
		<em class="date dtstart">${displayDate} ${displayTime}</em>\
		<a href="http://www.tudelft.nl${permalink}" class="link">Meer info</a>\
	</div>');
	return $(this).renderItems(doc, $.extend({ items_sel: '.box.vevent', tpl: tpl, inject: 'after', inject_sel: '.box.intro', sort: 'desc'}, options));	
 };

<!-- EINDE fullnews toegevoegd / kopie van bovenstaande met andere naam--> 
 
 $.fn.renderAnnouncements = function(doc, options) {
	var tpl = $.template('<div class="box vevent">\
		<h3 class="summary"><a href="http://www.tudelft.nl${permalink}">${fullTitle}</a></h3>\
		<em class="date dtstart">Geplaatst op ${displayPubDate}</em>\
		<a href="http://www.tudelft.nl${permalink}" class="link">Lees verder</a>\
	</div>');
	return $(this).renderItems(doc, $.extend({ items_sel: '.box.vevent', tpl: tpl, sort: 'desc' }, options));	
 };

 $.fn.renderErIsNogPlaats = function(doc, options) {

	var tpl = $.template('<div class="box vevent">\
		<h3 class="summary"><a href="http://www.tudelft.nl${permalink}">${fullTitle}</a></h3>\
		<p class="description">${bodyText} ${displayPrice}</p>\
		<em class="date"><span class="dtstart">${displayDate}, <span>${displayTime}</span></span></em>\
		<a href="https://sportsandculture.tudelft.nl/signin.php?act=login" class="link">Schrijf je nu in</a>\
	</div>');

	var retval = $(this).renderItems(doc, $.extend({ items_sel: '.box.vevent', tpl: tpl, inject: 'after', inject_sel: '.box:first'}, options));	
	return retval;
 };
})(jQuery);

/* Cheat sheet
{
	fullTitle: title,
	category: category,
	title: display_title,
	startDate: startDate,
	displayDate: meta_parts[0],
	displayTime: meta_parts[1],
	displayPrice: price,
	bodyText: body
}
*/