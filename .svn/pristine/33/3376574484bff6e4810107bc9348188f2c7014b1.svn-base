/*
    Document   : ZoomBar
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
 */


var ZoomBar=new Class({
    map:null,

    contenedor:null,
    zonaMover:null,
    zonaSlider:null,
    baseLayerGoogle:false,
    zoomIn:null,
    zoomOut:null,
    left:null,
    right:null,
    up:null,
    down:null,
    center:null,
    slider:null,
    knob:null,
    sliderControl:null,

    panFactor:7000,
    defaultZoom:13,
    maxZoom:18,
    initialize:function(map,defaultZoom,maxZoom,baseLayerGoogle){
        this.baseLayerGoogle=baseLayerGoogle;
        this.map=map;
        if(maxZoom)
        {
            this.maxZoom=maxZoom;
        }
        if(defaultZoom!=undefined && defaultZoom!=null && defaultZoom<=this.maxZoom && defaultZoom>=0)
        {
            this.defaultZoom=defaultZoom;
        }
        
        //DEFINO LOS ELEMENTOS CORRESPONDIENTES
        //GENERO EL CONTENEDOR
        this.generarEstructura();
    },
    generarEstructura:function(){
        this.contenedor=new Element('div',{
            'id':'zoomBar_contenedor'
        }).injectInside($(document.body));
        //DEFINDO LA ZONA DE MOVIMIENTO
        this.zonaMover=new Element('div',{
            'id':'zoomBar_zonaMover'
        }).injectInside(this.contenedor);
        //GENERO LOS BOTONES DE MOVIMIENTO
        this.generarElementosMovimiento();
        //DEFINO LA ZONA DEL SLIDER
        this.zonaSlider=new Element('div',{
            'id':'zoomBar_zonaSlider'
        }).injectInside(this.contenedor);
        //GENERO LOS ELEMENTOS DEL SLIDER
        this.generarElementosSlider();
        this.generarSlider();
    },
    generarElementosMovimiento:function()
    {
        //GENERO EL BOTON DE SUBIR
        this.up=new Element('div',{
            'id':'zoomBar_zonaMover_up',
            'events':{
                'click':function(){
                    bug.log("Botón up seleccionado","ZOOMBAR");
                    this.map.panTo(new OpenLayers.LonLat(this.map.getCenter().lon,this.map.getCenter().lat+this.panFactor));
                }.bind(this)
            }
        }).injectInside(this.zonaMover);
        //GENERO EL BOTON DE BAJAR
        this.down=new Element('div',{
            'id':'zoomBar_zonaMover_down',
            'events':{
                'click':function(){
                    bug.log("Botón down seleccionado","ZOOMBAR");
                    this.map.panTo(new OpenLayers.LonLat(this.map.getCenter().lon,this.map.getCenter().lat-this.panFactor));
                }.bind(this)
            }
        }).injectInside(this.zonaMover);
        //GENERO EL BOTON DE IZQUIERDA
        this.left=new Element('div',{
            'id':'zoomBar_zonaMover_left',
            'events':{
                'click':function(){
                    bug.log("Botón left seleccionado","ZOOMBAR");
                    this.map.panTo(new OpenLayers.LonLat(this.map.getCenter().lon-this.panFactor,this.map.getCenter().lat));
                }.bind(this)
            }
        }).injectInside(this.zonaMover);
        //GENERO EL BOTON DE DERECHA
        this.right=new Element('div',{
            'id':'zoomBar_zonaMover_right',
            'events':{
                'click':function(){
                    bug.log("Botón right seleccionado","ZOOMBAR");
                    this.map.panTo(new OpenLayers.LonLat(this.map.getCenter().lon+this.panFactor,this.map.getCenter().lat));
                }.bind(this)
            }
        }).injectInside(this.zonaMover);
        //GENERO EL BOTON DE ZOOM_EXTENSION
        this.center=new Element('div',{
            'id':'zoomBar_zonaMover_center',
            'events':{
                'click':function(){
                    bug.log("Botón center seleccionado","ZOOMBAR");
                    visor.map.zoomToMaxExtent();
                }.bind(this)
            }
        }).injectInside(this.zonaMover);
    },
    generarElementosSlider:function()
    {
        //DEFINO LA ZONA DEL BOTON ZOOMIN
        this.zoomIn=new Element('div',{
            'id':'zoomBar_zonaSlider_zoomIn',
            'events':{
                'click':function(){
                    bug.log("Zoom In seleccionado",'ZOOMBAR');
                    this.sliderControl.set(this.sliderControl.step-1);
                //this.map.zoomIn();
                }.bind(this)
            }
        }).injectInside(this.zonaSlider);
        //DEFINO LA ZONA DEL PROPIO SLIDER
        this.slider=new Element('div',{
            'id':'zoomBar_zonaSlider_slider',
            'class':'slider'
        }).injectInside(this.zonaSlider);
        //DEFINO LA ZONA DEL PROPIO SLIDER
        this.knob=new Element('div',{
            'id':'zoomBar_zonaSlider_slider_knob',
            'class':'knob'
        }).injectInside(this.slider);
        //DEFINO LA ZONA DEL BOTON ZOOMOUT
        this.zoomOut=new Element('div',{
            'id':'zoomBar_zonaSlider_zoomOut',
            'events':{
                'click':function(){
                    bug.log("Zoom Out seleccionado",'ZOOMBAR');
                    this.sliderControl.set(this.sliderControl.step+1);
                //this.map.zoomOut();
                }.bind(this)
            }
        }).injectInside(this.zonaSlider);
    },
    generarSlider:function(){
        //var el=$('zoomBar_zonaSlider_slider');
        var s=null;
        if (this.baseLayerGoogle){
            s=new Slider(this.slider,this.knob, {
                steps: this.maxZoom,
                range: [-18,-8], 
                mode: 'vertical',
                snap:true,
                wheel:true,
                invert:true,
                onChange: function(value){
                    this.map.zoomTo(value*(-1));
                }.bind(this) 
            }).set(this.defaultZoom*(-1));
        }else{
            s=new Slider(this.slider,this.knob, {
                //steps: this.maxZoom,	// There are 35 steps
                //range: [this.maxZoom*(-1),0],
                steps: this.maxZoom-1,	// There are 35 steps
                //range: [this.maxZoom*(-1),(this.maxZoom*(-1))+9],	
                //range: [-18,-8],	// Minimum value is 8
                mode: 'vertical',
                snap:true,
                wheel:true,
                onChange: function(value){
                    this.map.zoomTo(this.maxZoom-1-value);
                }.bind(this)
            }).set(this.maxZoom-1-this.defaultZoom);
        }
        this.sliderControl=s;
    }


})