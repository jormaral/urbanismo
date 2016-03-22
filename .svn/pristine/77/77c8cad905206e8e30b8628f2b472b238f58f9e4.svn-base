var subiendo = undefined;
var iniciadoProgresoValidacion = false;
var iniciadoProgresoSubida = false;
var stopObtenerProgresoValidacion = false;
var timeOutProgresoValidacion = 10000; //inicialmente esta a 10sg
var fornValEstadoValidacion;
var highlighterValidacion = undefined;

function initValidacion(){
	
	$('panelValidacionesPendientes').setStyle('height',(parseInt($('main-panel').getStyle('height')) - 2)+'px');
	$('panelValidacionesPendientesData').setStyle('height',(parseInt($('main-panel').getStyle('height')) - 32)+'px');
	
	//Lo primero es actualizar el contendo del tab de validaciones pendientes que debe estar PRE-CARGARDO
	MUI.updateContent({
		element: $('main-panel'),
		childElement: $('panelValidacionesPendientesData'),
		url: 'Pages/ValidadorFIP/validacionesPendientesDataTableFIP.jsp',
		/*title: 'Validaciones pendientes',
        header: true,
        headerToolbox: true,
		headerToolboxURL: 'Pages/ValidadorFIP/ValidadorFIPToolBoxData.html',
		headerToolboxOnload: function(){
			//La asignacion de eventos a botones se hace en loaderGestionDiccionario.js
		},*/
		padding: { top: 2, right: 2, bottom: 2, left: 2 }									
	});
	
	obtenerProgresoValidacion();
	
	if (!iniciadoProgresoValidacion){
		iniciadoProgresoValidacion = true;			
	} else{
		if (iniciadoProgresoSubida){
			modificarRefrescoProgresoValidacion(2000);
		}
	}
	
	if ($('btnSubmitSearch')){
		//Agrego evento de boton de busqueda
		highlighterValidacion = new Highlighter({
		    elements: '#contenedorTablaValidacionesPendientes td',
		    className: 'negrita',
		    autoUnhighlight: true,
		    caseSensitive: false
		  });
		
		$('btnSubmitSearch').addEvent('click',function(){
			var searchText = $('txtImputSearch').value;
			if (searchText) {
				highlighterValidacion.highlight(searchText);
		    }				
		});
		$('txtImputSearch').addEvent('keyup',function(e){
	    	if(e.code==13){
	    		$('btnSubmitSearch').fireEvent('click');               
	        }
	    });
	}
	
	if ($('btnSubmitSearchRealizadas')){
		//Agrego evento de boton de busqueda
		highlighterValidacion = new Highlighter({
		    elements: '#contenedorTablaValidacionesRealizadas td',
		    className: 'negrita',
		    autoUnhighlight: true,
		    caseSensitive: false
		  });
		
		$('btnSubmitSearchRealizadas').addEvent('click',function(){
			var searchText = $('txtImputSearchRealizadas').value;
			if (searchText) {
				highlighterValidacion.highlight(searchText);
		    }				
		});
		$('txtImputSearchRealizadas').addEvent('keyup',function(e){
	    	if(e.code==13){
	    		$('btnSubmitSearchRealizadas').fireEvent('click');               
	        }
	    });
	}
	
	//Setup tabs
    $('panelValidacionesRealizadas').set('left', '0px');
    $('panelValidacionesRealizadas').set('top', '0px');
    $('panelValidacionesRealizadas').setStyle('display', 'none');
    
    $('panelValidacionesRealizadas').setStyle('height',(parseInt($('main-panel').getStyle('height')) - 2)+'px');
    $('panelValidacionesRealizadasData').setStyle('height',(parseInt($('main-panel').getStyle('height')) - 32)+'px');
			
	$('mainColumn').fade(1);
}

function modificarRefrescoProgresoValidacion(timeOutValue){
	if (timeOutValue){
		timeOutProgresoValidacion = timeOutValue;		
	}		
}

function paraProgresoValidacion(){
	stopObtenerProgresoValidacion = true;
}
 
function obtenerProgresoValidacion(){
    var url = "ActionServletValidacionFIP";
    
    new Request({
        url: url,
        async:true,
        onFailure: function(response){
            alert('ERROR.'+response);
        },
        onSuccess: procesarRespuestaEstadoValidacion,
        noCache:true,
        method: 'get'
    }).send('wsName=GET_VALIDATION_STATUS');
}

function procesarRespuestaEstadoValidacion(respuesta, xml) {
	
	if (stopObtenerProgresoValidacion == false){
		// Lo primero que se debe hacer es modificar el formulario para prevenir
	    // que se pueda enviar una solicitud de validación varias veces seguidas.
		
		// Una vez solucionado este punto, se debe actualizar el estado de
		// todos los tramites.
		var contextos = JSON.decode(respuesta);
		
		//CONTROLAR CAMPO ERROR************
		
		if (contextos) {
			Array.each(contextos,function(contexto, index){

				var codigoFip = $("codigoFIP-"+contexto.codigo);
				var estado = $("estado-"+contexto.codigo);
				var progreso = $("progreso-"+contexto.codigo);
				var subir = $('subir-'+contexto.codigo);
				var validar = $('validar-'+contexto.codigo);
				var txtFile = $('txtFile-'+contexto.codigo);
				var informeImg = $('informeImg-'+contexto.codigo);
				var imagenInformeImg = $('imagenInformeImg-'+contexto.codigo); 
				var informeND = $('informeND-'+contexto.codigo);
				
				if(codigoFip){
					if (contexto.error){
						if (codigoFip.hasClass('fvFieldError')== false){
							fornValEstadoValidacion = new formVal(contexto.codigo);
							fornValEstadoValidacion.clearErrors();
							fornValEstadoValidacion.addError($("codigoFIP-"+contexto.codigo), contexto.error);
						} //else { //En este caso deberia de actualisar la informacion del error si cambia. }
					} else {
						if (codigoFip.hasClass('fvFieldError') == true){
							//fornValEstadoValidacion = new formVal(contexto.codigo);
							//fornValEstadoValidacion.clearErrors();
													
							//Elimino el borde rojo
							codigoFip.removeClass('fvFieldError');
							//Elimino el texto del error
							var errorMsg = codigoFip.getNext();
							//var errorMsg = Slick.find($("codigoFIP-"+contexto.codigo).parent, 'div.fvErrorMsg');
							if (errorMsg && errorMsg.hasClass('fvErrorMsg') == true){ errorMsg.destroy(); }
						}					
					}
				}					
				
				if(estado){
					estado.set('html', contexto.estado.descripcion);
					progreso.set('html',contexto.progreso);
					
					switch (contexto.estado.codigo) {
					case 0:	//Pendiente
							informeND.erase('style');
							informeImg.set('style', 'position: absolute; visibility: hidden;');
							imagenInformeImg.set('style', 'display: none;');
							subir.erase('disabled');
							txtFile.erase('disabled');
							progreso.set('style','text-align: center;');
							validar.setProperty('disabled','disabled');							
						break;
					case 5: // Subiendo
					case 15: // Desplegando
					case 25: // Guardando
					case 40: // Validando
							informeND.erase('style');
							informeImg.set('style', 'position: absolute; visibility: hidden;');
							imagenInformeImg.set('style', 'display: none;');
							subir.setProperty('disabled','disabled');
							txtFile.setProperty('disabled','disabled');
							progreso.set('style','text-align: center; background-image: url(styles/images/ajax-loader-24-transparente.gif); background-position: center center; background-repeat: no-repeat;');
					case 10: // Subido
					case 20: // Desplegado
							subir.setProperty('disabled','disabled');
							txtFile.setProperty('disabled','disabled');
							validar.setProperty('disabled','disabled');
						break;
					case 30: // Guardado
							informeND.erase('style');
							informeImg.set('style', 'position: absolute; visibility: hidden;');														
							imagenInformeImg.set('style', 'display: none;');
							progreso.set('style','text-align: center;');
							subir.erase('disabled');
							txtFile.erase('disabled');
							validar.erase('disabled');
						break;
					case 50: // Validacion erronea
							informeND.set('style', 'position:absolute; visibility: hidden;');							
							informeImg.set('style', 'width: 100%;');
							imagenInformeImg.set('style', 'display: block;');
							progreso.set('style','text-align: center;');
							subir.erase('disabled');
							txtFile.erase('disabled');
							validar.setProperty('disabled','disabled');
						break;
					case 60: // Validado
							informeND.set('style', 'position:absolute; visibility: hidden;');							
							informeImg.set('style', 'width: 100%;');
							imagenInformeImg.set('style', 'display: block;');
							progreso.set('style','text-align: center;');
							subir.erase('disabled'); // El boton permanece activo para permitir subir ficheros
							txtFile.erase('disabled');
							validar.setProperty('disabled','disabled');
						break;
					case 70: // Consolidado
							informeND.set('style', 'position:absolute; visibility: hidden;');							
							informeImg.set('style', 'width: 100%;');
							imagenInformeImg.set('style', 'display: block;');
							progreso.set('style','text-align: center;');
							subir.setProperty('disabled','disabled');
							txtFile.setProperty('disabled','disabled');
							validar.setProperty('disabled','disabled');
						break;
					default:
						break;
					}
					
					//Si el que se estaba subiendo ya ha terminado de subirse, no hay subiendo.
					if (contexto.codigo == subiendo && contexto.estado.codigo != 5) {
						subiendo = undefined;
					}					
				} //fin de si estado
				
				if (subiendo && subir) {
					subir.setProperty('disabled','disabled');
				}
				
			}); //fin del for			
		} //Fin if contextos
		
		window.setTimeout(obtenerProgresoValidacion, timeOutProgresoValidacion);
	}  else { 	//Fin control de bucle
		stopObtenerProgresoValidacion = false; //lo modifico par la siguiente llamada
	} 	

}

//onsubmit="setTimeout(obtenerProgresoSubida('<%= ctxtActual.getCodigoFip()%>'),2000);"
function obtenerProgresoSubida(tramite){
	
    var url = "ActionServletValidacionFIP";
    
    // Desactivo el botón hasta que termine el proceso iniciado.
    subiendo = tramite;
    
    iniciadoProgresoSubida = true;
    if (timeOutProgresoValidacion == 10000){
    	obtenerProgresoValidacion();
    }    
    modificarRefrescoProgresoValidacion(2000);
    
    
    new Request({
        url: url,
        data: {
        	'wsName': 'GET_UPLOAD_STATUS',
        	'tramite': tramite
        },
        async:true,
        onFailure: function(response){
            alert('ERROR.'+response);
        },
        onSuccess: procesarRespuestaEstadoSubida,
        noCache:true,
        method: 'get'

    }).send();
    
    //window.setTimeout(uploadFile.send(), 20);
}

function procesarRespuestaEstadoSubida(responseText, responseXML){

	var contextos = JSON.decode(responseText);
	
	var seguir = true;
	iniciadoProgresoSubida = true;
	if (contextos) {
		
		var estado = $("estado-"+contextos[0].codigo);
		if (estado){
			estado.set('html', contextos[0].estado.descripcion);
		}		
		
		var progreso = $("progreso-"+contextos[0].codigo);
		if (progreso){
			progreso.set('html',contextos[0].progreso);
		}		
		
		if (contextos[0].finalizado == 'true') {
			seguir = false;
			iniciadoProgresoSubida = false;
			modificarRefrescoProgresoValidacion(10000);
		}
		
		if (seguir)
			window.setTimeout(function () {obtenerProgresoSubida(contextos[0].codigo);}, 1000);
	}
}

function chargeTabValidacionesRealizadas(){
	//Actualizo los datos del tab de validaciones realizadas
	$('panelValidacionesRealizadasData').empty();
	
	MUI.updateContent({
		element: $('main-panel'),
		childElement: $('panelValidacionesRealizadasData'),
		url: 'Pages/ValidadorFIP/ValidacionesRealizadasDataTable.jsp',
		padding: { top: 2, right: 2, bottom: 2, left: 2 }									
	});
}
