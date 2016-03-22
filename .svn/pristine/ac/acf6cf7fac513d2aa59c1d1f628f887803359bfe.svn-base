var visorPLANESTree; //Arbol del visualizador de PLANES
var visorPLANESTreeDeterminaciones; //Arbol del visualizador de PLANES - DETERMINACIONES
var visorPLANESTreeEntidades; //Arbol del visualizador de PLANES - ENTIDADES
var visorPLANESAddPlanTree; //Arbol del formulario para anadir tramites
var visorPLANESAddPlanPlanesSelected;
var visorPLANESCurrentWindow = null; //Ventana mostrandose actualmente
var visorPLANESCurrentSearchWindow = undefined; //Ventana mostrandose actualmente
var visorPLANESviewerTreeType = null;
var visorPLANESIdTramiteCurentlySelected = -1;

//variables de datos cargados
var dataPlanesEsOperadorPor;
var dataPlanesOperaA;

var dataTramitesDeterminaciones;
var dataTramitesEntidades;
var dataTramitesAmbitosAplcacion;
var dataTramitesDocumentos;

//Variables de contadores de tabs
var countPlanesTabLoad = 0;
var countTramitesTabLoad = 0;
var countTramitesDeterminacionesTabLoad = 0;
var countTramitesEntidadesTabLoad = 0;

var textoAsociadoVisorPlanes = 'Sin texto asociado';

var buquedaModificadaVisorPlanes = true;
var resultadosBusquedaCounter = 0;
var resultadosBusquedaCount = 0;

function initVisualizadorPLANES(opcionPLANES){
	//$('LeftSideColumn').addEvent('OnResize',resizeVisorPLANES);	
	//$('LeftSideColumn').ResizeEvent = resizeVisorPLANES;
	
	var rootTree = 'Planes';
	
	if (opcionPLANES){
		
		visorPLANESviewerTreeType = opcionPLANES;
		
		switch (opcionPLANES) {		
	    case viewerTreeType.rpm: //visor RPM
	    	rootTree += ' consolidados';
	    	break;
	    case viewerTreeType.validacion: //visor validacion
	    	rootTree += ' no consolidados';
	    	break;
		}	
	}
	
	visorPLANESTree = new MooTreeControl({
        div: 'treePLANES',
        mode: 'folders',
        treeType: 'planes',
        iconBar: ['javascriptsV2/mooTree2/Planes.gif'],
        grid: true,
        onSelect: function(node, state) {
            if (visorPLANESTree.enabled == true && state) {
                cargarInfoDeNodoVisorPlanes(node);
            }
        }
    	},{
        text: cargarTextoSegunIdioma(rootTree),
        open: true            
    });      
    
    $('treePLANES').setStyle('height','95%'); //parseInt($('LeftSideColumn').getStyle('height'))  - 14 + 'px');
    $('treePLANES').setStyle('width','95%'); //parseInt($('LeftSideColumn').getStyle('width')) - 9 + 'px');

    visorPLANESTree.root.load('GestionConsola?wsName=GET_NODOS_AMBITOS&op='+escape(globalConfig.op_consola)+'&tipo='+opcionPLANES+'&idioma='+idioma);
                
    if ($('BtnVisorPlanesAddPlanes')){
    	//Asigno los eventos de la botonera del arbol
        $('BtnVisorPlanesAddPlanes').addEvent('click', BtnVisorPlanesAddPlanes);
        $('BtnVisorPlanesAddTramites').addEvent('click', BtnVisorPlanesAddTramites);
        $('BtnVisorPlanesEditPlanes').addEvent('click', BtnVisorPlanesEditPlanes);        
        $('BtnVisorPlanesEditTramites').addEvent('click', BtnVisorPlanesEditTramites);
        $('BtnVisorPlanesRemoveTramites').addEvent('click', BtnVisorPlanesRemoveTramites);
    
        //Deshabilito los botones
    	disableControl('BtnVisorPlanesAddPlanes');
        disableControl('BtnVisorPlanesAddTramites');
        disableControl('BtnVisorPlanesEditPlanes');
        disableControl('BtnVisorPlanesEditTramites');
        disableControl('BtnVisorPlanesRemoveTramites');
    }   
    
    if ($('BtnVisorPlanesExpandir')){
    	$('BtnVisorPlanesExpandir').addEvent('click',  function () { 
    		//visorPLANESTree.expand(); ESTO SERIA UN EXPAND ALL
    		if (visorPLANESTree.selected){
    			visorPLANESTree.selected.expand();
    		}
		});
    	$('BtnVisorPlanesContraer').addEvent('click',  function () { visorPLANESTree.collapse(); });
    	$('BtnVisorPlanesBuscar').addEvent('click',  BtnVisorPlanesBuscar);    	    	
    }
    
    $('LeftSideColumn').fade(1);
    $('mainColumn').fade(1);
}

/*
function resizeVisorPLANES(){}
}*/

// Al pulsar sobre un nodo se lanza esta funcion
function cargarInfoDeNodoVisorPlanes(node)
{   
	visorPLANESIdTramiteCurentlySelected = -1;
	
    switch(node.data.tipo)
    {
        case "ambito":
        	//Bajo un ambito solo se pueden crear planes y no tramites
        	if ($('BtnVisorPlanesAddPlanes')){
        		enableControl('BtnVisorPlanesAddPlanes');
            	disableControl('BtnVisorPlanesAddTramites');
            	disableControl('BtnVisorPlanesEditPlanes');
            	disableControl('BtnVisorPlanesEditTramites');
            	disableControl('BtnVisorPlanesRemoveTramites');
        	}        	
        	
        	$('mainColumn').empty();
            break;
        case "plan":
        	//Bajo un plan se pueden crear tanto tramites como planes
        	if ($('BtnVisorPlanesAddPlanes')){
        		enableControl('BtnVisorPlanesAddPlanes');
            	enableControl('BtnVisorPlanesAddTramites');
            	enableControl('BtnVisorPlanesEditPlanes');
            	disableControl('BtnVisorPlanesEditTramites');
            	disableControl('BtnVisorPlanesRemoveTramites');
        	}        	
        	
        	mostrarDatosDePlan(node.data.idNode);
            break;
        case "tramite":
        	//No se puede crear ni planes ni tramites bajo un tramite
        	if ($('BtnVisorPlanesAddPlanes')){
        		disableControl('BtnVisorPlanesAddPlanes');
                disableControl('BtnVisorPlanesAddTramites');
                disableControl('BtnVisorPlanesEditPlanes');
                enableControl('BtnVisorPlanesEditTramites');
        	}         	        
        	
        	//Determino si el tramite se puede borrar. Para consolidados(RPM) si el tipo de tramite es 11 y para no consolidados(validacion), siempre.
        	switch (visorPLANESviewerTreeType) {		
    	    case viewerTreeType.rpm: //visor RPM
    	    	if (node.data.idTipoTramite && node.data.idTipoTramite == '11'){
    	    		enableControl('BtnVisorPlanesRemoveTramites');
    	    	} else {
    	    		disableControl('BtnVisorPlanesRemoveTramites');
    	    	}
    	    	break;
    	    case viewerTreeType.validacion: //visor validacion
    	    	enableControl('BtnVisorPlanesRemoveTramites');
    	    	break;
    	    default:
    	    	disableControl('BtnVisorPlanesRemoveTramites');
    	    	break;
    		}
            
        	visorPLANESIdTramiteCurentlySelected = node.data.idNode;
            mostrarDatosDelTramite(node.data.idNode);
            break;            
        default:
        	//Si no esta contemplado no se permite crear nada
        	if ($('BtnVisorPlanesAddPlanes')){
        		disableControl('BtnVisorPlanesAddPlanes');
            	disableControl('BtnVisorPlanesAddTramites');
            	disableControl('BtnVisorPlanesEditPlanes');
            	disableControl('BtnVisorPlanesEditTramites');
            	disableControl('BtnVisorPlanesRemoveTramites');
        	}        	
        	
        	$('mainColumn').empty();
        	break;
    }
}


//***************** PLAN *****************************/
function mostrarDatosDePlan(id)
{			
	$('mainColumn').empty();
	
	//Cargo el plan como un TAB de mochaUI sobre el mainColum con un updatePanel	
	new MUI.Panel({
		id: 'tabPlanes-panel',
		title: '',
		contentURL: 'Pages/VisorPLANES/Planes/PlanesData.html',
		column: 'mainColumn',
		tabsURL: 'Pages/VisorPLANES/Planes/PlanesTabs.html',
		scrollbars : false,
        collapsible: false,
        header: true,
        onContentLoaded: function(){
			if($('panelPlanesDatosGenerales') && $('panelPlanesDatosGenerales').childNodes.length == 0)
            {							
				if($('tabPlanesCargando')){
					$('tabPlanesCargando').setStyle('background-image','url("")');
				}
				if($('tabPlanes-panel_pad')){
					$('tabPlanes-panel_pad').setStyle('padding','2px');
				}
				
				//Setup tabs 
				$('panelPlanesEsOperadorPor').set('left','0px');
				$('panelPlanesEsOperadorPor').set('top','0px');
				$('panelPlanesEsOperadorPor').setStyle('display', 'none');
				$('panelPlanesOperaA').set('left','0px');
				$('panelPlanesOperaA').set('top','0px');
				$('panelPlanesOperaA').setStyle('display', 'none');					
				
				countPlanesTabLoad = 0;					
				getProgressLoadTabStatus(3,'tabPlanesCargando','plan', true, 'treePLANES');											
				
                //ANADO LOS DATOS GENERALES- formulario simple										
				getFormData(id, 'plan', true, undefined, 
                		function(data){
						addPlanesTadLoad();
						ChargeTabPlanesDatosGenerales(data);						
					});
				
				getTableData(id, 'PlanEsOperadoPor', true, 'tabEsOperadorPorPlanes',
                		function(data){
						addPlanesTadLoad();
						dataPlanesEsOperadorPor = data;	                	
                	});
					
				getTableData(id, 'PlanOperaA', true, 'tabOperaAPlanes',
                		function(data){
						addPlanesTadLoad();
						dataPlanesOperaA = data;	                	
                	});											
            }
		}.delay(200)										
	});
}

function ChargeTabPlanesDatosGenerales(data){
	
	var result = false;
	
	//PANELES HORIZONTALES FORM - TEXTO
    $('panelPlanesDatosGenerales').setStyle('height', (parseInt($('tabPlanes-panel').getStyle('height')) - 4) + 'px');    
    
    addSplitPanel('panelPlanesDatosGenerales', 'HORIZONTAL', undefined, undefined, -1, true, 'Texto', undefined, 200, true,
		function() {	    
    
    		result = addForm(data, undefined, undefined, ViewerPlanesTypePlan, 'fspElement' + 'panelPlanesDatosGenerales', undefined, undefined);
    	
    		data = data.data[0];
    		
    		var toInsert = new Element('textarea',{'id':'sspElement'+'panelPlanesDatosGenerales'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		
    		if (data['texto'] != undefined && $('sspElement' + 'panelPlanesDatosGenerales')) {
    			if (data['texto'].length == 0 || data['texto'] == textoAsociadoVisorPlanes){
    				data['texto'] = textoAsociadoVisorPlanes;    				
	        	}
    			
    			//inserto un textArea    			    			    			
				toInsert.inject($('sspElement' + 'panelPlanesDatosGenerales'));
	        	$('sspElement'+'panelPlanesDatosGenerales'+'textArea').set('html', data['texto']);	        		        	
		    }    		    		    		
		});          
    
    return result;
    
}

function ChargeTabPlanesEsOperadorPor(data){

	//UN SOLO PANEL GRID		
	var supPanelHeight = (parseInt($('tabPlanes-panel').getStyle('height')) - 4) + 'px';
    var tableHeight = calculateTableHeight(data.total, supPanelHeight);
	
   var result = addTable(data, undefined, undefined, viewerPlanesTypePlanEsOperadoPor, 'panelPlanesEsOperadorPor', true, "100%", tableHeight, 'tabEsOperadorPorPlanes', undefined);      
   
   return result;
}

function ChargeTabPlanesOperaA(data){

	//UN SOLO PANEL GRID	
	var supPanelHeight = (parseInt($('tabPlanes-panel').getStyle('height')) - 4) + 'px';
    var tableHeight = calculateTableHeight(data.total, supPanelHeight);
    
    var result =  addTable(data, undefined, undefined, viewerPlanesTypePlanOperaA, 'panelPlanesOperaA', true, "100%", tableHeight, 'tabOperaAPlanes', undefined);
    
    return result;
}

function addPlanesTadLoad(){
	countPlanesTabLoad = countPlanesTabLoad + 1;
}

//***************** fin PLAN *****************************/

function mostrarDatosDelTramite(id)
{
	$('mainColumn').empty();	
	
	//Cargo el plan como un TAB de mochaUI sobre el mainColum con un updatePanel
	new MUI.Panel({
		id: 'tabTramites-panel',
		title: '',
		contentURL: 'Pages/VisorPLANES/Tramites/TramitesData.html',
		column: 'mainColumn',
		tabsURL: 'Pages/VisorPLANES/Tramites/TramitesTabs.html',
		scrollbars : false,
        collapsible: false,
        header: true,
        onContentLoaded: function(){				
			if($('panelTramitesDatosGenerales') && $('panelTramitesDatosGenerales').childNodes.length == 0)
            {				
				if($('tabTramitesCargando')){
					$('tabTramitesCargando').setStyle('background-image','url("")');
				}
				if($('tabTramites-panel_pad')){
					$('tabTramites-panel_pad').setStyle('padding','2px');
				}
				
				//Setup tabs 
				$('panelTramitesDeterminaciones').set('left','0px');
				$('panelTramitesDeterminaciones').set('top','0px');
				$('panelTramitesDeterminaciones').setStyle('display', 'none');
				$('panelTramitesEntidades').set('left','0px');
				$('panelTramitesEntidades').set('top','0px');
				$('panelTramitesEntidades').setStyle('display', 'none');
				/*
				$('panelTramitesAmbitosAplicacion').set('left','0px');
				$('panelTramitesAmbitosAplicacion').set('top','0px');
				$('panelTramitesAmbitosAplicacion').setStyle('display', 'none');
				*/
				$('panelTramitesDocumentos').set('left','0px');
				$('panelTramitesDocumentos').set('top','0px');
				$('panelTramitesDocumentos').setStyle('display', 'none');
				
		    	countTramitesTabLoad = 0;					
				getProgressLoadTabStatus(4,'tabTramitesCargando','tramite', true, 'treePLANES');
				
                //ANADO LOS DATOS GENERALES- formulario simple
				getFormData(id, 'tramite', true, undefined, 
            		function(data){
					addTramitesTadLoad();
					ChargeTabTramitesDatosGenerales(data);
				});	                 

                getTreeData('GestionConsola?wsName=GET_NODOS_DETERM_DE_TRAMITE&idTramite='+id+'&idioma='+idioma, undefined, true, 'tabTramitesDeterminaciones', 
            		function(data){
                	addTramitesTadLoad();
                	dataTramitesDeterminaciones = data;	                	
            	});	                	    		
                
                getTreeData('GestionConsola?wsName=GET_NODOS_ENTIDAD_DE_TRAMITE&idTramite='+id+'&idioma='+idioma, undefined, true, 'tabTramitesEntidades', 
            		function(data){
                	addTramitesTadLoad();
            		dataTramitesEntidades = data;                		
        		});
                
                /* Se elimina el tab incluyendolo como un campo mas en datos generales
                //AMBITO de APLICACION
                getTableData(id, 'AmbitoAplicacionTramite', true, 'tabTramitesAmbitosAplicacion', 
            		function(data){
                	addTramitesTadLoad();
            		dataTramitesAmbitosAplcacion = data;                		
        		});*/
                
                //ANADO LA TABLA DE DOCUMENTOS
                getTableData(id, 'DocumentosTramite', true, 'tabTramitesDocumentos',
            		function(data){
                	addTramitesTadLoad();
                	dataTramitesDocumentos = data;	                	
            	});									
            } //fin del if de si se ha cargado el panelTramitesDatosGenerales
		}.delay(200)										
	});
}

function ChargeTabTramitesDatosGenerales(data){
	
	var result = false;
	
	$('panelTramitesDatosGenerales').empty();
	
	//PANELES HORIZONTALES FORM - TEXTO
    $('panelTramitesDatosGenerales').setStyle('height', (parseInt($('tabTramites-panel').getStyle('height')) - 4) + 'px');    
    
    addSplitPanel('panelTramitesDatosGenerales', 'HORIZONTAL', undefined, undefined, -1, true, undefined, ['Pages/VisorPLANES/Tramites/DatosGeneralesTramitesData.html','Pages/VisorPLANES/Tramites/DatosGeneralesTramitesTabs.html'], 150, true,		
		function(){	   	
    		result = addForm(data, undefined, undefined, viewerPlanesTypeTramite, 'fspElement' + 'panelTramitesDatosGenerales', undefined, undefined);    		    	
    		
    		if($('panelDatosGeneralesTramitesTexto') && data != undefined)
            {			
				//Setup tabs 
				$('panelDatosGeneralesTramitesTexto').set('left','0px');
				$('panelDatosGeneralesTramitesTexto').set('top','0px');
				$('panelDatosGeneralesTramitesTexto').setStyle('height','95%');
				
				$('panelDatosGeneralesTramitesComentario').set('left','0px');
				$('panelDatosGeneralesTramitesComentario').set('top','0px');
				$('panelDatosGeneralesTramitesComentario').setStyle('height','95%');
				//$('panelDatosGeneralesTramitesComentario').setStyle('display', 'none');
				
				if ($('BtnVisorPlanesAddTramites')){ //Si puede gestionar tramites
					$('panelDatosGeneralesTramitesPublicaciones').set('left','0px');
					$('panelDatosGeneralesTramitesPublicaciones').set('top','0px');
					$('panelDatosGeneralesTramitesPublicaciones').setStyle('height','95%');
					$('panelDatosGeneralesTramitesPublicaciones').setStyle('display', 'block');
					//Si existe el tab se selecciona
	    			$('tabDatosGeneralesTramitesTexto').removeClass('selected');
	    			$('tabDatosGeneralesTramitesPublicaciones').addClass('selected');
	    			//$('panelDatosGeneralesTramitesTexto').setStyle('display', 'none');
				} else {
					$('tabDatosGeneralesTramitesPublicaciones').setStyle('display', 'none');	
					$('panelDatosGeneralesTramitesTexto').setStyle('display', 'block');
				}
				
				data = data.data[0];
				
				//TEXTO
				var toInsert = new Element('textarea',{'id':'panelDatosGeneralesTramitesTexto'+'textArea','class':'centerElement','readonly':'readonly'});    		
	    		toInsert.addClass('infomsgTextArea');
	    		
	    		if (data['texto'] != undefined && $('panelDatosGeneralesTramitesTexto')) {
	    			if (data['texto'].length == 0 || data['texto'] == textoAsociadoVisorPlanes){
	    				data['texto'] = textoAsociadoVisorPlanes;    				
		        	}
	    			
	    			//inserto un textArea    			    			    			
					toInsert.inject($('panelDatosGeneralesTramitesTexto'));
		        	$('panelDatosGeneralesTramitesTexto'+'textArea').set('html', data['texto']);	        		        	
			    }
	    		//COMENTARIO
	    		toInsert = new Element('textarea',{'id':'panelDatosGeneralesTramitesComentario'+'textArea','class':'centerElement','readonly':'readonly'});
	    		toInsert.addClass('infomsgTextArea');
	    		
	    		if (data['comentario'] != undefined && $('panelDatosGeneralesTramitesComentario')) {
	    			if (data['comentario'].length == 0 || data['comentario'] == textoAsociadoVisorPlanes){
	    				data['comentario'] = textoAsociadoVisorPlanes;    				
		        	}
	    			
	    			//inserto un textArea    			    			    			
					toInsert.inject($('panelDatosGeneralesTramitesComentario'));
		        	$('panelDatosGeneralesTramitesComentario'+'textArea').set('html', data['comentario']);	        		        	
			    }
	    		//PUBLICACIONES 
	    		if ($('BtnVisorPlanesAddTramites')){
	    			var tablePublicaciones;
	    			var idTramite = data['iden']; //identificador del tramite 
		    		getTableData(idTramite, 'Publicaciones', true, undefined,
	            		function(dataPublicacion){
		    			if (dataPublicacion){ //Si hay datos para la tabla
		    				var supPanelHeight = $('panelDatosGeneralesTramitesPublicacionesData').getStyle('height');
						    var tableHeight = calculateTableHeight(dataPublicacion.total, supPanelHeight);	    	
						    
						    tablePublicaciones = addTable(dataPublicacion, undefined, undefined, viewerPlanesTypePublicacionTramite, 'panelDatosGeneralesTramitesPublicacionesData', true, '100%', tableHeight, undefined,
				    			function(info) { //CLICK
				    			    if (info) {
				    			    	enableControl('BtnVisorPlanesTramitesEditPublicaciones');
				    			    	enableControl('BtnVisorPlanesTramitesRemovePublicaciones');
				    			    	
				    			    } else {
				    			    	disableControl('BtnVisorPlanesTramitesEditPublicaciones');
				    			    	disableControl('BtnVisorPlanesTramitesRemovePublicaciones');
				    			    }
				    			},undefined);	
		    			}	    			
	            	});
		    		
				    //Botones de publicaciones tablePublicaciones
		    		$('BtnVisorPlanesTramitesAddPublicaciones').addEvent('click',function(e){	    			
		    			visorPLANESCurrentWindow = function(){ 
		    				new MUI.Modal({
		    				id: 'VisorPLANESWindowAddPublicacionTramite',
		    				title: 'Crear nueva publicación',
		    				icon: 'styles/images/add.png',			
		    				contentURL: 'Pages/VisorPLANES/Tramites/VisorPLANESTramitesWindows/VisorPLANESWindowAddPublicacion.html',
		    				width: 630,
		    				height: 200,
		    				maximizable: false,
		    				resizable: false,
		    				scrollbars: false,
		    				onContentLoaded: function(){	
		    					$$('input').each(function(input){
		    						if (input.get('data-meiomask') != null){
		    							input.meiomask(input.get('data-meiomask'));
		    						}					
		    					});
		    					
	    						//Asigno el valor del tramite sobre el que crear la publicacion
	    						$('idTramite').value = idTramite;
	    						
	    						//TIPOS DE BOLETINES
	    						var result = runServiceJson({wsName: "GET_DATA", TIPO: "Boletin"});
	    						var resultLoad = loadDataOnSelect('idBoletin',result,false,undefined,'No ha sido posible cargar la lista de boletines, no es posible crear nuevos boletines');
	    						
	    						//Si no hay error pero no hay boletines en BD, no se puede agregar elementos	    						
	    						if (resultLoad == false || $('idBoletin').options.length == 0){
	    							if ($('idBoletin').options.length == 0){
	    								alert('No hay datos de boletines por lo que no es posible crear nuevos boletines. Consulte al administrador para dar de alta estos datos');
	    							}	    							
	    							$('VisorPLANESWindowAddPublicacionTramite').retrieve('instance').close();
	    						} else {
	    							//FECHA
		    						//Setup calendar (Esto crea un button calendar)
		    				        new Calendar({ txtFecha: 'd/m/Y' }, { classes: ['dashboard'],
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
		    						
		    						$('BtnVisorPlanesCancelarNuevaPublicacionTramite').addEvent('click', function(e){
		    				        	new Event(e).stop();
		    				        	$('VisorPLANESWindowAddPublicacionTramite').retrieve('instance').close();
		    				        });
		    						$('BtnVisorPlanesNuevaPublicacionTramite').addEvent('click', function(e){
		    							new Event(e).stop();

		    							var req = new Request.JSON({
		    						        url: 'GestionConsola',
		    						        data: {
		    						            wsName: 'CREAR_PUBLICACION',
		    						            idTramite: $('idTramite').value,
		    						            idBoletin: $('idBoletin').options[$('idBoletin').selectedIndex].value,
		    						            fecha: $('txtFechaVisible').value,					            
		    						            numero: $('txtNumero').value
		    						        },
		    						        onSuccess: function(res){
		    						        	if (res.error){
		    						            	alert('Error creando la publicación:\n'+res.result);
		    						            } else {
		    						            	MUI.notification('Datos guardados correctamente');
		    						            	
		    						            	//Si no hay tabla porque no habia datos se crea la tabla
		    						            	if (!tablePublicaciones){
		    						            		getTableData(idTramite, 'Publicaciones', true, undefined,
	    						    	            		function(dataPublicacion){
	    						    		    			if (dataPublicacion){ //Si hay datos para la tabla
	    						    		    				var supPanelHeight = $('panelDatosGeneralesTramitesPublicacionesData').getStyle('height');
	    						    						    var tableHeight = calculateTableHeight(dataPublicacion.total, supPanelHeight);	    	
	    						    						    
	    						    						    tablePublicaciones = addTable(dataPublicacion, undefined, undefined, viewerPlanesTypePublicacionTramite, 'panelDatosGeneralesTramitesPublicacionesData', true, '100%', tableHeight, undefined,
	    						    				    			function(info) { //CLICK
	    						    				    			    if (info) {
	    						    				    			    	enableControl('BtnVisorPlanesTramitesEditPublicaciones');
	    						    				    			    	enableControl('BtnVisorPlanesTramitesRemovePublicaciones');
	    						    				    			    	
	    						    				    			    } else {
	    						    				    			    	disableControl('BtnVisorPlanesTramitesEditPublicaciones');
	    						    				    			    	disableControl('BtnVisorPlanesTramitesRemovePublicaciones');
	    						    				    			    }
	    						    				    			},undefined);	
	    						    		    			}	    			
	    						    	            	});
		    						            	} else {
		    						            		//La tabla ya existe solo anado un elemento
		    						            		getTableData(idTramite, 'Publicaciones', true, undefined,
	    						    	            		function(dataPublicacion){
	    						    		    			if (dataPublicacion){ //Si hay datos para la tabla
	    						    		    				tablePublicaciones.setData(dataPublicacion);
	    						    		    			}	    			
	    						    	            	});
		    						            		disableControl('BtnVisorPlanesTramitesEditPublicaciones');
		    					    			    	disableControl('BtnVisorPlanesTramitesRemovePublicaciones');
		    						            	}
		    					                    $('VisorPLANESWindowAddPublicacionTramite').retrieve('instance').close();
		    						            }
		    						        }.bind(this),
		    						        onFailure: function(){
		    						        	alert('Error guardando la nueva publicación');
		    						        },
		    						        onCancel: function(){
		    						        	alert('Petición cancelada');
		    						        }
		    						    });
		    						    req.send();		    						    
		    						});
	    						} //Fin de si hay datos de boletines en bd
	    					} //Fin del onContentLoad			
		    			});
		    			}; //Fin var window
		    			
		    			visorPLANESCurrentWindow(); //Muestro la ventana
		    		});
		    		
		    		disableControl('BtnVisorPlanesTramitesEditPublicaciones');
		    		$('BtnVisorPlanesTramitesEditPublicaciones').addEvent('click',function(e){
		    			if ($('BtnVisorPlanesTramitesEditPublicaciones').disabled == false){		    				
		    				if (tablePublicaciones && tablePublicaciones.selected && tablePublicaciones.selected.length > 0) {
	    						var publicacion = tablePublicaciones.getDataByRow(tablePublicaciones.selected[0]);	    					
	    						
	    						visorPLANESCurrentWindow = function(){ 
				    				new MUI.Modal({
				    				id: 'VisorPLANESWindowEditPublicacionTramite',
				    				title: 'Modificar publicación',
				    				icon: 'styles/images/edit.png',			
				    				contentURL: 'Pages/VisorPLANES/Tramites/VisorPLANESTramitesWindows/VisorPLANESWindowEditPublicacion.html',
				    				width: 630,
				    				height: 200,
				    				maximizable: false,
				    				resizable: false,
				    				scrollbars: false,
				    				onContentLoaded: function(){
				    					$$('input').each(function(input){
				    						if (input.get('data-meiomask') != null){
				    							input.meiomask(input.get('data-meiomask'));
				    						}					
				    					});
				    					
			    						//Asigno los valores de la publicacion
				    					$('idTramite').value = idTramite;
				    					$('id').value = publicacion.id;
				    					//BOLETIN
				    					result = runServiceJson({wsName: "GET_DATA", TIPO: "Boletin"});
				    					var resultLoad = loadDataOnSelect('idBoletin',result,false,publicacion.boletin,'No ha sido posible cargar la lista de boletines, no es posible modificar el boletín');
				    					
				    					//Si no hay error pero no hay boletines en BD, no se puede agregar elementos	    						
			    						if (resultLoad == false || $('idBoletin').options.length == 0){
			    							if ($('idBoletin').options.length == 0){
			    								alert('No hay datos de boletines, no es posible modificar el boletín. Consulte al administrador para dar de alta estos datos');
			    							}	    							
			    							$('VisorPLANESWindowAddPublicacionTramite').retrieve('instance').close();
			    						} else {
			    							//FECHA
					    					//Setup calendar (Esto crea un button calendar)
					    			        new Calendar({ txtFecha: 'd/m/Y' }, { classes: ['dashboard'],
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
					    			        $('txtFechaVisible').value = publicacion.fecha;					    					
				    						$('txtNumero').value = publicacion.numero;			    						
				    						
				    						$('BtnVisorPlanesCancelarModificarPublicacionTramite').addEvent('click', function(e){
				    				        	new Event(e).stop();
				    				        	$('VisorPLANESWindowEditPublicacionTramite').retrieve('instance').close();
				    				        });
				    						$('BtnVisorPlanesGuardarModificarPublicacionTramite').addEvent('click', function(e){
				    							new Event(e).stop();

				    							var req = new Request.JSON({
				    						        url: 'GestionConsola',
				    						        data: {
				    						            wsName: 'MODIFICAR_PUBLICACION',
				    						            id: $('id').value,
				    						            idTramite: $('idTramite').value,
				    						            idBoletin: $('idBoletin').options[$('idBoletin').selectedIndex].value,
				    						            fecha: $('txtFechaVisible').value,					            
				    						            numero: $('txtNumero').value
				    						        },
				    						        onSuccess: function(res){
				    						        	if (res.error){
				    						            	alert('Error modificando la publicación:\n'+res.result);
				    						            } else {
				    						            	MUI.notification('Datos guardados correctamente');
				    						            	
			    						            		//La tabla ya existe solo anado un elemento
			    						            		getTableData(idTramite, 'Publicaciones', true, undefined,
		    						    	            		function(dataPublicacion){
		    						    		    			if (dataPublicacion){ //Si hay datos para la tabla
		    						    		    				tablePublicaciones.setData(dataPublicacion);
		    						    		    			}	    			
		    						    	            	});
			    						            		
			    						            		disableControl('BtnVisorPlanesTramitesEditPublicaciones');
			    					    			    	disableControl('BtnVisorPlanesTramitesRemovePublicaciones');
			    						            		
				    					                    $('VisorPLANESWindowEditPublicacionTramite').retrieve('instance').close();
				    						            }
				    						        }.bind(this),
				    						        onFailure: function(){
				    						        	alert('Error modificando la publicación');
				    						        },
				    						        onCancel: function(){
				    						        	alert('Petición cancelada');
				    						        }
				    						    });
				    						    req.send();		    						    
				    						});
			    						} //Fin del else de si no hay boletines				    					
			    					} //Fin del onContentLoad			
				    			});
				    			}; //Fin var window
				    			
				    			visorPLANESCurrentWindow(); //Muestro la ventana
	    					}
		    			}	    					    					
		    		});
		    		
		    		disableControl('BtnVisorPlanesTramitesRemovePublicaciones');
		    		$('BtnVisorPlanesTramitesRemovePublicaciones').addEvent('click',function(e){
		    			if ($('BtnVisorPlanesTramitesRemovePublicaciones').disabled == false){
		    				if (tablePublicaciones){
		    					if (tablePublicaciones.selected && tablePublicaciones.selected.length > 0) {
		    						var publicacion = tablePublicaciones.getDataByRow(tablePublicaciones.selected[0]);
		    						
		    						var req = new Request.JSON({
	    						        url: 'GestionConsola',
	    						        data: {
	    						            wsName: 'BORRAR_PUBLICACION',
	    						            id: publicacion.id,
	    						        },
	    						        onSuccess: function(res){
	    						        	if (res.error){
	    						            	alert('Error eliminando la publicación:\n'+res.result);
	    						            } else {
	    						            	MUI.notification('Publicaci&oacute;n eliminada correctamente');	    						            	
	    						            	tablePublicaciones.deleteRow(tablePublicaciones.selected[0]);
	    						            	
	    						            	disableControl('BtnVisorPlanesTramitesEditPublicaciones');
	    				    			    	disableControl('BtnVisorPlanesTramitesRemovePublicaciones');
	    						            }
	    						        }.bind(this),
	    						        onFailure: function(){
	    						        	alert('Error eliminando la publicación seleccionada');
	    						        },
	    						        onCancel: function(){
	    						        	alert('Petición cancelada');
	    						        }
	    						    });
	    						    req.send();	
		    						
			    			    } else {
			    			    	MUI.notification('No se ha seleccionado ninguna publicaci&oacute;n en la tabla');
			    			    }
		    			    }		    			    
		    			}
		    		});
	    		} //Fin de si es gestor de planes	    		
	    		
            } //Fin de si hay panel datos tramites publicaciones
    		
    		MUI.rWidth();
		});          
    
    return result;
    
}

function chargeTabTamitesDeterminaciones(data){
	
	//ANADO EL TREE DE LAS DETERMINACIONES - dos columnas, izquierda arbol y derecha TABs
	$('panelTramitesDeterminaciones').setStyle('height',(parseInt($('tabTramites-panel').getStyle('height')) - 2)+'px');
	
	var anchoPrincipal = $('mainColumn').getStyle('width');
	
    //Setup columns and panels
    new MUI.Column({
    	container: 'panelTramitesDeterminaciones',
        id: 'leftSideColumnDeterminaciones',
        placement: 'left',
        width: 250,
        resizeLimit: [200, parseInt(anchoPrincipal) - 60],
        sortable: false,
    });
    
    new MUI.Column({
    	container: 'panelTramitesDeterminaciones',
        id: 'mainColumnDeterminaciones',
        placement: 'main',
        sortable: false,
    });		        	    	        	          	    	        	   

	 new MUI.Panel({
			id: 'left-panel-determinaciones',
			title: '',
			height: $('panelTramitesDeterminaciones').getStyle('height'),
			contentURL: 'Pages/VisorPLANES/Tramites/Determinaciones/TreeViewerDeterminaciones.html',
			column: 'leftSideColumnDeterminaciones',
			collapsible: false,
			header: true,
            headerToolbox: true,
    		headerToolboxURL: 'Pages/VisorPLANES/Tramites/Determinaciones/TreeViewerToolBoxDeterminaciones.html',
    		headerToolboxOnload: function(){
    			if ($('BtnVisorPlanesDeterminacionesExpandir')){
		        	$('BtnVisorPlanesDeterminacionesExpandir').addEvent('click',  function () { 
		        		//visorPLANESTree.expand(); ESTO SERIA UN EXPAND ALL
		        		if (visorPLANESTreeDeterminaciones.selected){
		        			if (visorPLANESTreeDeterminaciones.selected.data.tipo || visorPLANESTreeDeterminaciones.root.text == visorPLANESTreeDeterminaciones.selected.text){ //Expando a el y el siguiente
		        				visorPLANESTreeDeterminaciones.selected.toggle(false, true);
		        				visorPLANESTreeDeterminaciones.selected.nodes.forEach( function(node) { node.toggle(false, true); });
		        			} else { //Expando solo a el
		        				visorPLANESTreeDeterminaciones.selected.toggle(false, true);
		        			}
		        		}
		    		});
		        	$('BtnVisorPlanesDeterminacionesContraer').addEvent('click',  function () { visorPLANESTreeDeterminaciones.collapse(); });
		        	$('BtnVisorPlanesDeterminacionesBuscar').addEvent('click',  BtnVisorPlanesBuscarDeterminaciones);    	    	
		        }
    		},
    		onContentLoaded: function(){
				visorPLANESTreeDeterminaciones = addTreeDeterminaciones(data, undefined,'treeDeterminaciones', 'mainColumnDeterminaciones', 'tabTramitesDeterminaciones');
				$('mainColumnDeterminaciones').setStyle('width',(parseInt($('panelTramitesDeterminaciones').getStyle('width')) - parseInt($('leftSideColumnDeterminaciones').getStyle('width')) - parseInt($('leftSideColumnDeterminaciones_handle').getStyle('width')) -2)+'px');								
				
				MUI.rWidth();
			}
		}); 
}

function chargeTabTramitesEntidades(data){
	
	//ANADO EL TREE DE LAS ENTIDADES - dos culumnas, izquierda arbol y derecha TABs
	$('panelTramitesEntidades').setStyle('height',(parseInt($('tabTramites-panel').getStyle('height')) - 2)+'px');
	
	var anchoPrincipal = $('mainColumn').getStyle('width');
	
	//Setup columns and panels
    new MUI.Column({
    	container: 'panelTramitesEntidades',
        id: 'leftSideColumnEntidades',
        placement: 'left',
        width: 250,
        resizeLimit: [200, parseInt(anchoPrincipal) - 60],
        sortable: false,
    });
    new MUI.Column({
    	container: 'panelTramitesEntidades',
        id: 'mainColumnEntidades',
        placement: 'main',
        sortable: false,
    });	
    
    new MUI.Panel({
		id: 'left-panel-entidades',
		title: '',
		height: $('panelTramitesEntidades').getStyle('height'),
		contentURL: 'Pages/VisorPLANES/Tramites/Entidades/TreeViewerEntidades.html',
		column: 'leftSideColumnEntidades',
		collapsible: false,
		header: true,
        headerToolbox: true,
		headerToolboxURL: 'Pages/VisorPLANES/Tramites/Entidades/TreeViewerToolBoxEntidades.html',
		headerToolboxOnload: function(){
			if ($('BtnVisorPlanesEntidadesExpandir')){
	        	$('BtnVisorPlanesEntidadesExpandir').addEvent('click',  function () { 
	        		//visorPLANESTree.expand(); ESTO SERIA UN EXPAND ALL
	        		if (visorPLANESTreeEntidades.selected){
	        			if (visorPLANESTreeEntidades.selected.data.tipo || visorPLANESTreeEntidades.root.text == visorPLANESTreeEntidades.selected.text){ //Expando a el y el siguiente
	        				visorPLANESTreeEntidades.selected.toggle(false, true);
	        				visorPLANESTreeEntidades.selected.nodes.forEach( function(node) { node.toggle(false, true); });
	        			} else { //Expando solo a el
	        				visorPLANESTreeEntidades.selected.toggle(false, true);
	        			}
	        		}
	    		});
	        	$('BtnVisorPlanesEntidadesContraer').addEvent('click',  function () { visorPLANESTreeEntidades.collapse(); });
	        	$('BtnVisorPlanesEntidadesBuscar').addEvent('click',  BtnVisorPlanesBuscarEntidades);    	    	
	        }
		},
		onContentLoaded: function(){
			visorPLANESTreeEntidades = addTreeEntidades(data, undefined,'treeEntidades','mainColumnEntidades','tabTramitesEntidades');
			$('mainColumnEntidades').setStyle('width',(parseInt($('panelTramitesEntidades').getStyle('width')) - parseInt($('leftSideColumnEntidades').getStyle('width')) - parseInt($('leftSideColumnEntidades_handle').getStyle('width')) -2)+'px');						
			
			MUI.rWidth();
		}
	});
}

function chargeTabTramitesDocumentos(data){
	
	var result = false;
	
	//PANELES HORIZONTALES GRID - TEXTO - DBL CLICK SOBRE GRID ABRE DOCUMENTO
    $('panelTramitesDocumentos').setStyle('height', (parseInt($('tabTramites-panel').getStyle('height')) - 4) + 'px');
    addSplitPanel('panelTramitesDocumentos', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function() {	    	    		
    	
	    	var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesDocumentos').getStyle('height');
		    var tableHeight = calculateTableHeight(data.total, supPanelHeight);
    	
		    result = addTable(data, undefined, undefined, viewerPlanesTypeDocumentos, 'fspElement' + 'panelTramitesDocumentos', true, '100%', tableHeight, 'tabTramitesDocumentos',
    			function(info) { //CLICK
    			    if (info && $('sspElement' + 'panelTramitesDocumentos')) {
    			        var dataRow = info.target.getDataByRow(info.row);
    			        if (dataRow) {
    			        	
    			        	if (!dataRow.comentario || dataRow.comentario.length == 0 || dataRow.comentario == textoAsociadoVisorPlanes){
    			        		dataRow.comentario = textoAsociadoVisorPlanes;
    			        		$('sspElement' + 'panelTramitesDocumentos').addClass('infomsg');
    			        	} else {
    			        		$('sspElement' + 'panelTramitesDocumentos').removeClass('infomsg');
    			        	}
    			        	$('sspElement' + 'panelTramitesDocumentos').set('html', dataRow.comentario);
    			        	
    			        }
    			    }
    			},
    			function(info) { //DBL CLICK
    				openGridDocument(info);    			  
    			});
		    
		    MUI.rWidth();
		});        
    
    return result;
}

function addTramitesTadLoad(){
	countTramitesTabLoad = countTramitesTabLoad + 1;
}

function addTramitesDeterminacionesTabLoad(){
	countTramitesDeterminacionesTabLoad = countTramitesDeterminacionesTabLoad + 1;
}

function addTramitesEntidadesTabLoad(){
	countTramitesEntidadesTabLoad = countTramitesEntidadesTabLoad + 1;
}

//********************************* eventos de la botonera ***********************************************

function BtnVisorPlanesAddPlanes(){
	//Depende del tipo de elemento seleccionado se actua
	if ($('BtnVisorPlanesAddPlanes').disabled == false && visorPLANESTree.selected){
		
		visorPLANESTree.selected.toggle(false, true);
		
		visorPLANESCurrentWindow = function(){			
			new MUI.Window({
			id: 'VisorPLANESWindowAddPlan',
			title: 'Crear nuevo Plan',
			icon: 'styles/images/addPlan.png',			
			contentURL: 'Pages/VisorPLANES/VisorPLANESWindows/VisorPLANESWindowAddPlan.html',
			width: 660,
			height: 515,
			maximizable: false,
			resizable: false,
			scrollbars: false,
			onContentLoaded: function(){					
				var tipoLlamadaCargaPlanesOperados = "wsName=GET_NODOS_PLANES";
				
				//Asigno el valor del idAmbito, idPadre e idBase asociado
				if (visorPLANESTree.selected && visorPLANESTree.selected.data){
					//idPadre: sobre el que se crea si es plan
					if (visorPLANESTree.selected.data.tipo == 'plan'){
						$('idPadre').value = visorPLANESTree.selected.data.idNode;
					} else {
						$('idPadre').value = null;
					}
					//idBase, fijo, lo asigna el servidor
					
					//idAmbito hay que buscarlo
					var node = visorPLANESTree.selected;
					while (node && node.data.tipo != 'ambito'){							
						node = node.parent; //Subo un nivel
					}
					if (node && node.data.tipo == 'ambito'){
						tipoLlamadaCargaPlanesOperados = "wsName=GET_NODOS_PLANES_DE_AMBITO&idAmbito="+node.data.idNode;
						$('idAmbito').value = node.data.idNode;
					}
				}
				
				//Compruebo que se ha definido el ambito
				if (isNumeric($('idAmbito').value) == false){
					alert('No ha sido posible determinar el identificador del ámbito asociado al plan a crear');
					$('VisorPLANESWindowAddPlan').retrieve('instance').close();
					return;
				}
				
				//LISTA DE INSTRUMENTOS 
				var result = runServiceJson({wsName: "GET_DATA", TIPO: "InstrumentoPlan"});
				loadDataOnSelect('idInstrumento',result,true,undefined,'No ha sido posible cargar la lista de instrumentos');					
				
				disableControl('planesOperados');
				//ARBOL DE PLANES					
				visorPLANESAddPlanTree = new MooTreeControl({
			        div: 'panelPlanesOperadosTree',
			        mode: 'folders',
			        treeType: 'planes',
			        iconBar: ['javascriptsV2/mooTree2/Planes.gif'],
			        grid: true,
			        onSelect: function(node, state) {
			        	if (visorPLANESAddPlanTree.enabled == true && state && node.data.tipo == 'plan' ) {
			        		enableControl('BtnVisorPlanesAddPlanesOperados');
			            } else { disableControl('BtnVisorPlanesAddPlanesOperados'); }
			        }
			    	},{
			        text: cargarTextoSegunIdioma("LISTADO DE PLANES"),
			        open: true            
			    });      
			    
			    $('panelPlanesOperadosTree').setStyle('height','220px');
			    $('panelPlanesOperadosTree').setStyle('width','300px');

			    visorPLANESAddPlanTree.root.load('GestionConsola?'+tipoLlamadaCargaPlanesOperados+'&op='+escape(globalConfig.op_consola)+'&tipo=TODOS&idioma='+idioma);				   
				
				//LISTA DE TIPOS DE OPERACION PLAN
			    $('idInstrumento').addEvent('change',function(){
			    	if ($('idInstrumento').selectedIndex > 0){
			    		var idInstrumento = $('idInstrumento').options[$('idInstrumento').selectedIndex].value;
				    	select = $('idTipoOperacion');
				    	select.empty(); //Elimimo lo que hubiera
						result = runServiceJson({wsName: "GET_DATA", TIPO: "OperacionesInstrumentoPlan", ID: idInstrumento});
						if (result && result.data && select){		
							if (result.data.length > 0){
								for(var rowidx=0,len=result.data.length; rowidx<len; rowidx++){
			    	                
			    	                var rowValue = result.data[rowidx];
			    	                
			    	                option = new Element('option',{});
			    	                option.text = rowValue.nombre;
			    	                option.value = rowValue.iden;
			    	                
			    	                try {
			    	                	select.add(option, null); // standards compliant; doesn't work in IE
			    	                  }
			    	                  catch(ex) {
			    	                	  select.add(option); // IE only
			    	                  }	                
								}
								enableControl('BtnVisorPlanesGuardarNuevoPlan');
								enableControl('planesOperados');
							} else {
								alert('No se han encontrado operaciones para el instrumento seleccionado, por lo que no se permite crear planes con este instrumento');
								disableControl('BtnVisorPlanesGuardarNuevoPlan');
								disableControl('planesOperados');
							}														
						} else {
							MUI.notification('No ha sido posible cargar la lista de tipos de operaci&oacute;n en función del instrumento seleccionado');
						}
			    	} else {
			    		select.empty(); //Elimimo lo que hubiera
			    		disableControl('BtnVisorPlanesGuardarNuevoPlan');
						disableControl('planesOperados');
			    	}			    	
			    });
				
				//BOTON DE ANADIR
				disableControl('BtnVisorPlanesAddPlanesOperados');
				$('BtnVisorPlanesAddPlanesOperados').addEvent('click',function(e){
					new Event(e).stop();
					
					if ($('BtnVisorPlanesAddPlanesOperados').disabled == false){
						var mydata = visorPLANESAddPlanPlanesSelected.getData();
						if (!mydata) { mydata = []; }
						
						//Si ya esta en la lista no lo anado
				        for(var i = 0; i < mydata.length; i++) {
				            if(mydata[i] && mydata[i].id === visorPLANESAddPlanTree.selected.data.idNode + $('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].value) {
				            	alert('La lista de planes operados ya contiene un plan de nombre '+ visorPLANESAddPlanTree.selected.text +' y operación ' + $('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].text);
				                return;
				            }
				        }												
						
						//Obtengo los datos del plan seleccionado y del tipo de operacion seleccionada
						mydata.push({
		                    'id': visorPLANESAddPlanTree.selected.data.idNode + $('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].value,
		                    'text': visorPLANESAddPlanTree.selected.text + ' ('+$('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].text+')',
		                    'data': {
		                        'idPlan': visorPLANESAddPlanTree.selected.data.idNode,
		                        'tipoOperacionPlan': $('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].value
		                    }
		                });

						visorPLANESAddPlanPlanesSelected.setData({
		                    data: mydata
		                });
						
						disableControl('idInstrumento');
					}						
					
				});
				
				//LISTADO DE PLANES OPERADOS SELECCIONADOS
				visorPLANESAddPlanPlanesSelected = new mooList('panelPlanesOperadosList', {
			        //title: 'Planes operados seleccionados',
			        alternaterows: true,
			        width: '100%',
			        height: '240',
			        rowbuttons: [
			        {
			            'name': 'Quitar plan',
			            'bclass': 'removeRoleButton',
			            'onclick': function(liData)
			            { 
			            	visorPLANESAddPlanPlanesSelected.removeRowById(liData.id);
			            	if (!visorPLANESAddPlanPlanesSelected.getData() || visorPLANESAddPlanPlanesSelected.getData().length == 0){
			            		enableControl('idInstrumento');
			            	}
			            }
			        }]
			    });	
				
				$('BtnVisorPlanesCancelarNuevoPlan').addEvent('click', function(e){
		        	new Event(e).stop();
		        	$('VisorPLANESWindowAddPlan').retrieve('instance').close();
		        });
				enableControl('BtnVisorPlanesGuardarNuevoPlan');
				$('BtnVisorPlanesGuardarNuevoPlan').addEvent('click', function(e){
					new Event(e).stop();
					
					if ($('BtnVisorPlanesGuardarNuevoPlan').disabled == false){
						if($('txtNombre').value.length == 0 || $('idInstrumento').selectedIndex == 0) {
			            	alert('El nombre del plan y el instrumento son datos obligatorios');
			                return;
			            }
						
						var listaPlanesOperadosParam='';
						var listaTipoOperacionOperadosParam='';
						var mydata = visorPLANESAddPlanPlanesSelected.getData();
						if (mydata) {
							//Calculo la lista de planes operados y los tipos de operacion
							for(var i = 0; i < mydata.length; i++) {
								
					            if(mydata[i]) {
					            	if (listaPlanesOperadosParam.length == 0){
					            		listaPlanesOperadosParam = mydata[i].data.idPlan;
					            	} else {
					            		listaPlanesOperadosParam = listaPlanesOperadosParam + ',' + mydata[i].data.idPlan;
					            	}					           
					            	if (listaTipoOperacionOperadosParam.length ==0){
					            		listaTipoOperacionOperadosParam = mydata[i].data.tipoOperacionPlan;
					            	} else {
					            		listaTipoOperacionOperadosParam = listaTipoOperacionOperadosParam + ',' + mydata[i].data.tipoOperacionPlan;
					            	}
					            }
					        }
						}						
						
						var req = new Request.JSON({
					        url: 'GestionConsola',
					        data: {
					            wsName: 'ADD_PLAN',
					            idAmbito: $('idAmbito').value,
					            idPadre: $('idPadre').value,
					            idBase: $('idBase').value,
					            nombre: $('txtNombre').value,
					            texto: $('txtTexto').value,
					            idInstrumento: $('idInstrumento').options[$('idInstrumento').selectedIndex].value,
					            listaPlanesOperados: listaPlanesOperadosParam,
					            listaTipoOperacionOperados: listaTipoOperacionOperadosParam,
					        },
					        onSuccess: function(res){
					        	
					        	if (res.error){
					            	alert('Error guardando los datos de plan:\n'+res.error);
					            } else {
					            	MUI.notification('Datos guardados correctamente');
					            	
					            	// anadir el nodo en el arbol a partir de los datos del formulario
				                    var currNode = visorPLANESTree.selected;
				                    
				                    switch (visorPLANESviewerTreeType) {		
				            	    case viewerTreeType.rpm: //consolidados
				            	    	alert('El nuevo plan creado solo es visible en el visor de planes NO consolidados');
				            	    	break;
				            	    case viewerTreeType.validacion: //NO consolidados
				            	    default:
				            	    	currNode.load(currNode.data.load);
				            	    	break;
				            		}				                    
				                    
				                    $('VisorPLANESWindowAddPlan').retrieve('instance').close();
					            }
					            
					        }.bind(this),
					        onFailure: function(){
					        	alert('Error guardando los datos del plan');
					        },
					        onCancel: function(){
					        	alert('Petición cancelada');
					        }
					    });

					    req.send();
					} //end if de si no enable el boton
				});
			}				
		});			
		}; //fin var window
		
		visorPLANESCurrentWindow(); //Muestro la ventana
		
	} //Fin if
} //BtnVisorPlanesAddPlanes

function BtnVisorPlanesEditPlanes(){
	//Depende del tipo de elemento seleccionado se actua
	if ($('BtnVisorPlanesEditPlanes').disabled == false && visorPLANESTree.selected){
		
		visorPLANESTree.selected.toggle(false, true);
		
		visorPLANESCurrentWindow = function(){			
			new MUI.Window({
			id: 'VisorPLANESWindowEditPlan',
			title: 'Editar Plan',
			icon: 'styles/images/ModificarPlan.png',			
			contentURL: 'Pages/VisorPLANES/VisorPLANESWindows/VisorPLANESWindowEditPlan.html',
			width: 660,
			height: 515,
			maximizable: false,
			resizable: false,
			scrollbars: false,
			onContentLoaded: function(){													
				
				//CREO LA ESTRUCTURA DEL ARBOL DE PLANES QUE SE PUEDEN OPERAR				
				visorPLANESAddPlanTree = new MooTreeControl({
			        div: 'panelPlanesOperadosTree',
			        mode: 'folders',
			        treeType: 'planes',
			        iconBar: ['javascriptsV2/mooTree2/Planes.gif'],
			        grid: true,
			        onSelect: function(node, state) {
			        	if (visorPLANESAddPlanTree.enabled == true && state && node.data.tipo == 'plan' ) {
			        		enableControl('BtnVisorPlanesAddPlanesOperados');
			            } else { disableControl('BtnVisorPlanesAddPlanesOperados'); }
			        }
			    	},{
			        text: cargarTextoSegunIdioma("LISTADO DE PLANES"),
			        open: true            
			    });      			    
			    $('panelPlanesOperadosTree').setStyle('height','220px');
			    $('panelPlanesOperadosTree').setStyle('width','300px');
			    
			    //CREO LA ESTRUCTURA DE LA LISTA DE PLANES QUE ESTAN SIENDO OPERADOS
				visorPLANESAddPlanPlanesSelected = new mooList('panelPlanesOperadosList', {
			        //title: 'Planes operados seleccionados',
			        alternaterows: true,
			        width: '100%',
			        height: '240',
			        rowbuttons: [
			        {
			            'name': 'Quitar plan',
			            'bclass': 'removeRoleButton',
			            'onclick': function(liData)
			            { 
			            	visorPLANESAddPlanPlanesSelected.removeRowById(liData.id);
			            	if (!visorPLANESAddPlanPlanesSelected.getData() || visorPLANESAddPlanPlanesSelected.getData().length == 0){
			            		enableControl('idInstrumento');
			            	}
			            }
			        }]
			    });	
				
				//Cargo los planes que el plan opera sobre la lista creada
				var planesOperados = runServiceJson({'wsName' : 'GET_DATA','ID' : visorPLANESTree.selected.data.idNode,'TIPO' : 'PlanOperaA'});
				if (planesOperados != null && planesOperados.data != null && planesOperados.data.length > 0){
					var mydata = visorPLANESAddPlanPlanesSelected.getData();
					if (!mydata) { mydata = []; }
					
					for(var i = 0; i < planesOperados.data.length; i++) {
						
			            if(planesOperados.data[i]) {
			            	mydata.push({
			                    'id': planesOperados.data[i].iden + planesOperados.data[i].idoperacion,
			                    'text': planesOperados.data[i].planOperado + ' ('+planesOperados.data[i].operacion+')',
			                    'data': {
			                        'idPlan': planesOperados.data[i].iden,
			                        'tipoOperacionPlan': planesOperados.data[i].idoperacion
			                    }
			                });
			            }
			        }
					
					visorPLANESAddPlanPlanesSelected.setData({
	                    data: mydata
	                });
				}
				
				//EVENTO DE MODFICACION DEL COMBO DE INSTRUMENTOS
			    $('idInstrumento').addEvent('change',function(){
			    	var select = undefined;
			    	if ($('idInstrumento').selectedIndex > 0){
			    		var idInstrumento = $('idInstrumento').options[$('idInstrumento').selectedIndex].value;
				    	select = $('idTipoOperacion');
				    	select.empty(); //Elimimo lo que hubiera
						result = runServiceJson({wsName: "GET_DATA", TIPO: "OperacionesInstrumentoPlan", ID: idInstrumento});
						if (result && result.data && select){		
							if (result.data.length > 0){
								for(var rowidx=0,len=result.data.length; rowidx<len; rowidx++){
			    	                
			    	                var rowValue = result.data[rowidx];
			    	                
			    	                option = new Element('option',{});
			    	                option.text = rowValue.nombre;
			    	                option.value = rowValue.iden;
			    	                
			    	                try {
			    	                	select.add(option, null); // standards compliant; doesn't work in IE
			    	                  }
			    	                  catch(ex) {
			    	                	  select.add(option); // IE only
			    	                  }	                
								}																
								
								enableControl('BtnVisorPlanesGuardarEditPlan');
								enableControl('planesOperados');
							} else {
								alert('No se han encontrado operaciones para el instrumento seleccionado, por lo que no se permite editar planes con este instrumento');
								disableControl('BtnVisorPlanesGuardarEditPlan');
								disableControl('planesOperados');
							}														
						} else {
							MUI.notification('No ha sido posible cargar la lista de tipos de operaci&oacute;n en función del instrumento seleccionado');
						}
			    	} else {
			    		if (select != undefined){
			    			select.empty(); //Elimimo lo que hubiera
			    		}			    		
			    		disableControl('BtnVisorPlanesGuardarEditPlan');
						disableControl('planesOperados');
			    	}			    	
			    });																								
				
				//CARGO LOS DATOS DEL PLAN QUE SE ESTA EDITANDO
				
				//Obtengo el plan seleccionado
				var dataSelected = undefined;
				if (visorPLANESTree.selected && visorPLANESTree.selected.data){
					dataSelected = visorPLANESTree.selected.data;																		
					
					//Asigno el valor del idAmbito, idPadre, idBase y idInstrumento asociado				
					getFormData(dataSelected.idNode, 'plan', true, undefined, 
	                		function(data){
							if (data.data && data.data.length > 0){
								data = data.data[0];
							}
							
							//Requiere del idAmbito para poder cargar los datos
							var tipoLlamadaCargaPlanesOperados = "wsName=GET_NODOS_PLANES";							
							
							//idAmbito
							if (data.idAmbito){
								$('idAmbito').value = data.idAmbito;
								tipoLlamadaCargaPlanesOperados = "wsName=GET_NODOS_PLANES_DE_AMBITO&idAmbito="+data.idAmbito;
							} else {
								$('idAmbito').value = null;
							}
							//idPadre
							if (data.idPadre){
								$('idPadre').value = data.idAmbito;
							} else {
								$('idPadre').value = null;
							}
							//idBase
							if (data.idBase){
								$('idBase').value = data.idBase;
							} else {
								$('idBase').value = null;
							}
							
							//nombre						
							$('txtNombre').value = data.nombre;		
							
							//texto						
							$('txtTexto').value = data.texto;							
							
							//idInstrumento
							var idInstrumento = undefined;
							if (data.idInstrumento){
								idInstrumento = data.idInstrumento;
							}
							
							//LISTA DE PLANES OPERADOS
							//CARGO LOS DATOS DEL ARBOL DE PLANES CON POSIBILIDAD DE OPERAR
						    visorPLANESAddPlanTree.root.load('GestionConsola?'+tipoLlamadaCargaPlanesOperados+'&op='+escape(globalConfig.op_consola)+'&tipo=TODOS&idioma='+idioma);
							
							//LISTA DE INSTRUMENTOS 
							var result = runServiceJson({wsName: "GET_DATA", TIPO: "InstrumentoPlan"});
							loadDataOnSelect('idInstrumento',result,true,idInstrumento,'No ha sido posible cargar la lista de instrumentos');
							
							//Simulo el evento del combo de instrumentos por si estuviera seleccionado
							$('idInstrumento').fireEvent('change');
						});	
				}							
				
				//EVENTOS DE LOS BOTONES
				disableControl('BtnVisorPlanesAddPlanesOperados');
				$('BtnVisorPlanesAddPlanesOperados').addEvent('click',function(e){
					new Event(e).stop();
					
					if ($('BtnVisorPlanesAddPlanesOperados').disabled == false){
						var mydata = visorPLANESAddPlanPlanesSelected.getData();
						if (!mydata) { mydata = []; }
						
						//Si ya esta en la lista no lo anado
				        for(var i = 0; i < mydata.length; i++) {
				            if(mydata[i] && mydata[i].id === visorPLANESAddPlanTree.selected.data.idNode + $('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].value) {
				            	alert('La lista de planes operados ya contiene un plan de nombre '+ visorPLANESAddPlanTree.selected.text +' y operación ' + $('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].text);
				                return;
				            }
				        }												
						
						//Obtengo los datos del plan seleccionado y del tipo de operacion seleccionada
						mydata.push({
		                    'id': visorPLANESAddPlanTree.selected.data.idNode + $('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].value,
		                    'text': visorPLANESAddPlanTree.selected.text + ' ('+$('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].text+')',
		                    'data': {
		                        'idPlan': visorPLANESAddPlanTree.selected.data.idNode,
		                        'tipoOperacionPlan': $('idTipoOperacion').options[$('idTipoOperacion').selectedIndex].value
		                    }
		                });

						visorPLANESAddPlanPlanesSelected.setData({
		                    data: mydata
		                });
						
						disableControl('idInstrumento');
					}						
					
				});
				
				$('BtnVisorPlanesCancelarEditPlan').addEvent('click', function(e){
		        	new Event(e).stop();
		        	$('VisorPLANESWindowEditPlan').retrieve('instance').close();
		        });
				
				$('BtnVisorPlanesGuardarEditPlan').addEvent('click', function(e){
					new Event(e).stop();
					
					if ($('BtnVisorPlanesGuardarEditPlan').disabled == false){
						if($('txtNombre').value.length == 0 || $('idInstrumento').selectedIndex == 0) {
			            	alert('El nombre del plan y el instrumento son datos obligatorios');
			                return;
			            }
						
						var listaPlanesOperadosParam='';
						var listaTipoOperacionOperadosParam='';
						var mydata = visorPLANESAddPlanPlanesSelected.getData();
						if (mydata) {
							//Calculo la lista de planes operados y los tipos de operacion
							for(var i = 0; i < mydata.length; i++) {
								
					            if(mydata[i]) {
					            	if (listaPlanesOperadosParam.length == 0){
					            		listaPlanesOperadosParam = mydata[i].data.idPlan;
					            	} else {
					            		listaPlanesOperadosParam = listaPlanesOperadosParam + ',' + mydata[i].data.idPlan;
					            	}					           
					            	if (listaTipoOperacionOperadosParam.length ==0){
					            		listaTipoOperacionOperadosParam = mydata[i].data.tipoOperacionPlan;
					            	} else {
					            		listaTipoOperacionOperadosParam = listaTipoOperacionOperadosParam + ',' + mydata[i].data.tipoOperacionPlan;
					            	}
					            }
					        }
						}						
						
						var req = new Request.JSON({
					        url: 'GestionConsola',
					        data: {
					            wsName: 'ADD_PLAN',
					            idPlan: visorPLANESTree.selected.data.idNode,
					            idAmbito: $('idAmbito').value,
					            idPadre: $('idPadre').value,
					            idBase: $('idBase').value,
					            nombre: $('txtNombre').value,
					            texto: $('txtTexto').value,
					            idInstrumento: $('idInstrumento').options[$('idInstrumento').selectedIndex].value,
					            listaPlanesOperados: listaPlanesOperadosParam,
					            listaTipoOperacionOperados: listaTipoOperacionOperadosParam,
					        },
					        onSuccess: function(res){
					        	
					        	if (res.error){
					            	alert('Error guardando los datos de plan:\n'+res.error);
					            } else {
					            	MUI.notification('Datos guardados correctamente');
					            	
					            	//Recargo el nodo padre para que muestre los datos correctos
				                    var currNode = visorPLANESTree.selected;
				                    if (currNode.parent){
				                    	visorPLANESTree.select(currNode.parent);
				                    	visorPLANESTree.selected.load(visorPLANESTree.selected.data.load);
				                    }				                    				                   
				                    
				                    $('VisorPLANESWindowEditPlan').retrieve('instance').close();
					            }
					            
					        }.bind(this),
					        onFailure: function(){
					        	alert('Error guardando los datos del plan');
					        },
					        onCancel: function(){
					        	alert('Petición cancelada');
					        }
					    });

					    req.send();
					} //end if de si no enable el boton
				});				
				
			} //fin onContentLoad				
		});			
		}; //fin var window
		
		visorPLANESCurrentWindow(); //Muestro la ventana
		
	} //Fin if
} //BtnVisorPlanesEditPlanes

function BtnVisorPlanesAddTramites(){
	//Depende del tipo de elemento seleccionado se actua
	if ($('BtnVisorPlanesAddTramites').disabled == false && visorPLANESTree.selected){
		
		visorPLANESTree.selected.toggle(false, true);
		
		visorPLANESCurrentWindow = function(){ 
			new MUI.Window({
			id: 'VisorPLANESWindowAddTramite',
			title: 'Crear nuevo Trámite',
			icon: 'styles/images/addTramite.png',			
			contentURL: 'Pages/VisorPLANES/VisorPLANESWindows/VisorPLANESWindowAddTramite.html',
			width: 630,
			height: 430,
			maximizable: false,
			resizable: false,
			scrollbars: false,
			onContentLoaded: function(){	
				$$('input').each(function(input){
					if (input.get('data-meiomask') != null){
						input.meiomask(input.get('data-meiomask'));
					}					
				});
				
				//Asigno el valor del idPlan asociado
				if (visorPLANESTree.selected && visorPLANESTree.selected.data.idNode){
					$('idPlan').value = visorPLANESTree.selected.data.idNode;
				} else {
					alert('No ha sido posible determinar el identificador del plan asociado al trámite a crear');
					$('VisorPLANESWindowAddTramite').retrieve('instance').close();
					return;
				}
				
				//TIPOS DE TRAMITES
				var result = runServiceJson({wsName: "GET_DATA", TIPO: "TipoTramite"});
				loadDataOnSelect('idTipoTramite',result,false,undefined,'No ha sido posible cargar la lista de tipos de tr&aacute;mites');
				
				//ORGANO
				result = runServiceJson({wsName: "GET_DATA", TIPO: "Organo"});
				loadDataOnSelect('idOrgano',result,false,undefined,'No ha sido posible cargar la lista de &oacute;rganos');
				
				//SENTIDO
				result = runServiceJson({wsName: "GET_DATA", TIPO: "Sentido"});
				loadDataOnSelect('idSentido',result,false,undefined,'No ha sido posible cargar la lista de sentidos');
				
				//FECHA
				//Setup calendar (Esto crea un button calendar)
		        new Calendar({ txtFecha: 'd/m/Y' }, { classes: ['dashboard'],
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
				
				//CENTROS DE PRODUCCION
		        result = runServiceJson({wsName: "GET_DATA", TIPO: "CentroProduccion"});
				loadDataOnSelect('idCentroProduccion',result,false,undefined,'No ha sido posible cargar la lista de centros de producci&oacute;n');
				
				$('BtnVisorPlanesCancelarNuevoTramite').addEvent('click', function(e){
		        	new Event(e).stop();
		        	$('VisorPLANESWindowAddTramite').retrieve('instance').close();
		        });
				$('BtnVisorPlanesGuardarNuevoTramite').addEvent('click', function(e){
					new Event(e).stop();
					
					if($('txtNombre').value.length == 0) {
		            	alert('El nombre del trámite es una dato obligatorio');
		                return;
		            }
					
					var req = new Request.JSON({
				        url: 'GestionConsola',
				        data: {
				            wsName: 'ADD_TRAMITE',
				            idPlan: $('idPlan').value,
				            nombre: $('txtNombre').value,
				            idTipoTramite: $('idTipoTramite').options[$('idTipoTramite').selectedIndex].value,
				            organo: $('idOrgano').options[$('idOrgano').selectedIndex].value,
				            sentido: $('idSentido').options[$('idSentido').selectedIndex].value,
				            fecha: $('txtFechaVisible').value,
				            idCentroProduccion: $('idCentroProduccion').options[$('idCentroProduccion').selectedIndex].value,					            
				            texto: $('txtTexto').value,
				            comentario: $('txtComentario').value
				        },
				        onSuccess: function(res){
				        	
				        	if (res.error){
				            	alert('Error guardando los datos de trámite:\n'+res.error);
				            } else {
				            	MUI.notification('Datos guardados correctamente');
				            					            					            					            
				            	// anadir el nodo en el arbol a partir de los datos del formulario
			                    var currNode = visorPLANESTree.selected;
			                    
			                    switch (visorPLANESviewerTreeType) {		
			            	    case viewerTreeType.rpm: //consolidados
			            	    	alert('El nuevo trámite creado solo es visible en el visor de planes NO consolidados');
			            	    	break;
			            	    case viewerTreeType.validacion: //NO consolidados
			            	    default:
			            	    	currNode.load(currNode.data.load);
			            	    	break;
			            		}
			                    
			                    /*
			                    if (currNode){			                    	
			                    	//Primero hay que comprobar si el nodo tiene ya una carpeta de tramites
		                    		var encontrado = false;
		                    		var i = 0;
			                    	if (currNode.nodes.length > 0) {					                    		
			                    		for(i = 0; i < currNode.nodes.length; i++) {
			                    			if (currNode.nodes[i].text.substring(0, 1).toUpperCase() == 'T'){
			                    				encontrado = true;
			                    				break;
			                    			}
								        }
			                    	}
			                    	if (encontrado == true){
			                    		//Selecciono la carpeta de plan como la current
			                    		currNode = currNode.nodes[i];
			                    	} else {
			                    		//Creo una carpeta de tramite
			                    		currNode = currNode.insert({
				                            data: {
				                                load: '',
				                            },
				                            text: 'Trámites (0)',
				                            open: true
				                        });
			                    	}
			                    	
			                    	//Ahora sobre ese nodo de carpeta creo el tramite
			                        var entNode = currNode.insert({
			                            data: {
			                                idNode: res.idTramite,
			                                tipo: 'tramite'
			                            },
			                            text: $('txtNombre').value,
			                            open: true
			                        });
			                        
			                        visorPLANESTree.select(entNode);
				                    currNode.update(true, true);
				                    _incrementarNumHijosNodoArbol(currNode);
				                    
			                    }*/
			                    
			                    $('VisorPLANESWindowAddTramite').retrieve('instance').close();
				            }
				            
				        }.bind(this),
				        onFailure: function(){
				        	alert('Error guardando los datos del trámite');
				        },
				        onCancel: function(){
				        	alert('Petición cancelada');
				        }
				    });

				    req.send();
				    
				});
			}			
		});
		}; //Fin var window
		
		visorPLANESCurrentWindow(); //Muestro la ventana
		
	} //Fin if
}

function BtnVisorPlanesEditTramites(){
	//Depende del tipo de elemento seleccionado se actua
	if ($('BtnVisorPlanesEditTramites').disabled == false && visorPLANESTree.selected){		
		visorPLANESTree.selected.toggle(false, true);
		
		visorPLANESCurrentWindow = function(){ 
			new MUI.Window({
			id: 'VisorPLANESWindowEditTramite',
			title: 'Editar Trámite',
			icon: 'styles/images/ModificarTramite.png',			
			contentURL: 'Pages/VisorPLANES/VisorPLANESWindows/VisorPLANESWindowEditTramite.html',
			width: 630,
			height: 355,
			maximizable: false,
			resizable: false,
			scrollbars: false,
			onContentLoaded: function(){								
				//Asigno el valor del idPlan asociado
				if (visorPLANESTree.selected && visorPLANESTree.selected.data.idNode){
					$('idTramite').value = visorPLANESTree.selected.data.idNode;
				} else {
					alert('No ha sido posible determinar el identificador del trámite');
					$('VisorPLANESWindowEditTramite').retrieve('instance').close();
					return;
				}
				
				$('txtNombre').value = $('nombre').get('html');
				$('txtTexto').value = $('panelDatosGeneralesTramitesTextotextArea').get('html');
				$('txtComentario').value = $('panelDatosGeneralesTramitesComentariotextArea').get('html');
				
				//ORGANO
				result = runServiceJson({wsName: "GET_DATA", TIPO: "Organo"});
				loadDataOnSelect('idOrgano',result,false,$('organo').get('html'),'No ha sido posible cargar la lista de &oacute;rganos');
				
				//SENTIDO
				result = runServiceJson({wsName: "GET_DATA", TIPO: "Sentido"});
				loadDataOnSelect('idSentido',result,false,$('sentido').get('html'),'No ha sido posible cargar la lista de sentidos');
				
				//FECHA
				//Setup calendar (Esto crea un button calendar)
		        new Calendar({ txtFecha: 'd/m/Y' }, { classes: ['dashboard'],
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
		        $('txtFechaVisible').value = $('fecha').get('html');		
		        
		        $$('input').each(function(input){ //Tengo que hacer el link despues de asignar el valor a la fecha
					if (input.get('data-meiomask') != null){
						input.meiomask(input.get('data-meiomask'));
					}					
				});
		        
		        $('BtnVisorPlanesCancelarTramite').addEvent('click', function(e){
		        	new Event(e).stop();
		        	$('VisorPLANESWindowEditTramite').retrieve('instance').close();
		        });
				$('BtnVisorPlanesGuardarTramite').addEvent('click', function(e){
					new Event(e).stop();
					
					var req = new Request.JSON({
				        url: 'GestionConsola',
				        data: {
				            wsName: 'UPDATE_TRAMITE',
				            idTramite: $('idTramite').value,
				            nombre: $('txtNombre').value,
				            organo: $('idOrgano').options[$('idOrgano').selectedIndex].value,
				            sentido: $('idSentido').options[$('idSentido').selectedIndex].value,
				            fecha: $('txtFechaVisible').value,
				            texto: $('txtTexto').value,
				            comentario: $('txtComentario').value
				        },
				        onSuccess: function(res){
				        	
				        	if (res == null){
				        		alert('Error modificando los datos de trámite');
				        	} else if (res.error){
				            	alert('Error modificando los datos de trámite:\n'+res.error);
				            } else {
				            	MUI.notification('Datos modificados correctamente');
				            	
				            	//Recargo el nodo padre para que muestre los datos correctos
			                    var currNode = visorPLANESTree.selected;
			                    if (currNode.parent){
			                    	visorPLANESTree.select(currNode.parent);
			                    	visorPLANESTree.selected.load(visorPLANESTree.selected.data.load);
			                    }
			                    
			                    $('VisorPLANESWindowEditTramite').retrieve('instance').close();
				            }
				            
				        }.bind(this),
				        onFailure: function(){
				        	alert('Error modificando los datos del trámite');
				        },
				        onCancel: function(){
				        	alert('Petición cancelada');
				        }
				    });
					
				    req.send();					   
				});
			}			
		});
		}; //Fin var window
		
		visorPLANESCurrentWindow(); //Muestro la ventana
	} //Fin if
}

function BtnVisorPlanesRemoveTramites(){
	if ($('BtnVisorPlanesRemoveTramites').disabled == false && visorPLANESTree.selected){
		
		if (!confirm('Se borrará el trámite seleccionado. ¿Desa continuar?')){
	        return;
	    }

	    var req = new Request.JSON({
	        url: 'GestionConsola',
	        data: {	            	            
	            wsName: 'BORRAR_TRAMITE',
	            idTramite: visorPLANESTree.selected.data.idNode,
	            idioma: idioma
	        },
	        onSuccess: function(resJson, resText){

	        	if (!resJson.exito){
	            	alert('Error borrando el trámite:\n'+resJson.mensaje);
	            } else {
	            	MUI.notification('Tr&aacute;mite borrado correctamente');
	            	
	            	//Recargo el nodo padre para que muestre los datos correctos
                    var currNode = visorPLANESTree.selected;
                    if (currNode.parent){
                    	if (!currNode.parent.idNode){
                    		currNode = currNode.parent;
                    	}
                    	if (currNode.parent){
                    		visorPLANESTree.select(currNode.parent);
                        	visorPLANESTree.selected.load(visorPLANESTree.selected.data.load);
                    	}                    	
                    }  	        
	            }       
	           
	        }.bind(this),
	        onFailure: function(){
	        	alert('Error borrando el trámite seleccionado');
	        },
	        onCancel: function(){
	        	alert('Petición cancelada');
	        }
	    });

	    req.send();
	}
}

function BtnVisorPlanesBuscar(){	
	
	if (visorPLANESCurrentSearchWindow == undefined){	
		visorPLANESCurrentSearchWindow = function(){ 
			new MUI.Modal({
			id: 'VisorPLANESWindowBusquedaPlanes',
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
		        	$('VisorPLANESWindowBusquedaPlanes').retrieve('instance').close();
		        });
		        $('BtnVisorPlanesBuscarPlanes').addEvent('click',function(e){
		        	new Event(e).stop();
		        	
		        	if (buquedaModificadaVisorPlanes == false)
		        	{
		        		var resultSearch = visorPLANESTree.findNext();
		        		if (resultSearch == -2){
		        			alert("No hay más resultados");
		        		} else {
		        			resultadosBusquedaCounter = resultadosBusquedaCounter + 1;
		        			$('BtnVisorPlanesResultadosBuscarPlanes').set('html',resultadosBusquedaCounter+' de '+resultadosBusquedaCount+' resultados');
		        		}	        		
		        	} else {
		        		var idRaizParam = '';
				        var tipoRaizParam = '';
				        if (visorPLANESTree.selected){
				        	var node = visorPLANESTree.selected;
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
				            tipo: visorPLANESviewerTreeType,
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
				        	enableDisableTree($('treePLANES'), true);
				        					        	
				        	if (result.resultado && result.resultado.length > 0){
				        		//Accion cuando la llamada ha devuelto un dato correcto
			        			buquedaModificadaVisorPlanes = false;
			        			visorPLANESTree.find(result.resultado);
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
				        	enableDisableTree($('treePLANES'), true);
				        	alert('Error al realizar la búsqueda');
				        },
				    	onTimeout: function(){
				    		enableDisableTree($('treePLANES'), true);
				    		alert('No se ha obtenido respuesta del servidor');
				    	}
				        });				        
				        enableDisableTree($('treePLANES'), false);				        
				        req.send();
		        	} //Fin de si es una busqueda nueva o no
		        });
			},
			onCloseComplete: function(){
				visorPLANESCurrentSearchWindow = undefined;
			}			
		});
		}; //Fin var window
		
		visorPLANESCurrentSearchWindow(); //Muestro la ventana
		
	} //Fin if
}