namespace('com.trifork.intrafoo.model');
using(com.trifork.intrafoo.model).run(function(model){
    model.SearchCriteria = Ext.data.Record.create([
        {name: "operator", defaultValue: ""},
        {name: "field"},
        {name: "showableField"},
        {name: "criteria"}
    ]);
});

