//Check Sessions Here!
Ext.Ajax.request({
    url: 'checksavedsession',
    method: "GET",
    timeout: 60000,
    data: {},
    headers: {Accept : "application/json;charset=utf-8"},
    success: function(response) {
        var assoc = Ext.decode(response.responseText);
        if (assoc['success']) {               //if there are session saved redirect to index.html
           location.assign('index.html');
        }
    }
});

var formLogin = Ext.create('Ext.form.Panel', {
    title: 'Login',
    bodyPadding: 5,
    width: 350,
    frame: true,
    style: {
        marginLeft	: 'auto',
        marginRight	: 'auto'
    },
    items: [{
        xtype: 'textfield',
        fieldLabel: 'Username',
        maskRe: /[A-Za-z0-9]/,
        name: 'admin_user',
        allowBlank: false,
        blankText: 'This field is required'
    },
    {
        xtype: 'textfield',
        id: 'password',
        fieldLabel: 'Password',
        name: 'admin_password',
        enforceMaxLength: true,
        maxLength: 32,
        allowBlank: false,
        inputType: 'password',
        blankText: 'This field is required'

    }],

    buttons: [{
        text: 'Login',
        formBind:true,  // button will be disabled if the textfield are empty
        handler: function () {
            Ext.MessageBox.show({
                msg: 'Loading Data',
                progressText: 'Verifying...',
                width: 300,
                wait: true,
                waitConfig: {
                    duration: 6000,
                    text: 'Verifying...',
                    scope: this,
                    fn: function() {
                        Ext.MessageBox.hide();
                    }
                }
            });

            var form = this.up('form').getForm();
            if(form.isValid()){
             // check password before submitting if it contains alphanumeric chars and special chars
            var password =  Ext.getCmp('password').getValue();
            var regexp = /^.*(?=.{8,16})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).*$/;
            if (regexp.test(password)) {
                form.submit({
                    url: 'login',
                    method: 'post',
                    success: function (form, action) {
                        var assoc = Ext.decode(action.response.responseText);
                            var secreteform = Ext.create('Ext.form.Panel',{
                                width: 400,
                                height: 150,
                                title: 'Enter Secret Key',
                                floating: true,
                                tabIndex: 0,
                                focusable: true,
                                modal: true,
                                items:[
                                    {
                                        xtype: 'textfield',
                                        id: 'securityKey',
                                        name: 'securityKey',
                                        fieldLabel: 'Secrete Key',
                                        padding: 10,
                                        allowBlank: false
                                    }
                                ],
                                buttons:[
                                    {
                                        text: 'Submit',
                                        disabled: true,
                                        formBind: true,
                                        handler: function () {
                                            Ext.MessageBox.show({
                                                msg: "Generating Secrete Key...",
                                                progressText: "Generating...",
                                                width: 300,
                                                wait: true,
                                                waitConfig:{
                                                    duration: 6000,
                                                    text: 'Verifying...',
                                                    scope: this,
                                                    fn: function() {
                                                        Ext.MessageBox.hide();
                                                    }

                                                }
                                            });
                                            var form = this.up('form').getForm();
                                            if (form.isValid()) {
                                                form.submit({
                                                   url: 'generateKey',
                                                   method: 'post',
                                                    params:{
                                                            admin_id:assoc['admin_id'],
                                                            role:assoc['isAdmin']
                                                    },
                                                   success: function (form,action) {
                                                       var  assoc = Ext.decode(action.response.responseText);
                                                       Ext.MessageBox.hide();

                                                       setTimeout(function() {
                                                           if (assoc['role']) {
                                                               location.assign("index.html");
                                                           } else {
                                                               location.assign("userSample.html");
                                                           }
                                                       }, 250);


                                                   },
                                                    failure: function(form, action){
                                                       var assoc = Ext.decode(action.response.responseText);
                                                       Ext.MessageBox.hide();
                                                       Ext.MessageBox.show({
                                                           title: 'Fail',
                                                           message: 'Security Code Invalid, Go Back to Login page to generate the code.',
                                                           buttons: Ext.Msg.YESNO,
                                                           buttonText: {
                                                               yes: 'OK',
                                                               no: 'Back to Login'
                                                           },
                                                           fn: function(btn) {
                                                               if (btn === 'no') {
                                                                   location.assign('login.html');
                                                               }
                                                           }

                                                       });

                                                    }

                                                });
                                            }else {
                                                Ext.Msg.alert('Warning','Form is not valid');
                                            }

                                        }
                                    }
                                ]
                            });
                            secreteform.show();
                            formLogin.close();

                             //location.assign('index.html');
                    },
                    failure: function (form, action) {
                        var assoc = Ext.decode(action.response.responseText);
                        Ext.MessageBox.hide();
                        Ext.MessageBox.show({
                            title: 'Fail',
                            //message: assoc['reason'] + ".Username and Password do not match.",
                            message: assoc['reason'],
                            buttons: Ext.Msg.YES,
                            buttonText: {
                                yes: 'Back to login'
                            }

                        });
                    }

                });
            } else {
                Ext.Msg.alert("Login Fail", "Password is invalid");
            }

            }
        }
    }]
});

Ext.onReady(function () {
    Ext.QuickTips.init();

    Ext.create('Ext.container.Viewport', {
        renderTo: Ext.getBody(),
        layout: {
            type: 'vbox',
            align: 'center',
            pack: 'center'
        },
    items: [formLogin]
    });
});