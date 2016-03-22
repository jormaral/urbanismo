/*
    Document   : BotoneraPrincipal
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/


var BotoneraPrincipal = new Class({
    contenedor: null,
    zonaIzquierda: null,
    zonaCentral: null,
    zonaDerecha: null,
    panelInformativo: null,
    panelInformativoAux: null,
    zonaActivaAux: null,
    opciones: ['hand', 'print', 'zoomIn_area', 'zoomOut_area', 'forward', 'previous', /*'measure',*/'paint', 'info'],
    opcionesPaint: ['PUNTO', 'LINEA', 'POLIGONO', 'POLIG_REGULAR', 'MODIFY', 'SELECT', 'LOAD', 'SAVE', 'DELETE'],
    btnOpcion: '',
    bgColor: '#004A85',
    anchoBoton: 30,
    duracionTransicion: 200,
    map: null,
    initialize: function () {
        this.generarEstructuraPrincipal();
        this.agregarContenido();
        //this.addHoverEventToContenedor();
    },
    generarEstructuraPrincipal: function () {
        //GENERO LA ESTRUCTURA CONTENEDORA
        this.contenedor = new Element('div', {
            'id': 'bpContenedor',
            'class': 'bpContenedor'
        }).injectInside($(document.body));
        //CREO LA ZONA IZQUIERDA
        this.zonaIzquierda = new Element('div', {
            'id': 'bpContenedor_zonaIzquierda'
        }).injectInside(this.contenedor);
        //CREO LA ZONA CENTRAL
        this.zonaCentral = new Element('div', {
            'id': 'bpContenedor_zonaCentral'
        }).injectInside(this.contenedor);
        //CREO LA ZONA DERECHA
        this.zonaDerecha = new Element('div', {
            'id': 'bpContenedor_zonaDerecha'
        }).injectInside(this.contenedor);
        //alert(this.zonaCentral.getWidth());



    },
    agregarContenido: function () {
        var cont = this.anchoBoton;
        var firstTime = true;
        for (var i = 0; i < this.opciones.length; i++) {
            //OBTENGO EL NOMBRE DE LA OPCION
            var nomOpcion = this.opciones[i];
            //GENERO EL ELEMENTO CORRESPONDIENTE
            bug.log(nomOpcion, 'BOTONERA_PRINCIPAL');
            var opc = new Element('div', {
                'id': 'bpOpcion_' + nomOpcion,
                'class': 'bpOpcion tipped',
                'tip': jsIO.cargarTextoSegunIdioma(nomOpcion)
                //'html':jsIO.cargarTextoSegunIdioma(nomOpcion),
            }).injectInside(this.zonaCentral);
            //if(nomOpcion!='forward' && nomOpcion!='previous'){
            opc.addEvents({
                'click': function () {this.bp.addEventClickSegunOpcion(this.op)} .bind({bp: this, op: nomOpcion}),
                'mouseover': function () {this.bp.addEventMouseOverSegunOpcion(this.op, this.posBG)} .bind({bp: this, op: nomOpcion, posBG: cont}),
                'mouseout': function () {this.bp.addEventMouseOutSegunOpcion(this.op, this.posBG)} .bind({bp: this, op: nomOpcion, posBG: cont})
            });
            //}

            if (firstTime) {
                cont = opc.getWidth();
                this.anchoBoton = cont;
                firstTime = false;
            }
            opc.setStyles({
                'background-position': '' + cont + 'px top'
            });
            cont = cont + this.anchoBoton;

        }
    },
    addHoverEventToContenedor: function () {
        if (this.contenedor) {
            this.contenedor.addEvents({
                'mouseover': function (e) {
                    this.zonaIzquierda.setStyle('background-image', 'url(styles/images/BotoneraOpaca.png)');
                    this.zonaCentral.setStyle('background-image', 'url(styles/images/BotoneraOpaca_center.png)');
                    this.zonaDerecha.setStyle('background-image', 'url(styles/images/BotoneraOpaca.png)');
                } .bind(this),
                'mouseout': function (e) {
                    e.stopPropagation();
                    this.zonaIzquierda.setStyle('background-image', 'url(styles/images/BotoneraTransparente.png)');
                    this.zonaCentral.setStyle('background-image', 'url(styles/images/BotoneraTransparente_center.png)');
                    this.zonaDerecha.setStyle('background-image', 'url(styles/images/BotoneraTransparente.png)');
                } .bind(this)
            });
        }
    },
    addEventClickSegunOpcion: function (nomOpcion) {
        if ($('cmbTipoBorrado')) {$('cmbTipoBorrado').dispose()}
        visor.consultaMapaActivada = false;
        if (this.opciones.contains(nomOpcion)) {
            this.removePanelInformativo();
            this.removePanelInformativoAux();
            this.opcionBeforePrint=this.btnOpcion;
            this.btnOpcion = nomOpcion;
            if (nomOpcion == 'info') {
                visor.removeMarkers();
            }
            if (visor.map.getLayersByName("layerPoligonoConsulta") && visor.map.getLayersByName("layerPoligonoConsulta")[0]) {
                var layerx = visor.map.getLayersByName("layerPoligonoConsulta")[0];
                layerx.removeFeatures(layerx.features);
            }
            if (this.layerVector) {
                this.layerVector.setZIndex((visor.map.layers[visor.map.layers.length - 1]).getZIndex() + 5);
            }
            switch (nomOpcion) {
                case 'hand':
                    bug.log('seleccionado hand', "BOTONERA_PRINCIPAL");
                    this.activarBoton(nomOpcion);
                    this.activarControl(nomOpcion);
                    visor.consultaFichaActivada = false;
                    $('cabOpcionFichas').setStyle('border-bottom', '0px solid red');
                    break;
                case 'print':
                    bug.log('seleccionado print', "BOTONERA_PRINCIPAL");
                    this.activarBoton(nomOpcion);
                    this.loadVentanaImpresion();
                    break;
                case 'zoomIn_area':
                    bug.log('seleccionado zoomIn_area', "BOTONERA_PRINCIPAL");
                    this.activarBoton(nomOpcion);
                    this.activarControl(nomOpcion);
                    visor.consultaFichaActivada = false;
                    $('cabOpcionFichas').setStyle('border-bottom', '0px solid red');
                    break;
                case 'zoomOut_area':
                    bug.log('seleccionado zoomOut_area', "BOTONERA_PRINCIPAL");
                    this.activarBoton(nomOpcion);
                    this.activarControl(nomOpcion);
                    visor.consultaFichaActivada = false;
                    $('cabOpcionFichas').setStyle('border-bottom', '0px solid red');
                    break;
                case 'forward':
                    bug.log('seleccionado forward', "BOTONERA_PRINCIPAL");

                    var control = visor.map.getControlsBy('id', 'navigationHistory')[0];
                    //control.next.activate();
                    //this.limpiaPila(control.previousStack)
                    control.activate();
                    control.nextTrigger();
                    //                    this.activarBoton(nomOpcion);
                    //                    this.map.getControlsBy('id','navigationHistory')[0].activate;
                    //                    //                        this.map.getControlsBy('id','navigationHistory')[0].next.trigger();
                    //                    //                        this.desactivarControles();
                    //                    this.map.getControlsBy('id','navigationHistory')[0].deactivate;
                    //
                    //                    visor.history.loadNext();
                    break;
                case 'previous':
                    bug.log('seleccionado previous', "BOTONERA_PRINCIPAL");
                    var control = visor.map.getControlsBy('id', 'navigationHistory')[0];
                    this.limpiaPila(control.previousStack)
                    control.activate();
                    //control.previous.activate();
                    control.previousTrigger();
                    //this.activarBoton(nomOpcion);
                    //this.map.getControlsBy('id','navigationHistory')[0].activate;
                    //////                        this.map.getControlsBy('id','navigationHistory')[0].previous.trigger();
                    //////                        this.desactivarControles();
                    //this.map.getControlsBy('id','navigationHistory')[0].deactivate;

                    //                    visor.history.loadPrevious();
                    break;
                case 'measure':
                    bug.log('seleccionado measure', "BOTONERA_PRINCIPAL");
                    this.activarBoton(nomOpcion);
                    visor.consultaFichaActivada = false;
                    $('cabOpcionFichas').setStyle('border-bottom', '0px solid red');
                    break;
                case 'paint':
                    bug.log('seleccionado paint', "BOTONERA_PRINCIPAL");
                    this.activarBoton(nomOpcion, false);
                    this.activarControl('ctrl_POLIGONO');
                    visor.consultaFichaActivada = false;
                    $('cabOpcionFichas').setStyle('border-bottom', '0px solid red');
                    this.generarPanelInformativo(this.contenedor.getWidth() - 6, this.contenedor.getHeight() - 22, nomOpcion);
                    this.generarPanelInformativoAux(this.contenedor.getWidth() - 6, 80, nomOpcion);
                    this.activarBotonDibujo('POLIGONO');
                    break;
                case 'info':
                    bug.log('seleccionado info', "BOTONERA_PRINCIPAL");
                    visor.consultaMapaActivada = true;
                    visor.consultaFichaActivada = false;
                    $('cabOpcionFichas').setStyle('border-bottom', '0px solid red');
                    this.activarBoton(nomOpcion);
                    this.activarControl('hand');
                    this.generarPanelInformativo(this.contenedor.getWidth() - 6, this.contenedor.getHeight() - 22, nomOpcion);
                    this.generarPanelInformativoAux(this.contenedor.getWidth() - 6, 80, nomOpcion);
                    $('bpPanelInformativo').fade(0);
                    $('bpPanelInformativoAux').setStyle('top', 118);

                    break;
                default:
                    bug.log("salida por defecto del click de una opcion", "BOTONERA_PRINCIPAL");
                    break;
            }
            this.changeCursor(nomOpcion);
        }
    },
    limpiaPila: function (pila) {
        for (var i = 0; i < pila.length - 1; i++) {
            if (JSON.encode(pila[i]) == JSON.encode(pila[i + 1])) {
                pila.splice(i, 1)
            }
        }
    },
    addEventMouseOverSegunOpcion: function (nomOpcion, posBG) {
        $('bpOpcion_' + nomOpcion).setStyle('background-position', '' + posBG + 'px bottom');
        $('bpOpcion_' + nomOpcion).setStyle('background-color', this.bgColor);
        if (this.opciones.contains(nomOpcion)) {
            switch (nomOpcion) {
                case 'hand':
                    bug.log('mouseover hand', "BOTONERA_PRINCIPAL");
                    break;
                case 'print':
                    bug.log('mouseover print', "BOTONERA_PRINCIPAL");
                    break;
                case 'zoomIn_area':
                    bug.log('mouseover zoomIn_area', "BOTONERA_PRINCIPAL");
                    break;
                case 'zoomOut_area':
                    bug.log('mouseover zoomOut_area', "BOTONERA_PRINCIPAL");
                    break;
                case 'forward':
                    bug.log('mouseover forward', "BOTONERA_PRINCIPAL");
                    break;
                case 'previous':
                    bug.log('mouseover previous', "BOTONERA_PRINCIPAL");
                    break;
                case 'measure':
                    bug.log('mouseover measure', "BOTONERA_PRINCIPAL");
                    break;
                case 'paint':
                    bug.log('mouseover paint', "BOTONERA_PRINCIPAL");
                    break;
                case 'info':
                    bug.log('mouseover info', "BOTONERA_PRINCIPAL");
                    break;
                default:
                    bug.log("mouseover por defecto del click de una opcion", "BOTONERA_PRINCIPAL");
                    break;
            }

        }
    },
    addEventMouseOutSegunOpcion: function (nomOpcion, posBG) {
        if (this.btnActivo != nomOpcion) {
            $('bpOpcion_' + nomOpcion).setStyle('background-position', '' + posBG + 'px top');
            $('bpOpcion_' + nomOpcion).setStyle('background-color', 'transparent');
        }
        if (this.opciones.contains(nomOpcion)) {
            switch (nomOpcion) {
                case 'hand':
                    bug.log('mouseout hand', "BOTONERA_PRINCIPAL");
                    break;
                case 'print':
                    bug.log('mouseout print', "BOTONERA_PRINCIPAL");
                    break;
                case 'zoomIn_area':
                    bug.log('mouseout zoomIn_area', "BOTONERA_PRINCIPAL");
                    break;
                case 'zoomOut_area':
                    bug.log('mouseout zoomOut_area', "BOTONERA_PRINCIPAL");
                    break;
                case 'forward':
                    bug.log('mouseout forward', "BOTONERA_PRINCIPAL");
                    break;
                case 'previous':
                    bug.log('mouseout previous', "BOTONERA_PRINCIPAL");
                    break;
                case 'measure':
                    bug.log('mouseout measure', "BOTONERA_PRINCIPAL");
                    break;
                case 'paint':
                    bug.log('mouseout paint', "BOTONERA_PRINCIPAL");
                    break;
                case 'info':
                    bug.log('mouseout info', "BOTONERA_PRINCIPAL");
                    break;
                default:
                    bug.log("salida por defecto del click de una opcion", "BOTONERA_PRINCIPAL");
                    break;
            }

        }
    },
    activarBoton: function (nomOpcion, activateControl) {
        //DESACTIVO LOS BOTONES
        this.apagarBotones();
        //ACTIVO EL CORRESPONDIENTE
        //$('bpOpcion_'+nomOpcion).setProperty('class','bpOpcion_click');
        this.btnActivo = nomOpcion;
        $('bpOpcion_' + nomOpcion).setStyle('background-position', '' + ((this.opciones.indexOf(nomOpcion) * this.anchoBoton) + this.anchoBoton) + 'px bottom');
        $('bpOpcion_' + nomOpcion).setStyle('background-color', this.bgColor);
        bug.log('Activado el boton ' + nomOpcion, 'BOTONERA_PRINCIPAL');
        //ACTIVAR CONTROL
        if (activateControl) {
            this.activarControl(nomOpcion);
        }


    },
    apagarBotones: function () {
        for (var i = 0; i < this.opciones.length; i++) {
            var nomOpcion = this.opciones[i];
            //if($('bpOpcion_'+nomOpcion)){$('bpOpcion_'+nomOpcion).setProperty('class','bpOpcion')}
            $('bpOpcion_' + nomOpcion).setStyle('background-position', '' + ((this.opciones.indexOf(nomOpcion) * this.anchoBoton) + this.anchoBoton) + 'px top');
            $('bpOpcion_' + nomOpcion).setStyle('background-color', 'transparent');
            bug.log('Apagado el boton ' + nomOpcion, 'BOTONERA_PRINCIPAL');
        }
    },
    setMap: function (map) {
        this.map = map;
    },

    activarControl: function (idControl) {
        this.desactivarControles();
        var control;
        var controles = this.map.getControlsBy('id', idControl);

        for (var i = 0; i < controles.length; i++) {
            control = controles[i];
            control.activate();
        }

        return control;
    },
    desactivarControles: function () {
        if (this.map) {
            this.map.controls.each(function (control) {
                bug.log("Desactivo el control: " + control.id);
                //ACR: Modificación motivada por la incidencia INC000000256847
                if (control.id != 'navigationHistory' && control.id != 'coordenadasMapa')
                    control.deactivate();
            });
        }
    },

    loadVentanaImpresion: function () {
        if (!this.printerWindow)
            this.printerWindow = new JobPrinter();
        
        this.printerWindow.addEvent('close', function () {
            delete this.printerWindow;
            if (this.opcionBeforePrint && this.opcionBeforePrint!=""){
                this.addEventClickSegunOpcion(this.opcionBeforePrint);
            }else{
                this.addEventClickSegunOpcion('hand');
            }
        } .bind(this));
        if (this.printerWindow.control){
            this.printerWindow.control.activate();
            this.printerWindow.control.setFeature(this.printerWindow.page.feature);
        }
            /*
        var boundsImpresion = this.map.getExtent();
        if (visor.projection != visor.displayProjection) {
            boundsImpresion.transform(new OpenLayers.Projection(this.map.projection), this.map.displayProjection);
        }
        var centerImpresion = this.map.getCenter();
        if (visor.projection != visor.displayProjection) {
            centerImpresion.transform(new OpenLayers.Projection(this.map.projection), this.map.displayProjection);
        }
        window.open('pages/imprimir.jsp?' +
                            'projection=' + visor.projection +
                            '&displayProjection=' + visor.displayProjection +
                            '&maxExtent=' + boundsImpresion.left + ',' + boundsImpresion.bottom + ',' + boundsImpresion.right + ',' + boundsImpresion.top +
                            '&tileSize=256,256' +
                            '&centerPoint=' + centerImpresion.lon + ',' + centerImpresion.lat +
                            '&zoomInicial=' + this.map.getZoom() +
                            '&maxResolution=' + visor.maxResolution
                            , 'popup', 'width=' + 630 + ',height=' + 800 + ',resizable=no'); //toolbar=yes,menubar=yes,status=yes,dependent=yes');*/
    },
    generarPanelInformativo: function (ancho, alto, nomOpcion) {
        if (this.panelInformativo) {
            var item = this.panelInformativo;
            if (!item) return;
            if (Browser.Engine.trident) {
                for (var p in item) {
                    if (typeof item[p] == 'function') item[p] = $empty;
                }
                Element.dispose(item);
            }
            if (item.uid && item.removeEvents) item.removeEvents();
            this.panelInformativo.destroy();
        }
        this.panelInformativo = new Element('div', {
            'id': 'bpPanelInformativo',
            'class': 'bpPanelInformativo',
            'html': '',
            'styles': {
                'position': 'absolute',
                'left': '0%',
                'top': this.contenedor.getStyle('top').toInt() + 35,
                'height': alto,
                'width': ancho - 8,
                'margin-left': '2%',
                'padding-right': 4,
                'padding-top': 4,
                'padding-bottom': 4,
                'padding-left': 0,
                'z-index': 9999,
                'border': '1px solid #DADADA',
                'opacity': 0
            }
        }).injectInside($(document.body));
        this.panelInformativo.set('tween', {duration: this.duracionTransicion});
        this.panelInformativo.tween('opacity', 0.9);
        //            if(!Browser.ie)
        //            {
        //                jsIO.curveBordersToObject('bpPanelInformativo',{
        //                      tl: {radius: 7},
        //                      tr: {radius: 7},
        //                      bl: {radius: 7},
        //                      br: {radius: 7},
        //                      antiAlias: true
        //                    });
        //            }


        this.addContenidoAlPanelInformativo(nomOpcion);

    },
    removePanelInformativo: function () {
        if (this.panelInformativo) {
            var item = this.panelInformativo;
            if (!item) return;
            if (Browser.Engine.trident) {
                for (var p in item) {
                    if (typeof item[p] == 'function') item[p] = $empty;
                }
                Element.dispose(item);
            }
            if (item.uid && item.removeEvents) item.removeEvents();
            this.panelInformativo.destroy();
        }
    },
    addContenidoAlPanelInformativo: function (nomOpcion) {
        switch (nomOpcion) {
            case 'info':
                visor.tipoConsulta = 'punto';
                //AÑADO EL CONTENIDO AL PANEL INFORMATIVO
                var center = new Element('div', {
                    'id': 'piInfo_Center',
                    'styles': {
                        'position': 'absolute',
                        'margin': 'auto',
                        'width': '100%',
                        'height': 20,
                        'top': 2
                    }
                }).injectInside(this.panelInformativo);
                new Element('div', {
                    'id': 'piInfo_porPunto',
                    'class': 'tipoConsultaON',
                    'html': jsIO.cargarTextoSegunIdioma('por Punto'),
                    'styles': {
                        'position': 'relative',
                        'float': 'left',
                        'left': 0,
                        'top': 0,
                        'width': '30%',
                        'height': '100%',
                        'text-align': 'left',
                        'line-height': 18,
                        'padding-left': 41,
                        'margin-top': 3
                    },
                    'events': {
                        'click': function () {
                            $('piInfo_porPunto').setProperty('class', 'tipoConsultaON');
                            //                                                            $('piInfo_porPoligono').setProperty('class','tipoConsultaOFF');
                            visor.tipoConsulta = 'punto';
                            if (this.map) {
                                this.removeControlConsulta();
                            }
                            this.generarPanelInformativoAux(this.contenedor.getWidth() - 6, 80, nomOpcion);
                        } .bind(this)
                    }
                }).injectInside(center);
                //                                                var divPoligono=new Element('div',{
                //                                                    'id':'piInfo_porPoligono',
                //                                                    'class':'tipoConsultaOFF',
                //                                                    'html':jsIO.cargarTextoSegunIdioma('por Poligono'),
                //                                                    'styles':{
                //                                                        'position':'relative',
                //                                                        'float':'left',
                //                                                        'left':0,
                //                                                        'top':0,
                //                                                        'width':'30%',
                //                                                        'height':'100%',
                //                                                        'text-align':'left',
                //                                                        'line-height':18,
                //                                                        'padding-left':41,
                //                                                        'margin-top':3
                //                                                    },
                //                                                    'events':{
                //                                                        'click':function(){
                //                                                            $('piInfo_porPunto').setProperty('class','tipoConsultaOFF');
                //                                                            $('piInfo_porPoligono').setProperty('class','tipoConsultaON');
                //                                                            visor.tipoConsulta='poligono';
                //                                                            if(this.map)
                //                                                            {
                //                                                                this.addControlConsulta();
                //                                                            }
                //                                                            this.generarPanelInformativoAux(this.contenedor.getWidth()-6,80,nomOpcion);
                //                                                        }.bind(this)
                //                                                    }
                //                                                }).injectInside(center);
                break;

            case 'paint':
                //AÑADO LOS ELEMENTOS DE DIBUJO
                var pos = 0;
                this.opcionesPaint.each(function (item) {
                    var el = new Element('div', {
                        'id': 'bp_BtnPaint_' + item,
                        'class': 'btnPaint tipped',
                        'tip': jsIO.cargarTextoSegunIdioma(item),
                        'styles': {
                            'left': 4,
                            'width': (this.panelInformativo.getWidth() - 25) / this.opcionesPaint.length,
                            'background-position': pos - 4 + 'px top'
                        },
                        'events': {
                            'mouseover': function () {
                                this.setStyle('background-position', this.retrieve('pos') + 'px bottom');
                            },
                            'mouseout': function () {
                                if (visor.getControl('ctrl_' + this.retrieve('item')) && !visor.getControl('ctrl_' + this.retrieve('item')).active) {
                                    this.setStyle('background-position', this.retrieve('pos') + 'px top');
                                }
                                if (item == 'SAVE' || item == 'LOAD') {
                                    this.setStyle('background-position', this.retrieve('pos') + 'px top');
                                }
                            },
                            'click': function () {
                                //visor.obtenerNuevaZ();
                                this.bp.activarBotonDibujo(this.el);
                            } .bind({bp: this, el: item})
                        }
                    }).injectInside(this.panelInformativo);
                    el.store('pos', pos - 4);
                    el.store('item', item);
                    pos = pos - el.getWidth().toInt() - 9;
                } .bind(this));
                new FaceTip({
                    'els': $$('.btnPaint')
                });
                break;
        }
    },
    activarBotonDibujo: function (nomBoton) {
        if ($('cmbTipoBorrado')) {$('cmbTipoBorrado').dispose()}
        visor.removeMarkers();
        var control = this.activarControl('ctrl_' + nomBoton);
        this.opcionesPaint.each(function (item) {
            if (item == nomBoton) {
                $('bp_BtnPaint_' + item).setStyle('background-position', $('bp_BtnPaint_' + item).retrieve('pos') + 'px bottom');
            }
            else {
                $('bp_BtnPaint_' + item).setStyle('background-position', $('bp_BtnPaint_' + item).retrieve('pos') + 'px top');
            }
        } .bind(this));
        switch (nomBoton) {
            case 'PUNTO':
                this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Click sobre el mapa para dibujar puntos'));
                break;
            case 'LINEA':
                this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Dibuje lineas sobre el mapa'));
                break;
            case 'POLIGONO':
                this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Dibuje poligonos irregulares sobre el mapa'));
                break;
            case 'POLIG_REGULAR':
                this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma("Dibuje poligonos regulares sobre el mapa") + "<br><br><label for='inputSides'>" + jsIO.cargarTextoSegunIdioma("Num. Lados") + ": </label><input name='inputSides' id='inputSides' value='4'>");
                $('inputSides').addEvents({
                    'keyup': function () {
                        control.handler.setOptions({sides: $('inputSides').getProperty('value')});
                    }
                });
                control.handler.setOptions({sides: $('inputSides').getProperty('value')});
                break;
            case 'MODIFY':
                this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma("Modifique los elementos dibujados") + "<br>" +
                                                    "<input type='checkbox' name='checkRotation' id='checkRotation'/><label style='font-size:8pt;font-variant:small-caps' for='checkRotation'>" + jsIO.cargarTextoSegunIdioma('Permitir rotacion') + "</label>" +
                                                    "<br><input type='checkbox' name='checkResize' id='checkResize'/><label style='font-size:8pt;font-variant:small-caps' for='checkResize'>" + jsIO.cargarTextoSegunIdioma('Permitir reescalado') + "</label>" +
                                                    "<br><input type='checkbox' name='checkMove' id='checkMove'/><label style='font-size:8pt;font-variant:small-caps' for='checkMove'>" + jsIO.cargarTextoSegunIdioma('Permitir desplazar') + "</label>"
                                            );
                $('checkRotation').addEvents({
                    'change': function () {
                        this.bp.updateModifyFeature(this.ctrl);
                    } .bind({bp: this, ctrl: control})
                });
                $('checkResize').addEvents({
                    'change': function () {
                        this.bp.updateModifyFeature(this.ctrl);
                    } .bind({bp: this, ctrl: control})
                });
                $('checkMove').addEvents({
                    'change': function () {
                        this.bp.updateModifyFeature(this.ctrl);
                    } .bind({bp: this, ctrl: control})
                });
                break;
            case 'SELECT':
                this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Relacion de superficies afectadas'));
                break;
            case 'LOAD':
                this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Cargue dibujos guardados anteriormente sobre el mapa'));
                //OBTENGO EL ID DEL USUARIO
                var id = Cookie.read('ID_USER');
                //LEO EL XML QUE CONTIENE LAS FEATURES
                var xmlFeatures = jsIO.leerXML('data/WKT/' + id + '.xml');
                if (xmlFeatures) {
                    var features = visor.getFeaturesFromWKT(xmlFeatures.getElementsByTagName('WKT')[0].childNodes[0].nodeValue);
                    if (jsIO.isArray(features)) {
                        visor.layerVector.addFeatures(features);
                    }
                    else {
                        visor.layerVector.addFeatures([features]);
                    }

                }
                break;
            case 'SAVE':
                this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Almacene los dibujos del mapa para utilizarlos mas tarde'));

                //OBTENGO EL TRNCHO WKT DE LA CAPA VECTOR

                //GENERO EL PARSER
                var wktFormat = new OpenLayers.Format.WKT();

                //OBTENGO LOS FEATURES DE VLAYER
                var vLayer = visor.layerVector;
                if (vLayer) {
                    var arrayFeatures = vLayer.features;
                    if (arrayFeatures.length > 0) {
                        //TRANSFORMO LOS FEATURES ENCONTRADOS
                        var stringWKT = wktFormat.write(arrayFeatures);
                        jsIO.getServiceAsync({
                            'wsName': 'EXPORT_VECTOR',
                            'USUARIO': Cookie.read('ID_USER'),
                            'WKT': stringWKT
                        }, function (response) {
                            if (response != 'null') {
                                alert(jsIO.cargarTextoSegunIdioma("EXPORTADO SATISFACTORIAMENTE"));
                            }
                            else {
                                alert(jsIO.cargarTextoSegunIdioma("PROBLEMAS AL EXPORTAR EL FICHERO"));
                            }
                        }, function (response) {
                            alert(jsIO.cargarTextoSegunIdioma("NO SE HA PODIDO GUARDAR LOS DIBUJOS"));
                        })
                    }
                    else {
                        alert(jsIO.cargarTextoSegunIdioma('NO SE HAN ENCONTRADO OBJETOS A GUARDAR'));
                    }
                }
                else {
                    alert(jsIO.cargarTextoSegunIdioma('NO SE HA ENCONTRADO UNA CAPA VECTOR ACTIVA'));
                }
                break;
            case 'DELETE':
                //GENERO EL DESPLEGABLE DE  TIPOS DE BORRADO
                var cmbTipoBorrado = new Element('div', {
                    'id': 'cmbTipoBorrado'
                }).injectInside($(document.body));
                //AÑADO LOS TIPOS DE BORRADO
                new Element('div', {
                    'id': 'tipoBorradoNormal',
                    'class': 'lineaTipoBorrado',
                    'html': jsIO.cargarTextoSegunIdioma('borrar una geometria'),
                    'events': {
                        'click': function () {
                            this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Seleccione un elemento dibujado para eliminarlo del mapa'));
                            $('cmbTipoBorrado').dispose();
                        } .bind(this)
                    }
                }).injectInside(cmbTipoBorrado);
                new Element('div', {
                    'id': 'tipoBorradoTotal',
                    'class': 'lineaTipoBorrado',
                    'html': jsIO.cargarTextoSegunIdioma('borrar todas las geometrias'),
                    'events': {
                        'click': function () {
                            if (confirm(jsIO.cargarTextoSegunIdioma('Desea Borrar todas las geometrias?'))) {
                                visor.layerVector.removeAllFeatures();
                                this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Se han eliminado todas las geometrias'));
                                this.activarBoton('paint', false);
                                this.activarControl('ctrl_POLIGONO');
                                $('cmbTipoBorrado').dispose();
                                this.activarBotonDibujo('POLIGONO');
                            }
                        } .bind(this)
                    }
                }).injectInside(cmbTipoBorrado);


                break;
            default:
                break;
        }

        //}.bind(this));
    },

    updateModifyFeature: function (control) {
        //                                    if(!$('checkResize').checked && !$('checkMove').checked && !$('checkMove').checked)
        //                                   {
        control.mode = OpenLayers.Control.ModifyFeature.RESHAPE;
        //                                   }
        //                                   else
        //                                   {
        if ($('checkRotation').checked) {
            control.mode |= OpenLayers.Control.ModifyFeature.ROTATE;
        }
        if ($('checkResize').checked) {
            control.mode |= OpenLayers.Control.ModifyFeature.RESIZE;
        }
        if ($('checkMove').checked) {
            control.mode |= OpenLayers.Control.ModifyFeature.DRAG;
        }
        if ($('checkRotation').checked || $('checkMove').checked) {
            control.mode &= ~OpenLayers.Control.ModifyFeature.RESHAPE;
        }
        //                                   }
    },
    addControlConsulta: function () {
        //GENERO UNA VECTOR LAYER TEMPORAL
        var layerPoligonoConsulta = new OpenLayers.Layer.Vector("layerPoligonoConsulta");
        layerPoligonoConsulta.events.register('featureadded', layerPoligonoConsulta, function (event) {
            //LANZO LA CONSULTA AL LOS SERVICIOS WEB SEGUN SE AÑADA EL POLIGONO
            var geom = event.feature.geometry.transform(new OpenLayers.Projection(visor.projection), new OpenLayers.Projection(visor.projectionConsultaServicios));
            var format = new OpenLayers.Format.WKT();
            //                                        format.externalProjection=new OpenLayers.Projection(visor.projectionConsultaServicios);
            //                                        format.internalProjection=visor.projectionConsultaServicios;
            var features = format.read(geom);
            visor.preloadConsultasPorPoligono(features.geometry);


            bug.log('finaliza el evento de dibujo', 'BOTONERA_PRINCIPAL');
        });
        this.map.addLayer(layerPoligonoConsulta);
        //AÑADO EL CONTROL DE DIBUJO
        var controlConsultaPoligono = new OpenLayers.Control.Measure(OpenLayers.Handler.Polygon, {'id': 'controlConsultaPoligono'});
        controlConsultaPoligono.events.on({'measure': function (elem) {
            //COMPRUEBO QUE EL AREA ES MENOR DE LA CANTIDAD LIMITE;

            var layerx = this.map.getLayersByName("layerPoligonoConsulta")[0];
            layerx.removeFeatures(layerx.features);
            if (this.checkMaxAreaConsulta(elem.units, elem.measure)) {
                layerx.addFeatures([new OpenLayers.Feature.Vector(elem.geometry)]);
            }
            else {
                alert(jsIO.cargarTextoSegunIdioma('EL TAMANO DEL POLIGONO ES DEMASIADO GRANDE'));
            }
        } .bind(this)
        });
        this.map.addControl(controlConsultaPoligono);
        //ACTIVO EL CONTROL DEL POLIGONO
        controlConsultaPoligono.activate();
    },
    checkMaxAreaConsulta: function (unit, measure) {
        var area = visor.maxAreaConsultaPoligono;
        if (unit == 'km') {
            area = visor.maxAreaConsultaPoligono / 1000;
        }
        return measure <= area ? true : false;
    },
    removeControlConsulta: function () {
        this.map.removeLayer(this.map.getLayersByName('layerPoligonoConsulta')[0], false);
        this.desactivarControles();
        this.activarControl('hand');
        this.map.removeControl(this.map.getControlsBy('id', 'controlConsultaPoligono')[0]);

    },
    generarPanelInformativoAux: function (ancho, alto, nomOpcion) {
        if (this.panelInformativoAux) {
            var item = this.panelInformativoAux;
            if (!item) return;
            if (Browser.Engine.trident) {
                for (var p in item) {
                    if (typeof item[p] == 'function') item[p] = $empty;
                }
                Element.dispose(item);
            }
            if (item.uid && item.removeEvents) item.removeEvents();
            this.panelInformativoAux.destroy();
        }
        this.panelInformativoAux = new Element('div', {
            'id': 'bpPanelInformativoAux',
            'class': 'bpPanelInformativoAux',
            'html': '',
            'styles': {
                'position': 'absolute',
                'left': '0%',
                'top': this.panelInformativo.getStyle('top').toInt() + this.panelInformativo.getHeight() + 3,
                'height': alto,
                'width': ancho - 8,
                'margin-left': '2%',
                'padding-right': 4,
                'padding-top': 4,
                'padding-bottom': 4,
                'padding-left': 0,
                'z-index': 1000,
                'opacity': 0,
                'border': '1px solid #DADADA'//,
                // 'overflow':'visible'
            }
        }).injectInside($(document.body));
        this.panelInformativoAux.set('tween', {duration: this.duracionTransicion});
        this.panelInformativoAux.tween('opacity', 0.9);
        //            if(!Browser.ie)
        //            {
        //                jsIO.curveBordersToObject('bpPanelInformativoAux',{
        //                      tl: {radius: 7},
        //                      tr: {radius: 7},
        //                      bl: {radius: 7},
        //                      br: {radius: 7},
        //                      antiAlias: true
        //                    });
        //            }

        this.addContenidoAlPanelInformativoAux(ancho, alto, nomOpcion);

    },
    removePanelInformativoAux: function () {
        if (this.panelInformativoAux) {
            var item = this.panelInformativoAux;
            if (!item) return;
            if (Browser.Engine.trident) {
                for (var p in item) {
                    if (typeof item[p] == 'function') item[p] = $empty;
                }
                Element.dispose(item);
            }
            if (item.uid && item.removeEvents) item.removeEvents();
            this.panelInformativoAux.destroy();
        }
    },
    addContenidoAlPanelInformativoAux: function (ancho, alto, nomOpcion) {
        switch (nomOpcion) {
            case 'info':
                //DEFINO LA ZONA QUE CONTIENE LA INFORMACION
                var center = new Element('div', {
                    'id': 'piInfoAux_Center',
                    'class': 'scroll',
                    'html': "<div id='contentInfo'></div>",
                    'styles': {
                        //'position':'absolute',
                        'width': ancho - 3,
                        'height': alto - 10,
                        'top': 8
                        //'left':0,
                        //'background-color':'red'
                    }
                }).injectInside(this.panelInformativoAux);
                this.zonaActivaAux = $('contentInfo');
                if (visor.tipoConsulta == 'punto') {
                    this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Seleccione un punto en el mapa'));
                }
                else {
                    this.zonaActivaAux.set('html', jsIO.cargarTextoSegunIdioma('Dibuje un poligono en el mapa'));
                }
                //new MooScroll({selector:'.scroll'});
                break;
            case 'paint':
                //DEFINO LA ZONA QUE CONTIENE LA INFORMACION
                var center = new Element('div', {
                    'id': 'piInfoAux_Center',
                    'class': 'scroll',
                    'html': "<div id='contentInfo'></div>",
                    'styles': {
                        //'position':'absolute',
                        'width': ancho - 3,
                        'height': alto,
                        'top': 8
                        //'left':0,
                        //'background-color':'red'
                    }
                }).injectInside(this.panelInformativoAux);
                this.zonaActivaAux = $('contentInfo');
                //this.zonaActivaAux.set('html',jsIO.cargarTextoSegunIdioma('Click sobre el mapa para dibujar puntos'));
                break;
        }
    },
    changeCursor: function (action) {

        var extension = 'cur';
        if (Browser.Engine.trident) {
            extension = 'cur';
        }
        var etiqueta = 'div[id=map_OpenLayers_ViewPort]';
        switch (this.btnOpcion) {
            case 'hand':
                if (action == true) {
                    $(visor.map.viewPortDiv).setStyle('cursor', 'url(styles/images/icons/cursores/Moving.' + extension + ') 10 26,move');
                    if ($(document.body).getElement(etiqueta)) {
                        $(document.body).getElement(etiqueta).setStyle('cursor', 'url(styles/images/icons/cursores/Moving.' + extension + ') 10 26,move');
                    }
                }
                else {
                    $(visor.map.viewPortDiv).setStyle('cursor', 'url(styles/images/icons/cursores/Move.' + extension + ') 10 26,move');
                    if ($(document.body).getElement(etiqueta)) {
                        $(document.body).getElement(etiqueta).setStyle('cursor', 'url(styles/images/icons/cursores/Move.' + extension + ') 10 26,move');
                    }
                }
                break;
            case 'print':
                bug.log('mouseover print', "BOTONERA_PRINCIPAL");
                break;
            case 'zoomIn_area':
                $(visor.map.viewPortDiv).setStyle('cursor', 'url(styles/images/icons/cursores/searchIn.' + extension + ') 13 13,move');
                if ($(document.body).getElement(etiqueta)) {
                    $(document.body).getElement(etiqueta).setStyle('cursor', 'url(styles/images/icons/cursores/searchIn.' + extension + ') 13 13,pointer');
                }
                break;
            case 'zoomOut_area':
                $(visor.map.viewPortDiv).setStyle('cursor', 'url(styles/images/icons/cursores/searchOut.' + extension + ') 13 13,move');
                if ($(document.body).getElement(etiqueta)) {
                    $(document.body).getElement(etiqueta).setStyle('cursor', 'url(styles/images/icons/cursores/searchOut.' + extension + ') 13 13,pointer');
                }
                break;
            case 'forward':
                bug.log('mouseover forward', "BOTONERA_PRINCIPAL");
                break;
            case 'previous':
                bug.log('mouseover previous', "BOTONERA_PRINCIPAL");
                break;
            case 'measure':
                $(visor.map.viewPortDiv).setStyle('cursor', 'url(styles/images/icons/cursores/Handwriting.' + extension + ') 10 26,move');
                if ($(document.body).getElement(etiqueta)) {
                    $(document.body).getElement(etiqueta).setStyle('cursor', 'url(styles/images/icons/cursores/Handwriting.' + extension + ') 10 26,crosshair');
                }
                break;
            case 'paint':
                $(visor.map.viewPortDiv).setStyle('cursor', 'url(styles/images/icons/cursores/Handwriting.' + extension + ') 10 26,move');
                if ($(document.body).getElement(etiqueta)) {
                    $(document.body).getElement(etiqueta).setStyle('cursor', 'url(styles/images/icons/cursores/Handwriting.' + extension + ') 10 26,crosshair');
                }
                break;
            case 'info':
                $(visor.map.viewPortDiv).setStyle('cursor', 'url(styles/images/icons/cursores/Help.' + extension + ') 10 26,move');
                if ($(document.body).getElement(etiqueta)) {
                    $(document.body).getElement(etiqueta).setStyle('cursor', 'url(styles/images/icons/cursores/Help.' + extension + ') 10 26,help');
                }
                break;
            default:
                bug.log("mouseover por defecto del click de una opcion", "BOTONERA_PRINCIPAL");
                break;
        }
    }
});