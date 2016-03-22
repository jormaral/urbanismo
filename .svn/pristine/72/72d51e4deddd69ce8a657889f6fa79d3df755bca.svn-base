var dictionaryAdministrationGrid; //Grid de administracion del diccionario de datos
var dictionaryAdministrationForm; //Datos de los diccionarios, es un grid, pero al tratarse de la gestion se coloca el FORM
var idDictionarytAdministrationSelected = -1; //indica cual es el id del diccionario actualmente seleccionado
var modalWindowDictionaryAdministration;

function initGestionDiccionario(){
	//$('LeftSideColumn').addEvent('OnResize',resizeDictionaryAdministration);	
	//$('LeftSideColumn').ResizeEvent = resizeDictionaryAdministration;
	
	warnNoUserSelected();
	
	// Setup user grid
    var cmu = [
    {
        header: "Diccionario",
        dataIndex: 'name',
        dataType:'string',
        width: parseInt($('LeftSideColumn').getStyle('width')) - 4
    }];

    dictionaryAdministrationGrid = new omniGrid('panelIzdoDic', {
        columnModel: cmu,
        //url: 'GestionConsola?wsName=GET_LISTA_USUARIOS',
        pagination:false,
        serverSort:false,
        showHeader: true,
        alternaterows: true,
        sortHeader:true,
        resizeColumns:true,
        multipleSelection:false,
        width: '100%', //parseInt($('LeftSideColumn').getStyle('width')) - 40,
        height: parseInt($('LeftSideColumn').getStyle('height')) // - 20
    });

    dictionaryAdministrationGrid.setData(dictionaryAdministrationNames);
    
    dictionaryAdministrationGrid.addEvent('click', onSelectDictionaryAdministration);        
    
    $('LeftSideColumn').fade(1);
}

function onSelectDictionaryAdministration(evt){				
	
    var id = evt.target.getDataByRow(evt.row).id;

    if (id)
    {
    	if (idDictionarytAdministrationSelected != id){
    		idDictionarytAdministrationSelected = id;
        	
        	$('panelDatosDiccionario').setStyle('display','none');
        	if ($('editform')) {$('editform').empty();}
        	
        	removeWarnNoUserSelected();
        	
        	dictionaryAdministrationForm = new GestDiccionario(id, {
                parentid: 'panelDchoDic',
                idioma: 'es'
            });
        	
        	dictionaryAdministrationForm.display();
    	}
    }
    else 
    {
        warnNoUserSelected();        
    }        
}

function warnNoUserSelected(){
	$('mainColumn').setOpacity(0);
}

function removeWarnNoUserSelected(){
	$('mainColumn').fade(1);
}

/*******************************************************************************
 *      Gestion de diccionarios
 ******************************************************************************/
GestDiccionario = new Class({
    Implements: [Events, Options],

    // Private vars
    options: {
        parentid: 'formdiv',
        idioma: 'es'
    },
    formdiv: null,
    griddiv: null,
    grid: null,

    columnmodel: [],
    selectioncolumns: {},   // this.selectioncolumns[selectcol.dataIndex] = virtualcol
    virtualcolumns: {},     // this.virtualcolumns[virtualcol.dataIndex] = selectcol
    namecache: {}, // this.namecache[colrefType][entity_id] = name (string) --> para interpolación de nombres
    columnoptions: {}, // this.columnoptions[col.refType] = {data rows of refType} --> para generación de opciones
    data:{},
    tipo: '',
    esnuevoelemento: false,
    textChanged: false,

    /*----------------------------
     * API Methods
     *---------------------------*/

    // API - Constructor
    initialize: function(tipo, options){

        // get grid data
        var griddata = runServiceJson({wsName: 'GET_DATA', 'TIPO': tipo, idioma: this.options.idioma});

        // get column model
        var cm = undefined;
        switch(tipo){
        	case 'Boletin':
        		cm = dictionarytypeBoletin;
        		break;
        	case 'CaracterDeterminacion':
        		cm = dictionaryTypeCaracterDeterminacion;
        		break;
        	case 'CentroProduccion':
        		cm = dictionaryTypeCentroProduccion;
        		break;
        	case 'GrupoDocumento':
        		cm = dictionaryTypeGrupoDocumento;
        		break;
        	case 'InstrumentoPlan':
        		cm = dictionaryTypeInstrumentoPlan;
        		break;
        	case 'InstrumentoTipoOperacion':
        		cm = dictionaryTypeInstrumentoTipoOperacion;
        		break;
        	case 'Naturaleza':
        		cm = dictionaryTypeNaturaleza;
        		break;   
        	case 'OperacionCaracter':
        		cm = dictionaryTypeOperacionCaracter;
        		break;
        	case 'Organo':
        		cm = dictionaryTypeOrgano;
        		break;
        	case 'TipoDocumento':        		
        		cm = dictionaryTypeTipoDocumento;
        		break;
        	case 'TipoOperacionPlan':
        		cm = dictionaryTypeTipoOperacionPlan;
        		break;
        	case 'TipoTramite':
        		cm = dictionaryTypeTipoTramite;
        		break;        		
        }

        if (cm==undefined){

            new PopupMsg('No hay datos del tipo ' + tipo,{type: 'ok' });
            return;
        }

        this.columnmodel = cm;
        this.data = griddata;
        this.options.parentid = options.parentid;
        this.tipo = tipo;
    },

    // API    
    display: function(){

        //Setup grid

        // si ya hay un grid borrarlo
        if ($('dicgrid')){
            $(this.options.parentid).empty();
        }

        this.griddiv = new Element('div',{
            'id': 'dicgrid',
            'class': 'gd-grid'
        });

        $(this.options.parentid).grab(this.griddiv);

        //this._hideIdColumns(); //Esconde la columna id para cada uno de los diccionarios

        this._createNamesColsForSelections(); //Crea las columnas para el diccionario definido

        this._extrapolateNamesInDataForSelections();

        this.grid = new omniGrid('dicgrid',{
            columnModel: this.columnmodel,
            /*
            buttons:[
            {
                name: 'Añadir',
                bclass: 'addButtonGridRefundido',
                onclick: this._onGridAddClick.bind(this)
            }
            ],*/
            pagination:false,
            serverSort:false,
            showHeader: true,
            alternaterows: true,
            sortHeader:true,
            resizeColumns:true,
            multipleSelection:false,
            width: '100%', //parseInt($('mainColumn').getStyle('width')) - 40,
            height: parseInt($('mainColumn').getStyle('height')) - 200
        });

        this.grid.setData(this.data);
        
        //Habilito solo el boton de nuevo
        enableControl('btnDictionaryAnadir');
        disableControl('btnDictionaryModify');
        disableControl('btnDictionaryRemove');
        disableControl('btnDictionarySave');   
        
        $('btnDictionaryAnadir').addEvent('click', this._onGridAddClick.bind(this));
        
        this.grid.addEvent('click',this._onGridSelChanged.bind(this));
    },


    /*----------------------------
     * Private Methods
     *---------------------------*/

    //Muestra el formulario de datos segun la seleccion en el grid
    _displayForm: function(itemdata){

        // Setup form

        //limpio el formulario
    	if ($('editform')){
    		
    		$('editform').empty();
    		
    		this.formdiv = $('editform'); 
    	}
    	else{
    		this.formdiv = new Element('div',{
        		'id':'editform'
        	});
    		
    		this.formdiv.inject($('editformWraper'));
    	}                                      

        for(var i=0, len = this.columnmodel.length; i<len; i++){
            var c = this.columnmodel[i];
            if (!c.isVirtual && (c.isSelection == undefined || c.isSelection == false)){
                if (itemdata){
                    this._insertInput(c, itemdata[c.dataIndex], false); //Pongo HasData a false para que siempre permita modificar
                } else {
                    this._insertInput(c, null, false);
                }
            } else if(c.isVirtual){
                var selcol = this.virtualcolumns[c.dataIndex];

                if (itemdata){
                    this._insertSelect(c, itemdata[selcol.dataIndex], false); //Pongo HasData a false para que siempre permita modificar
                } else {
                    this._insertSelect(c, null, false);

                }
            }
        }
        
        if(this.tipo=="CentroProduccion" ){        	        	        
            this._insertInputPass();
        }

        this._insertButtons(itemdata==null || itemdata== undefined);
        
        $('panelDatosDiccionario').setStyle('display', 'block');
    },
    
    _insertInput: function(cmi, value, hasData){
        // cmi --> Column Model Item
    	/*
    	 	<div class="formRow">
			<div class="formLabel">Nombre:</div>
			<div class="formField"><input type="text" class="input" id="txtNombre" value="" maxlength="50" /></div>
			<div class="clear"></div>
			</div>
    	 * */

        // campo identificador --> campo oculto
        var oculto = (cmi.header.toLowerCase() === "id" || cmi.header.toLowerCase() === "iden" || cmi.header.toLowerCase() === "codigo");
        
        var toInsert;
        var input; //lo defino aqui porque se usa fuera de los ifs
        
        if (!oculto){
        	//elemento principal
        	toInsert = new Element('div',{
        		'class':'formRow'
        	});
        	//label
        	var label = new Element('label',{
                'for': cmi.dataIndex,
                'class':'formLabel',
                'html': this._initCap(cmi.header.toLowerCase()) + ': '
            });
        	//formField
        	var formField = new Element('div',{
        		'class':'formField'
        	});
        	//input
        	if (hasData){
        		input = new Element('label', {
                    name: cmi.dataIndex,
                    id: cmi.dataIndex,
                    'html': value,
                    'class': 'imputLabel'
                });
        	} else {
        		input = new Element('input', {
                    type: 'text',
                    name: cmi.dataIndex,
                    id: cmi.dataIndex,
                    value: value,
                    'class': 'input',
                    'maxlength':'100'
                });
        	}  	
        	//input.setStyle('width',parseInt($('mainColumn').getStyle('width')) - 230 + 'px');
        	input.setStyle('width','100%');
        	//inserto el inputp en el formField
        	input.inject(formField);
        	//div clear
        	var divClear = new Element('div',{
        		'class':'clear'
        	});
        	
        	//inserto el label en el principal
        	label.inject(toInsert);
        	//inserto el field en el principal
        	formField.inject(toInsert);
        	//inserto el clear en el principal
        	divClear.inject(toInsert);
        }
        else{
        	input = new Element('input', {
                type: 'hidden',
                name: cmi.dataIndex,
                id: cmi.dataIndex,
                value: value,
                'class': 'input',
                'maxlength':'100'
            });
        	input.setStyle('width',parseInt($('mainColumn').getStyle('width')) - 230 + 'px');
        	toInsert = input;
        }

        if(cmi.editable === false && value != null){
            input.setProperty('readonly','true');
        }
        input.addEvent('change', this._onChangeText.bind(this));

        toInsert.inject(this.formdiv);
    },
    
    _insertSelect: function(cmi, value, hasData){
        var selcol = this.virtualcolumns[cmi.dataIndex];
        
        //elemento principal
    	var toInsert = new Element('div',{
    		'class':'formRow'
    	});
    	//label
    	var label = new Element('label',{
            'for': selcol.dataIndex,
            'class':'formLabel',
            'html': this._initCap(selcol.header.toLowerCase()) + ': '
        });
    	//formField
    	var formField = new Element('div',{
    		'class':'formField'
    	});
    	//input
    	var select;
    	if (hasData) {
    		select = new Element('label', {
                name: selcol.dataIndex,
                id: selcol.dataIndex,
                'html': value,
                'class': 'imputLabel'
            });
    	} else {
    		select = new Element('select', {
                name: selcol.dataIndex,
                id: selcol.dataIndex,
                value: value,
                'class': 'select'
            });
    	}    	
    	select.addEvent('change', this._onChangeText.bind(this));
    	
    	//select.setStyle('width',parseInt($('mainColumn').getStyle('width')) - 230 + 'px');
    	select.setStyle('width','100%');
    	//inserto el select en el formField
    	select.inject(formField);
    	//div clear
    	var divClear = new Element('div',{
    		'class':'clear'
    	});
    	
    	//inserto el label en el principal
    	label.inject(toInsert);
    	//inserto el field en el principal
    	formField.inject(toInsert);
    	//inserto el clear en el principal
    	divClear.inject(toInsert);

    	if (hasData){
    		/*if(this.columnoptions[selcol.refType]){
                var options = this.columnoptions[selcol.refType];
                for (var optidx=0, optlen=options.length; optidx<optlen; optidx++){
                    var optdata = options[optidx];
                    if (optdata[selcol.refIdCol] == value){
                    	select.set('html',optdata[selcol.refNameCol]);
                    	break;
                    }
                }
            } */   		    		
    	} else {
    		if(this.columnoptions[selcol.refType]){
                var options = this.columnoptions[selcol.refType];
                for (var optidx=0, optlen=options.length; optidx<optlen; optidx++){
                    var optdata = options[optidx];
                    var opelem = new Element('option',{
                        id: optdata[selcol.refIdCol],
                        value: optdata[selcol.refIdCol],
                        html: optdata[selcol.refNameCol]
                    });
                    select.grab(opelem);
                }
                
                var selected = value;
                
                for(var rowidx=0,len=select.options.length; rowidx<len; rowidx++){
    	            
    	            var rowValue = select.options[rowidx];
    	            
    	            if (selected && (selected == rowValue.value || selected == rowValue.text)){
    	            	select.selectedIndex = rowidx;
    	            	break;
                	}
    			}
            }
    	}        

        toInsert.inject(this.formdiv);
    },

    _insertInputPass: function(){        
	    	/*
		 	<div class="formRow">
			<div class="formLabel">Nombre:</div>
			<div class="formField"><input type="text" class="input" id="txtNombre" value="" maxlength="50" /></div>
			<div class="clear"></div>
			</div>
		 * */
    	//Input para password
    	var toInsert;
    
    	//elemento principal
    	toInsert = new Element('div',{
    		'class':'formRow'
    	});
    	//label
    	var label = new Element('label',{
            'for': 'password',
            'class':'formLabel',
            'html': 'Nueva clave'
        });
    	//formField
    	var formField = new Element('div',{
    		'class':'formField'
    	});
    	//input
    	var input = new Element('input', {
            type: 'password',
            name: 'password',
            id: 'password',
            'class': 'input',
            'maxlength':'20'
        });
    	//input.setStyle('width',parseInt($('mainColumn').getStyle('width')) - 230 + 'px');
    	input.setStyle('width','100%');
    	//inserto el inputp en el formField
    	input.inject(formField);
    	//div clear
    	var divClear = new Element('div',{
    		'class':'clear'
    	});
    	
    	//inserto el label en el principal
    	label.inject(toInsert);
    	//inserto el field en el principal
    	formField.inject(toInsert);
    	//inserto el clear en el principal
    	divClear.inject(toInsert);
    	
    	input.addEvent('change', this._onChangeText.bind(this));
    	toInsert.inject(this.formdiv);
    	

        //Input para repetir password
    	//elemento principal
    	toInsert = new Element('div',{
    		'class':'formRow'
    	});
    	//label
    	label = new Element('label',{
            'for': 'passwordRepeat',
            'class':'formLabel',
            'html': 'Repita la clave'
        });
    	//formField
    	formField = new Element('div',{
    		'class':'formField'
    	});
    	//input
    	input = new Element('input', {
            type: 'password',
            name: 'passwordRepeat',
            id: 'passwordRepeat',
            'class': 'input',
            'maxlength':'20'
        });
    	//input.setStyle('width',parseInt($('mainColumn').getStyle('width')) - 230 + 'px');
    	input.setStyle('width','100%');
    	//inserto el inputp en el formField
    	input.inject(formField);
    	//div clear
    	divClear = new Element('div',{
    		'class':'clear'
    	});
    	
    	//inserto el label en el principal
    	label.inject(toInsert);
    	//inserto el field en el principal
    	formField.inject(toInsert);
    	//inserto el clear en el principal
    	divClear.inject(toInsert);

        input.addEvent('change', this._onChangeText.bind(this));
        toInsert.inject(this.formdiv);
    },

    _hideIdColumns: function(){
        if (this.columnmodel==null){
            return;
        }

        for(var colidx=0, len=this.columnmodel.length; colidx<len; colidx++){
            var col = this.columnmodel[colidx];
            if (col!=null){
                if (col.header.toLowerCase() == "id"){
                    col.hidden=true;
                }
            }
        }

    },

    /*
     * Gestionar columnas de relacion con otras entidades
     */
    _createNamesColsForSelections: function(){
        if (this.columnmodel == null){
            return;
        }
        this.namecache = {};
        this.columnoptions = {};

        // new columns to store the names of the selectable data id's
        var addedcols = [];
        this.selectioncolumns = {};
        
        for(var colidx=0, len=this.columnmodel.length; colidx<len; colidx++){
            var col = this.columnmodel[colidx];

        	if (col.isSelection == false || col.isSelection == undefined){
        		addedcols.push(col);
        	} else {
        		 // Here we only deal with columns with isSelection = true
                var newCol = {
                    header: col.header,
                    dataType: 'string',
                    dataIndex: col.dataIndex, // + '_name',
                    isSelection: false,
                    isVirtual: true,
                    hidden: false,
                    width: 200
                };
                addedcols.push(newCol);

                this.selectioncolumns[col.dataIndex] = newCol;
                this.virtualcolumns[newCol.dataIndex] = col;

                //cache column's possible values for name interpolation and form's option generation
                var coldata= runServiceJson({wsName: 'GET_DATA',TIPO: col.refType});
                
                if (coldata != null) {
                    if (coldata.data != null){
                        coldata = coldata.data;
                    }
                    
                    this.columnoptions[col.refType] = coldata;
                    this.namecache[col.refType] = {};
                    
                    for (var dataidx=0, datalen=coldata.length; dataidx<datalen; dataidx++){
                        var entity_id = coldata[dataidx][col.refIdCol];
                        var entity_name = coldata[dataidx][col.refNameCol];
                        this.namecache[col.refType][entity_id] = entity_name;
                    }
                }
        	}        
        }
        
        if (addedcols.length > 0){
        	this.columnmodel = [];      	 
            
            for(colidx=0, len = addedcols.length; colidx<len; colidx++){
                this.columnmodel.push(addedcols[colidx]);
            }
        }                          
    },

    _extrapolateNamesInDataForSelections: function(){
        for (var colidx=0, collen=this.columnmodel.length; colidx<collen; colidx++){
            var vircol = this.columnmodel[colidx];

            //Skip non virtual columns
            if (vircol == null || vircol.isVirtual == false || vircol.isVirtual == undefined){
                continue;
            }

            // get virtual column metadata stored in the respective selection column
            var selcol = this.virtualcolumns[vircol.dataIndex];

            for (var dataidx=0, datalen=this.data.data.length; dataidx<datalen; dataidx++){
                var row = this.data.data[dataidx];
                var entity_id = row[selcol.dataIndex];

                // If we've already asked for the same name do not retreive it again
                if (this.namecache[selcol.refType] != null && this.namecache[selcol.refType][entity_id] != null){
                    this.data.data[dataidx][vircol.dataIndex] = this.namecache[selcol.refType][entity_id];
                }
            } // end for rows

        } // end for columns
    },


    /**
     *@param nuevo boolean
     */
    _insertButtons: function(nuevo){
        this.esnuevoelemento = nuevo;


        // Boton modificar. Descomentar para habilitar la modificacion de
        // elementos del diccionario
        if (nuevo){
        	
        	//habilito el boton GUARDAR
        	enableControl('btnDictionaryAnadir');
        	disableControl('btnDictionaryModify');
        	disableControl('btnDictionaryRemove');
            enableControl('btnDictionarySave');
            
        	$('btnDictionarySave').removeEvents('click');
        	$('btnDictionarySave').addEvent('click', this._onSubmitClick.bind(this));
        	
        	/*
            var add = new Element('div',{ //BOTON CREAR
                'class': 'add',
                'html': 'Crear'
            });
            add.addEvent('click', this._onSubmitClick.bind(this));
            this.formdiv.grab(add);*/
        } else { //boton BORRAR o MODIFICAR
        	
        	enableControl('btnDictionaryAnadir');
    		enableControl('btnDictionaryModify');
    		disableControl('btnDictionaryRemove');
            disableControl('btnDictionarySave');
        	        	     
            /*
        	if (this.tipo== "CentroProduccion"){
        		enableControl('btnDictionaryAnadir');
        		enableControl('btnDictionaryModify');
        		enableControl('btnDictionaryRemove');
        		enableControl('btnDictionarySave');
                
        		$('btnDictionarySave').removeEvents('click');
            	$('btnDictionarySave').addEvent('click', this._onUpdateClick.bind(this));
        	} else {
        		enableControl('btnDictionaryAnadir');
        		enableControl('btnDictionaryModify');
        		enableControl('btnDictionaryRemove');
                disableControl('btnDictionarySave');
        	}*/
        	        	       	
        	$('btnDictionaryModify').removeEvents('click');
       	 	$('btnDictionaryModify').addEvent('click', this._onGridSelChanged.bind(this));       	 	 	         	        	        	
        	
        	/*        	
            var del = new Element('div',{
                'class': 'delete',
                'html': 'Borrar'
            });            
            del.addEvent('click', this._onDeleteClick.bind(this));
            this.formdiv.grab(del);
            
            if (this.tipo== "CentroProduccion"){ 
                var upload = new Element('div',{ //BOTON MODIFICAR
                    'class': 'update',
                    'html': 'Actualizar'
                });
                upload.addEvent('click', this._onUpdateClick.bind(this));
                this.formdiv.grab(upload);
            }*/
        }
    },


    _showErrors: function(){

    },

    _showConfirmation: function(){

    },

    _validForm: function(){
        if(this.tipo!="OperacionCaracter" && this.tipo!="InstrumentoTipoOperacion"){
            this.frm_val = new formVal('editform');
            this.frm_val.clearErrorsFromDiv();

            if($("nombre").value == ""){
                this.frm_val.addError($('nombre'), 'El nombre no puede estar vacio');
            }
            
            if(this.tipo=="CentroProduccion"){
            	/*
                if($("mail").value == ""){
                    this.frm_val.addError($('mail'), 'El mail no puede estar vacio');
                }else{
                    if(!(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test($("mail").value))){
                        this.frm_val.addError($('mail'), 'El mail introducido no es correcto');
                    }
                }*/

                if(($("password").value == "") && ($("passwordRepeat").value == "")){
                    this.frm_val.addError($('password'), 'Debe introducir una password');
                }else{
                    if($("password").value.length<6){
                        this.frm_val.addError($('password'), 'La password debe tener al menos 6 caracteres');
                    }
                }

                if(($("passwordRepeat").value == "")&& !($("password").value == "" )){
                    this.frm_val.addError($('passwordRepeat'), 'Debe introducir la password en el segundo campo');
                }

                if(!($("password").value == $("passwordRepeat").value)& $("passwordRepeat").value !=""){
                    this.frm_val.addError($('passwordRepeat'), 'La password debe ser la misma');
                }

                return this.frm_val.isValid();

            }

        }
        return true;

    },

    _validName: function(){
        if((this.tipo!="OperacionCaracter" && this.tipo!="InstrumentoTipoOperacion")){
            var aux = this.data.data;
            this.frm_val = new formVal('editform');
            this.frm_val.clearErrorsFromDiv();
            for(var i=0,len=aux.length; i<len; i++){
                if($('nombre').value.toLowerCase() == aux[i].nombre.toLowerCase()){
                    this.frm_val.addError($('nombre'), 'Este nombre ya existe.');
                    return false;
                }
            }
        }
        return true;
    },

    _getDataFromForm: function(){
        var res = {};
        for (var i=0, len=this.columnmodel.length; i<len; i++){
            var fieldname = this.columnmodel[i].dataIndex;
            if($(fieldname)){
            	if ($(fieldname).value != undefined){
            		res[fieldname] = $(fieldname).value;
            	} else
            		res[fieldname] = $(fieldname).get('html');             
            }
        }
        if(this.tipo =="CentroProduccion"){//Recogemos la password de centro de produccion porque no esta en el columnModel
            res["password"] = $("password").value;
            //res["codigo"] = "";
        }
        return res;
    },

    _getIdenFromForm: function(){
        var res;
        var elems = $$('input[type=hidden]');

        if (elems){
            res = elems[0].id;
        }
        return res;
    },

    _initCap: function (text) {
        return text.substr(0, 1).toUpperCase() + text.substr(1);
    },

    _updateGrid: function(){
        this.initialize(this.tipo, this.options);

        // quitar el formulario del elemento actual ya que no hay nignun
        // elemento seleccionado en el grid
        $('panelDatosDiccionario').setStyle('display', 'none');

        this.display();
    },




    /*----------------------------
     * Events
     *---------------------------*/

    _onSubmitClick: function(){
    	if ($('btnDictionarySave').disabled == false){
    		//valida los campos del formulario
            if(this._validForm()){
                //funcion comprueba que el nombre no existe
                if(this._validName()){
                    var data = this._getDataFromForm();
                    var result = runService({
                        wsName: this.esnuevoelemento?'ADD_ITEM_DIC':'MOD_ITEM_DIC',
                        tipo: this.tipo,
                        data: JSON.encode(data),
                        idioma: this.options.idioma,
                        id: data[this._getIdenFromForm()]
                    });

                    new PopupMsg(result, {type: 'ok'});

                    this._updateGrid();
                }
            }
    	}        
    },
    
    _onModifyClick: function(){
    	if ($('btnDictionaryModify').disabled == false){
    		var sel = this.grid.getData()[this.grid.selected[0]];
            this._displayForm(sel); //Muestra el formulario en funcion del objeto seleccionado
            
            enableControl('btnDictionaryRemove');
            enableControl('btnDictionarySave');
            
            $('btnDictionaryRemove').removeEvents('click');
        	$('btnDictionaryRemove').addEvent('click', this._onDeleteClick.bind(this));
            
    		$('btnDictionarySave').removeEvents('click');
        	$('btnDictionarySave').addEvent('click', this._onUpdateClick.bind(this));
    	}        
    },

    _onDeleteClick: function(){
    	if ($('btnDictionaryRemove').disabled == false){
    		if (confirm('Se borrará el elemento. ¿Desea continuar?'))
            {
                var data = this._getDataFromForm();
                var result = runService({
                    wsName: 'DEL_ITEM_DIC',
                    data: JSON.encode(data),
                    tipo: this.tipo,
                    idioma: this.options.idioma,
                    id: data[this._getIdenFromForm()]
                });
                
                new PopupMsg(result,{type: 'ok'});

                this._updateGrid();
            }
    	}        
    },

    _onUpdateClick: function()
    {
    	if ($('btnDictionarySave').disabled == false){
    		if(this.textChanged){
                if(this._validForm()){
                    if (confirm('Se va a modificar la informacón. ¿Desea continuar?')){
                        this.textChanged = false;
                        var data = this._getDataFromForm();
                        var result = runService({
                            wsName: 'MOD_ITEM_DIC',
                            data: JSON.encode(data),
                            tipo: this.tipo,
                            idioma: this.options.idioma,
                            id: data[this._getIdenFromForm()]
                        });
                        
                        	new PopupMsg(result,{type: 'ok'});
                        
                        this._updateGrid();
                    }
                }
            }else{
                	new PopupMsg("No ha realizado ninguna modificación",{
                    type: 'ok'
                });
            }
    	}        
    },
    _onChangeText :function()
    {
        this.textChanged = true;
    },

    _onGridAddClick: function(){
    	if ($('btnDictionaryAnadir').disabled == false){
    		this._displayForm(null);
    	}        
    },


    _onGridDelClick: function(){
        alert('del clicked');

    },

    _onGridSelChanged: function(){
        if (this.grid.selected && this.grid.selected.length>0){
        	
        	$('panelDatosDiccionario').setStyle('display','none');
        	if ($('editform')) {$('editform').empty();}
        	
        	enableControl('btnDictionaryAnadir');
    		enableControl('btnDictionaryModify');
    		disableControl('btnDictionaryRemove');
            disableControl('btnDictionarySave');        	        	
        	
        	$('btnDictionaryModify').removeEvents('click');
       	 	$('btnDictionaryModify').addEvent('click', this._onModifyClick.bind(this));          
        }
    }
});