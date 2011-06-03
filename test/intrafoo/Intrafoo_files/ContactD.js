namespace('com.trifork.intrafoo.view');

using(com.trifork.intrafoo.view).run(function(view){
	var contactURLTemplate = "https://intranet.trifork.com/intratools/contactview?contactid={0}&fullversion=true",
        companyURLTemplate = "https://intranet.trifork.com/intratools/companyview?companyid={0}&fullversion=true";
	view.ContactDetail = Ext.extend(Ext.Panel, {
		id: 'contactDetail', 
        autoScroll: true,
		// add tplMarkup as a new property
		tpl: new Ext.XTemplate(
            '<strong>Name</strong>: <a href="{[this.getContactLink(values)]}">{Name}</a><br>',
            '<strong>Occupation</strong>: {Occupation}<br>',
            "<strong>Company</strong>: <a href='{[this.getCompanyLink(values)]}'>{[this.getCompanyname(values)]}</a><br>",
            '<strong>Activities</strong>: ',
            '<table>{[this.get(values,"Activities")]}</table>',
            '<strong>Interests</strong>: ',
            '<table>{[this.get(values,"Interests")]}</table>',
            '<strong>Campagnes</strong>: ',
            '<table>{[this.get(values,"Campagnes")]}</table>',  {
	        // XTemplate configuration:
	        compiled: true,
	        disableFormats: true,
            get: function(values,p){
                var a = values[p];
                a.sort();
                var i=0,
                    N=a.length,
                    res = [];
                for (;i<N;i += 1) {
                    if (i % 2 === 0) {
                        res.push("<tr><td>"+a[i]);
                    } else {
                        res.push("<td>"+a[i]);
                    }
                }
                return res.join("");
            },
	        getContactLink: function(values){
                return String.format(contactURLTemplate, values.ID);
	        },
            getCompanyLink: function(values){
                var c = values.Company;
                if (c && c.ID) {
                    return String.format(companyURLTemplate, c.ID);
                }
                return "#";
            },
            getCompanyname: function(values){
                var c = values.Company;
                if (c && c["Company name"]) {
                    return c["Company name"];
                }
                return "unknown";
            } 
	    }),

		// startingMarup as a new property
		startingMarkup: 'Please select a contact to see additional details',
		// override initComponent to create and compile the template
		// apply styles to the body of the panel and initialize
		// html to startingMarkup
		initComponent: function() {
			Ext.apply(this, {
				bodyStyle: {
					background: '#ffffff',
					padding: '7px'
				},
				html: this.startingMarkup
			});
			// call the superclass's initComponent implementation
			view.ContactDetail.superclass.initComponent.call(this);
		},
		// add a method which updates the details
		updateContact: function(data) {
			this.tpl.overwrite(this.body, data);
		}, 
        clearContact: function() {
            this.body.update(this.startingMarkup);
        }
        
	});
	// register the App.BookDetail class with an xtype of bookdetail
	Ext.reg('contactdetail', view.ContactDetail);
});