namespace('com.trifork.intrafoo.view');

using(com.trifork.intrafoo).run(function(ifoo){
	var view = ifoo.view,
		controller = ifoo.controller;
		
	/**
	 * helper sorting function:
	 * sort arrays of tags: interests, activities, campagnes
	 * so that those tags that contain the search criteria are listed first
	 * @param {Array} s tags to sort
	 * @param {String} fieldname the field-name for search criteria to use for sorting
	 * @param {String} separator to use when returning string
	 */
	function sortAndJoin(s, fieldname, separator) {
		  var criteria = controller.Controller.getSearchCriteriaStore(),
	          index = criteria.find("field",fieldname),
	          value;
		  if (index >= 0) {
			value = criteria.getAt(index).get("criteria");
			value = value.trim().replace(/[\*]/g,"");
            s.sort(function(a,b){
			   a = a.toLowerCase();
			   b = b.toLowerCase();
               var ia = a.indexOf(value),
                   ib = b.indexOf(value);
			   if (ia === ib) {
                   return a < b ? -1 : a === b ? 0 : 1;
			   }
			   if (ia > -1) {
                   return -1;
			   }
			   if (ib > -1) {
                   return 1;
			   }
               return ia - ib;
            });
		  }
		  return s.join(separator);
	}
	
	view.ContactsGrid = Ext.extend(Ext.grid.EditorGridPanel, {
		// override
		initComponent : function() {
			var sm = new Ext.grid.CheckboxSelectionModel({
		        listeners: {
		            // On selection change, set enabled state of the removeButton
		            // which was placed into the GridPanel using the ref config
		            selectionchange: function(sm) {
						if (sm.getCount()) {
                            Ext.getCmp('sendmail').enable();
                            Ext.getCmp('tagcontacts').enable();
                            Ext.getCmp('untagcontacts').enable();
                            Ext.getCmp('refreshcontacts').enable();
		                } else {
                            Ext.getCmp('sendmail').disable();
                            Ext.getCmp('tagcontacts').disable();
                            Ext.getCmp('untagcontacts').disable();
                            Ext.getCmp('refreshcontacts').disable();
		                }
					}
		        }
		    });

			Ext.apply(this, {	
				
		        columns: [
		                  sm,
		               
		              {header: 'Name', width: 150, sortable: true,  dataIndex: 'Name'},
		              {header: 'Company', width: 150, sortable: true,  dataIndex: 'CompanyName',
                          renderer: function(idx,row,data){
                              var company = data.get('Company');
                              if (company) {
                                  return company["Company name"] || "";  
                              }
                              return "";
                          }
                      },
	                  {header: 'Occupation', width: 150, sortable: true,  dataIndex: 'Occupation'},
	                  {header: 'Mobile phone', width: 150, sortable: true,  dataIndex: 'Mobile phone'},
	                  {header: 'Office phone', width: 150, sortable: true,  dataIndex: 'Office phone'},
	                  {header: 'Fax', width: 150, hidden: true, sortable: true,  dataIndex: 'Fax'},
	                  {header: 'Country', width: 100,  hidden: true, sortable: true,  dataIndex: 'Country'},
	                  {header: 'City', width: 150, sortable: true,  dataIndex: 'City'},
	                  {header: 'Address', width: 150, sortable: true, dataIndex: 'Address', renderer: function(idx,row,contact){
                              var s = contact.get("Address").join(", ");
                              if (s.trim() === ",") {
                                  return "";
                              }
                              return s;
                              }
                      },
	                  {header: 'Zip code', width: 80, sortable: true,  dataIndex: 'Zip code'},
	                  {header: 'Email address', width: 200, hidden: true, sortable: true,  dataIndex: 'Email address'},
	                  {header: 'Username', width: 100, hidden: true, sortable: true,  dataIndex: 'Username'},
	                  {header: 'Interests', width: 200, sortable: true, dataIndex: 'Interests',
                        renderer: function(idx,row,contact){
                             var s = contact.get("Interests");
                             return sortAndJoin(s, "interests", "; ");
                        },
                        editor: new Ext.form.TextField({
                            readOnly: true,
                            allowBlank: false
                        })
                      },
	                  {header: 'Activities', width: 200, sortable: true,  dataIndex: 'Activities',
                        renderer: function(idx,row,contact){
                            var s = contact.get("Activities");
                            return sortAndJoin(s, "activities", "; ");
                        },
                        editor: new Ext.form.TextField({
                            readOnly: true,
                            allowBlank: false
                        })
	                  },
	                  {header: 'Campagnes', width: 200, sortable: true,  dataIndex: 'Campagnes',
                        renderer: function(idx,row,contact){
                            var s = contact.get("Campagnes");
                            return sortAndJoin(s, "campagnes", "; ");
                        },
                        editor: new Ext.form.TextField({
                            readOnly: true,
                            allowBlank: false
                        })
	                  },
	                  {header: 'Last modified by', width: 200, hidden:true, sortable: true,  dataIndex: 'Last modified by'},
	                  {header: 'Modified date', width: 100, hidden:true, sortable: true,  dataIndex: 'Modified date'}
		         ],
				sm: sm,
				// Note the use of a storeId, this will register thisStore
				// with the StoreMgr and allow us to retrieve it very easily.
				store: controller.Controller.getContactStore(),
				// force the grid to fit the space which is available
				viewConfig: {
					forceFit: true
				},
				loadMask: true,
				bbar: new Ext.PagingToolbar({
                    id: 'contactsPaging',
		            pageSize: 25,
		            store: controller.Controller.getContactStore(),
		            displayInfo: true,
		            displayMsg: 'Displaying contacts {0} - {1} of {2}',
		            emptyMsg: "No contacts to display"
		        }),
				tbar:[{
		            text:'Send mail',
		            tooltip:'Send mail to selected recipients',
		            iconCls:'x-send-mail',
		            listeners: {
                        'click':function () {	
		                    var selections = sm.getSelections(),
		                        mails = [],
		                        i = selections.length;
		                    while (i--) {
		                        mails.push(selections[i].get('Email address'));
		                    }
		                    location.href = 'mailto:?bcc=' + mails.join(",");
		                }
		            },
		            // Place a reference in the GridPanel
		            id: 'sendmail',
		            disabled: true
		        },{
		            text:'Tag',
		            tooltip:'Tag selected contacts',
		            iconCls:'x-add-tag',
		            listeners: {
                        'click':function () {	
                            var selections = sm.getSelections(),
                                ids = [],
                                i = selections.length;
                            while (i--) {
                                ids.push(selections[i].get('ID'));
                            }
                            view.TagContacts.show({action: 'add', contactids:ids});
                        }
		            },
		            // Place a reference in the GridPanel
		            id: 'tagcontacts',
		            disabled: true
		        },{
		            text:'Un-tag',
		            tooltip:'Remove tag from selected contacts',
		            iconCls:'x-remove-tag',
		            listeners: {
                        'click':function () {	
		                    var selections = sm.getSelections(),
		                        ids = [],
		                        i = selections.length;
		                    while (i--) {
		                        ids.push(selections[i].get('ID'));
		                    }
		                    view.TagContacts.show({action: 'remove', contactids:ids});
		                }
		            },
		            // Place a reference in the GridPanel
		            id: 'untagcontacts',
		            disabled: true
		        },{
                    text:'Refresh',
                    tooltip:'Update data for selected contacts',
                    iconCls:'x-update-contacts',
                    listeners: {
                        'click':function () {   
                            var selections = sm.getSelections(),
                                id,
                                i = selections.length,
                                N = selections.length,
                                succ = function(response, opts) {
                                    N--;
                                    if (N === 0) {
                                        controller.Controller.getContactStore().reload();
                                    }
                                },
                                fail = function(response, opts) {
                                    N--;
                                    if (N === 0) {
                                        controller.Controller.getContactStore().reload();
                                        Ext.Msg.alert('Server error', 'Not all contacts were saved...');
                                    }
                                };
                            while (i--) {
                                id = selections[i].get('ID');
                                if (id) {
	                                Ext.Ajax.request({
	                                    url:'/intrafoo/contact/update/'+id+"?async=false",
	                                    method: 'PUT',
                                        success: succ,
                                        failure: fail
                                   });
                                }
                            }
                        }
                    },
                    // Place a reference in the GridPanel
                    id: 'refreshcontacts',
                    disabled: true
                }]
			});
			// finally call the superclasses implementation
			view.ContactsGrid.superclass.initComponent.call(this);
		}
	});
	Ext.reg('contactsgrid', view.ContactsGrid);
});
