/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var jsIO=({
    presets:{
        idioma:'es'
    },
    cargarTextoSegunIdioma:function(texto){
        return OpenLayers.i18n(texto);
    },
    curveBordersToObject:function(object,settings){
        if(object.charAt(0)!='#'){
            object='#'+object;
        };
        if(settings)
        {
            curvyCorners(settings, object);
        }
        else
        {
            curvyCorners({
                tl: {
                    radius: 10
                },
                tr: {
                    radius: 10
                },
                bl: {
                    radius: 10
                },
                br: {
                    radius: 10
                },
                antiAlias: true
            }, object);
        }
    },
    isIE:function(){
        if(Browser.Engine.trident){
            return true;
        }else{
            return false;
        }
    },
    setCookie:function(key,value,expiredays){
        Cookie.write(key, value, {
            duration:expiredays
        });
    },
    getCookie:function(key){
        return Cookie.read(key);
    },
    checkCookie:function(key){
        var temp=this.getCookie(key);
        if (temp!=null && temp!=""){
            return true;
        }else{
            return false;
        }
    },
    generarXMLvacio:function(){
        var xml;
        if(Browser.Engine.trident)
        {
            try //Internet Explorer
            {
                xml=new ActiveXObject("Microsoft.XMLDOM");
            }
            catch(e)
            {
                alert(e.message);
            }
        }
        else
        {
            try //Firefox, Mozilla, Opera, etc.
            {
                xml=document.implementation.createDocument("","",null);
            }
            catch(e)
            {
                alert(e.message);
            }

        }
        return xml;
    },
    generarXMLfromString:function(cadena,url){
        if (window.XMLHttpRequest)
        {
            xmlDoc=new window.XMLHttpRequest();
            xmlDoc.open("GET",url,false);
            xmlDoc.send(cadena);
            return;
        }
        // IE 5 and IE 6
        else if (ActiveXObject("Microsoft.XMLDOM"))
        {
            xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
            xmlDoc.async=false;
            xmlDoc.load(url);
            xmlDoc.send(cadena);
            return;
        }
        alert("Error loading document");
        return;
    },
    leerXMLFromString:function(cadena){
        var xmlDoc;
        if (this.isIE()){//Internet Explorer
          
            xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
            xmlDoc.validateOnParse=false;
            xmlDoc.async=false;
            xmlDoc.loadXML(cadena);
        }
        else
        {
            try //Firefox, Mozilla, Opera, etc.
            {
                parser=new DOMParser();
                xmlDoc=parser.parseFromString(cadena,"text/xml");
            }
            catch(e)
            {
                alert(e.message);
            }
        }
        return xmlDoc;
    },
    leerXML:function(url){
        if (window.XMLHttpRequest)
        {
            xmlDoc=new window.XMLHttpRequest();
            xmlDoc.open("GET",url,false);
            xmlDoc.send("");
            return xmlDoc.responseXML;
        }
        // IE 5 and IE 6
        else if (ActiveXObject("Microsoft.XMLDOM"))
        {
            xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
            xmlDoc.async=false;
            xmlDoc.load(url);
            return xmlDoc;
        }
        alert("Error loading document");
        return null;
    },
    leerJSONFromString:function(cadena){
        return JSON.decode(cadena);
    },
    getTipoServicioGoogle:function(cadena){
        if(google)
        {
            switch(cadena)
            {
                case 'G_HYBRID_MAP':
                    return google.maps.MapTypeId.HYBRID;//   return G_HYBRID_MAP;
                    break;
                case 'G_SATELLITE_MAP':
                    return google.maps.MapTypeId.SATELLITE;//   return G_SATELLITE_MAP;
                    break;
                case 'G_PHYSICAL_MAP':
                    return google.maps.MapTypeId.TERRAIN;//   return G_PHYSICAL_MAP;
                    break;
                default:
                    return google.maps.MapTypeId.ROADMAP;   //return G_NORMAL_MAP;
                    break;
            }
        }
        else
        {
            return null;
        }

    },
    getService:function(datos,asincrono){
        if(!asincrono)
        {
            asincrono=false;
        }
        var respuesta;
        new Request({
            url: 'ActionServlet',
            data:Hash.toQueryString(datos),
            async:asincrono,
            onSuccess: function(response){
                respuesta=response;
            }.bind(this),
            onFailure: function(response){
                respuesta=response;
            }.bind(this)
        }).send();
        return respuesta;
    },
    getServiceAsync:function(datos,accion,error){
        runServiceAsync(datos,accion);
//        new Request({
//            url: 'ActionServlet',
//            data:Hash.toQueryString(datos),
//            async:true,
//            onSuccess: accion.bind(this),
//            onFailure: error.bind(this)
//        }).send();
    },
    reformatURL:function(url){
        var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
        var isURL=regexp.test(url);
        if(isURL)
        {

            if(!url.contains('?'))
            {
                url = url+"?"+"service=WMS&version=1.1.0&request=GetCapabilities";
            }
            else
            {
                if(url.indexOf('?')==url.length-1)
                {
                    url = url+"service=WMS&version=1.1.0&request=GetCapabilities";
                }
                else
                {
                    url = url+"&service=WMS&version=1.1.0&request=GetCapabilities";
                }
            }
            return url;
        }
        else
        {
            return null;
        }
    },
    isArray:function(obj)
    {
        if(obj.constructor.toString().indexOf("Array") == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    },
    isNumero:function(nombre,valor){
        var NaN= isNaN(parseInt(valor));
        if (NaN) alert ("el valor "+ nombre +" debe ser un número");
        return !NaN;
    }

});
