namespace('com.trifork.intrafoo.controller');

using(com.trifork.intrafoo).run(function(ifoo){
	var controller = ifoo.controller,
		model = ifoo.model;
	
	controller.SearchCriteriaStore =  Ext.extend(Ext.data.ArrayStore, {
		constructor: function(config) {
			config = config || {};
			Ext.applyIf(config, {
				autoDestroy: true,
				idIndex: 0,  
				reader: new Ext.data.ArrayReader({
					idIndex: 0
				}, 
				model.SearchCriteria)
			});
			controller.SearchCriteriaStore.superclass.constructor.call(this, config);
		}
	});
});