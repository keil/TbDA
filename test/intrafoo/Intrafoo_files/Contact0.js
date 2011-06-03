namespace('com.trifork.intrafoo.model');

using(com.trifork.intrafoo.model).run(function(model){
	model.ContactFields = [
	                       {name: "ID"},
	                       {name: "Name"},
	                       {name: "Occupation"},
	                       {name: "Country"},
	                       {name: "City"},
	                       {name: "Zip code"},
	                       {name: "Activities"},
	                       {name: "Mobile phone"},
	                       {name: "Email address"},
	                       {name: "Address"},
	                       {name: "Last modified by"},
	                       {name: "Log"},
	                       {name: "Modified date", type:'date'},
	                       {name: "Company"},
	                       {name: "CompanyName", convert: function(_a,data){
                             if(data.Company) {
                                return data.Company['Company name'];
                             }
                             return "";
                           }},
	                       {name: "Campaign mail"},
	                       {name: "Interests"},
	                       {name: "Office phone"},
	                       {name: "Private phone"},
	                       {name: "Campagnes"},
	                       {name: "Username"},
	                       {name: "Fax"},
	                       {name: "Initial content"},
	                       {name: "Comment"}
	                   ];
	
	
    model.Contact = Ext.data.Record.create(model.ContactFields);
});