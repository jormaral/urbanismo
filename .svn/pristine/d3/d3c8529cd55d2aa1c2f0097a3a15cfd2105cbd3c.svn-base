/*
    Document   : Cabecera
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/


var Cabecera=new Class({
    contenedor:null,
    zonaIzquierda:null,
    zonaCentral:null,
    zonaDerecha:null,
    logoPrimario:"",
    logoSecundario:"",
    ambito:"",
    opciones:['registro','capas','leyenda','busqueda','vista_general','ayuda'],
    estadoOpciones:[true,true,false,true,true,true],//PERMITE ACTIVAR O DESACTIVAR LAS OPCIONES DEL ARRAY ANTERIOR
    botonesCapas:['WMS','WFS','KML','GML'],
    debug:true,
    btnActivos:[],
    anchoBoton:105,
    map:null,
    layer:null,
    provincia:null,
    municipio:null,
    direccion:null,
    numero:null,
    activado: null,
    activarConsultaFicha:false,
    tiposDeFichas:'',
    menuFichasOpen:false,
    fichaSeleccionada:0,
    fichaAConsultar:'',
    fichaAConsultarPorFiltro:'',
    fichaAConsultarPorVentanaEntidades:'',
    resultadoSeleccionado:-1,
    eventoFiltroZoom:new Array(),
    eventoFiltroFicha:new Array(),
    eventoFiltroCaptura:new Array(),
    menuFichasFiltrosOpen:false,
    capaWMS:{
        nombre:'',
        formato:''
    },
    initialize:function(logo1,logo2,ambito,activarConsultaFicha,tiposDeFichas){
        this.logoPrimario=logo1;
        this.logoSecundario=logo2;
        this.ambito=ambito;
        this.activarConsultaFicha=activarConsultaFicha;
        this.tiposDeFichas=tiposDeFichas;
        //ASIGNO EL PRIMER TIPO DE FICHA QUE ENCUENTRE
        this.fichaAConsultar=this.tiposDeFichas.split(';')[0]
        this.fichaAConsultarPorFiltro=this.tiposDeFichas.split(';')[0]
        this.generarEstructuraPrincipal();
        this.agregarContenido();
    },
    generarEstructuraPrincipal:function(){
        //GENERO LA ESTRUCTURA CONTENEDORA
        this.contenedor=new Element('div',{
            'id':'cabContenedor',
            'class':'cabContenedor'
        }).injectInside($(document.body));
        //CREO LA ZONA IZQUIERDA
        this.zonaIzquierda=new Element('div',{
            'id':'cabContenedor_zonaIzquierda'
        }).injectInside(this.contenedor);
        //CREO LA ZONA CENTRAL
        this.zonaCentral=new Element('div',{
            'id':'cabContenedor_zonaCentral'
        }).injectInside(this.contenedor);
        //CREO LA ZONA DERECHA
        this.zonaDerecha=new Element('div',{
            'id':'cabContenedor_zonaDerecha'
        }).injectInside(this.contenedor);
    },
    agregarContenido:function(){
        if(this.logoPrimario!=null && this.logoPrimario!="")
        {
            new Element('div',{
                'id':'cabLogoPrimario',
                'class':'cabLogo1 tipped',
                //'tip':this.ambito,
                'styles':{
                    'background-image':'url('+this.logoPrimario+')'
                }
            }).injectInside(this.zonaCentral);
        }
        if(this.logoSecundario!=null && this.logoSecundario!="")
        {
            new Element('div',{
                'id':'cabLogoSecundario',
                'class':'cabLogo2',
                'styles':{
                    'background-image':'url('+this.logoSecundario+')'
                }
            }).injectInside(this.zonaCentral);
        }
        var cont=this.anchoBoton+30;
        var firstTime=true;
        
        //AGREGO EL RESTO DE BOTONES DE LA CABECERA
        for(var i=this.opciones.length-1;i>=0;i--)
        {
            //OBTENGO EL NOMBRE DE LA OPCION
            var nomOpcion=this.opciones[i];
            //GENERO EL ELEMENTO CORRESPONDIENTE

            if(this.estadoOpciones[i])
            {
                bug.log(nomOpcion,'CABECERA');
                //GENRO EL BOTON

                var opc=new Element('div',{
                    'id':'cabOpcion_'+nomOpcion,
                    'class':'cabOpcion tipped',
                    'tip':jsIO.cargarTextoSegunIdioma('tip_'+nomOpcion),
                    'events':{
                        'click':function(){
                            this.cab.addEventClickSegunOpcion(this.op)
                            }.bind({
                            cab:this,
                            op:nomOpcion
                        })
                    //                                                'mouseover':function(){this.cab.addEventMouseOverSegunOpcion(this.op,this.posBG)}.bind({cab:this,op:nomOpcion,posBG:cont}),
                    //                                                'mouseout':function(){this.cab.addEventMouseOutSegunOpcion(this.op,this.posBG)}.bind({cab:this,op:nomOpcion,posBG:cont})
                    }
                }).injectInside(this.zonaCentral);
                //GENERO EL CONTENEDOR DEL TEXTO
                new Element('div',{
                    'class':'nombreCabOption tipped',
                    'tip':jsIO.cargarTextoSegunIdioma('tip_'+nomOpcion),
                    'html':jsIO.cargarTextoSegunIdioma(nomOpcion)
                }).injectInside(opc);
                //                                opc.setStyles({
                //                                     'background-position':''+cont+'px top'
                //                                });
                if(firstTime)
                {
                    cont=opc.getWidth();
                    this.anchoBoton=cont+30;
                    firstTime=false;
                }

            }
            cont=cont+this.anchoBoton;

        }
        new FaceTip({
            'els': $$('.cabOpcion')
        });

        //AGREGO EL BOTONES DE LAS FICHAS SI DEBEN ESTAR ACTIVOS
        this.generarBotonesFichas();
    },
    generarBotonesFichas:function(){

        //OBTENGO LOS TIPOS DE FICHAS Y GENERO LOS BOTONES CORRESPONDIENTES EN LA CABECERA
        var arrayFichas=this.tiposDeFichas.split(";");
        this.fichaSeleccionada=0;
        this.menuFichasOpen=false;
        //GENERO EL BOTON DE CONSULTA DE FICHA
        var cabOpcionFichas=new Element('div',{
            'id':'cabOpcionFichas',
            'class':'cabOpcionFichas tipped',
            'tip':jsIO.cargarTextoSegunIdioma('tip_fichas'),
            'html':"<div id='lblTipoFichas'>"+jsIO.cargarTextoSegunIdioma(arrayFichas[0])+"</div>",
            'events':{
                'click':function(){
                    if(!visor.consultaFichaActivada){
                        this.cab.activarConsultaTipoFicha(arrayFichas[visor.cabecera.fichaSeleccionada]);
                    }
                    else
                    {
                        //DESACTIVO EL BOTON
                        //$('cabOpcionFichas').setStyle('background-image','styles/images/cabOpcionFicha_'+arrayFichas[this.fichaSeleccionada]+'.png');
                        $('cabOpcionFichas').setStyle('border-bottom','0px solid red');
                        //DESACTIVO EL MODO SELECCION DE PUNTO EN EL MAPA
                        visor.consultaMapaActivada=false;
                        visor.consultaFichaActivada=false;
                        visor.botoneraPrincipal.removePanelInformativo();
                        visor.botoneraPrincipal.removePanelInformativoAux();
                        visor.botoneraPrincipal.activarBoton('hand');
                        visor.botoneraPrincipal.activarControl('hand');
                        $(visor.map.viewPortDiv).setStyle('cursor','url(styles/images/icons/cursores/Link.cur) 10 26,move');
                    }
                }.bind({
                    fichaSeleccionada:this.fichaSeleccionada,
                    cab:this
                })
            }
        }).injectInside(this.zonaCentral);
        //AÑADO EL TOOLTIP
        new FaceTip({
            'els': $$('.cabOpcionFichas')
        });
        cabOpcionFichas
        //GENERO EL BOTON QUE DESPLIEGA
        new Element('div',{
            'id':'cabOpcionSelect',
            'events':{
                'click':function(event){
                    event.stopPropagation();
                    if(!this.menuFichasOpen){
                        if(!$('containerTipoFicha')){
                            //GENERO EL DESPLEGABLE
                            var containerTipoFicha=new Element('div',{
                                'id':'containerTipoFicha',
                                'styles':{
                                    'top': $('cabOpcionFichas').getPosition().y + 45,
                                    //'right': 546,//$('cabOpcionFichas').getPosition().x - 56,
                                    'opacity':0,
                                    'visibility':'hidden'
                                }
                            }).injectInside(this.zonaCentral);
                            containerTipoFicha.fade(1);
                            //RELLENO EL DESPLEGABLE
                            for (var i = 0; i < arrayFichas.length; i++) {
                                new Element('div',{
                                    'id':'tipoFicha_'+arrayFichas[i],
                                    'class':'slotTipoFicha',
                                    'html':jsIO.cargarTextoSegunIdioma(arrayFichas[i]),
                                    'events':{
                                        'click':function(){
                                            //CIERRO EL DESPLEGABLE
                                            $('containerTipoFicha').fade(0);
                                            visor.cabecera.menuFichasOpen=false;
                                            //CAMBIO EL TEXTO DEL BOTON DE LA CONSULTA DE FICHAS
                                            $('lblTipoFichas').set('html',jsIO.cargarTextoSegunIdioma(this.nom));
                                            //CAMBIO EL TIPO DE FICHA
                                            visor.cabecera.fichaSeleccionada=this.pos;
                                            //ACTIVO LA FICHA
                                            visor.cabecera.activarConsultaTipoFicha(this.nom);
                                        }.bind({
                                            //nom:jsIO.cargarTextoSegunIdioma(arrayFichas[i]),
                                            nom:arrayFichas[i],
                                            pos:i
                                        })
                                    }
                                }).injectInside($('containerTipoFicha'));
                            }
                        }
                        else
                        {
                            $('containerTipoFicha').setStyle('opacity',1)
                        }
                        this.menuFichasOpen=true;
                    }
                    else
                    {
                        $('containerTipoFicha').fade(0);
                        this.menuFichasOpen=false;
                    }
                }.bind(this),
                'mouseover':function(event){
                    event.stopPropagation();
                }
            }
        }).injectInside(cabOpcionFichas);
    },
    activarConsultaTipoFicha:function(ficha){
        //ACTIVO EL BOTON
        //$('cabOpcionFichas').setStyle('background-image','styles/images/cabOpcionFicha_'+arrayFichas[this.fichaSeleccionada]+'_OVER.png');
        $('cabOpcionFichas').setStyle('border-bottom','2px solid red');
        //ACTIVAR MODO SELECCION DE PUNTO EN EL MAPA
        visor.cabecera.fichaAConsultar=ficha;
        visor.consultaMapaActivada=true;
        visor.consultaFichaActivada=true;
        visor.botoneraPrincipal.activarBoton('info');
        visor.botoneraPrincipal.activarControl('hand');
        visor.botoneraPrincipal.generarPanelInformativo(visor.botoneraPrincipal.contenedor.getWidth()-6,visor.botoneraPrincipal.contenedor.getHeight()-22,'info');
        visor.botoneraPrincipal.generarPanelInformativoAux(visor.botoneraPrincipal.contenedor.getWidth()-6,80,'info');
        $(visor.map.viewPortDiv).setStyle('cursor','url(styles/images/icons/cursores/Help.cur) 10 26,help');
        $('bpPanelInformativo').fade(0);
        $('bpPanelInformativoAux').setStyle('top',118);
    //AL HACER CLICK EN EL MAPA ACTIVAR EL BOTON DE CONSULTA DE LAS HERRAMIENTAS
    //ADEMAS DE SU LISTA DE RESULTADOS (ENTIDADES/REGISTRO/PLANOS)
    },
    addEventClickSegunOpcion:function(nomOpcion){
        if(this.opciones.contains(nomOpcion))
        {
            switch (nomOpcion) {
                case 'FichaUrbanistica':
                    bug.log('seleccionado ficha','CABECERA');
                    if(!visor.consultaFichaActivada){
                        //ACTIVO EL BOTON
                        $('cabOpcion_'+nomOpcion).setStyle('background-image','styles/images/cabOpcionFichaOVER.png');
                        //ACTIVAR MODO SELECCION DE PUNTO EN EL MAPA
                        this.fichaAConsultar=nomOpcion;
                        visor.consultaMapaActivada=true;
                        visor.consultaFichaActivada=true;
                        visor.botoneraPrincipal.activarBoton('info');
                        visor.botoneraPrincipal.activarControl('hand');
                        visor.botoneraPrincipal.generarPanelInformativo(visor.botoneraPrincipal.contenedor.getWidth()-6,visor.botoneraPrincipal.contenedor.getHeight()-22,'info');
                        visor.botoneraPrincipal.generarPanelInformativoAux(visor.botoneraPrincipal.contenedor.getWidth()-6,80,'info');
                        $(visor.map.viewPortDiv).setStyle('cursor','url(styles/images/icons/cursores/Help.cur) 10 26,help');
                    //AL HACER CLICK EN EL MAPA ACTIVAR EL BOTON DE CONSULTA DE LAS HERRAMIENTAS
                    //ADEMAS DE SU LISTA DE RESULTADOS (ENTIDADES/REGISTRO/PLANOS)
                    }
                    else
                    {
                        //DESACTIVO EL BOTON
                        $('cabOpcion_'+nomOpcion).setStyle('background-image','styles/images/cabOpcionFichaOVER.png');
                        //DESACTIVO EL MODO SELECCION DE PUNTO EN EL MAPA
                        visor.consultaMapaActivada=false;
                        visor.consultaFichaActivada=false;
                        visor.botoneraPrincipal.removePanelInformativo();
                        visor.botoneraPrincipal.removePanelInformativoAux();
                        visor.botoneraPrincipal.activarBoton('hand');
                        visor.botoneraPrincipal.activarControl('hand');
                        $(visor.map.viewPortDiv).setStyle('cursor','url(styles/images/icons/cursores/Link.cur) 10 26,move');
                    }

                    //this.toggleBoton(nomOpcion);
                    break;
                case 'registro':
                    bug.log('seleccionado REGISTRO','CABECERA');

                    var jw=new JobWindow(jsIO.cargarTextoSegunIdioma('REGISTRO'),false,500,300,300,200,true);
                    jw.loadIframe(visor.rutaServicioRegistro+'?&rutaServ='+visor.rutaServicioRegistroWS+'&debugMode=on&idAmbito='+visor.idAmbito);
                    break;
                case 'capas':
                    bug.log('seleccionado capas','CABECERA');
                    var ventana=new JobWindow(nomOpcion,false,560,400,200,200,true,true);
                    this.loadBotonesDeCapas(ventana);
                    //this.toggleBoton(nomOpcion);
                    visor.mostrarArbolCapas();
                    break;
                case 'leyenda':
                    bug.log('seleccionado leyenda','CABECERA');
                    var JV = new JobWindow(nomOpcion,false,600,500,220,180,true);
                    //this.toggleBoton(nomOpcion);
                    this.loadImageLegends(JV.zonaActiva);
                    break;
                case 'busqueda':
                    bug.log('seleccionado busqueda','CABECERA');
                    var JWB=new JobWindow(nomOpcion,false,508,528,150,150,true);
                    JWB.loadAJAX('busqueda.jsp',function(response){
                        this.ventana.loadZonaActiva(response);
                        this.cab.preloadBusquedas()
                    }.bind({
                        cab:this,
                        ventana:JWB
                    }));
                    //this.toggleBoton(nomOpcion);

   
                    break;
                case 'vista_general':
                    bug.log('seleccionado vista_general','CABECERA');
                    if(!this.btnActivos.contains(nomOpcion))
                    {
                        var opcionesMapaOverview;
                        if(visor.baseLayerGoogle)
                        {
                            opcionesMapaOverview={
                                'projection':visor.projection,
                                'displayProjection':visor.displayProjection,
                                'maxResolution': visor.maxResolution,
                                'maxRatio':13,
                                'minRatio':13,
                                'maxExtent': visor.boundsMaxExtent
                            }
                        }
                        else
                        {
                            opcionesMapaOverview={
                                'projection':visor.projection,
                                'displayProjection':visor.displayProjection,
                                'maxExtent': visor.boundsMaxExtent
                            }
                        }

                        var JV = new JobWindow('vista_general',false,270,270,260,260,true);
                        this.map.addControl(new OpenLayers.Control.OverviewMap({
                            'id':'controlOverviewMap',
                            'div':JV.zonaActiva,
                            'size':new OpenLayers.Size(242,233),
                            'layers':[this.getLayerOverview()],
                            'mapOptions':opcionesMapaOverview
                        }));
                    }
                    //this.toggleBoton('vista_general');
                        
                    break;
                case 'ayuda':
                    bug.log('seleccionado ayuda','CABECERA');
                    //new JobWindow(nomOpcion,false,300,400,280,280,true);
                    new SplashScreen();
                    //this.toggleBoton(nomOpcion);
                    break;
                default:
                    bug.log("salida por defecto del click de una opcion","CABECERA");
                    break;
            }

        }
    },
    getLayerOverview:function(){
        if(visor.baseLayerGoogle)
        {
            return new OpenLayers.Layer.Google(
                'GOOGLE-OVERVIEW',
                {
                    'type':google.maps.MapTypeId.ROADMAP,
                    'sphericalMercator': true
                //'wrapDateLine':true,
                //'isBaseLayer':true
                }
                );
        }
        else
        {
            var layerTemp=visor.map.layers[1];
            if(layerTemp && layerTemp.params && layerTemp.CLASS_NAME=="OpenLayers.Layer.WMS")
            {
                return new OpenLayers.Layer.WMS(
                    'OVERVIEW',
                    layerTemp.url,
                    {
                        'layers': layerTemp.params.LAYERS,
                        'transparent':false,
                        'format':layerTemp.params.FORMAT
                    },
                    {
                        'isBaseLayer':true,
                        'displayInLayerSwitcher':false,
                        //'projection':new OpenLayers.Projection(visor.displayProjection),
                        'displayOutsideMaxExtent':true,
                        'gutter':0,
                        //'transitionEffect': 'resize',
                        //'reproject':true,
                        'buffer':0,
                        'singleTile':false,
                        'visibility':true
                    }
                    );
            }
            else
            {
                return new OpenLayers.Layer.WMS(
                    'OVERVIEW',
                    'http://www.idee.es/wms/PNOA/PNOA',
                    {
                        'layers': 'PNOA',
                        'transparent':false,
                        'format':'image/png'
                    },
                    {
                        'isBaseLayer':true,
                        'displayInLayerSwitcher':false,
                        //'projection':new OpenLayers.Projection(visor.displayProjection),
                        'displayOutsideMaxExtent':true,
                        'gutter':0,
                        //'transitionEffect': 'resize',
                        //'reproject':true,
                        'buffer':0,
                        'singleTile':false,
                        'visibility':true
                    }
                    );
            }



        //                visor.crearCapaWMS(
        //                                    'OVERVIEW',
        //                                    visor.map.layers[2].url,
        //                                    visor.map.layers[2].params.LAYERS,
        //                                    false,
        //                                    visor.map.layers[2].params.FORMAT,
        //                                    false,
        //                                    true,
        //                                    true,
        //                                    false
        //                          );
                              
        //alert('NECESARIO DEFINIR QUE CARTOGRAFIA SE DEBE MOSTRAR COMO CAPA BASE')
        }
    },
    addEventMouseOverSegunOpcion:function(nomOpcion,posBG){
        if(!this.btnActivos.contains(nomOpcion) && nomOpcion!='FichaUrbanistica'){
            $('cabOpcion_'+nomOpcion).setStyle('background-position',''+posBG+'px bottom');
        }
        if(this.opciones.contains(nomOpcion))
        {
            switch (nomOpcion) {
                case 'capas':
                    bug.log('mouseover capas','CABECERA');
                    break;
                case 'leyenda':
                    bug.log('mouseover leyenda','CABECERA');
                    break;
                case 'busqueda':
                    bug.log('mouseover busqueda','CABECERA');
                    break;
                case 'vista_general':
                    bug.log('mouseover vista_general','CABECERA');
                    break;
                case 'ayuda':
                    bug.log('mouseover ayuda','CABECERA');
                    break;
                default:
                    bug.log("salida por defecto del mouseover de una opcion","CABECERA");
                    break;
            }

        }
    },
    addEventMouseOutSegunOpcion:function(nomOpcion,posBG){
        if(!this.btnActivos.contains(nomOpcion) && nomOpcion!='FichaUrbanistica'){
            $('cabOpcion_'+nomOpcion).setStyle('background-position',''+posBG+'px top');
        }
        if(this.opciones.contains(nomOpcion))
        {
            switch (nomOpcion) {
                case 'capas':
                    bug.log('mouseout capas','CABECERA');
                    break;
                case 'leyenda':
                    bug.log('mouseout leyenda','CABECERA');
                    break;
                case 'busqueda':
                    bug.log('mouseout busqueda','CABECERA');
                    break;
                case 'vista_general':
                    bug.log('mouseout vista_general','CABECERA');
                    break;
                case 'ayuda':
                    bug.log('mouseout ayuda','CABECERA');
                    break;
                default:
                    bug.log("salida por defecto del mouseout de una opcion","CABECERA");
                    break;
            }

        }
    },
    toggleBoton:function(nomOpcion){
        if(this.btnActivos.contains(nomOpcion)) //ENTONCES APAGO EL BOTON
        {
            this.apagarBoton(nomOpcion);
        }
        else //ENTONCES ENCIENDO EL BOTON
        {
            this.encenderBoton(nomOpcion);
        }

    },
    apagarBoton:function(nomOpcion){
        if($('cabOpcion_'+nomOpcion))
        {
        //$('cabOpcion_'+nomOpcion).setStyle('background-position',''+((this.opciones.indexOf(nomOpcion)*this.anchoBoton))*(-1)+'px top');
        }
        if($('jwContenedor_'+nomOpcion))
        {
            $('jwContenedor_'+nomOpcion).destroy();
        }
        this.btnActivos.erase(nomOpcion);
    },
    encenderBoton:function(nomOpcion){
        //$('cabOpcion_'+nomOpcion).setStyle('background-position',''+((this.opciones.indexOf(nomOpcion)*this.anchoBoton))*(-1)+'px bottom');
        this.btnActivos.push(nomOpcion);
    }
    ,
    setMap:function(map){
        this.map=map;
    },
    setLayer:function(layer){
        this.layer=layer;
    },
    loadBotonesDeCapas:function(ventana){
          
        this.botonesCapas.each(function(boton){
            //              if(boton=='KML' && visor.displayProjection=='EPSG:4326')
            //              {
            this.generarBotonCabecera(ventana,boton);

        //              }
        //              else if(boton=='KML' && visor.displayProjection!='EPSG:4326')
        //              {
        //                  //KML NO SOPORTADO
        //              }
        //              else
        //              {
        //                  this.generarBotonCabecera(ventana,boton);
        //              }

        }.bind(this)) ;
        new FaceTip({
            'els':$$('.jwBtnBotonera')
        });
    },
    generarBotonCabecera: function (ventana, boton) {
        if(ventana.botoneraZonaActiva){
            new Element('div',{
                'id':'btnCapa_'+boton,
                'class':'jwBtnBotonera tipped',
                'html':jsIO.cargarTextoSegunIdioma('+ '+boton),
                'tip':jsIO.cargarTextoSegunIdioma('botonAdd'+boton),
                'events':{
                    'click':function(){
                        if(boton=='WMS')
                        {
                            var ventanaWMS=SqueezeBox;
                            ventanaWMS.initialize({
                                size: {
                                    x: 400,
                                    y: 400
                                }
                            });
                            ventanaWMS.presets.size.x=400;
                            ventanaWMS.presets.size.y=400;

                            ventanaWMS.addEvent('onAjax', function(){
                                this.addContenidoLectorWMS()
                                }.bind(this));
                            ventanaWMS.open('pages/lectorWMS.html', {
                                handler: 'ajax'
                            });

                        }
                        else if(boton=='WFS')
                        {
                            var ventanaWFS=SqueezeBox;
                            ventanaWFS.initialize({
                                size: {
                                    x: 400,
                                    y: 400
                                }
                            });
                            ventanaWFS.presets.size.x=400;
                            ventanaWFS.presets.size.y=400;

                            ventanaWFS.addEvent('onAjax', function(){
                                this.addContenidoLectorWFS()
                                }.bind(this));
                            ventanaWFS.open('pages/lectorWMS.html', {
                                handler: 'ajax'
                            });
                        }
                        else if(boton=='KML')
                        {
                            var ventanaKML=SqueezeBox;
                            ventanaKML.initialize({
                                size: {
                                    x: 120,
                                    y: 61
                                }
                            });
                            ventanaKML.presets.size.x=120;
                            ventanaKML.presets.size.y=61;
                            ventanaKML.addEvent('onAjax', function(){
                                this.cab.addContenidoDeKML('selector');
                            }.bind({
                                cab:this,
                                boton:boton
                            }));
                            ventanaKML.open('pages/lectorKML.html', {
                                handler: 'ajax'
                            });

                        }
                        else if(boton=='GML')
                        {
                            var ventanaKML=SqueezeBox;
                            ventanaKML.initialize({
                                size: {
                                    x: 120,
                                    y: 61
                                }
                            });
                            ventanaKML.presets.size.x=120;
                            ventanaKML.presets.size.y=61;
                            ventanaKML.addEvent('onAjax', function(){
                                this.cab.addContenidoDeGML('selector');
                            }.bind({
                                cab:this,
                                boton:boton
                            }));
                            ventanaKML.open('pages/lectorGML.html', {
                                handler: 'ajax'
                            });
                        }

                    }.bind(this)
                }
            }).injectInside(ventana.botoneraZonaActiva);
        }
    },
    addContenidoDeKML:function(boton){

        new AjaxUpload(boton, {
            action: 'readKML',
            //                                                        data:{
            //                                                            'wsName':'readKML'
            //                                                        },
            onSubmit : function(file , ext){
                if (! (ext && /^(kml)$/.test(ext))){
                    // extension is not allowed
                    alert(jsIO.cargarTextoSegunIdioma('Fichero con extension invalida'));
                    // cancel upload
                    return false;
                }
            },
            onComplete:function(filename,name){
                if(!name.contains('ERROR'))
                {
                    //Si no existe la capa
                    if(!this.map.getLayer(filename)){
                        //AÑADO EL KML
                        var path=this.getPath();
                        var layerKML=visor.crearCapaKML(filename,path+'data/KML/'+name+'.kml');
                        visor.actualizarZIndexMarkers();
                        //LA AÑADO AL ARBOL
                        visor.treeServicios.insert({
                            text:layerKML.id,
                            data:{
                                type:'KML',
                                typeButton:'delete'
                            }
                        });
                        //ELIMINO EL ARCHIVO TRAS SER LEIDO

                        setTimeout(function(){
                            this.cab.removeFileFromServer(this.id,this.ext)
                        }.bind({
                            cab:this,
                            id:layerKML.id,
                            ext:'kml'
                        }),1000);
                    }
                    //Si existe la capa
                    else{
                        if(confirm(jsIO.cargarTextoSegunIdioma('Ya tiene una capa con el mismo nombre:') + name + ', '+ jsIO.cargarTextoSegunIdioma('Desea sobreescribirla?'))){
                            //ELIMINO LA CAPA
                            this.map.removeLayer(this.map.getLayer(filename));
                            //LA GENERO DE NUEVO
                            //  layerKML=visor.crearCapaKML(filename,path+'data/KML/'+name+'.kml');

                            this.sobreescribeCapa('KML',filename);
                        }
                    }
                }
                else
                {
                    alert('SE HA PRODUCIDO UN ERROR AL LEER EL ARCHIVO')
                }
            }.bind(this)
        });
    },
    addContenidoDeGML:function(boton){
        new AjaxUpload(boton, {
            action: 'readGML',
            onSubmit : function(file , ext){
                if (! (ext && /^(xml)$/.test(ext))){
                    if (! (ext && /^(gml)$/.test(ext) )){
                        // extension is not allowed
                        alert(jsIO.cargarTextoSegunIdioma('Fichero con extension invalida'));
                        // cancel upload
                        return false;
                    }
                }

            },
            onComplete:function(filename,name){
                if(!name.contains('ERROR'))
                {
                    // alert('filename '+filename+'name '+name);
                    //Si no existe la capa
                    if(!this.map.getLayer(filename)){

                        //AÑADO EL GML
                        var path=this.getPath();
                        var layerGML=visor.crearCapaGML(filename,path+'data/GML/'+name+'.xml');
                        visor.actualizarZIndexMarkers();
                        //LA AÑADO AL ARBOL
                        visor.treeServicios.insert({
                            text:layerGML.id,
                            data:{
                                type:'GML',
                                typeButton:'delete'
                            }
                        });
                        //ELIMINO EL ARCHIVO TRAS SER LEIDO

                        setTimeout(function(){
                            this.cab.removeFileFromServer(this.id,this.ext)
                        }.bind({
                            cab:this,
                            id:layerGML.id,
                            ext:'gml'
                        }),1000);
                    }
                    //Si existe la capa
                    else{
                        if(confirm(jsIO.cargarTextoSegunIdioma('Ya tiene una capa con el mismo nombre:')+filename+',' + jsIO.cargarTextoSegunIdioma('Desea sobreescribirla?'))){
                            //ELIMINO LA CAPA
                            this.map.removeLayer(this.map.getLayer(filename));
                            //LA GENERO DE NUEVO
                            // layerGML=visor.crearCapaGML(filename,path+'data/GML/'+name+'.xml');

                            this.sobreescribeCapa('GML',filename);
                        }
                    }
                }
                else
                {
                    alert('SE HA PRODUCIDO UN ERROR AL LEER EL ARCHIVO')
                }
            }.bind(this)
        });
    },
    removeFileFromServer:function(id,extensionArchivo){
        new Request({
            'url':'ActionServlet',
            'async':true,
            'data':Hash.toQueryString({
                'wsName':'REMOVE_FILE_FROM_SERVER',
                'id':id,
                'extension':extensionArchivo
            }),
            onSuccess: function(response){
            //alert(response);
            },
            onFailure: function(response){
            //alert(response);
            }
        }).send();
    },
    getPath:function(){
        var path=location.pathname;
        if(path.contains('index.jsp'))
        {
            path=path.substr(0,path.length-9);
        }
        return path;
    },
    //PRE: Las capas nuevas que se quieren visualizar se añaden directamente en el primer nivel del arbol
    sobreescribeCapa:function(tipoCapa,filename){
        //ELIMINO LA CAPA DEL ARBOL
        var capas=visor.treeServicios.root.nodes;
        for (var i=0;i<capas.length;i++){
            if (capas[i].id==filename){
                capas[i].remove();
            }
        }
        // GENERO DE NUEVO LA CAPA DEL ARBOL
        visor.treeServicios.insert({
            text:filename,
            data:{
                type:tipoCapa,
                typeButton:'delete'
            }
        });
    },
    addContenidoLectorWMS:function(){
        if($('URL'))
        {
            $('URL').empty();
            //GENERO LOS TITULOS
            new Element('div',{
                'id':'txtWMS_URL',
                'html':jsIO.cargarTextoSegunIdioma('URL del WMS')
            }).injectInside('URL');
            new Element('div',{
                'id':'txtWMS_FORMATO',
                'html':jsIO.cargarTextoSegunIdioma('FORMATO')
            }).injectInside('URL');
            new Element('div',{
                'id':'txtWMS_CAPAS',
                'html':jsIO.cargarTextoSegunIdioma('CAPAS')
            }).injectInside('URL');
            //GENERO LAS CASILLAS
            new Element('input',{
                'id':'campoWMS_URL',
                'type':'text',
                'value':'',
                'selectBoxOptions':visor.serviciosWMS
            }).injectInside($('URL'));
            createEditableSelect($('campoWMS_URL'),$('campoWMS_URL').parentNode.parentNode);
            $('campoWMS_URL').top=0;
            $('campoWMS_URL').left=0;
            new Element('select',{
                'id':'campoWMS_FORMATO',
                'value':''
            }).injectInside($('URL'));
            new Element('select',{
                'id':'campoWMS_CAPAS',
                'multiple':true,
                'size':4
            }).injectInside($('URL'));
            //            GENERO LOS BOTONES
            new Element('div',{
                'id':'btnWMS_URL',
                'events':{
                    'click':function(){
                        $('URL').setStyle('height',100);
                        $('campoWMS_FORMATO').empty();
                        $('campoWMS_CAPAS').empty();
                        var url=$('campoWMS_URL').getProperty('value');
                        url=jsIO.reformatURL(url);
                        if(url!=null)
                        {
                            var title=this.cargarConfWMS(url);
                        //                                    var title=this.leerDataFromURL(url);
                        }
                        else
                        {
                            alert(jsIO.cargarTextoSegunIdioma('LA URL ENVIADA NO TIENE UN FORMATO CORRECTO'));
                        }
                    }.bind(this)
                }
            }).injectInside('URL');
            new Element('div',{
                'id':'btnWMS_ADD',
                'html':jsIO.cargarTextoSegunIdioma('CARGAR WMS'),
                'styles':{
                    'text-align':'center'
                },
                'events':{
                    'click':function(){
                        var arrayCapasSelecionadas=new Array();
                        var title=$('URL').retrieve('title') ;
                        //OBTENGO LA URL
                        var url=$('campoWMS_URL').getProperty('value');
                        //OBTENGO EL FORMATO
                        var format=$('campoWMS_FORMATO').getProperty('value');
                        //OBTENGO LA LISTA DE CAPAS
                        var cmbCapas=document.getElementById('campoWMS_CAPAS');
                        for (var i=0;i < cmbCapas.options.length;i++)
                        {
                            if (cmbCapas.options[i].selected)
                            {
                                arrayCapasSelecionadas.push(cmbCapas.options[i].value);
                            //title=title + "-" + cmbCapas.options[i].value;
                            }
                        }

                        //GENERO LA CAPA
                        if(!this.map.getLayer(title))
                        {
                            visor.crearCapaWMS(title,url,arrayCapasSelecionadas,true,format,false,true,true,false,false);
                            visor.actualizarZIndexMarkers();
                            //LA AÑADO AL ARBOL
                            visor.treeServicios.insert({
                                id:title,
                                text:title,
                                data:{
                                    type:'WMS',
                                    typeButton:'delete'
                                }
                            });
                            alert(jsIO.cargarTextoSegunIdioma('WMS ANADIDO A ARBOL DE CAPAS'));
                        }
                        else
                        {
                            if (confirm(jsIO.cargarTextoSegunIdioma('Ya tiene una capa con el mismo nombre:') + title + ',' + jsIO.cargarTextoSegunIdioma('Desea sobreescribirla?')))
                            {
                                //ELIMINO LA CAPA
                                this.map.removeLayer(this.map.getLayer(title));
                                var node=visor.treeServicios.get(title)
                                if(node)
                                {
                                    node.div.main.destroy();
                                }

                                //LA GENERO DE NUEVO
                                visor.crearCapaWMS(title,url,arrayCapasSelecionadas,true,format,false,true,false);
                                visor.actualizarZIndexMarkers()
                                //LA AÑADO AL ARBOL
                                visor.treeServicios.insert({
                                    id:title,
                                    text:title,
                                    data:{
                                        type:'WMS',
                                        typeButton:'delete'
                                    }
                                });
                            }
                        }

                    }.bind(this)
                }
            }).injectInside('URL');
        }
        else
        {
            alert(jsIO.cargarTextoSegunIdioma('NO SE HA PODIDO CARGAR CORRECTAMENTE EL CONTENIDO'));
        }
    },
    cargarConfWMS:function(url){
        //ACTIVAR LOADING
        var loadingElem=new Element('div',{
            'id':'loading_URL_WMS'
        }).injectInside($('URL'));
        //LEER URL
        jsIO.getServiceAsync({
            'wsName':'CROSS-DOMAIN',
            'url':url
        },
        function(wmsResponse){
            //DESACTIVAR LOADING
            this.loadingElem.dispose();
            //PARSEAR XML
            var wmsXML=jsIO.leerXMLFromString(wmsResponse);
            //LEO EL NOMBRE DE LA CAPA
            if(wmsXML.getElementsByTagName('Title') && wmsXML.getElementsByTagName('Title')[0])
            {
                this.cab.capaWMS.nombre=wmsXML.getElementsByTagName('Title')[0].childNodes[0].nodeValue;
            }
            else
            {
                this.cab.capaWMS.nombre='defaultName_'+Math.floor(Math.random()*11111)
            }
            //LEO LOS FORMATOS SOPORTADOS Y LOS AÑADO AL COMBOBOX DE FORMATOS
            var getMap=wmsXML.getElementsByTagName("GetMap");
            if(getMap[0]!=undefined)
            {
                var formats=getMap[0].getElementsByTagName('Format');
                if(formats.length>=1){
                    $('campoWMS_FORMATO').empty();
                }
                //RECORRO LOS FORMATOS Y LOS AÑADO
                for (var i = 0; i < formats.length; i++) {
                    //SI NO ESTA REPETIDO, LO AÑADO
                    var numVeces=$('campoWMS_FORMATO').getElements("option[value="+formats[i].childNodes[0].nodeValue+"]");
                    if(numVeces.length==0)
                    {
                        new Element('option',{
                            'value':formats[i].childNodes[0].nodeValue,
                            'html':formats[i].childNodes[0].nodeValue
                        }).injectInside($('campoWMS_FORMATO'));
                    }
                }
            }
            else
            {
                new Element('option',{
                    'value':'image/jpeg',
                    'html':'image/jpeg'
                }).injectInside($('campoWMS_FORMATO'));
            }
            //LEO LAS CAPAS
            var layerPpal=wmsXML.getElementsByTagName("Layer")[0];
            if(layerPpal!=undefined)
            {
                var layers=layerPpal.getElementsByTagName("Layer");
                var stringLayers='';
                if(layers.length>=1)
                {
                    $('campoWMS_CAPAS').removeProperty('disabled');
                    $('campoWMS_CAPAS').empty();
                }
                for (var i = 0; i < layers.length; i++) {
                    var nombres=layers[i].getElementsByTagName("Name");
                    var titulos=layers[i].getElementsByTagName("Title");

                    new Element('option',{
                        'value':stringLayers+nombres[0].childNodes[0].nodeValue,
                        'html':stringLayers+titulos[0].childNodes[0].nodeValue
                    }).injectInside($('campoWMS_CAPAS'));
                //stringLayers=stringLayers+nombres[0].childNodes[0].nodeValue+",";
                }
                stringLayers=stringLayers.substr(0,stringLayers.length-1);
            }
            else
            {
                alert(jsIO.cargarTextoSegunIdioma('No ha sido posible cargar las capas de este servicio. Intentelo mas tarde o contacte con el proveedor del servicio seleccionado'));
                $('campoWMS_CAPAS').empty();
            }
            //LEO LAS PROYECCIONES
            //alert(wmsXML.getElementsByTagName("WMS_Capabilities")[0].getAttributte('version'));

            //return this.cab.capaWMS.nombre;

            $('URL').tween('height',340);
            $('URL').store('title',this.cab.capaWMS.nombre);

        }.bind({
            cab:this,
            loadingElem:loadingElem
        }),
        function(error){
            //DESACTIVAR LOADING
            this.loadingElem.dispose();
            //MOSTRAR ALERTA
            alert('NO HA SIDO POSIBLE LEER LA URL INTRODUCIDA');
        }.bind(loadingElem));
    },

    addContenidoLectorWFS:function(){
        if($('URL'))
        {
            $('URL').empty();
            //GENERO LOS TITULOS
            new Element('div',{
                'id':'txtWFS_URL',
                'html':jsIO.cargarTextoSegunIdioma('URL del WFS')
            }).injectInside('URL');
            new Element('div',{
                'id':'txtWFS_CAPAS',
                'html':jsIO.cargarTextoSegunIdioma('CAPAS')
            }).injectInside('URL');
            //GENERO LAS CASILLAS
            /*new Element('input',{
                    'id':'campoWFS_URL',
                    'type':'text',
                    'value':''
                }).injectInside($('URL'));*/
            new Element('input',{
                'id':'campoWFS_URL',
                'type':'text',
                'value':'',
                'selectBoxOptions':visor.serviciosWFS
            }).injectInside($('URL'));
            createEditableSelect($('campoWFS_URL'),$('campoWFS_URL').parentNode.parentNode);
            $('campoWFS_URL').top=0;
            $('campoWFS_URL').left=0;
            new Element('select',{
                'id':'campoWFS_CAPAS',
                'multiple':false,
                'size':4
            }).injectInside($('URL'));
            //GENERO LOS BOTONES
            new Element('div',{
                'id':'btnWFS_URL',
                'events':{
                    'click':function(){
                        $('URL').setStyle('height',100);
                        $('campoWFS_CAPAS').empty();
                        var url=$('campoWFS_URL').getProperty('value');
                        url=jsIO.reformatURL_WFS(url);
                        if(url!=null)
                        {
                            var title=this.leerDataWFSFromURL(url);
                            $('URL').tween('height',340);
                            $('URL').store('title',title);
                        }
                        else
                        {
                            alert(jsIO.cargarTextoSegunIdioma('LA URL ENVIADA NO TIENE UN FORMATO CORRECTO'));
                        }
                    }.bind(this)
                }
            }).injectInside('URL');
            new Element('div',{
                'id':'btnWFS_ADD',
                'html':jsIO.cargarTextoSegunIdioma('CARGAR WFS'),
                'styles':{
                    'text-align':'center'
                },
                'events':{
                    'click':function(){
                            
                        var title=$('URL').retrieve('title');
                        //OBTENGO LA URL
                        var url=$('campoWFS_URL').getProperty('value');
                        //OBTENGO LA LISTA DE CAPAS
                        var cmbCapas=document.getElementById('campoWFS_CAPAS');
                                
                        var dropdownIndex = document.getElementById('campoWFS_CAPAS').selectedIndex;
                        var dropdownText = document.getElementById('campoWFS_CAPAS')[dropdownIndex].text;
                        var dropdownValue = document.getElementById('campoWFS_CAPAS')[dropdownIndex].value;
                        var dropdownNamespace = document.getElementById('campoWFS_CAPAS')[dropdownIndex].id;
                        var dropdownPrefix=null;
                        if (dropdownNamespace!=null && dropdownNamespace!=""){
                            var matriz=dropdownValue.split(":");
                            if (matriz.length>0){
                                dropdownValue=matriz[matriz.length-1]
                                dropdownPrefix=matriz[0];
                            }
                        }
                        if (dropdownValue){
                            //GENERO LA CAPA
                            if(!this.map.getLayer(dropdownValue))
                            {
                                //nombre,url,layer,visibility
                                visor.crearCapaWFS(dropdownText,url,dropdownValue,true,dropdownPrefix,dropdownNamespace);
                                visor.actualizarZIndexMarkers();
                                //LA AÑADO AL ARBOL
                                visor.treeServicios.insert({
                                    text:dropdownValue,
                                    data:{
                                        type:'WMS',
                                        typeButton:'delete'
                                    }
                                });
                                alert(jsIO.cargarTextoSegunIdioma('Servicio WFS cargado'));
                            }
                            else
                            {
                                if(confirm(jsIO.cargarTextoSegunIdioma('Ya tiene una capa con el mismo nombre') + ', ' + jsIO.cargarTextoSegunIdioma('Desea sobreescribirla?')))
                                {
                                    //ELIMINO LA CAPA
                                    this.map.removeLayer(this.map.getLayer(title));
                                    //LA GENERO DE NUEVO
                                    visor.crearCapaWFS(dropdownValue,url,dropdownValue,true,dropdownPrefix,dropdownNamespace);
                                    visor.actualizarZIndexMarkers();
                                    //LA AÑADO AL ARBOL
                                    visor.treeServicios.insert({
                                        text:dropdownValue,
                                        data:{
                                            type:'WMS',
                                            typeButton:'delete'
                                        }
                                    });
                                }
                            }
                        }

                    }.bind(this)
                }
            }).injectInside('URL');
        }
        else
        {
            alert(jsIO.cargarTextoSegunIdioma('NO SE HA PODIDO CARGAR CORRECTAMENTE EL CONTENIDO'));
        }
    },
    leerDataWFSFromURL:function(url){
        new Element('div',{
            'id':'loading_URL_WFS'
        }).injectInside($('URL'));
        var wfsResponse=jsIO.getService({
            'wsName':'CROSS-DOMAIN',
            'url':url
        });
        //PARSEO EL XML
        $('loading_URL_WFS').dispose();
        var wmsXML=jsIO.leerXMLFromString(wfsResponse);
        //LEO EL NOMBRE DE LA CAPA
        var title;
        if(wmsXML.getElementsByTagName('ows:Title').length>0)
        {
            title=wmsXML.getElementsByTagName('ows:Title')[0].childNodes[0].nodeValue;
        }
        else if(wmsXML.getElementsByTagName('wfs:Title').length>0)
        {
            title=wmsXML.getElementsByTagName('wfs:Title')[0].childNodes[0].nodeValue;
        }
        //LEO LAS CAPAS Y LOS SRS
                
                
        //LEO LAS CAPAS
        var layers=wmsXML.getElementsByTagName("FeatureType");
        if(layers==undefined || layers.length==0)
        {
            layers=wmsXML.getElementsByTagName("wfs:FeatureType");
        }
        if(layers!=undefined && layers.length>0)
        {
            var stringLayers='';
            if(layers.length>=1)
            {
                $('campoWFS_CAPAS').removeProperty('disabled');
                $('campoWFS_CAPAS').empty();
            }
            for (var i = 0; i < layers.length; i++) {
                        
                var nombres=layers[i].getElementsByTagName("Name");
                if(nombres==undefined || nombres.length==0)
                {
                    nombres=layers[i].getElementsByTagName("wfs:Name");
                }
                var titulos=layers[i].getElementsByTagName("Title");
                if(titulos==undefined || titulos.length==0)
                {
                    titulos=layers[i].getElementsByTagName("wfs:Title");
                }
                var nombre="";
                var titulo="";
                if ((nombres[0]) && (nombres[0].childNodes[0])) nombre=nombres[0].childNodes[0].nodeValue;
                if ((titulos[0]) && (titulos[0].childNodes[0])) titulo=titulos[0].childNodes[0].nodeValue;
                        
                        
                var prefijo=nombre.substr(0,nombre.indexOf(":",0));
                var nameSpaceFeature=layers[i].getAttribute("xmlns:" + prefijo);
                        
                if (!titulo) titulo=titulo + "(" + nombre + ")"; //en caso de que no aparezca el Title se coge el Name
                else if (this.estaRepetido(layers,titulo,i)) titulo=nombre;//en caso de que el Title esté duplicado se coge el name
                //Añadimos la capa al select para su visualización

                new Element('option',{
                    'id': nameSpaceFeature,//prefijo + "=" + nameSpaceFeature,
                    'value':nombre,
                    'html':titulo
                }).injectInside($('campoWFS_CAPAS'));
            //stringLayers=stringLayers+nombres[0].childNodes[0].nodeValue+",";
            }
            stringLayers=stringLayers.substr(0,stringLayers.length-1);

        }
        else
        {
            alert(jsIO.cargarTextoSegunIdioma('No ha sido posible cargar las capas de este servicio. Intentelo mas tarde o contacte con el proveedor del servicio seleccionado'));
            $('campoWFS_CAPAS').empty();
            new Element('option',{
                'value':'-',
                'html':jsIO.cargarTextoSegunIdioma('No se han podido cargar las capas')
            }).injectInside($('campoWFS_CAPAS'));
        }
        return title;
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
    },
    leerDataFromURL:function(url){



        //

        new Element('div',{
            'id':'loading_URL_WMS'
        }).injectInside($('URL'));
        var wmsResponse=jsIO.getService({
            'wsName':'CROSS-DOMAIN',
            'url':url
        });
        //PARSEO EL XML
        $('loading_URL_WMS').dispose();
        var wmsXML=jsIO.leerXMLFromString(wmsResponse);
        //LEO EL NOMBRE DE LA CAPA
        var title;
        if(wmsXML.getElementsByTagName('Title') && wmsXML.getElementsByTagName('Title')[0])
        {
            title=wmsXML.getElementsByTagName('Title')[0].childNodes[0].nodeValue;
        }
        else
        {
            title='defaultName_'+Math.floor(Math.random()*11111)
        }
        //LEO LOS FORMATOS
        var getMap=wmsXML.getElementsByTagName("GetMap");
        if(getMap[0]!=undefined)
        {
            var formats=getMap[0].getElementsByTagName('Format');
            if(formats.length>=1)
            {
                $('campoWMS_FORMATO').empty();
            //$('campoWMS_FORMATO').removeProperty('disabled');
            }
            for (var i = 0; i < formats.length; i++) {
                var numVeces=$('campoWMS_FORMATO').getElements("option[value="+formats[i].childNodes[0].nodeValue+"]");
                if(numVeces.length==0)
                {
                    new Element('option',{
                        'value':formats[i].childNodes[0].nodeValue,
                        'html':formats[i].childNodes[0].nodeValue
                    }).injectInside($('campoWMS_FORMATO'));
                }
            }
        }
        else
        {
            new Element('option',{
                'value':'image/jpeg',
                'html':'image/jpeg'
            }).injectInside($('campoWMS_FORMATO'));
        }
        //LEO LAS CAPAS Y LOS SRS
        var layerPpal=wmsXML.getElementsByTagName("Layer")[0];
        if(layerPpal!=undefined)
        {
            //LEO LAS CAPAS
            var layers=layerPpal.getElementsByTagName("Layer");
            var stringLayers='';
            if(layers.length>=1)
            {
                $('campoWMS_CAPAS').removeProperty('disabled');
                $('campoWMS_CAPAS').empty();
            }
            for (var i = 0; i < layers.length; i++) {
                var nombres=layers[i].getElementsByTagName("Name");
                var titulos=layers[i].getElementsByTagName("Title");

                new Element('option',{
                    'value':stringLayers+nombres[0].childNodes[0].nodeValue,
                    'html':stringLayers+titulos[0].childNodes[0].nodeValue
                }).injectInside($('campoWMS_CAPAS'));
            //stringLayers=stringLayers+nombres[0].childNodes[0].nodeValue+",";
            }
            stringLayers=stringLayers.substr(0,stringLayers.length-1);

            //LEO LOS SRS
            var proyeccionesWMS=wmsXML.getElementsByTagName("SRS");
        }
        else
        {
            alert(jsIO.cargarTextoSegunIdioma('No ha sido posible cargar las capas de este servicio. Intentelo mas tarde o contacte con el proveedor del servicio seleccionado'));
            $('campoWMS_CAPAS').empty();
            new Element('option',{
                'value':'-',
                'html':jsIO.cargarTextoSegunIdioma('No se han podido cargar las capas')
            }).injectInside($('campoWMS_CAPAS'));
        }
        return title;
    },
    
    loadAmbitos:function(){
        $('lblPlanEntidad').setStyles({
            'background-image':'url(styles/images/ajax-loader_busquedas.gif)',
            'background-repeat':'no-repeat',
            'background-position':'right 2px'
        });
        jsIO.getServiceAsync({
            'wsName':'GET_AMBITOS',
            'idioma':visor.Lang
        },
        function(response){
            $('lblPlanEntidad').setStyles({
                'background':'transparent'
            });
            //LEO LOS AMBITOS ENCONTRADOS
            var arrayAmbitos=new Array();
            var xmlAmbitos=jsIO.leerXMLFromString(response);
            if(xmlAmbitos && xmlAmbitos.getElementsByTagName('AMBITOS'))
            {
                var ambitos=xmlAmbitos.getElementsByTagName('AMBITOS')[0];
                if(ambitos)
                {
                    for(var i=0;i<ambitos.getElementsByTagName('AMBITO').length;i++)
                    {
                        var ambito=ambitos.getElementsByTagName('AMBITO')[i];
                        var id=ambito.getElementsByTagName('id')[0].childNodes[0].nodeValue;
                        var nombre=ambito.getElementsByTagName('nombre')[0].childNodes[0].nodeValue;
                        arrayAmbitos.push({
                            id:id,
                            nombre:nombre
                        });
                    }
                }
                else
                {
                    //jsBUSQUEDAS.generarLineaResultado('err','NO SE HAN ENCONTRADO AMBITOS',null);
                    $('select_ambitos').empty();
                    new Element('option',{
                        'value':'-',
                        'html':'-'
                    }).injectInside($('select_ambitos'));
                }
            }

            //AÑADO LOS AMBITOS ENCONTRADOS

            $('select_ambitos').empty();
            var posAmbito=-1;
            for (var i = 0; i < arrayAmbitos.length; i++) {
                if (arrayAmbitos[i].id == visor.idAmbitoBusquedas)
                {
                    posAmbito=i;
                }
            }
            if(posAmbito!=-1)
            {
                new Element('option',{
                    'value':arrayAmbitos[posAmbito].id,
                    'html':arrayAmbitos[posAmbito].nombre
                }).injectInside($('select_ambitos'));

            }
            else
            {
                for (var i = 0; i < arrayAmbitos.length; i++) {
                    new Element('option',{
                        'value':arrayAmbitos[i].id,
                        'html':arrayAmbitos[i].nombre
                    }).injectInside($('select_ambitos'));
                }
            }
                

        },
        function(error){
            $('lblPlanEntidad').setStyles({
                'background':'transparent'
            });
            $('select_ambitos').empty();
            new Element('option',{
                'value':'-',
                'html':'-'
            }).injectInside($('select_ambitos'));
        });
            
    },
    loadImageLegends:function(zonaActiva){
        if(this.map)
        {
            var layers=this.map.layers;
            if(layers.length>=1)
            {
                var listaImagenesLeyenda=0;
                for(var i=0;i<layers.length;i++)
                {
                    var layer=layers[i];
                    if(layer.isBaseLayer==false && layer.CLASS_NAME=='OpenLayers.Layer.WMS' && layer.url && this.checkHasLegendGraphic(layer)==true)
                    {

                        var div=new Element('div',{
                            'id':'x_'+layer.id
                        });
                        new Element('img',{
                            'id':'imageLegend_'+layer.id,
                            'src':layer.url+"request=GetLegendGraphic&LAYER="+layer.params.LAYERS+"&FORMAT=image/png",
                            'title':layer.id,
                            'alt':layer.id,
                            'height':'100%'
                        }).injectInside(div);
                        div.injectInside(zonaActiva);
                        listaImagenesLeyenda=listaImagenesLeyenda+1;
                            
                    }
                }
                if(listaImagenesLeyenda>0)
                {
                    //AÑADO EL MOOFLOW
                    var mf = new MooFlow(zonaActiva, {
                        //startIndex: 1,
                        useSlider: true,
                        useAutoPlay: true,
                        useCaption: true,
                        useResize: true,
                        useMouseWheel: true,
                        useWindowResize: true,
                        useKeyInput: true
                    //                                        onClickView: function(obj){
                    //                                                Milkbox.ss(obj.href, obj.title + ' - ' + obj.alt);
                    //                                        }

                    //heightRatio:'100%'
                    });
                }
                else
                {
                    zonaActiva.set('html','<BR><BR><BR><center>'+jsIO.cargarTextoSegunIdioma('NINGUNA DE LAS CAPAS ACTIVAS TIENEN LEYENDAS QUE MOSTRAR')+'</center>');
                }
            }
            else
            {
                zonaActiva.set('html','<BR><BR><BR><center>'+jsIO.cargarTextoSegunIdioma('NO HAY LEYENDAS QUE MOSTRAR')+'</center>')
            }

        }
    },
    checkHasLegendGraphic:function(layer){
        var url=jsIO.reformatURL_WMS_for_Legend(layer.url);
        if(url!=null)
        {
            var response=jsIO.getService({
                'wsName':'CROSS-DOMAIN',
                'url':url
            }, false)
            if(!response.contains('REQUEST INEXISTENTE'))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }

    },
    selectBusqueda:function(opcion){
         
        this.activado=opcion;
        var visible={
            'visibility':'visible'
        };
        var invisible={
            'visibility':'hidden'
        };

        switch (opcion){
         
            case 'direccion':

                //            $('lblDireccion').setStyles(visible);
                //            $('selectTipoVia').setStyles(visible);
                //            $('selectDireccion').setStyle('visibility','visible');
                //            $('lblNumero').setStyle('visibility','visible');
                //            $('selectNumero').setStyle('visibility','visible');
                //            $('lblRefCat').setStyle('visibility','hidden');
                //            $('select_refCat').setStyle('visibility','hidden');
                //            $('lblPoligono').setStyle('visibility','hidden');
                //            $('select_poligono').setStyle('visibility','hidden');
                //            $('lblParcela').setStyle('visibility','hidden');
                //            $('select_parcela').setStyle('visibility','hidden');
                $('contenedorBusqDireccion').setStyles(visible);
                $('contenedorBusqCatastro').setStyles(invisible);
                $('contenedorBusqPolParc').setStyles(invisible);

                break;

            case 'refCat':
                //                $('lblDireccion').setStyle('visibility','hidden');
                //                $('selectTipoVia').setStyle('visibility','hidden');
                //                $('selectDireccion').setStyle('visibility','hidden');
                //                $('lblNumero').setStyle('visibility','hidden');
                //                $('selectNumero').setStyle('visibility','hidden');
                //                $('lblRefCat').setStyle('visibility','visible');
                //                $('select_refCat').setStyle('visibility','visible');
                //                $('lblPoligono').setStyle('visibility','hidden');
                //                $('select_poligono').setStyle('visibility','hidden');
                //                $('lblParcela').setStyle('visibility','hidden');
                //                $('select_parcela').setStyle('visibility','hidden');
                $('contenedorBusqDireccion').setStyles(invisible);
                $('contenedorBusqCatastro').setStyles(visible);
                $('contenedorBusqPolParc').setStyles(invisible);
                break;
            case 'polparc':
                //                $('lblDireccion').setStyle('visibility','hidden');
                //                $('selectTipoVia').setStyle('visibility','hidden');
                //                $('selectDireccion').setStyle('visibility','hidden');
                //                $('lblNumero').setStyle('visibility','hidden');
                //                $('selectNumero').setStyle('visibility','hidden');
                //                $('lblRefCat').setStyle('visibility','hidden');
                //                $('select_refCat').setStyle('visibility','hidden');
                //                $('lblPoligono').setStyle('visibility','visible');
                //                $('select_poligono').setStyle('visibility','visible');
                //                $('lblParcela').setStyle('visibility','visible');
                //                $('select_parcela').setStyle('visibility','visible');
                $('contenedorBusqDireccion').setStyles(invisible);
                $('contenedorBusqCatastro').setStyles(invisible);
                $('contenedorBusqPolParc').setStyles(visible);
                break;
        }
    },
    preloadBusquedas:function(){
        //UBICO EL BLOQUEADOR DE CATASTRO
        $('block_catastro').setOpacity(0.2);
        this.loadBlockerCatastro('PROVINCIA');
        //RELLENAR PROVINCIAS
        jsBUSQUEDAS.findProvincias();
        // Se activa el tipo de busqueda que se desea
        $('selectTipoBusq').addEvent('change',function(){
            this.cab.selectBusqueda( $('selectTipoBusq').getProperty('value'))
        }.bind({
            cab:this
        }));

        //READ AMBITOS
        setTimeout(function()
        {
            this.cab.loadAmbitos(this.idioma)
        }.bind({
            cab:this,
            idioma:visor.Lang
            }),
        600);
        //AÑADO LOS EVENTOS DE LOS BOTONES DE BUSQUEDAS

        $('chkCombinado').addEvent('change',function(){
            if(this.checked)
            {
                $('btnEnviarPlan').fade(0);
            }
            else
            {
                $('btnEnviarPlan').fade(1);
            }
        });

        $('btnEnviarGoogle').addEvent('click',function(){
            jsBUSQUEDAS.findByGoogle($('select_google').getProperty('value'));
        });
        $('btnEnviarAmbito').addEvent('click',function(){
            if($('select_ambitos').getProperty('value')!='-')
            {
                var bounds=jsBUSQUEDAS.obtenerExtentAmbito($('select_ambitos').getProperty('value'));
                if(bounds)
                {
                    visor.map.zoomToExtent(bounds,false);
                }
            }
            else
            {
                alert('Seleccione un ámbito o espere que cargue la lista completa de ambitos. Gracias')
            }
        });
        $('btnEnviarEntidad').addEvent('click',function(){
            if($('chkCombinado').checked)
            {
                jsBUSQUEDAS.findByEntidadSegunPlan($('select_ambitos').getProperty('value'),
                    $('select_entidad').getProperty('value'),
                    $('select_plan').getProperty('value'));
            }
            else{
                jsBUSQUEDAS.findByEntidad($('select_ambitos').getProperty('value'),
                    $('select_entidad').getProperty('value'));
            }
        });
        $('btnEnviarPlan').addEvent('click',function(){
            jsBUSQUEDAS.findByPlan($('select_ambitos').getProperty('value'),
                $('select_plan').getProperty('value'));
        });

        //BUSQUEDA DE CATASTRO

        $('btnEnviarCatastro').addEvent('click',function(){
            var activado=$('selectTipoBusq').getProperty('value');
            var municipio=jsIO.cargarTextoSegunIdioma($('select_municipio').getProperty('value'));
            var provincia=jsIO.cargarTextoSegunIdioma($('select_provincia').getProperty('value'));

            switch (activado){
                case 'direccion':
                    jsBUSQUEDAS.findByCatastro_Direccion(
                        provincia, //this.provincia,
                        municipio,//this.municipio,
                        jsIO.cargarTextoSegunIdioma($('selectTipoVia').getProperty('value')),
                        jsIO.cargarTextoSegunIdioma($('select_direccion').getProperty('value')),
                        jsIO.cargarTextoSegunIdioma($('select_numero').getProperty('value')));
                    break;
                case 'refCat':
                    jsBUSQUEDAS.findByCatastro_RefCat(
                        provincia,//this.provincia,
                        municipio, //this.municipio,
                        jsIO.cargarTextoSegunIdioma($('select_refCat').getProperty('value')));
                    break;
                case 'polparc':
                    jsBUSQUEDAS.findByCatastro_PolParc(
                        provincia, //this.provincia,
                        municipio,//this.municipio,
                        jsIO.cargarTextoSegunIdioma($('select_poligono').getProperty('value')),
                        $('select_parcela').getProperty('value'));
                    break;
            }


        //}.bind({provincia:visor.provincia,municipio:visor.ambito, activado:this.activado})
        }.bind({
            activado:this.activado
            }))

        $('lblProvincia').set('html',jsIO.cargarTextoSegunIdioma($('lblProvincia').get('html')));
        $('lblMunicipio').set('html',jsIO.cargarTextoSegunIdioma($('lblMunicipio').get('html')));

        //AÑADO LOS EVENTOS A LOS FILTROS DE LOS RESULTADOS
        $('filtroZoom').addEvents({
            'click':function(){
                if($('resultados').getElements('div').length>0){
                    if(this.resultadoSeleccionado!=-1){
                        //OBTENGO LA POSICION DEL ELEMENTO SELECCIONADO
                        var pos=0;
                        var i=0;
                        $('resultados').getElements('div').each(function(item){
                            if(item.retrieve('id_elem')==this.resultadoSeleccionado){
                                pos=i;
                            }
                            i++;
                        }.bind({
                            resultadoSeleccionado:this.resultadoSeleccionado
                            }));
                        //LANZO EL EVENTO DE LA POSICION POS
                        this.eventoFiltroZoom[pos]();
                    }
                    else
                    {
                        alert(jsIO.cargarTextoSegunIdioma("seleccione un resultado primero"))
                    }
                                            
                }
                else
                {
                    alert(jsIO.cargarTextoSegunIdioma("no existen resultados para realizar esta operacion"))
                }
            }.bind(this)
        });
        $('filtroFicha').addEvents({
            'click':function(){
                if($('resultados').getElements('div').length>0){
                    if(this.resultadoSeleccionado!=-1){
                        //OBTENGO LA POSICION DEL ELEMENTO SELECCIONADO
                        var pos=0;
                        var tiene_geom=undefined;
                        var i=0;
                        $('resultados').getElements('div').each(function(item){
                            if(item.retrieve('id_elem')==this.resultadoSeleccionado){
                                pos=i;
                            }
                            i++;
                        }.bind({
                            resultadoSeleccionado:this.resultadoSeleccionado
                            }));
                        //LANZO EL EVENTO DE LA POSICION POS
                        this.eventoFiltroFicha[pos]();
                    }
                    else
                    {
                        alert(jsIO.cargarTextoSegunIdioma("seleccione un resultado primero"))
                    }
                }
                else
                {
                    alert(jsIO.cargarTextoSegunIdioma("no existen resultados para realizar esta operacion"))
                }
            }.bind(this)
        });
        //AÑADO EL FILTRO DE SELECTOR DE FICHA PARA LOS RESULTADOS
        $('lblFiltroFicha').set('html',jsIO.cargarTextoSegunIdioma(this.fichaAConsultarPorFiltro));
        $('selectTipoFichaParaFiltro').addEvents({
            'click':function(e){
                e.stopPropagation();
                //GENERO EL CONTAINER DE LAS FICHAS
                if($('containerFichasParaFiltros')){
                    $('containerFichasParaFiltros').dispose()
                }
                else{

                    new Element('div',{
                        'id':'containerFichasParaFiltros'
                    }).injectInside($('filtros'));
                    //GENERO LOS DISTINTOS TIPOS DE FICHAS
                    var arrayFichas=this.tiposDeFichas.split(';');
                    for (var i = 0; i < arrayFichas.length; i++) {
                        new Element('div',{
                            'id':'tipoFichaFiltro_'+arrayFichas[i],
                            'html':jsIO.cargarTextoSegunIdioma(arrayFichas[i]),
                            'class':'slotTipoFichaFiltro',
                            'events':{
                                'click':function(){
                                    //ALMACENO EL TIPO DE FICHA SELECCINADA
                                    this.cab.fichaAConsultarPorFiltro=this.tipoFicha;
                                    //MUESTRO EL TEXTO DE LA FICHA SELECCIONADA EN EL BOTON DE MOSTREAR FICHA
                                    $('lblFiltroFicha').set('html',jsIO.cargarTextoSegunIdioma(this.tipoFicha));
                                    //OCULTO EL DESPLEGABLE CON LOS TIPOS DE FICHAS TRAS HABER SELECCIONADO UNO
                                    $('containerFichasParaFiltros').dispose();
                                }.bind({
                                    cab:this,
                                    tipoFicha:arrayFichas[i]
                                    })
                            }
                        }).injectInside($('containerFichasParaFiltros'))
                        .store('tipoFicha',arrayFichas[i]);
                    }
                }
            //                                            //SI TIENE GEOMETRIA MUESTRO EL COMBO DE DISTINTAS FICHAS
            //                                            if($('selectTipoFichaParaFiltro').retrive('tiene_geom')!=undefined){
            //                                                $('selectTipoFichaParaFiltro').fade(1);
            //                                                $('selectTipoFichaParaFiltro').getParent().set('morph',{duration:100});
            //                                                if(!this.menuFichasFiltrosOpen){
            //                                                    $('selectTipoFichaParaFiltro').getParent().morph({height:150});
            //                                                    this.menuFichasFiltrosOpen=true;
            //                                                }
            //                                                else
            //                                                {
            //                                                    $('selectTipoFichaParaFiltro').getParent().morph({height:30});
            //                                                    this.menuFichasFiltrosOpen=false;
            //                                                }
            //                                            }
            //                                            else
            //                                            {
            //                                                //SI NO TIENE GEOMETRIAS DESACTIVO EL COMBO PARA QUE SOLO SE PUEDAN VER LA PRIMERA FICHA
            //                                                    $('selectTipoFichaParaFiltro').fade(0);
            //                                            }
            }.bind(this)
        })
        $('filtroCaptura').addEvents({
            'click':function(){
                if($('resultados').getElements('div').length>0){
                    if(this.resultadoSeleccionado!=-1){
                        //OBTENGO LA POSICION DEL ELEMENTO SELECCIONADO
                        var pos=0;
                        var tiene_geom=undefined;
                        var i=0;
                        $('resultados').getElements('div').each(function(item){
                            if(item.retrieve('id_elem')==this.resultadoSeleccionado){
                                pos=i;
                            }
                            i++;
                        }.bind({
                            resultadoSeleccionado:this.resultadoSeleccionado
                            }));
                        //MUESTRO EL ICONO DE LOADING PARA EL EVENTO DE CAPTURA
                        new Element('div',{
                            'id':'loading_captura',
                            'class': 'loading_captura'
                        }).injectInside($('filtroCaptura'))
                        //LANZO EL EVENTO DE LA POSICION POS
                        this.eventoFiltroCaptura[pos]();

                    }
                    else
                    {
                        alert(jsIO.cargarTextoSegunIdioma("seleccione un resultado primero"))
                    }
                }
                else
                {
                    alert(jsIO.cargarTextoSegunIdioma("no existen resultados para realizar esta operacion"))
                }
            }.bind(this)
        });
    },
    loadBlockerCatastro:function(opcion){
        switch(opcion){
            case 'PROVINCIA':
                //                    $('block_catastro').morph({
                //                       'top':62,
                //                       'height':199
                //                    });
                //                    $('block_catastro').fade(0.2);
                //                    $('block_catastro').addEvent('click',function(){
                //                        alert(jsIO.cargarTextoSegunIdioma('SELECCIONE PRIMERO UNA PROVINCIA'));
                //                    })
                break;
            case 'MUNICIPIO':
                //                    $('block_catastro').morph({
                //                       'top':87,
                //                       'height':174
                //                    });
                //                    $('block_catastro').fade(0.2);
                //                    $('block_catastro').addEvent('click',function(){
                //                        alert(jsIO.cargarTextoSegunIdioma('SELECCIONE PRIMERO UNA MUNICIPIO'));
                //                    })
                break;
            case 'ALL':
                //                    $('block_catastro').morph({
                //                       'top':256,
                //                       'height':2
                //                    });
                //                    $('block_catastro').fade(0);
                break;
        }
    }
});

