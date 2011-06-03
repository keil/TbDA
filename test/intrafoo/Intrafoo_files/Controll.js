namespace('com.trifork.intrafoo.controller');

using(com.trifork.intrafoo.controller).run(function(c){
	
	var contactStore = null,
		searchFieldStore = null,
		searchCriteriaStore = null,
		contactGroupingStore = null,
		currentStore = null,
        interestStore = null,
        activityStore = null,
        campagneStore = null,
        tagData = null;
        
	c.Controller = Ext.apply(new Ext.util.Observable(),{
		init: function() {
			currentStore = this.getContactStore();
            this.getTagData();
		},
	
		setCurrentStore: function(s) {
			currentStore = s;
		},
		
		getCurrentStore: function() {
			return currentStore;
		},
		
		getContactGroupingStore: function() {
			if (!contactGroupingStore) {
				contactGroupingStore = new c.ContactGroupingStore({
					storeId: 'contactgrouping'
				});
			}
			return contactGroupingStore;
		},
		
		getContactStore: function() {
			if (!contactStore) {
				contactStore = new c.ContactStore({
					storeId: 'contact'
				});
			}
			return contactStore;
		},
		
		getSearchFieldStore: function() {
			if (!searchFieldStore) {
				searchFieldStore = new c.SearchFieldStore({
					storeId: 'searchfield'
				});
			}
			return searchFieldStore;
		},
		
		getSearchCriteriaStore: function() {
			if (!searchCriteriaStore) {
				searchCriteriaStore = new c.SearchCriteriaStore({
					storeId: 'searchcriteria'
				});
			}
			return searchCriteriaStore;
		},
        
        getInterestStore: function() {
            if (!interestStore) {
                interestStore = new c.TagStore({
                    storeId: "interestStore",
                    dataProperty: "Interests",
                    data: tagData || {Interests: [], totalProperty: 0}
                });
            }
            return interestStore;
        },
        getActivityStore: function() {
            if (!activityStore) {
                activityStore = new c.TagStore({
                    storeId: "activityStore",
                    dataProperty: "Activities",
                    data: tagData || {Activities: [], totalProperty: 0}
                });
            }
            return activityStore;
        },
        getCampagneStore: function() {
            if (!campagneStore) {
                campagneStore = new c.TagStore({
                    storeId: "campagneStore",
                    dataProperty: "Campagnes",
                    data: tagData || {Campagnes: [], totalProperty: 0}
                });
            }
            return campagneStore;
        },
        
        getTagData: function() {
            Ext.Ajax.request({
                url: '/intrafoo/tag/all',
                headers: {
                    "Accept": "application/json"
                },
                success: function(resp, opt) {
                    if (resp.status === 200) {
                        tagData = JSON.parse(resp.responseText);
                    }
                },
                failure: function(resp, opt) {
                    //silently fail.. report error on usage.
                }
            });       
        },
        
        extractFailureInformation: function(spec) {
            return {simple: "url: "+ spec.options.url +"<br>" +
                             "params: "+JSON.stringify(spec.options.params), 
                full: {
	                storeId: spec.store? spec.store.storeId : "",
	                url: spec.options.url,
	                params: JSON.stringify(spec.options.params),
	                headers: JSON.stringify(spec.options.headers),
	                type:spec.type,
	                action: spec.action,
	                status: spec.response.status,
	                response: spec.response.responseText ||  spec.response.statusText
            }};
        },
        logError: function(info, spec) {
            Ext.Ajax.request(Ext.apply({
                url: '/intrafoo/errors',
                headers: {
                    "Content-Type": "application/json"
                },
                method: "POST",
                jsonData: info.full                
            }, spec));
        },
        handleAjaxError: function(spec) {
             var info = this.extractFailureInformation(spec);
             Ext.Msg.alert('Server error', 
               'Server error occured during operation. This will be logged on server...<br>'+info.simple);
             this.logError(info, Ext.apply({
                success: function() {
                 //no op
                }, 
                failure: function() {
	                Ext.Msg.alert('Server error', 
	                   'Loggin of error failed. Please send this technical info to support: <br><br>'+
	                   JSON.stringify(info.full));
	                }
             },spec));
             this.resetUI(spec);
        },
        resetUI: function(spec) {
           if (spec.uicomponents) {
                Ext.each(spec.uicomponents, function(c){
                    if (c && c.loadMask && typeof c.loadMask.hide === 'function') {
                        c.loadMask.hide();
                    }
                });
            }
        }
		
	});
});