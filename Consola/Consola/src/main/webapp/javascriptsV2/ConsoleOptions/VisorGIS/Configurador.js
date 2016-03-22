/*
 * JSP referenciadas:
 * 	google.jsp
 * 	wms.jsp
 * 	kml.jsp
 * 	rutaJSPsConfiguradorVisorGis +'nuevaCarpeta.jsp
 * 
 * RUTA DE LAS PAGINAS JSPs referenciadas
 */
var rutaJSPsConfiguradorVisorGis = 'Pages/VisorGIS/confVisor/';

var Configurador=new Class({
//ATRIBUTOS
    idAmbito:0,
    url:null,
    xml:null,
    capa:null,
    tipoCapas:null,
    atributosGOOGLE:['layers'],//atributosGOOGLE:['text','layers'],
    atributosWMS:['text','type','url','layers','transparent','format','tile','visibility','standard','filtro'],
    atributosWFS:['text','type','url','layers','porDefecto','visibility'],
    atributosKML:['text','type','url','porDefecto','visibility'],
    treeServicios:null,
    JW_Capa:undefined,


//METODOS
initialize:function(idAmbito,tipoCapas,contenedor){

    var esNumAmbito=!isNaN(parseInt(idAmbito));
    if (esNumAmbito){ this.idAmbito= parseInt(idAmbito);}
    if (tipoCapas) this.tipoCapas=tipoCapas;

    //Si el ambito es un numero se carga el arbol
    if (esNumAmbito){
    	
        this.cargarCapas();
        
        try {
        	// standards compliant; doesn't work in IE
        	if (this.xml && this.xml.toString().substr(0,5) != 'ERROR'){

            	if (this.xml.toString() != '[object XMLDocument]'){   
            		//MUI.notification(this.xml); Muestro un mensaje o no??
            		this.xml = null;        		
                }
            	
            	this.loadArbolCapas(contenedor);
            } else {
            	alert(this.xml);
            }
          }
          catch(ex) {
        	  // IE only
        	  this.loadArbolCapas(contenedor);
          }
        
        /* 
        if (this.xml && this.xml.toString().substr(0,5) != 'ERROR'){

        	if (this.xml.toString() != '[object XMLDocument]'){   
        		//MUI.notification(this.xml); Muestro un mensaje o no??
        		this.xml = null;        		
            }
        	
        	this.loadArbolCapas(contenedor);
        } else {
        	alert(this.xml);
        } */      
    } else {
    	MUI.notification("El ambito seleccionado no es un ambito valido");
    }
    
},

/*Metodo que devuelve el XML modificado al servidor cuando se da al botón de guardar
 llama a guardarXML para que genere el string modificado*/
saveArbolCapas:function(){

	//Solo permito guardar si existe alguna capa NO DEBERIA DE SER NECESARIO YA QUE SIEMPRE SE CARGA AL MENOS EL ARBOL POR DEFECTO
	//if (this.tieneCapaMapa(this.treeServicios.root)){
		var xmlRespuesta=this.guardarXML();
	    var res=runService({
	        'wsName':'SAVE_LAYERCONFIG_DE_AMBITO',
	        'idAmbito':this.idAmbito.toString(),
	        'configXML':xmlRespuesta
	    });        
	    
	    MUI.notification(res);	    
	/*} else {
		alert('No es posible guardar la configuración porque no se ha detectado ninguna capa de datos');
	}*/
},

/*Metodo que carga el fichero de configuración del servidor*/
cargarCapas:function(){

    var result = runServiceJson({
        'wsName':'GET_LAYERCONFIG_DE_AMBITO',
        'idAmbito':this.idAmbito
    });
    
    if(result.xml){
    	this.xml=jsIO.leerXMLFromString(result.xml);
    } else {    	
    	if (result.codigoError){
    		if (result.codigoError == 1){
    			this.xml = 'No existe archivo de configuración del visor para el ámbito seleccionado';
    		} else {
    			this.xml = 'ERROR: A ocurrido un error tratando de obtener el fichero de configuración para el ámbito seleccionado';
    		}
    	} else {
    		this.xml = 'ERROR: No ha sido posible obtener la configuración del visor para el ámbito seleccionado';
    	}
    }                
},

/*Metodo que dibuja el arbol según el XML obtenido.
 maneja un evento de carga de ventana intermedia 'capa' al pinchar en las carpetas o ficheros del nodo del arbol
 para seleccionar el tipo de carga deseada */
loadArbolCapas:function(contenedor){
	
   this.treeServicios = new MooTreeControl({
        div: contenedor,
        mode: 'files',
        grid: true,
        onSelect: function(node, state) {   
        	if (state){ //solo me interesa el evento si esta seleccionado
        		//Evento de pulsacion sobre el arbol de capas        	
        		this.cancelarVentana();
        		
            	if (node != null){
            		//enableControl('configuradorVisorBtnAddLayerGoogle');
            		//enableControl('configuradorVisorBtnAddLayerKml');
            		//enableControl('configuradorVisorBtnAddLayerWfs');
            		if (!node.data){
            			enableControl('configuradorVisorBtnAddFolder');
            			disableControl('configuradorVisorBtnAddLayerWms');
            			disableControl('configuradorVisorBtnDeleteLayer');
            		} else {
            			if (node.data.type){
            				if (node.data.type == 'folder'){
            					enableControl('configuradorVisorBtnAddLayerWms');
            	        		enableControl('configuradorVisorBtnDeleteLayer');
            	        		enableControl('configuradorVisorBtnAddFolder');
            	        		
            	        		//permito editar la carpeta
            	        		enableControl('configuradorVisorBtnSaveElementChanges');
    	            			enableControl('configuradorVisorBtnCancelElementChanges');
    	            			this.generarVentana('FOLDER',this,true);
            	        		
            				} else if (node.data.type == 'WMS') {
            					disableControl('configuradorVisorBtnAddFolder');
                    			disableControl('configuradorVisorBtnAddLayerWms');
                    			enableControl('configuradorVisorBtnDeleteLayer');
                    			
                    			//permito editar el wms
            	        		enableControl('configuradorVisorBtnSaveElementChanges');
    	            			enableControl('configuradorVisorBtnCancelElementChanges');
    	            			this.generarVentana('WMS',this,true);
            				} else {
            					disableControl('configuradorVisorBtnAddLayerWms');
            	    			disableControl('configuradorVisorBtnDeleteLayer');
            	    			disableControl('configuradorVisorBtnAddFolder');
            				}
            			} else {
            				enableControl('configuradorVisorBtnAddFolder');
                			disableControl('configuradorVisorBtnAddLayerWms');
                			disableControl('configuradorVisorBtnDeleteLayer');
            			}
            		}
            		
            		enableControl('configuradorVisorBtnSaveChanges');
        		} else {
        			//deshabilito todos los botones del arbol        			
        			//disableControl('configuradorVisorBtnAddLayerGoogle');
        			//disableControl('configuradorVisorBtnAddLayerKml');
        			//disableControl('configuradorVisorBtnAddLayerWfs');
        			disableControl('configuradorVisorBtnAddLayerWms');
        			disableControl('configuradorVisorBtnDeleteLayer');
        			disableControl('configuradorVisorBtnAddFolder');
        			disableControl('configuradorVisorBtnSaveChanges');
        		}
        	}        	        	        	
        }.bind(this) //end onclick capas
    },{
        text: 'CONFIGURACIÓN DEL VISOR',
        open: false
    });
   
   if (this.xml){ //SOLO SI HAY DATOS PARA CARGAR
	   (this.treeServicios.selected||this.treeServicios.root)._loaded('', this.xml);
   }  
 },
     
 /* Metodo que determina si un nodo del arbol es capa GOOGLE o no */
 isGOOGLE:function(nodo){
    return (nodo.text=='GOOGLE');
   },
   
  /*Metodo que genera los formularios específicos para cada capa segun el boton pulsado.
   * La capa se genera sobre el nodo del arbol seleccionado */
  generarVentana:function(tipo, manager, update){
	  //CARGO LOS FORMULARIOS SOBRE EL PANEL DERECHO
	  var valida=true;
      var ubicacion = this.treeServicios.selected;
      switch (tipo) {
        case 'GOOGLE':
        	
        	MUI.updateContent({
        		element: $('main-panel'),
        		url: rutaJSPsConfiguradorVisorGis +'google.jsp',
        		//header: false,
        		padding: { top: 8, right: 8, bottom: 8, left: 8 },
        		require: {
        			onload: function(){      
        				$('configuradorVisorBtnSaveElementChanges').removeEvents('click');
        				$('configuradorVisorBtnSaveElementChanges').addEvent('click',function(){
        					if ($('configuradorVisorBtnSaveElementChanges').disabled == false){
                                
        						var layer = $('GOOGLEselectLayer').getProperty('value');
                                var valoresGOOGLE=[layer];

                                var id=this.cab.getAtributosGoogle();
                                var hashGOOGLE=new Hash();
                                for(var i=0;i<valoresGOOGLE.length;i++){
                                     hashGOOGLE.set(id[i],valoresGOOGLE[i]);
                                }
                                this.cab.actualizarCapaNodo(hashGOOGLE, tipo, ubicacion);
                                
                                MUI.notification('Datos guardados');
                                
                                this.cab.cancelarVentana();
        					}                                                        
                         }.bind({cab:manager}));        				
        			}	
        		}										
        	});

            break;
        case 'WMS':
        	
        	MUI.updateContent({
        		element: $('main-panel'),
        		url: rutaJSPsConfiguradorVisorGis +'wms.jsp',
        		//header: false,
        		padding: { top: 8, right: 8, bottom: 8, left: 8 },
        		require: {
        			onload: function(){              				        				        				
        				$('configuradorVisorBtnSaveElementChanges').removeEvents('click');
        				
        				new ConfiguradorLayerWMS('WMSbotonUrl','WMSinputUrl');
        				
        				if (update){
        					$('legend').set('html','Capa WMS');
        					$('WMSinputText').value = ubicacion.text;
        	                $('WMSinputUrl').value = ubicacion.data.url;
        	                $('WMSbotonUrl').fireEvent('click');
        	                manager.setLayers(ubicacion.data.layers);
        	                $('WMSinputFormat').setProperty('value',ubicacion.data.format);        	                
        	                if (ubicacion.data.transparent) {
        	                	$('WMSselectTransparentSi').setProperty('checked','true');
        	                } else {
        	                	$('WMSselectTransparentNo').setProperty('checked','true');
        	                }
        	                if (ubicacion.data.tile) {
        	                	$('WMSselectSingleTileSi').setProperty('checked','true');
        	                } else {
        	                	$('WMSselectSingleTileNo').setProperty('checked','true');
        	                }
        	                if (ubicacion.data.visibility) {
        	                	$('WMSselectVisibilidadSi').setProperty('checked','true');
        	                } else {
        	                	$('WMSselectVisibilidadNo').setProperty('checked','true');
        	                }
        	                if (ubicacion.data.standard) {
        	                	$('WMSselectStandarSi').setProperty('checked','true');
        	                } else {
        	                	$('WMSselectStandarNo').setProperty('checked','true');
        	                }
        	                if (ubicacion.data.filtro) {
        	                	$('WMSselectFilterSi').setProperty('checked','true');
    	                	} else {
    	                		$('WMSselectFilterNo').setProperty('checked','true');
    	                	}
          				}
        				
        	             $('configuradorVisorBtnSaveElementChanges').addEvent('click',function(){
        	            	 if ($('configuradorVisorBtnSaveElementChanges').disabled == false){
        	            		         	            		 	
        	            		 	var texto = $('WMSinputText').getProperty('value');
	 	        	                var url = $('WMSinputUrl').getProperty('value');
	 	        	                var layers = this.cab.getLayers();	 	        	                
	 	        	                var transparent = $('WMSselectTransparentSi').getProperty('checked');
	 	        	                var format = $('WMSinputFormat').getProperty('value');
	 	        	                var tile = $('WMSselectSingleTileSi').getProperty('checked');
	 	        	                var visibilidad = $('WMSselectVisibilidadSi').getProperty('checked');
	 	        	                var standar = $('WMSselectStandarSi').getProperty('checked');
	 	        	                var filter = undefined;
	 	        	                if ($('WMSselectFilterSi').getProperty('checked')){
	 	        	                	filter = this.cab.idAmbito;
	 	        	                } else {
	 	        	                	filter = '';
	 	        	                }
	 	        	                
	 	        	               if (texto.length == 0 || url.length == 0 || layers.length == 0 || format.length == 0){
	 	        	            	   alert('Los datos marcados con asteriscos son obligatorios');
	 	        	            	   return;
	 	        	               } else if (this.cab.buscarNombreCapa(texto,'WMS') != null){
	 	        	            	   alert('Ya existe una capa WMS con el nombre indicado');
	 	        	            	   return;
	 	        	               }
	 	        	                
	 	        	                if (valida){
	 	        	                    var valoresWMS=[texto,'WMS',url,layers,transparent,format,tile,visibilidad,standar,filter];
	 	
	 	        	                    var id=this.cab.getAtributosWMS();
	 	        	                    var hashWMS=new Hash();
	 	        	                    for(var i=0;i<valoresWMS.length;i++){
	 	        	                        hashWMS.set(id[i],valoresWMS[i]);
	 	        	                    }
	 	        	                    
	 	        	                    if (update){		 	        	                   
		 	        	                    ubicacion.text = texto;
		 	        	                	ubicacion.data = hashWMS;
		 	        	                	ubicacion.update();
		 	        	                	
		 	        	                	MUI.notification('Datos actualizados');
		 	        	                } else {
		 	        	                	this.cab.actualizarCapaNodo(hashWMS, tipo, ubicacion);
			         	                    
			         	                    MUI.notification('Datos guardados');
		 	        	                }	 	        	                    	         	                    
	                                 
		         	                    this.cab.cancelarVentana();
	 	        	                }	        	                
        	                }
        	             }.bind({cab:manager}));   				
        			}	
        		}										
        	});

          break;
        case 'KML':
        	
        	MUI.updateContent({
        		element: $('main-panel'),
        		url: rutaJSPsConfiguradorVisorGis +'kml.jsp',
        		//header: false,
        		padding: { top: 8, right: 8, bottom: 8, left: 8 },
        		require: {
        			onload: function(){      
        				$('configuradorVisorBtnSaveElementChanges').removeEvents('click');
        				
        				$('configuradorVisorBtnSaveElementChanges').addEvent('click',function(){
        					if ($('configuradorVisorBtnSaveElementChanges').disabled == false){
        						var texto=$('KMLinputText').getProperty('value');
                                var url=$('KMLinputUrl').getProperty('value');
                                var defecto=$('KMLselectPorDefectoSi').getProperty('checked');
                                var visibilidad=$('KMLselectVisibilidadSi').getProperty('checked');

                                var valoresKML=[texto,'KML',url,defecto,visibilidad];

                                var id=this.cab.getAtributosKML();
                                var hashKML=new Hash();
                                for(var i=0;i<valoresKML.length;i++){
                                    hashKML.set(id[i],valoresKML[i]);
                                }
                                this.cab.actualizarCapaNodo(hashKML, tipo, ubicacion);
                             
                                MUI.notification('Datos guardados');
                             
                                this.cab.cancelarVentana();
        					}                            

                        }.bind({cab:manager}));        			
        			}	
        		}										
        	});

            break;
            case 'WFS':
            	
            	MUI.updateContent({
            		element: $('main-panel'),
            		url: rutaJSPsConfiguradorVisorGis +'wfs.jsp',
            		//header: false,
            		padding: { top: 8, right: 8, bottom: 8, left: 8 },
            		require: {
            			onload: function(){              				            				            				
            				$('configuradorVisorBtnSaveElementChanges').removeEvents('click');
            				
            				new ConfiguradorLayerWFS('WFSbotonUrl','WFSinputUrl');
                            $('configuradorVisorBtnSaveElementChanges').addEvent('click',function(){
                            	if ($('configuradorVisorBtnSaveElementChanges').disabled == false){
                            		var texto=$('WFSinputText').getProperty('value');
                                    var url=$('WFSinputUrl').getProperty('value');
                                    var layers=$('WFSinputLayers').getProperty('value');
                                    var defecto=$('WFSselectPorDefectoSi').getProperty('checked');
                                    var visibilidad=$('WFSselectVisibilidadSi').getProperty('checked');

                                    var valoresWFS=[texto,'WFS',url,layers,defecto,visibilidad];

                                    var id=this.cab.getAtributosWFS();
                                    var hashWFS=new Hash();
                                    for(var i=0;i<valoresWFS.length;i++){
                                        hashWFS.set(id[i],valoresWFS[i]);
                                    }
                                    this.cab.actualizarCapaNodo(hashWFS, tipo, ubicacion);
                                    
                                    MUI.notification('Datos guardados');
                                    
                                    this.cab.cancelarVentana();
                            	}                                
                            }.bind({cab:manager}));
            			}	
            		}										
            	});
            	
                break;

            case 'FOLDER':
            	
            	//Sobre un nodo de tipo folder o sobre el nodo raiz
            	if ((ubicacion.data.type && ubicacion.data.type=='folder')||(ubicacion.parent==null)){
                    //puedes añadir una carpeta en cualquier sitio menos en GOOGLE
                      if (!this.isGOOGLE(ubicacion)){
                    	  
                    	  MUI.updateContent({
                      		element: $('main-panel'),
                      		url: rutaJSPsConfiguradorVisorGis +'nuevaCarpeta.jsp',
                      		//header: false,
                      		padding: { top: 8, right: 8, bottom: 8, left: 8 },
                      		require: {
                      			onload: function(){      
                      				
                      				if (update){
                      					$('legend').set('html','Carpeta');
                      					$('CarpetaInput').value = ubicacion.text;
                      				}
                      				
                      				$('configuradorVisorBtnSaveElementChanges').removeEvents('click');
                      				
                      				$('configuradorVisorBtnSaveElementChanges').addEvent('click',function(){
                      					if ($('configuradorVisorBtnSaveElementChanges').disabled == false){
                      						//guarda el nombre de la carpeta introducida
                                            var nombre=$('CarpetaInput').getProperty('value');
                                            
                                            if (update){
        	 	        	                	ubicacion.text = nombre;
        	 	        	                	ubicacion.update();
        	 	        	                	
        	 	        	                	MUI.notification('Carpeta modificada');        	 	        	                	
        	 	        	                } else {
        	 	        	                	this.cab.addNewCarpeta(nombre,ubicacion);
                                                
                                                MUI.notification('Carpeta creada');
        	 	        	                }
                                            
                                            this.cab.cancelarVentana();
                      					}
                                      }.bind({cab:manager}));      			
                      			}	
                      		}										
                      	});
                    	  
                      }                      
                      else alert('No puede añadir una carpeta dentro de la capa GOOGLE');
                   }
                   else alert("Debe seleccionar una carpeta para añadir otra");                        	

            break;
        default:
            return;
            break;
        }	        
  },
  
  borrarCapa:function(node){
	  //CARGO LOS FORMULARIOS SOBRE EL PANEL DERECHO

	  var aBorrar = null;
	  if (node){
		  aBorrar = node;
	  } else {
		  aBorrar = this.treeServicios.selected;
	  }
      
      if (aBorrar && aBorrar.parent && confirm('Se borrará la capa seleccionada. ¿Desa continuar?')){    	  
    	//Intento borrar la capa                  
          if (aBorrar.nodes != null){
        	  aBorrar.nodes.each(function(subNode){
        		  this.borrarCapa(subNode);
        	  });  			  
          }

          if (aBorrar.parent != null){
        	  this._decrementarNumHijosNodoArbol(aBorrar.parent);  
          }      
    	  aBorrar.remove();
    	  
    	  MUI.notification('Capa borrada correctamente');
      } else {
    	  if (aBorrar && aBorrar.parent == null){
    		  alert('No puede borrar el nodo raíz del árbol.');
    	  }
      }   
  },
  
  cancelarVentana:function(){
	  
	  disableControl('configuradorVisorBtnSaveElementChanges');
	  disableControl('configuradorVisorBtnCancelElementChanges');
	  
	  MUI.updateContent({
			element: $('main-panel'),
			url: rutaJSPsConfiguradorVisorGis + 'DataViewerFormEmptyConfiguradorVisor.html',
			//header: false,
			padding: { top: 8, right: 8, bottom: 8, left: 8 },
		});
  },
  
  /* Recupera los valores seleccionados en el select multiple */
getLayers:function(){
  var layers=[];
  var num=$('WMSinputLayers').options.length;
  var j=0;
  for (var i=0;i<num;i++){
    var layer=$('WMSinputLayers').options[i];
    if (layer.selected==true){
        layers[j]=layer.value;
        j++;
    }
  }
  if (layers) layers=layers.join(',');
  return layers;
},

setLayers:function(layers){
	
	layers = layers.split(',');
	
	var layer;
	var num=$('WMSinputLayers').options.length;
	
	for (var i=0;i<layers.length;i++){
		layer = layers[i];
		for (var j=0;j<num;j++){
		  if ($('WMSinputLayers').options[j].value == layer){
			  $('WMSinputLayers').options[j].selected = true;
			  break;
		  }
		}
	}
},

/*Metodo que Actualiza el árbol con los nuevos archivos que el administrador va añadiendo*/
actualizarCapaNodo:function(valores,nomOpcion,ubicacion){

  switch (nomOpcion){
  case 'GOOGLE':
	var nodo=this.buscarCapaGOOGLE();
    if ((nodo==null) || !(nodo.nodes)){
    	//Si no existe simplemente la creo
    	this.newNodoArbol(this.atributosGOOGLE,valores,ubicacion);
    }else {
    	//Si existe explico que solo puede haber una capa google
    	alert('Solo puede existir una capa google por lo que se modificará la configuración de la ya existente');
    	nodo.nodes[0].data.layers=valores.get( this.atributosGOOGLE[0]);
    }  
    break;
  case 'WMS':
    this.newNodoArbol(this.atributosWMS,valores,ubicacion);
    break;
  case 'KML':
    this.newNodoArbol(this.atributosKML,valores,ubicacion);
    break;
  case 'WFS':
    this.newNodoArbol(this.atributosWFS,valores,ubicacion);
  break;
  }

 return;

},
  
/* Busca la capa google del arbol, si es que existe */
buscarCapaGOOGLE:function(){
    var arbol=this.treeServicios.root.nodes;
    for (var i=0;i<arbol.length;i++){
        var nodo=arbol[i];
        if (nodo.text=='GOOGLE') return nodo;
    }
    return null;
},

buscarNombreCapa:function(name,type){
    var arbol=this.treeServicios.root.nodes;
    for (var i=0;i<arbol.length;i++){
        var nodo=arbol[i];
        if (nodo.text== name && nodo.data && nodo.data.type == type) return nodo;
    }
    return null;
},

/* Busca alguna capa de mapa, si existe alguna devuelve true. Si no existe no permite guardar la configuracion */
/*
tieneCapaMapa:function(node){
    var arbol=node.nodes;
    var dev = false;
    for (var i=0;i<arbol.length;i++){
        var nodo=arbol[i];
        if (nodo.data.type !='folder') {
        	dev = true;
        	break;
        } else {
        	dev = this(nodo);
        	if (dev) {break;}
        }
    }
    return dev;
},*/

  /*Dependiendo de la situación el nuevo archivo se puede poner al final de la carpeta,
  al final del árbol o a continuación del archivo seleccionado.*/
  newNodoArbol:function(atributos,valores,ubicacion){
    var listaValores=[];
    for (var i=0;i<atributos.length;i++){
        listaValores[i]=valores.get(atributos[i]);
    }

    if (ubicacion=='') this.addNodoFinal(atributos,listaValores);
    else {
        if ((ubicacion.data.type && ubicacion.data.type=='folder')||(ubicacion.parent==null))
            this.addNodoFinalCarpeta(atributos,listaValores,ubicacion);
        else if (ubicacion.data.type && ubicacion.data.type!='folder')
                this.addNodoNextFichero(atributos,listaValores,ubicacion);
        }
  },
  
  addNodoFinalCarpeta:function (atributos,valores,id){

        var h= new Hash();
        //Solo meter en la Hash lo que vaya dentro de data
        for (var i=1;i<atributos.length;i++){
             h.set(atributos[i],valores[i]);
        }
       id.insert({
          text:valores[0],
          // data:{url:valores[1],porDefecto:valores[2],visibility:valores[3]}
          data: h
       });
  },
  
  addNodoNextFichero:function (atributos,valores,id){

        var h= new Hash();
        //Solo meter en la Hash lo que vaya dentro de data
        for (var i=1;i<atributos.length;i++){
             h.set(atributos[i],valores[i]);
        }
      //falta por ponerlo justo detras del archivo seleccionado
        id.parent.insert({
             text:valores[0],
             data: h
        });
  },
  
  addNodoFinal:function (atributos,valores){

        var h= new Hash();
        //Solo meter en la Hash lo que vaya dentro de data
        for (var i=1;i<atributos.length;i++){
             h.set(atributos[i],valores[i]);
        }
        this.treeServicios.insert({
             text:valores[0],
             data: h
        });
  },

 /*Metodo que Actualiza el árbol con las nuevas carpetas que el administrador va añadiendo*/
  addNewCarpeta:function(valor, id){
      id.insert({
         text:valor,
         icon:'styles/images/icons/folderTree.gif',
         data: {'type':'folder'}
      });
  },

/*Metodo que devuelve un XML en formato String construido a partir del árbol
  se activa cuando el administrador ha terminado y quiere guardar todas las capas configuradas*/
  guardarXML:function(){
      var arbol=this.treeServicios.root.nodes;
      this.xml=jsIO.generarXMLvacio();

      var nuevoNodo=this.xml.createElement('nodes');
      nuevoNodo.setAttribute('icon','styles/images/folderTree.gif');
      this.xml.appendChild(nuevoNodo);

      this.guardarNodes(arbol,nuevoNodo);

      var xmlString = (new XMLSerializer()).serializeToString(this.xml);
      
      xmlString = '<?xml version="1.0" encoding="UTF-8"?>'+xmlString;
      return xmlString;
  },
  
  //Recorre secuencialmente los nodos de un nivel
  guardarNodes:function(arbol,padre){
      for (var i=0;i<arbol.length;i++){

          //Atributos a guardar
          var data=arbol[i].data;
          var txt=arbol[i].text;
          var icon=arbol[i].icon;

          var nuevoNodo=this.xml.createElement('node');
          if (txt) nuevoNodo.setAttribute('text',txt);
          if (icon) nuevoNodo.setAttribute('icon',icon);
          if (data) this.addData(nuevoNodo, data);

          if (padre) padre.appendChild(nuevoNodo);
          this.recorrerNodesHijos(arbol[i],nuevoNodo);
      }
  },
  
  //Profundizar a niveles mas bajos del arbol
  recorrerNodesHijos:function(arbol,nodo){
      var arbol=arbol.nodes;
      if (arbol.length ==0) null;
      else this.guardarNodes(arbol,nodo);
  },
  
  //Anade los elementos que tiene el arbol en la variable data y que son diferentes para cada capa
  addData:function(nodo, data){
        //Mete el tipo que es un valor obligatorio y comun
        if (data.type) nodo.setAttribute('type',data.type);

        //Que tipo de Capa estamos
        for(var i=0; i<this.tipoCapas.length;i++){
        if (data.type==this.tipoCapas[i]) break;
        }

        //AÃ±adir los atributos que hayan sido configurados de todos los posibles para esa capa
        //Tambien hay que tener en cuenta los atributos no configurables y que deben estar por defecto
        switch (this.tipoCapas[i]){
            case 'GOOGLE':
                if (data.layers) nodo.setAttribute('layers',data.layers);
                if (data.porDefecto) nodo.setAttribute('porDefecto',data.porDefecto); //No configurable
                if (data.isBaseLayer) nodo.setAttribute('isBaseLayer',data.isBaseLayer); //No configurable
                if (data.visibility) nodo.setAttribute('visibility',data.visibility); //No configurable
                break;
            case 'WMS':
                if (data.url) nodo.setAttribute('url',data.url);
                if (data.layers) nodo.setAttribute('layers',data.layers);
                if (data.transparent) nodo.setAttribute('transparent',data.transparent);
                if (data.format) nodo.setAttribute('format',data.format);
                if (data.filtro) nodo.setAttribute('filter',"idambito="+data.filtro);
                if (data.standard) nodo.setAttribute('standard',data.standard);
                if (data.visibility) nodo.setAttribute('visibility',data.visibility);
                if (data.tile) nodo.setAttribute('tile',data.tile);
                break;
            case 'KML':
                if (data.url) nodo.setAttribute('url',data.url);
                if (data.porDefecto) nodo.setAttribute('porDefecto',data.porDefecto);
                if (data.visibility) nodo.setAttribute('visibility',data.visibility);
                break;
            case 'WFS':
                if (data.url) nodo.setAttribute('url',data.url);
                if (data.layers) nodo.setAttribute('layers',data.layers);
                if (data.porDefecto) nodo.setAttribute('porDefecto',data.porDefecto);
                if (data.visibility) nodo.setAttribute('visibility',data.visibility);
                break;
            default: //otro tipo de archivos:Dibujo,markets...
                if (data.porDefecto) nodo.setAttribute('porDefecto',data.porDefecto); //No configurable
                if (data.visibility) nodo.setAttribute('visibility',data.visibility); //No configurable
                break;
        }
  },
  
  getAtributosGoogle:function(){
      return this.atributosGOOGLE;
  },
  
  getAtributosWMS:function(){
      return this.atributosWMS;
  },
  
  getAtributosWFS:function(){
      return this.atributosWFS;
  },
  
  getAtributosKML:function(){
      return this.atributosKML;
  },
  
	_decrementarNumHijosNodoArbol:function(nodo){
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

});

