namespace('com.trifork.intrafoo.model');

using(com.trifork.intrafoo.model).run(function(model){
    
    model.Tag = Ext.data.Record.create({name: "name", type: "string"});

});