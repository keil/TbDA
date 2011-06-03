namespace('com.trifork.intrafoo.controller');

using(com.trifork.intrafoo).run(function(ifoo){
	var controller = ifoo.controller,
		model = ifoo.model;
	
	controller.SearchFieldStore = Ext.extend( Ext.data.ArrayStore, {
		constructor: function(config) {
			config = config || {};
			Ext.applyIf(config, {
				proxy: new Ext.data.MemoryProxy([["occupation", "Occupation", null],["fax", "Fax", null],["campagnes", "Campagnes", null],
				                                 ["zip_code", "Zip Code", null],["interests", "Interests", null],
				                                 ["private_phone", "Private Phone", null],
				                                 ["country", "Country", null],["city", "City", null],["username", "Username", null],
				                                 ["mobile_phone", "Mobile Phone", null],["address","Address", null],
				                                 ["name", "Name", null],["company", "Company", null],
				                                 ["izip_code", "Zip Code (Range)", "int"],["activities", "Activities", null],
				                                 ["comment", "Comment", null],["email_address", "Email", null],
				                                 ["office_phone", "Office Phone", null]]),
				autoDestroy: true,
				idIndex: 0,  
				fields: [ 'name', 'shownName', 'type'],
				sortInfo: {
					field: 'shownName',
					direction: 'ASC' // or 'DESC' (case sensitive for local sorting)
				}
			});
			controller.SearchFieldStore.superclass.constructor.call(this, config);
			this.load();
		}
	});

});