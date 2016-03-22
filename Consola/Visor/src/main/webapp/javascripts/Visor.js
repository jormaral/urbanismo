var Visor = new Class({
    //ESTRUCTURA
    cabecera: null,
    botoneraPrincipal: null,
    botoneraGoogle: null,
    pie: null,
    zoomBar: null,
    map: null,
    //DATOS GENERALES
    idUser: 'default',
    perfilesXML: null,
    visorXML: null,
    serviciosXML: null,
    capasXML: null,
    entidadesPlanosXML: null,
    entidades: null,
    planos: null,
    treeEntidades: null,
    treePlanos: null,
    treeServicios: null,
    //DATOS DE IMAGEN
    //    rutaLogo1:'styles/images/icons/logotipoSantiago.gif',
    rutaLogo2: 'styles/images/icons/logotipoFEDER.png',
    //DATOS ESPECIFICOS
    ubicacionVisor: 'SPAIN',
    ambito: 'MADRID',
    provincia: 'MADRID',
    projectionConsultaServicios: 'EPSG:23030',
    rutaServicioRegistro: '',
    rutaServicioRegistroCoordenadas: '',
    rutaServicioRegistroWS: '',
    rutaXMLServicios: 'rutaXMLServicios',
    rutaPlanos: '',
    rutaFichasBase: '',
    maxAreaConsultaPoligono: '50000',
    zIndexCapas: 90,
    activarConsultaFicha: false,
    tiposDeFichas: "",
    zoomResultadosBusquedas: "",
    maxPuntosGeometria: 5000,
    //DATOS GIS
    optionsMap: {},
    layerMarkers: null,
    consultas: [/*'entidades_ext',*/'entidades', 'registro', 'planos'],
    consultasPorPoligono: ['entidades', 'planos'],

    //BASICOS
    baseLayerGoogle: true,
    zoomInicial: 13,
    Lang: 'es',
    projection: 'EPSG:900913',
    displayProjection: 'EPSG:900913',
    tileSize_width: 256,
    tileSize_height: 256,
    tileSize: new OpenLayers.Size(this.tileSize_width, this.tileSize_height),
    numZoomLevels: 26,
    imagePath: 'styles/images/',
    posActual: null,
    mapaDesplazandose: false,
    estadoPos: false,
    consultaMapaActivada: false,
    consultaFichaActivada: false,
    tipoConsulta: 'punto', //punto - para cosulta de un punto ||poligono - para consultar dibujando un poligono que cubra la zona de interes
    //BOUNDS
    boundsMaxExtent_minX: -179,
    boundsMaxExtent_minY: -89,
    boundsMaxExtent_maxX: 179,
    boundsMaxExtent_maxY: 89,
    boundsMaxExtent: new OpenLayers.Bounds(this.boundsMaxExtent_minX, this.boundsMaxExtent_minY, this.boundsMaxExtent_maxX, this.boundsMaxExtent_maxY),
    ExtensionTiles:null,
    centerX: -3.70326,
    centerY: 40.41709,
    center: new OpenLayers.LonLat(this.centerX, this.centerY),
    //CAPAS
    layerVector: null,
    firstWMS: true,

    initialize: function (visor,divId,perfilesXML) {
        //CARGO LOS DATOS GENERALES DEL VISOR
        this.generateIdUser();
        this.perfilesXML = perfilesXML;
        this.visorXML = this.perfilesXML.getElementsByTagName('visor')[visor];
        //CARGO DATOS ESPECIFICOS Y DATOS GIS
        if (this.visorXML) {
            this.cargarDatosEspecificosYDeGIS();
        }
        else {
            bug.log(jsIO.cargarTextoSegunIdioma("NO HA SIDO PODIBLE LEER EL FICHERO DE CONFIGURACION DE CAPAS"), "VISOR");
            //bug.alerta(jsIO.cargarTextoSegunIdioma("NO HA SIDO PODIBLE LEER EL FICHERO DE CONFIGURACION DE CAPAS"),"VISOR");
            alert(jsIO.cargarTextoSegunIdioma("NO HA SIDO PODIBLE LEER EL FICHERO DE CONFIGURACION DE CAPAS"));
        }
        //CARGO LA ESTRUCTURA DEL VISOR
        this.cabecera = new Cabecera(this.rutaLogo1, this.rutaLogo2, this.ambito, this.activarConsultaFicha, this.tiposDeFichas);

        this.pie = new Pie();
        this.botoneraPrincipal = new BotoneraPrincipal();
        if (this.baseLayerGoogle) {
            this.botoneraGoogle = new BotoneraGoogle();
        }
        //GENERO LAS OPCIONES DEL MAPA
        this.generarOpcionesDelMapa();

        //GENERO EL PROPIO MAPA
        this.map = new OpenLayers.Map(divId, this.optionsMap);
        //$('map').setStyle('cursor', 'url(styles/images/icons/cursores/Link.cur),move');
        //A ADO LAS CAPAS AL MAPA
        this.agregarCapas(true);
        this.actualizarZIndexMarkers();
        //UBICO EL MAPA SEGUN LOS PARAMETROS RECIBIDOS O POR DEFECTO
        this.map.setCenter(this.center, this.zoomInicial);
        //REASIGNO EL MAPA A LOS COMPONENTES
        this.zoomBar = new ZoomBar(this.map, this.zoomInicial, this.numZoomLevels,this.baseLayerGoogle);
        this.botoneraPrincipal.setMap(this.map);
        if (this.baseLayerGoogle) {
            this.botoneraGoogle.setMap(this.map);
        }
        this.cabecera.setMap(this.map);
        this.pie.setMap(this.map);
        this.pie.setInfoGeoPos();
        //A ADO LOS EVENTOS DEL MAPA
        this.addEventsToMap();
        //AGREGAR CONTROLES DE DIBUJO
        this.addControlesDibujo();
        //AGREGAR VERSION
        this.showVersion();
        //A ADO NUEVOS CONTROLES AL MAPA
        var nav = new OpenLayers.Control.NavigationHistory({
            'id': 'navigationHistory'
        });
        this.map.addControl(nav);
        //A ADO EL PREVIOUS
        var panelPrevious = new OpenLayers.Control.Panel(
        {
            div: document.getElementById("bpOpcion_previous")
        }
        );
        panelPrevious.addControls([nav.previous]);
        this.map.addControl(panelPrevious);
        //A ADO EL NEXT
        var panelNext = new OpenLayers.Control.Panel(
        {
            div: document.getElementById("bpOpcion_forward")
        }
        );
        panelNext.addControls([nav.next]);
        this.map.addControl(panelNext);
        //CARGO EL WEB MAP CONTEXT
        //    this.loadWebMapContext();
        //A ADO LOS TOOLTIPS
        /*new FaceTip({
        'els':$$('.cabOpcion')
        });*/
        new FaceTip({
            'els': $$('.bpOpcion')
        });
        new FaceTip({
            'els': $$('.cabLogo1')
        });



        this.mostrarAreaVisible();
        this.mostrarEscala();
    //nav.clear();
    //nav.activate();
    },
    showVersion: function () {
        jsIO.getServiceAsync({
            'wsName': 'GET_VERSION'
        }, function (response) {
            new Element('div', {
                'id': 'versionContainer',
                'html': response
            }).injectInside($(document.body));
        }, function (error) {
            new Element('div', {
                'id': 'versionContainer',
                'html': '-'
            }).injectInside($(document.body));
        });

    },
    generateIdUser: function () {
        this.idUser = $random(9999999, 9999999 * 1000);
        if (!jsIO.checkCookie('ID_USER')) {
            jsIO.setCookie('ID_USER', this.idUser, 150)
        };
    },
    obtenerCapasDeXML: function (servicios) {
        var capasBuscadas = jsIO.generarXMLvacio();
        capasBuscadas = capasBuscadas.appendChild(capasBuscadas.createElement('nodes'));
        //          var first=true;
        for (var i = 0; i < servicios.length; i++) {
            if (servicios[i].getAttribute('porDefecto') && servicios[i].getAttribute('porDefecto') == 'true') {
                if (servicios[i].getAttribute('type') == 'GOOGLE') {
                    if (this.visorXML.getAttribute('baseLayerGoogle').test('true')) {
                        //                          if(first==true){
                        //                              capasBuscadas=servicios[i].cloneNode();
                        //                              first=false;
                        //                          }else{
                        capasBuscadas.appendChild(servicios[i].cloneNode(false));

                    //                          }
                    }
                }
                else {
                    //                      if(first)
                    //                      {
                    //                          capasBuscadas=servicios[i].cloneNode();
                    //                          first=false;
                    //                      }
                    //                      else
                    //                      {
                    capasBuscadas.appendChild(servicios[i].cloneNode(false));
                //                      }
                }
            }
        }
        return capasBuscadas;
    },
    importXML: function (rutaWS, idAmbito) {
        var respuesta = undefined;
        new Request({
            url: 'ActionServlet',
            data: Hash.toQueryString({
                'wsName': 'CROSS-DOMAIN',
                'url': rutaWS + '?wsName=GET_LAYERCONFIG_DE_AMBITO&idAmbito=' + idAmbito
            }),
            async: false,
            onSuccess: function (response) {
                respuesta = response;
            } .bind(this),
            onFailure: function (response) {
                respuesta = response;
            } .bind(this)
        }).send();
        return respuesta;
    },
    cargarDatosEspecificosYDeGIS: function () {
        var xmlString = this.importXML(this.visorXML.getAttribute('rutaXMLServicios'), this.visorXML.getAttribute('idAmbito'));
        this.serviciosXML = jsIO.leerXMLFromString(xmlString);
        this.capasXML = this.obtenerCapasDeXML(this.serviciosXML.getElementsByTagName('node'));
        //DATOS ESPECIFICOS DEL VISOR
        this.ubicacionVisor = this.visorXML.getAttribute('ubicacionVisor');
        this.idAmbito = this.visorXML.getAttribute('idAmbito');
        this.idAmbitoBusquedas = this.visorXML.getAttribute('idAmbitoBusquedas');
        this.ambito = this.visorXML.getAttribute('ambito');
        this.provincia = this.visorXML.getAttribute('provincia');
        //this.municipio = this.visorXML.getAttribute('municipio');
        this.projectionConsultaServicios = this.visorXML.getAttribute('projectionConsultaServicios');
        this.rutaServicioRegistro = this.visorXML.getAttribute('urlRegistro');
        this.rutaServicioRegistroCoordenadas = this.visorXML.getAttribute('urlRegistroCoordenadas');
        this.rutaServicioRegistroWS = this.visorXML.getAttribute('urlRegistroWS');
        this.rutaXMLServicios = this.visorXML.getAttribute('rutaXMLServicios');
        //this.rutaPlanos = this.visorXML.getAttribute('rutaPlanos');
        this.rutaFichasBase = this.visorXML.getAttribute('rutaFichasBase');
        this.rutaSuperficies = this.visorXML.getAttribute('rutaSuperficies')
        this.rutaPrintModule = this.visorXML.getAttribute('rutaPrintModule');
        this.rutaLogo1 = this.visorXML.getAttribute('rutaLogo1');
        this.rutaLogo2 = this.visorXML.getAttribute('rutaLogo2');
        this.maxAreaConsultaPoligono = 0;// this.visorXML.getAttribute('maxAreaConsultaPoligono');
        this.baseLayerGoogle = this.visorXML.getAttribute('baseLayerGoogle').test('true');
        this.rutaCatastroProvincias = this.visorXML.getAttribute('rutaCatastroProvincias');
        this.rutaCatastroMunicipios = this.visorXML.getAttribute('rutaCatastroMunicipios');
        this.rutaCatastroCoord = this.visorXML.getAttribute('rutaCatastroCoord');
        this.rutaCatastroRefCat = this.visorXML.getAttribute('rutaCatastroRefCat');
        this.factorLeyenda = this.visorXML.getAttribute('factorLeyenda');
        this.serviciosWMS = this.visorXML.getAttribute('serviciosWMS');
        this.serviciosWFS = this.visorXML.getAttribute('serviciosWFS');
        this.activarConsultaFicha = this.visorXML.getAttribute('activarConsultaFicha').test('true');
        this.tiposDeFichas = this.visorXML.getAttribute('tiposDeFichas');
        this.zoomResultadosBusquedas = this.visorXML.getAttribute('zoomResultadosBusquedas').toInt();
        this.maxPuntosGeometria = this.visorXML.getAttribute('maxPuntosGeometria').toInt();
        //DATOS ESPECIFICOS DEL VISOR
        //BASICOS
        OpenLayers.ImgPath = this.visorXML.getAttribute('imgPath');
        OpenLayers.Util.onImageLoadErrorColor = this.visorXML.getAttribute('onImageLoadErrorColor');
        OpenLayers.IMAGE_RELOAD_ATTEMPTS = (this.visorXML.getAttribute('IMAGE_RELOAD_ATTEMPTS')).toInt();
        OpenLayers.Lang.setCode(this.visorXML.getAttribute('lang'));
        this.Lang = this.visorXML.getAttribute('lang');
        this.zoomInicial = (this.visorXML.getAttribute('zoomInicial')).toInt();
        this.projection = this.visorXML.getAttribute('projection');
        this.displayProjection = this.visorXML.getAttribute('displayProjection');
        this.imagePath = this.visorXML.getAttribute('imgPath');
        //BOUNDS
        this.tileSize_width = (this.visorXML.getAttribute('tileWidth')).toInt();
        this.tileSize_height = (this.visorXML.getAttribute('tileHeight')).toInt();
        this.tileSize = new OpenLayers.Size(this.tileSize_width, this.tileSize_height);
        this.maxResolution = (this.visorXML.getAttribute('maxResolution')).toFloat();
        this.numZoomLevels = (this.visorXML.getAttribute('numZoomLevels')).toInt();
        this.boundsMaxExtent_minX = parseFloat((this.visorXML.getAttribute('maxExtent')).split(',')[0]);
        this.boundsMaxExtent_minY = parseFloat((this.visorXML.getAttribute('maxExtent')).split(',')[1]);
        this.boundsMaxExtent_maxX = parseFloat((this.visorXML.getAttribute('maxExtent')).split(',')[2]);
        this.boundsMaxExtent_maxY = parseFloat((this.visorXML.getAttribute('maxExtent')).split(',')[3]);
        this.boundsMaxExtent = new OpenLayers.Bounds(this.boundsMaxExtent_minX, this.boundsMaxExtent_minY, this.boundsMaxExtent_maxX, this.boundsMaxExtent_maxY);
        
        this.ExtensionTiles = this.boundsMaxExtent; 
        if (this.visorXML.getAttribute('ExtensionTiles')){
            this.ExtensionTiles_minX = parseFloat((this.visorXML.getAttribute('ExtensionTiles')).split(',')[0]);
            this.ExtensionTiles_minY = parseFloat((this.visorXML.getAttribute('ExtensionTiles')).split(',')[1]);
            this.ExtensionTiles_maxX = parseFloat((this.visorXML.getAttribute('ExtensionTiles')).split(',')[2]);
            this.ExtensionTiles_maxY = parseFloat((this.visorXML.getAttribute('ExtensionTiles')).split(',')[3]);
            this.ExtensionTiles = new OpenLayers.Bounds(this.ExtensionTiles_minX, this.ExtensionTiles_minY, this.ExtensionTiles_maxX, this.ExtensionTiles_maxY);
        }
        //new OpenLayers.Bounds(-2.003750834E7,-2.003750834E7,2.003750834E7,2.003750834E7),// this.boundsMaxExtent,
        
        
        if (this.displayProjection != this.projection) {
            this.boundsMaxExtent = this.boundsMaxExtent.transform(new OpenLayers.Projection(this.displayProjection), new OpenLayers.Projection(this.projection))
        }
        this.centerX = parseFloat((this.visorXML.getAttribute('center')).split(',')[0]);
        this.centerY = parseFloat((this.visorXML.getAttribute('center')).split(',')[1]);
        this.center = new OpenLayers.LonLat(this.centerX, this.centerY);
        if (this.displayProjection != this.projection) {
            this.center = this.center.transform(new OpenLayers.Projection(this.displayProjection), new OpenLayers.Projection(this.projection))
        }
        bug.log('-----------------', 'visor');
        bug.log('center: ' + this.center, 'visor');
        bug.log('bounds: ' + this.boundsMaxExtent, 'visor');
        bug.log('-----------------', 'visor');
    },
    generarOpcionesDelMapa: function () {
        if (this.baseLayerGoogle){
            this.optionsMap = {
                'projection': this.projection,
                'displayProjection': new OpenLayers.Projection(this.displayProjection),
                'units': 'm',
                'controls': this.agregarControles(),
                'maxExtent': this.ExtensionTiles,
                'restrictedExtent': this.boundsMaxExtent,
                'maxResolution': this.maxResolution,
                'zoom': this.zoomInicial,
				'numZoomLevels': this.numZoomLevels,
                'theme': null, //this.imagePath,
                'allOverlays':  false
            }
        }else{
            this.optionsMap = {
                'projection': this.projection,
                'displayProjection': new OpenLayers.Projection(this.displayProjection),
                'units': 'm',
                'controls': this.agregarControles(),
                'maxExtent': this.ExtensionTiles,
                'restrictedExtent': this.boundsMaxExtent,
                'maxResolution': this.maxResolution,
                'numZoomLevels': this.numZoomLevels,
                'zoom': this.zoomInicial,
                'theme': null, //this.imagePath,
                'allOverlays': true
            }
        }
    },
    agregarControles: function () {
        return [
        new OpenLayers.Control.Navigation({
            'id': 'hand'
        }),
        //ACR: Modificaci n motivada por la incidencia INC000000256847
        new OpenLayers.Control.MousePosition({
            'id': 'coordenadasMapa',
            'div': $('pieOpcion_coordenadas')
        }),
        new OpenLayers.Control.ZoomBox({
            'id': 'zoomIn_area',
            'out': false,
            'alwaysZoom': true
        }),
        new OpenLayers.Control.ZoomBox({
            'id': 'zoomOut_area',
            'out': true,
            'alwaysZoom': true
        })/*,
            new OpenLayers.Control.NavigationHistory({
                'id': 'navigationHistory'
            })*/

        ];
    },
    agregarCapas: function () {
        if (this.capasXML) {
            //this.crearCapaSegunXML(this.capasXML);
            for (var h = 0; h < this.capasXML.getElementsByTagName('node').length; h++) {
                this.crearCapaSegunXML(this.capasXML.getElementsByTagName('node')[h]);
            }
        }
        else {
            bug.log(jsIO.cargarTextoSegunIdioma("NO HA SIDO PODIBLE LEER EL FICHERO DE CONFIGURACION DE CAPAS"), "VISOR");
            //bug.alerta(jsIO.cargarTextoSegunIdioma("NO HA SIDO PODIBLE LEER EL FICHERO DE CONFIGURACION DE CAPAS"),"VISOR");
            alert(jsIO.cargarTextoSegunIdioma("NO HA SIDO PODIBLE LEER EL FICHERO DE CONFIGURACION DE CAPAS"));

        }
    },
    addControlesDibujo: function () {
        if (this.layerVector) {
            //GENERO LAS OPCIONES DE DIBUJO GENERALES
            var drawFeaturesOptions = new Hash({
                handlerOptions: {
            //                            style: "prueba", // this fveorces default render intent
            //                            layerOptions: {styleMap: styleMap},
            //                            persist: true
            //                              mouseup:function(evt){
            //                                  var evento=evt;
            //                              }
            }
            });
            var modifyOptions = new Hash({
                onSelect: function (feature) {
                    this.layer.removeFeatures(feature);
                }
            //                  handlerOptions: {
            //                      //style: "prueba", // this forces default render intent
            //                      //layerOptions: {styleMap: styleMap},
            //                      //persist: true,
            //                      radiusHandle:rh
            //                  }
            });
            //GENERO LOS PROPIOS CONTROLES
            this.map.addControl(new OpenLayers.Control.DrawFeature(this.layerVector, OpenLayers.Handler.Point, drawFeaturesOptions.set('id', 'ctrl_PUNTO')));
            this.map.addControl(new OpenLayers.Control.DrawFeature(this.layerVector, OpenLayers.Handler.Path, drawFeaturesOptions.set('id', 'ctrl_LINEA')));
            this.map.addControl(new OpenLayers.Control.DrawFeature(this.layerVector, OpenLayers.Handler.Polygon, drawFeaturesOptions.set('id', 'ctrl_POLIGONO')));
            this.map.addControl(new OpenLayers.Control.DrawFeature(this.layerVector, OpenLayers.Handler.RegularPolygon, new Hash({
                id: 'ctrl_POLIG_REGULAR',
                handlerOptions: {
                    sides: 4
                }
            })));
            var modify = new OpenLayers.Control.ModifyFeature(this.layerVector, modifyOptions.set('id', 'ctrl_MODIFY'));
            redefinirModifyFeature(modify);
            this.map.addControl(modify);
            this.map.addControl(new OpenLayers.Control.SelectFeature(this.layerVector, {
                'id': 'ctrl_SELECT', 
                onSelect: this.onFeatureSelect, 
                onUnselect: this.onFeatureUnselect.bind(this)
            }));
            this.map.addControl(new OpenLayers.Control.SelectFeature(this.layerVector, {
                'id': 'ctrl_DELETE', 
                onSelect: this.onFeatureSelectForDelete.bind(this)
            }));

        }
    },
    onFeatureSelect: function (feature) {
        var area=feature.geometry.getArea().toFixed(2);
        var perimetro=feature.geometry.getLength().toFixed(2);
        if (visor.map.getProjectionObject().proj.sphere || visor.projectionConsultaServicios=="EPSG:4326"){
            if (area>0 && feature.geometry.getGeodesicArea){
                area=feature.geometry.getGeodesicArea(visor.map.getProjectionObject()).toFixed(2);
            }
            if (feature.geometry.getGeodesicLength){
                perimetro=feature.geometry.getGeodesicLength(visor.map.getProjectionObject()).toFixed(2);
            }
        }
        visor.addLittlePopupToMap(visor.obtenerCentroide(feature.geometry.getCentroid(), false),
            "<div style='font-size:9pt'><b>" + 
            jsIO.cargarTextoSegunIdioma('Area') + ": </b>" + area + " m<sup>2</sup><br /><b>" + 
            jsIO.cargarTextoSegunIdioma('Perimetro') + ": </b>" + perimetro + " m</div>", true);
        var sf = new OpenLayers.Control.SelectFeature(visor.layerVector, {});
        sf.unselectAll();
    
        if (confirm(jsIO.cargarTextoSegunIdioma("La generacion de la ficha puede tardar varios minutos segun la geometria solicitada. Continuar?"))) {
            //window.open(this.rutaSuperficies + '?WKT=' + feature.geometry.transform(new OpenLayers.Projection(visor.projection), new OpenLayers.Projection(visor.projectionConsultaServicios)) + '&SRS=' + this.projectionConsultaServicios);
            var geo=feature.geometry.clone();
            relocate(visor.rutaSuperficies, {
                'WKT': geo.transform(new OpenLayers.Projection(visor.projection), new OpenLayers.Projection(visor.projectionConsultaServicios)),
                'SRS': visor.projectionConsultaServicios,
                'idAmbito': visor.idAmbito
            });
        }
   
    },
    onFeatureUnselect: function (feature) {
        this.botoneraPrincipal.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Relacion de superficies afectadas')); //Seleccione un elemento dibujado para obtener su informacion asociada'));
    //            this.map.removePopup(feature.popup);
    //            feature.popup.destroy();
    //            feature.popup = null;
    },
    onFeatureSelectForDelete: function (feature) {
        var selectedFeature = feature;
        if (confirm(jsIO.cargarTextoSegunIdioma('DESEA BORRAR ESTE ELEMENTO?'))) {
            this.layerVector.removeFeatures([selectedFeature]);
        }
    },
    crearCapaSegunXML: function (capa) {
        var layer;
        switch (capa.getAttribute('type')) {
            case 'GOOGLE':
                if (this.baseLayerGoogle) {
                    layer = this.crearCapaGoogle(
                        jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')),
                        capa.getAttribute('layers'),
                        true, //capa.getAttribute('isBaseLayer').test('true'),
                        capa.getAttribute('visibility').test('true')
                        );
					//PARA EVITAR LA VISTA OBLICUA A ZOOM CERCANOS :              
                    layer.mapObject.setTilt(0);
                    //ACTIVO EL BOTON DE LA CABECERA CORRESPONDIENTE
                    this.botoneraGoogle.activarBoton(capa.getAttribute('layers'));
                }
                break;
            case 'VIRTUAL_EARTH':
                break;
            case 'WMS':
                var singleTile = false;
                var visibility = false;
                var isBaseLayer = false;
                if (capa.getAttribute('singleTile') && capa.getAttribute('singleTile').test('true') == true) {
                    singleTile = true;
                }
                if (capa.getAttribute('visibility') && capa.getAttribute('visibility').test('true') == true) {
                    visibility = true;
                }
                if (capa.getAttribute('isBaseLayer') && capa.getAttribute('isBaseLayer').test('true') == true) {
                    isBaseLayer = true;
                }
                layer = this.crearCapaWMS(
                    jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')),
                    capa.getAttribute('url'),
                    capa.getAttribute('layers'),
                    capa.getAttribute('transparent').test('true'),
                    capa.getAttribute('format'),
                    singleTile,
                    visibility,
                    capa.getAttribute('standard').test('true'),
                    capa.getAttribute('filter'),
                    isBaseLayer,
                    capa.getAttribute('style')

                    );
                break;
            case 'WFS':


                layer = new OpenLayers.Layer.WFS(
                    jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')),
                    capa.getAttribute('url'),
                    {
                        typename: 'app:Entidad'
                    },
                    {
                        typename: 'Entidad',
                        featureNS: 'http://www.opengis.net/ows',
                        'projection': new OpenLayers.Projection(this.displayProjection),
                        extractAttributes: false
                    });

                //                    var styleMap = new OpenLayers.StyleMap({
                //                        strokeColor: "blue",
                //                        strokeWidth: 2,
                //                        strokeOpacity: 0.5,
                //                        fillOpacity: 0.2
                //                    });
                //                    // create a color table for state FIPS code
                //                        var colors = ["red", "orange", "yellow", "green", "blue", "purple"];
                //                        var code, fips = {};
                //                        for(var i=1; i<=66; ++i) {
                //                            code = "0" + i;
                //                            code = code.substring(code.length - 2);
                //                            fips[code] = {fillColor: colors[i % colors.length]};
                //                        }
                //                        // add unique value rules with your color lookup
                //                        styleMap.addUniqueValueRules("default", "DATA", fips);
                //
                //
                //                    layer = new OpenLayers.Layer.Vector(
                //                        jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')),
                //                        {
                //                            strategies: [new OpenLayers.Strategy.BBOX()],
                //                            protocol: new OpenLayers.Protocol.WFS({
                //                                version: "1.0.0",
                //                                srsName: this.displayProjection, // this is the default
                //                                url:  capa.getAttribute('url'),
                //                                featureType: "data WFS",
                //                                featureNS: capa.getAttribute('text')+"/topp"
                //                            }),
                //                            projection: new OpenLayers.Projection(this.displayProjection), // specified because it is different than the map
                //                            styleMap: styleMap
                //                        });

                break;
            case 'ArcGIS':
                break;
            case 'VECTOR':
                document.namespaces;
                layer = this.crearCapaVector(jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')), 'blue');
                layer.id = jsIO.cargarTextoSegunIdioma(capa.getAttribute('text'));
                if (!this.layerVector) {
                    this.layerVector = layer;
                }
                this.map.addLayer(this.layerVector);
                break;
            case 'KML':
                this.crearCapaKML(
                    jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')),
                    capa.getAttribute('url')
                    );
                break;
            case 'MARKERS':
                this.layerMarkers = new OpenLayers.Layer.Markers(jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')));
                this.layerMarkers.id = jsIO.cargarTextoSegunIdioma(capa.getAttribute('text'));
                this.map.addLayer(this.layerMarkers);
                break;
        }

        this.obtenerNuevaZ();
        return layer;
    },
    crearCapaGoogle: function (nombre, tipo, isBaseLayer, visibility) {
        var layer = new OpenLayers.Layer.Google(
            nombre,
            {
                'type': jsIO.getTipoServicioGoogle(tipo),
                'sphericalMercator': true,
                'displayInLayerSwitcher': false,
                'wrapDateLine': true,
                'isBaseLayer': isBaseLayer,
                'visibility': visibility
            }
            );
        layer.id = nombre;


        redefinirGOOGLE_stylePane(layer);
        //redefinirEVENTPANE();


        this.map.addLayer(layer);
        redefinirGOOGLE_BackgroundColor(layer);
        return layer;
    },
    crearCapaVector: function (nombre, color) {
        var layer = new OpenLayers.Layer.Vector(
            nombre,
            {
                'displayInLayerSwitcher': false,
                'isBaseLayer': false,
                'styleMap': this.regenerarStyleMap('blue')//,
            //	          'renderers': ["Canvas", "SVG", "VML"]
            //'numZoomLevels':(visorXML.getAttribute('numZoomLevels')).toInt()+5
            }
            );
        return layer;
    },
    crearCapaKML: function (nombre, url) {
        var layer = new OpenLayers.Layer.GML(
            nombre,
            url,
            {
                'format': OpenLayers.Format.KML,
                'formatOptions': {
                    'extractStyles': true,
                    'extractAttributes': true,
                    'internalProjection': new OpenLayers.Projection(visor.displayProjection),
                    'externalProjection': new OpenLayers.Projection('EPSG:4326')
                },
                //'numZoomLevels':(visorXML.getAttribute('numZoomLevels')).toInt()+5,
                'displayInLayerSwitcher': false
            //'projection':new OpenLayers.Projection(this.projectionConsultaServicios)
            },
            {
            //'projection':new OpenLayers.Projection(this.projectionConsultaServicios)
            //'displayProjection':new OpenLayers.Projection(displayProjection_)
            }
            );
        layer.id = nombre;
        //redefinirKML(layer);
        this.map.addLayer(layer);
        return layer;
    },
    crearCapaGML: function (nombre, url) {
        var layerGML = new OpenLayers.Layer.GML(
            nombre,
            url,
            {
                //'numZoomLevels':(visorXML.getAttribute('numZoomLevels')).toInt()+5,
                //'displayInLayerSwitcher':false,
                'projection': new OpenLayers.Projection(this.projectionConsultaServicios)
            },
            {
                'projection': new OpenLayers.Projection(this.projectionConsultaServicios)
            //'displayProjection':new OpenLayers.Projection(displayProjection_)
            }
            );
        layerGML.id = nombre;
        redefinirGML(layerGML);
        this.map.addLayer(layerGML);
        return layerGML;
    },
    crearCapaWMS: function (nombre, url, layers, transparent, format, singleTile, visibility, standard, filtro, isBaseLayer) {

        if (isBaseLayer == null || isBaseLayer == undefined) {
            isBaseLayer = false;
        }
        var projeccionWMS = new OpenLayers.Projection(this.displayProjection);
        if (standard) {
            projeccionWMS = new OpenLayers.Projection(this.projectionConsultaServicios);
        }
        var layer = new OpenLayers.Layer.WMS(
            nombre,
            url,
            {
                'layers': layers,
                'transparent': transparent,
                'format': format
            },
            {
                'isBaseLayer': isBaseLayer,
                'displayInLayerSwitcher': false,
                'projection': projeccionWMS,
                'displayOutsideMaxExtent': true,
                'gutter': 0,
                'transitionEffect': 'resize',
                'reproject': true,
                'buffer': 0,
                'singleTile': singleTile,
                'visibility': visibility
            }
            );

        //COMPRUEBO SI ES UN PLANO

        //layer.opacity=0.6;
        if (filtro && filtro != "") {
            layer.CQL_FILTER = filtro;
        }
        layer.id = nombre;
        redefinirWMS(layer, this.projection);
        this.map.addLayer(layer);
    },
    getWFSGeometryField: function (url, servicio, prefijo, namespace) {
        url = jsIO.reformatURL_WFS_for_DescribeType(url);

        var entityResponse = jsIO.getService({
            'wsName': 'CROSS-DOMAIN',
            'url': url + '&TypeName=' + prefijo + ':' + servicio + '&Namespace=xmlns(' + prefijo + '=' + namespace + ')'
        });
        //PARSEO EL XML
        var wfsXML = jsIO.leerXMLFromString(entityResponse);
        var nodes = null;
        nodes = wfsXML.getElementsByTagName('xsd:element');
        for (var idNode = 0; idNode < nodes.length; idNode++) {
            var node = nodes[idNode];
            if (node.getAttribute('type').indexOf("gml:") == 0) {
                return prefijo + ':' + node.getAttribute('name');
            }
        }
        return null;
    },
    crearCapaWFS: function (nombre, url, layerServicio, visibility, prefijo, namespace) {
        var projeccionWFS = new OpenLayers.Projection(this.projectionConsultaServicios);

        OpenLayers.ProxyHost = "ActionServlet?wsName=CROSS-DOMAIN&url=";
        var layer = new OpenLayers.Layer.Vector(nombre,
        {
            visibility: visibility,
            strategies: [new OpenLayers.Strategy.BBOX()],
            protocol: new OpenLayers.Protocol.WFS({
                url: url,
                featureType: layerServicio,
                featurePrefix: prefijo,
                featureNS: namespace,
                srsName: this.projectionConsultaServicios,
                geometryName: this.getWFSGeometryField(url, layerServicio, prefijo, namespace),
                version: '1.1.0'
            // outputFormat: 'GML3'
            }),
            styleMap: this.regenerarStyleMap('orange')//,
        //	      renderers: ["Canvas", "SVG", "VML"]
        },
        {
            isBaseLayer: false,
            buffer: 0
        }
        );


        layer.id = layerServicio;

        this.map.addLayer(layer);
        layer.refresh();
    },
    mostrarEscala: function () {
        if (this.map && this.pie) {
            bug.log('ESCALA: ' + this.map.getScale(), 'VISOR');
            this.pie.setEscala(this.map.getScale());
        }
    },
    mostrarAreaVisible: function () {
        if (this.map && this.pie) {
            bug.log('AREA VISIBLE: ' + this.map.calculateBounds(), 'VISOR');
            this.pie.setAreaVisible(this.map.calculateBounds());
        }
    },
    /*saveWebMapContext:function(){
jsIO.setCookie('ALMTR_pos_lon', this.map.getCenter().lon, 365)
jsIO.setCookie('ALMTR_pos_lat', this.map.getCenter().lat, 365)
jsIO.setCookie('ALMTR_zoom', this.map.getZoom(), 365);
jsIO.setCookie('ALMTR_capas', this.map.layers, 365);
bug.log('-------------> WEB MAP CONTEXT ALMACENADO: '+jsIO.getCookie('ALMTR_zoom'),'VISOR');
            
},
loadWebMapContext:function(){
if(jsIO.checkCookie("ALMTR_pos_lon") && jsIO.checkCookie("ALMTR_pos_lat") && jsIO.checkCookie("ALMTR_zoom"))
{
this.map.setCenter(new OpenLayers.LonLat(jsIO.getCookie('ALMTR_pos_lon'),jsIO.getCookie('ALMTR_pos_lat')),(jsIO.getCookie('ALMTR_zoom')).toInt(),false,true);
}
},*/
    addEventsToMap: function () {
        //MOVESTART
        this.map.events.register('movestart', this.map, function (evt) {
            this.mapaDesplazandose = true;
        } .bind(this));
        //MOUSEMOVE
        this.map.events.register('mousemove', this.map, function (evt) {
            if (!this.mapaDesplazandose) {
                //OBTENGO EL PUNTO DONDE ESTA SITUADO EL RATON
                this.posActual = this.map.getLonLatFromPixel(evt.xy);
                //COMPRUEBO SI DESPUES DE 2 SEGUNDOS EL PUNTO SIGUE SIENDO EL MISMO
                this.checkIdemPos(this.posActual, evt.xy)

            }
        } .bind(this));
        //MOVEEND
        this.map.events.register('moveend', this.map, function (evt) {
            this.mostrarAreaVisible();
            this.mostrarEscala();
            this.mapaDesplazandose = false;
            if (this.botoneraPrincipal) {
                this.botoneraPrincipal.changeCursor();
            }
        //this.saveHistorico();
        //this.saveWebMapContext();
        } .bind(this));
        //MOVE
        this.map.events.register('move', this.map, function (evt) {
            if (this.botoneraPrincipal) {
                this.botoneraPrincipal.changeCursor(true);
            }
        } .bind(this));
        //ZOOMEND
        this.map.events.register('zoomend', this.map, function (evt) {
            this.mostrarAreaVisible();
            if (this.zoomBar) {
                if (this.baseLayerGoogle){
                    this.zoomBar.sliderControl.set(this.map.getZoom()*(-1));
                }else{
                    this.zoomBar.sliderControl.set(this.map.numZoomLevels-1-this.map.getZoom());
                }
            }
            if (this.pie) {
                this.pie.resetEscala();
            }
            if (this.botoneraPrincipal) {
                this.botoneraPrincipal.changeCursor();
            }
        //this.saveHistorico();
        //this.saveWebMapContext();
        } .bind(this));
        //CLICK
        this.map.events.register('click', this.map, function (evt) {
            if (this.consultaMapaActivada) {
                if (this.tipoConsulta == 'punto') {
                    //CONSULTO POR PUNTO
                    bug.log("Punto seleccionado: " + this.map.getLonLatFromPixel(evt.xy), "VISOR");
                    //A ADO EL MARKER
                    var pixel=evt.xy;
                    this.addMarkerToMap(this.map.getLonLatFromPixel(new OpenLayers.Pixel(pixel.x+4,pixel.y-5)), jsIO.cargarTextoSegunIdioma('CONSULTA'), true)
                    //PREPARO LAS CONSULTAS
                
                    pixel=new OpenLayers.Pixel(pixel.x+3,pixel.y);
                    var puntoXY = this.map.getLonLatFromViewPortPx(pixel);
                    //                                if(this.displayProjetion!=this.projection)
                    //                                {
                    puntoXY = puntoXY.transform(new OpenLayers.Projection(this.displayProjection), new OpenLayers.Projection(this.projectionConsultaServicios));
                    //                                }

                    this.preloadConsultasPorPunto(puntoXY);

                    //LANZO LA PETICION DE LA FICHA SI CORRESPONDE
                    if (visor.consultaFichaActivada) {
                        //window.open(this.rutaFichasBase + visor.cabecera.fichaAConsultar + '?X=' + puntoXY.lon + '&Y=' + puntoXY.lat + '&SRS=' + this.projectionConsultaServicios);
                        //Primero obtenemos el IdAmbito del punto pinchado:
                        new Request({
                            url: 'ActionServlet?wsName=GET_ID_AMBITO&X=' + puntoXY.lon + '&Y=' + puntoXY.lat + '&srs=' + this.projectionConsultaServicios,
                            ////                              data:Hash.toQueryString({
                            //                                      'wsName':'CONSULTA_GRAFICA',
                            //                                      'wkt':stringWKT,
                            //                                      'srs':this.displayProjection,
                            //                                      'idioma':'es'
                            //                              }),
                            onSuccess: function (response) {
                                if (response == -1) {
                                    alert(jsIO.cargarTextoSegunIdioma("No se han localizado datos para el punto indicado"));
                                } else {
                                    relocate(this.rutaFichasBase + visor.cabecera.fichaAConsultar, {
                                        'X': puntoXY.lon,
                                        'Y': puntoXY.lat,
                                        'SRS': this.projectionConsultaServicios,
                                        'idAmbito': response
                                    });
                                }
                            } .bind(this),
                            onFailure: function (response) {
                                alert(jsIO.cargarTextoSegunIdioma("No se han localizado datos para el punto pinchado"));
                            } .bind(this)
                        }).send();
                    }
                }
                else {
                    //CONSULTO POR POLIGONO
                    bug.log('se procede a consultar por poligono');
                    //ELIMINO LOS MARKERS
                    this.removeMarkers();
                //ACTIVO EL CONTROL DE DIBUJO DE POLIGONOS

                }
            }
        } .bind(this));
    },
    regenerarStyleMap: function (COLOR) {
        var simbolos = {
            "Point": {
                pointRadius: 4,
                graphicName: "circle",
                fillColor: COLOR,
                fillOpacity: 0.8,
                strokeWidth: 1,
                strokeOpacity: 1,
                strokeColor: "#ffffff",
                cursor: 'pointer'
            },
            "Line": {
                strokeWidth: 2,
                strokeOpacity: 1,
                strokeColor: COLOR,
                strokeDashstyle: "solid",
                cursor: 'pointer'
            },
            "Polygon": {
                strokeWidth: 2,
                strokeOpacity: 1,
                strokeColor: COLOR,
                fillColor: COLOR,
                strokeDashstyle: "solid",
                fillOpacity: 0.3,
                cursor: 'pointer'
            }
        };
        //DEFINO EL STYLE
        var myStyleDraw = new OpenLayers.Style();
        myStyleDraw.addRules([
            new OpenLayers.Rule({
                symbolizer: simbolos
            })
            ]);
        //A ADO TODO AL STYLE MAP
        var myStyleMap = new OpenLayers.StyleMap({
            "default": myStyleDraw
        });
        return myStyleMap;
    },
    preloadConsultasPorPoligono: function (wkt) {
        bug.log('WKT pasado para la consulta por punto: ' + wkt);
        this.botoneraPrincipal.zonaActivaAux.set('html', '');
        //DEFINO EL ICONO DE CARGA
        this.listaDeConsultas(undefined, wkt);
    },
    preloadConsultasPorPunto: function (lonlat) {
        bug.log('LonLat pasado para la consulta por punto: ' + lonlat);
        this.botoneraPrincipal.zonaActivaAux.set('html', '');
        //DEFINO EL ICONO DE CARGA
        //                if(this.displayProjection!=this.projection)
        //                {
        //                    lonlat=lonlat.transform(new OpenLayers.Projection(this.projection),new OpenLayers.Projection(this.projectionConsultaServicios))
        //                }
        this.listaDeConsultas(lonlat, 'POINT(' + lonlat.lon + ' ' + lonlat.lat + ')');
    },
    listaDeConsultas: function (lonlat, stringWKT) {
        if (!lonlat) {
            lonlat = this.obtenerCentroide(stringWKT, false);
        }
        this.consultas.each(function (elem) {


            //GENERO LA LINEA DE LA CONSULTA
            new Element('div', {
                'id': 'piInfoAux_Center_consulta_' + elem,
                'class': 'lineaConsulta',
                'tip': jsIO.cargarTextoSegunIdioma('txt_' + elem),
                'events': {
                    'click': function () {
                        switch (elem) {
                            case 'entidades_ext':
                                if (this.entidadesPlanosXML_Extend) {
                                    this.loadVentanaEntidadesExtend(lonlat, stringWKT);
                                }
                                break; 
                            case 'entidades':
                                if (this.entidadesPlanosXML) {
                                    this.loadVentanaEntidades(lonlat);
                                    new FaceTip({
                                        'els': $$('.mooTree_img')
                                    });
                                }
                                break;
                            case 'registro':
                                var jw = new JobWindow('REGISTRO_SEGUN_COORDENADAS', false, 500, 300, 300, 200, true);
                                jw.loadIframe(this.rutaServicioRegistroCoordenadas + '?x=' + lonlat.lon + '&y=' + lonlat.lat + '&srs=' + this.projectionConsultaServicios + '&rutaServ=' + this.rutaServicioRegistroWS + '&debugMode=on');

                                break;
                            case 'planos':
                                if (this.entidadesPlanosXML || this.entidadesPlanosXML_Extend) {
                                    this.loadVentanaPlanos(lonlat);
                                }
                                break;
                        }
                    } .bind(this)
                },
                'html': jsIO.cargarTextoSegunIdioma(elem).toUpperCase()
            }).injectInside(this.botoneraPrincipal.zonaActivaAux).setStyle('opacity', 0.8);
            //PRECARGO LA INFORMACION
            switch (elem) {
                case 'entidades_ext': //UTILIZA EL MISMO ACCESO A LOS DATOS QUE LOS PLANOS
                    this.entidadesPlanosXML = null;
                    this.entidades = null;
                    this.planos = null;
                    //this.readEntidadesPlanos(stringWKT);
                    this.readEntidadesPlanosExtend(stringWKT);
                    break; 
                case 'entidades': //UTILIZA EL MISMO ACCESO A LOS DATOS QUE LOS PLANOS
                    this.entidadesPlanosXML = null;
                    this.entidades = null;
                    this.planos = null;
                    this.readEntidadesPlanos(stringWKT);

                    break;
                case 'registro':
                    $('piInfoAux_Center_consulta_' + elem).setStyle('background-image', 'url(styles/images/accept.png)');

                    break;
                case 'planos':
                    //UTILIZA EL MISMO ACCESO A LOS DATOS QUE LAS ENTIDADES
                    break;
            }
        } .bind(this));

        new FaceTip({
            'els': $$('.lineaConsulta')
        });

    },
    readEntidadesPlanosExtend: function (stringWKT) {
        new Request({
            url: 'ActionServlet?wsName=CONSULTA_GRAFICA_EXTENDIDA&wkt=' + stringWKT + '&srs=' + this.projectionConsultaServicios + '&idioma=es',
            //// data:Hash.toQueryString({
            // 'wsName':'CONSULTA_GRAFICA',
            // 'wkt':stringWKT,
            // 'srs':this.displayProjection,
            // 'idioma':'es'
            // }),
            onSuccess: function (response) {
                if (response && response != '' && response != ' ' && response != '\\r\\n' && !response.contains('Sin datos')) {
                    $('piInfoAux_Center_consulta_' + 'entidades_ext').setStyle('background-image', 'url(styles/images/accept.png)');
                    $('piInfoAux_Center_consulta_' + 'planos').setStyle('background-image', 'url(styles/images/accept.png)');
                    this.entidadesPlanosXML_Extend = jsIO.leerXMLFromString(response);
                }
                else {
                    $('piInfoAux_Center_consulta_' + 'entidades_ext').setStyle('background-image', 'url(styles/images/delete.png)');
                    $('piInfoAux_Center_consulta_' + 'planos').setStyle('background-image', 'url(styles/images/delete.png)');
                    this.entidadesPlanosXML_Extend = null;
                }
            } .bind(this),
            onFailure: function (response) {
                $('piInfoAux_Center_consulta_' + 'entidades_ext').setStyle('background-image', 'url(styles/images/delete.png)');
                $('piInfoAux_Center_consulta_' + 'planos').setStyle('background-image', 'url(styles/images/delete.png)');
                this.entidadesPlanosXML_Extend = null;
            } .bind(this)
        }).send();
    }, 
    readEntidadesPlanos: function (stringWKT) {
        new Request({
            url: 'ActionServlet?wsName=CONSULTA_GRAFICA&wkt=' + stringWKT + '&srs=' + this.projectionConsultaServicios + '&idioma=es',
            ////                              data:Hash.toQueryString({
            //                                      'wsName':'CONSULTA_GRAFICA',
            //                                      'wkt':stringWKT,
            //                                      'srs':this.displayProjection,
            //                                      'idioma':'es'
            //                              }),
            onSuccess: function (response) {
                if (response && response != '' && response != ' ' && response != '\\r\\n' && !response.contains('Sin datos')) {
                    if ($('piInfoAux_Center_consulta_' + 'entidades') != null) {
                        $('piInfoAux_Center_consulta_' + 'entidades').setStyle('background-image', 'url(styles/images/accept.png)');
                    }
                    if ($('piInfoAux_Center_consulta_' + 'planos') != null) {
                        $('piInfoAux_Center_consulta_' + 'planos').setStyle('background-image', 'url(styles/images/accept.png)');
                    }
                    //LEO EL XML RECIBIDO
                    this.entidadesPlanosXML = jsIO.leerXMLFromString(response);
                }
                else {
                    $('piInfoAux_Center_consulta_' + 'entidades').setStyle('background-image', 'url(styles/images/delete.png)');
                    $('piInfoAux_Center_consulta_' + 'planos').setStyle('background-image', 'url(styles/images/delete.png)');
                }
            } .bind(this),
            onFailure: function (response) {
                this.entidadesPlanosXML = null;
                $('piInfoAux_Center_consulta_' + 'entidades').setStyle('background-image', 'url(styles/images/delete.png)');
                $('piInfoAux_Center_consulta_' + 'planos').setStyle('background-image', 'url(styles/images/delete.png)');
            } .bind(this)
        }).send();
    },
    loadVentanaEntidades: function (lonlat) {

        var respuesta = this.entidadesPlanosXML.getElementsByTagName('RESPUESTA')[0];
        if (respuesta) {
            //OBTENGO LA SITUACION
            var situacion = respuesta.getElementsByTagName('SITUACION');
            //OBTENGO LAS ENTIDADES
            var tagEntidades = respuesta.getElementsByTagName('ENTIDADES');
            if (tagEntidades && tagEntidades[0].childNodes.length > 0) {
                //GENERO LA VENTANA
                var ventana = new JobWindow(jsIO.cargarTextoSegunIdioma('ENTIDADES'), false, 500, 400, 200, 200, true, true);
                //GENERO EL BOTON PARA VER EL PDF
                new Element('div', {
                    'id': 'btnVerPDF',
                    'class': 'jwBtnBotonera',
                    'html': "<div id=lblBtnVerPDF>" + jsIO.cargarTextoSegunIdioma('VER FICHA URBANISTICA') + "</div>",
                    'events': {
                        'click': function () {
                            //OBTENGO EL ID DE LA ENTIDAD SELECCIONADA
                            if (visor.treeEntidades.selected && visor.treeEntidades.selected.id) {
                                jsBUSQUEDAS.loadFichaPorFiltroParaEntidad(visor.treeEntidades.selected.id, true, visor.treeEntidades.selected.text);
                            }
                            else {
                                alert(jsIO.cargarTextoSegunIdioma("No hay ninguna entidad seleccionada"));
                            }
                        } .bind(this)
                    }
                }).injectInside(ventana.botoneraZonaActiva);

                //GENERO EL ARBOL
                this.treeEntidades = new MooTreeControl({
                    div: ventana.zonaActiva.getProperty('id'),
                    mode: 'files',
                    grid: true
                }, {
                    text: ventana.textTitulo,
                    open: true
                });
                //OBTENGO EL AMBITO
                var ambitos = tagEntidades[0].getElementsByTagName('AMBITO');
                if (ambitos && ambitos.length > 0) {

                    for (var t = 0; t < ambitos.length; t++) {
                        //INSERTO EL AMBITO
                        var nodoAmbito = this.treeEntidades.insert({
                            text: ambitos[t].getAttribute('nombre')
                        });
                        //OBTENGO LAS CAPAS
                        var capas = ambitos[t].getElementsByTagName('CAPA');
                        if (capas && capas.length > 0) {
                            //INSERTO LAS CAPAS
                            for (var n = 0; n < capas.length; n++) {
                                var nodoCapa = nodoAmbito.insert({
                                    //id:capas[n].getAttribute('orden'),
                                    text: capas[n].getAttribute('nombre')
                                });
                                //INSERTO CADA NODO GRUPO SI ES QUE LO TIENE
                                this.loadGruposDeEntidades(capas[n], nodoCapa);
                            }
                        }
                        else {
                            alert(jsIO.cargarTextoSegunIdioma('NO HAY CAPAS EN ESTA ZONA'))
                        }
                    }
                }
                else {
                    this.loadGruposDeEntidades(tagEntidades[0], this.treeEntidades);
                }

            }
            else {
                alert(jsIO.cargarTextoSegunIdioma('NO HAY ENTIDADES EN ESTA ZONA'))
            }
        }
        else {
            alert(jsIO.cargarTextoSegunIdioma('No se han obtenido datos'));
        }

    },
    loadGruposDeEntidades: function (tagEntidades, tree) {
        var gruposEntidades = tagEntidades.getElementsByTagName('GRUPO');
        //COMPRUEBO SI HAY ENTIDADES
        if (gruposEntidades && gruposEntidades.length >= 1) {


            //RELLENO EL ARBOL

            for (var i = 0; i < gruposEntidades.length; i++) {
                //GENERO EL GRUPO
                var nodoGrupo = tree.insert({
                    text: gruposEntidades[i].getAttribute('nombre')
                });
                //COMPRUEBO SI TIENE ENTIDADES Y SI LAS TIENE LAS A ADO AL ESTE GRUPO
                var entidades = gruposEntidades[i].getElementsByTagName('ENTIDAD');
                if (entidades.length >= 1) {
                    for (var h = 0; h < entidades.length; h++) {
                        var nodoentidad = undefined;
                        nodoentidad = nodoGrupo.insert({
                            id: entidades[h].getAttribute('id'),
                            icon: '_doc',
                            openIcon: '_doc',
                            tipIcon: jsIO.cargarTextoSegunIdioma('Capturar geometria de la entidad'),
                            text: entidades[h].getAttribute('clave') + "- " + entidades[h].getAttribute('nombre')
                        });
                        nodoentidad.div.icon.setStyles({
                            'cursor': 'pointer'
                        });
                        nodoentidad.div.icon.addEvents({
                            'click': function () {
                                //CENTRO EL
                                jsIO.getServiceAsync({
                                    'wsName': 'GET_GEOMETRIA_DE_ENTIDAD',
                                    'idEntidad': this.idEntidad,
                                    'srs': visor.projectionConsultaServicios,
                                    'maxPoints': visor.maxPuntosGeometria
                                }, function (response) {
                                    if ($('loading_captura_entidad_' + this.idEntidad)) {
                                        $('loading_captura_entidad_' + this.idEntidad).dispose();
                                    }
                                    if (response != 'null') {
                                        //MUESTRO LA GEOMETRIA EN EL MAPA
                                        var formatWKT = new OpenLayers.Format.WKT();
                                        var features = formatWKT.read(response);
                                        var isArray = jsIO.isArray(features);
                                        if (visor.projectionConsultaServicios!=visor.map.getProjectionObject().projCode){
                                            features.geometry=features.geometry.transform(new OpenLayers.Projection(visor.projectionConsultaServicios),visor.map.getProjectionObject());
                                        }
                                        //SE METE EL FEATURE O FEATURES EN LA CAPA DE VECTOR
                                        //SE COMPRUEBA SI EL OBJETO WKT DEVUELTO ES UN ARRAY O NO. SI NO LO ES SE INTRODUCE EN UNO
                                        if (isArray) {
                                            visor.layerVector.addFeatures(features);
                                        }
                                        else {
                                            var arrayFeatures = new Array();
                                            arrayFeatures.push(features);
                                            visor.layerVector.addFeatures(arrayFeatures);
                                        }
                                        //CENTRO EL MAPA EN LOS BOUNDS DE LOS FEATURES
                                        visor.map.zoomToExtent(features.geometry.getBounds(), false);
                                    }
                                    else {
                                        alert(jsIO.cargarTextoSegunIdioma("La geometria solicitada tiene mas vertices de los permitidos"));
                                    }
                                } .bind({
                                    idEntidad: this.idEntidad
                                }), function (error) {

                                    });
                                new Element('div', {
                                    'id': 'loading_captura_entidad_' + this.idEntidad,
                                    'class': 'loading_captura',
                                    'style': 'float:right;height:20px'
                                }).injectInside(this.iconDiv)

                                new FaceTip({
                                    'els': $$('.mooTree_img')
                                });
                            } .bind({
                                idEntidad: entidades[h].getAttribute('id'), 
                                iconDiv: nodoentidad.div.icon
                            })
                        });
                        //COMPRUEBO SI TIENE ADSCRIPCIONES Y SI LAS TIENE LAS A ADO AL ESTE GRUPO
                        var adscripciones = entidades[h].getElementsByTagName('ADSCRIPCION');
                        if (adscripciones.length >= 1) {
                            for (var ad = 0; ad < adscripciones.length; ad++) {
                                if (adscripciones[ad].getAttribute('origen') != null) {
                                    nodoentidad.insert({
                                        //id:entidades[h].getAttribute('id'),
                                        text: adscripciones[ad].getAttribute('tipo') + ": " + adscripciones[ad].getAttribute('origen') + " (" + jsIO.cargarTextoSegunIdioma('ORIGEN') + ")"
                                    });
                                }
                                if (adscripciones[ad].getAttribute('destino') != null) {
                                    nodoentidad.insert({
                                        //id:entidades[h].getAttribute('id'),
                                        text: adscripciones[ad].getAttribute('tipo') + ": " + adscripciones[ad].getAttribute('destino') + " (" + jsIO.cargarTextoSegunIdioma('DESTINO') + ")"
                                    });
                                }
                            }
                        }
                    }
                }
            }

        //MUESTRO EL ARBOL
        }
        else {
            alert(jsIO.cargarTextoSegunIdioma('NO HAY GRUPOS DE ENTIDADES EN ESTA ZONA'))
        }
    },
    loadVentanaPlanos: function (lonlat) {
        var respuesta =null;
        if (this.entidadesPlanosXML){
            respuesta = this.entidadesPlanosXML.getElementsByTagName('RESPUESTA')[0];
        }else if (this.entidadesPlanosXML_Extend){
            respuesta = this.entidadesPlanosXML_Extend.getElementsByTagName('RESPUESTA')[0];
        }
        if (respuesta) {
            //OBTENGO LA SITUACION
            var situacion = respuesta.getElementsByTagName('SITUACION');
            //OBTENGO LAS ENTIDADES
            var tagPlanos = respuesta.getElementsByTagName('PLANOS');
            if (tagPlanos && tagPlanos[0].getAttribute('count') != '0') {
                //GENERO LA VENTANA
                var ventana = new JobWindow(jsIO.cargarTextoSegunIdioma('PLANOS'), false, 500, 400, 200, 200, true, true);
                //GENERO EL BOTON PARA VER EL PDF
                new Element('div', {
                    'id': 'btnVerPLANO',
                    'class': 'jwBtnBotonera',
                    'html': jsIO.cargarTextoSegunIdioma('VER PLANO'),
                    'events': {
                        'click': function () {
                            if (this.treePlanos.selected) {
                                if (this.treePlanos.selected.id != '-' && this.treePlanos.selected.id != null) {
                                    new Request({
                                        url: 'ActionServlet?wsName=CREAR_WMS&idHoja=' + this.treePlanos.selected.id,
                                        onSuccess: function (response) {
                                            if (response && response != '' && response != ' ' && response != '\\r\\n' && !response.contains('Sin datos')) {
                                                var nombrePlano = this.treePlanos.selected.text;
                                                if (nombrePlano.length > 50) {
                                                    nombrePlano = nombrePlano.substr(0, 50) + '...';
                                                }
                                                //nombre,url,layers,transparent,format,singleTile,visibility,standard
                                                this.crearCapaWMS(nombrePlano, response, "", true, "image/png", false, true, true);
                                            }
                                            else {
                                                alert(jsIO.cargarTextoSegunIdioma('Error creando servicio'));
                                            }
                                        } .bind(this),
                                        onFailure: function (response) {
                                            this.entidadesPlanosXML = null;
                                            this.entidadesPlanosXML_Extend=null;
                                            $('piInfoAux_Center_consulta_' + 'entidades').setStyle('background-image', 'url(styles/images/delete.png)');
                                            $('piInfoAux_Center_consulta_' + 'planos').setStyle('background-image', 'url(styles/images/delete.png)');
                                        } .bind(this)
                                    }).send();

                                    //LA A ADO AL ARBOL
                                    var nombrePlano = this.treePlanos.selected.text;
                                    if (nombrePlano.length > 50) {
                                        nombrePlano = nombrePlano.substr(0, 50) + '...';
                                    }
                                    this.treeServicios.insert({
                                        text: nombrePlano,
                                        data: {
                                            type: 'WMS', 
                                            typeButton: 'delete', 
                                            idHoja: this.treePlanos.selected.id, 
                                            layers: 'Plano' + this.treePlanos.selected.id
                                        }
                                    });
                                    this.actualizarZIndexMarkers();
                                }
                                else {
                                    alert(jsIO.cargarTextoSegunIdioma("DEBE SELECCIONAR UN PLANO"))
                                }

                            }
                            else {
                                alert(jsIO.cargarTextoSegunIdioma('Por favor, seleccione un archivo del arbol'));
                            }
                        } .bind(this)
                    }
                }).injectInside(ventana.botoneraZonaActiva);
                //GENERO EL ARBOL
                this.treePlanos = new MooTreeControl({
                    div: ventana.zonaActiva.getProperty('id'),
                    mode: 'files',
                    grid: true
                }, {
                    text: ventana.textTitulo,
                    open: true
                });
                //OBTENGO LOS PLANES
                var planes = tagPlanos[0].getElementsByTagName('PLAN');
                //RECORRO LOS PLANES
                for (var i = 0; i < planes.length; i++) {
                    //A ADO LA CARPETA DE PLAN
                    var nodoPlan = this.treePlanos.insert({
                        id: '-', //planes[i].getAttribute('id'),
                        text: planes[i].getAttribute('nombre')
                    });
                    //COMPRUEBO SI TIENE TRAMITES Y SI TIENE LOS A ADO A ESTE PLANO
                    var tramites = planes[i].getElementsByTagName('TRAMITE');
                    if (tramites && tramites.length) {
                        for (var h = 0; h < tramites.length; h++) {
                            //A ADO EL TRAMITE
                            var nodoTramites = nodoPlan.insert({
                                id: '-', //tramites[h].getAttribute('id'),
                                text: tramites[h].getAttribute('nombre')
                            });
                            //COMPRUEBO SI TIENE GRUPOS Y SI TIENE LOS A ADO AL TRAMITE
                            var grupos = tramites[h].getElementsByTagName('GRUPO');
                            if (grupos && grupos.length) {
                                for (var j = 0; j < grupos.length; j++) {
                                    //A ADO EL GRUPO
                                    var nodoGrupos = nodoTramites.insert({
                                        id: '-', //grupos[j].getAttribute('id'),
                                        text: grupos[j].getAttribute('nombre')
                                    });
                                    //COMPRUEBO SI TIENE PLANOS Y SI TIENE LOS A ADO AL GRUPO
                                    var planos = grupos[j].getElementsByTagName('PLANO');
                                    if (planos && planos.length) {
                                        for (var k = 0; k < planos.length; k++) {
                                            //A ADO EL PLANO
                                            nodoGrupos.insert({
                                                id: planos[k].getAttribute('idDoc'),
                                                hoja: planos[k].getAttribute('hoja'),
                                                text: planos[k].getAttribute('nombre') + ' - Hoja ' + planos[k].getAttribute('hoja')
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                alert(jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO PLANOS EN ESTA ZONA'))
            }
        }
        else {
            alert(jsIO.cargarTextoSegunIdioma('No se han obtenido datos'));
        }
    },
    addLittlePopupToMap: function (punto, texto, clearMarkers) {
        if (this.botoneraPrincipal.zonaActivaAux) {
            this.botoneraPrincipal.zonaActivaAux.set('html', texto);
        }

    ////        if(this.layerMarkers)
    ////        {
    ////            //BORRO EL RESTO DE MARKERS SI ES NECESARIO
    ////                if(clearMarkers)
    ////                {
    ////                    this.removeMarkers();
    ////                }
    ////            //GENERO LOS PARAMETROS BASICOS DEL MARKER
    ////                var size = new OpenLayers.Size(32,32);
    ////                var offset = new OpenLayers.Pixel(-3, -28);
    ////                var icon = new OpenLayers.Icon('styles/images/pin.png',size,offset);
    ////            //A ADO EL PROPIO MARKER
    ////                var markerLabel=new OpenLayers.Marker.Label(punto,icon,texto,{
    ////                        'labelClass':'littlePopupMarkerLabelClass'
    ////                });
    ////                this.layerMarkers.addMarker(markerLabel);
    ////            //REDEFINO EL MARKER
    //////                if(texto && texto!='')
    //////                {
    //////                    redefinirMarkerLabel(markerLabel,"littlePopupMarkerLabelClass");
    //////                }
    ////        }
    },
    addMarkerToMap: function (punto, texto, clearMarkers) {
        if (this.layerMarkers) {
            //BORRO EL RESTO DE MARKERS SI ES NECESARIO
            if (clearMarkers) {
                this.removeMarkers();
            }
            //GENERO LOS PARAMETROS BASICOS DEL MARKER
            var size = new OpenLayers.Size(32, 32);
            //var offset = new OpenLayers.Pixel(4, -37);
            var offset = new OpenLayers.Pixel(0, -32);
            var icon = new OpenLayers.Icon('styles/images/pin.png', size, offset);
            //A ADO EL PROPIO MARKER
            var markerLabel = new OpenLayers.Marker.Label(punto, icon, texto);
            this.layerMarkers.addMarker(markerLabel);
            //REDEFINO EL MARKER
            if (texto && texto != '') {
                redefinirMarkerLabel(markerLabel, "enlaceMarkerLabelClass");
            }
        }
    },
    removeMarkers: function () {
        bug.log(this.layerMarkers);
        if (this.layerMarkers) {
            this.layerMarkers.clearMarkers();
        }
    },
    checkIdemPos: function (lonlat, pixel) {
        setTimeout(function () {

            if (this.posAnt && this.posAnt.lon == this.visor.posActual.lon && this.posAnt.lat == this.visor.posActual.lat) {
                //                bug.log("-------------------------------------",'VISOR');
                //                bug.log("SE PROCEDE A LA CONSULTA DE GEOPOSICIONAMIENTO INVERSO.",'VISOR');
                //                bug.log("POSICION ANTERIOR: "+this.posAnt,'VISOR');
                //                bug.log("POSICION ACTUAL: "+this.visor.posActual,'VISOR');
                //                bug.log("-------------------------------------",'VISOR');
                this.visor.pie.setInfoGeoPos(this.pixel);
            }
        } .bind({
            visor: this, 
            posAnt: lonlat, 
            pixel: pixel
        }), 800);
    },
    getControl: function (id) {
        var listControls = this.map.getControlsBy('id', id)
        if (listControls && listControls.length >= 1) {
            return listControls[0];
        }
    },
    obtenerCentroide: function (stringWKT, reproject) {
        var format = new OpenLayers.Format.WKT();
        if (reproject) {
            format.externalProjection = new OpenLayers.Projection(this.map.projection);
            format.internalProjection = this.map.displayProjection;
        }
        var texto=stringWKT;
        if (typeof(texto)!='string'){
            texto=stringWKT.toString();
        }
        var features = format.read(texto);
        var centroide = features.geometry.getCentroid();
        return new OpenLayers.LonLat(centroide.x, centroide.y);

    },
    loadArbolCapas: function () {
        this.treeServicios = new MooTreeControl_Capas({
            div: 'auxCapas',
            mode: 'files',
            grid: true
        //                    onClick: function(node, state) {
        //			if(node.data.type && node.data.type!='folfer')
        //                        {
        //                            alert(node.text);
        //                        }
        //                    }
        }, {
            text: 'CAPAS',
            open: false
        });
        //(this.treeServicios.selected||this.treeServicios.root).load(this.rutaXMLServicios+'?wsName=GET_LAYERCONFIG_DE_AMBITO&idAmbito='+this.idAmbito);
        (this.treeServicios.selected || this.treeServicios.root)._loaded('', this.serviciosXML);

    },
    mostrarArbolCapas: function () {
        $('jwZonaActiva_capas').adopt($('auxCapas'));
        $('auxCapas').setStyle('visibility', 'visible');
    },
    ocultarArbolCapas: function () {
        $('auxCapas').setStyle('visibility', 'hidden');
        $(document.body).adopt($('auxCapas'));
    },
    obtenerNuevaZ: function () {
        this.zIndexCapas = this.zIndexCapas + 20;
        if (this.layerVector) {
            this.map.raiseLayer(this.layerVector, 10);
        }
        if (this.layerMarkers) {
            this.map.raiseLayer(this.layerMarkers, 11);
        }
        return this.zIndexCapas;
    },
    getLayerLegendDeCapaSiLoTiene: function (text) {
        var capaBuscada = jsIO.generarXMLvacio();
        var servicios = this.serviciosXML.getElementsByTagName('node');
        for (var i = 0; i < servicios.length; i++) {
            if (servicios[i].getAttribute('text') == text) {
                capaBuscada = servicios[i];
            }
        }
        if (capaBuscada && capaBuscada.getAttribute('layerLegend')) {
            return capaBuscada.getAttribute('layerLegend');
        }
        else {
            return capaBuscada.getAttribute('layers')
        }
    },
    generarNuevaCapaSegunNodeArbol: function (text) {
        if (visor.baseLayerGoogle == true && text == 'G-BASE') {
            if (visor.map.layers[0] && visor.map.layers[0].id == text) {
                if (visor.map.layers[0].opacity > 0) {
                    visor.map.layers[0].setOpacity(0);
                }
                else {
                    visor.map.layers[0].setOpacity(1);
                }
                return false;
            }
            else {
                return true;
            }
        }
        else {
            var layer = this.map.getLayer(jsIO.cargarTextoSegunIdioma(text));
            if (!layer) {
                var capaBuscada = jsIO.generarXMLvacio();
                var servicios = this.serviciosXML.getElementsByTagName('node');
                for (var i = 0; i < servicios.length; i++) {
                    if (servicios[i].getAttribute('text') == text) {
                        capaBuscada = servicios[i];
                    }
                }

                capaBuscada.setAttribute('visibility', 'true');
                capaBuscada.setAttribute('singleTile', false);

                this.crearCapaSegunXML(capaBuscada);
                this.actualizarZIndexMarkers();
                return true;
            }
            else {
                layer.setVisibility(!layer.getVisibility());
                //this.map.removeLayer(layer, false);
                return layer.getVisibility();
            }
            this.obtenerNuevaZ();
        }

    },
    getFeaturesFromWKT: function (wkt) {
        var wktFormat = new OpenLayers.Format.WKT();
        return wktFormat.read(wkt);
    },
    actualizarZIndexMarkers: function () {
        var layer = this.map.getLayer('MARKER');
        if (layer) {
            this.map.setLayerZIndex(layer, 999999);
        }
    },
    loadVentanaEntidadesExtend:function(lonlat,stringWKT){
        //GENERO LA VENTANA
        var respuesta=this.entidadesPlanosXML_Extend.getElementsByTagName('RESPUESTA')[0];
        var situacion='';
        if(respuesta)
        {
            //OBTENGO LA SITUACION
            situacion=respuesta.getElementsByTagName('SITUACION');
            //OBTENGO LAS ENTIDADES
            var tagEntidades=respuesta.getElementsByTagName('ENTIDADES');
            if(tagEntidades)
            {
                //GENERO LA VENTANA
                var ventana=new JobWindow(jsIO.cargarTextoSegunIdioma('FITXA'),false,620,400,200,200,true,false);
                //AÃ‘ADO EL CONTENIDO
                //ventana.zonaActiva.setStyle('background-image','url(styles/images/FICHA.png)');
                //AÃ‘ADO LA ZONA DE TABS PESTAÃ‘AS
                var zonaTabsFitxa=new Element('div',{
                    'id':'zonaTabsFitxa'
                }).injectInside(ventana.zonaActiva);
                //AÃ‘ADO EL CONTENEDOR DE INFORMACION
                var zonaContainerFitxa=new Element('div',{
                    'id':'zonaContainerFitxa'
                }).injectInside(ventana.zonaActiva);
                //AÃ‘ADO LOS TABS
                new Element('div',{
                    'id':'tabFitxaNorm',
                    'class':'tabFitxaON',
                    'html':'NORMALITZADA',
                    'events':{
                        'click':function(){
                            $('tabFitxaNorm').setProperty('class','tabFitxaON');
                            $('tabFitxaNoNorm').setProperty('class','tabFitxa');
                            this.zonaContainerFitxa.empty();
                            this.v.loadFitxaNormalizada(this.zonaContainerFitxa,this.situacion,this.tagEntidades)
                        }.bind({
                            v:this,
                            situacion:situacion[0],
                            tagEntidades:tagEntidades[0],
                            zonaContainerFitxa:zonaContainerFitxa
                        })
                    }
                }).injectInside(zonaTabsFitxa);
                new Element('div',{
                    'id':'tabFitxaNoNorm',
                    'class':'tabFitxa',
                    'html':'NO NORMALITZADA',
                    'events':{
                        'click':function(){
                            $('tabFitxaNorm').setProperty('class','tabFitxa');
                            $('tabFitxaNoNorm').setProperty('class','tabFitxaON');
                            this.zonaContainerFitxa.empty();
                            this.v.loadFitxaNoNormalizada(this.zonaContainerFitxa,this.situacion,this.tagEntidades)
                        }.bind({
                            v:this,
                            situacion:situacion[0],
                            tagEntidades:tagEntidades[0],
                            zonaContainerFitxa:zonaContainerFitxa
                        })
                    }
                }).injectInside(zonaTabsFitxa);
                //
                this.loadFitxaNormalizada(zonaContainerFitxa,situacion[0],tagEntidades[0])
            }
            else
            {
                alert('NO SE HAN ENCONTRADO ENTIDADES');
            }
        }
        else
        {
            alert('NO SE HAN ENCONTRADO RESULTADOS PARA ESTE PUNTO');
        }
    },
    loadFitxaNormalizada:function(zonaContainerFitxa,situacion,tagEntidades){
        //AÃ‘ADO LA SITUACION
        var fitxa_situacion=new Element('div',{
            'id':'fitxa_situacion'
        }).injectInside(zonaContainerFitxa);
        new Element('div',{
            'id':'fitxa_situacion_txt',
            'html':situacion.childNodes[0].nodeValue
        }).injectInside(fitxa_situacion);
        //OBTENGO LAS ENTIDADES
        //OBTENGO EL AMBITO
        var ambitos=tagEntidades.getElementsByTagName('AMBITO');
        if(ambitos && ambitos.length>0)
        {
            var tagCapas=ambitos[0].getElementsByTagName('CAPA');
            if(tagCapas && tagCapas.length>0)
            {
                for (var g = 0; g < tagCapas.length; g++) {
                    var gruposEntidades=tagCapas[g].getElementsByTagName('GRUPO');
                    //COMPRUEBO SI HAY ENTIDADES
                    if(gruposEntidades && gruposEntidades.length>=1)
                    {
                        //RELLENO EL ARBOL
                        for (var i = 0; i < gruposEntidades.length; i++) {
                            //GENERO EL GRUPO
                            new Element('div',{
                                'id':'fitxa_grupo_'+gruposEntidades[i].getAttribute('id'),
                                'class':'fitxa_grupo'
                            }).injectInside(zonaContainerFitxa);
                            new Element('div',{
                                'id':'fitxa_grupo_txt',
                                'class':'fitxa_grupo_txt',
                                'html':gruposEntidades[i].getAttribute('nombre')
                            }).injectInside($('fitxa_grupo_'+gruposEntidades[i].getAttribute('id')));
                            // var nodoGrupo=tree.insert({
                            // id:gruposEntidades[i].getAttribute('id'),
                            // text:gruposEntidades[i].getAttribute('nombre')
                            // });
                            //COMPRUEBO SI TIENE ENTIDADES Y SI LAS TIENE LAS AÃ‘ADO AL ESTE GRUPO
                            /*var listaEntidades=gruposEntidades[i].getElementsByTagName('ENTIDADES');
if(listaEntidades.length>=1)
{*/
                            var entidades = gruposEntidades[i].getElementsByTagName('ENTIDAD');
                            for (var h = 0; h < entidades.length; h++) {
                                new Element('div',{
                                    'id':'fitxa_entidad_'+entidades[h].getAttribute('id'),
                                    'class':'fitxa_entidad'
                                }).injectInside(zonaContainerFitxa);
                                new Element('div',{
                                    'id':'fitxa_entidad_txt',
                                    'class':'fitxa_entidad_txt',
                                    'html':entidades[h].getAttribute('nombre')
                                }).injectInside($('fitxa_entidad_'+entidades[h].getAttribute('id')));
                                //AÃ‘ADO LA CABECERA PARA LAS DETERMINACIONES DE LA ENTIDAD
                                new Element('div',{
                                    'id':'fitxa_entidadCab_'+entidades[h].getAttribute('id'),
                                    'class':'fitxa_entidadCab'
                                }).injectInside(zonaContainerFitxa);
                                //AÃ‘ADO EL ESPACIO PARA MOSTRAR LAS DETERMINACIONES DE LA ENTIDAD
                                //OBTENGO LAS DETERMINACIONES DE LA ENTIDAD
                                var listCondiciones=entidades[h].getElementsByTagName('CONDICIONES');
                                if(listCondiciones && listCondiciones.length>0)
                                {
                                    var condiciones=listCondiciones[0].getElementsByTagName('CONDICION');
                                    if(condiciones)
                                    {
                                        var txtValor="-";
                                        var txtObserv="-";
                                        for(var w = 0; w < condiciones.length; w++) {
                                            var det=new Element('div',{
                                                'id':'fitxa_entidadDet_'+condiciones[w].getAttribute('iddeterminacion')+"_"+Math.floor(Math.random()*1111),
                                                'class':'fitxa_entidadDet'
                                            }).injectInside(zonaContainerFitxa);
                                            //OBTENGO LOS CASOS
                                            var casos=condiciones[w].getElementsByTagName('CASOS');
                                            if(casos && casos.length>0)
                                            {
                                                var caso=casos[0].getElementsByTagName('CASO');
                                                if(caso && caso.length>0)
                                                {
                                                    var valores=caso[0].getElementsByTagName('VALORES');
                                                    if(valores && valores.length>0)
                                                    {
                                                        var valor=valores[0].getElementsByTagName('VALOR');
                                                        if(valor && valor.length>0)
                                                        {
                                                            //txtValor=valor[0].getAttribute('valor');
                                                            txtValor=valor[0].getAttribute('valor_normalizado');
                                                        //OBTENGO LAS OBSERVACIONES ACR: Las quito
                                                        ////// var regEsps=valor[0].getElementsByTagName('REGIMENES-ESPECIFICOS');
                                                        ////// if(regEsps && regEsps.length>0)
                                                        ////// {
                                                        ////// var reg=regEsps[0].getElementsByTagName('REGIMEN-ESPECIFICO');
                                                        ////// if(reg && reg.length>0)
                                                        ////// {
                                                        ////// if(reg[0].childNodes[0])
                                                        ////// {
                                                        ////// txtObserv=reg[0].childNodes[0].nodeValue;
                                                        ////// }
                                                        ////// }
                                                        ////// }
                                                        }
                                                    }
                                                }
                                            }
                                            //OBTENGO LAS OBSERVACIONES
                                            new Element('div',{
                                                'id':'fitxa_entidadDet_nombre'+condiciones[w].getAttribute('iddeterminacion')+"_"+Math.floor(Math.random()*1111),
                                                'class':'fitxa_entidadDet_nombre',
                                                //'html':condiciones[w].getAttribute('determinacion')
                                                'html':condiciones[w].getAttribute('determinacion_normalizada')
                                            }).injectInside(det);
                                            new Element('div',{
                                                'id':'fitxa_entidadDet_valor'+condiciones[w].getAttribute('iddeterminacion')+"_"+Math.floor(Math.random()*1111),
                                                'class':'fitxa_entidadDet_valor',
                                                'html':txtValor
                                            }).injectInside(det);
                                            new Element('div',{
                                                'id':'fitxa_entidadDet_nombre'+condiciones[w].getAttribute('iddeterminacion')+"_"+Math.floor(Math.random()*1111),
                                                'class':'fitxa_entidadDet_observacion',
                                                'html':txtObserv
                                            }).injectInside(det);
                                        }
                                    }
                                }
                                else
                                {
                                    new Element('div',{
                                        'id':'fitxa_entidadDet_'+entidades[h].getAttribute('id')+"_"+Math.floor(Math.random()*1111),
                                        'class':'fitxa_entidadDet'
                                    }).injectInside(zonaContainerFitxa);
                                }
                            }
                            //}
                            //FINALIZO EL GRUPO EL GRUPO
                            new Element('div',{
                                'id':'fitxa_grupoFin_'+gruposEntidades[i].getAttribute('id'),
                                'class':'fitxa_grupoFin'
                            }).injectInside(zonaContainerFitxa);
                        }
                    }
                }
            }
            else
            {
                alert('NO SE HA ENCONTRADO EL ELEMENTO CAPA')
            }
        }
        else
        {
    }
    },
    loadFitxaNoNormalizada:function(zonaContainerFitxa,situacion,tagEntidades){
        //AÃ‘ADO LA SITUACION
        var fitxa_situacion=new Element('div',{
            'id':'fitxa_situacion'
        }).injectInside(zonaContainerFitxa);
        new Element('div',{
            'id':'fitxa_situacion_txt',
            'html':situacion.childNodes[0].nodeValue
        }).injectInside(fitxa_situacion);
        //OBTENGO LAS ENTIDADES
        //OBTENGO EL AMBITO
        var ambitos=tagEntidades.getElementsByTagName('AMBITO');
        if(ambitos && ambitos.length>0)
        {
            var tagCapas=ambitos[0].getElementsByTagName('CAPA');
            if(tagCapas && tagCapas.length>0)
            {
                for (var g = 0; g < tagCapas.length; g++) {
                    var gruposEntidades=tagCapas[g].getElementsByTagName('GRUPO');
                    //COMPRUEBO SI HAY ENTIDADES
                    if(gruposEntidades && gruposEntidades.length>=1)
                    {
                        //RELLENO EL ARBOL
                        for (var i = 0; i < gruposEntidades.length; i++) {
                            //GENERO EL GRUPO
                            new Element('div',{
                                'id':'fitxa_grupo_'+gruposEntidades[i].getAttribute('id'),
                                'class':'fitxa_grupo'
                            }).injectInside(zonaContainerFitxa);
                            new Element('div',{
                                'id':'fitxa_grupo_txt',
                                'class':'fitxa_grupo_txt',
                                'html':gruposEntidades[i].getAttribute('nombre')
                            }).injectInside($('fitxa_grupo_'+gruposEntidades[i].getAttribute('id')));
                            // var nodoGrupo=tree.insert({
                            // id:gruposEntidades[i].getAttribute('id'),
                            // text:gruposEntidades[i].getAttribute('nombre')
                            // });
                            //COMPRUEBO SI TIENE ENTIDADES Y SI LAS TIENE LAS AÃ‘ADO AL ESTE GRUPO
                            /* var listaEntidades=gruposEntidades[i].getElementsByTagName('ENTIDADES');
if(listaEntidades.length>=1)
{*/
                            var entidades = gruposEntidades[i].getElementsByTagName('ENTIDAD');
                            for (var h = 0; h < entidades.length; h++) {
                                new Element('div',{
                                    'id':'fitxa_entidad_'+entidades[h].getAttribute('id'),
                                    'class':'fitxa_entidad'
                                }).injectInside(zonaContainerFitxa);
                                new Element('div',{
                                    'id':'fitxa_entidad_txt',
                                    'class':'fitxa_entidad_txt',
                                    'html':entidades[h].getAttribute('nombre')
                                }).injectInside($('fitxa_entidad_'+entidades[h].getAttribute('id')));
                                //AÃ‘ADO LA CABECERA PARA LAS DETERMINACIONES DE LA ENTIDAD
                                new Element('div',{
                                    'id':'fitxa_entidadCab_'+entidades[h].getAttribute('id'),
                                    'class':'fitxa_entidadCab'
                                }).injectInside(zonaContainerFitxa);
                                //AÃ‘ADO EL ESPACIO PARA MOSTRAR LAS DETERMINACIONES DE LA ENTIDAD
                                //OBTENGO LAS DETERMINACIONES DE LA ENTIDAD
                                var listCondiciones=entidades[h].getElementsByTagName('CONDICIONES');
                                if(listCondiciones && listCondiciones.length>0)
                                {
                                    var condiciones=listCondiciones[0].getElementsByTagName('CONDICION');
                                    if(condiciones)
                                    {
                                        var txtValor="-";
                                        var txtObserv="-";
                                        for(var w = 0; w < condiciones.length; w++) {
                                            var det=new Element('div',{
                                                'id':'fitxa_entidadDet_'+condiciones[w].getAttribute('iddeterminacion')+"_"+Math.floor(Math.random()*1111),
                                                'class':'fitxa_entidadDet'
                                            }).injectInside(zonaContainerFitxa);
                                            //OBTENGO LOS CASOS
                                            var casos=condiciones[w].getElementsByTagName('CASOS');
                                            if(casos && casos.length>0)
                                            {
                                                var caso=casos[0].getElementsByTagName('CASO');
                                                if(caso && caso.length>0)
                                                {
                                                    var valores=caso[0].getElementsByTagName('VALORES');
                                                    if(valores && valores.length>0)
                                                    {
                                                        var valor=valores[0].getElementsByTagName('VALOR');
                                                        if(valor && valor.length>0)
                                                        {
                                                            txtValor=valor[0].getAttribute('valor');
                                                            //txtValor=valor[0].getAttribute('valor_normalizado');
                                                            //OBTENGO LAS OBSERVACIONES ACR: Las quito
                                                            var regEsps=valor[0].getElementsByTagName('REGIMENES-ESPECIFICOS');
                                                            if(regEsps && regEsps.length>0)
                                                            {
                                                                var reg=regEsps[0].getElementsByTagName('REGIMEN-ESPECIFICO');
                                                                if(reg && reg.length>0)
                                                                {
                                                                    if(reg[0].childNodes[0])
                                                                    {
                                                                        txtObserv=reg[0].childNodes[0].nodeValue;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            //OBTENGO LAS OBSERVACIONES
                                            new Element('div',{
                                                'id':'fitxa_entidadDet_nombre'+condiciones[w].getAttribute('iddeterminacion')+"_"+Math.floor(Math.random()*1111),
                                                'class':'fitxa_entidadDet_nombre',
                                                //'html':condiciones[w].getAttribute('determinacion')
                                                'html':condiciones[w].getAttribute('determinacion')
                                            }).injectInside(det);
                                            new Element('div',{
                                                'id':'fitxa_entidadDet_valor'+condiciones[w].getAttribute('iddeterminacion')+"_"+Math.floor(Math.random()*1111),
                                                'class':'fitxa_entidadDet_valor',
                                                'html':txtValor
                                            }).injectInside(det);
                                            new Element('div',{
                                                'id':'fitxa_entidadDet_nombre'+condiciones[w].getAttribute('iddeterminacion')+"_"+Math.floor(Math.random()*1111),
                                                'class':'fitxa_entidadDet_observacion',
                                                'html':txtObserv
                                            }).injectInside(det);
                                        }
                                    }
                                }
                                else
                                {
                                    new Element('div',{
                                        'id':'fitxa_entidadDet_'+entidades[h].getAttribute('id')+"_"+Math.floor(Math.random()*1111),
                                        'class':'fitxa_entidadDet'
                                    }).injectInside(zonaContainerFitxa);
                                }
                            // nodoGrupo.insert({
                            // id:entidades[h].getAttribute('id'),
                            // text:entidades[h].getAttribute('clave') + "- " + entidades[h].getAttribute('nombre')
                            // });
                            }
                            //}
                            //FINALIZO EL GRUPO EL GRUPO
                            new Element('div',{
                                'id':'fitxa_grupoFin_'+gruposEntidades[i].getAttribute('id'),
                                'class':'fitxa_grupoFin'
                            }).injectInside(zonaContainerFitxa);
                        }
                    }
                }
            }
            else
            {
                alert('NO SE HA ENCONTRADO EL ELEMENTO CAPA')
            }
        }
        else
        {
    }
    }




});