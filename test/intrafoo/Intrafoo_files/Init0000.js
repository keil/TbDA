var namespace = com.trifork.common.Common.namespace,
using = com.trifork.common.Common.using;

Ext.onReady(function(){
	var ifoo = com.trifork.intrafoo,
		view = ifoo.view,
		controller = ifoo.controller,
		model = ifoo.model;

    var contactsView,
        groupedContactsView,
	    searchCriteriaList,
	    searchForm;
        
    var queryObj = [],
	    doQuery = function(){ 
	        controller.Controller.getCurrentStore().load({
	            params : {
	                query: queryObj.join(" ") 
	            },
	
	        callback : function(rs, opt, succ) {
	            controller.Controller.getCurrentStore().baseParams = {
	                query: queryObj.join(" ")
	            };
	        }
	        });
	    };
    
	// turn on validation errors beside the field globally
	Ext.form.Field.prototype.msgTarget = 'side';
	controller.Controller.init();
	controller.Controller.getContactStore().on('exception', function(dataproxy, type, action, options, response, arg){
		controller.Controller.handleAjaxError({
            store:controller.Controller.getContactStore(), 
            dataproxy:dataproxy, 
            type:type, 
            uicomponents: [groupedContactsView,
                           contactsView],
            action:action, 
            options:options, 
            response: response, 
            arg:arg
        });
	});  
	
	controller.Controller.getContactGroupingStore().on('exception', function(dataproxy, type, action, options, response, arg){
		controller.Controller.handleAjaxError({
            store:controller.Controller.getContactGroupingStore(), 
            dataproxy:dataproxy, 
            type:type, 
            uicomponents: [groupedContactsView,
                           contactsView],
            action:action, 
            options:options, 
            response: response, 
            arg:arg
        });
	}); 
	
	var addCriteriaAction = new Ext.Action({
		text: 'Add',
		handler: function(field, e){
		var search = Ext.getCmp('searchCombo'),
		field_text = Ext.getCmp('search_criteria'),
		search_op = Ext.getCmp('search_operator');

		controller.Controller.getSearchCriteriaStore().add([new model.SearchCriteria({
			"field":search.getValue(),
			"criteria":field_text.getValue(), 
			"operator": search_op.getValue().getGroupValue(),
			"showableField":search.getValue()
		})]);
		search.clearValue();
		field_text.reset();
		search.focus.defer(50,search);
	}
	});

	

	var viewport = new Ext.Viewport({
		layout:'border',
		defaults: {
			collapsible: true,
			split: true
		},

	items:[new Ext.Panel({
               layout: 'anchor',
               title: "Search",
               region: 'west',
               bodyStyle: 'padding:5px',
               width: 300,
               minSize: 200,
               maxSize: 400,
               collapsible: true,
               margins:'5 0 5 5',
               items: [
                      (searchForm = new view.SearchForm({
                           anchor: '100%',
                           doAction: addCriteriaAction,
                           height: 135
                       })), 
                       new Ext.Panel({
                           baseCls: 'x-plain',
                           anchor: '100%',
                           title: "Search criteria",
                           items: (searchCriteriaList = new view.SearchCriteriaList({
                               autoEl: 'div',
                               deferEmptyText: false
                           }))    
                       })
               ]               
           }), {
	           region: 'center',
               activeTab : 0,
               xtype: 'tabpanel',
               margins:'5 5 5 0',
               collapsible: false,
               items: [contactsView = new view.ContactsGridAndDetailPanel({
                        title: "Contacts",
                        id: "contacts"
                    }),
                    groupedContactsView = new view.GroupedContactsGrid({
                        id: "groupedContacts",
                        title: 'Grouped contacts',
                        disabled: true
                    })],
               listeners: {
					'tabchange': function(tabPanel, tab){
						if (tab.id === "groupedContacts") {
							controller.Controller.setCurrentStore(controller.Controller.getContactGroupingStore());
							doQuery();
						} else {
							controller.Controller.setCurrentStore(controller.Controller.getContactStore());
						}
                    }
			   }
           }
	       ]
	});

	view.LoginForm.createAndShowIfNotLoggedIn();

    
    var getQueryFromRecord = function(r) {
        var op = r.get("operator"),
        crit = r.get('criteria'),
        terms,i,//iteration vars
        f = r.get("field"),
        queryTerms = [],
        luceneOp = "",
        fieldType;
        switch (op) {
        case "MUST":
            luceneOp = "+";
            break;    
        case "NOT":
            luceneOp = "-";
            break;
        default:
            luceneOp = "";
        }
        fieldType = com.trifork.intrafoo.controller.Controller.getSearchFieldStore().getById(f).data.type;

        if (fieldType === 'int') {
            f = f + "<" + fieldType + ">";
            queryTerms = crit.split('-');
            if (queryTerms.length === 1) {
                queryTerms.push(queryTerms[0]);
            }
            return luceneOp+f+":["+queryTerms.join(" TO ") + "]";
        } 
        terms = crit.split(" ");
        for (i = 0; i < terms.length; i++) {
            queryTerms.push(luceneOp + f + ":" + terms[i]); 
        }
        return queryTerms.join(" ");
    };

	

	controller.Controller.getSearchCriteriaStore().on({
		'add': function(sc, records, index){
			Ext.each(records, function(r){
				queryObj[index] = getQueryFromRecord(r);
			});
		doQuery();
	},
	'remove': function(sc, records, index ){
		queryObj.splice(index,1);
		if (sc.getCount() === 0) {
			controller.Controller.getContactStore().removeAll();
			controller.Controller.getContactStore().baseParams = {};
		} else {
			doQuery();
		}
	}
	});

	controller.Controller.getContactStore().on('clear', function(){
		Ext.getCmp('contacttab').clearContact();
        Ext.getCmp('companytab').clearCompany();
		controller.Controller.getContactStore().totalLength = 0;
		Ext.getCmp('contactsPaging').beforeLoad();
		Ext.getCmp('contactsPaging').onLoad(controller.Controller.getContactStore(),[],{});
	});

	var search = Ext.getCmp('searchCombo'),
	field_text = Ext.getCmp('search_criteria');
	search.on('select', function(combo,rcd,idx){
		field_text.focus.defer(50,field_text);
	});
    search.on('specialkey', function(field,e){
        if (e.getKey() === e.TAB) {
            this.onViewClick();
        }
    });
    
	field_text.on('specialkey', function(field,e){
		if (e.getKey() === e.ENTER) {
			addCriteriaAction.execute(e);
		}
	});

	search.focus();
});