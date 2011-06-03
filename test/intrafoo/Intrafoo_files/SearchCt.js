namespace('com.trifork.intrafoo.view');

using(com.trifork.intrafoo).run(function(ifoo){
	var view = ifoo.view,
		c = ifoo.controller;
	
	var tpl = new Ext.XTemplate(
			'<tpl for=".">',
	            '<div class="SearchCriteraWrap">',
                    '<div class="SearchCritera"><span>{operator}: {field} = {criteria}</span></div>',
			    '</div>',
	        '</tpl>',
	        '<div class="x-clear"></div>');
	
	view.SearchCriteriaList = Ext.extend(Ext.DataView, {
		
		constructor: function(config) {
            config = config || {};
			Ext.applyIf(config, {
		          store: c.Controller.getSearchCriteriaStore(),
		          tpl: tpl,
		          autoHeight:true,
		          multiSelect: true,
		          overClass:'x-view-over',
		          itemSelector:'.SearchCriteraWrap',
		          emptyText: 'No search criteria',
		          
		          listeners: {
		              click: function(dv, idx) {
	                      c.Controller.getSearchCriteriaStore().removeAt(idx);
			          }
                  }
            });
            view.SearchCriteriaList.superclass.constructor.call(this, config);
		}
    });
});
