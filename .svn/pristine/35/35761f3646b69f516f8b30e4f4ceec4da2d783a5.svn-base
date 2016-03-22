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
/*CAMBIOS EN LA CLASE OpenLayers/Layer/WMS.js */
function redefinirWMS(wms,mapa)
{
    wms.yx= ['EPSG:4326'];
    ////$(wms.div).injectInside($('map_OpenLayers_ViewPort'));
    //$(wms.div).injectInside($('OpenLayers.Map_4_OpenLayers_ViewPort'));

    wms.getURL= function (bounds) {
        bounds = this.adjustBounds(bounds);
               
        if(this.map.getProjectionObject().projCode!=this.projection.projCode)
        {
            bounds=bounds.transform(this.map.getProjectionObject(),this.projection);//MODIFIED
        }
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
        this.layer.addFeatures([this.radiusHandle], {
            silent: true
        });
    };
}
