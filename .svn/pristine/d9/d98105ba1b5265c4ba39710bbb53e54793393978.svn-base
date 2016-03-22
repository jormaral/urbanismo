
var showingUserNotFound = false;

//initializeLogingFunctions
function debugLoging(text)
{
    if (false)
    {
        MochaUI.notification(text);		
    }
}

function ChargeLoginType(page)
{
    ajustarPaneles_AbrirLogin();
	
    new Request({
        url: page,
        async:false,
        onSuccess: function(response){       
            $('cabLogin').setOpacity(0);
            $('cabTituloSeccion').setOpacity(0);
            $(idPanelCentral).set('html',response);
            $(idPanelCentral).fade(1);
        },
        onFailure: function(response){
            $(idPanelCentral).set('html','<center>'+cargarTextoSegunIdioma('SE HA PRODUCIDO UN PROBLEMA AL CARGAR LA PÁGINA')+'</center>');
        }
    }).send();
}

function onConfirmCloseSessionLogin(){
    if (confirExitMsg && confirExitMsg.getResult() === 'yes') {
        CloseSessionLogin();
    }
}

function CloseSessionLogin(){
    paraProgresoValidacion(); //para la carga del bucle de validacion 
	
    var result = runService({
        'wsName':'DISCONNECT_USER'
    });				
    if (result == 'true'){
        //Oculto y elimino los botones del panel superior
        $('botoneraSuperior').fade(0);
        $('botoneraSuperior').empty();
		
        ChargeLoginType('Pages/Login/disconnect.html');
        setTimeout(function(){					
            UpDateConsoleStatus();
        }, loginDisconnectTimePersistence);
    }
}



//VARIABLES
var confirExitMsg;
var loginPanelsState = "CLOSED"; //CLOSED - OPENED por defecto estan cerrados
var loginDisconnectTimePersistence = 2000; //tiempo de persistencia de la ventana de desconexión



//EVENTOS

initializeLogingEvents = function () {
			
    if ($('opcionLoginUSERLink')){
        $('opcionLoginUSERLink').addEvent('click', function(e) {
			
            if ($("opcionLoginUSERLink").get('html') == cargarTextoSegunIdioma("Desconectar")){ //DESCONECTAR
                debugLoging("Desconectar");
				
                if (iniciadoProgresoSubida){
                    var msg = 'Existe algún archivo del validador de FIPs que esta en proceso de subida, si cierra el explorador o sale de la web se suspenderá la carga de este archivo. ¿Desea salir de todas formas?';
                    confirExitMsg = new PopupMsg(msg, {
                        type: 'yesno'
                    });
                    confirExitMsg.addEvent('close', onConfirmCloseSessionLogin.bind(this));
                } else {
                    CloseSessionLogin();
                }
            }
            else{ //CONECTAR
                ChargeLoginType('Pages/Login/loginUsuario.html');
                initializeLogingUSEREvents();
            }													
        });		
    }
	

    if ($('opcionLoginUSER')){
		
        setBoteneraOptionImage($('opcionLoginUSER'), 0, 0);
		
        $('opcionLoginUSER').addEvents({
            'click':function(){
                setBoteneraOptionImage($('opcionLoginUSER'), 0, 2);
                ChargeLoginType('Pages/Login/loginUsuario.html');
                initializeLogingUSEREvents();
            },
            'mouseover':function(){
                setBoteneraOptionImage($('opcionLoginUSER'), 0, 1);
                $('cabTituloSeccion').set('html',cargarTextoSegunIdioma('ACCESO POR USUARIO/CLAVE'));
            },
            'mouseout':function(){
                setBoteneraOptionImage($('opcionLoginUSER'), 0, 0);
                $('cabTituloSeccion').set('html',cargarTextoSegunIdioma(textoTituloAcceso));
            }
        });
    }
	
    if ($('opcionLoginCERTIFICADO')){
		
        setBoteneraOptionImage($('opcionLoginCERTIFICADO'), 1, 0);
        var cp= document.getElementById("ValidacionDNIe");
        if (cp){
            cp.setStyle('visibility','visible');	
        }
        $('opcionLoginCERTIFICADO').addEvents({
            'click':function(){
                setBoteneraOptionImage($('opcionLoginCERTIFICADO'), 1, 2);
                ChargeLoginType('Pages/Login/loginCertificado.jsp');
                initializeLogingCERTIFICATEEvents();
            },
            'mouseover':function(){
                setBoteneraOptionImage($('opcionLoginCERTIFICADO'), 1, 1);
                $('cabTituloSeccion').set('html',cargarTextoSegunIdioma('ACCESO POR DNI ELECTRÓNICO'));
            },
            'mouseout':function(){
                setBoteneraOptionImage($('opcionLoginCERTIFICADO'), 1, 0);
                $('cabTituloSeccion').set('html',cargarTextoSegunIdioma(textoTituloAcceso));
            }
        });
    }
};

initializeLogingUSEREvents = function() {
	
    if($('loginBtnSubmit'))
    {
        debugLoging('initializeLogingUSEREvents');
    	
        $('loginTxtPasswd').addEvent('keyup',function(e){
            if(e.code==13)
            {
                if (showingUserNotFound == false){
                    $('loginBtnSubmit').fireEvent('click');
                } else {
                    showingUserNotFound = false;
                }                
            }
        });
        
        $('loginBtnSubmit').addEvent('click',function(e){
            usuarioLogueado = $('loginTxtUsuario').getProperty('value');
            passUsuarioLogueado = $('loginTxtPasswd').getProperty('value');
            var result = runService({
                'wsName':'VALIDAR_USUARIO',
                'USUARIO':$('loginTxtUsuario').getProperty('value'),
                'PASSWD':$('loginTxtPasswd').getProperty('value')
            });
            if(result=='OK')
            {                	
                debugLoging("Cargar consola");
                $('cabTituloSeccion').fade(1);
                UpDateConsoleStatus();
            } else {
                showingUserNotFound = true;
                alert("ERROR: "+result);
            }
        });
        
        $('loginBtnBack').addEvent('click',function(e){        		
            ajustarPaneles_CerrarLogin();
            $('cabLogin').fade(1);
            $('cabTituloSeccion').fade(1);
        });
        
        $('loginTxtUsuario').focus();
    }
};
    
initializeLogingCERTIFICATEEvents = function() {
	
	
    $('loginBtnSubmitCert').addEvent('click',function(e){
        var cp= document.getElementById("ValidacionDNIe");
        var result = runService({
            'wsName':'VALIDAR_USUARIO',
            'DNI':cp.getDNInumber()
        });
        
        if(result=='OK')
        {
            debugLoging("Cargar consola");    
            $('cabTituloSeccion').fade(1);
            UpDateConsoleStatus();
        } else {
            alert("ERROR: "+result);
        }
    });
    $('loginBtnBackCert').addEvent('click',function(e){
        ajustarPaneles_CerrarLogin();
        $('cabLogin').fade(1);
        $('cabTituloSeccion').fade(1);
        var cp= document.getElementById("ValidacionDNIe");
        if (cp){
            cp.setStyle('visibility','hidden');
        }
    });
};


