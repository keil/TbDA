namespace('com.trifork.intrafoo.view');

using(com.trifork.intrafoo).run(function(ifoo){
	var view = ifoo.view,
		controller = ifoo.controller,
		TAG_URL = '/intrafoo/tag',
		win = null,
		action = null,
		titles = {
			'add': "Add tags",
			'remove': "Remove tags"
		};
		
	function doAction() {
        var fs = Ext.getCmp("tagForm");
        fs.getForm().submit({
            waitMsg:'Waiting for intratools...',
            clientValidation: false,
            params: {
                contactids: action.contactids,
                action: action.action
            },
            url:TAG_URL,
            success: function(form, action) {
		       win.hide();
		       Ext.getCmp("searchCombo").focus();
               //controller.Controller.getContactStore().reload();
		    },
		    failure: function(form, action) {
		       Ext.Msg.alert("Error occurred on server...","Error: ..", function(){
			       win.hide();
			       Ext.getCmp("searchCombo").focus();
		       }); 
               //controller.Controller.getContactStore().reload();
		    }
        });
    }
	  
	view.TagContacts = {
		show: function(act) {
			if (win === null) {
				win = this.createTagContactsWindow(titles[act.action]);
			} 
			action = act;
            win.setTitle(titles[act.action]);
            win.show(this);
		},
		
		createTagContactsWindow: function(tit) {
			return new Ext.Window({
	            layout:'fit',
	            width:400,
	            height:200,
	            title: tit,
	            closeAction:'hide',
	            defaultButton: 'taginterest',
	            plain: true,
	            items: new Ext.FormPanel({
                    id: 'tagForm',
                    labelWidth: 75, // label settings here cascade unless overridden
	                frame:true,
	                title: false,
	                bodyStyle:'padding:5px 5px 0',
	                width: 350,
	                defaults: {width: 230},
	                defaultType: 'textfield',
					keys: [{
					    key: Ext.EventObject.ENTER,
					    fn: doAction
					},{
					    key: Ext.EventObject.ESC,
                        fn: function() {
                            win.hide();
                            Ext.getCmp("searchCombo").focus();
                        }
					}],
					
	                items: [{
                        id: 'taginterest',
                        xtype:"combo",
                        fieldLabel: 'Interest',
                        name: 'interest',
				        displayField:'name',
				        typeAhead: true,
				        mode: 'local',
				        forceSelection: true,
				        triggerAction: 'all',
				        emptyText:'Select an interest...',
				        selectOnFocus:true,
                        store: controller.Controller.getInterestStore()
                    },{
                        id: 'tagactivity',
                        fieldLabel: 'Activity',
                        name: 'activity',
                        xtype:"combo",
                        displayField:'name',
                        typeAhead: true,
                        mode: 'local',
                        forceSelection: true,
                        triggerAction: 'all',
                        emptyText:'Select an activity...',
                        selectOnFocus:true,
                        store: controller.Controller.getActivityStore()
                    },{
                        id: 'tagcampagne',
                        fieldLabel: 'Campagne',
                        name: 'campagne',
                        xtype:"combo",
                        displayField:'name',
                        typeAhead: true,
                        mode: 'local',
                        forceSelection: true,
                        triggerAction: 'all',
                        emptyText:'Select an campagne...',
                        selectOnFocus:true,
                        store: controller.Controller.getCampagneStore()
                    }]
	            }),
		
	            buttons: [{
	                text:'OK',
	                handler: doAction
	            },{
		             text: 'Fortryd',
		             handler: function(){ 
		                 win.hide();
                         Ext.getCmp("searchCombo").focus();
		             }
		        }]
		        /*,
		        bbar: new Ext.ux.StatusBar({
		            id: 'basic-statusbar',
		            defaultText: '',
		            text: 'Please log in'
		        })*/
			});
		}
	};
});