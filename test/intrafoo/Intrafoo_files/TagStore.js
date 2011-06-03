namespace('com.trifork.intrafoo.controller');

using(com.trifork.intrafoo).run(function(ifoo){
	var controller = ifoo.controller,
		model = ifoo.model;
	
    
    controller.TagStore = function(config) {
        config = config || {};
        Ext.applyIf(config, {
            //TODO remoteSort: true,
            proxy : new Ext.data.MemoryProxy(config.data),
            reader: Ext.apply(new Ext.data.JsonReader({ 
				                root: config.dataProperty
				            }, model.Tag),{
							    extractValues : function(data, items, len) {
							        return {name: data};
							    }
				            })
        });
        controller.TagStore.superclass.constructor.call(this, config);
    };
    
    Ext.extend(controller.TagStore, Ext.data.Store);
});
