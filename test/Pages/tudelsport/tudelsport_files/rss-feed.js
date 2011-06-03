$(document).ready(function() {
	
	/**
	 * NOTE @david Change urls if need here
	 */
	var feed_urls = {
		agenda: "/feeds/mapper.php?feed_id=b8192a4a-41ee-4d28-917d-0aba7c459895&lang=nl",
		news: "/feeds/mapper.php?feed_id=4a706e39-e846-43da-9808-cfbcc589423b&lang=nl",
		fullnews: "/feeds/mapper.php?feed_id=4a706e39-e846-43da-9808-cfbcc589423b&lang=nl",
		announcements: "/feeds/mapper.php?feed_id=05a8ab59-e78f-4ba0-98d4-7ec2f82bc0c1&lang=nl",
		er_is_nog_plaats: "/feeds/mapper.php?feed_id=43db2224-1aed-428d-85cb-040dfa6ca962&lang=nl"
	};
	
	// Agenda
	$.get(feed_urls.agenda+'&ticker='+(Math.floor(Math.random()*10000)+100), function(rss){
		$('.agenda .box').renderAgenda(rss, { max: 4 });
	});
	
	// News
	$.get(feed_urls.news+'&ticker='+(Math.floor(Math.random()*10000)+100), function(rss){
		$('.promo').renderNews(rss, { max: 4 });
	});
		// FullNews
	$.get(feed_urls.fullnews+'&ticker='+(Math.floor(Math.random()*10000)+100), function(rss){
		$('#fullnews').renderFullnews(rss, { max: 100 });
	});
	
	// Announcements
	$.get(feed_urls.announcements+'&ticker='+(Math.floor(Math.random()*10000)+100), function(rss){
		$('#mededelingen').renderAnnouncements(rss, { max: 4 });
	});
	
	// ErIsNogPlaats
	$.get(feed_urls.er_is_nog_plaats+'&ticker='+(Math.floor(Math.random()*10000)+100), function(rss){
		$('#nogplaats').renderErIsNogPlaats(rss, { max: 4 });
	});
})