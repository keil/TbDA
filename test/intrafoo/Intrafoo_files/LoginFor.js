namespace('com.trifork.intrafoo.view');

using(com.trifork.intrafoo).run(function(ifoo){
	var view = ifoo.view,
		c = ifoo.controller,
		SESSION_URL = '/intrafoo/session';
	  
	view.LoginForm = {
		createAndShowIfNotLoggedIn: function() {
			Ext.Ajax.request({
				url: SESSION_URL,
				  success: function(response, opts) {
                      Ext.getCmp("searchCombo").focus();
				  },
				  failure: function(response, opts) {
                      //not logged in must do it...
                      view.LoginForm.showLoginWindow();
				  }
			});
		},
		
		showLoginWindow: function() {
			var win = new Ext.Window({
	            applyTo:'login-win',
	            layout:'fit',
	            width:400,
	            height:200,
	            closeAction:'hide',
	            defaultButton: 'j_username',
	            plain: true,
	
	            items: new Ext.FormPanel({
                    id: 'loginForm',
                    labelWidth: 75, // label settings here cascade unless overridden
	                frame:true,
	                title: false,
	                bodyStyle:'padding:5px 5px 0',
	                width: 350,
	                defaults: {width: 230},
	                defaultType: 'textfield',
					keys: [{
					    key: Ext.EventObject.ENTER,
					    fn: function() {
                            var fs = Ext.getCmp("loginForm");
                            fs.getForm().submit({
                                waitMsg:'Logging into Intratools...',
                                clientValidation: false,
                                url:SESSION_URL,
                                success: function(form, action) {
							       win.hide();
							       Ext.getCmp("searchCombo").focus();
							    },
							    failure: function(form, action) {
							       Ext.getCmp('j_username').focus(); 
							    }
				            });
					    }
					},{
					    key: Ext.EventObject.ESC,
                        fn: function() {
                            win.hide();
                            Ext.getCmp("searchCombo").focus();
                        }
					}],
					
	                items: [{
                        id: 'j_username',
                        fieldLabel: 'Login',
                        name: 'j_username',
                        allowBlank:false
                    },{
                        fieldLabel: 'Password',
                        inputType:'password',
                        allowBlank: false,
                        name: 'j_password'
                    }]
	            }),
		
	            buttons: [{
	                text:'Login',
	                handler: function(){
	                    var fs = Ext.getCmp("loginForm");
	                    fs.getForm().submit({
	                        waitMsg:'Logging into Intratools...',
	                        clientValidation: false,
	                        url:SESSION_URL,
	                        success: function(form, action) {
	                            win.hide();
                                Ext.getCmp("searchCombo").focus();
	                            
	                        },
	                        failure: function(form, action) {
                                var sb = Ext.getCmp('basic-statusbar');
                                sb.setStatus({
                                    text: 'Username/Password combination is not valid.',
                                    iconCls: 'x-status-error',
                                    clear: true // auto-clear after a set interval
                                }); 
                                Ext.getCmp('j_username').focus();
	                        }
	                    });
	                }
		         },{
		             text: 'Fortryd',
		             handler: function(){
		                 win.hide();
		             }
		        }],
		        
		        bbar: new Ext.ux.StatusBar({
		            id: 'basic-statusbar',
		            defaultText: '',
		            text: 'Please log in'
		        })
			});
		    
		    win.show(this);
		}
	};
});