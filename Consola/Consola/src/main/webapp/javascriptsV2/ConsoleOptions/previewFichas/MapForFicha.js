var MapForFicha=new Class({
    Implements:[Options],
    options:{
        idFicha:null,
        idTramite:null,
        target:$('panelPreviewFichas')
    },
    initialize:function(options){
        this.setOptions(options);
        if(this.options.idTramite)
        {
            this.options.extensionTramite=this.getExtentFromTramite();
            if(this.options.extensionTramite && this.options.extensionTramite!='')
            {

                if(this.options.target){
                    this.options.target.empty();
                }
                //DEFINO LOS PARAMETROS GENERALES
                //OpenLayers.ImgPath='styles/images/';
                OpenLayers.Util.onImageLoadErrorColor = 'transparent';
                OpenLayers.IMAGE_RELOAD_ATTEMPTS = 2;
         
                //OBTENGO LA PROJECTION
                this.options.projection=runService({
                    'wsName':'GET_PROJECTION'
                });
                //GENERO EL MAPA
                var toolBarHeader=new Element('div',{
                    'class':'panel-header',
                    'style':'display:block;',
                    'id':'toolBarMapaFichas'
                }).injectInside($(this.options.target.getProperty('id')));
                this.options.mapa = new OpenLayers.Map(this.options.target.getProperty('id'),{
                    'projection':new OpenLayers.Projection(this.options.projection),
                    'displayProjection':new OpenLayers.Projection(this.options.projection),
                    'resolutions':[1222.9924523925781, 611.4962261962891, 305.74811309814453, 152.87405654907226, 76.43702827453613, 38.218514137268066, 19.109257068634033, 9.554628534317017, 4.777314267158508, 2.388657133579254, 1.194328566789627, 0.5971642833948135, 0.29858214169740677, 0.14929107084870338, 0.07464553542435169, 0.037322767712175846, 0.018661383856087923],//[156543.03390625, 78271.516953125, 39135.7584765625, 19567.87923828125, 9783.939619140625, 4891.9698095703125, 2445.9849047851562, 1222.9924523925781, 611.4962261962891, 305.74811309814453, 152.87405654907226, 76.43702827453613, 38.218514137268066, 19.109257068634033, 9.554628534317017, 4.777314267158508, 2.388657133579254, 1.194328566789627, 0.5971642833948135, 0.29858214169740677, 0.14929107084870338, 0.07464553542435169, 0.037322767712175846, 0.018661383856087923, 0.009330691928043961, 0.004665345964021981],
                    'units':'m',
                    'allOverlays':true,
                    'controls':[],
                    'maxExtent':this.getBoundsFromWKT(this.options.extensionTramite),
                    //'maxExtent':new OpenLayers.Bounds(-179,-90,179,90).transform(new OpenLayers.Projection("EPSG:4326"),new OpenLayers.Projection(this.options.projection)),
                    'restrictedExtent':this.getBoundsFromWKT(this.options.extensionTramite),
                    'tileSize':new OpenLayers.Size(256,256)
                });

                this.addMapLayers();
                new ZoomBar(this.options.mapa);
                this.options.mapa.zoomToExtent(this.getBoundsFromWKT(this.options.extensionTramite),false);
                this.options.mapa.zoomToExtent(this.getBoundsFromWKT(this.options.extensionTramite),false);
                new ToolBar(this.options.mapa,toolBarHeader,'panelPreviewFichas')
                .addButton(new Element('div',
                {
                    'id': 'toolPreviewFicha',
                    'title': 'Pedir Ficha',
                    'type': OpenLayers.Control.TYPE_TOOL,
                    'events': {
                        'click': function () {
                            $(this.map.viewPortDiv).setStyle('cursor','url(styles/images/cursores/question.cur),move');
                            var urlFichas=runService({
                                wsName:'GET_URL_FICHAS'
                            });
                            Array.each(this.map.controls, function(control, index){
                               control.deactivate(); 
                            });
                            var clickUrbr = new OpenLayers.Control.MapClick({
                                onClick: function (e) {
                                    var puntoXY = this.map.getLonLatFromViewPortPx(this.map.getLayerPxFromViewPortPx(e.xy));
                                    window.open(urlFichas + '?Ficha=' + this.idFicha + 
                                                            '&X=' +  puntoXY.lon + 
                                                            '&Y=' +  puntoXY.lat + 
                                                            '&SRS=' + this.map.projection  );
                                    clickUrbr.deactivate();
                                    this.map.removeControl(clickUrbr);
                                } .bind(this)
                            });
                            this.map.addControl(clickUrbr);
                            clickUrbr.activate();
                        } .bind({
                            map: this.options.mapa,
                            idFicha:this.options.idFicha
                        })
                    }
                }));
                            
            }
        }
        else
        {
            alert('NO SE HA PODIDO OBTENER EL ID DEL TRAMITE DE LA FICHA');
        }
    },
    addWMSTramite:function(){
        //ELIMINO LA CAPA ANTERIOR
        this.removeWMSTramite();
        if(this.getBoundsFromWKT(this.options.extensionTramite))
        {
            //OBTENGO LOS ATRIBUTOS DEL WMS
            this.options.atributesWMSTramite=JSON.decode(runService({
                'wsName': 'GET_WMS_SERVER_DE_ENT',
                'zona': this.options.type
            }));
            //COMPRUEBO SI LOS DATOS SON CORRECTOS
            if (!this.options.atributesWMSTramite.url){
                alert(this.options.atributesWMSTramite);
                return;
            }
            //GENERO EL WMS 
            this.options.WMSTramite = new OpenLayers.Layer.WMS(
                "Trámite",
                this.options.atributesWMSTramite.url,
                {
                    'layers': this.options.atributesWMSTramite.layers,
                    'transparent':true,
                    'format':this.options.atributesWMSTramite.format
                },
                {
                    'isBaseLayer':false,
                    'displayInLayerSwitcher':true,
                    //'projection':projectionWMS,
                    'gutter':0,
                    //transitionEffect: 'resize',
                    //'reproject':true,
                    'buffer':0,
                    'singleTile':false,
                    'visibility':true
                }
                );
            //ANADO EL FILRTO CORRESPONDIETNE
            this.options.WMSTramite.setOpacity(0.5);
            this.options.WMSTramite.CQL_FILTER='idtramite='+this.options.idTramite;

            //ASIGNO LA MISMA ID QUE EL NOMBRE DEL WMS
            this.options.WMSTramite.id='trm' + this.options.idTramite;
            //REASIGNO LA CAPA A LA VARIABLE GLOBAL WMS
            //REDEFINO EL WMS PARA INCORPORAR LOS FILTROS EN LAS PETICIONES
            this.redefinirWMSTramite(this.options.WMSTramite);
            //AVISO AL USUARIO DE QUE SE ESTA PROCESANDO LA CAPA WMS


            //ANADO LA CAPA AL MAPA
            this.options.mapa.addLayer(this.options.WMSTramite);

        }
    },
    removeWMSTramite:function(){
        if(this.options.WMSTramite)
        {
            this.options.mapa.removeLayer(this.options.WMSTramite,false);
        }
    },
    addWMS:function(atributos)
    {

        var layerWMS = new OpenLayers.Layer.WMS(
            atributos.nombre,
            atributos.url,
            {
                'layers': atributos.layers,
                'transparent':true,
                'format':atributos.format
            },
            {
                'isBaseLayer':false,
                'displayInLayerSwitcher':true,
                //'projection':projectionWMS,
                'gutter':0,
                //transitionEffect: 'resize',
                //'reproject':true,
                'buffer':0,
                'singleTile':false,
                'visibility':atributos.visible=='true'
            }
            );
        //COMPRUEBO SI ES UN PLANO
        layerWMS.setOpacity(1);
        layerWMS.id=atributos.nombre;
        //redefinirWMS(layerWMS);
        this.options.mapa.addLayer(layerWMS);
    },
    addMapLayers:function(){
        //this.options.mapa.addLayer(new OpenLayers.Layer.OSM( "OpenStreetMap"));
        var WMS=JSON.decode(runService({
            'wsName':'GET_CAPAS_WMS'
        }));
        if (WMS && WMS.data){
            for (var i=0; i<WMS.data.length;i++){
                var atributos=WMS.data[i];//url, layers, format, visible
                this.addWMS(atributos);
            }
        }

        this.addWMSTramite();
    },
    addMapControls:function(){

        this.options.mapa.addControl(new OpenLayers.Control.Navigation());
        var panZoom=new OpenLayers.Control.PanZoom();
        //panZoom._removeButton("zoomworld");
        this.options.mapa.addControl(panZoom);
        var ctlLayer=new OpenLayers.Control.LayerSwitcher();
        ctlLayer.ascending=false;
        this.options.mapa.addControl(ctlLayer);
    },
    getBoundsFromWKT:function(WKT){
        var format=new OpenLayers.Format.WKT();
        var feature=format.read(WKT);
        return feature.geometry.getBounds();
    },
    /*
     * FUNCTION QUE OBTIENE LA EXTENSION DE UN TRAMITE SEGUN EL ID DEL TRAMITE
     **/
    getExtentFromTramite:function(){
        return runService({
            'wsName':'GET_EXTENSION_DE_TRAMITE',
            'idTramite':this.options.idTramite
        });
    },
    redefinirWMSTramite:function(wms){
        wms.getURL= function (bbs) {
            bbs = wms.adjustBounds(bbs);

            var imageSize = wms.getImageSize();
            var newParams = {
                'BBOX': this.encodeBBOX ?  bbs.toBBOX() : bbs.toArray(),
                'WIDTH': imageSize.w,
                'HEIGHT': imageSize.h,
                'CQL_FILTER': wms.CQL_FILTER,
                'FEATUREID': wms.FEATUREID
            };
            var requestString = this.getFullRequestString(newParams);

            return requestString;
        };
    }
});
