/*
    Document   : JobPrinter
    Author     : Alejandro.Centeno
 */

var JobPrinter = new Class({
    Implements: Events,
    page: null,
    control: null,
    printProvider: null,
    anexos: null,
    initialize: function () {

        var printConfig = JSON.decode(jsIO.getService({
            'wsName': 'CROSS-DOMAIN',
            'url': visor.rutaPrintModule + '/info.json'
        }));

        this.printProvider = printConfig;

        var jwPrint = new JobWindow('Configuracion_de_impresion', false, 521, 360, 300, 200, true);
        jwPrint.loadAJAX('impresion.jsp', function (response) {
            this.ventana.loadZonaActiva(response);
        } .bind({
            cab: this,
            ventana: jwPrint
        }));
        jwPrint.addEvent('close', function () {
            this.removePage(this.page);
            this.fireEvent("close");
        } .bind(this)
            );


        for (i = 0; i < printConfig.scales.length; i++) {
            new Element('option', {
                //'id':'escalaPrint_'+printConfig.scales[i].value,
                'value': printConfig.scales[i].value,
                'html': printConfig.scales[i].name
            }).injectInside($('select_escala'));
        }

        for (i = 0; i < printConfig.dpis.length; i++) {
            new Element('option', {
                //'id':'resolucionPrint_'+printConfig.dpis[i].value,
                'value': printConfig.dpis[i].value,
                'html': printConfig.dpis[i].name + ' dpi'
            }).injectInside($('select_resolucion'));
        }

        for (i = 0; i < printConfig.outputFormats.length; i++) {
            new Element('option', {
                //'id':'resolucionPrint_'+printConfig.outputFormats[i].value,
                'value': printConfig.outputFormats[i].name,
                'html': printConfig.outputFormats[i].name
            }).injectInside($('select_formato'));
        }

        for (i = 0; i < printConfig.layouts.length; i++) {
            new Element('option', {
                //'id':'resolucionPrint_'+printConfig.layouts[i].value,
                'value': printConfig.layouts[i].name,
                'html': printConfig.layouts[i].name
            }).injectInside($('select_hoja'));
        }
        this.LayerPreview = this.creaLayerPreview();
        this.createControl(this.LayerPreview);


        var pagina = new PagePrinter(printConfig);

        $('text_filas').addEvent('keyup', function () {
            this.refreshAnexos($('text_filas').value,$('text_columnas').value);
        }.bind(this));  
        $('text_filas').addEvent('keypress', function (evt) {
            this.validateNumero(evt);
        } .bind(this));

        $('text_columnas').addEvent('keyup', function () {
            this.refreshAnexos($('text_filas').value,$('text_columnas').value);
        }.bind(this));
        $('text_columnas').addEvent('keypress', function (evt) {
            this.validateNumero(evt)
        }.bind(this));
            
        $('select_escala').addEvent('change', function () {
            this.page.setScale($('select_escala').value, visor.map.getUnits());
            this.job.refreshAnexos($('text_filas').value, $('text_columnas').value);
        } .bind({
            page: pagina,
            job: this
        })
        );

        $('btnEnviarImpresion').addEvent('click', function () {
            if (confirm(jsIO.cargarTextoSegunIdioma('La impresion puede tardar varios minutos. Se mostrara un aviso cuando este lista. Continuar?'))) {
                this.sendPeticion();
            }
        } .bind(this)
            );

        $('select_hoja').addEvent('change', function () {
            this.config.each(function (lout) {
                if (lout.name == $('select_hoja').value)
                    this.page.setLayout(lout);
            } .bind(this));
            this.job.refreshAnexos($('text_filas').value, $('text_columnas').value);
        } .bind({
            page: pagina,
            config: printConfig.layouts,
            job: this
        })
        );

        this.addPage(pagina, this.LayerPreview);
        this.show(this.LayerPreview);
    },
    sendPeticion: function () {
        var peticion = new Object();
        peticion.layout = $('select_hoja').value;
        peticion.srs = visor.projection;
        peticion.title = $('text_cabecera').value;
        peticion.units = visor.map.getUnits();
        peticion.geodetic = false;// visor.projectionConsultaServicios == "EPSG:4326";
        peticion.outputFormat = $('select_formato').value;
        if ($('text_cabecera').value != null && $('text_cabecera').value != "") {
            peticion.outputFilename = $('text_cabecera').value;
        }
        var capas = new Array();
        for (var h = 0; h < visor.map.layers.length; h++) {
            var capa = null
            var layer = visor.map.layers[h];
            if (layer !== this.LayerPreview && layer.visibility){
                switch (visor.map.layers[h].CLASS_NAME) {
                    case 'OpenLayers.Layer.Google':
                        /*capa = new Object();
                        capa.language=visor.Lang;
                        capa.type = 'TiledGoogle';
                        capa.baseURL = 'http://maps.google.com/maps/api/staticmap';
                        capa.format = 'png';
                        capa.extension = 'png';
                        capa.sensor= 'false';
                        capa.tileSize=[256,256];
                        capa.maxExtent= [layer.maxExtent.left,layer.maxExtent.bottom,layer.maxExtent.right,layer.maxExtent.top];
                        capa.opacity = layer.opacity;
                        capa.singleTile = false;
                        capa.maptype=layer.type;
                        capa.resolutions= layer.resolutions;*/
                        break;
                    case 'OpenLayers.Layer.WMS':
                        capa = new Object();
                        capa.type = "WMS";
                        capa.baseURL = layer.url.replace("gwc/service/",""); //La impresión nunca funcionará con gwc
                        capa.layers = layer.params.LAYERS.split(",");
                        capa.format = layer.params.FORMAT;
                        capa.opacity = layer.opacity;
                        capa.singleTile = false;
                        capa.customParams={
                            'PROJECTION': visor.projectionConsultaServicios,
                            'TRANSPARENT': layer.params.TRANSPARENT
                        };
                        break;
                    case 'OpenLayers.Layer.Vector':
                        if (!layer.features.length) {
                            break;
                        }
                        var encFeatures = [];
                        var encStyles = {};
                        var features = layer.features;
                        var featureFormat = new OpenLayers.Format.GeoJSON();
                        var styleFormat = new OpenLayers.Format.JSON();
                        var nextId = 1;
                        var styleDict = {};
                        var feature, style, dictKey, dictItem, styleName;
                        for (var i = 0, len = features.length; i < len; ++i) {
                            feature = features[i];
                            style = feature.style || layer.style ||
                            layer.styleMap.createSymbolizer(feature,feature.renderIntent);
                            dictKey = styleFormat.write(style);
                            dictItem = styleDict[dictKey];
                            if (dictItem) {
                                //this style is already known
                                styleName = dictItem;
                            } else {
                                //new style
                                styleDict[dictKey] = styleName = nextId++;
                                if (style.externalGraphic) {
                                    encStyles[styleName] = Ext.applyIf({
                                        externalGraphic: this.getAbsoluteUrl(
                                        style.externalGraphic)
                                    }, style);
                                } else {
                                    encStyles[styleName] = style;
                                }
                            }
                            var featureGeoJson = featureFormat.extract.feature.call(
                            featureFormat, feature);

                            featureGeoJson.properties = OpenLayers.Util.extend({
                                _gx_style: styleName
                            }, featureGeoJson.properties);

                            encFeatures.push(featureGeoJson);
                        }
                        capa =  {
                            type: 'Vector',
                            styles: encStyles,
                            styleProperty: '_gx_style',
                            geoJson: {
                                type: "FeatureCollection",
                                features: encFeatures
                            },
                            name: layer.name,
                            opacity: (layer.opacity != null) ? layer.opacity : 1.0
                        };
                        break;
                }
            }
            if (capa && layer.getVisibility()){
                capas.push(capa);
            }
        }
        //Marcos:
        capas.push(this.getCapaMarco());
                
        peticion.layers = capas;

        var pages = new Array();
        var page = new Object();
        var centro=null;
//        if (visor.projectionConsultaServicios == "EPSG:4326"){
//            centro=this.page.feature.geometry.getCentroid().transform(visor.map.getProjectionObject(),new OpenLayers.Projection("EPSG:4326"));
//        }else{
            centro=this.page.feature.geometry.getCentroid();
//        }
        page.center=new Array(centro.x,centro.y);
        page.scale = $('select_escala').value;
        page.comment = $('text_pie').value
        page.mapTitle = $('text_cabecera').value
        page.dpi = $('select_resolucion').value;
        page.geodetic =false;// visor.projectionConsultaServicios == "EPSG:4326";
        page.rotation = 0;
        pages.push(page);

        this.anexos.each(function (hoja) {
            var page = new Object();
            var centroAnexo=null;
//            if (visor.projectionConsultaServicios == "EPSG:4326"){
//                centroAnexo=hoja.geometry.getCentroid().transform(visor.map.getProjectionObject(),new OpenLayers.Projection("EPSG:4326"));
//            }else{
                centroAnexo=hoja.geometry.getCentroid();
//            }
            page.center = new Array(centroAnexo.x, centroAnexo.y);
            page.scale = $('select_escala').value;
            page.comment = $('text_pie').value
            page.mapTitle = $('text_cabecera').value
            page.dpi = $('select_resolucion').value;
            page.geodetic = false;// visor.projectionConsultaServicios == "EPSG:4326";
            page.rotation = 0;
            this.push(page);
        }, pages);

        peticion.pages = pages;

        this.createPDF(peticion);
        //        var response=jsIO.getService({
        //            'wsName': 'CROSS-DOMAIN',
        //            'url': this.printProvider.createURL,
        //            'spec':JSON.stringify(peticion)
        //        })

        //        var json=new OpenLayers.Format.JSON();
        //        var res=json.read(response)
        //        if (res && res.getURL){
        //            window.open(res.getURL, '_blank');
        //        }


        //        relocate(this.printProvider.createURL,{
        //            'spec':JSON.stringify(peticion)
        //        });

    },
    getCapaMarco:function(){
        var encFeatures = [];
        var encStyles = {};
        var features=new Array()
        this.anexos.each(function(anex){
            features.push (anex);
        });
        features.push(this.page.feature);
        var featureFormat = new OpenLayers.Format.GeoJSON();
        var styleFormat = new OpenLayers.Format.JSON();
        var nextId = 1;
        var styleDict = {};
        var feature, style, dictKey, dictItem, styleName;
        for (var i = 0, len = features.length; i < len; ++i) {
            feature = features[i];
            style = {
                            strokeWidth: 4,
                            strokeOpacity: 1,
                            strokeColor: '#000000',
                            strokeDashstyle: 'solid',
                            fillOpacity: 0
                        };
            dictKey = styleFormat.write(style);
            dictItem = styleDict[dictKey];
            if (dictItem) {
                //this style is already known
                styleName = dictItem;
            } else {
                //new style
                styleDict[dictKey] = styleName = nextId++;
                if (style.externalGraphic) {
                    encStyles[styleName] = Ext.applyIf({
                        externalGraphic: this.getAbsoluteUrl(
                        style.externalGraphic)
                    }, style);
                } else {
                    encStyles[styleName] = style;
                }
            }
            var featureGeoJson = featureFormat.extract.feature.call(
            featureFormat, feature);

            featureGeoJson.properties = OpenLayers.Util.extend({
                _gx_style: styleName
            }, featureGeoJson.properties);

            encFeatures.push(featureGeoJson);
        }
        var capa =  {
            type: 'Vector',
            styles: encStyles,
            styleProperty: '_gx_style',
            geoJson: {
                type: "FeatureCollection",
                features: encFeatures
            },
            name: 'capaMarco',
            opacity: 1.0
        };
        return capa;
    },
    createPDF: function (spec) {
        var specTxt = new OpenLayers.Format.JSON().write(spec);
        OpenLayers.Console.info(specTxt);

        try {
            //The charset seems always to be UTF-8, regardless of the page's
            var charset = "ISO-8859-1"; //UTF-8";  /*+document.characterSet*/
            OpenLayers.ProxyHost = "ActionServlet?wsName=CROSS-DOMAIN&url=";
            /* var params = OpenLayers.Util.extend({
            url: this.printProvider.createURL
            }, this.params);*/

            OpenLayers.Request.POST({
                url: this.printProvider.createURL,
                data: specTxt,
                //params: params,
                headers: {
                    'CONTENT-TYPE': "application/json; charset=" + charset
                },
                callback: function (request) {
                    if (request.status >= 200 && request.status < 300) {
                        var json = new OpenLayers.Format.JSON();
                        var answer = json.read(request.responseText);
                        if (answer && answer.getURL) {
                            if (confirm(jsIO.cargarTextoSegunIdioma('La impresion solicitada ha finalizado. Descargar?'))) {
                                //window.open(answer.getURL, '_blank');
                                //window.open(answer.getURL);
                                window.location.href = answer.getURL;
                            }
                        } else {
                            //Intentar otro metodo
                            //this.peticionGET(spec);
                        }
                    } else {
                        //Intentar otro metodo
                        //this.peticionGET(spec);
                    }
                } .bind(this)
            });
        } catch (err) {
            //Intentar otro metodo
            //this.peticionGET(spec);
        }
    },
    peticionGET: function (spec) {
        var json = new OpenLayers.Format.JSON();
        var result = this.printProvider.printURL + "?spec=" + json.write(spec);
        window.location.href = result;
    },
    refreshAnexos: function (filas, columnas) {
        this.page.feature.layer.removeFeatures(this.anexos);
        delete this.anexos;
        this.anexos = new Array();

        for (var iFila = 0; iFila < filas; iFila++) {
            var yAnexo = this.page.feature.geometry.getCentroid().y - (this.page.feature.geometry.getBounds().getHeight() * (iFila));
            if (iFila != 0) {
                var newAnexoFila = this.page.feature.clone();
                this.page.feature.layer.addFeatures([newAnexoFila]);
                newAnexoFila.move(new OpenLayers.LonLat(newAnexoFila.geometry.getCentroid().x, yAnexo));
                this.anexos.push(newAnexoFila);
            }
            for (var iCol = 0; iCol < columnas; iCol++) {
                if (iCol != 0) {
                    var newAnexoCol = this.page.feature.clone();
                    this.page.feature.layer.addFeatures([newAnexoCol]);
                    newAnexoCol.move(new OpenLayers.LonLat(newAnexoCol.geometry.getCentroid().x + (newAnexoCol.geometry.getBounds().getWidth() * (iCol)), yAnexo));
                    this.anexos.push(newAnexoCol);
                }
            }
        }
    },
    validateNumero: function (evt) {
        var key = evt.event.keyCode || evt.event.which;
        key = String.fromCharCode(key);
        var regex = /[0-9]/;
        if (!regex.test(key)) {
            evt.returnValue = false;
            if (evt.preventDefault) evt.preventDefault();
            return false;
        }
        return true;
    },
    creaLayerPreview: function () {
        var extentLayer = new OpenLayers.Layer.Vector("print", {
            displayInLayerSwitcher: false,
            styleMap: new OpenLayers.StyleMap(new OpenLayers.Style({
                pointRadius: 4,
                graphicName: "square",
                rotation: "${getRotation}",
                strokeColor: "${getStrokeColor}",
                fillOpacity: "${getFillOpacity}",
                fillColor: "#FFFFFF",
                cursor: "move"
            }, {
                context: {
                    //                    getRotation: function(feature) {
                    //                        return printForm.printPage.rotation;
                    //                    },
                    getStrokeColor: function (feature) {
                        return feature.geometry.CLASS_NAME == "OpenLayers.Geometry.Point" ?
                        "#000" : "#FFFFFF";
                    },
                    getFillOpacity: function (feature) {
                        return feature.geometry.CLASS_NAME == "OpenLayers.Geometry.Point" ?
                        0 : 0.4;
                    }
                }
            })
            )

            /* styleMap: new OpenLayers.StyleMap(new OpenLayers.Style(Object.append({
            pointRadius: 4,
            graphicName: "square",
            rotation: "${getRotation}",
            strokeColor: "${getStrokeColor}",
            fillOpacity: "${getFillOpacity}",
            fillColor:"#FFFFFF"
            }, OpenLayers.Feature.Vector.style["default"]), {
            context: {
            //                    getRotation: function(feature) {
            //                        return printForm.printPage.rotation;
            //                    },
            getStrokeColor: function(feature) {
            return feature.geometry.CLASS_NAME == "OpenLayers.Geometry.Point" ?
            "#000" : "#ee9900";
            },
            getFillOpacity: function(feature) {
            return feature.geometry.CLASS_NAME == "OpenLayers.Geometry.Point" ?
            0 : 0.4;
            }
            }
            })
            )*/
        });
        //visor.map.addLayer(extentLayer);
        return extentLayer;
    },
    addPage: function (newPage, layer) {
        newPage = newPage || new GeoExt.data.PrintPage({
            printProvider: this.printProvider
        });
        layer.addFeatures([newPage.feature]);
        newPage.addEvent("change", this.onPageChange.bind(this));

        this.page = newPage;

        if (visor.map.getCenter()) {
            this.fitPage();
        } else {
            visor.map.events.register("moveend", this, function () {
                visor.map.events.unregister("moveend", this, arguments.callee);
                this.fitPage();
            });
        }
    },
    removePage: function (page) {
        if (page) {
            var layerDib = page.feature.layer;
            page.feature.layer.removeAllFeatures();
            if (layerDib) {
                visor.map.removeLayer(layerDib);
            }
            delete page;
        }
        delete anexos;
        delete this.control;
        delete this.printProvider;
    },
    fitPage: function () {
        if (this.page) {
            var escalaObtenida = this.page.fit(visor.map, {
                mode: "screen"
            });
            if (escalaObtenida){
                this.setEscala(escalaObtenida["value"]);
            }
        }
    },
    setEscala: function (escala) {
        var s = $('select_escala');
        for (var iOpt = 0; iOpt < s.options.length; iOpt++) {
            if (s.options[iOpt].value == escala) {
                s.options[iOpt].selected = true;
                return;
            }
        }
    },
    selectPage: function (page) {
        this.control.active && this.control.setFeature(page.feature);
        // FIXME raise the feature up so that it is on top
    },
    updateBox: function () {
        var page = this.page;
        this.control.active &&
        this.control.setFeature(page.feature/*, {
            rotation: -page.rotation
        }*/);
        this.refreshAnexos($('text_filas').value, $('text_columnas').value);
    },
    onPageChange: function (page, mods) {
        //bug.log('onPageChange - this._updating' + this._updating);
        if (!this._updating) {
            //bug.log('Dentro onPageChange - this._updating', this._updating);
            this.control.active &&
            this.control.setFeature(page.feature/*, {
                rotation: -page.rotation
            }*/);
        }
    },
    show: function () {
        visor.map.addLayer(this.LayerPreview);
        visor.map.addControl(this.control);
        this.control.activate();

        // if we have a page and if the map has a center then update the
        // transform box for that page, in case the transform control
        // was deactivated when fitPage (and therefore onPageChange)
        // was called.
        if (this.page && visor.map.getCenter()) {
            this.updateBox();
        }
    },
    createControl: function () {
        this.control = new OpenLayers.Control.TransformFeature(this.LayerPreview, {
            preserveAspectRatio: true,
            rotate: false,
            eventListeners: {
                "beforesetfeature": function (e) {
                    //                    for(var i=0, len=this.pages.length; i<len; ++i) {
                    //                        if(this.pages[i].feature === e.feature) {
                    //                            this.page = this.pages[i];
                    //                            e.object.rotation = 0;// -this.pages[i].rotation;
                    //                            break;
                    //                        }
                    //                    }
                    //                     if(this.page.feature != e.feature) {
                    //                         this.control.deactivate();
                    //                     }
                } .bind(this),
                "setfeature": function (e) {
                    //                    for(var i=0, len=this.pages.length; i<len; ++i) {
                    //                        if(this.pages[i].feature === e.feature) {
                    //                            this.fireEvent("selectpage", this.pages[i]);
                    //                            break;
                    //                        }
                    //                    }
                    if (this.page) {
                        if (this.page.feature === e.feature) {
                            this.fireEvent("selectpage", this.page);
                        }
                        else {
                            this.control.setFeature(this.page.feature);
                        }
                    }
                } .bind(this),
                "beforetransform": function (e) {
                    this._updating = true;
                    //bug.log('beforetransform - this._updating' + this._updating);
                    var page = this.page;
                    if (e.rotation) {
                        if (page.layout["rotation"]) {
                            page.setRotation(0); //(-e.object.rotation);
                        } else {
                            e.object.setFeature(page.feature);
                        }
                    } else if (e.center) {
                        page.setCenter(OpenLayers.LonLat.fromString(
                            e.center.toShortString()
                            ));
                    } else {
                        var escalaObtenida = page.fit(e.object.box, {
                            mode: "closest"
                        });
                        this.setEscala(escalaObtenida["value"]);
                    }
                    delete this._updating;
                    //bug.log('beforetransform - this._updating' +this._updating);
                    return false;
                } .bind(this),
                "transformcomplete": function () {
                    this.updateBox();
                } .bind(this)
            }
        });
    }
});