Ext.define('movieModel', {
    extend: 'Ext.data.Model',
    fields: [
				{name: 'film_id'	, 	type: 'string'},
				{name: 'title'		, 	type: 'string'},
				{name: 'description', 	type: 'string'},
				{name: 'release_year', 	type: 'string'},
				{name: 'language'	, 	type: 'string'},
				{name: 'director'	, 	type: 'string'},
				{name: 'rating'		, 	type: 'string'},
				{name: 'feature'	, 	type: 'string'},
			]
});

var movieStore = Ext.create('Ext.data.Store', {
	storeId: 'movieStore',
	model:  'movieModel',
	pageSize: 7,
	autoLoad: true,
	proxy: {
        type: 'ajax',
		url: '/SakilaProject/GetDataFromDb',
		appendId: true,
		noCache: false,
		enablePaging: true,
        reader: {
            type: 'json',
			rootProperty: 'film',
			totalProperty: 'total',
        },
		success: function(response){
			Ext.Msg.alert('Status','Request Passed.');
			consol.log(response);
		},
		failure: function(response){
			Ext.Msg.alert('Status','Request Failed.');
		},
	}
});

movieStore.load({
	params: {
		start: 0,
		limit: 7,
		query: 'select'
	}
});

Ext.define('languageModel', {
    extend: 'Ext.data.Model',
    fields: [{name: 'languageSelection', type: 'string'}]
});

var langStore = Ext.create('Ext.data.Store', {
	storeId: 'langStore',
    model:  'languageModel',
    data: [
        {'langSelect' : "English" 	,	'langId' : "1"},
		{'langSelect' : "Italian" 	, 	'langId' : "2"},
		{'langSelect' : "Japanese" 	, 	'langId' : "3"},
		{'langSelect' : "Mandarin" 	, 	'langId' : "4"},
		{'langSelect' : "French" 	, 	'langId' : "5"},
		{'langSelect' : "German" 	, 	'langId' : "6"},
		{'langSelect' : "Mongolian" , 	'langId' : "7"},
    ],
});

Ext.define('featuresModel', {
    extend: 'Ext.data.Model',
    fields: [{name: 'languageSelection', type: 'string'}]
});

var featureStore = Ext.create('Ext.data.Store', {
	storeId: 'featuresStore',
    model:  'featuresModel',
    data: [
        {'featureSelect' : "Trailers"},
        {'featureSelect' : "Commentaries"},
		{'featureSelect' : "Deleted Scenes"},
		{'featureSelect' : "Behind the Scenes"}
    ],
});

var addForm = Ext.create('Ext.window.Window', {
	title: 'Add Film',
	id: 'addFormId',
	width: 500,
	closeAction: 'close',
	layout: 'fit',
	items: {
		xtype: 'form',
		bodyPadding: 10,
		layout: 'anchor',
	    defaults: {
	        anchor: '100%'
	    },
		items: [{
			xtype: 'textfield',
			fieldLabel: 'Title',
			id: 'addTitle',
			name: 'title',
			allowBlank: false
		}, {
			fieldLabel: 'Release Year',
			id: 'addYear',
			name: 'release_year',
			xtype: 'numberfield',
			minvalue: 1700,
			maxvalue: 2022,
			allowBlank: false
		}, {
			fieldLabel: 'Special Features',
			id: 'addFeatures',
			name: 'feature',
			store: featureStore,
			displayField: 'featureSelect',
			allowBlank: false,
			xtype: 'tagfield',
          	queryMode: 'local',
          	filterPickList: true
		}, {
			fieldLabel: 'Rating',
			id: 'addRating',
			name: 'rating',
			xtype: 'textfield',
			allowBlank: false
		}, {
			fieldLabel: 'Language',
			id: 'addLang',
			name: 'language',
			store: langStore,
			xtype: 'combobox',
			displayField: 'langSelect',
			queryMode: 'local',
			forceSelection: 'true',
			allowBlank: false
		}, {
			xtype: 'textfield',
			id: 'addDirector',
			fieldLabel: 'Director Name',
			name: 'director',
			allowBlank: false
		}, {
			xtype: 'textfield',
			id: 'addDesc',
			fieldLabel: 'Description',
			name: 'description',
		}],
		buttons: [{
            text: 'Save',
			handler: function(){
                var form = this.up('form').getForm()
                var addFormValues = form.getValues()
				//var addFormEncoded = Ext.encode(addFormValues)
                //console.log("Add Button: " + addFormEncoded)
                if (form.isValid()) {
                    Ext.Ajax.request({
                        url: '/SakilaProject/AddData',
                        params: addFormValues,
                        success: function (response) {
							console.log('Request Passed')
                            movieStore.currentPage = 1;
                            movieStore.load({
                                params: {
                                    start: 0,
                                    limit: 7,
                                }
                            });
							movieGrid.getView().refresh();
							form.reset();
                            addForm.close();
                        },
                        failure: function (response) {
                            Ext.Msg.alert('Request Failed', 'Please Try Again!');
                        }
                    });
                }
            }
        }, {
            text: 'Cancel',
            handler: function(){
                addForm.close();
            }
        }],
        buttonAlign: 'center',
	}
});

var editForm = Ext.create('Ext.window.Window', {
    title: 'Edit',
	width: 500,
	closeAction: 'close',
    items: [{ 
        xtype: 'form',
		id: 'editFormId',
		bodyPadding: 10,
		layout: 'anchor',
	    defaults: {
	        anchor: '100%'
	    },
        items: [{
            xtype : 'textfield',
            fieldLabel: 'Film ID',
            name: 'film_id',
            id: 'editFilmId',
			hidden: true,
        }, {
            xtype : 'textfield',
            fieldLabel: 'Title',
            name: 'title',
            id: 'editTitle'
        }, {
            xtype : 'textfield',
            fieldLabel: 'Description',
            name: 'description',
            id: 'editDescription'
        }, {
            xtype : 'textfield',
            fieldLabel: 'Release Year',
            name: 'release_year',
            id: 'editReleaseYear'
        }, {
            xtype: 'combobox',
            fieldLabel: 'Language',
            store: langStore,
            queryMode: 'local',
            displayField: 'langSelect',
            name: 'language',
            id: 'editLanguage'
        }, {
            xtype : 'textfield',
            fieldLabel: 'Director',
            name: 'director',
            id: 'editDirector'
        }, {
            xtype : 'textfield',
            fieldLabel: 'Rating',
            name: 'rating',
            id: 'editFilmRating'
        }, {
            //xtype : 'textfield',
            fieldLabel: 'Special Features',
            name: 'feature',
            id: 'editSpecialFeatures',

			store: featureStore,
			displayField: 'featureSelect',
			allowBlank: false,
			xtype: 'tagfield',
          	queryMode: 'local',
          	filterPickList: true
			
        }],
        buttons: [{
            text: 'Edit',
			handler: function(){
                var form = this.up('form').getForm()
                var editFormValues = form.getValues()
				//var editFormEncoded = Ext.encode(editFormValues)
				//console.log("Delete Form: " + deleteFormEncoded);
                if (form.isValid()) {
                    Ext.Ajax.request({
                        url: '/SakilaProject/EditData',
                        params: editFormValues,
                        success: function (response) {
							console.log("Request Passed");
							movieStore.currentPage = 1;
                            movieStore.load({
                                params: {
                                    start: 0,
                                    limit: 7,
                                }
                            });
							movieGrid.getView().refresh();
							form.reset();
                            editForm.close();
                        },
                        failure: function (response) {
							console.log("Request Failed")
                            Ext.Msg.alert('Request Failed', 'Please Try Again!');
                        }
                    });
                }
            }
        }, {
            text: 'Cancel',
            handler: function(){
                editForm.hide();
            }
        }],
        buttonAlign: 'center',
    }]
});

var deleteForm = Ext.create('Ext.window.Window', {
    title: 'Delete',
    width: 400,
    html: "<p>Are you sure you want to Delete!?</p>",
    layout: 'fit',
    closeAction: 'close',
    items: [{ 
        xtype: 'form',
        bodyPadding: 10,
        id: 'deleteFormId',
		html: "Are you sure you want to delete the selected records ?",
        items: [{
            xtype : 'textfield',
            fieldLabel: 'Film ID',
            name: 'film_id',
            id: 'deleteFilmId',
			hidden: true,
        }],
        
        buttons: [{
            text: 'Delete',
            iconCls: 'x-fa fa-trash',
            handler: function(){
                Ext.getCmp('deleteFilmId').enable();
                var form = this.up('form').getForm();
                var deleteFormValues = form.getValues()
                //var deleteFormEncoded = Ext.encode(deleteFormValues);
                //console.log("Delete Form: " + deleteFormEncoded);
                if (form.isValid()) {
                    Ext.Ajax.request({
                        url: '/SakilaProject/DeleteData',
                        params: deleteFormValues,
                        success: function (response) {
							console.log("Request Passed")
                            movieStore.currentPage = 1;
                            movieStore.load({
                                params: {
                                    start: 0,
                                    limit: 7,
                                }
                            });
							movieGrid.getView().refresh();
							form.reset();
                            deleteForm.close();
                        },
                        failure: function (response) {
                            Ext.Msg.alert('Request Failed', 'Oops, Please Try Again!');
                        }
                    });
                }
            }
        }, {
            text: 'Cancel',
            iconCls: 'x-fa fa-times',
            handler: function(){
                deleteForm.hide();
            }
        }],
        buttonAlign: 'center',
    }]
});

var movieAdvanceSearch = Ext.create('Ext.form.Panel', {
	renderTo: document.body,
	title: 'Movie Advance Search',
	width: 1280,
	height: 250,
	layout: 'vbox',
	buttonAlign: 'center',
	items: [{
			xtype: 'container',
			layout: 'hbox',
			items: [{
					xtype: 'textfield',
					id: 'movieNameId',
					fieldLabel: 'Movie Name',
					name: 'movieName',
					flex: 1,
					margin: '40 0 0 210'
				},
				{
					xtype: 'textfield',
					id: 'directorNameId',
					fieldLabel: 'Director Name',
					name: 'dirName',
					flex: 1,
					margin: '40 0 0 310'
				}
			]
		},
		{
			xtype: 'container',
			layout: 'hbox',
			items: [{
					xtype: 'datefield',
					format: 'Y',
					id: 'releaseYearId',
					fieldLabel: 'Release Year',
					name: 'releaseDate',
					margin: '15 0 0 210'
				},
				{
					xtype: 'combobox',
					id: 'languageId',
					fieldLabel: 'Language',
					store: langStore,
					displayField: 'langSelect',
					valueField: 'langId',
					name: 'lang',
					margin: '15 0 0 310',
					queryMode: 'local',
					forceSelection: 'true',
				}
			]
		}
	],
	fbar: [{
			type: 'button',
			text: 'Search',
			handler: function () {
				Ext.ComponentQuery.query("#movieGrid")[0].store.proxy.setExtraParam("title",this.up('form').getForm().findField("movieName").getValue());
				Ext.ComponentQuery.query("#movieGrid")[0].store.proxy.setExtraParam("director",this.up('form').getForm().findField("dirName").getValue());
				Ext.ComponentQuery.query("#movieGrid")[0].store.proxy.setExtraParam("release_year",this.up('form').getForm().findField("releaseDate").getValue());
				Ext.ComponentQuery.query("#movieGrid")[0].store.proxy.setExtraParam("language",this.up('form').getForm().findField("lang").getValue());
				Ext.ComponentQuery.query("#movieGrid")[0].store.reload();
			}
		},
		{
			type: 'button',
			text: 'Reset',
			handler: function () {
            	this.up('form').getForm().reset();
				movieStore.clearFilter();
                movieStore.load({
                	params: {
                    	start: 0,
                        limit: 7
                    }
                });
                //this.up('form').getForm().reset();
            }
		}
	],
});

Ext.define('MyGrid', {
	extend: 'Ext.app.ViewModel',
	alias: 'viewmodel.test'
});

var movieGrid = Ext.create('Ext.grid.Panel', {
	title: 'Movie Grid',
	store: movieStore,
	id: 'movieGrid',
	reference: 'mygrid',
	viewModel: {
		type: 'test'
	},
	columns: [
				{text: 'FilmID', dataIndex: 'film_id', hidden: true},
				{text: 'Title', dataIndex: 'title', flex: 0.8},
				{text: 'Description', dataIndex: 'description', flex: 1.8},
				{text: 'Release Year', dataIndex: 'release_year'},
				{text: 'Language', dataIndex: 'language'},
				{text: 'Director', dataIndex: 'director'},
				{text: 'Rating', dataIndex: 'rating'},
				{text: 'Special Features', dataIndex: 'feature', flex: 1}
	],
	width: 1280,
	height: 370,
	renderTo: document.body,
	selModel: {
		selType: 'checkboxmodel'
	},
	listeners: {
        selectionChange: function(){
			if(Ext.ComponentQuery.query('#movieGrid')[0].getSelectionModel().getSelection().length==1){
				Ext.ComponentQuery.query('#editButtonId')[0].enable();
				Ext.ComponentQuery.query('#deleteButtonId')[0].enable();
				var gridData = {}
				var selected = Ext.ComponentQuery.query('#movieGrid')[0].getSelectionModel().getSelection();
				gridData = selected[0].data;
                Ext.getCmp('editFormId').getForm().setValues(gridData);
				Ext.getCmp('deleteFilmId').setValue(gridData.film_id);
			}
			else if(Ext.ComponentQuery.query('#movieGrid')[0].getSelectionModel().getSelection().length>1){
				Ext.ComponentQuery.query('#editButtonId')[0].disable(true);
				Ext.ComponentQuery.query('#deleteButtonId')[0].enable();
				var selected = Ext.ComponentQuery.query('#movieGrid')[0].getSelectionModel().getSelection();
				gridFilmList = [];
                for(let i = 0; i < selected.length; i++) {
                	gridFilmList.push(selected[i].data.film_id)
                }
				Ext.getCmp('deleteFilmId').setValue(gridFilmList);
			}
			else{
				Ext.ComponentQuery.query('#editButtonId')[0].disable(true);
				Ext.ComponentQuery.query('#deleteButtonId')[0].disable(true);
			}
		},
    },
	dockedItems: [{
		xtype: 'pagingtoolbar',
		store: movieStore,
		dock: 'top',
		displayInfo: true,
		inputItemWidth: 45,
		items: [
			'|', {
				xtype: 'button',
				text: 'Add',
				id: 'addButtonId',
				iconCls: 'fa fa-plus-circle',
				handler: function () {
					addForm.show();
				},
			},
			'|', {
				xtype: 'button',
				text: 'Edit',
				id: 'editButtonId',
				iconCls: 'fa fa-edit',
				disabled: true,
				handler: function () {
					editForm.show();
				},
			},
			'|', {
				xtype: 'button',
				text: 'Delete',
				id: 'deleteButtonId',
				iconCls: 'fa fa-trash',
				disabled: true,
				handler: function () {
					deleteForm.show();
				}
			}, '|'
		]
	}]
});

Ext.application({
	name: 'HighRadiusMovieDatabase',
	launch: function () {
		Ext.create('Ext.container.Viewport', {
			items: [movieAdvanceSearch, movieGrid]
		});
	}
});