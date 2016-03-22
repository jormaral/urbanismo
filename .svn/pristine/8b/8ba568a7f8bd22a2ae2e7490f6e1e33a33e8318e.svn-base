var previewVisor=new Class({
    map:null,
    baseLayerGoogle:false,
    proyeccionConsultaServicios:null,
    idTramite:null,
    initialize:function(divId){
        this.map = new OpenLayers.Map(divId,{
            controls:[]
        });
        this.pie = new Pie(this.map);
        this.map.events.register('moveend', this.map, function (evt) {
            this.mostrarAreaVisible();
            this.mostrarEscala();
        } .bind(this));
        this.map.events.register('zoomend', this.map, function (evt) {
            this.mostrarAreaVisible();
            this.mostrarEscala();
        } .bind(this));
        this.map.events.register('mousemove', this.map, function (evt) {
            this.posActual = this.map.getLonLatFromPixel(evt.xy);
            //COMPRUEBO SI DESPUES DE 2 SEGUNDOS EL PUNTO SIGUE SIENDO EL MISMO
            this.checkIdemPos(this.posActual, evt.xy)
        } .bind(this));
    },
    mostrarEscala: function () {
        if (this.map && this.pie) {
            this.pie.setEscala(this.map.getScale());
        }
    },
    mostrarAreaVisible: function () {
        if (this.map && this.pie) {
            this.pie.setAreaVisible(this.map.calculateBounds());
        }
    },
    addLayers:function(idTramite){
        var num = this.map.getNumLayers(); 
        for (var i = num - 1; i >= 0; i--) { 
            this.map.removeLayer(this.map.layers[i]); 
        }
        
        var capasXml=runService({
            wsName:'GET_CAPAS_VISOR'
        });

        var capas=jsIO.leerXMLFromString(capasXml);  

        var configCapaEntidades=JSON.decode(runService({
            'wsName': 'GET_WMS_SERVER_DE_ENT'
        }));
        var servicios = capas.getElementsByTagName('node');
        for (var j = 0; j < servicios.length; j++) {
            var nodo=servicios[j];
            if (nodo.getAttribute('urbrType') && nodo.getAttribute('urbrType')=="GRUPOS") {
                var grupos=runServiceJson({
                    'wsName':'GET_GRUPOS_TRAMITE',
                    'idTramite':idTramite
                });
                if (grupos){
                    for(var codigo in grupos) {
                        var nodoGrupo=capas.createElement("node");
                        nodoGrupo.setAttribute("urbr",false);
                        nodoGrupo.setAttribute("type","WMS");
                        nodoGrupo.setAttribute("text",grupos[codigo]);
                        nodoGrupo.setAttribute("standard",this.proyeccionConsultaServicios=='EPSG:4326');
                        nodoGrupo.setAttribute("visibility",false);
                        nodoGrupo.setAttribute("transparent",true);
                        nodoGrupo.setAttribute("filter",'idtramite=' + idTramite + ' and codigo=' + codigo);
                        nodoGrupo.setAttribute("url",configCapaEntidades.url);
                        nodoGrupo.setAttribute("layers",configCapaEntidades.layers);
                        nodoGrupo.setAttribute("format",configCapaEntidades.format);
                        nodo.appendChild(nodoGrupo);
                    }
                }
            }
        }
        for (var h = 0; h < servicios.length; h++) {
            var capa=servicios[h];
            switch (capa.getAttribute('type')) {
                case 'GOOGLE':
                    if (this.baseLayerGoogle) {
                        var layer = new OpenLayers.Layer.Google(
                            jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')),
                            {
                                'type': jsIO.getTipoServicioGoogle(capa.getAttribute('layers')),
                                'sphericalMercator': true,
                                'displayInLayerSwitcher': false,
                                'wrapDateLine': true,
                                'isBaseLayer': true,
                                'visibility': capa.getAttribute('visibility').test('true')
                            }
                            );
                        layer.id = capa.getAttribute('text');
                        redefinirGOOGLE_stylePane(layer);
                        this.map.addLayer(layer);
                        redefinirGOOGLE_BackgroundColor(layer);
						//PARA EVITAR LA VISTA OBLICUA A ZOOM CERCANOS :              
						layer.mapObject.setTilt(0);
                        //ACTIVO EL BOTON DE LA CABECERA CORRESPONDIENTE
                        Asset.javascript('javascriptsV2/ConsoleOptions/previewVisor/BotoneraGoogle.js', {
                            onload:function(){
                                new BotoneraGoogle(this.map);
                            }.bind(this)
                        });
                        Asset.css('styles/BotoneraGoogle.css');

                    }
                    break;
                case 'WMS':
                    var visibility = false;
                    if (capa.getAttribute('visibility') && capa.getAttribute('visibility').test('true') == true) {
                        visibility = true;
                    }
                    if (capa.getAttribute('urbr') && capa.getAttribute('urbr').test('true')){
                        layer = this.crearCapaWMS(
                            jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')),
                            configCapaEntidades.url,
                            configCapaEntidades.layers,
                            capa.getAttribute('transparent').test('true'),
                            configCapaEntidades.format,
                            false,
                            visibility,
                            capa.getAttribute('standard').test('true'),
                            "idtramite=" + idTramite + " AND idcapa=" + capa.getAttribute('idcapa'),
                            false,
                            capa.getAttribute('style')
                            );
                        layer.urbr=true;
                    }else{
                        layer = this.crearCapaWMS(
                            jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')),
                            capa.getAttribute('url'),
                            capa.getAttribute('layers'),
                            capa.getAttribute('transparent').test('true'),
                            capa.getAttribute('format'),
                            false,
                            visibility,
                            capa.getAttribute('standard').test('true'),
                            capa.getAttribute('filter'),
                            false,
                            capa.getAttribute('style')
                            );
                    }
                    this.map.addLayer(layer);
                    break;
                case 'VECTOR':
                    if (!this.layerVector) {
                        this.layerVector=new OpenLayers.Layer.Vector(jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')),{
                            'displayInLayerSwitcher': false,
                            'isBaseLayer': false,
                            'styleMap': this.regenerarStyleMap('blue')
                        });

                        this.layerVector.id = jsIO.cargarTextoSegunIdioma(capa.getAttribute('text'));
                        this.map.addLayer(this.layerVector);
                    }
                    break;
                case 'MARKERS':
                    if (!this.layerMarkers) {
                        this.layerMarkers = new OpenLayers.Layer.Markers(jsIO.cargarTextoSegunIdioma(capa.getAttribute('text')));
                        this.layerMarkers.id = jsIO.cargarTextoSegunIdioma(capa.getAttribute('text'));
                        this.map.addLayer(this.layerMarkers);
                    }
                    break;
            }
        }
        this.capas=capas;
        if (contextoMapaPreviewFicha){
            this.setContext(contextoMapaPreviewFicha);
        }
        this.loadArbolCapas();
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
    crearCapaWMS: function (nombre, url, layers, transparent, format, singleTile, visibility, standard, filtro, isBaseLayer,style) {
        if (isBaseLayer == null || isBaseLayer == undefined) {
            isBaseLayer = false;
        }
        var projeccionWMS = this.map.displayProjection;
        if (standard) {
            projeccionWMS = new OpenLayers.Projection(this.proyeccionConsultaServicios);
        }
        var layer = new OpenLayers.Layer.WMS(
            nombre,
            url,
            {
                'layers': layers,
                'transparent': transparent,
                'format': format,
                'styles': style
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
        return layer;
    },
    setPerfil:function(xml,idTramite){
        this.idTramite=idTramite;
        var nodo=xml.firstChild;
        if (nodo.nodeTypeString && nodo.nodeTypeString=="processinginstruction"){
            nodo=nodo.nextSibling;
        }
        this.rutaCatastroCoord= nodo.getAttribute('rutaCatastroCoord');
        OpenLayers.ImgPath = nodo.getAttribute('imgPath');
        OpenLayers.Util.onImageLoadErrorColor = nodo.getAttribute('onImageLoadErrorColor');
        if (nodo.getAttribute('IMAGE_RELOAD_ATTEMPTS')){
            OpenLayers.IMAGE_RELOAD_ATTEMPTS = (nodo.getAttribute('IMAGE_RELOAD_ATTEMPTS')).toInt();
        }
        OpenLayers.Lang.setCode(nodo.getAttribute('lang'));
        var proj=nodo.getAttribute('projection');
        this.proyeccionConsultaServicios=nodo.getAttribute('projectionConsultaServicios');
        this.baseLayerGoogle=nodo.getAttribute('baseLayerGoogle').test('true');
        var boundsMaxExtent=this.getBoundsFromWKT(runService({
            'wsName':'GET_EXTENSION_DE_TRAMITE',
            'idTramite':idTramite,
            'SRS':this.proyeccionConsultaServicios
        }));
        if (!boundsMaxExtent){
            var boundsMaxExtent_minX = parseFloat((nodo.getAttribute('maxExtent')).split(',')[0]);
            var boundsMaxExtent_minY = parseFloat((nodo.getAttribute('maxExtent')).split(',')[1]);
            var boundsMaxExtent_maxX = parseFloat((nodo.getAttribute('maxExtent')).split(',')[2]);
            var boundsMaxExtent_maxY = parseFloat((nodo.getAttribute('maxExtent')).split(',')[3]);
            boundsMaxExtent = new OpenLayers.Bounds(boundsMaxExtent_minX, boundsMaxExtent_minY, boundsMaxExtent_maxX, boundsMaxExtent_maxY);
        }else{
            boundsMaxExtent.transform(new OpenLayers.Projection(this.proyeccionConsultaServicios),
                new OpenLayers.Projection(proj));
        }
        var ExtensionTiles = boundsMaxExtent; 
        if (nodo.getAttribute('ExtensionTiles')){
            var ExtensionTiles_minX = parseFloat((nodo.getAttribute('ExtensionTiles')).split(',')[0]);
            var ExtensionTiles_minY = parseFloat((nodo.getAttribute('ExtensionTiles')).split(',')[1]);
            var ExtensionTiles_maxX = parseFloat((nodo.getAttribute('ExtensionTiles')).split(',')[2]);
            var ExtensionTiles_maxY = parseFloat((nodo.getAttribute('ExtensionTiles')).split(',')[3]);
            ExtensionTiles = new OpenLayers.Bounds(ExtensionTiles_minX, ExtensionTiles_minY, ExtensionTiles_maxX, ExtensionTiles_maxY);
        }
        var optionsMap = {
            'projection':proj,
            'displayProjection': new OpenLayers.Projection(nodo.getAttribute('displayProjection')),
            'units': 'm',
            'controls': [],
            'maxResolution': (nodo.getAttribute('maxResolution')).toFloat(),
            'maxExtent': ExtensionTiles,
            'restrictedExtent': boundsMaxExtent,
            'numZoomLevels': (nodo.getAttribute('numZoomLevels')).toInt(),
            'zoom': (nodo.getAttribute('zoomInicial')).toInt(),
            'theme': null, //this.imagePath,
            'allOverlays': (this.baseLayerGoogle) ? false : true
        }
        this.agregarControles()
        this.toolInfo();
        this.map.setOptions(optionsMap);
        this.map.updateSize();
        this.addLayers(idTramite);
        this.map.zoomToExtent(boundsMaxExtent);
        this.pie.setInfoGeoPos();
        //AGREGAR CONTROLES DE DIBUJO
        this.addControlesDibujo();

    },
    getBoundsFromWKT:function(WKT){
        var format=new OpenLayers.Format.WKT();
        var feature=format.read(WKT);
        if (feature!=null){
            return feature.geometry.getBounds();
        }else{
            return null;
        }
    },
    removeMarkers: function () {
        if (this.layerMarkers) {
            this.layerMarkers.clearMarkers();
        }
    },
    agregarControles: function () {
        this.map.addControl(new OpenLayers.Control.Navigation());
        this.controlZoom = new OpenLayers.Control.ZoomBox({
            alwaysZoom: true
        });
        this.controlPan = new OpenLayers.Control.DragPan({
            enableKinetic: true
        });
        this.map.addControls([this.controlZoom,this.controlPan]);
        this.navHis = new OpenLayers.Control.NavigationHistory({});
        this.map.addControl(this.navHis);
        this.map.addControl(new OpenLayers.Control.MousePosition({
            'div': $('pieOpcion_coordenadas')
        }));
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
    toolInfo:function(){
        if (!this.clickUrbr){
            this.clickUrbr = new OpenLayers.Control.MapClick({
                onClick: function (e) {
                    var puntoXY = this.map.getLonLatFromViewPortPx(this.map.getLayerPxFromViewPortPx(e.xy));
                    puntoXY = puntoXY.transform(this.map.getProjectionObject(), new OpenLayers.Projection(this.proyeccionConsultaServicios));
                    runServiceAsync({
                        wsName:'GET_INFO_PREVIEW_VISOR',
                        idTramite : this.idTramite ,
                        X : puntoXY.lon ,
                        Y : puntoXY.lat,
                        srs: this.proyeccionConsultaServicios
                    },
                    function (response) {
                        if (response == "") {
                            alert(jsIO.cargarTextoSegunIdioma("No se han localizado datos para el punto indicado"));
                        } else { 
                            var divResultados=new Element('div');
                            divResultados.id='auxResultadosPreview';
                            divResultados.setStyle('visibility','hidden');
                            divResultados.setStyle('position','absolute');
                            divResultados.inject($(this.map.div),'top');
                            
                            var btnLoad=new Element('div');
                            btnLoad.set('class', 'btnLoadPlano');
                            btnLoad.set('title', 'Cargar plano');
                            
                            var treeEntidades=new MooTreeControl({
                                div: 'auxResultadosPreview',
                                mode: 'folders',
                                treeType: 'entidades',
                                iconBar: ['javascriptsV2/mooTree2/Entidades.gif'],
                                grid: true,
                                toolbar:false,
                                onSelect: function(node, state) {
                                    if (state && node.data.idHoja){
                                        btnLoad.setStyle('visibility','visible');
                                        btnLoad.setStyle('background-image', 'url(styles/images/load.gif)');
                                    }else{
                                        btnLoad.setStyle('visibility','hidden');
                                    }
                                }
                            },{
                                text: cargarTextoSegunIdioma('Resultados'),
                                open: true,
                                progressive: false
                            });
                            var respuesta=jsIO.leerXMLFromString(response);
                            var capas = respuesta.getElementsByTagName('CAPA');
                            if (capas && capas.length > 0) {
                                var nodoEntidades = treeEntidades.insert({
                                    text: cargarTextoSegunIdioma('Entidades')
                                });
                                for (var t = 0; t < capas.length; t++) {
                                    //INSERTO LA CAPA
                                    var nodoCapa = nodoEntidades.insert({
                                        text: capas[t].getAttribute('nombre')
                                    });
                                    this.loadGruposDeEntidades(capas[t], nodoCapa);
                                }
                            }
                            
                            var documentos = respuesta.getElementsByTagName('DOCUMENTO');
                            if (documentos && documentos.length > 0) {
                                var nodoDocumentos = treeEntidades.insert({
                                    text: cargarTextoSegunIdioma('Planos')
                                });
                                for (var d = 0; d < documentos.length; d++) {
                                    var nodoDoc = nodoDocumentos.insert({
                                        text: documentos[d].getAttribute('nombre')
                                    });
                                    this.loadHojas(documentos[d], nodoDoc,btnLoad);
                                }
                            }
                            var rndmId='ventanaResultadosVisor_' + Math.random().toString().replace('.','_');
                            new MUI.Window({
                                'icon':'styles/images/info.png',
                                'id':rndmId,
                                'title':'Resultados',
                                'content':divResultados,
                                'width': 350,
                                'height': 250,
                                'maximizable':false,
                                'resizable':true,
                                'container':$('panelPreviewVisor'),
                                'resizeLimit': {
                                    'x': [300, 2500], 
                                    'y': [150, 2000]
                                },
                                'onContentLoaded':function(){
                                    btnLoad.injectTop($(rndmId + '_controls'));
                                }
                            });
                            
                            divResultados.setStyle('visibility','visible');
                            divResultados.setStyle('position','relative');
                        }
                    } .bind(this))
                } .bind(this)
            });
            this.map.addControl(this.clickUrbr);
        }
    },
    loadHojas: function (tagDocumento, tree, btnLoad) {
        var hojas = tagDocumento.getElementsByTagName('HOJA');
        if (hojas && hojas.length >= 1) {
            //RELLENO EL ARBOL
            for (var i = 0; i < hojas.length; i++) {
                var nodoHoja=tree.insert({
                    text: hojas[i].getAttribute('nombre'),
                    data: {
                        idHoja: hojas[i].getAttribute('id')
                    }
                });
                
                nodoHoja.onClick= function() {
                    btnLoad.setStyle('visibility', 'visible');
                    btnLoad.removeEvents();
                    btnLoad.addEvent('click', function() {
                        runServiceAsync({
                            wsName:'CREAR_WMS',
                            idHoja : this.idHoja
                        },
                        function (response) {
                            if (response && response != '' && response != ' ' && response != '\\r\\n' && !response.contains('Sin datos')) {
                                if (!this.visor.map.getLayer(this.nombrePlano)){
                                    this.visor.map.addLayer(this.visor.crearCapaWMS(this.nombrePlano, response, null, true, "image/png", false, true, (this.visor.baseLayerGoogle) ? true : false,null,false,null));
                                    this.visor.treeServicios.insert({
                                        text: this.nombrePlano,
                                        data: {
                                            type: 'WMS', 
                                            typeButton: 'delete', 
                                            idHoja: this.idHoja, 
                                            esPlano:true
                                        }
                                    });
                                    this.visor.actualizarZIndexMarkers();
                                }
                            }
                            else {
                                alert(jsIO.cargarTextoSegunIdioma('Error creando servicio'));
                            }
                            btnLoad.setStyle('visibility','hidden');
                        }.bind(this));
                        btnLoad.setStyle('background-image', 'url(styles/images/ajax-loader-16.gif)');
                        btnLoad.removeEvents();
                    }.bind({
                        idHoja:nodoHoja.data.idHoja,
                        nombrePlano:tagDocumento.getAttribute('nombre') + ' ' + nodoHoja.text,
                        visor:this
                    })
                    );
                }.bind(this)
            }
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
                    text: gruposEntidades[i].getAttribute('nombre'),
                    data: {
                        codigoGrupoEntidad: gruposEntidades[i].getAttribute('codigo')
                    }
                });
                //COMPRUEBO SI TIENE ENTIDADES Y SI LAS TIENE LAS A ADO AL ESTE GRUPO
                var entidades = gruposEntidades[i].getElementsByTagName('ENTIDAD');
                if (entidades.length >= 1) {
                    for (var h = 0; h < entidades.length; h++) {
                        var nodoEntidad=nodoGrupo.insert({
                            id: entidades[h].getAttribute('id'),
                            text: entidades[h].getAttribute('clave') + "- " + entidades[h].getAttribute('nombre')
                        });
                        this.loadCasos(entidades[h],nodoEntidad);
                    }
                }
            }

        //MUESTRO EL ARBOL
        }
        else {
            alert(jsIO.cargarTextoSegunIdioma('NO HAY GRUPOS DE ENTIDADES EN ESTA ZONA'))
        }
    },
    loadCasos: function (tagEntidad, nodo) {
        var condiciones = tagEntidad.getElementsByTagName('CONDICION');
        if (condiciones && condiciones.length >= 1) {
            for (var i = 0; i < condiciones.length; i++) {
                //GENERO EL GRUPO
                if (condiciones[i].getAttribute('determinacion')){
                    var nodoDeterminacion = nodo.insert({
                        text: condiciones[i].getAttribute('determinacion')
                    });
                    var casos = condiciones[i].getElementsByTagName('CASO');
                    if (casos){
                        if (casos.length == 1) {
                            this.loadValores(casos[0],nodoDeterminacion);
                        }
                        else if (casos.length > 1) {
                            for (var h = 0; h < casos.length; h++) {
                                var nodoCaso=nodoDeterminacion.insert({
                                    text: casos[h].getAttribute('nombre')
                                });
                                this.loadValores(casos[h],nodoCaso);
                            }
                        }
                    }
                }
            }
        }
    },
    loadValores: function (tagCaso, nodo) {
        var valores = tagCaso.getElementsByTagName('VALOR');
        if (valores && valores.length >= 1) {
            for (var i = 0; i < valores.length; i++) {
                var nodoValor=null;
                if (valores[i].getAttribute('valor')){
                    nodoValor=nodo.insert({
                        text: valores[i].getAttribute('valor')
                    });
                }else{
                    nodoValor=nodo.insert({
                        text: 'Valor'
                    });
                }
                this.loadRegimenesEspecificos(valores[i],nodoValor);
            }
        }
    },
    loadRegimenesEspecificos: function (tagValor, nodo) {
        var regs = tagValor.getElementsByTagName('REGIMENES-ESPECIFICOS');
        if (regs && regs.length >= 1) {
            for (var i = 0; i < regs.length; i++) {
                this.loadRegimenEspecifico(regs[i],nodo);
            }
        }
    },
    loadRegimenEspecifico: function (tagPadre, nodo) {
        var reg=tagPadre.getChildren();
        for (var h = 0; h < reg.length; h++) {
            if (reg[h].getAttribute('nombre')!=null){
                var nodoReg=nodo.insert({
                    text: reg[h].getAttribute('nombre')
                });
                nodoReg.color='#0000FF';
                nodoReg.texto=reg[h].textContent;
                this.loadRegimenEspecifico(reg[h],nodoReg);
                nodoReg.onClick= function() {
                    //if (){
                    new MUI.Modal({
                        'icon':'styles/images/text.png',
                        'title':this.text,
                        'content':'<textarea style="width: 100%;height: 100%;resize: none;">' + this.texto + '</textarea>',
                        'modalOverlayClose':false,
                        'draggable':true,
                        'width': 300,
                        'height': 250,
                        'maximizable':false,
                        'resizable':true,
                        'container':$('panelPreviewVisor')
                    });
                //}
                }
            }
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
    },
    onFeatureSelectForDelete: function (feature) {
        var selectedFeature = feature;
        if (confirm(jsIO.cargarTextoSegunIdioma('DESEA BORRAR ESTE ELEMENTO?'))) {
            this.layerVector.removeFeatures([selectedFeature]);
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
    loadArbolCapas: function () {
        if (!$('auxCapas')){
            var div=new Element('div');
            div.id='auxCapas';
            div.setStyle('display','none');
            div.setStyle('position','absolute');
            div.inject($(this.map.div),'top');
            this.treeServicios = new MooTreeControl_Capas({
                div: div,
                mode: 'files',
                grid: true
            }, {
                text: 'CAPAS',
                open: false
            },
            this);
            //(this.treeServicios.selected||this.treeServicios.root).load(this.rutaXMLServicios+'?wsName=GET_LAYERCONFIG_DE_AMBITO&idAmbito='+this.idAmbito);
            (this.treeServicios.selected || this.treeServicios.root)._loaded('', this.capas);
            div.setStyle('position','relative');
        }
    },
    generarNuevaCapaSegunNodeArbol: function (text) {
        if (this.baseLayerGoogle == true && text == 'G-BASE') {
            if (this.map.layers[0] && this.map.layers[0].id == text) {
                if (this.map.layers[0].opacity > 0) {
                    this.map.layers[0].setOpacity(0);
                }
                else {
                    this.map.layers[0].setOpacity(1);
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
    setContext:function(wmc){
        //COGEMOS SOLO LA VISIBILIDAD Y TRANSPARENCIA DE LAS CAPAS
        wmc.layersContext.each(function (layerContext) {
            var layer = this.map.getLayer(layerContext.title);
            if (layer){
                layer.setVisibility(layerContext.visibility);
                layer.setOpacity(layerContext.opacity);
            }
        }.bind(this));
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
    actualizarZIndexMarkers: function () {
        var layer = this.map.getLayer('MARKER');
        if (layer) {
            this.map.setLayerZIndex(layer, 999999);
        }
    }
});

