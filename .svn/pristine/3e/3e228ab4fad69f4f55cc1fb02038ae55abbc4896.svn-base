/*
    Document   : jsIO
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/


var jsIO=({
//    presets:{
//        idioma:'es'
//    },
    arrayContainsElems:function(array,elems){
        var arrayElementosCoincidentes=new Array();
        for (var i = 0; i < elems.length; i++) {
            if(array.contains(elems[i].toUpperCase())){
                arrayElementosCoincidentes.push(elems[i].toUpperCase());
            }
        }
        return arrayElementosCoincidentes;
    },
    cargarTextoSegunIdioma:function(texto){
        var ret=OpenLayers.i18n(texto);
        if (ret==texto){
            ret=OpenLayers.i18n(texto.toUpperCase());
        }
        if (ret==texto.toUpperCase()){
            ret=OpenLayers.i18n(texto.toLowerCase());
        }
        if (ret==texto.toLowerCase()){
            return texto;
        }else{
            return ret;
        }
    },
    curveBordersToObject:function(object,settings){
        if(object.charAt(0)!='#'){object='#'+object}
        if(settings)
        {
            curvyCorners(settings, object);
        }
        else
        {
            curvyCorners({
                      tl: {radius: 10},
                      tr: {radius: 10},
                      bl: {radius: 10},
                      br: {radius: 10},
                      antiAlias: true
                    }, object);
        }
    },
    isIE:function(){
        if(Browser.Engine.trident){return true;}else{return false;}
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
        if (temp!=null && temp!=""){return true;}else{return false;}
    },
    generarXMLvacio:function(){
        var xml;
                if(Browser.Engine.trident)
                {
                    try //Internet Explorer
                    {
                        xml=new ActiveXObject("Microsoft.XMLDOM");
                        xml.validateOnParse=false;
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
    leerXMLFromString:function(cadena){
         var xmlDoc;
          try //Internet Explorer
          {
              xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
              xmlDoc.validateOnParse=false;
              xmlDoc.async=false;
              xmlDoc.loadXML(cadena);
              return xmlDoc;
          }
          catch(e)
          {
              try //Firefox, Mozilla, Opera, etc.
              {
                    parser=new DOMParser();
                    xmlDoc=parser.parseFromString(cadena,"text/xml");
                    return xmlDoc;
              }
              catch(e)
              {
                  alert(e.message);
              }
          }
          return null;
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
                xmlDoc.validateOnParse=false;
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
    loadWindowByPost:function(url,keys,values){
        var name="ventanaSuperf";
        var newWindow = window.open(url, name);
        if (!newWindow) return false;
        var html = "";
        html += "<html><head></head><body><form id='formid' method='post' action='" + url + "'>";
        if (keys && values && (keys.length == values.length))
        for (var i=0; i < keys.length; i++)
        html += "<input type='hidden' name='" + keys[i] + "' value='" + values[i] + "'/>";
        html += "</form><script type='text/javascript'>document.getElementById(\"formid\").submit()</script></body></html>";
        newWindow.document.write(html);
        return newWindow;
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
            method:'POST',
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
        new Request({
            url: 'ActionServlet',
            data:Hash.toQueryString(datos),
            method:'POST',
            async:true,
            onSuccess: accion.bind(this),
            onFailure: error.bind(this)
        }).send();
    },
    formatoSinEspaciosParaURL:function(texto){
        while(texto.contains(" "))
        {
            texto=texto.replace(" ","%20");
        }
      return texto;
    },
    reformatURL:function(url){
        var version='1.3.0';
        var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
        var isURL=regexp.test(url);
        if(isURL)
        {

            if(!url.contains('?'))
            {
                url = url+"?"+"service=WMS&version="+version+"&request=GetCapabilities";
            }
            else
            {
                if(url.indexOf('?')==url.length-1)
                {
                  url = url+"service=WMS&version="+version+"&request=GetCapabilities";
                }
                else
                {
                  url = url+"&service=WMS&version="+version+"&request=GetCapabilities";
                }
            }
            return url;
        }
        else
        {
            return null;
        }
    },
    reformatURL_WFS:function(url){
        var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
        var isURL=regexp.test(url);
        if(isURL)
        {

            if(!url.contains('?'))
            {
                url = url+"?"+"service=WFS&version=1.1.0&request=GetCapabilities";
            }
            else
            {
                if(url.indexOf('?')==url.length-1)
                {
                  url = url+"service=WFS&version=1.1.0&request=GetCapabilities";
                }
                else
                {
                  url = url+"&service=WFS&version=1.1.0&request=GetCapabilities";
                }
            }
            return url;
        }
        else
        {
            return null;
        }
    },
    reformatURL_WFS_for_DescribeType:function(url){
        var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
        var isURL=regexp.test(url);
        if(isURL)
        {

            if(!url.contains('?'))
            {
                url = url+"?"+"service=WFS&version=1.1.0&request=DescribeFeatureType";
            }
            else
            {
                if(url.indexOf('?')==url.length-1)
                {
                  url = url+"service=WFS&version=1.1.0&request=DescribeFeatureType";
                }
                else
                {
                  url = url+"&service=WFS&version=1.1.0&request=DescribeFeatureType";
                }
            }
            return url;
        }
        else
        {
            return null;
        }
    },
    reformatURL_WMS_for_Legend:function(url){
        var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
        var isURL=regexp.test(url);
        if(isURL)
        {

            if(!url.contains('?'))
            {
                url = url+"?"+"service=WMS&version=1.1.0&request=getLegendGraphic";
            }
            else
            {
                if(url.indexOf('?')==url.length-1)
                {
                  url = url+"service=WMS&version=1.1.0&request=getLegendGraphic";
                }
                else
                {
                  url = url+"&service=WMS&version=1.1.0&request=getLegendGraphic";
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
    openWindowByPost:function(url,params){
        relocate(url,params);
    }

});

