//Datos generales del tramite
var dataValoresReferencia;
var dataRegulacionesEspecificas;
var dataDocumentos;
var dataEsOperadoPor;
var dataOperaA;
var dataAplicaciones;
var dataGruposAplicacion;
var dataOperaDeterminacionesReguladoras;

var dataRegimenDirecto;
var dataUsos;
var dataActos;
var dataPropiedadesAdscripcionOrigen;
var dataPropiedadesAdscripcionDestino;


// ****************** DETERMINACIONES **********************
//Arbol de determinaciones
function addTreeDeterminaciones(data, id, sitio, sitioDataDeterminaciones, idContainerTab)
{
    return addTree(data, 'GET_NODOS_DETERM_DE_TRAMITE', 'idTramite', id, sitio, 'folders', 'Determinaciones', false, 'determinaciones', ['javascriptsV2/mooTree2/Determinaciones.gif'], '97%', '97%', idContainerTab,
		function(node, state) {    	    		    	    	
		    //Anado el tab de determinaciones
		    if ($(sitioDataDeterminaciones)) {

		        $(sitioDataDeterminaciones).empty(); //vacio lo que antes tuviera el elemento

		        new MUI.Panel({
		            id: 'tabTramitesDeterminaciones-panel',
		            contentURL: 'Pages/VisorPLANES/Tramites/Determinaciones/DeterminacionesData.html',
		            column: sitioDataDeterminaciones,
		            tabsURL: 'Pages/VisorPLANES/Tramites/Determinaciones/DeterminacionesTabs.html',
		            scrollbars: false,
		            collapsible: false,
		            header: true,
		            require: {
		                onload: function() {		                	

		                    if (node.data.idNode && $('panelTramitesDeterminacionesDatosGenerales')) {
		                    	
		                    	$('mainColumnDeterminaciones').setOpacity(1);
		                    	
		                    	if($('tabTramitesDeterminacionesCargando')){
			                		$('tabTramitesDeterminacionesCargando').setStyle('background-image','url("")');
			                	}
			                	if ($('tabTramitesDeterminaciones-panel_pad')){
			                		$('tabTramitesDeterminaciones-panel_pad').setStyle('padding', '2px');
			                	}

		                        //Setup tabs
		                        $('panelTramitesDeterminacionesValoresReferencia').set('left', '0px');
		                        $('panelTramitesDeterminacionesValoresReferencia').set('top', '0px');
		                        $('panelTramitesDeterminacionesValoresReferencia').setStyle('display', 'none');
		                        $('panelTramitesDeterminacionesRegulacionesEspecificas').set('left', '0px');
		                        $('panelTramitesDeterminacionesRegulacionesEspecificas').set('top', '0px');
		                        $('panelTramitesDeterminacionesRegulacionesEspecificas').setStyle('display', 'none');
		                        $('panelTramitesDeterminacionesDocumentos').set('left', '0px');
		                        $('panelTramitesDeterminacionesDocumentos').set('top', '0px');
		                        $('panelTramitesDeterminacionesDocumentos').setStyle('display', 'none');
		                        $('panelTramitesDeterminacionesEsOperadoPor').set('left', '0px');
		                        $('panelTramitesDeterminacionesEsOperadoPor').set('top', '0px');
		                        $('panelTramitesDeterminacionesEsOperadoPor').setStyle('display', 'none');
		                        $('panelTramitesDeterminacionesOperaA').set('left', '0px');
		                        $('panelTramitesDeterminacionesOperaA').set('top', '0px');
		                        $('panelTramitesDeterminacionesOperaA').setStyle('display', 'none');
		                        $('panelTramitesDeterminacionesAplicaciones').set('left', '0px');
		                        $('panelTramitesDeterminacionesAplicaciones').set('top', '0px');
		                        $('panelTramitesDeterminacionesAplicaciones').setStyle('display', 'none');
		                        $('panelTramitesDeterminacionesGruposAplicacion').set('left', '0px');
		                        $('panelTramitesDeterminacionesGruposAplicacion').set('top', '0px');
		                        $('panelTramitesDeterminacionesGruposAplicacion').setStyle('display', 'none');
		                        $('panelTramitesDeterminacionesDeterminacionesReguladoras').set('left', '0px');
		                        $('panelTramitesDeterminacionesDeterminacionesReguladoras').set('top', '0px');
		                        $('panelTramitesDeterminacionesDeterminacionesReguladoras').setStyle('display', 'none');
		                        
		                        countTramitesDeterminacionesTabLoad = 0;
		                        getProgressLoadTabStatus(9,'tabTramitesDeterminacionesCargando','determinacion', true, 'treeDeterminaciones' );

		                        //UN SOLO PANEL FORMULARIO
		                        getFormData(node.data.idNode, 'determinacion', true, undefined, 
    	                		function(data){		             
		                        	addTramitesDeterminacionesTabLoad();
		                        	ChargeTabDeterminacionesDatosGenerales(data);
		                        });
		                        		
                        		//UN SOLO PANEL GRID
	                        	getTableData(node.data.idNode, 'valoresReferencia', true, 'tabTramitesDeterminacionesValoresReferencia', 
        	                		function(data){
	                        		addTramitesDeterminacionesTabLoad();
	                        		dataValoresReferencia = data;	                        		
                        		});

	                            //PANELES HORIZONTALES ARBOL - TEXTO
	                        	getTreeData('GestionConsola?wsName=GET_NODOS_REGESP_DET_RPM&idDet=' + node.data.idNode + '&idioma=' + idioma, undefined, true, 'tabTramitesDeterminacionesRegulacionesEspecificas', 
        	                		function(data){
	                        		addTramitesDeterminacionesTabLoad();
	                        		dataRegulacionesEspecificas = data;	                        		
                        		});

	                        	//PANELES HORIZONTALES GRID - TEXTO - DBL CLICK SOBRE GRID ABRE DOCUMENTO
	                        	getTableData(node.data.idNode, 'documentosDeterminacion', true, 'tabTramitesDeterminacionesDocumentos', 
        	                		function(data){
	                        		addTramitesDeterminacionesTabLoad();
	                        		dataDocumentos = data;	                        		
                        		});

	                            //PANELES HORIZONTALES GRID - TEXTO
	                        	getTableData(node.data.idNode, 'DeterminacionEsOperadoPor', true, 'tabTramitesDeterminacionesEsOperadoPor', 
        	                		function(data){
	                        		addTramitesDeterminacionesTabLoad();
	                        		dataEsOperadoPor = data;	                        		
                        		});

	                            //PANELES HORIZONTALES GRID - TEXTO
	                        	getTableData(node.data.idNode, 'DeterminacionOperaA', true, 'tabTramitesDeterminacionesOperaA', 
        	                		function(data){
	                        		addTramitesDeterminacionesTabLoad();
	                        		dataOperaA = data;	                        		
                        		});

	                            //UN SOLO PANEL ARBOL
	                        	getTreeData('GestionConsola?wsName=GET_NODOS_PLANES_APLICACION&idDet=' + node.data.idNode + '&idioma=' + idioma, undefined, true, 'tabTramitesDeterminacionesAplicaciones', 
        	                		function(data){
	                        		addTramitesDeterminacionesTabLoad();
	                        		dataAplicaciones = data;	                        		
                        		});		                        	
	                            //addTable(undefined, node.data.idNode, 'AplicacionesDeDet', viewerPlanesTypeAplicacionesDeDet, 'gridTramitesDeterminacionesAplicaciones', true, '100%', (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px', 'tabTramitesDeterminacionesAplicaciones', undefined);

	                            //UN SOLO PANEL GRID
	                        	getTableData(node.data.idNode, 'GruposAplicacion', true, 'tabTramitesDeterminacionesGruposAplicacion', 
        	                		function(data){
	                        		addTramitesDeterminacionesTabLoad();
	                        		dataGruposAplicacion = data;	                        		
                        		});                       

	                            //PANELES HORIZONTALES GRID - TEXTO
	                        	getTableData(node.data.idNode, 'DetReguladoras', true, 'tabTramitesDeterminacionesDeterminacionesReguladoras', 
        	                		function(data){
	                        		addTramitesDeterminacionesTabLoad();
	                        		dataOperaDeterminacionesReguladoras = data;	                        		
                        		});

	                            //OCULTO
	                            //addTable(node.data.idNode,'Unidades', 'gridTramitesDeterminacionesUnidades',true, '100%', 200, null, null, 'tabTramitesDeterminacionesUnidades');
		                    } else {
		                    	$('mainColumnDeterminaciones').setOpacity(0);
		                    }
		                } //fin onLoad
		            } //fin require
		            });
		        } //fin if si existe sitioData
		    });    //fin de la llamda al addTree
}

// ***************** ENTIDADES ******************************
//Arbol de entidades
function addTreeEntidades(data, id,sitio, sitioDataEntidades, idContainerTab)
{
	return addTree(data, 'GET_NODOS_ENTIDAD_DE_TRAMITE', 'idTramite', id, sitio, 'folders', 'Entidades', false, 'entidades', ['javascriptsV2/mooTree2/Entidades.gif'], '97%', '97%', idContainerTab, 
		function(node, state){
			//Anado el tab de entidades
	    	if ($(sitioDataEntidades)){
	    		
	    		$(sitioDataEntidades).empty(); //vacio lo que antes tuviera el elemento
	    		
	    		//$(sitioData).setStyle('width',(parseInt($('mainColumn').getStyle('width'))  - parseInt($('leftSideColumnEntidades').getStyle('width')) - 11) + 'px');
	    		
        		new MUI.Panel({
        			id: 'tabTramitesEntidades-panel',
        			contentURL: 'Pages/VisorPLANES/Tramites/Entidades/EntidadesData.html',
        			column: sitioDataEntidades,
        			tabsURL: 'Pages/VisorPLANES/Tramites/Entidades/EntidadesTabs.html',
        			scrollbars : false,
        	        collapsible: false,
        	        header: true,
	    			require: {
	    				onload: function(){	    						    					
        					
        					if(node.data.idNode && $('panelTramitesEntidadesDatosGenerales')){
        						
        						$('mainColumnEntidades').setOpacity(1);
        						
        						if($('tabTramitesEntidadesCargando')){
    	    						$('tabTramitesEntidadesCargando').setStyle('background-image','url("")');
    	    					}
    	    					if ($('tabTramitesEntidades-panel_pad')){
    	    						$('tabTramitesEntidades-panel_pad').setStyle('padding','2px');
    	    					}
        						       						
        						//Setup tabs
        						$('panelTramitesEntidadesRegimenDirecto').set('left','0px');
        						$('panelTramitesEntidadesRegimenDirecto').set('top','0px');	
        						$('panelTramitesEntidadesRegimenDirecto').setStyle('display', 'none');		
        						$('panelTramitesEntidadesUsos').set('left','0px');
        						$('panelTramitesEntidadesUsos').set('top','0px');	
        						$('panelTramitesEntidadesUsos').setStyle('display', 'none');					
        						$('panelTramitesEntidadesActos').set('left','0px');
        						$('panelTramitesEntidadesActos').set('top','0px');
        						$('panelTramitesEntidadesActos').setStyle('display', 'none');						
        						$('panelTramitesEntidadesEsOperadorPor').set('left','0px');
        						$('panelTramitesEntidadesEsOperadorPor').set('top','0px');
        						$('panelTramitesEntidadesEsOperadorPor').setStyle('display', 'none');						
        						$('panelTramitesEntidadesOperaA').set('left','0px');
        						$('panelTramitesEntidadesOperaA').set('top','0px');
        						$('panelTramitesEntidadesOperaA').setStyle('display', 'none');						
        						$('panelTramitesEntidadesAdscripcionOrigen').set('left','0px');
        						$('panelTramitesEntidadesAdscripcionOrigen').set('top','0px');
        						$('panelTramitesEntidadesAdscripcionOrigen').setStyle('display', 'none');
        						$('panelTramitesEntidadesAdscripcionDestino').set('left','0px');
        						$('panelTramitesEntidadesAdscripcionDestino').set('top','0px');
        						$('panelTramitesEntidadesAdscripcionDestino').setStyle('display', 'none');        						
        						$('panelTramitesEntidadesDocumentos').set('left','0px');
        						$('panelTramitesEntidadesDocumentos').set('top','0px');
        						$('panelTramitesEntidadesDocumentos').setStyle('display', 'none');						
        						$('panelTramitesEntidadesGeometria').set('left','0px');
        						$('panelTramitesEntidadesGeometria').set('top','0px');
        						$('panelTramitesEntidadesGeometria').setStyle('display', 'none');
        						
        						countTramitesEntidadesTabLoad = 0;
        						if (node.data.codigoGrupoEntidad && node.data.codigoGrupoEntidad != "7000000001"){        							
            						getProgressLoadTabStatus(10,'tabTramitesEntidadesCargando','entidad', true, 'treeEntidades' );
        						}  
        						
        	                    //UN SOLO PANEL FORM
		                        getFormData(node.data.idNode, 'entidad', true, undefined, 
        	                		function(data){
		                        	addTramitesEntidadesTabLoad();
		                        	addForm(data, undefined, undefined, viewerPlanesTypeEntidad, 'panelTramitesEntidadesDatosGenerales', '100px', undefined);
		                        });
			                        		
                        		/*
		                         * Las entidades que tienen asignado el valor "Carpeta" con c�digo "7000000001" a la determinaci�n de Grupo de Entidad, s�lo constan de la tab de "Datos Generales" 
		                         * */
		                        if (node.data.codigoGrupoEntidad && node.data.codigoGrupoEntidad != "7000000001"){
		                        	//PANELES HORIZONTALES ARBOL y DOS TABs SIN CABECERA SEGUN SE SELECCIONE VALORES (TEXTO) o DOCUMENTOS (FORMULARIO). DOBLE CLICK ARBOL DOCUMENTO ABRE DOCUMENTO
	    							getTreeData('GestionConsola?wsName=GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO&idEnt='+node.data.idNode+'&tipo=regimendirecto&idioma='+idioma, undefined, true, 'tabTramitesEntidadesRegimenDirecto', 
	        	                		function(data){
	    								addTramitesEntidadesTabLoad();
	    								dataRegimenDirecto = data;    								
									});
	    							
	    							//PANELES HORIZONTALES ARBOL - TEXTO
	    							getTreeData('GestionConsola?wsName=GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO&idEnt='+node.data.idNode+'&tipo=uso&idioma='+idioma, undefined, true, 'tabTramitesEntidadesUsos',
										function(data){
	    								addTramitesEntidadesTabLoad();
	    								dataUsos = data;    								
									});
	    							
	    							//PANELES HORIZONTALES ARBOL - TEXTO
	    							getTreeData('GestionConsola?wsName=GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO&idEnt='+node.data.idNode+'&tipo=acto&idioma='+idioma, undefined, true, 'tabTramitesEntidadesActos',
										function(data){
	    								addTramitesEntidadesTabLoad();
	    								dataActos = data;    								
									});
	    							
	    							//PANELES HORIZONTALES GRID - TEXTO
	    							getTableData(node.data.idNode, 'EntidadEsOperadoPor', true, 'tabTramitesEntidadesEsOperadorPor', 
	        	                		function(data){
	    								addTramitesEntidadesTabLoad();
	    								dataEsOperadoPor = data;    								
									});
		    	                    
	    							//PANELES HORIZONTALES GRID - TEXTO
		                        	getTableData(node.data.idNode, 'EntidadOperaA', true, 'tabTramitesEntidadesOperaA', 
	        	                		function(data){
		                        		addTramitesEntidadesTabLoad();
		                        		dataOperaA = data;	                        		
	                        		});    	                    	          	                        	                    
	        	                    
		                        	//PANELES HORISONTALES GRID - TEXTO
		                        	getTableData(node.data.idNode, 'PropiedadesAdscripcionOrigen', true, 'tabTramitesEntidadesAdscripcionOrigen', 
	        	                		function(data){
		                        		addTramitesEntidadesTabLoad();
		                        		dataPropiedadesAdscripcionOrigen = data;	                        		
	                        		});
		                        	
		                        	getTableData(node.data.idNode, 'PropiedadesAdscripcionDestino', true, 'tabTramitesEntidadesAdscripcionDestino', 
		        	                		function(data){
			                        		addTramitesEntidadesTabLoad();
			                        		dataPropiedadesAdscripcionDestino = data;	                        		
		                        		});  
		                        	
		                        	//PANELES HORIZONTALES GRID - TEXTO - DBL CLICK SOBRE GRID ABRE DOCUMENTO
		                        	getTableData(node.data.idNode, 'documentosEntidad', true, 'tabTramitesEntidadesDocumentos', 
	        	                		function(data){
		                        		addTramitesEntidadesTabLoad();
		                        		dataDocumentos = data;	                        		
	                        		});                   
	        	                    
		                        	preInitializeMapForEntity(node.data.idNode, $('mapTramitesEntidadesGeometria'), 'RPM', true, 'tabTramitesEntidadesGeometria',
	                        			function(data){
		                        		if (globalMapForEntity){
                                                            var wmc = new OpenLayers.Format.WMC();
                                                            contextoMapaPreviewEntidades=wmc.toContext(globalMapForEntity.options.mapa);
                                                        }
                                                        addTramitesEntidadesTabLoad();
		                        		idAmbitoDeEntidadMapForEntity = data;	                        		
	                        		});
		                        } 						        	                    
        					} else {
        						$('mainColumnEntidades').setOpacity(0);
        					}      					        					
        				} //fin onLoad
	    			} //fin require
	    		});        	        		
	    	} //fin if si existe sitioData
		}); //fin de la llamda al addTree
}



