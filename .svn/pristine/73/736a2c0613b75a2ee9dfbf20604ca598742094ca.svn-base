//initializeConsoleFunctions
function debugConsole(text)
{
    if (true)
    {
        MochaUI.notification(text);	    	
    }
}

ChargeConsoleTypeInternal = function(consoleType)
{	
    $(idPanelCentral).empty();
	
    //Hay dos tipos de entorno, dividido y completo. Primero creo los entornos
    if (consoleType == 'opcionConsolaValFIP' || consoleType == 'opcionConsolaDiarioOp' || consoleType == 'opcionConsolaConsolidar' || consoleType == 'opcionConsolaRefundido')
    {				
        //Un solo panel, para el caso del diario de operaciones tiene un panel de busqueda que por ahora se queda como esta
        new MUI.Column({
            id: 'mainColumn',
            placement: 'main',
            sortable: false
        //isCollapsed: false
        });		
    }
    else
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
    	
        //Columns definition
        new MUI.Column({
            id: 'LeftSideColumn',
            placement: 'left',
            width: 300,
            resizeLimit: [200, parseInt(anchoPrincipal) - 40],
            sortable: false
        //isCollapsed: false
        });
		
        //Dos paneles, uno a la izquierda y otro central
        new MUI.Column({
            id: 'mainColumn',
            placement: 'main',
            //resizeLimit: [200, parseInt(anchoPrincipal) - 40],
            sortable: false
        //isCollapsed: false
        });	    
    }
	
    MUI.myChain.callChain();
	
    if (consoleType != 'opcionConsolaValFIP'){
        modificarRefrescoProgresoValidacion(10000); //realentiza el bucle del progreso de validacion
    }
	
    switch (consoleType) {		
        case 'opcionConsolaVisRPM': //Visualizador de planes consolidados
            $('LeftSideColumn').setOpacity(0);
            $('mainColumn').setOpacity(0);
            
            //SOLO SI TIENE EL ROL DE GESTOR DE PLANES MOSTRAMOS LA BOTONERA
            var urlBotonera = 'Pages/VisorPLANES/TreeViewerToolBoxPLANES.html';
            if (esGestorDePlanes()){
            	urlBotonera = 'Pages/VisorPLANES/TreeViewerToolBoxPLANESGestorPlanes.html';
            }
            
            new MUI.Panel({
                id: 'left-panel',
                title: '',
                contentURL: 'Pages/VisorPLANES/TreeViewerPLANES.html',
                column: 'LeftSideColumn',
                //scrollbars : false,
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: urlBotonera,
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderVisorPLANES.js
        		}
            });
	    	
            //Panel de datos del visualizador de planes consolidados
            new MUI.Panel({
                id: 'main-panel',
                contentURL: 'Pages/VisorPLANES/DataViewerPLANES.html',
                column: 'mainColumn',
                collapsible: false,
                header: false,
                require: {		        	
                    js: ['javascriptsV2/ConsoleOptions/PLANES/loaderVisorPLANES.js'],
                    onload: function(){						
                        if (initVisualizadorPLANES) initVisualizadorPLANES(viewerTreeType.rpm);
                    }
                }
            });		    		    
            break;
        case 'opcionConsolaVisValidacion': //Visualizador de planes no consolidados  
            $('LeftSideColumn').setOpacity(0);
            $('mainColumn').setOpacity(0);
            
          //SOLO SI TIENE EL ROL DE GESTOR DE PLANES MOSTRAMOS LA BOTONERA
            var urlBotonera = 'Pages/VisorPLANES/TreeViewerToolBoxPLANES.html';
            if (esGestorDePlanes()){
            	urlBotonera = 'Pages/VisorPLANES/TreeViewerToolBoxPLANESGestorPlanes.html';
            }
	    	
            //Arbol del visualizador de validacion
            new MUI.Panel({
                id: 'left-panel',
                title: '',
                contentURL: 'Pages/VisorPLANES/TreeViewerPLANES.html',
                column: 'LeftSideColumn',
                //scrollbars : false,
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: urlBotonera,
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderVisorPLANES.js
        		}
            });
	    	
            //Panel de datos del visualizador de validacion
            new MUI.Panel({
                id: 'main-panel',
                contentURL: 'Pages/VisorPLANES/DataViewerPLANES.html',
                column: 'mainColumn',
                collapsible: false,
                header: false,
                require: {		        	
                    js: ['javascriptsV2/ConsoleOptions/PLANES/loaderVisorPLANES.js'],
                    onload: function(){						
                        if (initVisualizadorPLANES) initVisualizadorPLANES(viewerTreeType.validacion);
                    }
                }
            });
            break;
        case 'opcionConsolaGestDiccionario': //GESTION DEL DICCIONARIO DE DATOS
            $('LeftSideColumn').setOpacity(0);
            $('mainColumn').setOpacity(0);
	    	
            //Lista de diccionarios del sistama		    
            new MUI.Panel({
                id: 'left-panel',
                contentURL: 'Pages/GestionDiccionario/DictionaryControl.html',
                column: 'LeftSideColumn',
                //scrollbars : false,
                collapsible: false,
                header: false
            });
		    
            //Datos de los diccionarios del sistema
            new MUI.Panel({
                id: 'main-panel',
                title: '',
                contentURL: 'Pages/GestionDiccionario/DictionaryData.html',
                column: 'mainColumn',
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/GestionDiccionario/DictionaryToolBoxData.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderGestionDiccionario.js
        		},
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderGestionDiccionario.js'],
                    onload: function(){		
                    	
                    	if($('left-panel_pad')){
                    		$('left-panel_pad').setStyle('padding','0px');
                    	}
                    	
                        if (initUserAdministration) initGestionDiccionario();
                    }
                }
            });
            break;
        case 'opcionConsolaValFIP': //Validador de FIPs
            $('mainColumn').setOpacity(0);
            
            new MUI.Panel({
                id: 'main-panel',
                title: '',
                contentURL: 'Pages/ValidadorFIP/ValidacionData.html',
                tabsURL: 'Pages/ValidadorFIP/ValidacionTabs.html',
                column: 'mainColumn',
                scrollbars : false,
                collapsible: false,
                header: true,
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderValidacion.js'],
                    onload: function(){
                        if (initValidacion) initValidacion();
                    }	
                }
            });
	    	/*
            new MUI.Panel({
                id: 'main-panel',
                title: 'Validaciones pendientes',
                contentURL: 'Pages/ValidadorFIP/validacionesFIP.jsp',
                column: 'mainColumn',
                scrollbars : true,
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/ValidadorFIP/ValidadorFIPToolBoxData.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderGestionDiccionario.js
        		},
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderValidacion.js'],
                    onload: function(){
                        if (initValidacion) initValidacion();
                    }	
                }
            });*/
            break;
        case 'opcionConsolaRefundido':
            //$('LeftSideColumn').setOpacity(0);
            $('mainColumn').setOpacity(0);
            
            new MUI.Panel({
                id: 'main-panel',
                title: '',
                contentURL: 'Pages/Refundido/RefundidoData.html',
                tabsURL: 'Pages/Refundido/RefundidoTabs.html',
                column: 'mainColumn',
                scrollbars : false,
                collapsible: false,
                header: true,
                require: {
                	js: ['javascriptsV2/ConsoleOptions/loaderRefundido.js'],
                    onload: function(){
                    	if (initRefundido) initRefundido();
                    }	
                }
            });
	    	
            /*
            //Arbol de refundido 
            new MUI.Panel({
                id: 'left-panel',
                title: '',
                contentURL: 'Pages/Refundido/TreeRefundido.html',
                column: 'LeftSideColumn',
                //scrollbars : false,
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/Refundido/TreeRefundidoToolBox.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderRefundido.js
        		},
            });
	    	
            //Panel de datos de refundido
            new MUI.Panel({
                id: 'main-panel',
                contentURL: 'Pages/Refundido/DataRefundido.html',
                column: 'mainColumn',
                collapsible: false,
                header: false,
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderRefundido.js'],
                    onload: function(){
                        if (initRefundido) initRefundido();
                    }	
                }
            });	*/	    		   
            break;
        case 'opcionConsolaAdmin': //Administracion de usuarios
            $('LeftSideColumn').setOpacity(0);
            $('mainColumn').setOpacity(0);
	    	
            //Lista de usuarios del sistema
            new MUI.Panel({
                id: 'left-panel',
                title: '',
                contentURL: 'Pages/Administracion/UserControl.html',
                column: 'LeftSideColumn',
                //scrollbars : false,
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/Administracion/UserToolBoxControl.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderAdministracion.js
        		}
            });
	    		    	
            //Datos y roles del usuario seleccionado
            new MUI.Panel({
                id: 'main-panel',
                title: '',
                contentURL: 'Pages/Administracion/UserData.html',
                column: 'mainColumn',
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/Administracion/UserToolBoxData.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderAdministracion.js
        		},
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderAdministracion.js'],
                    onload: function(){						
                        if (initUserAdministration) initUserAdministration();
                    }
                }
            });		    		    
            break;
        case 'opcionConsolaConsolidar': //Consolidador
            $('mainColumn').setOpacity(0);
	    	
            //Panel de datos del consolidador
            new MUI.Panel({
                id: 'main-panel',
                contentURL: 'Pages/Consolidador/consolidador.jsp',
                column: 'mainColumn',
                collapsible: false,
                header: false,
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderConsolidador.js'],
                    onload: function(){						
                        if (initConsolidador) initConsolidador();
                    }
                }
            });		   
            break;
        case 'opcionConsolaPlanBase': //Plan base
            $('LeftSideColumn').setOpacity(0);
            $('mainColumn').setOpacity(0);
	    	
            new MUI.Panel({
                id: 'left-panel',
                contentURL: 'Pages/PlanBase/TreeViewerPlanBase.html',
                column: 'LeftSideColumn',
                //scrollbars : false,
                collapsible: false,
                header: false
            });
 
            new MUI.Panel({
                id: 'main-panel',
                title: '',
                contentURL: 'Pages/PlanBase/DataViewerPlanBase.html',
                column: 'mainColumn',
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/PlanBase/DataViewerToolBoxPlanBase.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderPlanBase.js
        		},
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderPlanBase.js'],
                    onload: function(){
                        if (initPlanBase) initPlanBase();
                    }	
                }
            });
            break;
        case 'opcionConsolaDiarioOp': //Diario de operaciones
            $('mainColumn').setOpacity(0);
            new MUI.Panel({
                id: 'main-panel',
            	title: '',
                contentURL: 'Pages/DiarioOperaciones/diarioDeOperacion.html',
                column: 'mainColumn',
                collapsible: false,
                header: false,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/DiarioOperaciones/DiarioDeOperacionToolBoxData.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderVisorGIS.js
        		},
                require: {	
                    js: ['javascriptsV2/ConsoleOptions/loaderDiarioDeOperacion.js'],
                    onload: function(){
                        if (initDiarioDeOperacion) initDiarioDeOperacion();
                    }	
                }                        
            });
            break;
        case 'opcionConsolaConfigVisor': //Configurador del visor de GIS
        	$('LeftSideColumn').setOpacity(0);
            $('mainColumn').setOpacity(0);
	    	
            //Arbol de refundido
            new MUI.Panel({
                id: 'left-panel',
                title: '',
                contentURL: 'Pages/VisorGIS/TreeConfiguradorVisor.html',
                column: 'LeftSideColumn',
                //scrollbars : false,
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/VisorGIS/TreeToolBoxConfiguradorVisor.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderVisorGIS.js
        		}                
            });
	    	
            //Panel de datos de refundido
            new MUI.Panel({
                id: 'main-panel',
                title: '',
                contentURL: 'Pages/VisorGIS/DataConfiguradorVisor.html',
                column: 'mainColumn',
                collapsible: false,
                header: true,
                footer:true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/VisorGIS/DataToolBoxConfiguradorVisor.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderVisorGIS.js
        		},
                require: {
                    js: ['javascriptsV2/ConsoleOptions/VisorGIS/loaderVisorGIS.js'],
                    onload: function(){
                        if (initVisorGIS) initVisorGIS();
                    }	
                }
            });	
            break;
        case 'opcionConsolaPreviewVisor': //Previsualizador del visor
            $('LeftSideColumn').setOpacity(0);
            $('mainColumn').setOpacity(0);
            //Lista de tramites		    
            new MUI.Panel({
                id: 'left-panel',
                title: '',
                contentURL: 'Pages/PreviewVisor/Tramites.html',
                column: 'LeftSideColumn',
                header: false,
                //scrollbars : false,
                collapsible: false
            });
		    
            //Visor
            new MUI.Panel({
                id: 'main-panel',
                title: '',
                contentURL: 'Pages/PreviewVisor/Visor.html',
                column: 'mainColumn',
                collapsible: false,
                header: true,
                padding:'0px',
                headerToolbox: true,
                headerToolboxURL: 'Pages/PreviewVisor/DataToolBoxPreviewVisor.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderPreviewVisor.js
        		},
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderPreviewVisor.js'],
                    onload: function(){						
                    	if (initPreviewVisor) initPreviewVisor();
                    }
                }
            });
            break;
        case 'opcionConsolaConfigFichas': //Gestor de fichas
            $('LeftSideColumn').setOpacity(0);
            $('mainColumn').setOpacity(0);
	    	
            //Lista de fichas		    
            new MUI.Panel({
                id: 'left-panel',
                title: '',
                contentURL: 'Pages/GestionFichas/FichasControl.html',
                column: 'LeftSideColumn',
                scrollbars : true,
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/GestionFichas/FichasControlToolBox.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderGestionFichas.js
        		}
            });
		    
            //Datos de las fichas
            new MUI.Panel({
                id: 'main-panel',
                title: '',
                contentURL: 'Pages/GestionFichas/FichasData.html',
                column: 'mainColumn',
                collapsible: false,
                scrollbars : true,
                header: false,
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderGestionFichas.js'],
                    onload: function(){						
                    	if (initGestionFichas) initGestionFichas();
                    }
                }
            });
            break;
        default:
            break;
    }
};

function ChargePageOnPnlCentral(page)
{	
    new Request({
        url: page,
        async:false,
        onSuccess: function(response){
            $(idPanelCentral).fade(0);
            $(idPanelCentral).set('html',response);
            $(idPanelCentral).fade(1);      
        },
        onFailure: function(response){
            $(idPanelCentral).set('html','<center>'+cargarTextoSegunIdioma('SE HA PRODUCIDO UN PROBLEMA AL CARGAR LA PÁGINA')+'</center>');
        }
    }).send();
}

function ChargeConsoleType(consoleType)
{
    MUI.myChain = new Chain();
    MUI.myChain.chain(
        function () {
            MUI.Desktop.initialize();
        },
        //function () { MUI.Dock.initialize(); },
        //function () { initializeColumns(); },
        function () {
            ChargeConsoleTypeInternal(consoleType);
        }
        ).callChain();
}


