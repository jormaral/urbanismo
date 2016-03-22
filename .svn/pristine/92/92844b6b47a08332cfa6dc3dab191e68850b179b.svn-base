/*////////////////////////////////////////////////////////////////////////////////////////////////////*/
/*CAMBIOS EN LA CLASE OpenLayers/Popup.js */

        function redefinirPOPUP(popupTemp)
        {
            popupTemp.div.style.overflow="visible";
            $(popupTemp.div).setStyle('opacity',0);
            //asignoTipoTWEENGenerico($(popupTemp.div));
            $(popupTemp.div).tween('opacity',0.95);
            popupTemp.draw= function(px) {
                if (px == null) {
                    if ((this.lonlat != null) && (this.map != null)) {
                        px = this.map.getLayerPxFromLonLat(this.lonlat);
                    }
                }
////                if(Browser.Engine.trident==false)
////                {
                  this.div.style.overflow="visible";
                  this.div.appendChild(new Element('div',{
                      'id':'prueba',
                      'class':'olPopupArrow'
                  }));
                  this.div.appendChild(new Element('div',{
                      'id':'iconoPopup',
                      'class':'olPopupIcon'
                  }));
////                }

                //listen to movestart, moveend to disable overflow (FF bug)
                if (OpenLayers.Util.getBrowserName() == 'firefox') {
                    this.map.events.register("movestart", this, function() {
                        var style = document.defaultView.getComputedStyle(
                            this.contentDiv, null
                        );
                        var currentOverflow = style.getPropertyValue("overflow");
                        if (currentOverflow != "hidden") {
                            this.contentDiv._oldOverflow = currentOverflow;
                            this.contentDiv.style.overflow = "hidden";
                        }
                    });
                    this.map.events.register("moveend", this, function() {
                        var oldOverflow = this.contentDiv._oldOverflow;
                        if (oldOverflow) {
                            this.contentDiv.style.overflow = oldOverflow;
                            this.contentDiv._oldOverflow = null;
                        }
                    });
                }
                px=new OpenLayers.Pixel(px.x+15,px.y+15);
                this.moveTo(px);
                if (!this.autoSize && !this.size) {
                    this.setSize(this.contentSize);
                }
                this.setBackgroundColor();
                this.setOpacity();
                //this.setBorder();
                this.setContentHTML();

                if (this.panMapIfOutOfView) {
                    this.panIntoView();
                }
                
                
                return this.div;
            };

        }


function redefinirGML(gml)
{
    gml.loadGML=function(){
     //if (!this.loaded) {

            OpenLayers.Request.GET({
                url: this.url,
                success: this.requestSuccess,
                failure: this.requestFailure,
                scope: this
            });
            this.loaded = true;
       // }
    };
    gml.requestSuccess=function(request) {
        var doc = request.responseXML;

        if (!doc || !doc.documentElement) {
            doc = request.responseText;
        }

        var options = {};

        OpenLayers.Util.extend(options, this.formatOptions);
        if (this.map && !this.projection.equals(this.map.getProjectionObject())) {
            options.externalProjection = this.projection;
            options.internalProjection = this.map.getProjectionObject();
        }
        /*MODIDIED*///////////////////////////////////////////////////////////////////////////////////////////////////////
             var gml=redefinirKML(this.format,options);
        //SUSTITUIDO//var gml = this.format ? new this.format(options) : new OpenLayers.Format.GML(options);
        /*MODIDIED*///////////////////////////////////////////////////////////////////////////////////////////////////////
        this.addFeatures(gml.read(doc));
        this.events.triggerEvent("loadend");
    };
}


function redefinirKML(format,options)
{
            var gml;
             if(format)
             {
                 if(format==OpenLayers.Format.KML)
                 {
                     var kml=new format(options);
                     //REDEFINO ALGUNAS FUNCIONES
                        gml=redefinoFuncionesKMLParaAdmitirDescargaDeURL(kml);


                 }
                 else
                 {
                     gml=new format(options);
                 }
             }
             else
             {
                 gml=new OpenLayers.Format.GML(options);
             }
             return gml;
}


function redefinoFuncionesKMLParaAdmitirDescargaDeURL(kml)
{
            kml.maxDepth=1;
            kml.read=function(data){
                  this.features = [];
                  this.styles   = {};
                  this.fetched  = {};

                  // Set default options
                  var options = {
                      depth: 0,//MODIFIED//this.maxDepth,
                      styleBaseUrl: this.styleBaseUrl
                  };

                  return this.parseData(data, options);
            }

            kml.parseFeatures= function(nodes, options) {
 	        var features = new Array(nodes.length);
 	        for(var i=0, len=nodes.length; i<len; i++) {
 	            var featureNode = nodes[i];
 	            var feature = this.parseFeature.apply(this,[featureNode]) ;
 	            if(feature) {

 	                // Create reference to styleUrl
 	                if (this.extractStyles && feature.attributes &&
 	                    feature.attributes.styleUrl) {
 	                    feature.style = this.getStyle(feature.attributes.styleUrl, options);
 	                }

 	                if (this.extractStyles) {
 	                    // Make sure that <Style> nodes within a placemark are
 	                    // processed as well
 	                    var inlineStyleNode = this.getElementsByTagNameNS(featureNode,
 	                                                        "*",
 	                                                        "Style")[0];
 	                    if (inlineStyleNode) {
 	                        var inlineStyle= this.parseStyle(inlineStyleNode);
 	                        if (inlineStyle) {
 	                            feature.style = OpenLayers.Util.extend(
 	                                feature.style, inlineStyle
 	                            );
 	                        }
 	                    }
 	                }

 	                // add feature to list of features
                            feature.geometry=feature.geometry.transform(new OpenLayers.Projection("EPSG:4326"),new OpenLayers.Projection("EPSG:32630"))
                            features[i] = feature;


 	            } else {
 	                throw "Bad Placemark: " + i;
 	            }
 	        }
 	        // add new features to existing feature list
 	        this.features = this.features.concat(features);
 	    }
            //REDEFINO PARSELINKS PARA TRANSFORMAR LA PETICION DE GEOSERVER DE KMZ A KML
            kml.parseLinks= function(nodes, options) {

                  // Fetch external links <NetworkLink> and <Link>
                  // Don't do anything if we have reached our maximum depth for recursion
                  if (options.depth >= this.maxDepth) {
                      return false;
                  }

                  // increase depth
                  var newOptions = OpenLayers.Util.extend({}, options);
                  newOptions.depth++;

                  for(var i=0, len=nodes.length; i<len; i++) {
                      var href = this.parseProperty(nodes[i], "*", "href");

                      if(href && !this.fetched[href]) {
                          /*MODIFIED*///////////////////////////////////////////////////////////////////////////////////////
                            //SUSTITUYO KMZ POR KML EN LA URL
                                href=href.replace("kmz","kml");
                            //AÑADO EL BBOX DEL MAPA EN LA URL
                                var bounds=map.getExtent();
                                    bounds=bounds.transform(new OpenLayers.Projection("EPSG:32630"),new OpenLayers.Projection("EPSG:4326"));
                                    //alert(bounds);
                                href=href+'&BBOX='+bounds.toArray();
                          /*MODIFIED*/////////////////////////////////////////////////////////////////////////////////

                          this.fetched[href] = true; // prevent reloading the same urls

                          var data = this.fetchLink(href);

                          if (data) {
                              data=data.replace("<kml>","<kml  xmlns='http://earth.google.com/kml/2.0'>")
                              this.parseData(data, newOptions);
                          }
                      }
                  }

              }

              //REDEFINO EL PARSESTYLE PARA SALTAR EL BUG DE GEOSERVER QUE PONE UN ICONO VACIO PARA CADA PUNTO DEVUELTO
              kml.parseStyle= function(node) {
                    var style = {};

                    var types = ["LineStyle", "PolyStyle", "IconStyle", "BalloonStyle"];
                    var type, nodeList, geometry, parser;
                    for(var i=0, len=types.length; i<len; ++i) {
                        type = types[i];
                        styleTypeNode = this.getElementsByTagNameNS(node,
                                                               "*", type);

                        if(styleTypeNode.length==0) {
                            continue;
                        }

                        // only deal with first geometry of this type
                        switch (type.toLowerCase()) {
                            case "linestyle":
                                var color = this.parseProperty(styleTypeNode[0], "*", "color");
                                if (color) {
                                    var matches = (color.toString()).match(
                                                                     this.regExes.kmlColor);

                                    // transparency
                                    var alpha = matches[1];
                                    style["strokeOpacity"] = parseInt(alpha, 16) / 255;

                                    // rgb colors (google uses bgr)
                                    var b = matches[2];
                                    var g = matches[3];
                                    var r = matches[4];
                                    style["strokeColor"] = "#" + r + g + b;
                                }

                                var width = this.parseProperty(styleTypeNode[0], "*", "width");
                                if (width) {
                                    style["strokeWidth"] = width;
                                }

                            case "polystyle":
                                var color = this.parseProperty(styleTypeNode[0], "*", "color");
                                if (color) {
                                    var matches = (color.toString()).match(
                                                                     this.regExes.kmlColor);

                                    // transparency
                                    var alpha = matches[1];
                                    style["fillOpacity"] = parseInt(alpha, 16) / 255;

                                    // rgb colors (google uses bgr)
                                    var b = matches[2];
                                    var g = matches[3];
                                    var r = matches[4];
                                    style["fillColor"] = "#" + r + g + b;
                                }

                                break;
                            case "iconstyle":
                                      var node_aux=null;
                                      for(var h=0;h<styleTypeNode.length;h++)
                                      {

                                          var hrefs=styleTypeNode[h].getElementsByTagName('href');
                                              if(hrefs.length>0)
                                              {
                                                  node_aux=styleTypeNode[h];
                                                  break;
                                              }

                                      }
                                      if(node_aux!=null)
                                      {
                                            // set scale
                                            var scale = parseFloat(this.parseProperty(node_aux,
                                                                                  "*", "scale") || 1);

                                            // set default width and height of icon
                                            var width = 32 * scale;
                                            var height = 32 * scale;

                                            var iconNode = this.getElementsByTagNameNS(node_aux,
                                                                       "*",
                                                                       "Icon")[0];

                                            if (iconNode) {
                                                var href = this.parseProperty(iconNode, "*", "href");
                                                if (href) {

                                                    var w = this.parseProperty(iconNode, "*", "w");
                                                    var h = this.parseProperty(iconNode, "*", "h");

                                                    // Settings for Google specific icons that are 64x64
                                                    // We set the width and height to 64 and halve the
                                                    // scale to prevent icons from being too big
                                                    var google = "http://maps.google.com/mapfiles/kml";
                                                    if (OpenLayers.String.startsWith(
                                                                         href, google) && !w && !h) {
                                                        w = 64;
                                                        h = 64;
                                                        scale = scale / 2;
                                                    }

                                                    // if only dimension is defined, make sure the
                                                    // other one has the same value
                                                    w = w || h;
                                                    h = h || w;

                                                    if (w) {
                                                        width = parseInt(w) * scale;
                                                    }

                                                    if (h) {
                                                        height = parseInt(h) * scale;
                                                    }

                                                    // support for internal icons
                                                    //    (/root://icons/palette-x.png)
                                                    // x and y tell the position on the palette:
                                                    // - in pixels
                                                    // - starting from the left bottom
                                                    // We translate that to a position in the list
                                                    // and request the appropriate icon from the
                                                    // google maps website
                                                    var matches = href.match(this.regExes.kmlIconPalette);
                                                    if (matches)  {
                                                        var palette = matches[1];
                                                        var file_extension = matches[2];

                                                        var x = this.parseProperty(iconNode, "*", "x");
                                                        var y = this.parseProperty(iconNode, "*", "y");

                                                        var posX = x ? x/32 : 0;
                                                        var posY = y ? (7 - y/32) : 7;

                                                        var pos = posY * 8 + posX;
                                                        href = "http://maps.google.com/mapfiles/kml/pal"
                                                             + palette + "/icon" + pos + file_extension;
                                                    }

                                                    style["graphicOpacity"] = 1; // fully opaque
                                                    style["externalGraphic"] = href;
                                                }
                                                /*MODIFIED*/
                                                else
                                                {

                                                }

                                            }


                                            // hotSpots define the offset for an Icon
                                            var hotSpotNode = this.getElementsByTagNameNS(node_aux,
                                                                       "*",
                                                                       "hotSpot")[0];
                                            if (hotSpotNode) {
                                                var x = parseFloat(hotSpotNode.getAttribute("x"));
                                                var y = parseFloat(hotSpotNode.getAttribute("y"));

                                                var xUnits = hotSpotNode.getAttribute("xunits");
                                                if (xUnits == "pixels") {
                                                    style["graphicXOffset"] = -x * scale;
                                                }
                                                else if (xUnits == "insetPixels") {
                                                    style["graphicXOffset"] = -width + (x * scale);
                                                }
                                                else if (xUnits == "fraction") {
                                                    style["graphicXOffset"] = -width * x;
                                                }

                                                var yUnits = hotSpotNode.getAttribute("yunits");
                                                if (yUnits == "pixels") {
                                                    style["graphicYOffset"] = -height + (y * scale) + 1;
                                                }
                                                else if (yUnits == "insetPixels") {
                                                    style["graphicYOffset"] = -(y * scale) + 1;
                                                }
                                                else if (yUnits == "fraction") {
                                                    style["graphicYOffset"] =  -height * (1 - y) + 1;
                                                }
                                            }

                                            style["graphicWidth"] = width;
                                            style["graphicHeight"] = height;
                                      }
                                break;

                            case "balloonstyle":
                                var balloonStyle = OpenLayers.Util.getXmlNodeValue(
                                                        styleTypeNode[0]);
                                if (balloonStyle) {
                                    style["balloonStyle"] = balloonStyle.replace(
                                                   this.regExes.straightBracket, "${$1}");
                                }
                                break;
                            default:
                        }
                    }

                    // Some polygons have no line color, so we use the fillColor for that
                    if (!style["strokeColor"] && style["fillColor"]) {
                        style["strokeColor"] = style["fillColor"];
                    }

                    var id = node.getAttribute("id");
                    if (id && style) {
                        style.id = id;
                    }

                    return style;
                }




                return kml;

}
/*////////////////////////////////////////////////////////////////////////////////////////////////////*/
/*CAMBIOS EN LA CLASE OpenLayers/Layer/Google.js */

        function redefinirGOOGLE_stylePane(layer)
        {
//           layer.pane.style.left="50px";
           layer.pane.style.width=screen.availWidth;
           layer.pane.style.height=screen.availHeight;
           //redefinirEVENTPANE();
        }
        function redefinirGOOGLE_BackgroundColor(layer)
        {
            //$(layer.div).injectInside($('map_OpenLayers_Container'));
////           layer.mapObject.enableContinuousZoom();
////           layer.mapObject.enableDoubleClickZoom();
           layer.div.style.backgroundColor="transparent";
           //layer.mapObject.enableContinuousZoom();
//           svOverlay = new GStreetviewOverlay();
//           layer.mapObject.addOverlay(svOverlay);
           //layer.mapObject.checkResize();
           
//           layer.div.style.width="100%";

        }

/*////////////////////////////////////////////////////////////////////////////////////////////////////*/
/*CAMBIOS EN LA CLASE OpenLayers/Layer.js */

        function redefinirEVENTPANE()
        {
           OpenLayers.Util.createDiv = function(id, px, sz, imgURL, position,
                                     border, overflow, opacity) {
                //var dom = document.createElement('div');
                var dom=new Element('div');
                if (imgURL) {
                    
                    dom.setStyle('background-image','url('+imgURL+')');
                    //dom.style.backgroundImage = 'url(' + imgURL + ')';
                }
                    
                //set generic properties
                if (!id) {
                    id = OpenLayers.Util.createUniqueID("OpenLayersDiv");
                }
                if (!position) {
                    position = "absolute";
                }
                OpenLayers.Util.modifyDOMElement(dom, id, px, sz, position,
                                                 border, overflow, opacity);
                dom.style.visibility='hidden';
                dom.fade(1);
                return dom;
                
            };
        }


/*////////////////////////////////////////////////////////////////////////////////////////////////////*/
/*CAMBIOS EN LA CLASE OpenLayers/Layer/WMS.js */

        function redefinirWMS(wms,visorProjection)
        {
            wms.yx= ['EPSG:4326'];
            ////$(wms.div).injectInside($('map_OpenLayers_ViewPort'));
            //$(wms.div).injectInside($('OpenLayers.Map_4_OpenLayers_ViewPort'));

            wms.getURL= function (bounds) {
                bounds = this.adjustBounds(bounds);
               
                    if(visorProjection!=this.projection)
                    {
                       bounds=bounds.transform(new OpenLayers.Projection(visor.projection),this.projection);//MODIFIED
//                       if(this.projection.getCode()=='EPSG:4326')
//                       {
//                            bounds.left=bounds.left+0.00014;
//                            bounds.right=bounds.right+0.00014;
//                            bounds.bottom=bounds.bottom+0.000019;
//                            bounds.top=bounds.top+0.000019;
//                       }
                    }
//                    bounds.left=bounds.left-3;
//                    bounds.right=bounds.left-2;
//                    bounds.bottom=bounds.left-3;
//                    bounds.top=bounds.left-3;
                /*MODIFIED*/
                var imageSize = this.getImageSize();

                // WMS 1.3 introduced axes order
                    if (parseFloat(this.params.VERSION) >= 1.3) {
                        // currently there is no way to ask proj4js for the axes order
                        // so this is what we assume: if the projection is an EPSG code
                        // >= 4000 and < 5000 the axes order is Latitude Longitude instead
                        // of Longitude Latitude (or X Y).
                        var projection = this.map.getProjection();
                        var projArray = projection.split(":");
                        var prefix = projArray[0];
                        var code = parseInt(projArray[1]);
                        if (prefix.toUpperCase() === 'EPSG' && code >= 4000 &&
                            code < 5000) {
                                bounds = bounds.swapAxisOrder();
                        }
                    }
                var newParams = {
                    'BBOX': this.encodeBBOX ?  bounds.toBBOX() : bounds.toArray(),
                    'WIDTH': imageSize.w,
                    'HEIGHT': imageSize.h,
                    'CQL_FILTER': wms.CQL_FILTER
                };
                var requestString = this.getFullRequestString(newParams);
                return requestString;
            };

            wms.getFullRequestString=function(newParams, altUrl) {

//                var projectionCode = this.map.getProjection();
//                this.params.SRS = (projectionCode == "none") ? null : projectionCode;
                
        ////
                /*MODIFIED*/
                var projectionCode = this.projection;
                //this.params.SRS = (projectionCode == "none") ? null : projectionCode;
                var value = (projectionCode == "none") ? null : projectionCode
 		        if (parseFloat(this.params.VERSION) >= 1.3) {
 		            this.params.CRS = value;
 		        } else {
 		            this.params.SRS = value;
 		        }

                /*MODIFIED*/
                return OpenLayers.Layer.Grid.prototype.getFullRequestString.apply(this, arguments);
            }
        }

/*////////////////////////////////////////////////////////////////////////////////////////////////////*/
/*CAMBIOS EN LA CLASE OpenLayers/Control/LayerSwitcher.js */

function prepararYMostrarTodasLasCarpetas()
{
                  zonaCarpetas=$(lwControl.layersDiv.getElementsByTagName('div')[0]);
                  zonaCapas=$(lwControl.layersDiv.getElementsByTagName('div')[1]);
    //OCULTO LAS ZONAS DE LAS CARPETAS Y LAS CAPAS PARA HACER EL CAMBIO DE CONTENIDO
                  zonaCapas.setStyle('opacity',0);
                  zonaCarpetas.setStyle('opacity',0);
              //LEO NODOS SEGUN NUMNODE
                  var carpetas=capasXML.getElementsByTagName('node'+numNode);
              //LEO LOS SERVICIOS SEGUN NUMNODE
                  var servicios=capasXML.getElementsByTagName('servicio'+numNode);
              //VACIO LA ZONA DE LAS CARPETAS
                    zonaCarpetas.empty();
                    zonaCapas.empty();
                    
              //GENERO TANTAS CARPETAS COMO NODOS+NUMNODE HAY
                      for (var p = carpetas.length-1; p >= 0; p--)
                      {
                          //OBTENGO LA INFORMACION DE LA CARPETA
                              var carpeta=carpetas[p];
                                  var label=carpeta.getAttribute('label');
                                  var id=carpeta.getAttribute('id');
                          //GENERO LAS LINEAS DE LAS CARPETAS
                              if(carpetaAnterior==null)
                              {
                                   crearLineaCarpeta(carpeta);
                              }
                              else if(id.contains(carpetaAnterior))
                              {
                                   crearLineaCarpeta(carpeta);
                              }

                      }

                      //AJUSTO EL TAMAÑO Y LA POSICION DE LA ZONACARPETAS Y LA ZONACAPAS
                          //ZONA CARPETAS
                              zonaCarpetas.setStyle('height',(zonaCarpetas.getElement('div[class=CAPERTA_Class]').getHeight()+(zonaCarpetas.getElement('div[class=CAPERTA_Class]').getStyle('margin-bottom')).toInt())*obtenerNumNodosAMostrar(carpetas));
                          //ZONA CAPAS
                              zonaCapas.setStyle('top',zonaCarpetas.getHeight()+1);
      //alert(12);
              //MUESTRO LAS ZONAS DE LAS CARPETAS Y LAS CAPAS TRAS HACER EL CAMBIO DE CONTENIDO
                      zonaCarpetas.fade(1);
                      zonaCapas.fade(1);
                      
                      return servicios;
}
function desactivarActivarCapasCorrespondientes(lwControl,servicios)
{
    var is3D=false;
    //DESACTIVO TODAS LAS CAPAS
            var layers = lwControl.map.layers.slice();
                    if (!lwControl.ascending) { layers.reverse(); }
                    for(var h=0, len=layers.length; h<len; h++)
                    {
                        
                          layers[h].displayInLayerSwitcher=false;
                       
                    }
         //LAS CAPAS QUE CORRESPONDAN
              for (var p = 0; p < servicios.length; p++)
                    {
                            
                            if(servicios[p].getAttribute('id').contains(carpetaAnterior))
                            { 
                                if(servicios[p].getAttribute('name')!='GOOGLE-3D')
                                {
                                    var layer=map.getLayer(OpenLayers.i18n(servicios[p].getAttribute('name')));
                                    layer.displayInLayerSwitcher=true;
                                }
                                else
                                {
                                    is3D=true;
                                }
                            }
                        
                    }
     return is3D;
}
function addBotonesVistas3d(lineaLayer,icono)
{
    /*GENERO LOS BOTONES PARA VER EL 3D EN VARIOS TAMAÑOS*/
                              var verGrande=new Element('div',{
                                 'id':'verGrande3D',
                                 'class':'verGrande3D',
                                 'title':'Ver Google Earth a pantalla completa',
                                 'events':{
                                     'click':function(){
                                         if(is3dactivo)
                                         {
                                             ajustarMapas();
                                             tipoMapa='Full';
                                         }
                                         else
                                         {
                                             if(icono.getProperty('class')=='iconoLineaCapa_Class_OFF')
                                              {
                                                  mostrarGoogleEarth('Full');
                                                  //inputElem.setProperty('checked','true');
                                                    //icono.toggleClass('superCheck');
                                                    icono.setProperty('class','iconoLineaCapa_Class_ON');
                                              }
                                              else
                                              {
                                                  ocultarGoogleEarth();
                                                  //inputElem.removeProperty('checked');
                                                   icono.setProperty('class','iconoLineaCapa_Class_OFF');
                                              }
                                         }
                                     }
                                 }
                              }).injectInside(lineaLayer);
                              var verMini=new Element('div',{
                                 'id':'verMini3D',
                                 'class':'verMini3D',
                                 'title':'Ver 2D y 3D simultáneamente',
                                 'events':{
                                     'click':function(){
                                         if(is3dactivo)
                                         {
                                             ajustarMapasMini();
                                             tipoMapa='Mini';
                                         }
                                         else
                                         {
                                             if(icono.getProperty('class')=='iconoLineaCapa_Class_OFF')
                                              {

                                                  mostrarGoogleEarth('Mini');
                                                    icono.setProperty('class','iconoLineaCapa_Class_ON');
                                              }
                                              else
                                              {
                                                  ocultarGoogleEarth();
                                                   icono.setProperty('class','iconoLineaCapa_Class_OFF');
                                              }
                                         }
                                     }
                                 }
                              }).injectInside(lineaLayer);
                              var verStreetView=new Element('div',{
                                 'id':'verStreetView3D',
                                 'class':'verStreetView3D',
                                 'title':'Ver 2D, 3D y StreetView simultáneamente',
                                 'events':{
                                     'click':function(){
                                         if(is3dactivo)
                                         {
//                                             if(tipoMapa=='StreetView')
//                                             {
                                                ajustarMapasStreetView();
                                                panoClient.getNearestPanorama((map.baseLayer).mapObject.getCenter(), showPanoData);
                                                tipoMapa='StreetView';
//                                             }
                                         }
                                         else
                                         {
                                              if(icono.getProperty('class')=='iconoLineaCapa_Class_OFF')
                                              {

                                                  mostrarGoogleEarth('StreetView');
                                                    icono.setProperty('class','iconoLineaCapa_Class_ON');
                                              }
                                              else
                                              {
                                                  ocultarGoogleEarth();
                                                   icono.setProperty('class','iconoLineaCapa_Class_OFF');
                                              }
                                         }
                                     }
                                 }
                              }).injectInside(lineaLayer);
}
function redefinirLW()
{


    lwControl.redraw= function() {
        if(Browser.Engine.trident)
        {
            zonaCarpetas=$(this.layersDiv.getElementsByTagName('div')[0]);
            zonaCapas=$(this.layersDiv.getElementsByTagName('div')[1]);
        }
        var arraySliders=new Array();
        var arrayIconos=new Array();
        /*PREPARO EL ESPACIO PARA LAS CARPETAS Y AÑADO LAS CARPETAS****/
              var servicios=prepararYMostrarTodasLasCarpetas();
        /**************************************************************/
        /*ACTIVO Y DESACTIVO LAS CAPAS*********************************/
              var is3d=desactivarActivarCapasCorrespondientes(this,servicios);
        /**************************************************************/
        /*RECORRO CADA CAPA Y LA MUESTRO SEGUN SU VISIBIIDAD***********/
              var layers = this.map.layers.slice();
              if (!this.ascending) { layers.reverse(); }
              //var temp3d=true;
              
              for(var i=0, len=layers.length; i<len; i++) 
              {
                  var layer = layers[i];
                  var baseLayer = layer.isBaseLayer; 
                  
                  //GENERO UNA NUEVA LINEA
                      if(is3d)
                      {
                          
                          var lineaLayer=new Element('div',{
                                      'id':'LINEA_LAYER_'+OpenLayers.i18n('GOOGLE-3D'),
                                      'class':'LINEA_LAYER',
                                      'styles':{
                                          'font-size':($(window.parent.document.getElementById('appPIE_POSICION')).getStyle('font-size')).toInt()-2+'px'
                                      }
                                }).injectInside(zonaCapas);
                               
                                //GENERO EL BOTON DE ACTIVACION
                                    //ICONO
                                      var icono=new Element('div',{
                                          'id':'iconoLineaCapa_'+OpenLayers.i18n('GOOGLE-3D'),
                                          'class':'iconoLineaCapa_Class_OFF'
                                      }).injectInside(lineaLayer);
                                      icono.store('idLayer',OpenLayers.i18n('GOOGLE-3D'));
                                      if(is3dactivo){icono.setProperty('class','iconoLineaCapa_Class_ON')}

                                    //LABEL
                                      new Element('div',{
                                          'id':'labelLineaCapa_'+OpenLayers.i18n('GOOGLE-3D'),
                                          'class':'labelLineaCapa_Class',
                                          'html':OpenLayers.i18n('GOOGLE-3D')
                                      }).injectInside(lineaLayer);
                                      arrayIconos.include(icono); 
                                    //SLIDER
                                              var sliderCapa=new Element('div',{
                                                 'id':'sliderCapa_'+layer.id,
                                                 'class':'sliderCapa_Class'
                                              }).injectInside(lineaLayer);
                                                  sliderCapa.store('idLayer',OpenLayers.i18n('GOOGLE-3D'));
                                                  sliderCapa.store('is3d',true);
                                                  var knobCapa=new Element('div',{
                                                     'id':'knobSliderCapa_'+OpenLayers.i18n('GOOGLE-3D'),
                                                     'class':'knobSliderCapa_Class'
                                                  }).injectInside(sliderCapa);

                                                  var updCapa=new Element('div',{
                                                     'id':'updSliderCapa_'+OpenLayers.i18n('GOOGLE-3D'),
                                                     'class':'updSliderCapa_Class',
                                                     'html':''+0.6*100
                                                  }).injectInside(sliderCapa);
                                                  knobCapa.setStyle('left',(updCapa.get('html')).toInt());

                                              arraySliders.include(sliderCapa);      
                                 addBotonesVistas3d(lineaLayer,icono);
                                 is3d=false;
                      }
                      else
                      {
                          if(layer.displayInLayerSwitcher)
                          {


                                  var lineaLayer=new Element('div',{
                                              'id':'LINEA_LAYER_'+OpenLayers.i18n(layer.id),
                                              'class':'LINEA_LAYER',
                                              'styles':{
                                                  'font-size':($(window.parent.document.getElementById('appPIE_POSICION')).getStyle('font-size')).toInt()-2+'px'
                                              }
                                        }).injectInside(zonaCapas);
                                        
                                        //GENERO EL BOTON DE ACTIVACION
                                            //ICONO
                                              var icono=new Element('div',{
                                                  'id':'iconoLineaCapa_'+layer.id,
                                                  'class':'iconoLineaCapa_Class_OFF'
                                              }).injectInside(lineaLayer);
                                              icono.store('idLayer',layer.id);
                                              if(layer.visibility){icono.setProperty('class','iconoLineaCapa_Class_ON')}

                                            //LABEL
                                              new Element('div',{
                                                  'id':'labelLineaCapa_'+layer.id,
                                                  'class':'labelLineaCapa_Class',
                                                  'html':OpenLayers.i18n(layer.id)
                                              }).injectInside(lineaLayer);
                                            //SLIDER
                                              var sliderCapa=new Element('div',{
                                                 'id':'sliderCapa_'+layer.id,
                                                 'class':'sliderCapa_Class'
                                              }).injectInside(lineaLayer);
                                                  sliderCapa.store('idLayer',layer.id);
                                                  var knobCapa=new Element('div',{
                                                     'id':'knobSliderCapa_'+layer.id,
                                                     'class':'knobSliderCapa_Class'
                                                  }).injectInside(sliderCapa);

                                                  var updCapa=new Element('div',{
                                                     'id':'updSliderCapa_'+layer.id,
                                                     'class':'updSliderCapa_Class',
                                                     'html':''+(layer.opacity)*100
                                                  }).injectInside(sliderCapa);
                                                  knobCapa.setStyle('left',(updCapa.get('html')).toInt());

                                              arraySliders.include(sliderCapa);    
                                              arrayIconos.include(icono);    
                                              
                                              if(layer.id!=OpenLayers.i18n('VIRTUAL_EARTH'))
                                              {
                                                  if(layer.mapObject!=undefined)
                                                  {
                                                      $('map').setStyles({
                                                         'height':'100%',
                                                         'width':'100%'
                                                      });

                                                      layer.mapObject.checkResize();
                                                      layer.redraw();
                                                  }
                                              }




                          }
                      }
                  
              }
              
              //AÑADO LOS SLIDERS
                  arraySliders.each(function(sliderCapa){
                             
                              new Drag.Move(sliderCapa.getElement('div[class=knobSliderCapa_Class]'),{
                                  container:sliderCapa,
                                  //grid:paso,
                                  onDrag:function(){
//                                      if(sliderCapa.retrieve('is3d'))
//                                      {
//                                          var kmlCalif=ge.getFeatures().getChildNodes().item(0);
//                                              kmlCalif.getNextSibling();
//                                      }
//                                      else
//                                      {
                                          var pos=(sliderCapa.getElement('div[class=knobSliderCapa_Class]').getStyle('left')).toInt();
                                          map.getLayer(sliderCapa.retrieve('idLayer')).setOpacity(pos/100);
                                          sliderCapa.getElement('div[class=updSliderCapa_Class]').set('html',''+pos);
//                                      }
                                  }
                              });
                              
//                              map.getLayer(sliderCapa.retrieve('idLayer')).setOpacity(44/100);
//                              sliderCapa.getElement('div[class=updSliderCapa_Class]').set('html',44/100);
                  });
                  arrayIconos.each(function(icono){
                      icono.addEvents({
                          'click':function(){
                              if(icono.getProperty('id')=='iconoLineaCapa_'+OpenLayers.i18n('GOOGLE-3D'))
                              {
                                      if(is3dactivo)
                                      {
                                          icono.setProperty('class','iconoLineaCapa_Class_OFF');
                                          ocultarGoogleEarth();
                                      }
                                      else
                                      {
                                          icono.setProperty('class','iconoLineaCapa_Class_ON');
                                          mostrarGoogleEarth('StreetView');
                                      }
                              }
                              else
                              {
                                  var l=map.getLayer(icono.retrieve('idLayer'));
                                      l.setVisibility(!l.getVisibility());
                                      if(l.getVisibility())
                                      {
                                          icono.setProperty('class','iconoLineaCapa_Class_ON');
                                      }
                                      else
                                      {
                                          icono.setProperty('class','iconoLineaCapa_Class_OFF');
                                      }
                              }
                          }
                      })
                  });
        /**************************************************************/
        
    };
    
    

}
function desactivarDisplayLS(lw)
    {
        var layers = lw.map.layers.slice();
                    if (!lw.ascending) { layers.reverse(); }
                    for(var h=0, len=layers.length; h<len; h++)
                    {
                        
                          layers[h].displayInLayerSwitcher=false;
                       
                    }
    }
        function generarSlider(zona,id,i)
            {
               var slider=document.createElement('div');
                    slider.id='SLIDER_'+id;
                    slider.className='slider';
                    slider.tabIndex=i;
                    var tirador=document.createElement('input');
                        tirador.id='SLIDER_INPUT_'+id;
                        tirador.className='slider-input';
                    var dato=document.createElement('input');
                        dato.id='SLIDER_VALUE_'+id;
                        dato.className='sliderValue';
                        
                        
                        slider.appendChild(tirador);
                        zona.appendChild(slider);
                        zona.appendChild(dato);
            }


function ajustarEstiloLayerSwitcher()
{
    lwControl.loadContents=function() {


        //window.parent.insertarDivLWControlEnVentana(this);
        this.layersDiv = document.createElement("div");
        this.layersDiv.id = this.id + "_layersDiv";
        this.layersDiv.className='lwControlZonaLayers_Class';
            zonaCarpetas=new Element('div',{
                    'id':'zonaCarpetas_ID',
                    'class':'zonaCarpetas'
            }).injectInside(this.layersDiv);
            zonaCapas= new Element('div',{
                    'id':'zonaCapas_ID',
                    'class':'zonaCapas'
            }).injectInside(this.layersDiv);
            
            if(Browser.Engine.trident)
            {
                
                //this.div.appendChild(this.layersDiv);
                //window.parent.insertarDivLWControlEnVentana(this.div,this.layersDiv);
            }
            else
            {
                
                $(this.div).adopt($(this.layersDiv));
                $(this.div).adopt(zonaCarpetas);
                $(this.div).adopt(zonaCapas);
            }
            
          
    }
    
}

/*////////////////////////////////////////////////////////////////////////////////////////////////////*/
/*CAMBIOS EN LA CLASE OpenLayers/Control/MouseDefaults.js */

        function redefinirMouseDefaultControl(control)
        {
            
            control.oncontextmenu=function(){alert('pepe')};
            
            control.onWheelEvent=function(e){

                // first determine whether or not the wheeling was inside the map
                var inMap = false;
                var elem = OpenLayers.Event.element(e);
                while(elem != null) {
                    if (this.map && elem == this.map.div) {
                        inMap = true;
                        break;
                    }
                    elem = elem.parentNode;
                }

                if (inMap) {

                    var delta = 0;
                    if (!e) {
                        e = window.event;
                    }
                    if (e.wheelDelta) {
                        delta = e.wheelDelta/120; 
                        if (window.opera && window.opera.version() < 9.2) {
                            delta = -delta;
                        }
                    } else if (e.detail) {
                        delta = -e.detail / 3;
                    }
                    if (delta) {
                        // add the mouse position to the event because mozilla has a bug
                        // with clientX and clientY (see https://bugzilla.mozilla.org/show_bug.cgi?id=352179)
                        // getLonLatFromViewPortPx(e) returns wrong values
                        //e.xy = this.mousePosition;
                        e.xy = map.getCenter();

                        if (delta < 0) {
                           this.defaultWheelDown(e);
                        } else {
                           this.defaultWheelUp(e);
                        }
                    }

                    //only wheel the map, not the window
                    OpenLayers.Event.stop(e);
                }
            }
            
        }
        
/*////////////////////////////////////////////////////////////////////////////////////////////////////*/
/*CAMBIOS EN LA CLASE Label.js */

        function redefinirMarkerLabel(markerLabel,markerClass)
        {
            
            var div=markerLabel.div;
            if(div)
            {
            //GENERO EL ELEMENTO QUE ENLAZA EL ICONO CON EL LABEL
                var enlaceML = OpenLayers.Util.createDiv("enlaceMarkerLabel", null, null);
                    enlaceML.className=markerClass;
                    div.appendChild(enlaceML);
            }
        }
        
/*//////////////////////////////////////////////////////////////////////////////////////////////////////*/
/*CAMBIOS EN MODIFY FEATURE*/
        function redefinirModifyFeature(control)
        {
            control.collectRadiusHandle= function() {
                var geometry = this.feature.geometry;
                var bounds = geometry.getBounds();
                var center = bounds.getCenterLonLat();
                var originGeometry = new OpenLayers.Geometry.Point(
                    center.lon, center.lat
                );
                var radiusGeometry = new OpenLayers.Geometry.Point(
                    bounds.right, bounds.bottom
                );
                     //GENERO EL RADIUSHANDLE 
                                            var style_mark = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
                                                style_mark.fillOpacity = 0.9;
                                                style_mark.graphicWidth = 84;
                                                style_mark.graphicHeight = 97;
                                                style_mark.graphicXOffset = -(style_mark.graphicWidth/2);  // this is the default value
                                                style_mark.graphicYOffset = -style_mark.graphicHeight;
                                                style_mark.externalGraphic = "styles/images/tiradorFeature.png";
                                                style_mark.graphicTitle = "this is a test tooltip";
  

        //                                    var rh = new OpenLayers.Feature.Vector("RadiusHandle", {style: style_mark})

                var radius = new OpenLayers.Feature.Vector(radiusGeometry, null,style_mark);

//                var radius = new OpenLayers.Feature.Vector(radiusGeometry);
                var resize = (this.mode & OpenLayers.Control.ModifyFeature.RESIZE);
                var rotate = (this.mode & OpenLayers.Control.ModifyFeature.ROTATE);
                
                if(rotate && !resize)
                {
                    style_mark.externalGraphic = "styles/images/tiradorFeatureROTATE.png";
                }
                else if(!rotate && resize)
                {
                    style_mark.externalGraphic = "styles/images/tiradorFeatureESCALE.png";
                }
                
                radiusGeometry.move = function(x, y) {
                    OpenLayers.Geometry.Point.prototype.move.call(this, x, y);
                    var dx1 = this.x - originGeometry.x;
                    var dy1 = this.y - originGeometry.y;
                    var dx0 = dx1 - x;
                    var dy0 = dy1 - y;
                    if(rotate) {
                        
                        var a0 = Math.atan2(dy0, dx0);
                        var a1 = Math.atan2(dy1, dx1);
                        var angle = a1 - a0;
                        angle *= 180 / Math.PI;
                        geometry.rotate(angle, originGeometry);
                    }
                    if(resize) {
                        var l0 = Math.sqrt((dx0 * dx0) + (dy0 * dy0));
                        var l1 = Math.sqrt((dx1 * dx1) + (dy1 * dy1));
                        geometry.resize(l1 / l0, originGeometry);
                    }
                    
                };
                this.radiusHandle = radius;
                this.layer.addFeatures([this.radiusHandle], {silent: true});
            };
        }
/*//////////////////////////////////////////////////////////////////////////////////////////////////////*/
/*CAMBIOS EN MODIFY FEATURE*/
        function redefinirZoombox(control)
        {
            control.zoomBox=function (position) {
                  if (position instanceof OpenLayers.Bounds) {
                      if (!this.out) {
                          var minXY = this.map.getLonLatFromPixel(
                                      new OpenLayers.Pixel(position.left, position.bottom));
                          var maxXY = this.map.getLonLatFromPixel(
                                      new OpenLayers.Pixel(position.right, position.top));
                          var bounds = new OpenLayers.Bounds(minXY.lon, minXY.lat,
                                                         maxXY.lon, maxXY.lat);
                      } else {
                          var pixWidth = Math.abs(position.right-position.left);
                          var pixHeight = Math.abs(position.top-position.bottom);
                          var zoomFactor = Math.min((this.map.size.h / pixHeight),
                              (this.map.size.w / pixWidth));
                          var extent = this.map.getExtent();
                          var center = this.map.getLonLatFromPixel(
                              position.getCenterPixel());
                          var xmin = center.lon - (extent.getWidth()/2)*zoomFactor;
                          var xmax = center.lon + (extent.getWidth()/2)*zoomFactor;
                          var ymin = center.lat - (extent.getHeight()/2)*zoomFactor;
                          var ymax = center.lat + (extent.getHeight()/2)*zoomFactor;
                          var bounds = new OpenLayers.Bounds(xmin, ymin, xmax, ymax);
                      }
                      this.map.zoomToExtent(bounds);
                  } else { // it's a pixel
                      if (!this.out) {
                          this.map.setCenter(this.map.getLonLatFromPixel(position),
                                         this.map.getZoom() + 1);
                      } else {
                          this.map.setCenter(this.map.getLonLatFromPixel(position),
                                         this.map.getZoom() - 1);
                      }
                  }
                  controlActivo=control;
                  asignarCursorAMapa();
              };
        }

        function redefinirBounds(bounds)
        {
            bounds.swapAxisOrder=function() {
                return new OpenLayers.Bounds(this.bottom, this.left, this.top, this.right);
            }

            bounds.toArray=function(reverseAxisOrder){
                if (reverseAxisOrder === true) {
                    return [this.bottom, this.left, this.top, this.right];
                } else {
                    return [this.left, this.bottom, this.right, this.top];
                }
            }

            bounds.toBBOX=function(decimal, reverseAxisOrder) {
                if (decimal== null) {
                    decimal = 6;
                }
                var mult = Math.pow(10, decimal);
//                var bbox = Math.round(this.left * mult) / mult + "," +
//                           Math.round(this.bottom * mult) / mult + "," +
//                           Math.round(this.right * mult) / mult + "," +
//                           Math.round(this.top * mult) / mult;
//
//                return bbox;
                var xmin = Math.round(this.left * mult) / mult;
                var ymin = Math.round(this.bottom * mult) / mult;
                var xmax = Math.round(this.right * mult) / mult;
                var ymax = Math.round(this.top * mult) / mult;
                if (reverseAxisOrder === true) {
                    return ymin + "," + xmin + "," + ymax + "," + xmax;
                } else {
                    return xmin + "," + ymin + "," + xmax + "," + ymax;
                }
            }
        }