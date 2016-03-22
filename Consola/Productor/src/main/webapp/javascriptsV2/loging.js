
var showingUserNotFound = false;

function runService(parametros)
{
    var resultado = null;
    new Request({
        url: '../ActionServletFip1',
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


//EVENTOS
initializeLogingUSEREvents = function() {
	
    if($('loginBtnSubmit'))
    {    	    	
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
            //usuarioLogueado = $('loginTxtUsuario').getProperty('value');
            //passUsuarioLogueado = $('loginTxtPasswd').getProperty('value');
            var result = runService({
                'wsName':'VALIDAR_CP',
                'cprod':$('loginTxtUsuario').options[$('loginTxtUsuario').selectedIndex].id,
                'pass':$('loginTxtPasswd').getProperty('value')
            });
            if(result=='S')
            {                	
                window.location="../indice";
            } else {
                showingUserNotFound = true;
                alert("ERROR: Centro de producción no encontrado o contraseña incorrecta");
            }
        });
        
        $('loginTxtUsuario').focus();
    }
};

//EVENTOS
initializeUnLogingUSEREvents = function() {
	
    if ($('opcionLoginUSERLink')){
        $('opcionLoginUSERLink').addEvent('click', function(e) {
        	//var result = 
        	runService({
                'wsName':'DESCONECTAR'
            });
        	window.location="../indice";							
        });		
    }
};


