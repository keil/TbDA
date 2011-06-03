namespace('com.trifork.intrafoo.controller');

using(com.trifork.intrafoo).run(function(ifoo){
	var controller = ifoo.controller,
		model = ifoo.model,
		fields = (function() {
			var l = model.ContactFields.length,
				res = []; 
			for (var i = 0; i < l; i++) {
				res.push(model.ContactFields[i].name);
			}
			return res;
		})();
	
	
	controller.ContactGroupingStore = function(config) {
		config = config || {};
		var proxy = new Ext.data.HttpProxy({
				    method: 'GET',
				    disableCaching: true,
				    url: '/intrafoo/search/by'
				}),
		    tmpStore = new Ext.data.GroupingStore({
				    reader: new Ext.data.ArrayReader({}, model.Contact),
					groupField: "Interests",
					sortInfo:{field: 'Name', direction: "ASC"}
				}),
            tmpObjects = []; 
		
		Ext.applyIf(config, {
			//TODO remoteSort: true,
			proxy : proxy,
		    reader: new Ext.data.JsonReader({ 
			    idProperty: 'ID',
			    root: 'rows',
			    totalProperty: 'total_rows'
			},
			model.Contact)
		});
		
		controller.ContactGroupingStore.superclass.constructor.call(this, config);
		this.on("datachanged", function(t) {
			tmpStore.removeAll();
			tmpObjects = [];
			
			t.each(function(o) {
				var a = o.json[tmpStore.groupField],
                    tmpRow,
                    i,l,N,M; //iteration vars
				if (a instanceof Array) { //if grouping is on an array, "clone" the rows
					for(i =0,N=a.length; i < N; i++) {
						tmpRow = [];
						for (l = 0, M=fields.length; l<M; l++) {
							if (fields[l] === tmpStore.groupField) {
								tmpRow.push(a[i]);
							} else {
								tmpRow.push(o.json[fields[l]]);
							}
						}
						tmpObjects.push(tmpRow);					
					}
				} else {
					tmpRow = [];
					for (l = 0, N=fields.length; l<N; l++) {
							tmpRow.push(o.json[fields[l]]);
					}
					tmpObjects.push(tmpRow);					
				}
			});
			tmpStore.loadData(tmpObjects);
		});
		return tmpStore;
	};
	
	Ext.extend(controller.ContactGroupingStore, Ext.data.GroupingStore);
});



/*namespace('com.trifork.intrafoo.controller');

using(com.trifork.intrafoo).run(function(ifoo){
	var controller = ifoo.controller,
		model = ifoo.model;
	
	controller.ContactGroupingStore = function(config) {
		var proxy = new Ext.data.HttpProxy({
                method: 'GET',
                disableCaching: true,
                url: '/intrafoo/search/by'
			}), 
			data = [],
			totalCount = 1000;
		
		proxy.on("load", function(t, o, options) {
			console.log(o);
		}); 
		
		config = config || {};
		Ext.applyIf(config, {
			//TODO remoteSort: true,
			proxy : proxy,
		    reader: new Ext.data.ArrayReader({ 
			    idProperty: 'ID_generated',
			    root: 'rows',
			    getTotal: function() { return totalCount; }
			}, model.Contact),
			data: data
		});
		controller.ContactGroupingStore.superclass.constructor.call(this, config);
	};
	
	Ext.extend(controller.ContactGroupingStore, Ext.data.GroupingStore);
});
*/