/*
    Document   : jsBusquedas
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/


var jsBUSQUEDAS = ({
    idElem: 'resultados',
    findProvincias: function () {

        //CARGO PROVINCIAS
        new Element('option', {
            'id': 'selecciona_provincia',
            'value': 'Cargando...',
            'html': 'Cargando...'
        }).injectInside($('select_provincia'));

        $('lblCatastro').setStyles({
            'background-image': 'url(styles/images/ajax-loader_busquedas.gif)',
            'background-repeat': 'no-repeat',
            'background-position': 'right 2px'
        });


        var response = jsIO.getServiceAsync({
            'wsName': 'GET_PROVINCIAS',
            'urlData': visor.rutaCatastroProvincias
        },
        function (response) {
            $('lblCatastro').setStyles({
                'background': 'transparent'
            });
            if (response != null && response != "") {
                //AÑADO EL EVENTO DEL COMBO
                $('select_provincia').addEvent('change', function () {
                    this.cab.findMunicipio($('select_provincia').getProperty('value'));
                } .bind({
                    cab: this
                }));

                var listaProvincias = JSON.decode(response).provincias;
                if (listaProvincias.length > 0) {
                    var elementosCoincidentes = jsIO.arrayContainsElems(listaProvincias, visor.provincia.split(";"));
                    if (elementosCoincidentes && elementosCoincidentes.length > 0) {
                        var selected = true;
                        for (var i = 0; i < elementosCoincidentes.length; i++) {
                            //RELLENO LA PROVINCIA
                            new Element('option', {
                                'id': 'selecciona_' + elementosCoincidentes[i],
                                'value': elementosCoincidentes[i],
                                'html': elementosCoincidentes[i],
                                'selected': selected
                            }).injectInside($('select_provincia'));
                            selected = false;

                        }
                        //BUSCO LOS MUNICIPIOS
                        jsBUSQUEDAS.findMunicipio(elementosCoincidentes[0]);

                    }
                    else {
                        $('selecciona_provincia').set('html', jsIO.cargarTextoSegunIdioma('Selecc. provincia'));
                        $('selecciona_provincia').setProperty('value', jsIO.cargarTextoSegunIdioma('Selecc. provincia'));


                        for (var i = 0; i < listaProvincias.length; i++) {

                            var opcion_select = new Element('option', {
                                'id': 'selecciona_' + listaProvincias[i],
                                'value': listaProvincias[i],
                                'html': listaProvincias[i]
                            }).injectInside($('select_provincia'));
                            if (listaProvincias[i].toUpperCase() == visor.provincia.toUpperCase()) {
                                opcion_select.setProperty('selected', true);
                                jsBUSQUEDAS.findMunicipio(listaProvincias[i]);
                            }
                        }
                    }

                }
                else {
                    $('selecciona_provincia').set('html', jsIO.cargarTextoSegunIdioma('Error de carga'));
                    $('selecciona_provincia').setProperty('value', jsIO.cargarTextoSegunIdioma('Error de carga'));
                }

            }
            else {
                $('selecciona_provincia').set('html', jsIO.cargarTextoSegunIdioma('Error de carga'));
                $('selecciona_provincia').setProperty('value', jsIO.cargarTextoSegunIdioma('Error de carga'));
            }
        } .bind(this),
            function (response) {
                $('lblCatastro').setStyles({
                    'background': 'transparent'
                });
                alert(jsIO.cargarTextoSegunIdioma('NO SE HAN PODIDO OBTENER LAS PROVINCIAS. ' + response));
                $('selecciona_provincia').set('html', jsIO.cargarTextoSegunIdioma('Error de carga'));
                $('selecciona_provincia').setProperty('value', jsIO.cargarTextoSegunIdioma('Error de carga'));
            });


        //Recibe las provincias en el response y las muestra
    },
    findMunicipio: function (provincia) {
        //  alert("la provincia buscada es "+ provincia);

        //PREPARO EL SELECT DE MUNICIPIOS
        $('select_municipio').empty();
        new Element('option', {
            'id': 'selecciona_municipio',
            'html': jsIO.cargarTextoSegunIdioma('Cargando...')
        }).injectInside($('select_municipio'));
        //RELLENAR MUNICIPIOS
        jsIO.getServiceAsync({
            'wsName': 'GET_MUNICIPIOS_SEGUN_PROVINCIA',
            'urlData': visor.rutaCatastroMunicipios + '?provincia=' + provincia + '&municipio=_'
        },
        function (res) {
            this.mostrarMunicipios(res)
        } .bind(this),
            function (res) {
                this.errorMunicipios(res)
            } .bind(this)
            );
    },
    mostrarMunicipios: function (response) {
        //Recibe los municipios en el response y los muestra
        if (response != null) {

            var listaMunicipios = JSON.decode(response).municipios;
            if (listaMunicipios.length > 0) {
                //CARGO EL EVENTO DEL COMBO DE MUNICIPIOS
                $('select_municipio').addEvent('change', function () {
                    visor.cabecera.loadBlockerCatastro('ALL');
                } .bind({
                    cab: this
                }));

                //MUESTRO TODOS LOS MUNICIPIOS ENCONTRADOS


                //if(!listaMunicipios.contains(visor.municipio))
                var elementosCoincidentes = jsIO.arrayContainsElems(listaMunicipios, visor.ambito.split(";"));
                if (elementosCoincidentes && elementosCoincidentes.length > 0) {
                    var selected = true;
                    for (var i = 0; i < elementosCoincidentes.length; i++) {
                        //AÑADO EL MUNICIPIO
                        new Element('option', {
                            'id': 'selecciona_' + elementosCoincidentes[i],
                            //                                        'value':visor.municipio.toUpperCase(),
                            //                                        'html':visor.municipio.toUpperCase(),
                            'value': elementosCoincidentes[i],
                            'html': elementosCoincidentes[i],
                            'selected': selected
                        }).injectInside($('select_municipio'));
                        selected = false;
                    }

                }
                else {
                    //MUEVO EL BLOQUEADOR Y MUESTRO EL TEXTO POR DEFECTO
                    visor.cabecera.loadBlockerCatastro('MUNICIPIO');
                    $('selecciona_municipio').set('html', 'Selecc. Municipio');
                    //
                    for (var i = 0; i < listaMunicipios.length; i++) {
                        new Element('option', {
                            'id': 'selecciona_' + listaMunicipios[i],
                            'value': listaMunicipios[i],
                            'html': listaMunicipios[i]
                        }).injectInside($('select_municipio'));
                    }
                }
            }
        }
    },
    errorMunicipios: function (response) {
        alert(jsIO.cargarTextoSegunIdioma('NO SE HAN PODIDO OBTENER LAS PROVINCIAS. ' + response));
    },
    findByCatastro_Direccion: function (provincia, municipio, tipo, calle, numero) {
        visor.cabecera.eventoFiltroZoom = new Array();
        visor.cabecera.eventoFiltroFicha = new Array();
        visor.cabecera.eventoFiltroCaptura = new Array();

        this.addPreloadImage();

        var response = jsIO.getService({
            'wsName': 'GET_VIAS_POR_NOMBRE',
            'urlData': 'http://ovc.catastro.meh.es/ovcservweb/OVCSWLocalizacionRC/OVCCallejero.asmx/ConsultaVia?Provincia=' + jsIO.formatoSinEspaciosParaURL(provincia) + '&Municipio=' + jsIO.formatoSinEspaciosParaURL(municipio) + '&TipoVia=' + tipo + '&NombreVia=' + jsIO.formatoSinEspaciosParaURL(calle)
        });
        if (response != null) {
            this.removePreloadImage();
            var respuesta = JSON.decode(response);
            var error = respuesta.error;
            if (error != null) {
                this.generarLineaResultadoParaFiltro('err', error, null);
            } else {
                respuesta.via.each(function (via) {
                    this.findByCatastro_DireccionExacta(provincia, municipio, tipo, via, numero);
                } .bind(this));
            }
        }
        else {
            this.removePreloadImage();
        }
        if ($('resultados').getElements('div').length == 0) {
            this.generarLineaResultadoParaFiltro('err', jsIO.cargarTextoSegunIdioma('NO SE HAN OBTENIDO RESULTADOS'), null);
        }
    },
    findByCatastro_DireccionExacta: function (provincia, municipio, tipo, calle, numero) {
        var nomVia=jsIO.formatoSinEspaciosParaURL(calle);
        //LIMITACION DE 25 CARACTERES EN EL NOMBRE DE LA VIA
//        if (nomVia.length>25){
//            nomVia=nomVia.substr(0,25);
//        }
        var response = jsIO.getService({
            'wsName': 'GET_CATASTRO_POR_DIRECCION',
            'urlData': 'http://ovc.catastro.meh.es/ovcservweb/OVCSWLocalizacionRC/OVCCallejero.asmx/ConsultaNumero?Provincia=' + jsIO.formatoSinEspaciosParaURL(provincia) + '&Municipio=' + jsIO.formatoSinEspaciosParaURL(municipio) + '&TipoVia=' + tipo + '&NomVia=' + nomVia + '&Numero=' + numero
        });
        //Recibe la referencia en el response y las muestra
        if (response != null) {

            // encodeURIcomponent(response);
            //this.removePreloadImage();

            var respuesta = JSON.decode(response);
            //var xmls = respuesta.xml;
            //alert("xml:"+xmls);

            var error = respuesta.error;
            if (error != null) {
                this.generarLineaResultadoParaFiltro('err', calle + ' ' + numero + ': ' + error, null);
            }
            if (respuesta.ref1) {
                var referencia = respuesta.ref1;
                var referencia2 = respuesta.ref2;
                var numReturn = respuesta.numeros;
                // alert ("la ref es "+ referencia+" "+referencia2);
                for (var i = 0; i < referencia.length; i++) {
                    //Introducir en una linea los dos fragmentos de referencia
                    this.generarLineaResultadoParaFiltro(i + referencia[i] + referencia2[i],
                        calle + ' ' + numReturn[i] + ' (' + referencia[i] + " " + referencia2[i] + ')',
                        function () {
                            this.centrar.centerByRefCat(provincia, municipio, this.t + ' ' + this.c + ',' + this.n, this.ref1 + this.ref2,1000)
                        } .bind({
                            centrar: this,
                            t: tipo,
                            c: calle,
                            n: numReturn[i],
                            ref1: referencia[i],
                            ref2: referencia2[i]
                        }),
                        function () {
                            var lonlat = this.centrar.getCenterByRefCat(provincia, municipio, this.t, this.c + ',', this.n + ',', this.ref1 + this.ref2);
                            if (lonlat) {
								lonlat = lonlat.transform(new OpenLayers.Projection(visor.projection), new OpenLayers.Projection(visor.projectionConsultaServicios));
                                //MUESTRO EL COMBO DE FICHAS PARA FILTROS
                                $('selectTipoFichaParaFiltro').fade(1);
                                //CARGO LA FICHA SEGUN EL TIPO DE FICHA SELECCIONADA EN EL COMBO
                                //LANZO LA FICHA CORRESPONDIENTE
                                //window.open(visor.rutaFichasBase+visor.cabecera.fichaAConsultarPorFiltro+'?X='+lonlat.lon+'&Y='+lonlat.lat+'&SRS=' + visor.projectionConsultaServicios);
                                new Request({
                                    url: 'ActionServlet?wsName=GET_ID_AMBITO&X=' + lonlat.lon + '&Y=' + lonlat.lat + '&srs=' + visor.projectionConsultaServicios,
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
                                            relocate(visor.rutaFichasBase + visor.cabecera.fichaAConsultarPorFiltro, {
                                                'X': lonlat.lon,
                                                'Y': lonlat.lat,
                                                'SRS': visor.projectionConsultaServicios,
                                                'idAmbito': response
                                            });
                                        }
                                    } .bind(this),
                                    onFailure: function (response) {
                                        alert(jsIO.cargarTextoSegunIdioma("No se han localizado datos para el punto pinchado"));
                                    } .bind(this)
                                }).send();
                                
                            }
                            else {
                                alert("No ha sido posible capturar el punto seleccionado")
                            }
                        } .bind({
                            centrar: this,
                            t: tipo,
                            c: calle,
                            n: numReturn[i],
                            ref1: referencia[i],
                            ref2: referencia2[i]
                        }),
                        function () {
                            var lonlat = this.centrar.getCenterByRefCat(provincia, municipio, this.t, this.c + ',', this.n + ',', this.ref1 + this.ref2);
                            if (lonlat) {
                                //DIBUJO EL PUNTO
                                this.centrar.dibujarPunto(lonlat, jsIO.cargarTextoSegunIdioma("La geometria seleccionada se ha anadido al mapa correctamente. Desea visualizarla?"))
                            }
                            else {
                                alert("No ha sido posible capturar el punto seleccionado")
                            }
                        } .bind({
                            centrar: this,
                            t: tipo,
                            c: calle,
                            n: numReturn[i],
                            ref1: referencia[i],
                            ref2: referencia2[i]
                        }),

                            {
                                'tiene_geom': 'POINT'
                            })
                }
            }
        }

        return;

    },
    findByCatastro_RefCat: function (provincia, municipio, refCat) {

        visor.cabecera.eventoFiltroZoom = new Array();
        visor.cabecera.eventoFiltroFicha = new Array();
        visor.cabecera.eventoFiltroCaptura = new Array();
        //alert("llego al findByCatastro_RefCat "+provincia+" e "+municipio+" e "+refCat);

        this.addPreloadImage();
        var response = jsIO.getService({
            'wsName': 'GET_CATASTRO_POR_REFERENCIA',
            'urlData': visor.rutaCatastroRefCat + '?Provincia=' + jsIO.formatoSinEspaciosParaURL(provincia) + '&Municipio=' + jsIO.formatoSinEspaciosParaURL(municipio) + '&SRS=' + visor.projectionConsultaServicios + '&RC=' + refCat
        });
        if (response != null && response!='') {

            this.removePreloadImage();

            var respuesta = JSON.decode(response);
            var error = respuesta.error;

            if (error != null) {
                this.generarLineaResultadoParaFiltro('err', error, null);
            }
            else {

                var calle = respuesta.calle;
                var num = respuesta.numero;
                var tipo = respuesta.tipo;

                var bloq = respuesta.bloque;
                var esc = respuesta.escalera;
                var pl = respuesta.planta;
                var pu = respuesta.puerta;

                var bloque = null;
                var escalera = null;
                var planta = null;
                var puerta = null;

                for (var i = 0; i < calle.length; i++) {
                    // valores que no tienen porqué aparecer
                    /*if ((bloq[i]) != "") bloque = 'bloque ' + bloq[i] + ' '; else bloque = '';
                    if ((esc[i]) != "") escalera = 'escalera ' + esc[i] + ' '; else escalera = '';
                    if ((pl[i]) != "") planta = 'planta ' + pl[i] + ' '; else planta = '';
                    if ((pu[i]) != "") puerta = 'puerta ' + pu[i] + ' '; else puerta = '';*/

                    this.generarLineaResultadoParaFiltro(i,
                        jsIO.cargarTextoSegunIdioma('La direccion es') + ' ' + calle[i],// + ' ' + num[i] + ' ' + bloque + escalera + planta + puerta,
                        function () {
                            this.centrar.centerByRefCat(provincia, municipio, this.c, refCat,5000)
                        } .bind({
                            centrar: this,
                            c: calle[i]
                        }),
                        function () {
                            var lonlat = this.centrar.getCenterByRefCat(provincia, municipio, '', '', '', refCat);
                            if (lonlat) {
								lonlat = lonlat.transform(new OpenLayers.Projection(visor.projection), new OpenLayers.Projection(visor.projectionConsultaServicios));
                                //MUESTRO EL COMBO DE FICHAS PARA FILTROS
                                $('selectTipoFichaParaFiltro').fade(1);
                                //CARGO LA FICHA SEGUN EL TIPO DE FICHA SELECCIONADA EN EL COMBO
                                //LANZO LA FICHA CORRESPONDIENTE
                                //window.open(visor.rutaFichasBase+visor.cabecera.fichaAConsultarPorFiltro+'?X='+lonlat.lon+'&Y='+lonlat.lat+'&SRS=' + visor.projectionConsultaServicios);
                                new Request({
                                    url: 'ActionServlet?wsName=GET_ID_AMBITO&X=' + lonlat.lon + '&Y=' + lonlat.lat + '&srs=' + visor.projectionConsultaServicios,
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
                                            relocate(visor.rutaFichasBase + visor.cabecera.fichaAConsultarPorFiltro, {
                                                'X': lonlat.lon,
                                                'Y': lonlat.lat,
                                                'SRS': visor.projectionConsultaServicios,
                                                'idAmbito': response
                                            });
                                        }
                                    } .bind(this),
                                    onFailure: function (response) {
                                        alert(jsIO.cargarTextoSegunIdioma("No se han localizado datos para el punto pinchado"));
                                    } .bind(this)
                                }).send();
                            }
                            else {
                                alert("No ha sido posible capturar el punto seleccionado")
                            }
                        } .bind({
                            centrar: this,
                            t: tipo,
                            c: calle
                        }),
                        function () {
                            var lonlat = this.centrar.getCenterByRefCat(provincia, municipio, '', '', '', refCat);
                            if (lonlat) {
                                //DIBUJO EL PUNTO
                                this.centrar.dibujarPunto(lonlat, jsIO.cargarTextoSegunIdioma("La geometria seleccionada se ha anadido al mapa correctamente. Desea visualizarla?"));

                            }
                            else {
                                alert("No ha sido posible capturar el punto seleccionado")
                            }
                        } .bind({
                            centrar: this,
                            t: tipo,
                            c: calle
                        }),
                        {
                            'tiene_geom': 'POINT'
                        })
                }
                // function(){
                //  this.cab.findMunicipio(this.provincia)}.bind({cab:this,provincia:listaProvincias[i]})
            }
        }
    },

    findByCatastro_PolParc: function (provincia, municipio, poligono, parcela) {

        visor.cabecera.eventoFiltroZoom = new Array();
        visor.cabecera.eventoFiltroFicha = new Array();
        visor.cabecera.eventoFiltroCaptura = new Array();

        // alert("llego al findByCatastro_PolParc");
        this.addPreloadImage();
        var response = jsIO.getService({
            'wsName': 'GET_CATASTRO_POR_POL_PARC',
            'urlData': 'http://ovc.catastro.meh.es/ovcservweb/OVCSWLocalizacionRC/OVCCallejero.asmx/Consulta_DNPPP?Provincia=' + jsIO.formatoSinEspaciosParaURL(provincia) + '&Municipio=' + jsIO.formatoSinEspaciosParaURL(municipio) + '&Poligono=' + poligono + '&Parcela=' + parcela
        });
        if (response != null) {

            this.removePreloadImage();

            var dataJSON = JSON.decode(response);
            var error = dataJSON.error;

            if (error != null) {
                this.generarLineaResultadoParaFiltro('err', error, null);
            }
            else {

                var refCat = dataJSON.ref;
                var par = dataJSON.parcela;

                for (var i = 0; i < par.length; i++)
                    this.generarLineaResultadoParaFiltro('parcela',
                        par[i],
                        function () {
                            this.centrar.centerByRefCat(provincia, municipio,  this.parc,  this.ref,10000)
                        } .bind({
                            centrar: this,
                            parc: par[i] + '. ',
                            ref: refCat[i]
                        }),
                        function () {
                            var lonlat = this.jsb.getCenterByRefCat(provincia, municipio, '', this.parc, '', this.ref);
                            if (lonlat) {
								lonlat = lonlat.transform(new OpenLayers.Projection(visor.projection), new OpenLayers.Projection(visor.projectionConsultaServicios));
                                //MUESTRO EL COMBO DE FICHAS PARA FILTROS
                                $('selectTipoFichaParaFiltro').fade(1);
                                //CARGO LA FICHA SEGUN EL TIPO DE FICHA SELECCIONADA EN EL COMBO
                                //LANZO LA FICHA CORRESPONDIENTE
                                //window.open(visor.rutaFichasBase+visor.cabecera.fichaAConsultarPorFiltro+'?X='+lonlat.lon+'&Y='+lonlat.lat+'&SRS=' + visor.projectionConsultaServicios);
                                new Request({
                                    url: 'ActionServlet?wsName=GET_ID_AMBITO&X=' + lonlat.lon + '&Y=' + lonlat.lat + '&srs=' + visor.projectionConsultaServicios,
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
                                            relocate(visor.rutaFichasBase + visor.cabecera.fichaAConsultarPorFiltro, {
                                                'X': lonlat.lon,
                                                'Y': lonlat.lat,
                                                'SRS': visor.projectionConsultaServicios,
                                                'idAmbito': response
                                            });
                                        }
                                    } .bind(this),
                                    onFailure: function (response) {
                                        alert(jsIO.cargarTextoSegunIdioma("No se han localizado datos para el punto pinchado"));
                                    } .bind(this)
                                }).send();
                            }
                            else {
                                alert("No ha sido posible capturar el punto seleccionado")
                            }
                        } .bind({
                            jsb: this,
                            parc: par[i] + '. ',
                            ref: refCat[i]
                        }),
                        function () {
                            var lonlat = this.jsb.getCenterByRefCat(provincia, municipio, '', this.parc, '', this.ref);
                            if (lonlat) {
                                //DIBUJO EL PUNTO
                                this.jsb.dibujarPunto(lonlat, jsIO.cargarTextoSegunIdioma("La geometria seleccionada se ha anadido al mapa correctamente. Desea visualizarla?"))

                            }
                            else {
                                alert("No ha sido posible capturar el punto seleccionado")
                            }
                        } .bind({
                            jsb: this,
                            parc: par[i] + '. ',
                            ref: refCat[i]
                        }),

                            {
                                'tiene_geom': 'POINT'
                            }
                        )
            }
        }


    },
    findByGoogle: function (texto) {

        var boundsMap = visor.map.maxExtent.clone();

        boundsMap = boundsMap.transform(new OpenLayers.Projection(visor.projection), new OpenLayers.Projection("EPSG:4326"));

        var boundsGoogle = new google.maps.LatLngBounds(new google.maps.LatLng(boundsMap.bottom, boundsMap.left), new google.maps.LatLng(boundsMap.top, boundsMap.right));

        visor.cabecera.eventoFiltroZoom = new Array();
        visor.cabecera.eventoFiltroFicha = new Array();
        visor.cabecera.eventoFiltroCaptura = new Array();
        $('lblFiltroFicha').set('html', jsIO.cargarTextoSegunIdioma(visor.cabecera.tiposDeFichas.split(';')[0]));
        $('selectTipoFichaParaFiltro').fade(1);
        this.addPreloadImage();

        var geocoder = new google.maps.Geocoder();


        this.removePreloadImage();
        //geocoder.geocode( { 'address': texto + ',' + visor.municipio}, function(results, status) {


        geocoder.geocode({
            'address': texto + ',' + visor.ubicacionVisor,
            'bounds': boundsGoogle,
            'language': visor.Lang
        }, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                for (var i = 0; i < results.length; i++) {
                    var result = results[i];
                    var direccion = "<b>" + result.formatted_address + '</b>'; //('+result.address_components[0]+','+result.addressLines[1]+")";

                    //GENERO LA LINEA DE RESULTADO Y LOS EVENTOS DE LOS FILTROS ASOCIADOS
                    jsBUSQUEDAS.generarLineaResultadoParaFiltro(i, direccion, function () {
                        var lonlat = new OpenLayers.LonLat(this.resultado.geometry.location.lng(), this.resultado.geometry.location.lat());

                        lonlat = lonlat.transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection(visor.projection));

                        visor.map.setCenter(lonlat, visor.zoomResultadosBusquedas);
                        //visor.map.zoomTo(17);
                        visor.addMarkerToMap(lonlat, this.resultado.formatted_address, true);
                    } .bind({
                        resultado: result
                    }),
                    function () {
                        //MUESTRO EL COMBO DE FICHAS PARA FILTROS
                        $('selectTipoFichaParaFiltro').fade(1);
                        //CARGO LA FICHA SEGUN EL TIPO DE FICHA SELECCIONADA EN EL COMBO
                        //OBTENGO EL PUNTO EN LA PROYECCION CORRESPODIENTE
                        var lonlat = new OpenLayers.LonLat(this.resultado.geometry.location.lng(), this.resultado.geometry.location.lat());
                        lonlat = lonlat.transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection(visor.projectionConsultaServicios));
                        //LANZO LA FICHA CORRESPONDIENTE
                        //window.open(visor.rutaFichasBase+visor.cabecera.fichaAConsultarPorFiltro+'?X='+lonlat.lon+'&Y='+lonlat.lat+'&SRS=' + visor.projectionConsultaServicios);
                        new Request({
                            url: 'ActionServlet?wsName=GET_ID_AMBITO&X=' + lonlat.lon + '&Y=' + lonlat.lat + '&srs=' + visor.projectionConsultaServicios,
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
                                    relocate(visor.rutaFichasBase + visor.cabecera.fichaAConsultarPorFiltro, {
                                        'X': lonlat.lon,
                                        'Y': lonlat.lat,
                                        'SRS': visor.projectionConsultaServicios,
                                        'idAmbito': response
                                    });
                                }
                            } .bind(this),
                            onFailure: function (response) {
                                alert(jsIO.cargarTextoSegunIdioma("No se han localizado datos para el punto pinchado"));
                            } .bind(this)
                        }).send();
                    } .bind({
                        resultado: result
                    }),
                    function () {
                        //OBTENGO EL PUNTO DEL MAPA
                        var lonlat = new OpenLayers.LonLat(this.resultado.geometry.location.lng(), this.resultado.geometry.location.lat());
                        lonlat = lonlat.transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection(visor.projection));
                        //DIBUJO EL PUNTO
                        this.jsb.dibujarPunto(lonlat, jsIO.cargarTextoSegunIdioma("La geometria seleccionada se ha anadido al mapa correctamente. Desea visualizarla?"));

                    } .bind({
                        resultado: result,
                        jsb: this.jsb
                    }),

                    {
                        'tiene_geom': 'POINT'
                    }
                    );
                }
            }
            else {
                jsBUSQUEDAS.generarLineaResultadoParaFiltro('err', 'NO SE HAN ENCONTRADO RESULTADOS', null);
            }
        } .bind({ jsb: this }));
    },
    findByEntidadSegunPlan: function (idAmbito, texto, plan) {
        visor.cabecera.eventoFiltroZoom = new Array();
        visor.cabecera.eventoFiltroFicha = new Array();
        visor.cabecera.eventoFiltroCaptura = new Array();
        this.addPreloadImage();
        var response = jsIO.getServiceAsync({
            'wsName': 'BUSCAR_ENTIDADES_SEGUN_PLAN',
            'idAmbito': idAmbito,
            'plan': plan,
            'nombre': texto
        },
        function (response) {
            visor.cabecera.eventoFiltroZoom = new Array();
            visor.cabecera.eventoFiltroFicha = new Array();
            visor.cabecera.eventoFiltroCaptura = new Array();
            this.removePreloadImage();
            var xmlEntidades = jsIO.leerXMLFromString(response);
            if (xmlEntidades && xmlEntidades.getElementsByTagName('ENTIDADES')) {
                var entidades = xmlEntidades.getElementsByTagName('ENTIDADES')[0];
                if (entidades && entidades.getElementsByTagName('ENTIDAD') && (entidades.getElementsByTagName('ENTIDAD').length > 0)) {
                    for (var i = 0; i < entidades.getElementsByTagName('ENTIDAD').length; i++) {
                        var entidad = entidades.getElementsByTagName('ENTIDAD')[i];
                        var id = entidad.getAttribute('id');
                        var nombre = entidad.getAttribute('nombre');
                        this.generarLineaResultadoParaFiltro(id,
                            nombre + ' (' + id + ')',
                            function () {
                                if (this.tiene_geom) {
                                    var bounds = this.jsb.obtenerExtentEntidad(this.entidad.getAttribute('id'));
                                    if (bounds) {
                                        visor.map.zoomToExtent(bounds, false);
                                    }
                                }
                                else {
                                    alert(jsIO.cargarTextoSegunIdioma("Esta entidad no tiene geometría"));
                                }
                            } .bind({
                                jsb: this,
                                entidad: entidad,
                                tiene_geom: entidad.getAttribute('geo_type')
                            }),
                            function () {
                                this.jsb.loadFichaPorFiltroParaEntidad(this.entidad.getAttribute('id'), this.tiene_geom,nombre);
                            } .bind({
                                jsb: this,
                                entidad: entidad,
                                tiene_geom: entidad.getAttribute('geo_type')
                            }),
                            function () {
                                this.jsb.loadCapturaParaEntidad(this.entidad, this.tiene_geom);
                            } .bind({
                                jsb: this,
                                entidad: entidad,
                                tiene_geom: entidad.getAttribute('geo_type')
                            }),

                                {
                                    'tiene_geom': entidad.getAttribute('geo_type')
                                });
                    }
                }
                else {
                    this.generarLineaResultadoParaFiltro('err', 'NO SE HAN ENCONTRADO ENTIDADES', null);
                    //                        alert(jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO ENTIDADES'));
                }
            }
            else {
                this.generarLineaResultadoParaFiltro('err', 'NO SE HAN ENCONTRADO ENTIDADES', null);
                //                   alert(jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO ENTIDADES'));
            }

        } .bind(this),
            function (response) {
                this.removePreloadImage();
                $(this.idElem).set('html', '<br><br><center>' + jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO ENTIDADES') + '</center>')
            } .bind(this));
    },
    findByEntidad: function (idAmbito, texto) {
        visor.cabecera.eventoFiltroZoom = new Array();
        visor.cabecera.eventoFiltroFicha = new Array();
        visor.cabecera.eventoFiltroCaptura = new Array();
        this.addPreloadImage();
        var response = jsIO.getServiceAsync({
            'wsName': 'BUSCAR_ENTIDADES',
            'idAmbito': idAmbito,
            'nombre': texto
        },
        function (response) {
            this.removePreloadImage();
            var xmlEntidades = jsIO.leerXMLFromString(response);
            if (xmlEntidades && xmlEntidades.getElementsByTagName('ENTIDADES')) {
                var entidades = xmlEntidades.getElementsByTagName('ENTIDADES')[0];
                if (entidades && entidades.getElementsByTagName('ENTIDAD') && (entidades.getElementsByTagName('ENTIDAD').length > 0)) {
                    for (var i = 0; i < entidades.getElementsByTagName('ENTIDAD').length; i++) {
                        var entidad = entidades.getElementsByTagName('ENTIDAD')[i];
                        var id = entidad.getAttribute('id');
                        var nombre = entidad.getAttribute('nombre');
                        this.generarLineaResultadoParaFiltro(id,
                            nombre + ' (' + id + ')',
                            function () {
                                if (this.tiene_geom) {
                                    var bounds = this.jsb.obtenerExtentEntidad(this.entidad.getAttribute('id'));
                                    if (bounds) {
                                        visor.map.zoomToExtent(bounds, false);
                                    }
                                }
                                else {
                                    alert(jsIO.cargarTextoSegunIdioma("Esta entidad no tiene geometría"));
                                }
                            } .bind({
                                jsb: this,
                                entidad: entidad,
                                tiene_geom: entidad.getAttribute('geo_type')
                            }),
                            function () {
                                this.jsb.loadFichaPorFiltroParaEntidad(this.entidad.getAttribute('id'), this.tiene_geom,this.name);
                            } .bind({
                                jsb: this,
                                entidad: entidad,
                                tiene_geom: entidad.getAttribute('geo_type'),
                                name:nombre
                            }),
                            function () {
                                this.jsb.loadCapturaParaEntidad(this.entidad, this.tiene_geom);
                            } .bind({
                                jsb: this,
                                entidad: entidad,
                                tiene_geom: entidad.getAttribute('geo_type')
                            }),

                                {
                                    'tiene_geom': entidad.getAttribute('geo_type')
                                });
                    }
                }
                else {
                    this.generarLineaResultadoParaFiltro('err', 'NO SE HAN ENCONTRADO ENTIDADES', null);
                    //                        alert(jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO ENTIDADES'));
                }
            }
            else {
                this.generarLineaResultadoParaFiltro('err', 'NO SE HAN ENCONTRADO ENTIDADES', null);
                //                   alert(jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO ENTIDADES'));
            }

        } .bind(this),
            function (response) {
                this.removePreloadImage();
                $(this.idElem).set('html', '<br><br><center>' + jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO ENTIDADES') + '</center>')
            } .bind(this));
    },
    findByPlan: function (idAmbito, texto) {
        visor.cabecera.eventoFiltroZoom = new Array();
        visor.cabecera.eventoFiltroFicha = new Array();
        visor.cabecera.eventoFiltroCaptura = new Array();
        this.addPreloadImage();
        var response = jsIO.getServiceAsync({
            'wsName': 'BUSCAR_PLANES',
            'idAmbito': idAmbito,
            'nombre': texto
        },
        function (response) {
            this.removePreloadImage();
            var xmlPlanes = jsIO.leerXMLFromString(response);
            var planes = xmlPlanes.getElementsByTagName('PLANES');
            if (xmlPlanes && planes && planes.length > 0) {
                var planPadre = planes[0];
                var listaPlan = planPadre.getElementsByTagName('PLAN');
                if (planPadre && listaPlan && (listaPlan.length > 0)) {
                    for (var i = 0; i < listaPlan.length; i++) {
                        var plan = listaPlan[i];
                        var id = plan.getAttribute('id');
                        var nombre = plan.getAttribute('nombre');
                        this.generarLineaResultadoParaFiltro(id,
                            nombre + ' (' + id + ')',
                            function () {
                                var bounds = this.jsb.obtenerExtentPlan(this.plan.getAttribute('id'));
                                if (bounds) {
                                    visor.map.zoomToExtent(bounds, false);
                                }
                            } .bind({
                                jsb: this,
                                plan: plan
                            }),
                            function () {
                                this.jsb.loadFichaPorFiltroParaPlan(this.plan, this.tiene_geom, this.name);
                            } .bind({
                                jsb: this,
                                plan: plan,
                                tiene_geom: plan.getAttribute('geo_type'),
                                name: nombre,
                            }),
                            function () {
                                this.jsb.loadCapturaParaPlan(this.plan, this.tiene_geom);
                            } .bind({
                                jsb: this,
                                plan: plan,
                                tiene_geom: plan.getAttribute('geo_type')
                            }),

                                {
                                    'tiene_geom': plan.getAttribute('geo_type')
                                });
                    }
                }
                else {
                    this.generarLineaResultadoParaFiltro('err', 'NO SE HAN ENCONTRADO PLANES', null, null, null, {
                        'tiene_geom': 'POINT'
                    });
                    //                        alert(jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO PLANES'));
                }
            }
            else {
                this.generarLineaResultadoParaFiltro('err', 'NO SE HAN ENCONTRADO PLANES', null, null, null, {
                    'tiene_geom': 'POINT'
                });
            }

        } .bind(this),
            function (response) {
                this.removePreloadImage();
                $(this.idElem).set('html', '<br><br><center>' + jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO PLANES') + '</center>')
            } .bind(this));
    },

    loadFichaPorFiltroParaEntidad: function (idEntidad, tiene_geom,entidadQuery) {
        if (tiene_geom) {
            if (confirm(jsIO.cargarTextoSegunIdioma("La generacion de la ficha puede tardar varios minutos segun la geometria solicitada. Continuar?"))) {
                jsIO.getServiceAsync({
                    'wsName': 'GET_GEOMETRIA_DE_ENTIDAD',
                    'idEntidad': idEntidad,
                    'srs': visor.projectionConsultaServicios,
                    'maxPoints': 0
                }, function (response) {
                    if (response != 'null') {
                        //CARGO LA FICHA EN UNA VENTANA
                        //jsIO.loadWindowByPost(visor.rutaSuperficies,['WKT','SRS'],[response,visor.projectionConsultaServicios]);
                        //window.open(visor.rutaSuperficies+"?WKT="+response+"&SRS="+visor.projectionConsultaServicios);
                        relocate(visor.rutaSuperficies, {
                            'WKT': response,
                            'SRS': visor.projectionConsultaServicios,
                            'idAmbito': visor.idAmbito,
                            'entidadQuery': entidadQuery
                        });
                    }
                    else {
                        alert(jsIO.cargarTextoSegunIdioma("La geometria solicitada tiene mas vertices de los permitidos"));
                    }
                }, function (error) {
                    alert("ERROR: " + error);
                })

            }
            //window.open(this.rutaFichasBase+visor.cabecera.fichaAConsultarDeFiltro+'?X='+puntoXY.lon+'&Y='+puntoXY.lat+'&SRS=' + visor.projectionConsultaServicios);
        }
    },
    loadFichaPorFiltroParaPlan: function (plan, tiene_geom, entidadQuery) {
        if (tiene_geom) {
            if (confirm(jsIO.cargarTextoSegunIdioma("La generacion de la ficha puede tardar varios minutos segun la geometria solicitada. Continuar?"))) {
                jsIO.getServiceAsync({
                    'wsName': 'GET_GEOMETRIA_DE_PLAN',
                    'idPlan': plan.getAttribute('id'),
                    'srs': visor.projectionConsultaServicios
                }, function (response) {
                    if (response != 'null') {
                        //window.open(visor.rutaSuperficies+"?WKT="+response+"&SRS="+visor.projectionConsultaServicios);
                        relocate(visor.rutaSuperficies, {
                            'WKT': response,
                            'SRS': visor.projectionConsultaServicios,
                            'idAmbito': visor.idAmbito,
                            'entidadQuery': entidadQuery
                        });
                        //                    //CARGO LA FICHA EN UNA VENTANA
                        //                        var arrayKeys=new Array();
                        //                            arrayKeys.push('WKT');
                        //                            arrayKeys.push('SRS');
                        //                        var arrayData=new Array();
                        //                            arrayData.push(response);
                        //                            arrayData.push(visor.projectionConsultaServicios);
                        //                        jsIO.loadWindowByPost(visor.rutaSuperficies,arrayKeys,arrayData);
                    }
                    else {
                        alert(jsIO.cargarTextoSegunIdioma("La geometria solicitada tiene mas vertices de los permitidos"));
                    }
                }, function (error) {
                    alert("ERROR: " + error);
                })
            }
        }
        //window.open(this.rutaFichasBase+visor.cabecera.fichaAConsultarDeFiltro+'?X='+puntoXY.lon+'&Y='+puntoXY.lat+'&SRS=' + visor.projectionConsultaServicios);
    },
    loadCapturaParaEntidad: function (entidad, tiene_geom) {
        if (tiene_geom) {
            jsIO.getServiceAsync({
                'wsName': 'GET_GEOMETRIA_DE_ENTIDAD',
                'idEntidad': entidad.getAttribute('id'),
                'srs': visor.projectionConsultaServicios,
                'maxPoints': visor.maxPuntosGeometria
            }, function (response) {
                if ($('loading_captura')) { $('loading_captura').dispose(); }
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
                    if (confirm(jsIO.cargarTextoSegunIdioma("La geometria de la entidad seleccionada se ha anadido al mapa correctamente. Desea visualizarla?"))) {
                        visor.map.zoomToExtent(features.geometry.getBounds(), false);
                    }
                }
                else {
                    alert(jsIO.cargarTextoSegunIdioma("La geometria solicitada tiene mas vertices de los permitidos"));
                }
            }, function (error) {
                if ($('loading_captura')) { $('loading_captura').dispose(); }
            });
        }
    },
    loadCapturaParaPlan: function (plan, tiene_geom) {
        if (tiene_geom) {
            jsIO.getServiceAsync({
                'wsName': 'GET_GEOMETRIA_DE_PLAN',
                'idPlan': plan.getAttribute('id'),
                'srs': visor.projectionConsultaServicios,
                'maxPoints': visor.maxPuntosGeometria
            }, function (response) {
                if ($('loading_captura')) { $('loading_captura').dispose(); }
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
                if (confirm(jsIO.cargarTextoSegunIdioma("La geometria del plan seleccionado se ha anadido al mapa correctamente. Desea visualizarla?"))) {
                    visor.map.zoomToExtent(features.geometry.getBounds(), false);
                }
            }, function (error) {
                if ($('loading_captura')) { $('loading_captura').dispose(); }
            });
        }
    },

    formatearDireccion: function (provincia, municipio, texto) {
        var address = texto;
        if (municipio) {
            address = address + "," + municipio;
        }
        if (provincia) {
            address = address + "," + provincia;
        }
        return address;
    },
    generarLineaResultadoParaFiltro: function (id, texto, eventoZoom, eventoFicha, eventoCaptura, auxParms) {
        //ALMACENO EL EVENTO DE ZOOM
        visor.cabecera.eventoFiltroZoom.push(eventoZoom);
        visor.cabecera.eventoFiltroFicha.push(eventoFicha);
        visor.cabecera.eventoFiltroCaptura.push(eventoCaptura);

        var tiene_geom = undefined;
        if (auxParms && auxParms.tiene_geom) {
            tiene_geom = auxParms.tiene_geom;


        }
        var elem = new Element('div', {
            'id': 'lineaResultado_' + id,
            'class': 'lineaResultado',
            'html': jsIO.cargarTextoSegunIdioma(texto),
            'alt': texto,
            'rel': texto,
            'events': {
                'click': function () {
                    //SI NO ES UN PUNTO DESACTIVO EL COMBO SELECTOR DE FICHAS PARA PODER PEDIR SOLO LA FICHA POR COORDENADAS
                    var tiene_geom = $('lineaResultado_' + this.id).retrieve('tiene_geom');
                    if (tiene_geom && tiene_geom.toUpperCase() != 'POINT') {
                        //DESACTIVO EL COMBO
                        $('selectTipoFichaParaFiltro').fade(0);
                        //MUESTRO EL TIPO DE FICHA SUPERFICIE
                        $('lblFiltroFicha').set('html', jsIO.cargarTextoSegunIdioma('Ficha Superf.'));
                        //GUARDO EL TIPO DE FICHA A CONSULTAR

                    }
                    else {
                        //ACTIVO EL COMBO
                        $('selectTipoFichaParaFiltro').fade(1);
                    }

                    //DESACTIVO TODAS LAS LINEAS DE RESULTADOS
                    $$('.lineaResultadoON').each(function (item) {
                        item.setProperty('class', 'lineaResultado')
                    });
                    //ACTIVO LA LINEA DE RESULTADO CORRESPONDIENTE
                    $('lineaResultado_' + this.id).setProperty('class', 'lineaResultadoON');
                    visor.cabecera.resultadoSeleccionado = this.id;
                    //SI NO TIENE GEOMETRIA DESACTIVO EL BOTON DE FICHA
                    //ACR: Y LOS OTROS 2
                    $('selectTipoFichaParaFiltro').store('tiene_geom', $('lineaResultado_' + this.id).retrieve('tiene_geom'));
                    //if ($('lineaResultado_' + this.id).retrieve('tiene_geom')) {
                    if (tiene_geom) {
                        //$('filtroFicha').addEvent('click',eventoFicha);
                        $('filtroFicha').fade(1);
                        $('filtroZoom').fade(1);
                        $('filtroCaptura').fade(1);
                    }
                    else {
                        $('filtroFicha').set('morph', {
                            duration: 100
                        });
                        $('filtroFicha').addEvent('click', function () { });
                        $('filtroFicha').morph({
                            'opacity': 0
                        });
                        $('filtroZoom').set('morph', {
                            duration: 100
                        });
                        $('filtroZoom').addEvent('click', function () { });
                        $('filtroZoom').morph({
                            'opacity': 0
                        });
                        $('filtroCaptura').set('morph', {
                            duration: 100
                        });
                        $('filtroCaptura').addEvent('click', function () { });
                        $('filtroCaptura').morph({
                            'opacity': 0
                        });
                    }
                } .bind({
                    id: id,
                    'tiene_geom': tiene_geom
                })
            },
            'styles': {
                'visibility': 'hidden'
            }
        }).injectInside($(this.idElem));
        elem.store('id_elem', id);
        elem.store('tiene_geom', tiene_geom);
        elem.setOpacity(0);
        elem.fade(1);
    },
    generarLineaResultado: function (id, texto, evento) {
        var elem = null;
        if (evento != null) {
            elem = new Element('div', {
                'id': 'lineaResultado_' + id,
                'class': 'lineaResultado',
                'html': jsIO.cargarTextoSegunIdioma(texto),
                'alt': texto,
                'rel': texto,
                'events': {
                    'click': evento
                },
                'styles': {
                    'visibility': 'hidden'
                }
            }).injectInside($(this.idElem));
        } else {
            elem = new Element('div', {
                'id': 'lineaResultado_' + id,
                'class': 'lineaResultado',
                'html': jsIO.cargarTextoSegunIdioma(texto),
                'alt': texto,
                'rel': texto,
                'styles': {
                    'visibility': 'hidden'
                }
            }).injectInside($(this.idElem));
        }

        elem.setOpacity(0);
        elem.fade(1);

    },
    readAmbitos: function (idioma) {

        var result = jsIO.getService({
            'wsName': 'GET_AMBITOS',
            'idioma': idioma
        });
        var arrayAmbitos = new Array();
        var xmlAmbitos = jsIO.leerXMLFromString(result);
        if (xmlAmbitos && xmlAmbitos.getElementsByTagName('AMBITOS')) {
            var ambitos = xmlAmbitos.getElementsByTagName('AMBITOS')[0];
            if (ambitos) {
                for (var i = 0; i < ambitos.getElementsByTagName('AMBITO').length; i++) {
                    var ambito = ambitos.getElementsByTagName('AMBITO')[i];
                    var id = ambito.getElementsByTagName('id')[0].childNodes[0].nodeValue;
                    var nombre = ambito.getElementsByTagName('nombre')[0].childNodes[0].nodeValue;
                    arrayAmbitos.push({
                        id: id,
                        nombre: nombre
                    });
                }
            }
            else {
                this.generarLineaResultadoParaFiltro('err', 'NO SE HAN ENCONTRADO AMBITOS', null, null, null, {
                    'tiene_geom': 'POINT'
                });
                //                alert(jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO AMBITOS'));
            }
        }
        return arrayAmbitos;
    },
    addPreloadImage: function () {
        $(this.idElem).empty();
        new Element('div', {
            'id': 'preloadBusquedas',
            'class': 'preloadImage'
        }).injectInside($(this.idElem));
    },
    removePreloadImage: function () {
        $(this.idElem).empty();
    },
    obtenerExtentEntidad: function (id) {
        var bounds;
        var response = jsIO.getService({
            'wsName': 'UBICAR_ENTIDAD',
            'idEntidad': id,
            'srs': visor.projectionConsultaServicios
        });
        //TRANSFORMO LA PROJECTION DE LO RECIBIDO
        if (response == '|||' || response.contains('null')) {
            //this.generarLineaResultadoParaFiltro('err','LA ENTIDAD NO ES GRAFIABLE');
            alert(jsIO.cargarTextoSegunIdioma('LA ENTIDAD NO TIENE GEOMETRIA'));
        }
        else {
            var boundsTemp = response.split('|');
            bounds = new OpenLayers.Bounds(boundsTemp[0], boundsTemp[1], boundsTemp[2], boundsTemp[3]);
            //bounds=bounds.transform(new OpenLayers.Projection(projectionConsultaServicios),new OpenLayers.Projection(displayProjection_));
            //                        if(visor.displayProjection!=visor.projection)
            //                        {
            bounds = bounds.transform(new OpenLayers.Projection(visor.projectionConsultaServicios), new OpenLayers.Projection(visor.projection));
            //                        }

        }

        return bounds;
    },
    obtenerExtentPlan: function (id) {
        var bounds;
        var response = jsIO.getService({
            'wsName': 'UBICAR_PLAN',
            'idPlan': id,
            'srs': visor.projectionConsultaServicios
        });
        //TRANSFORMO LA PROJECTION DE LO RECIBIDO
        if (response == '|||') {
            this.generarLineaResultadoParaFiltro('err', 'EL PLAN NO ES GRAFIABLE');
            //                  alert(jsIO.cargarTextoSegunIdioma('EL PLAN NO ES GRAFIABLE'));
        }
        else {
            var boundsTemp = response.split('|');
            bounds = new OpenLayers.Bounds(boundsTemp[0], boundsTemp[1], boundsTemp[2], boundsTemp[3]);
            //bounds=bounds.transform(new OpenLayers.Projection(projectionConsultaServicios),new OpenLayers.Projection(displayProjection_));
            if (visor.projectionConsultaServicios != visor.projection) {
                bounds = bounds.transform(new OpenLayers.Projection(visor.projectionConsultaServicios), new OpenLayers.Projection(visor.projection));
            }

        }

        return bounds;
    },
    obtenerExtentAmbito: function (id) {
        var bounds;
        var response = jsIO.getService({
            'wsName': 'UBICAR_AMBITO',
            'idAmbito': id,
            'srs': visor.projectionConsultaServicios
        });
        //TRANSFORMO LA PROJECTION DE LO RECIBIDO
        if (response == '|||') {
            //                  alert(jsIO.cargarTextoSegunIdioma('EL AMBITO NO ES GRAFIABLE'));
            this.generarLineaResultadoParaFiltro('err', 'EL AMBITO NO ES GRAFIABLE');
        }
        else {
            var boundsTemp = response.split('|');
            bounds = new OpenLayers.Bounds(boundsTemp[0], boundsTemp[1], boundsTemp[2], boundsTemp[3]);
            //bounds=bounds.transform(new OpenLayers.Projection(projectionConsultaServicios),new OpenLayers.Projection(displayProjection_));
            if (visor.projectionConsultaServicios != visor.projection) {
                bounds = bounds.transform(new OpenLayers.Projection(visor.projectionConsultaServicios), new OpenLayers.Projection(visor.projection));
            }

        }

        return bounds;
    },
    findDireccion: function (address) {
        if (!visor.displayProjection) {
            visor.displayProjection = 'EPSG:900913';
        }
        //var geocoder = new GClientGeocoder();
        var geocoder = new google.maps.Geocoder();
        if (geocoder) {
            //UBICO EL MAPA EN LA QUE GOOGLE DEVUELVE POR DEFECTO
            geocoder.getLatLng(address, function (point) {
                if (!point) {
                    //                           alert(address + jsIO.cargarTextoSegunIdioma('NOT-FOUND'));
                    this.generarLineaResultadoParaFiltro('err', address + ' NOT-FOUND');
                }
                else {
                    var temp = (point.toString()).split(',');
                    var latitudLength = (temp[1].length) - 1;
                    //GENERO UN LONLAT DE OPENLAYERS
                    var lonlat = new OpenLayers.LonLat(temp[1].slice(0, latitudLength), temp[0].substr(1));
                    //TRANSFORMO EL LONLAT A LA PROJECION DEL VISOR
                    if (visor.displayProjection != visor.projection) {
                        lonlat = lonlat.transform(new OpenLayers.Projection(visor.displayProjection), new OpenLayers.Projection(visor.projection));
                    }
                    //UBICO EN EL MAPA EL PUNTO LONLAT
                    visor.map.setCenter(lonlat, visor.zoomResultadosBusquedas);
                    visor.map.zoomToScale(1000);
                    //MUESTRO EL MARKER LABEL
                    visor.addMarkerToMap(lonlat, address.toUpperCase(), true);
                }
            });
        }
        else {
            alert(geocoder);
        }
    },
    getCenterByRefCat: function (provincia, municipio, tipoVia, via, numero, refCat) {
        var stringLonLat = jsIO.getService({
            'wsName': 'CROSS-DOMAIN',
            'url': 'http://ovc.catastro.meh.es/ovcservweb/OVCSWLocalizacionRC/OVCCoordenadas.asmx/Consulta_CPMRC?' +
            "Provincia=" + jsIO.formatoSinEspaciosParaURL(provincia) +
            "&Municipio=" + jsIO.formatoSinEspaciosParaURL(municipio) +
            "&SRS=" + visor.projectionConsultaServicios +
            "&RC=" + refCat
        }
        );
        //PARSEO EL STRING
        var xmlLonLat = jsIO.leerXMLFromString(stringLonLat);
        //UBICO LA REFERENCIA CATASTRAL
        if (xmlLonLat.getElementsByTagName('xcen') && xmlLonLat.getElementsByTagName('xcen').length > 0) {
            var lon = xmlLonLat.getElementsByTagName('xcen')[0].childNodes[0].nodeValue;
            var lat = xmlLonLat.getElementsByTagName('ycen')[0].childNodes[0].nodeValue;
            var lonlat = new OpenLayers.LonLat(lon, lat)
            if (visor.projection != visor.projectionConsultaServicios) {
                lonlat = lonlat.transform(new OpenLayers.Projection(visor.projectionConsultaServicios), new OpenLayers.Projection(visor.projection));
            }
            return lonlat;
        }
        else {
            return undefined;
        }
    },
    centerByRefCat: function (provincia, municipio, calle, refCat, escala) {
        var stringLonLat = jsIO.getService({
            'wsName': 'CROSS-DOMAIN',
            'url': 'http://ovc.catastro.meh.es/ovcservweb/OVCSWLocalizacionRC/OVCCoordenadas.asmx/Consulta_CPMRC?' +
            "Provincia=" + jsIO.formatoSinEspaciosParaURL(provincia) +
            "&Municipio=" + jsIO.formatoSinEspaciosParaURL(municipio) +
            "&SRS=" + visor.projectionConsultaServicios +
            "&RC=" + refCat
        }
        );
        //PARSEO EL STRING
        var xmlLonLat = jsIO.leerXMLFromString(stringLonLat);
        //UBICO LA REFERENCIA CATASTRAL
        if (xmlLonLat.getElementsByTagName('xcen') && xmlLonLat.getElementsByTagName('xcen').length > 0) {
            var lon = xmlLonLat.getElementsByTagName('xcen')[0].childNodes[0].nodeValue;
            var lat = xmlLonLat.getElementsByTagName('ycen')[0].childNodes[0].nodeValue;
            var lonlat = new OpenLayers.LonLat(lon, lat)
            if (visor.projection != visor.projectionConsultaServicios) {
                lonlat = lonlat.transform(new OpenLayers.Projection(visor.projectionConsultaServicios), new OpenLayers.Projection(visor.projection));
            }
            visor.map.setCenter(lonlat, visor.zoomResultadosBusquedas);
            if (escala) {
                visor.map.zoomToScale(escala);
            } else {
                visor.map.zoomToScale(5000);
            }
            visor.addMarkerToMap(lonlat, calle.toUpperCase(), true);
        }
        else {
            //this.generarLineaResultadoParaFiltro('err','PROBLEMAS AL OBTENER LA LOCALIZACIÓN. INTÉNTELO DE NUEVO MÁS TARDE');
            alert(jsIO.cargarTextoSegunIdioma('PROBLEMAS AL OBTENER LA LOCALIZACION. INTENTELO DE NUEVO MAS TARDE'));
        }
    },
    dibujarPunto: function (lonlat, texto) {
        var point = new OpenLayers.Geometry.Point(lonlat.lon, lonlat.lat);
        var style = {
            pointRadius: 4,
            graphicName: "circle",
            fillColor: "#0000ff",
            fillOpacity: 0.9,
            strokeWidth: 1,
            strokeOpacity: 1,
            strokeColor: "#ffffff",
            cursor: 'pointer'
        }

        var circleFeature = new OpenLayers.Feature.Vector(OpenLayers.Geometry.Polygon.createRegularPolygon(point, 5, 3, 0), null, style);
        visor.layerVector.addFeatures([circleFeature]);
        if (confirm(texto)) {
            visor.map.setCenter(lonlat, 16);
        }
        if ($('loading_captura')) { $('loading_captura').dispose(); }
    }
});