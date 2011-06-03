namespace('com.trifork.intrafoo.view');

using(com.trifork.intrafoo).run(function(ifoo){
	var view = ifoo.view,
		controller = ifoo.controller;
		
	view.GroupedContactsGrid = Ext.extend(Ext.grid.EditorGridPanel, {
		// override
		initComponent : function() {

			Ext.apply(this, {	
		        columns: [
			              {header: 'Name', width: 150, sortable: true,  dataIndex: 'Name'},
		                  {header: 'Country', width: 100, sortable: true,  dataIndex: 'Country'},
		                  {header: 'Zip code', width: 80, sortable: true,  dataIndex: 'Zip code'},
		                  {header: 'Email address', width: 200, hidden: true, sortable: true,  dataIndex: 'Email address'},
		                  {header: 'Interests', width: 200, sortable: true, dataIndex: 'Interests'},
		                  {header: 'Activities', width: 200, sortable: true,  dataIndex: 'Activities'},
		                  {header: 'Campagnes', width: 200, sortable: true,  dataIndex: 'Campagnes'}
		            ],
				// Note the use of a storeId, this will register thisStore
				// with the StoreMgr and allow us to retrieve it very easily.
				store: controller.Controller.getContactGroupingStore(),
				// force the grid to fit the space which is available
				viewConfig: {
					forceFit: true
				},
				loadMask: true
			});
			// finally call the superclasses implementation
			view.GroupedContactsGrid.superclass.initComponent.call(this);
		}
	});
	Ext.reg('groupedcontactsgrid', view.GroupedContactsGrid);
});
