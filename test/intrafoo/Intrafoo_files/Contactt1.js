namespace('com.trifork.intrafoo.view');

using(com.trifork.intrafoo.view).run(function(view){
	
	view.ContactsGridAndDetailPanel = Ext.extend(Ext.Panel, {
		// override initComponent
		initComponent: function() {
			// used applyIf rather than apply so user could
			// override the defaults
			Ext.applyIf(this, {
				frame: true,
				title: 'Contacts',
				width: "100%",
				height: 400,
				layout: 'border',
				items: [{
					xtype: 'contactsgrid',
					itemId: 'gridPanel',
					region: 'north',
					height: 210,
					split: true
				}, {
					xtype: 'contacttab',
                    deferredRender: false,
					itemId: 'detailPanel',
                    id: 'detailPanel',
					region: 'center'
				}]
            });
			// call the superclass's initComponent implementation
			view.ContactsGridAndDetailPanel.superclass.initComponent.call(this);
		},
		// override initEvents
		initEvents: function() {
			// call the superclass's initEvents implementation
			view.ContactsGridAndDetailPanel.superclass.initEvents.call(this);
	
			// now add application specific events
			// notice we use the selectionmodel's rowselect event rather
			// than a click event from the grid to provide key navigation
			// as well as mouse navigation
			var bookGridSm = this.getComponent('gridPanel').getSelectionModel();
			bookGridSm.on('rowselect', this.onRowSelect, this);
		},
		// add a method called onRowSelect
		// This matches the method signature as defined by the 'rowselect'
		// event defined in Ext.grid.RowSelectionModel
		onRowSelect: function(sm, rowIdx, r) {
			// getComponent will retrieve itemId's or id's. Note that itemId's
			// are scoped locally to this instance of a component to avoid
			// conflicts with the ComponentMgr
			var detailPanel = this.getComponent('detailPanel'),
			compPanel = detailPanel.getComponent('companytab');
            
            detailPanel.getComponent('contacttab').updateContact(r.data);
            compPanel.updateCompany(r.data.Company);
		}
	});
	
	Ext.reg('contactsmasteranddetail', view.ContactsGridAndDetailPanel);
});

