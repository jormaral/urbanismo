var refundidoTree; //Arbol del refundido
var refundidoGrid; //Grid de datos del refundido
var confirmAddTramitesAmbitoRefundido;
var confirRefundirMsg;

function initRefundido()
{
	var anchoPrincipal = 630;
	if (document.body && document.body.offsetWidth) {
		anchoPrincipal = document.body.offsetWidth;
	}
	if (document.compatMode=='CSS1Compat' &&
	    document.documentElement &&
	    document.documentElement.offsetWidth ) {
		anchoPrincipal = document.documentElement.offsetWidth;
	}
	if (window.innerWidth && window.innerHeight) {
		anchoPrincipal = window.innerWidth;
	}
	
	$('panelNuevoRefundido').setStyle('height',(parseInt($('main-panel').getStyle('height')) - 2)+'px');
		
	if ($('main-panel_pad')){
		$('main-panel_pad').setStyle('padding', '2px');
	}	
	
    //Columns definition
    new MUI.Column({
    	container: 'panelNuevoRefundido',
        id: 'LeftSideColumnRefundido',
        placement: 'left',
        width: 300,
        resizeLimit: [200, parseInt(anchoPrincipal) - 40],
        sortable: false
    });
	
    //Dos paneles, uno a la izquierda y otro central
    new MUI.Column({
    	container: 'panelNuevoRefundido',
        id: 'mainColumnRefundido',
        placement: 'main',
        sortable: false
    });
	
	 //Arbol de refundido 
    new MUI.Panel({
        id: 'left-panel-refundido',
        title: '',
        contentURL: 'Pages/Refundido/TreeRefundido.html',
        column: 'LeftSideColumnRefundido',
        //scrollbars : false,
        collapsible: false,
        header: true,
        headerToolbox: true,
		headerToolboxURL: 'Pages/Refundido/TreeRefundidoToolBox.html',
		headerToolboxOnload: function(){
			if ($('BtnRefundidoExpandir')){
		    	$('BtnRefundidoExpandir').addEvent('click',  function () { 
		    		//visorPLANESTree.expand(); ESTO SERIA UN EXPAND ALL
		    		if (refundidoTree.selected){
		    			refundidoTree.selected.expand();
		    		}
				});
		    	$('BtnRefundidoContraer').addEvent('click',  function () { refundidoTree.collapse(); });
		    	$('BtnVisorRefundidoBuscar').addEvent('click',  BtnVisorRefundidoBuscar);    	    	
		    }
		},
		onContentLoaded: function(){
			//GENERO EL ARBOL
			refundidoTree = new MooTreeControl({
		        div: 'treeRefundido',
		        mode: 'folders',
		        treeType: 'planes',
		        iconBar: ['javascriptsV2/mooTree2/Planes.gif'],
		        grid: true,
		        onSelect: function(node, state) {
		            onSelectNodeRefundidoTree(node,state);
		        }
		    },{
		        text: cargarTextoSegunIdioma('Planes'),
		        open: true
		    });
			
			$('treeRefundido').setStyle('height','98%');//parseInt($('LeftSideColumn').getStyle('height'))  - 14 + 'px');
		    $('treeRefundido').setStyle('width','98%'); //parseInt($('LeftSideColumn').getStyle('width')) - 9 + 'px');

			refundidoTree.root.load('GestionConsola?wsName=GET_NODOS_AMBITOS&op='+escape(globalConfig.op_refundido)+'&tipo='+viewerTreeType.ref+'&idioma='+idioma);								
			
			MUI.rWidth();
		}
    });
	
    //Panel de datos de refundido
    new MUI.Panel({
        id: 'main-panel-refundido',
        contentURL: 'Pages/Refundido/DataRefundido.html',
        column: 'mainColumnRefundido',
        scrollbars : false,
        collapsible: false,
        header: false,
		onContentLoaded: function(){
			
			if ($('main-panel-refundido_pad')){
				$('main-panel-refundido_pad').setStyle('padding', '2px');
			}			
			
			disableControl('BtnRefundirAdd');
		    
			setupDataRefundido();									
			
			MUI.rWidth();
		}        
    });		
    
    //Setup tabs
    $('panelRefundidosRealizados').set('left', '0px');
    $('panelRefundidosRealizados').set('top', '0px');
    $('panelRefundidosRealizados').setStyle('display', 'none');
    
    $('panelRefundidosRealizados').setStyle('height',(parseInt($('main-panel').getStyle('height')) - 2)+'px');
	
	//$('LeftSideColumn').fade(1);
	$('mainColumn').fade(1);
}

function setupDataRefundido(){
	
	//Creo los paneles horizontales de datos
	var altoPrincipal = $('mainColumnRefundido').getStyle('height');
	var anchoPrincipal = $('mainColumnRefundido').getStyle('width');
	//var mitadAnchoPrincipal = 200;
	/*
	if (parseInt(anchoPrincipal) % 2 == 0){
		mitadAnchoPrincipal = parseInt(anchoPrincipal) / 2);
	} else {
		mitadAnchoPrincipal = (parseInt(anchoPrincipal) + 1) / 2;
	}*/
	
	$('gridLogDataREFUNDIDO').setStyle('height',(parseInt(parseInt(altoPrincipal) - parseInt($('formDataREFUNDIDO').getStyle('height')) - 10 )) + 'px');		
	
	new MUI.Column({
    	container: $('gridLogDataREFUNDIDO'),
        id: 'gridColumnDataREFUNDIDO',
        placement: 'left',  
        width: parseInt(parseInt(anchoPrincipal) / 2),
        resizeLimit: [200, parseInt(anchoPrincipal) - 40],
        sortable: false,
        isCollapsed: false
    });
	
	new MUI.Column({
    	container: $('gridLogDataREFUNDIDO'),
        id: 'logColumnDataREFUNDIDO',
        placement: 'main',
        //width: 300,
        //resizeLimit: [200, parseInt(anchoPrincipal) - 40],
        sortable: false,
        isCollapsed: false
    });	
	
	//GRID DE DATOS DE REFUNDIDO
    new MUI.Panel({
		id: 'gridDataREFUNDIDO',
		title: '',
		contentURL: 'Pages/Refundido/GridDataRefundido.html',
		column: 'gridColumnDataREFUNDIDO',
		collapsible: false,
		scrollbars: true,
		header: true,
		headerToolbox: true,
		headerToolboxURL: 'Pages/Refundido/DataToolBoxRefundido.html',
		headerToolboxOnload: function(){
			$('BtnRefundirAdd').addEvent('click', function(e){
				onBtnAddClick($('BtnRefundirAdd'), refundidoGrid);
			});
			$('BtnRefundirRemove').addEvent('click', function(e){
				onBtnRemoveClick($('BtnRefundirRemove'), refundidoGrid);
			});
			$('BtnRefundirClean').addEvent('click', function(e){
				onBtnCleanClick($('BtnRefundirClean'), refundidoGrid);
			});
			$('BtnRefundirRefundir').addEvent('click', function(e){
				onBtnRefundirClick($('BtnRefundirRefundir'), refundidoGrid);
			});			
			
			enableControl('BtnRefundirRefundir');  
	        enableControl('BtnRefundirAdd');
	        enableControl('BtnRefundirRemove');
	        enableControl('BtnRefundirClean');	 
	        //enableControl('chkCrearTramite');
		},
		require: {		        	
			onload: function(){
				
				if ($('gridDataREFUNDIDO'+'_pad')){
					$('gridDataREFUNDIDO'+'_pad').setStyle('padding','2px');
				}
				
				// AQUI CARGO EL GRID DE TRAMITES A REFUNDIR
				refundidoGrid = new omniGrid('gridDataREFUNDIDO', {
			        columnModel: refundidoGridColumns,
			        /*
			        buttons : [					
					{name: 'Añadir', bclass: 'addButtonGridRefundido', onclick : onBtnAddClick},
			        {name: 'Quitar', bclass: 'deleteButtonGridRefundido', onclick : onBtnRemoveClick},
			        {separator: true},
			        {name: 'Refundir', bclass: 'refundirButtonGridRefundido', onclick : onBtnRefundirClick}										
					],*/
			        pagination:false,
			        serverSort:false,
			        showHeader: true,
			        alternaterows: true,
			        resizeColumns:true,
			        multipleSelection:false,
			        sortOn: null,
			        sortBy: 'ASC',
			        width: '500', //$('gridDataREFUNDIDO').getStyle('width'),
			        height: $('gridDataREFUNDIDO').getStyle('height') //'100%'//(parseInt(parseInt(altoPrincipal) - parseInt($('formDataREFUNDIDO').getStyle('height')) )) + 'px'
			    });
				
			}
        }
	});
    
	//LOG DE REFUNDIDO
	new MUI.Panel({
		id: 'logDataREFUNDIDO',
		contentURL: 'Pages/Refundido/LogDataRefundido.html',
		column: 'logColumnDataREFUNDIDO',
        collapsible: false,
        header: false,
        scrollbars: false,
		require: {		        	
			onload: function(){
				
				if ($('logDataREFUNDIDO'+'_pad')){
					$('logDataREFUNDIDO'+'_pad').setStyle('padding','0px');
				}
				
				// AQUI CARGO LA VENTANA DE LOGS
				//var toInsert = new Element('textarea',{'id':'logRefundido','wrap':'off'});
				var toInsert = new Element('div',{'id':'logRefundido','wrap':'off'});
				toInsert.addClass('centerElementScroll');
				toInsert.setStyle('width','98%');
				toInsert.setStyle('height', (parseInt($('gridLogDataREFUNDIDO').getStyle('height')) - 3) + 'px'); //(parseInt(parseInt(altoPrincipal) - parseInt($('formDataREFUNDIDO').getStyle('height')) )) + 'px');
								
				toInsert.inject($('logDataREFUNDIDO'+'_pad'));

			}
        }
	});	    		

	onSelectNodeRefundidoTree();
}

//Al seleccionar un elemento sobre el arbol
function onSelectNodeRefundidoTree(node,state)
{
    if (!state){
        return;
    }
    // Borrar el contenido del panel anterior si existe
    if($('formDataREFUNDIDO')){
    	$('columnSecondFormDataREFUNDIDO').empty();
		$('columnFirstFormDataREFUNDIDO').empty();
		$('columnLogFormDataREFUNDIDO').empty();
        //$('formDataREFUNDIDO').empty();
    }

    // Si el nodo seleccionado es un tramite mostramos sus metadatos en el panel de informacion,
    // si no entonces solo informamos al usuario que no se ha seleccionado ningun plan
    var innerText = '';
    if (node && node.data.tipo == 'tramite' && node.data.idNode > 0 ) {
    	loadInfoFormRefundido('GET_INFO_TRAMITE_REFUNDIDO','idTram', node.data.idNode,'idTram',2,refundidoTypeFromTramite);
        //loadInfoRefundidoTramite(node.data.idNode, node.text);
    } else if (node && node.data.tipo =='ambito' && node.data.idNode > 0) {
    	loadInfoFormRefundido('GET_INFO_AMBITO_REFUNDIDO','idAmb', node.data.idNode,'idAmb',3,refundidoTypeFromAmbito);
        //loadInfoRefundidoAmbito(node.data.idNode, node.text);
    } else {
        innerText = '</br><center><h3>'+cargarTextoSegunIdioma('Debe seleccionar un tr&aacute;mite o &aacute;mbito')+'</center></h3>';
        $('columnLogFormDataREFUNDIDO').set('html',innerText);
        disableControl('BtnRefundirAdd');
    }
}

//Panel de informacion a mostrar cuando se seleccionar de un ambito o  en el arbol
function loadInfoFormRefundido(wsNameValue, idParam, idValue, idColumn, numFilas, columnData){

	// limpiar estructura del panel info
    $('formDataREFUNDIDO').addClass('progreso');
    enableControl('BtnRefundirAdd');
    
 // Pedir la tabla de datos de refundido del ambito. Depositarla en infoNodoDetail
    var jsonData = undefined;
    var labelWidth = '100px';
    var firstColumWitdh = '50%';
    var secondColumWitdh = '70%';
    if (idParam == 'idAmb'){
    	jsonData = {'wsName' : wsNameValue, 'idAmb' : idValue,'idioma' : idioma};
    	labelWidth = '130px';
        firstColumWitdh = '50%';
        secondColumWitdh = '60%';
    } else if (idParam == 'idTram'){
    	jsonData = {'wsName' : wsNameValue, 'idTram' : idValue,'idioma' : idioma};
    }
    //{'wsName' : wsNameValue, idParam : idValue,'idioma' : idioma}
	runServiceAsyncJson(jsonData,
			function(response){
		
			$('formDataREFUNDIDO').removeClass('progreso');
		
			if (response != null){														
				
				var countInserted = 1;
				
				//Relleno el tab con los datos recibidos
				for(var colidx=0,len=columnData.length; colidx<len; colidx++){
	                
	            	// cmi --> Column Model Item
	                var cmi = columnData[colidx];

	                // campo identificador --> campo oculto
	                var oculto = (cmi.dataIndex== idColumn);
	                
	                var toInsert;
	                var input; //lo defino aqui porque se usa fuera de los ifs
	                
	                if (!oculto && cmi.hidden == false){
	                	//elemento principal
	                	toInsert = new Element('div',{
	                		'class':'formRow'
	                	});
	                	//label
	                	var label = new Element('label',{
	                        'for': cmi.dataIndex,
	                        'class':'formLabel',
	                        'html': initCap(cmi.header.toLowerCase()) + ': '
	                    });
	                	
	                	//labelWidth)
	                	label.setStyle('width',labelWidth);
	                	
	                	//formField
	                	var formField = new Element('div',{
	                		'class':'formField'
	                	});
	                	
	                	//valeWidth	                	
	                	if (countInserted > numFilas){
	                		formField.setStyle('width',firstColumWitdh);
	                	} else {
	                		formField.setStyle('width',secondColumWitdh);
	                	}	                	
	                	
	                	//input
	                	var responseValue = response[cmi.dataIndex];
	                	if (responseValue && responseValue.constructor == Boolean){
	                		if (responseValue == true){
		                		responseValue = 'S&iacute;';
		                	} else{
		                		responseValue = 'No';
		                	}
	                	}
	                	
	                	input = new Element('label', {
	                        name: cmi.dataIndex,
	                        id: cmi.dataIndex,
	                        'html': responseValue,
	                        'class': 'imputLabel'
	                    }); 
	                	
	                	//input.setStyle('width',parseInt($(sitio).getStyle('width')) - 230 + 'px');
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
	                	
	                	if (countInserted > numFilas){
	                		toInsert.inject($('columnSecondFormDataREFUNDIDO'));
	                	} else {
	                		toInsert.inject($('columnFirstFormDataREFUNDIDO'));
	                	}	                	
	                	
	                	//toInsert.inject(infoNodoDetail);
	                	
	                	countInserted = countInserted + 1;
	                }               
	            }		    	
		    }else{		    	
		    	$('columnLogFormDataREFUNDIDO').set('html', '</br><center><h3>'+cargarTextoSegunIdioma('No se ha podido encontrar la informaci&oacute;n del elemento seleccionado')+'</center></h3>');		    	
		    }
		}, function(response){ 
			$('formDataREFUNDIDO').removeClass('progreso');
			$('columnLogFormDataREFUNDIDO').set('html', '</br><center><h3>'+cargarTextoSegunIdioma('Ha ocurrido un error obteniendo los datos del nodo seleccionado')+'</center></h3>');
		});    
}
/**************************************************
 * Gestion de anadir y eliminar elementos de la tabla de tramites o planes a refundir
 **************************************************
 */

//Anadir el tramite seleccionado en el arbol a la tabla de tramites seleccionados para refundir
function onBtnAddClick(button,grid){
	
	if ($('BtnRefundirAdd').disabled == false){

		if (!grid){
	        return;
	    }
		
		 if(!refundidoTree.selected){
			 MUI.notification('No se ha seleccionado ning&uacute;n nodo del arbol');
		     return;
		 }
		
		 if (refundidoTree.selected.data.tipo == 'ambito'){
		     addAmbitoToGrid(grid, refundidoTree.selected);
		 } else if(refundidoTree.selected.data.tipo == 'tramite'){
		     addTramiteToGrid(grid, refundidoTree.selected);
		 } else {
			 MUI.notification('Solo los nodos de ambito y trámite pueden añadirse a la tabla');
		 }
		 checkCanRefundir(refundidoGrid);
	 
	}
}

//Quitar un plan de la tabla de planes seleccionados para refundir
function onBtnRemoveClick(button,grid){
	
	if ($('BtnRefundirRemove').disabled == false){
	
	    if (!grid){
	        return;
	    }
	    if (!grid.selected || grid.selected.length == 0) {
	    	MUI.notification('No se ha seleccionado ningún trámite en la tabla');
	        return;
	    }
	    grid.deleteRow(grid.selected[0]);
	    checkCanRefundir();
    
	}
}

//Limpio la tabla de tramites a refundir
function onBtnCleanClick(button,grid){
	
	if ($('BtnRefundirClean').disabled == false){
	
	    if (!grid){
	        return;
	    }

	    var myData = []; //ELIMINO TO DO LO QUE HUBIERA ANTES EN LA TABLA
	    grid.setData(myData);
	    
	    checkCanRefundir();
    
	}
}

//Anado un nuevo plan para refundir
function addTramiteToGrid(grid, treenode){
    
	var myData = grid.getData();
    
    if (!myData){
        myData = [];
    }

    // obtener el plan al que pertence el tramite a partir de la estructura del arbol de planes y tramites
    var idPlan  = '';
    var nombrePlan = '';
    var nodoPlan = getPlanOfTramite(treenode);
    if (nodoPlan){
        idPlan = nodoPlan.data.idNode;
        nombrePlan = nodoPlan.text;
    }
    var puedoRefundir = true;
    var newRow = {
        'idTramite': treenode.data.idNode,
        'nombreTramite': treenode.text,
        'idPlan': idPlan,
        'nombrePlan': nombrePlan
    };

    //no se permite refundir dos trámites que pertenezcan al mismo plan
    if (alreadyReferencedPlan(idPlan)){
        alert('No se pueden refundir dos trámites del mismo plan');
        puedoRefundir = false;
    }

    // no se permiten tramites repetidos en el grid. Dos tramites son iguales si tienen el mismo idTramite
    if (puedoRefundir && !dataArrayContainsPlan(myData, newRow)){
    	myData.combine([newRow]);
    } else
        alert('El trámite ya se encuentra en la lista');

    grid.setData({
        'data': myData
    });
    
}


function addAmbitoToGrid(grid, treenode){
	
	if (grid.getData() && grid.getData().length>0){
		var idAmb = treenode.data.idNode;
	    var nomAmb = treenode.text;
		
		var msg = 'Al a&ntilde;adir los tramites del &aacute;mbito ' + nomAmb + ' se sustituir&aacute;n a los actualmente seleccionados para refundir.\n &iquest;Desea Continuar?';
		confirmAddTramitesAmbitoRefundido = new PopupMsg(msg, {type: 'yesno'});
		confirmAddTramitesAmbitoRefundido.addEvent('close', onAddAmbitoToGridConfirm.bind(this,grid,treenode));
	} else{
		onAddAmbitoToGridConfirm(grid, treenode);
	}	
}

function onAddAmbitoToGridConfirm(grid,treenode){
	
	if (!confirmAddTramitesAmbitoRefundido || confirmAddTramitesAmbitoRefundido.getResult() === 'yes') {
		
		if (!treenode){
			treenode = refundidoTree.selected;
		}
		
		var idAmb = treenode.data.idNode;
	    var nomAmb = treenode.text;

	    var myData = []; //ELIMINO TO DO LO QUE HUBIERA ANTES EN LA TABLA
	    grid.setData(myData);
	    
	    /*
	    var myData = grid.getData();
	    if (!myData){
	        myData = [];
	    }*/

	    MUI.notification('A&ntilde;adiendo tr&aacute;mites refundibles del &aacute;mbito '+nomAmb+ ' ' + idAmb);
	    
	    disableControl('BtnRefundirRefundir');
	    disableControl('BtnRefundirAdd');
	    disableControl('BtnRefundirRemove');
	    disableControl('BtnRefundirClean');
	    disableControl('gridRefundido');
	    
	    runServiceAsyncJson({'wsName' : 'GET_TRAMITES_REFUNDIBLES_DE_AMBITO','idAmb' : idAmb,'idioma' : idioma},
			function(response){
	    	
	    	enableControl('BtnRefundirRefundir');
	    	enableControl('BtnRefundirAdd');
	    	enableControl('BtnRefundirRemove');
	    	enableControl('BtnRefundirClean');
	    	enableControl('gridRefundido');
	    	
	    	if (response != null && response.length > 0){
	    		
	    		var puedoRefundir = true;
	            
	    		for (var i=0, len = response.length; i<len; i++){
	                
	    			var newRow = {
	                    'idTramite': response[i].idTram,
	                    'nombreTramite': response[i].nombreTram,
	                    'idPlan': response[i].idPlan,
	                    'nombrePlan': response[i].nombrePlan
	                };
	               
	                //no se permite refundir dos tramites que pertenezcan al mismo plan
	                if (alreadyReferencedPlan(response[i].idPlan)){
	                    alert('No se pueden refundir dos trámites del mismo plan');
	                    puedoRefundir = false;
	                }

	                // no se permiten tramites repetidos en el grid. Dos tramites son iguales
	                // si tienen el mismo idTramite
	                if (puedoRefundir && !dataArrayContainsPlan(myData, newRow)){
	                	myData.combine([newRow]);
	                }
	            } //for
	            refundidoGrid.setData({data:myData});
	            
	            checkCanRefundir(refundidoGrid); 
	    	}
	    	
	    });
		
	}
	confirmAddTramitesAmbitoRefundido = undefined;
	
}


/**************************************************
 * Invocacion al procedimiento de refundido
 **************************************************
 */
function onBtnRefundirClick(button,grid){
	
	if ($('BtnRefundirRefundir').disabled == false){
		
		var myData = grid.getData();    
	    
	    if (!myData || myData.length == 0){
	    	MUI.notification(cargarTextoSegunIdioma('La lista de tr&aacute;mites a refundir est&aacute; vac&iacute;a'));
	    	return;
	    }
	    
	    var msg = 'Se va a proceder a iniciar el proceso de refundido de los tr&aacute;mites indicados. &iquest;Desea Continuar?';
		confirRefundirMsg = new PopupMsg(msg, {
            type: 'yesno'
        });
		confirRefundirMsg.addEvent('close', onConfirmRefundir.bind(this, myData));		
	}	    
}

function onConfirmRefundir(myData){
	
	if (confirRefundirMsg && confirRefundirMsg.getResult() === 'yes') {		
		
	    var tramites= new Array();
	    for (var i=0, len=myData.length; i<len; i++ ){
	        tramites.push(myData[i].idTramite);
	    }

	    var strTramites = tramites.join(",\n");
		
		disableControl('BtnRefundirRefundir');
	    disableControl('BtnRefundirAdd');
	    disableControl('BtnRefundirRemove');
	    disableControl('BtnRefundirClean');	    
	    //disableControl('chkCrearTramite');
	    disableControl('gridRefundido');
	    
	    var ahora = new Date();
	    var fecha = ahora.getDate() +"-"+ ahora.getMonth() +"-"+ ahora.getFullYear() +"-"+ ahora.getHours()+"-"+ ahora.getMinutes()+"-"+ ahora.getSeconds();
	    var logName="Refundido_"+fecha;    
	    
	    //Limpio el log de refundido
	    $("logRefundido").set('html','');

	    runServiceAsync({
	        'wsName': 'REFUNDIR_TRAMITES',
	        'listaTramites':strTramites,
	        'crearTramite': true, //$('chkCrearTramite').checked,
	        'logName' : logName
	    },function(response){                            
	        
	        if (response.toString().toUpperCase()=='TRUE') {        	
	            alert("Refundido finalizado con éxito");
	            $('infoTextWrapper').set('html','Refundido finalizado');
            	$('infoTextWrapperProgeso').setStyle('display','none');
	        }else{
	            alert("Se ha producido un error al realizar el refundido. Consulte el log de la operación para obtener más información.");
	            $('infoTextWrapper').set('html','Refundido erroneo');
            	$('infoTextWrapperProgeso').setStyle('display','none');
	        }

	        enableControl('BtnRefundirRefundir');  
	        enableControl('BtnRefundirAdd');
	        enableControl('BtnRefundirRemove');
	        enableControl('BtnRefundirClean');	 
	        //enableControl('chkCrearTramite');
	        enableControl('gridRefundido');
	    });    
	    
	    //Retardo para esperar a que el log esta creado
	    window.setTimeout(function(){
	    	$('infoTextWrapper').set('html','Refundido en proceso');
    		$('infoTextWrapperProgeso').setStyle('display','inline-block');
    		
	        onRefreshLogRefundidoClick(this.logName);
	    }.bind({
	        logName:logName
	    }),1000);
	    
	}
}


/*
 * Refrescar el log del proceso de refundido
 */

function onRefreshLogRefundidoClick(logName){
	
	runServiceAsyncJson({'wsName': 'LOG_REFUNDIDO','logName': logName}, 
    	function(resultado){
		        
    	if (resultado){    		    
    		if (resultado.log &&  resultado.log.length>0){
	    		var valorAnterior = $("logRefundido").get('html');
	    		var valorNuevo = valorAnterior + ' ' + resultado.log;
	    		    		    		
	    		if (valorNuevo.length >= 50000){
	    			valorNuevo = valorNuevo.substr(resultado.log.length);
	    		}
	            
	            $("logRefundido").set('html',valorNuevo);
	            
	            $("logRefundido").scrollTop = $("logRefundido").scrollHeight;
	            
	            enableControl("panelResultado");
    		}
                    
            //Si esta inciado o no estando en inciado sigue habiendo datos, continuo haciendo peticiones
            if (resultado.estado == 'INICIADO' || resultado.log.length>0 ){            	
            	//polling para traer informacion del log
                window.setTimeout(function(){
                    onRefreshLogRefundidoClick(this.logName);
                }.bind({logName:this.logName}) ,1000);
            } else {
            	$('infoTextWrapper').set('html','Refundido finalizado');
            	$('infoTextWrapperProgeso').setStyle('display','none');
            }                            		
    	}    	
        
    }.bind({
        logName:logName
    }));
    
}


/*UTILITY FUNCTIONS*////////////////////////////////////////////////////////////

/*
     * Comprueba si un tramite ya esta contenido en el grid de planes para refundir
     *
     * @param arr. Array de objetos {idTramite, nombreTramite} que corresponde con el
     *          contenido del grid.
     * @param item. Objeto {idTramite, nombreTramite} del que deseamos conocer su
     *          pertenencia al grid.
     * @return true si el tramite ya esta contenido en el grid de planes, false en
     *          caso contrario
     */
function dataArrayContainsPlan(arr, item){
    if (!arr)
        return false;

    var contains = false;
    for (var i=0, len = arr.length; i<len; i++){
        if (arr[i].idTramite == item.idTramite ){
            contains = true;
            break;
        }
    }
    return contains;
}

/*
     * Si hay planes en el grid habilitamos el boton de refundir y la consola de log.
     * Si no hay planes que refundir lo desactivamos
     */
function checkCanRefundir(dg){
    if (!dg)
        return;

    var canRefundir = false;
    var myData = dg.getData();
    if (myData){
        canRefundir = myData.length >0;
    } else {
        canRefundir = false;
    }

    if (canRefundir) {
        enableControl('BtnRefundirRefundir');
    } else {
        disableControl('BtnRefundirRefundir');
    }
}

/*
     * Devuelve el nodo del arbol que corresponde al plan del nodo tramite pasado
     * como parametro
     *
     */
function getPlanOfTramite(nodo){
    var res = null;
    if (nodo && nodo.data && nodo.data.tipo == 'tramite' && nodo.parent && nodo.parent.parent && nodo.parent.parent.data.tipo == 'plan') {
        res = nodo.parent.parent;
    }

    return res;
}


function alreadyReferencedPlan(plan){
    var res = false;
    var myData = refundidoGrid.getData();
    if (!myData)
        myData = new Array();

    for(var i=0, len = myData.length; i<len; i++){
        if (plan == myData[i].idPlan){
            res = true;
            break;
        }
    }
    return res;
}

function chargeTabRefundidosRealizados(){
	//Actualizo los datos del tab de refundido
	$('panelRefundidosRealizados').empty();
	
	MUI.updateContent({
		element: $('main-panel'),
		childElement: $('panelRefundidosRealizados'),
		url: 'Pages/Refundido/RefundidoDataTable.jsp',
		padding: { top: 2, right: 2, bottom: 2, left: 2 }									
	});
}

function BtnVisorRefundidoBuscar(){
	
	refundidoSearchWindow = function(){ 
		new MUI.Modal({
		id: 'RefundidoWindowBusquedaPlanes',
		title: 'Búsqueda de planes',
		icon: 'styles/images/search_icon.png',			
		contentURL: 'Pages/VisorPLANES/VisorPLANESWindows/VisorPLANESWindowBusquedaPlanes.jsp',
		width: 600,
		height: 220,
		maximizable: false,
		resizable: false,
		scrollbars: false,
		draggable: true,
		onContentLoaded: function(){
			buquedaModificadaVisorPlanes = true; //Para que se inicie el parametro de busqueda
			
			$$('input').each(function(input){
				if (input.get('data-meiomask') != null){
					input.meiomask(input.get('data-meiomask'));
				}					
			});
			
			//Setup calendar (Esto crea un button calendar)
	        new Calendar({ txtFechaTramiteDesde: 'd/m/Y' }, { classes: ['dashboard'],
	        	direction: -1,
	            navigation: 2,
	            offset: 1,
	            days: ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'], 
	            months: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'] 
	        });
	        
	        new Calendar({ txtFechaTramiteHasta: 'd/m/Y' }, { classes: ['dashboard'],
	        	direction: -1,
	            navigation: 2,
	            offset: 1,
	            days: ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'], 
	            months: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'] 
	        });
	        
	        $("buttonCalendar").setStyles({
	            'position':'relative',
	            'top':(Browser.Engine.gecko?'0px': (Browser.Engine.webkit?'-2px':(Browser.Engine.trident?(Browser.Engine.version==6?'5px':'-1px'):'')))
	        });
	        
	        $('txtNombre').focus();
	        
	        //accion del boton buscar, hay que seleccionar el nodo inicio de la busqueda
	        $('BtnVisorPlanesCancelarBuscarPlanes').addEvent('click',function(e){
	        	new Event(e).stop();
	        	$('RefundidoWindowBusquedaPlanes').retrieve('instance').close();
	        });
	        $('BtnVisorPlanesBuscarPlanes').addEvent('click',function(e){
	        	new Event(e).stop();
	        	
	        	if (buquedaModificadaVisorPlanes == false)
	        	{
	        		var resultSearch = refundidoTree.findNext();
	        		if (resultSearch == -2){
	        			alert("No hay más resultados");
	        		} else {
	        			resultadosBusquedaCounter = resultadosBusquedaCounter + 1;
	        			$('BtnVisorPlanesResultadosBuscarPlanes').set('html',resultadosBusquedaCounter+' de '+resultadosBusquedaCount+' resultados');
	        		}		        		
	        	} else {
	        		var idRaizParam = '';
			        var tipoRaizParam = '';
			        if (refundidoTree.selected){
			        	var node = refundidoTree.selected;
			        	while(!node.data.tipo && node.parent){
			        		node = node.parent;
			        	}
			        	
			        	if (node.data.tipo){
			        		idRaizParam = node.data.idNode;
			        		tipoRaizParam = node.data.tipo;
			        	}
			        }
		        	
			        var req = new Request.JSON({
			        url: 'GestionConsola',
			        data: {
			            wsName: 'SEARCH_ON_TREE',
			            tipoBusqueda: 'planes',
			            tipo: viewerTreeType.ref,
			            idRaiz: idRaizParam,
			            tipoRaiz: tipoRaizParam,
			            nombrePlan: $('txtNombre').value,
			            codigoPlan: $('txtCodigoPlan').value,
			            desde: $('txtFechaTramiteDesdeVisible').value,
			            hasta: $('txtFechaTramiteHastaVisible').value,
			        },
			        timeout: 5000,
			        async:true,
			        onSuccess: function(result){
			        	enableDisableTree($('treeRefundido'), true);
			        					        	
			        	if (result.resultado && result.resultado.length > 0){
			        		//Accion cuando la llamada ha devuelto un dato correcto
		        			buquedaModificadaVisorPlanes = false;
		        			refundidoTree.find(result.resultado);
		        			if (result.total) { 
		        				alert('Se han obtenido '+result.total+' resultados para la búsqueda realizada, para ir recorriendolos sin modificar los parametros de búsqueda vaya pulsando el botón buscar');
		        				$('BtnVisorPlanesResultadosBuscarPlanes').set('html','1 de '+result.total+' resultados');
		        				resultadosBusquedaCounter = 1;
		        				resultadosBusquedaCount = result.total;
	        				}
			        	} else {
			        		alert('No se han obtenido resultados para la búsqueda realizada');
			        	}
			        	
			        	//$('VisorPLANESWindowBusquedaPlanes').retrieve('instance').close();
		        	},
			        onFailure: function(response){
			        	enableDisableTree($('treeRefundido'), true);
			        	alert('Error al realizar la búsqueda');
			        },
			    	onTimeout: function(){
			    		enableDisableTree($('treeRefundido'), true);
			    		alert('No se ha obtenido respuesta del servidor');
			    	}
			        });				        
			        enableDisableTree($('treeRefundido'), false);				        
			        req.send();
	        	} //Fin de si es una busqueda nueva o no
	        });
		},
		onCloseComplete: function(){
			visorPLANESCurrentSearchWindow = undefined;
		}			
	});
	}; //Fin var window
	
	refundidoSearchWindow(); //Muestro la ventana
}