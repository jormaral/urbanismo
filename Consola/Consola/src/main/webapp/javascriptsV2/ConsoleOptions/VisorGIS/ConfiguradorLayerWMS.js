/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var ConfiguradorLayerWMS=new Class({
    url:'',
    initialize:function(boton,idUrl){

        $(boton).addEvent('click',function(){

            var url=$(idUrl).getProperty('value');
            url=jsIO.reformatURL(url,'WMS');
            if(url!=null)
            {
                this.url=url;
                this.leerDataFromURL();
            }
            else
            {
                alert(jsIO.cargarTextoSegunIdioma('LA URL ENVIADA NO TIENE UN FORMATO CORRECTO'));
            }

        }.bind(this));
     },
     leerDataFromURL:function(){
    	 var url=this.url;
         //PIDO EL XML    	 
    	 
         var response = runService({'wsName':'CROSS-DOMAIN','url':url});
         
         //PARSEO EL XML
    	 var wmsXML=jsIO.leerXMLFromString(response);

       	//LEO EL NOMBRE DEL WMS
         /*var title;
         if(wmsXML.getElementsByTagName('Title'))
         {
             title=wmsXML.getElementsByTagName('Title')[0].childNodes[0].nodeValue;
         }*/

         //LEO LAS CAPAS PRINCIPALES QUE ENGLOBAN AL RESTO
         var layerPpal=wmsXML.getElementsByTagName("Layer")[0];
         var formatPpal=wmsXML.getElementsByTagName("GetMap")[0];
         //Metodos que parsean el documento
         this.leerLayers(layerPpal);
         this.leerFormats(formatPpal);
        },
        leerLayers:function(layerPpal){
        	var stringLayers = '';
        	
        	if(layerPpal!=undefined)
            {
                //LEO LAS CAPAS
                var layers=layerPpal.getElementsByTagName("Layer");                    
                
                for (var i = 0; i < layers.length; i++) {
                  var nombres=layers[i].getElementsByTagName("Name");
                  var titulos=layers[i].getElementsByTagName("Title");
                  
                  //Anadimos la capa al select para su visualizacion
                  new Element('option',{
                      'value':nombres[0].childNodes[0].nodeValue,
                      'html':titulos[0].childNodes[0].nodeValue
                  }).injectInside($('WMSinputLayers'));
                  
                  //Se anade la capa separada por comas
                  stringLayers=stringLayers+nombres[0].childNodes[0].nodeValue+",";
                }

                if ((layers) && (layers.length>0)){
                    //Si se encuentran los layers se quita el mensaje de aviso
                    $('WMSinputLayers').options[0].destroy();
                    $('WMSinputLayers').setProperties({'size':5});
                    stringLayers=stringLayers.substr(0,stringLayers.length-1);
                }
                else{
                	$('WMSinputLayers').options[0].text='No existen capas';
                	$('WMSinputLayers').setProperties({'size':1});
                }
            }
            else
            {
            	$('WMSinputLayers').empty();
                alert('No ha sido posible cargar las capas de este servicio. Inténtelo mas tarde o contacte con el proveedor del servicio seleccionado');
            }

            return stringLayers;
        },
        leerFormats:function(formatPpal){
            if(formatPpal!=undefined){
            	//LEO LOS FORMATOS
                var formats=formatPpal.getElementsByTagName("Format");
                
                for (var i = 0; i < formats.length; i++) {
                    var formato=formats[i].childNodes[0].nodeValue;
                    var f=formato.split('/');
                    if (f[0]=='image'){
                        new Element('option',{
                          'value':formato,
                          'html':f[1]
                        }).injectInside($('WMSinputFormat'));
                    }
                }
                
                if ((formats) && (formats.length>0)){
                    //Si se encuentran los layers se quita el mensaje de aviso y se agranda el espacio
                    $('WMSinputFormat').options[0].destroy();
                }
                else  $('WMSinputFormat').options[0].text='No existen formatos';
            }
            else{
                  $('WMSinputFormat').empty();
                  alert('No ha sido posible cargar los formatos de este servicio. Inténtelo mas tarde o contacte con el proveedor del servicio seleccionado');
            }
        }
    });


