
var itemsPerPage = 5;
//user store
var userStore = Ext.create('Ext.data.Store',{
    autoLoad: true,
    autoSync: true,
    pageSize: itemsPerPage,
   proxy: {
       type: 'rest',
       url: 'userServlet',
       enablePaging: true,
       reader: {
           type:'json',
           rootProperty: 'data',
           totalProperty: 'total',
           successProperty: 'success'
       },
       writer:{
           type: 'json'
       }
   }


});





//user grid

var userGrid = Ext.create('Ext.grid.Panel',{
    height: '100%',
    width: '100%',
    id: 'userGrid',
    frame: true,
    title: 'Welcome Users',
    store: userStore,
    selModel: {
        mode: 'SINGLE',
        allowDeselect: true
    },
    style: {
        marginLeft	: 'auto',
        marginRight	: 'auto'
    },
    columns: [
        {
        text: 'Firstname',
        width: 80,
        flex: 1,
        sortable: true,
        dataIndex: 'firstname',
        field: {
            xtype: 'textfield'
        }
    }, {
        text: 'Lastname',
        width: 80,
        flex: 1,
        sortable: true,
        dataIndex: 'lastname',
        field: {
            xtype: 'textfield'
        }
    }, {
        text:'Email',
        width: 80,
        flex: 1,
        sortable: true,
        dataIndex:'email',
        field:{
            xtype: 'textfield'
        }
    },{
        text:'Phone',
        width: 80,
        flex: 1,
        sortable: true,
        dataIndex:'phone',
        field:{
            xtype: 'textfield'
        }
    }


    ],
        dockedItems:[{
            xtype:'toolbar',
            dock: 'top',
            items:[
                {
                    text: 'Create',
                    iconCls: 'icon-add',
                    listeners: {
                        click: function () {
                            var createUsers = Ext.create('Ext.form.Panel',{
                               items: [
                                   {
                                       xtype: 'textfield',
                                       fieldLabel: 'Firstname',
                                       name: 'fname',
                                       allowBlank: false,
                                       blankText: 'This field is required',
                                       padding: 10
                                   },
                                   {
                                       xtype: 'textfield',
                                       fieldLabel: 'Lastname',
                                       name:'lname',
                                       allowBlank: false,
                                       blankText: 'This field is required',
                                       padding: 10

                                   },
                                   {
                                       xtype: 'textfield',
                                       fieldLabel:'Email',
                                       name: 'email',
                                       vtype: 'email',
                                       allowBlank: false,
                                       blankText: 'This field is required',
                                       padding: 10
                                   },
                                   {
                                       xtype: 'textfield',
                                       fieldLabel: 'Phone',
                                       name: 'phone',
                                       inputType: 'number',
                                       allowBlank: false,
                                       blankText: 'This field is required',
                                       padding: 10
                                   }
                               ],
                                buttons:[
                                    {
                                        text: 'Save',
                                        handler: function () {
                                            var form = this.up('form').getForm();
                                            if(form.isValid()){
                                                form.submit({
                                                    url:'createUser',
                                                    method: 'POST',
                                                    success: function (form, action) {
                                                        var assoc = Ext.decode(action.response.responseText);
                                                        Ext.MessageBox.show({
                                                            msg: 'Saving changes, please wait...',
                                                            progressText: 'Adding...',
                                                            width: 300,
                                                            wait: true,
                                                            waitConfig:
                                                                {
                                                                    duration: 5000,
                                                                    increment: 15,
                                                                    text: 'Adding...',
                                                                    scope: this,
                                                                    fn : function () {
                                                                        Ext.MessageBox.hide();
                                                                        Ext.Msg.alert('Status','Added Successfully');
                                                                        userStore.load(); //reload grid
                                                                    }
                                                                }
                                                        });
                                                        //close createUser form after submission and enable grid
                                                        win.close();
                                                        userGrid.enable();
                                                    },


                                                })
                                            }else {
                                                Ext.MessageBox.show({
                                                    title: 'Failed',
                                                    msg: 'Please fill the required fields correctly',
                                                    buttons: Ext.MessageBox.OK,
                                                    icon: Ext.MessageBox.WARNING
                                                });
                                            }
                                        }
                                    },
                                    {
                                        text: 'Cancel',
                                        handler: function () {
                                            win.close();
                                            userGrid.enable();
                                        }
                                    }
                                ]
                            }); // end of create form
                                //create a window
                            userGrid.disable(); //disable grid if button is selected
                            var win = Ext.create('Ext.window.Window',{
                                    renderTo: Ext.getBody(),
                                    title: 'Create User',
                                    layout: 'fit',
                                    width: 400,
                                    closable: false,
                                    closeAction: 'hide',
                                    plain: true,
                                    items: [createUsers]
                            }).show();

                        }
                    }
                },
                {
                    text: 'Update',
                    iconCls: 'icon-update',
                    handler: function (){
                        var userData = userGrid.getSelectionModel().getSelection();

                        if(userData === null){
                            Ext.Msg.alert('Warning', 'Must Select');
                            return;
                        }
                        if(userData.length < 1) {
                            Ext.Msg.alert('Warning', 'Must Select');
                            return;
                        }
                        console.log(userData);

                        var userUpdate = Ext.create('Ext.form.Panel',{
                            items:[
                                {
                                    xtype: 'textfield',
                                    fielLabel: 'User Id',
                                    name: 'user_id',
                                    value: userData[0].data.user_id,
                                    padding: 10,
                                    hidden: true
                                },
                                {
                                    xtype:'textfield',
                                    fieldLabel: 'Firstname',
                                    name: 'firstname',
                                    value: userData[0].data.firstname,
                                    padding: 10
                                },
                                {
                                    xtype:'textfield',
                                    fieldLabel: 'Lastname',
                                    name:'lastname',
                                    value: userData[0].data.lastname,
                                    padding: 10
                                },
                                {
                                    xtype:'textfield',
                                    fieldLabel:'Email',
                                    name: 'email',
                                    value: userData[0].data.email,
                                    padding: 10
                                },
                                {
                                    xtype: 'textfield',
                                    fieldLabel: 'Phone',
                                    name:'phone',
                                    value: userData[0].data.phone
                                }
                            ],
                            buttons:[
                                {

                                    text: 'Update',
                                    id: 'submit',
                                    handler: function(){

                                        var form = this.up('form').getForm();
                                        if (form.isValid()) {
                                            form.submit({
                                                url: 'update',
                                                method: 'POST',
                                                success: function(form, action) {
                                                    var assoc = Ext.decode(action.response.responseText);
                                                    Ext.MessageBox.show({
                                                        msg : 'Saving changes, please wait...',
                                                        progressText : 'Updating...',
                                                        width : 300,
                                                        wait : true,
                                                        waitConfig :
                                                            {
                                                                duration : 5000,
                                                                increment : 15,
                                                                text : 'Updating...',
                                                                scope : this,
                                                                fn : function(){
                                                                    Ext.MessageBox.hide();
                                                                    Ext.Msg.alert('Status', 'Successfully Updated!');
                                                                    store.load(); //reload grid
                                                                }
                                                            }
                                                    });
                                                    winUpdate.destroy();
                                                    grid.enable();
                                                }
                                            });
                                        }
                                    }
                                },
                                {
                                    text: 'Cancel'
                                }
                            ]
                        });
                    }
                },
                {
                    text: 'Delete',
                    iconCls: 'icon-delete',
                    handler: function () {
                        var userdata = userGrid.getSelectionModel().getSelection();

                        if(userdata === null){
                            Ext.Msg.alert('Warning', 'Must Select');
                            return;
                        }
                        if(userdata.length < 1){
                            Ext.Msg.alert('Warning', 'Must Select');
                            return;
                        }
                       // console.log(data)
                        Ext.Msg.confirm('Delete?', 'Are you sure you want to delete ('+userdata[0].data.firstname +')?',function (answer) {
                            if(answer === 'yes'){
                                var user_id =  userdata[0].data.user_id;
                                Ext.Ajax.request({
                                    url: 'deleteUser',
                                    method: 'POST',
                                    params:{
                                        user_id:user_id
                                    },
                                    success: function (response) {
                                        var assoc = Ext.decode(response.responseText);
                                        Ext.MessageBox.show({
                                            msg: 'Saving changes, please wait...',
                                            progressText: 'Deleting....',
                                            width: 300,
                                            wait: true,
                                            waitConfig:
                                                {
                                                    duration: 5000,
                                                    increment: 15,
                                                    text: 'Deleting...',
                                                    scope: this,
                                                    fn: function () {
                                                        Ext.MessageBox.hide();
                                                        Ext.Msg.alert('Status', 'Deleted successfully!');
                                                        userStore.load(); //reload grid
                                                    }
                                                }
                                        });
                                    }
                                });
                            }
                        });
                    }
                },
                //createform for userSample with image upload
                {
                    text: 'Create',
                    iconCls: 'icon-add',
                    listeners: {
                        click: function () {
                            var createUsers = Ext.create('Ext.form.Panel',{
                                items: [
                                    {
                                        xtype: 'textfield',
                                        fieldLabel: 'Firstname',
                                        name: 'fname',
                                        allowBlank: false,
                                        blankText: 'This field is required',
                                        padding: 10
                                    },
                                    {
                                        xtype: 'textfield',
                                        fieldLabel: 'Lastname',
                                        name:'lname',
                                        allowBlank: false,
                                        blankText: 'This field is required',
                                        padding: 10

                                    },
                                    {
                                        xtype: 'textfield',
                                        fieldLabel:'Email',
                                        name: 'email',
                                        vtype: 'email',
                                        allowBlank: false,
                                        blankText: 'This field is required',
                                        padding: 10
                                    },
                                    {
                                        xtype: 'textfield',
                                        fieldLabel: 'Phone',
                                        name: 'phone',
                                        inputType: 'number',
                                        allowBlank: false,
                                        blankText: 'This field is required',
                                        padding: 10
                                    }
                                ],
                                buttons:[
                                    {
                                        text: 'Save',
                                        handler: function () {
                                            var form = this.up('form').getForm();
                                            if(form.isValid()){
                                                form.submit({
                                                    url:'createUser',
                                                    method: 'POST',
                                                    success: function (form, action) {
                                                        var assoc = Ext.decode(action.response.responseText);
                                                        Ext.MessageBox.show({
                                                            msg: 'Saving changes, please wait...',
                                                            progressText: 'Adding...',
                                                            width: 300,
                                                            wait: true,
                                                            waitConfig:
                                                                {
                                                                    duration: 5000,
                                                                    increment: 15,
                                                                    text: 'Adding...',
                                                                    scope: this,
                                                                    fn : function () {
                                                                        Ext.MessageBox.hide();
                                                                        Ext.Msg.alert('Status','Added Successfully');
                                                                        userStore.load(); //reload grid
                                                                    }
                                                                }
                                                        });
                                                        //close createUser form after submission and enable grid
                                                        win.close();
                                                        userGrid.enable();
                                                    }
                                                })
                                            }else {
                                                Ext.MessageBox.show({
                                                    title: 'Failed',
                                                    msg: 'Please fill the required fields correctly',
                                                    buttons: Ext.MessageBox.OK,
                                                    icon: Ext.MessageBox.WARNING
                                                });
                                            }
                                        }
                                    },
                                    {
                                        text: 'Cancel',
                                        handler: function () {
                                            win.close();
                                            userGrid.enable();
                                        }
                                    }
                                ]
                            }); // end of create form
                                //create a window
                            userGrid.disable(); //disable grid if button is selected
                            var win = Ext.create('Ext.window.Window',{
                                renderTo: Ext.getBody(),
                                title: 'Create User',
                                layout: 'fit',
                                width: 400,
                                closable: false,
                                closeAction: 'hide',
                                plain: true,
                                items: [createUsers]
                            }).show();

                        }
                    }
                }


            ]
        }],
            bbar: [{
                xtype: 'pagingtoolbar',
                pageSize: itemsPerPage,
                store:userStore,
                displayInfo: true,
                displayMsg: 'Displaying {0} to {1} of {2} &nbsp;records ',
                emptyMsg: "No records to display&nbsp;"

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
        items: [userGrid]
    });
});