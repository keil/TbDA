namespace('com.trifork.intrafoo.view');

using(com.trifork.intrafoo).run(function(ifoo){
	var view = ifoo.view,
		c = ifoo.controller;
	view.SearchForm = Ext.extend(Ext.form.FormPanel, {
		constructor: function(config){
            config = config || {};
			Ext.applyIf(config, {
		        baseCls: 'x-plain',
                title: "Search",
		        labelWidth: 75,
		        defaultType: 'textfield',
		        items: [{
		            id: "searchCombo",
                    xtype: 'combo',
                    forceSelection  : true,
                    fieldLabel: 'Søge felt',
                    minChars: 1,
                    name: 'search_field',
                    anchor:'100%',  // anchor width by percentage
                    store: c.Controller.getSearchFieldStore(),
                    displayField:'shownName',
                    valueField:'name',
                    mode: 'local',
                    triggerAction: 'all',
                    emptyText:'Vælg et søge felt...'
                }, {
                    id: 'search_criteria',
                    fieldLabel: 'Søge kriterie',
                    name: 'search_criteria',
                    anchor: '100%',  // anchor width by percentage
                    emptyText:'Skriv søge kriterie...'
                },{
                    id: 'search_operator',
                    xtype: 'radiogroup',
                    fieldLabel: 'Operator',
                    items: [{
                            boxLabel: 'Must', name: 'operator', checked: true,
                            inputValue: "MUST"
                        }, {
                            boxLabel: 'May', name: 'operator',
                            inputValue: "MAY"
                        }, {
                            boxLabel: 'Not', name: 'operator',
                            inputValue: "NOT"
                        }
                    
                    ]
                    
                }],
                 buttons: [{
                    id: 'search_add',
                    xtype: 'button',
                    text:"Add",
                    handler: function(b,e) {
                        config.doAction.execute(e);
                    }
                
		        }]

		    });
			view.SearchForm.superclass.constructor.call(this, config);
		}
	});   
});