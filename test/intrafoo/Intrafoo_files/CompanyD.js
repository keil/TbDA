namespace('com.trifork.intrafoo.view');

using(com.trifork.intrafoo.view).run(function(view){
	var companyURLTemplate = "https://intranet.trifork.com/intratools/companyview?companyid={0}&fullversion=true";
	
    view.CompanyDetail = Ext.extend(Ext.Panel, {
		id: 'companyDetail', 
        autoScroll: true,
		// add tplMarkup as a new property
		tpl: new Ext.XTemplate(
            '<strong>Name</strong>: <a href="{[this.getCompanyLink(values)]}">{[this.getCompanyname(values)]}</a><br/>',
            {
	        // XTemplate configuration:
	        compiled: true,
	        disableFormats: true,
            
            getCompanyLink: function(c){
                if (c && c.ID) {
                    return String.format(companyURLTemplate, c.ID);
                }
                return "#";
            },
            getCompanyname: function(c){
                if (c && c["Company name"]) {
                    return c["Company name"];
                }
                return "unknown";
            } 
	    }),

		// startingMarup as a new property
		startingMarkup: 'Please select a contact to see company details',
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
			view.CompanyDetail.superclass.initComponent.call(this);
		},
		// add a method which updates the details
		updateCompany: function(data) {
			this.tpl.overwrite(this.body, data);
		},
        clearCompany: function() {
            this.body.update(this.startingMarkup);
        }
        
	});
	// register the App.BookDetail class with an xtype of bookdetail
	Ext.reg('companydetail', view.CompanyDetail);
});