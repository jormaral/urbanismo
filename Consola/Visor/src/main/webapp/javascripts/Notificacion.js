/*
    Document   : Notificacion
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/
var Notificacion=new Class({
    debug:false,
    initialize:function(debug){
        if(debug=='_debug' || debug==true)
        {
            this.debug=true;
        }
        
    },
    log:function(texto,nomClase){
        if(this.debug)
        {
            if(nomClase==null || nomClase==undefined || nomClase=="")
            {
                nomClase="VISOR"
            }
            if(!Browser.Engine.trident && console!=undefined)
            {
                console.log('>> ['+nomClase.toUpperCase()+'] - '+texto);
            }
        }
    },
    alerta:function(texto,nomClase){
        if(this.debug)
        {
            if(nomClase==null || nomClase==undefined || nomClase=="")
            {
                nomClase="VISOR"
            }
            alert('>> ['+nomClase+'] - '+texto);
        }
    }

});

