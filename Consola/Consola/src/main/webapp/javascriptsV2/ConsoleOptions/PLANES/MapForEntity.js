var globalMapForEntity=null;
var idAmbitoDeEntidadMapForEntity;
var idEntidadMapForEntity;
var targetMapForEntity;
var typeMapForEntity;
var contextoMapaPreviewEntidades =null;
function preInitializeMapForEntity(idEntidadParam, targetParam, typeParam, async, idTabContainer, accion){
	
    var dev;
    var response;
	
    if(idEntidadParam)
    {
        idEntidadMapForEntity = idEntidadParam;
        targetMapForEntity = targetParam;
        typeMapForEntity = typeParam;
		
        if(typeParam=='RPM')
        {
            if (async){
        		
                runServiceAsync({
                    'wsName' : 'GET_ID_AMBITO_DE_ENTIDAD',
                    'idEntidad' : idEntidadParam
                },
                function(response){
                    if (response != null){
        					
                        if (accion){
                            accion(response);
                        }
        					
                        if(idTabContainer && $(idTabContainer)){
                            $(idTabContainer).setStyle('display', '');
                        }    	    		
                    }
                    else {
                        if (accion){
                            accion(undefined);
                        }
                    }
                });
            } else {
        		
                response = runService({
                    'wsName' : 'GET_ID_AMBITO_DE_ENTIDAD',
                    'idEntidad' : idEntidadParam
                });	
        		
                if (response != null){
                    dev = response;
                    if(idTabContainer && $(idTabContainer)){
                        $(idTabContainer).setStyle('display', '');
                    } 
                }
        		
            }
        
        }
    }
	
    return dev;
}

function addMapForEntity(){

    globalMapForEntity=new MapForEntity({
        idAmbitoDeEntidad:idAmbitoDeEntidadMapForEntity,
        idEntidad:idEntidadMapForEntity,
        target:targetMapForEntity,
        type:typeMapForEntity
    });                	   
    
/* if(mapRPM)
    {
        setTimeout(function(){
            mapRPM.zoomToExtent(boundsEntidad,true);
            mapRPM.zoomToExtent(boundsEntidad,true);
        },600);
        
    } else {
        if(mapVal) {
            setTimeout(function(){
                mapVal.zoomToExtent(boundsEntidad,true);
                mapVal.zoomToExtent(boundsEntidad,true);
            },600);
        }
    }  */
    
}

var MapForEntity=new Class({
    Implements:[Options],
    options:{
        idAmbitoDeEntidad:undefined,
        idEntidad:'',
        target:$('mapGeom'),
        type:'RPM' //RPM or VAL
    },
    setContext:function(wmc){
        //COGEMOS SOLO LA VISIBILIDAD Y TRANSPARENCIA DE LAS CAPAS
        wmc.layersContext.each(function (layerContext) {
            var layer = this.options.mapa.getLayer(layerContext.title);
            if (layer){
                layer.setVisibility(layerContext.visibility);
                layer.setOpacity(layerContext.opacity);
            }
        }.bind(this));
    },
    initialize:function(options){
        this.setOptions(options);
        this.options.idAmbitoDeEntidad=this.getAmbitoFromEntity();
        if(this.options.idAmbitoDeEntidad)
        {
            this.options.extensionAmbito=this.getExtentFromAmbito();
            if(this.options.extensionAmbito && this.options.extensionAmbito!='')
            {
                this.options.boundsEntidad=this.getBounds(this.options.idEntidad,this.options.type);
                if(this.options.boundsEntidad)
                {

                    mapaRPM=undefined;
                    mapaVAL=undefined;
                    wms=undefined;
                    zoomPrimeraVez = false;
                    if(this.options.target){
                        this.options.target.empty();
                    }
                    //DEFINO LOS PARAMETROS GENERALES
                    //OpenLayers.ImgPath='styles/images/';
                    OpenLayers.Util.onImageLoadErrorColor = 'transparent';
                    OpenLayers.IMAGE_RELOAD_ATTEMPTS = 2;
                    //OBTENGO EL BOUNDS DE LA ENTIDAD
                    //OBTENGO LA PROJECTION
                    this.options.projection=runService({
                        'wsName':'GET_PROJECTION'
                    });
                    //GENERO EL MAPA
                    var toolBarHeader=new Element('div',{
                        'class':'panel-header',
                        'style':'display:block;',
                        'id':'toolBarMapaEntidades'
                    }).injectInside($(this.options.target.getProperty('id')));
                    this.options.mapa = new OpenLayers.Map(this.options.target.getProperty('id'),{
                        'projection':new OpenLayers.Projection(this.options.projection),
                        'displayProjection':new OpenLayers.Projection(this.options.projection),
                        'resolutions':[1222.9924523925781, 611.4962261962891, 305.74811309814453, 152.87405654907226, 76.43702827453613, 38.218514137268066, 19.109257068634033, 9.554628534317017, 4.777314267158508, 2.388657133579254, 1.194328566789627, 0.5971642833948135, 0.29858214169740677, 0.14929107084870338, 0.07464553542435169, 0.037322767712175846, 0.018661383856087923],//[156543.03390625, 78271.516953125, 39135.7584765625, 19567.87923828125, 9783.939619140625, 4891.9698095703125, 2445.9849047851562, 1222.9924523925781, 611.4962261962891, 305.74811309814453, 152.87405654907226, 76.43702827453613, 38.218514137268066, 19.109257068634033, 9.554628534317017, 4.777314267158508, 2.388657133579254, 1.194328566789627, 0.5971642833948135, 0.29858214169740677, 0.14929107084870338, 0.07464553542435169, 0.037322767712175846, 0.018661383856087923, 0.009330691928043961, 0.004665345964021981],
                        'units':'m',
                        'allOverlays':true,
                        'controls':[],
                        'maxExtent':this.getBoundsFromWKT(this.options.extensionAmbito),
                        //'maxExtent':new OpenLayers.Bounds(-179,-90,179,90).transform(new OpenLayers.Projection("EPSG:4326"),new OpenLayers.Projection(this.options.projection)),
                        'restrictedExtent':this.getBoundsFromWKT(this.options.extensionAmbito),
                        'tileSize':new OpenLayers.Size(256,256)
                    });

                    this.addMapLayers();
                    new ZoomBar(this.options.mapa);
                    this.options.mapa.zoomToExtent(this.options.boundsEntidad,false);
                    this.options.mapa.zoomToExtent(this.options.boundsEntidad,false);
                    //ALMACENO EL MAPA
                    //if (this.options.target.getProperty('id')=="mapGeom"){
                    mapRPM=this.options.mapa;
                    //}else{
                    //    mapVal=this.options.mapa;
                    //}
                    new ToolBar(this.options.mapa,toolBarHeader,'panelTramitesEntidadesGeometria');
                    if (contextoMapaPreviewEntidades){
                        this.setContext(contextoMapaPreviewEntidades);
                    }
                }       
                else
                {
                    if(this.options.accordion)
                    {
                        this.options.accordion.hideTabById('geometria');
                    }
                }
            }
            else
            {
                if(this.options.accordion)
                {
                    this.options.accordion.hideTabById('geometria');
                }
            }
        }
        else
        {
            alert('NO SE HA PODIDO OBTENER EL ID DEL AMBITO DE LA ENTIDAD');
        }
    },
    addWMSEntidad:function(){
        //ELIMINO LA CAPA ANTERIOR
        this.removeWMSEntidad();
        if(this.options.boundsEntidad)
        {
            //OBTENGO LOS ATRIBUTOS DEL WMS DE LA ENTIDAD SELECCIONADA
            this.options.atributesWMSEntidad=JSON.decode(runService({
                'wsName': 'GET_WMS_SERVER_DE_ENT',
                'zona': this.options.type
            }));
            //COMPRUEBO SI LOS DATOS SON CORRECTOS
            if (!this.options.atributesWMSEntidad.url){
                alert(this.options.atributesWMSEntidad);
                return;
            }
            //GENERO EL WMS DE LA ENTIDAD
            this.options.WMSEntidad = new OpenLayers.Layer.WMS(
                "Entidad",//this.options.atributesWMSEntidad.nombre,
                this.options.atributesWMSEntidad.url,
                {
                    'layers': this.options.atributesWMSEntidad.layers,
                    'transparent':true,
                    'format':this.options.atributesWMSEntidad.format
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
            this.options.WMSEntidad.setOpacity(1);
            if (this.options.type == 'RPM'){
                this.options.WMSEntidad.CQL_FILTER='identidad='+this.options.idEntidad;
            } else if (this.options.type == 'VAL'){
                this.options.WMSEntidad.FEATUREID=this.options.idEntidad;
            }
            //ASIGNO LA MISMA ID QUE EL NOMBRE DEL WMS
            this.options.WMSEntidad.id="Entidad";
            //REASIGNO LA CAPA A LA VARIABLE GLOBAL WMS
            wms=this.options.WMSEntidad;
            //REDEFINO EL WMS PARA INCORPORAR LOS FILTROS EN LAS PETICIONES
            this.redefinirWMSEntidad(wms);
            //AVISO AL USUARIO DE QUE SE ESTA PROCESANDO LA CAPA WMS DE LAS ENTIDADES
            var textoTabGeometria='';
            if($('TramitesEntidadesGeometria')){
                textoTabGeometria=$('TramitesEntidadesGeometria').get('html');
                $('TramitesEntidadesGeometria').set('html',cargarTextoSegunIdioma(textoTabGeometria+'(Cargando datos...)'));
            }
            if(zoomPrimeraVez)
            {
                setTimeout(function(){
                    this.options.mapa.updateSize();
                    $('TramitesEntidadesGeometria').set('html',textoTabGeometria);
                    this.options.mapa.zoomToExtent(this.options.boundsEntidad,true);
                    this.options.mapa.zoomToExtent(this.options.boundsEntidad,true);
                }.bind({
                    options:this.options
                }),1000);
                zoomPrimeraVez=false;
            }
            else
            {
                this.options.mapa.zoomToExtent(this.options.boundsEntidad,true);
                this.options.mapa.zoomToExtent(this.options.boundsEntidad,true);
                this.options.mapa.updateSize();
                $('TramitesEntidadesGeometria').set('html',textoTabGeometria);
            }
            //ANADO LA CAPA AL MAPA
            this.options.mapa.addLayer(wms);

        }
    },
    removeWMSEntidad:function(){
        if(wms)
        {
            this.options.mapa.removeLayer(wms,false);
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

        //AÑADO LA CAPA DE LA ENTIDAD
        this.addWMSEntidad();
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
    getBounds:function(id,zona)
    {
        var dataWKT={};
        if (zona == 'RPM'){
            dataWKT=JSON.decode(runService({
                'wsName':'GET_WKT_FROM_ENTIDAD',
                'ID':id
            }));
        } else if (zona == 'VAL'){
            dataWKT=JSON.decode(runService({
                'wsName':'GET_WKT_FROM_ENTIDAD_VAL',
                'ID':id
            }));
        }

        if(dataWKT && dataWKT.extension) {
            var extension=dataWKT.extension;
            //alert(extension);
            extension=((((extension.replace('Env[','')).replace(']','')).replace(',',':'))).replace(' ','');

            var xmin=(extension.split(':')[0]);
            var ymin=(extension.split(':')[2]);

            var xmax=(extension.split(':')[1]);
            var ymax=(extension.split(':')[3]);

            return new OpenLayers.Bounds(xmin,ymin,xmax,ymax);
        }
        else{
            return null;
        }
    },
    /*
     * FUNCTION QUE OBTIENE LA EXTENSION DE UN AMBITO SEGUN EL ID DEL AMBITO
     **/
    getExtentFromAmbito:function(){
        return runService({
            'wsName':'GET_EXTENSION_DE_AMBITO',
            'idAmbito':this.options.idAmbitoDeEntidad
        });
    },
    /*
     * FUNCTION QUE OBTIENE EL ID DEL AMBITO DE UNA ENTIDAD. EN SU DEFECTO DEVUELVE UNDEFINED
     **/
    getAmbitoFromEntity:function(){
        if(this.options.idEntidad)
        {
            if(this.options.type=='RPM')
            {
                return runService({
                    'wsName':'GET_ID_AMBITO_DE_ENTIDAD',
                    'idEntidad':this.options.idEntidad
                });
            }
            else
            {
                return runService({
                    'wsName':'GET_ID_AMBITO_DE_TRAMITE_VAL',
                    'idTramite': tramiteVal
                });
            }
        }
        else
        {
            return undefined;
        }
    },
    redefinirWMSEntidad:function(wms){
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
