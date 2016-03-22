//VARIABLES
var activeView = "LOGING"; //LOGING - CONSOLE Indica la vista que se esta mostrando
var textoSeleccionado = textoTituloConsola;
var opcionSeleccionada;

function UpDateConsoleStatus()
{	
    var name = getUserName();
	
    if (name)
    {
        //USUARIO CONECTADO
        $("labelUserState").set('html',name);
        $("opcionLoginUSERLink").set('html',cargarTextoSegunIdioma("Desconectar"));
		
        //Según el estado actual de la consola hago una cosa u otra
        if (activeView == "LOGING"){
            //Hay que cargar la consola
            $('cabLogin').fade(1);			
            ChargeConsole();
        }
    }
    else
    {
        //USUARIO DESCONECTADO
        $("labelUserState").set('html',cargarTextoSegunIdioma("Usuario desconectado"));
        $("opcionLoginUSERLink").set('html',cargarTextoSegunIdioma("Iniciar Sesión"));
		
        //Según el estado actual de la consola hago una cosa u otra
        if (activeView != "LOGING"){
            //Hay que mostrar la pantalla inicial
            $('cabLogin').fade(1);
            UnChargeConsole();
        }		
    }
}

function getUserName()
{	
    var result;
    result = runServiceJson({
        'wsName':'GET_USUARIO'
    });
    
    var name = "";
    if (result)
    {
        if (result.nombre)
        {
            name = result.nombre;
        }
        else
        {
            debugLoging(result.error);
        }
    }    
    
    return name;
}

function ChargeConsole()
{
    //Elimina lo que hubiera en el pnlCentral
    $(idPanelCentral).empty();
	
    ajustarPaneles_AbrirLogin();
	
    //Cambia la apariencia de la ventana para mostrar la consola 
    cambiarTitulo('CONSOLA');	
    
    
    $('cabTituloSeccion').set('html',cargarTextoSegunIdioma(textoTituloConsola));
	
    //Oculta la botonera central
    $('botonera').fade(0);
	
    //Cambio el tamano del logo
    changeLogoToConsolePosition();
		
    //Cargar botonera superior segun permisos de usuario y asigno eventos click
    cargarBotonera();
       
    $('botoneraSuperior').setStyle('position','relative');
    //$('botoneraSuperior').setStyle('left','50%');
    checkPositionBotones();
    var anchoBotonera=$('botoneraSuperior').getElement('div[class=opcionBotonera]').getWidth().toInt()*($('botoneraSuperior').getElements('div[class=opcionBotonera]').length);
    //$('botoneraSuperior').setStyle('margin-left',(anchoBotonera/2)*(-1));      
    
    //Oculto el titulo porque se queda debajo de la botonera superior
    $('cabTituloPpal').fade(0);
    
    activeView = 'CONSOLE';
}

function UnChargeConsole(){
    //Cambia la apariencia de la ventana para mostrar el loging	
    cambiarTitulo('CONTROL DE ACCESO');	
	
    $('cabTituloSeccion').set('html',cargarTextoSegunIdioma(textoTituloAcceso));
	
    //Muestro el estado inicial de los botones
    //ajustarPaneles_EstadoInicial();
    ajustarPaneles_CerrarLogin();	
	
    //Elimina lo que hubiera en el pnlCentral
    $(idPanelCentral).empty();
	
    //Muestro la botonera central
    $('botonera').fade(1);
	
    //Cambio el tamano del logo
    changeLogoToLoginPosition();
	
    initializeLogingEvents();

    activeView = 'LOGING';
}

function cargarBotonera()
{   
    //Obtengo los permisos del usuario y según los permisos cargo unos botones u otros
    var permisos;
    permisos = runServiceJson({
        'wsName':'ALLOWED_MODULE'
    });
    
    //Cargo los botones segun los permisos del usuario
    var arrayBotoneraConsola = [];
    if (permisos) {
        if (permisos.VAL){ // || permisos.ADM){ DESHABILITO EL VALIDADOR FIP EN ADMINISTRACION
            arrayBotoneraConsola.push('opcionConsolaValFIP');
        }
        if (permisos.VAL){
            arrayBotoneraConsola.push('opcionConsolaVisValidacion');
        }
        if (permisos.CSD){
            arrayBotoneraConsola.push('opcionConsolaConsolidar');
        }
        if (permisos.CON || permisos.PLA){
            arrayBotoneraConsola.push('opcionConsolaVisRPM');
        }
        if (permisos.REF){
            arrayBotoneraConsola.push('opcionConsolaRefundido');
        }
        if (permisos.ADM){
            var authDic = "S" === runService({
                wsName: "DIC_VISIBLE"
            });
        	
            if (authDic){
                arrayBotoneraConsola.push('opcionConsolaGestDiccionario');
            }

            var authPlanBase = "S" == runService({
                wsName: "PLANBASE_VISIBLE"
            });
                        
            if (authPlanBase){
                arrayBotoneraConsola.push('opcionConsolaPlanBase');
            }
            
            arrayBotoneraConsola.push('opcionConsolaDiarioOp');
            arrayBotoneraConsola.push('opcionConsolaAdmin');
        }

        if (permisos.CFV){
            arrayBotoneraConsola.push('opcionConsolaConfigVisor');
        }
        if (permisos.FIC){
            arrayBotoneraConsola.push('opcionConsolaConfigFichas');
        }
        if (permisos.CON){
            arrayBotoneraConsola.push('opcionConsolaPreviewVisor');
        }
    } else {
        MUI.notification(cargarTextoSegunIdioma('El usuario no está autorizado a acceder a la consola'));
        return;
    }
   
    //Inserto la botones resultado de aplicar permisos y sus eventos
    arrayBotoneraConsola.each(function(boton){
        var widthBoton = getwithNumberBotoneraOption(boton);
        	    	
        var divToAdd = new Element('div',{
            'id':boton,
            'class':'opcionBotonera',
            'events':{
                'click':function(){
                    //quitar el estilo selected a todos los botones y colocarselo solo al boton
                    //que se ha pulsado                 	
                	
                    $$('.opcionBotonera').each(function (b){
                        setBoteneraOptionImage(b, getwithNumberBotoneraOption(b.get('id')), 0);
                    });
                    
                    textoSeleccionado = textoTituloConsola;
                    
                    ajustarPaneles_AbrirLogin();
                    
                    setBoteneraOptionImage($(boton), widthBoton, 2);
                    
                    opcionSeleccionada = boton;

                    //Selecciono el texto a mostrar
                    switch (boton) {
                        case 'opcionConsolaVisRPM':
                            textoSeleccionado = cargarTextoSegunIdioma('VISUALIZACIÓN DE PLANES CONSOLIDADOS');
                            break;
                        case 'opcionConsolaVisValidacion':
                            textoSeleccionado = cargarTextoSegunIdioma('VISUALIZACIÓN DE PLANES NO CONSOLIDADOS');
                            break;
                        case 'opcionConsolaGestDiccionario':
                            textoSeleccionado = cargarTextoSegunIdioma('CONFIGURACIÓN DEL DICCIONARIO');
                            break;
                        case 'opcionConsolaValFIP':
                            textoSeleccionado = cargarTextoSegunIdioma('VALIDACIÓN DE PLANES');
                            break;
                        case 'opcionConsolaRefundido':
                            textoSeleccionado = cargarTextoSegunIdioma('REFUNDIDO DE PLANES');
                            break;
                        case 'opcionConsolaAdmin':
                            textoSeleccionado = cargarTextoSegunIdioma('ADMINISTRACIÓN DE USUARIOS');
                            break;
                        case 'opcionConsolaConsolidar':
                            textoSeleccionado = cargarTextoSegunIdioma('CONSOLIDACIÓN DE PLANES');
                            break;
                        case 'opcionConsolaPlanBase':
                            textoSeleccionado = cargarTextoSegunIdioma("EDICIÓN DEL PLAN BASE");
                            break;
                        case 'opcionConsolaDiarioOp':
                            textoSeleccionado = cargarTextoSegunIdioma("VISUALIZACIÓN DEL DIARIO DE OPERACIONES");
                            break;
                        case 'opcionConsolaConfigVisor':
                            textoSeleccionado = cargarTextoSegunIdioma("CONFIGURACIÓN DEL VISOR DE PLANEAMIENTO");
                            break;
                        case 'opcionConsolaConfigFichas':
                            textoSeleccionado = cargarTextoSegunIdioma("CONFIGURACIÓN DE FICHAS URBANÍSTICAS");
                            break;
                        case 'opcionConsolaPreviewVisor':
                            textoSeleccionado = cargarTextoSegunIdioma("PRESENTACIÓN EN EL VISOR");
                            break;
                        default:
                            break;
                    }                    
                    
                    //Cargo el tipo de pagina segun el boton seleccionado (Console.js)
                    ChargeConsoleType(boton);
                },
                'mouseover':function(){
                	
                    if (opcionSeleccionada != boton){
                        setBoteneraOptionImage(divToAdd, widthBoton, 1);
                    }                	
                	
                    switch (boton) {
                        case 'opcionConsolaVisRPM':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("VISUALIZACIÓN DE PLANES CONSOLIDADOS"));
                            break;
                        case 'opcionConsolaVisValidacion':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("VISUALIZACIÓN DE PLANES NO CONSOLIDADOS"));
                            break;
                        case 'opcionConsolaGestDiccionario':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("CONFIGURACIÓN DEL DICCIONARIO"));
                            break;
                        case 'opcionConsolaValFIP':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("VALIDACIÓN DE PLANES"));
                            break;
                        case 'opcionConsolaRefundido':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("REFUNDIDO DE PLANES"));
                            break;
                        case 'opcionConsolaAdmin':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("ADMINISTRACIÓN DE USUARIOS"));
                            break;
                        case 'opcionConsolaConsolidar':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("CONSOLIDACIÓN DE PLANES"));
                            break;
                        case 'opcionConsolaPlanBase':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("EDICIÓN DEL PLAN BASE"));
                            break;
                        case 'opcionConsolaDiarioOp':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("VISUALIZACIÓN DEL DIARIO DE OPERACIONES"));
                            break;
                        case 'opcionConsolaConfigVisor':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("CONFIGURACIÓN DEL VISOR DE PLANEAMIENTO"));
                            break;
                        case 'opcionConsolaConfigFichas':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("CONFIGURACIÓN DE FICHAS URBANÍSTICAS"));
                            break;
                        case 'opcionConsolaPreviewVisor':
                            $('cabTituloSeccion').set('html',cargarTextoSegunIdioma("PRESENTACIÓN EN EL VISOR"));
                            break;
                        default:
                            break;
                    }                    
                },
                'mouseout':function(){
                	
                    if (opcionSeleccionada != boton){
                        setBoteneraOptionImage(divToAdd, widthBoton, 0);
                    }                	
                	
                    $('cabTituloSeccion').set('html',cargarTextoSegunIdioma(textoSeleccionado));
                }
            }
        });                
        
        divToAdd.injectInside($('botoneraSuperior'));
        
        setBoteneraOptionImage(divToAdd, widthBoton, 0);
    });
}

function cambiarTitulo(texto)
{
    $('cabTituloPpal').setOpacity(0);
    $('cabTituloPpal').set('html',texto);
    $('cabTituloPpal').fade(1);
}

function esGestorDePlanes()
{  
    var dev = false;
	
    var permisos = runServiceJson({
        'wsName':'ALLOWED_MODULE'
    });
    
    if (permisos && permisos.PLA){
        dev = true;
    }
    
    return dev;
}

function getwithNumberBotoneraOption(botonId){
	
    var widthBoton = 0;
	
    switch (botonId) {
        case 'opcionConsolaVisRPM':
            widthBoton = 2;
            break;
        case 'opcionConsolaVisValidacion':
            widthBoton = 3;
            break;
        case 'opcionConsolaGestDiccionario':
            widthBoton = 4;
            break;
        case 'opcionConsolaValFIP':
            widthBoton = 5;
            break;
        case 'opcionConsolaRefundido':
            widthBoton = 6;
            break;
        case 'opcionConsolaAdmin':
            widthBoton = 7;
            break;
        case 'opcionConsolaConsolidar':
            widthBoton = 8;
            break;
        case 'opcionConsolaPlanBase':
            widthBoton = 8;
            break;
        case 'opcionConsolaDiarioOp':
            widthBoton = 9;
            break;
        case 'opcionConsolaConfigVisor':
            widthBoton = 10;
            break;
        case 'opcionConsolaConfigFichas':
            widthBoton = 9;
            break;
        case 'opcionConsolaPreviewVisor':
            widthBoton = 11;
            break;
        default:
            break;
    }
    return widthBoton;
}

function setBoteneraOptionImage(div, width, height) {
	
    if (div){
        if (width >= 0 && height >= 0) {
            div.setStyle('background-image','url(styles/images/botonera.gif)');
            div.setStyle('background-position','-'+(width*65) + 'px '+ (height*65) +'px');
        }		
    }			
}

