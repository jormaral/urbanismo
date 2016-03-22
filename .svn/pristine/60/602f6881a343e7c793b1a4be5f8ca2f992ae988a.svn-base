/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var ConfiguradorLayerWFS=new Class({
    url:'',
    initialize:function(boton,idUrl){

        $(boton).addEvent('click',function(){

            var url=$(idUrl).getProperty('value');
            url=jsIO.reformatURL(url,'WFS');
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
         
         new Request({
             url: url,
             timeout: 5000,
             async:true,
             onSuccess: function(response){
            	//PARSEO EL XML
                 var WFSXML=jsIO.leerXMLFromString(response);
                 this.leerLayers(WFSXML);
             },
             onFailure: function(response){
                 alert('Ha ocurrido un error, no ha sido posible cargar las capas y el formato disponble de la URL indicada');
             },
         	onTimeout: function(){
         		alert('No se ha obtenido respuesta, no ha sido posible cargar las capas y el formato disponble de la URL indicada');
         	}
         }).send();                  
        },
            leerLayers:function(layerPpal){
            	var stringLayers='';
            	
                if(layerPpal!=undefined)
                {
                    //LEO LAS CAPAS
                    var layers=layerPpal.getElementsByTagName("FeatureType");                    
                    for (var i = 0; i < layers.length; i++) {
                      var nombres=layers[i].getElementsByTagName("Name");
                      var titulos=layers[i].getElementsByTagName("Title");
                      
                      var nombre="";
                      var titulo="";
                      
                      if ((nombres[0]) && (nombres[0].childNodes[0])) nombre=nombres[0].childNodes[0].nodeValue;
                      if ((titulos[0]) && (titulos[0].childNodes[0])) titulo=titulos[0].childNodes[0].nodeValue;

                      if (!titulo){
                    	  titulo=nombre; //en caso de que no aparezca el Title se coge el Name
                      } else if (this.estaRepetido(layers,titulo,i)) {
                    	  titulo=nombre;//en caso de que el Title este duplicado se coge el name
                      }
                      
                      //Anadimos la capa al select para su visualizacion
                      new Element('option',{
                          'value':nombre,
                          'html':titulo
                      }).injectInside($('WFSinputLayers'));
                      
                      //Se anade la capa separada por comas
                      stringLayers=stringLayers+nombre+",";
                    }

                    if ((layers) && (layers.length>0)){
                        //Si se encuentran los layers se quita el mensaje de aviso
                        $('WFSinputLayers').options[0].destroy();
                        stringLayers=stringLayers.substr(0,stringLayers.length-1);
                    }
                    else $('WFSinputLayers').options[0].text='No existen capas';
                }
                else
                {
                    alert(jsIO.cargarTextoSegunIdioma('No ha sido posible cargar las capas de este servicio. Inténtelo mas tarde o contacte con el proveedor del servicio seleccionado'));
                }

                return stringLayers;
            },
                estaRepetido:function(lista,elem, posicion){
                    var estaRepetido=false;
                    for (var i = 0; i < lista.length; i++) {
                        if (i!=posicion){ //nos saltamos la comparación del elemento consigo mismo
                            var titulos=lista[i].getElementsByTagName("Title");
                            //comparación del elemento con todos los titulos del documento
                            if ((titulos[0]) && (titulos[0].childNodes[0]) && (titulos[0].childNodes[0].nodeValue==elem)){
                               estaRepetido=true;
                               break;
                            }
                        }
                    }
                    return estaRepetido;
                }
    });




