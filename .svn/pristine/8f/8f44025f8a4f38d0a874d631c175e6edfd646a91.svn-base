//initializeGlobalVariables
var textoTituloAcceso = 'SELECCIONE UNA OPCIÓN DE ACCESO';
var textoTituloConsola = 'SELECCIONE UN MÓDULO';
var idPanelCentral = 'pnlCentral';
var idioma='es'; //Hay que estandarizar TODO

var viewerTreeType = { //Define los tipos de inicios del arbol de planes
	    rpm: escape('RPM'),
	    ref: escape('REF'),
	    validacion: escape('VALIDACION'),
	};

//CODIGOS DE ROLES
var globalConfig = {
	    op_validacion: escape('VAL'), //Validador
	    op_gestionplanes: escape('PLA'), //GEstor de planes
	    op_consola: escape('CON'), //Operador consola
	    op_refundido: escape('REF'), //Refundido
	    op_configvisor: escape('CFV'), //Configurador de visor
	    op_configfichas: escape('FIC') //Configurador de fichas
	}; //Aun no entiendo para que se usa esta lista de variables TODO

//initializeGlobalFunctions	
function runServiceAsync(parametros,accion)
{
    new Request({
        url: 'GestionConsola',
        data:Hash.toQueryString(parametros),
        timeout: 5000,
        async:true,
        onSuccess: function(response){
            accion(response);
        },
        onFailure: function(response){
            //alert('ERROR, failure on asyncRequest ' + this.response.text);
        },
    	onTimeout: function(){
    		//alert('ERROR, timeout on asyncRequest ' + this.response.text);
    	}
    }).send();
}

function runService(parametros)
{
    var resultado = null;
    new Request({
        url: 'GestionConsola',
        data:Hash.toQueryString(parametros),
        timeout: 5000,
        async:false,
        onSuccess: function(response){
            resultado=response;
        },
        onFailure: function(){
            alert('ERROR, failure on syncRequest ' + this.response.text);
        },
        onTimeout: function(){
    		alert('ERROR, timeout on syncRequest ' + this.response.text);
    	}
    }).send();
    return resultado;
}

function runServiceAsyncJson(parametros,accion,error)
{
    new Request({
        url: 'GestionConsola',
        data:Hash.toQueryString(parametros),
        timeout: 5000,
        async:true,
        headers: {
        	'Accept': 'application/json'
    	},
        onSuccess: function(response){
        	accion(JSON.decode(response));
        },
        onFailure: function(response){
        	if (error){
        		error(JSON.decode(response));
        	}
            //alert('ERROR, failure on asyncRequest ' + this.response.text);
        },
    	onTimeout: function(){
    		if (error){
        		error(JSON.decode(response));
        	}
    		//alert('ERROR, timeout on asyncRequest ' + this.response.text);
    	}
    }).send();
}

function runServiceJson(parametros)
{
    var resultado = null;
    new Request({
        url: 'GestionConsola',
        data:Hash.toQueryString(parametros),
        timeout: 5000,
        async:false,
        headers: {
        	'Accept': 'application/json'
    	},
        onSuccess: function(response){
            resultado=JSON.decode(response,true);
        },
        onFailure: function(){
            alert('ERROR, failure on syncRequest ' + this.response.text);
        },
        onTimeout: function(){
    		alert('ERROR, timeout on syncRequest ' + this.response.text);
    	}
    }).send();
    return resultado;
}

function cargarTextoSegunIdioma(texto)
{
    //TODO DISCRIMINO SEGUN IDIOMA
    return texto;
}

/*
 * Se usa en los formularios para modificar la capitalizacion de los labels
 */
function initCap(text) {
    return text.substr(0, 1).toUpperCase() + text.substr(1);
}

/*
 * Deshabilita un control. Establece disabled = true y su opacidad al 50%
 * (semitransparente)
 *
 * @param id Id del elemento html a deshabilitar
 */
function disableControl(id){
try {
    var myCtl = $(id);
    if (!myCtl)
        return;

    myCtl.disabled = true;
    myCtl.setOpacity(0.5);
    if (myCtl.childNodes && myCtl.childNodes.length > 0){
        for (var x=0, len=myCtl.childNodes.length; x<len; x++){
            disableControl(myCtl.childNodes[x].id);
        }
    }
} catch (e) {}
}

/*
 * Habilita un control. Establece disabled = false y su opacidad al 100% (opaco)
 *
 * @param id Id del elemento html a habilitar
 */
function enableControl(id){
try {
    var myCtl = $(id);
    if (!myCtl)
        return;

    myCtl.disabled = false;
    myCtl.setOpacity(100);
    if (myCtl.childNodes && myCtl.childNodes.length > 0){
        for (var x=0, len=myCtl.childNodes.length; x<len; x++){
            enableControl(myCtl.childNodes[x].id);
        }
    }
} catch (e) {}
}

/*
 * Habilita o deshabilita un arbol de datos
*/
function enableDisableTree(treeDiv, enabled){
	
	if (treeDiv){
				
		var container;
		if (treeDiv.getParent() && treeDiv.getParent().getParent()){
	    	container = treeDiv.getParent().getParent();
	    } else {
	    	container = treeDiv;
	    }
		
		if (enabled == false){
			if (!container.hasClass('tree-disable')){
				container.addClass('tree-disable');
			}

			//Si ya exite lo elimino
			var divdisable = $('disabletree'+treeDiv.get('id'));
			if (divdisable){ divdisable.destroy(); }
			
			divdisable = new Element('div',{
		        id:'disabletree'+treeDiv.get('id'),
		        'class': 'tree-disable-DivSuperPuesto'
		    });
			
			var coo;
		    
		    if (treeDiv.getParent().getParent().getParent().getParent()){
		    	container = treeDiv.getParent().getParent().getParent().getParent();
		    	coo = container.getCoordinates();
		    } else {
		    	container = treeDiv;
		    	coo = treeDiv.getCoordinates();
		    }
		    
		    divdisable.setStyles({
		        position: 'relative',
		        top: 0, //coo.top,
		        bottom: coo.bottom,
		        left: coo.left,
		        right: coo.right,
		        width: coo.width,
		        height: coo.height
		    });
		    
		    $('pnlCentral').grab(divdisable);
		    //container.getParent().grab(divdisable);

		} else {
			if (container && container.hasClass('tree-disable')){
				container.removeClass('tree-disable');
			}

			
			var divdisable = $('disabletree'+treeDiv.get('id'));
			if (divdisable){ divdisable.destroy(); }
		}				
		
	} //fin if treeDiv	
}

/* 
* Devuelve true si el parámetro pasado es un número o un string "convertible" a
* un número
*/
function isNumeric(n) {
   return !isNaN(parseFloat(n)) && isFinite(n);
}

/* 
* Devuelve true si el texto indicado representa una direccion de correo electronico valida
*/
function isEmail(texto){ 

    var mailres = true;             
    var cadena = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ1234567890@._-";
     
    var arroba = texto.indexOf("@",0); 
    if ((texto.lastIndexOf("@")) != arroba) arroba = -1; 
     
    var punto = texto.lastIndexOf("."); 
                 
     for (var contador = 0 ; contador < texto.length ; contador++){ 
        if (cadena.indexOf(texto.substr(contador, 1),0) == -1){ 
            mailres = false; 
            break; 
     } 
    } 

    if ((arroba > 1) && (arroba + 1 < punto) && (punto + 1 < (texto.length)) && (mailres == true) && (texto.indexOf("..",0) == -1)) 
     mailres = true; 
    else 
     mailres = false; 
                 
    return mailres; 
}

function isNIF(dni) {
	
	var dev = false;
	
  numero = dni.substr(0,dni.length-1);
  let = dni.substr(dni.length-1,1);
  numero = numero % 23;
  letra='TRWAGMYFPDXBNJZSQVHLCKET';
  letra=letra.substring(numero,numero+1);
  if (letra==let) {
    dev = true;
  }
  
  return dev;
}

function _incrementarNumHijosNodoArbol(nodo){
    if (nodo === null){
        return;
    }
    
    var re= /\([0-9]+\)$/;
    var text = nodo.text;
    var match = re.exec(text);
    var newNodeName = '';
    if(match === null){
        newNodeName = text.trim() + ' (1)';
    } else {
        var nodeName = text.substring(0, match.index);
        var nodeNum = text.substring(match.index +1, match.index + match[0].length-1);
        var newChildCount = 1+parseInt(nodeNum);
        newNodeName = nodeName +'('+newChildCount+')';
    }
    
    nodo.text = newNodeName;
    nodo.update();
}

function _decrementarNumHijosNodoArbol(nodo){
    if (nodo === null){
        return;
    }

    var re= /\([0-9]+\)$/;
    var text = nodo.text;
    var match = re.exec(text);
    var newNodeName = '';
    if(match === null){
        newNodeName = text.trim() + ' (1)';
    } else {
        var nodeName = text.substring(0, match.index);
        var nodeNum = text.substring(match.index +1, match.index + match[0].length-1);
        var newChildCount = parseInt(nodeNum)-1;
        if (newChildCount === 0){
            newNodeName = nodeName.trim();
        } else {
            newNodeName = nodeName +'('+newChildCount+')';
        }
    }

    nodo.text = newNodeName;
    nodo.update();
}


function initializeGlobalEvents() {

    // Deactivate menu header links
    $$('a.returnFalse').each(function (el) {
        el.addEvent('click', function (e) {
            new Event(e).stop();
        });
    });
}

//Esto inicializa el manejo de mascaras
 

window.addEvent('load',function(){
	
	initializeGlobalEvents();
	
	//Cada vez que se recarga la pagina se cargan las funciones y hay que controlar el estatus del usuario
	UpDateConsoleStatus();
	
	initializeLogingEvents();		
	
	/*
    MUI.myChain = new Chain();
    MUI.myChain.chain(
		function () { MUI.Desktop.initialize(); },
		function () { MUI.Dock.initialize(); }
	).callChain();*/	
});

window.addEvent('resize',function(){
	
	window.removeEvent('resize');
	
	if (activeView == "CONSOLE"){
		if (MUI.Desktop){
    		MUI.Desktop.onBrowserResize();
    	}
	} else {
		if(loginPanelsState=="OPENED")
	    {
	    	ajustarPaneles_AbrirLogin();
	    }
	    else
	    {
	    	ajustarPaneles_CerrarLogin();
	    }
	}	   
});