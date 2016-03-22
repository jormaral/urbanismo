/*
    Document   : Pie
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/

var Pie = new Class({
    map: null,
    contenedor: null,
    zonaIzquierda: null,
    zonaCentral: null,
    zonaDerecha: null,
    opciones: ['geopos_inversa', 'coordenadas', 'area_visible', 'escala'],
    duracionMorph: 400,
    transicionMoprh: Fx.Transitions.Expo.easeOut,

    //GEOPOSICIONAMIENTO INVERSO
    zonaInfoGeoposInversa: null,
    stGeoPos: false,
    geocoder: null,
    posRatonAnterior: null,
    posRaton: null,
    //ESCALAS
    escalas: ['-', '1000', '5000', '10000', '25000', '50000', '100000', '200000', '500000', '1000000'],
    zonaInfoEscalas: null,
    stEscalas: false,
    initialize: function (map) {
        this.map=map;
        this.generarEstructuraPrincipal();
        this.agregarContenido();
    },
    generarEstructuraPrincipal: function () {
        //GENERO LA ESTRUCTURA CONTENEDORA
        this.contenedor = new Element('div', {
            'id': 'pieContenedor',
            'class': 'pieContenedor'
        }).inject($(this.map.div),'top');
        //CREO LA ZONA IZQUIERDA
        this.zonaIzquierda = new Element('div', {
            'id': 'pieContenedor_zonaIzquierda'
        }).injectInside(this.contenedor);
        //CREO LA ZONA CENTRAL
        this.zonaCentral = new Element('div', {
            'id': 'pieContenedor_zonaCentral'
        }).injectInside(this.contenedor);
        //CREO LA ZONA DERECHA
        this.zonaDerecha = new Element('div', {
            'id': 'pieContenedor_zonaDerecha'
        }).injectInside(this.contenedor);
    },
    agregarContenido: function () {
        for (var i = 0; i < this.opciones.length; i++) {
            //OBTENGO EL NOMBRE DE LA OPCION
            var nomOpcion = this.opciones[i];
            //GENERO EL ELEMENTO CORRESPONDIENTE
            new Element('div', {
                'id': 'pieOpcion_' + nomOpcion,
                'class': 'pieOpcion',
                'alt': jsIO.cargarTextoSegunIdioma(nomOpcion),
                'events': {
                    'click': function () {
                        this.pie.addEventClickSegunOpcion(this.op)
                    } .bind({
                        pie: this, 
                        op: nomOpcion
                    }),
                    'mouseover': function () {
                        this.pie.addEventMouseOverSegunOpcion(this.op)
                    } .bind({
                        pie: this, 
                        op: nomOpcion
                    }),
                    'mouseout': function () {
                        this.pie.addEventMouseOutSegunOpcion(this.op)
                    } .bind({
                        pie: this, 
                        op: nomOpcion
                    })
                }
            }).injectInside(this.zonaCentral);
            if (nomOpcion == 'geopos_inversa') {
                //GENERO EL ELEMNTO PARA LA INFORMACION ABREVIADA
                new Element('div', {
                    'id': 'pieCalle',
                    'class': 'pieCalle',
                    'style': {
                        'height': '100%',
                        'width': '100%'
                    }
                }).injectInside($('pieOpcion_' + nomOpcion))
                //GENERO EL ELEMENTO DONDE SE MUESTRA LA INFO DEL GEOPOSICIONAMIENTO INVERSO
                this.zonaInfoGeoposInversa = new Element('div', {
                    'id': 'pieZonaGeoposInversa',
                    'class': 'pieZonaGeoposInversa',
                    'styles': {
                        'position': 'absolute',
                        'left': 0,
                        'bottom': 20,
                        'width': '100%',
                        'height': 1,
                        'visibility': 'hidden',
                        'overflow': 'hidden'
                    }
                }).injectInside('pieOpcion_geopos_inversa');
                this.zonaInfoGeoposInversa.set('morph', {
                    duration: this.duracionMorph,
                    transition: this.transicionMoprh
                });
                this.setInfoGeoPos();
            }
            else if (nomOpcion == 'escala') {
        /* //GENERO EL ELEMNTO PARA LA INFORMACION ABREVIADA
                new Element('div',{
                'id':'pieEscala',
                'class':'pieEscala',
                'style':{e
                'height':'100%',
                'width':'100%'
                }
                }).injectInside($('pieOpcion_'+nomOpcion));
                //GENERO EL ELEMENTO DONDE SE MUESTRAN LAS ESCALAS
                this.zonaInfoEscalas=new Element('div',{
                'id':'pieZonaEscalas',
                'class':'pieZonaEscalas',
                'styles':{
                'position':'absolute',
                'left':0,
                'bottom':20,
                'width':'100%',
                'height':1,
                'visibility':'hidden',
                'overflow':'hidden'
                }
                }).injectInside('pieOpcion_escala');
                this.zonaInfoEscalas.set('morph',{
                duration:this.duracionMorph,
                transition: this.transicionMoprh
                });
                //this.setInfoEscalas();*/
        }
        }




    },
    addEventClickSegunOpcion: function (nomOpcion) {
        if (this.opciones.contains(nomOpcion)) {
            switch (nomOpcion) {
                case 'geopos_inversa':
                    this.togglePanelGeoPos();
                    break;
                case 'coordenadas':
                    this.activarBoton(nomOpcion);
                    break;
                case 'area_visible':
                    this.activarBoton(nomOpcion);
                    break;
                case 'escala':
                    break;
                default:
                    this.activarBoton(nomOpcion);
                    break;
            }

        }
    },
    addEventMouseOverSegunOpcion: function (nomOpcion) {
        if (this.opciones.contains(nomOpcion)) {
            switch (nomOpcion) {
                case 'geopos_inversa':
                    break;
                case 'coordenadas':
                    break;
                case 'area_visible':
                    break;
                case 'escala':
                    break;
                default:
                    break;
            }

        }
    },
    addEventMouseOutSegunOpcion: function (nomOpcion) {
        if (this.opciones.contains(nomOpcion)) {
            switch (nomOpcion) {
                case 'geopos_inversa':
                    break;
                case 'coordenadas':
                    break;
                case 'area_visible':
                    break;
                case 'escala':
                    break;
                default:
                    break;
            }

        }
    },
    activarBoton: function (nomOpcion) {
        this.apagarBotones();
    },
    apagarBotones: function () {
        for (var i = 0; i < this.opciones.length; i++) {
            var nomOpcion = this.opciones[i];
            if ($('pieOpcion_' + nomOpcion)) {
                $('pieOpcion_' + nomOpcion).setProperty('class', 'pieOpcion')
            }
        }
    },
    setEscala: function (escalaMapa) {
        $('pieOpcion_escala').set('html', jsIO.cargarTextoSegunIdioma('ESCALA APROXIMADA') + ': 1:' + Math.round(escalaMapa))
    },
    setAreaVisible: function (bounds) {
        //CALCULO EL AREA
        var area = ((Math.abs(bounds.right - bounds.left) / 1000) * (Math.abs(bounds.top - bounds.bottom) / 1000)).toFixed(2); //KM2
        //MUESTRO EL AREA
        $('pieOpcion_area_visible').set('html', jsIO.cargarTextoSegunIdioma('Area Visible') + ': ' + area + ' Km<sup>2</sup>')
        if (Browser.Engine.trident) {
            $('pieOpcion_area_visible').setStyle('top', -1);
            $('pieOpcion_area_visible').setStyle('height', 23);
        }
    },
    togglePanelGeoPos: function () {
        if (this.stGeoPos) {
            this.zonaInfoGeoposInversa.morph({
                'height': 1,
                'opacity': 0
            });
            //this.zonaInfoGeoposInversa.fade(0);
            this.stGeoPos = false;
        }
        else {
            this.zonaInfoGeoposInversa.morph({
                'height': 300,
                'opacity': 0.7
            });
            //this.zonaInfoGeoposInversa.fade(0.7);
            this.stGeoPos = true;
        }
    },
    togglePanelEscalas: function () {
        if (this.stEscalas) {
            this.zonaInfoEscalas.morph({
                'height': 1,
                'opacity': 0
            });
            //this.zonaInfoEscalas.fade(0);
            this.stEscalas = false;
        }
        else {
            this.zonaInfoEscalas.morph({
                'height': 253,
                'opacity': 0.85
            });
            //this.zonaInfoEscalas.fade(0.85);
            this.stEscalas = true;
        }
    },
    setInfoGeoPos: function (pixel) {
        if (this.map && pixel) {
            if (globalVisorPreview.baseLayerGoogle) {
                //this.getHTMLGeoPosInversa(this.map.baseLayer.getMapObjectLonLatFromMapObjectPixel(new GPoint(pixel.x,pixel.y)));
                this.getHTMLGeoPosInversa(this.map.baseLayer.getMapObjectLonLatFromMapObjectPixel(new google.maps.Point(pixel.x, pixel.y)));
            }
            else {
                this.getHTMLGeoPosInversaByCatastro(this.map.getLonLatFromPixel(pixel));
            }
        }
        else {


    }
    },
    getHTMLGeoPosInversaByCatastro: function (lonlat) {

        runServiceAsync({
            'wsName': 'CROSS-DOMAIN',
            'url': globalVisorPreview.rutaCatastroCoord + '?srs=' + this.map.projection + '&Coordenada_X=' + lonlat.lon + '&Coordenada_Y=' + lonlat.lat
        }, function (response) {
            var xml = jsIO.leerXMLFromString(response);
            if (xml) {
                var root = xml.getElementsByTagName('consulta_coordenadas_distancias');
                if (root && root[0] && root[0].getElementsByTagName('ldt') && root[0].getElementsByTagName('ldt')[0]) {
                    if (root.length > 0) {
                        if (root[0].getElementsByTagName('ldt').length > 0) {
                            if (root[0].getElementsByTagName('ldt')[0].childNodes.length > 0) {
                                var extent = this.map.getExtent();
                                var center = this.map.getCenter();
                                if (this.map.displayProjection != this.map.projection) {
                                    extent = extent.transform(new OpenLayers.Projection(this.map.projection), this.map.displayProjection);
                                    center = center.transform(new OpenLayers.Projection(this.map.projection), this.map.displayProjection);
                                }

                                var nodeEl = root[0].getElementsByTagName('ldt')[0].childNodes[0].nodeValue;

                                if ($('pieCalle')){
                                    $('pieCalle').set('html', nodeEl.substr(0, 25) + '...');
                                }
                                var htmlZona = '<b>' + root[0].getElementsByTagName('ldt')[0].childNodes[0].nodeValue + ': </b><br><br>';
                                htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('PROYECCION') + ': </b> ' + this.map.displayProjection.getCode();
                                if (this.map.displayProjection.proj.projName)
                                    htmlZona += ' ' + this.map.displayProjection.proj.projName;
                                if (this.map.displayProjection.proj.zone)
                                    htmlZona += ' ' + this.map.displayProjection.proj.zone;

                                htmlZona += '<br>';

                                if (this.map.displayProjection.proj.datumCode)
                                    htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('CODIGO') + ': </b> ' + this.map.displayProjection.proj.datumCode + '<br>';
                                if (this.map.displayProjection.proj.datumName)
                                    htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('DATUM') + ': </b> ' + this.map.displayProjection.proj.datumName + '<br>';
                                if (this.map.displayProjection.proj.ellipseName)
                                    htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('ELIPSOIDE') + ': </b> ' + this.map.displayProjection.proj.ellipseName + '<br><br>';

                                //htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('CENTRO') + ': </b> ' + (center.lon).toFixed(5) + ',' + (center.lat).toFixed(5) + '<br><br>';
                                htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('EXTENSION') + ': </b> ' + '<br>';
                                htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('Min. X') + ': </b> ' + (extent.left).toFixed(5) + '<br>';
                                htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('Min. Y') + ': </b> ' + (extent.bottom).toFixed(5) + '<br>';
                                htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('Max. X') + ': </b> ' + (extent.right).toFixed(5) + '<br>';
                                htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('Max. Y') + ': </b> ' + (extent.top).toFixed(5)
                                this.zonaInfoGeoposInversa.set('html', htmlZona);
                            }
                        }
                    }
                }
                else {
                    if ($('pieCalle')){
                        $('pieCalle').set('html', "-");
                    }
                    this.zonaInfoGeoposInversa.set('html', "-");
                }
            }
            else {
                if ($('pieCalle')){
                    $('pieCalle').set('html', "-");
                }
                this.zonaInfoGeoposInversa.set('html', "-");
            }
        } .bind(this), function (response) {
            if ($('pieCalle')){
                $('pieCalle').set('html', "-");
            }
        //this.zonaInfoGeoposInversa.set('html', "-");
        });

    },
    getHTMLGeoPosInversa: function (latlng) {

        if (this.zonaInfoGeoposInversa) {
            var result = "-";

            //if(!this.geocoder){this.geocoder=new GClientGeocoder();}
            if (!this.geocoder) {
                this.geocoder = new google.maps.Geocoder();
            }

            if (latlng) {
                //this.geocoder.getLocations(latlng,function(response){
                this.geocoder.geocode({
                    'latLng': latlng
                }, function (results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        if (results[1]) {
                            //OBTENGO LOS PARAMETROS
                            var extent = this.map.getExtent();
                            var center = this.map.getCenter();
                            if (this.map.displayProjection != this.map.projection) {
                                extent = extent.transform(new OpenLayers.Projection(this.map.projection), this.map.displayProjection);
                                center = center.transform(new OpenLayers.Projection(this.map.projection), this.map.displayProjection);
                            }

                            //OBTENGO EL OBJETO DE GEOPOSICIONAMIENTO
                            var place = results[0];
                            //DEVUELVO EL VALOR DEL FORMULARIO
                            if ($('pieCalle')){
                                $('pieCalle').set('html', (place.formatted_address).substr(0, 25) + '...');
                            }
                            var htmlZona = '<b>' + jsIO.cargarTextoSegunIdioma('DIRECCION') + ': </b><br>' + place.formatted_address + '<br><br>'
                            htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('PROYECCION') + ': </b> ' + this.map.displayProjection.getCode();
                            if (this.map.displayProjection.proj.projName)
                                htmlZona += ' ' + this.map.displayProjection.proj.projName;
                            if (this.map.displayProjection.proj.zone)
                                htmlZona += ' ' + this.map.displayProjection.proj.zone;

                            htmlZona += '<br>';
                            if (this.map.displayProjection.proj.datumCode)
                                htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('CODIGO') + ': </b> ' + this.map.displayProjection.proj.datumCode + '<br>';
                            if (this.map.displayProjection.proj.datumName)
                                htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('DATUM') + ': </b> ' + this.map.displayProjection.proj.datumName + '<br>';
                            if (this.map.displayProjection.proj.ellipseName)
                                htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('ELIPSOIDE') + ': </b> ' + this.map.displayProjection.proj.ellipseName + '<br><br>';

                            //htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('CENTRO') + ': </b> ' + (center.lon).toFixed(5) + ',' + (center.lat).toFixed(5) + '<br><br>';
                            htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('EXTENSION') + ': </b> ' + '<br>';
                            htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('Min. X') + ': </b> ' + (extent.left).toFixed(5) + '<br>';
                            htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('Min. Y') + ': </b> ' + (extent.bottom).toFixed(5) + '<br>';
                            htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('Max. X') + ': </b> ' + (extent.right).toFixed(5) + '<br>';
                            htmlZona += '<b>' + jsIO.cargarTextoSegunIdioma('Max. Y') + ': </b> ' + (extent.top).toFixed(5)
                            this.zonaInfoGeoposInversa.set('html', htmlZona);

                        }
                    }


                } .bind(this));
            }
        }
    },
    setInfoEscalas: function () {
        //A�ADO EL TEXTO
        $('pieEscala').set('html', jsIO.cargarTextoSegunIdioma('ESCALAS'));
        //GENERO LAS OPCIONES
        this.escalas.each(function (datoEscala) {
            var escala = new Element('div', {
                'id': 'pieValorEscala_' + datoEscala,
                'class': 'valorEscala',
                'html': '1/' + datoEscala,
                'events': {
                    'click': function (e) {
                        this.pie.loadEscala(this.value);
                    } .bind({
                        pie: this, 
                        value: datoEscala
                    })
                }
            }).injectInside(this.zonaInfoEscalas);
            escala.store('value', datoEscala);
        } .bind(this));
    },
    loadEscala: function (escala) {
        if (this.map) {
            switch (escala) {
                case '-':

                    break;
                case '1000':
                    this.map.zoomTo(18);
                    break;
                case '2000':
                    this.map.zoomTo(18);
                    break;
                case '5000':
                    this.map.zoomTo(17);
                    break;
                case '10000':
                    this.map.zoomTo(16);
                    break;
                case '25000':
                    this.map.zoomTo(15);
                    break;
                case '50000':
                    this.map.zoomTo(14);
                    break;
                case '100000':
                    this.map.zoomTo(13);
                    break;
                case '200000':
                    this.map.zoomTo(12);
                    break;
                case '500000':
                    this.map.zoomTo(11);
                    break;
                default:
                    this.map.zoomTo(10);
                    break;
            }
        //$('pieEscala').set('html',jsIO.cargarTextoSegunIdioma('ESCALA')+': '+ '1/'+escala);
        }
    },
    resetEscala: function () {
    //$('pieEscala').set('html',jsIO.cargarTextoSegunIdioma('ESCALAS'));
    }

});