/*
    Document   : BotoneraGoogle
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/


var BotoneraGoogle=new Class({
    map:null,
    contenedor:null,
    zonaIzquierda:null,
    zonaCentral:null,
    zonaDerecha:null,
    opciones:['G_HYBRID_MAP','G_SATELLITE_MAP','G_PHYSICAL_MAP','G_NORMAL_MAP'],
    initialize:function(){
        this.generarEstructuraPrincipal();
        this.agregarContenido();
        this.activarBoton(this.opciones[0]);
        //this.addHoverEventToContenedor();
    },
    generarEstructuraPrincipal:function(){
        //GENERO LA ESTRUCTURA CONTENEDORA
                this.contenedor=new Element('div',{
                        'id':'bgContenedor',
                        'class':'bgContenedor'
                }).injectInside($(document.body));
            //CREO LA ZONA IZQUIERDA
                    this.zonaIzquierda=new Element('div',{
                            'id':'bgContenedor_zonaIzquierda'
                    }).injectInside(this.contenedor);
            //CREO LA ZONA CENTRAL
                    this.zonaCentral=new Element('div',{
                            'id':'bgContenedor_zonaCentral'
                    }).injectInside(this.contenedor);
            //CREO LA ZONA DERECHA
                    this.zonaDerecha=new Element('div',{
                            'id':'bgContenedor_zonaDerecha'
                    }).injectInside(this.contenedor);
    },
    agregarContenido:function(){
        for(var i=0;i<this.opciones.length;i++)
        {
            //OBTENGO EL NOMBRE DE LA OPCION
                var nomOpcion=this.opciones[i];
            //GENERO EL ELEMENTO CORRESPONDIENTE
                bug.log(nomOpcion,'BOTONERA_GOOGLE');
                new Element('div',{
                        'id':'bgOpcion_'+nomOpcion,
                        'class':'bgOpcion',
                        'alt':jsIO.cargarTextoSegunIdioma(nomOpcion),
                        'html':jsIO.cargarTextoSegunIdioma(nomOpcion),
                        'events':{
                            'click':function(){this.bg.addEventClickSegunOpcion(this.op)}.bind({bg:this,op:nomOpcion}),
                            'mouseover':function(){this.bg.addEventMouseOverSegunOpcion(this.op)}.bind({bg:this,op:nomOpcion}),
                            'mouseout':function(){this.bg.addEventMouseOutSegunOpcion(this.op)}.bind({bg:this,op:nomOpcion})
                        }
                }).injectInside(this.zonaCentral);


        }
    },
    addHoverEventToContenedor:function(){
        if(this.contenedor)
        {
            this.contenedor.addEvents({
               'mouseover':function(e){
                   this.zonaIzquierda.setStyle('background-image','url(styles/images/BotoneraOpaca.png)');
                   this.zonaCentral.setStyle('background-image','url(styles/images/BotoneraOpaca_center.png)');
                   this.zonaDerecha.setStyle('background-image','url(styles/images/BotoneraOpaca.png)');
               }.bind(this),
               'mouseout':function(e){
                   this.zonaIzquierda.setStyle('background-image','url(styles/images/BotoneraTransparente.png)');
                   this.zonaCentral.setStyle('background-image','url(styles/images/BotoneraTransparente_center.png)');
                   this.zonaDerecha.setStyle('background-image','url(styles/images/BotoneraTransparente.png)');
               }.bind(this)
            });
        }
    },
    addEventClickSegunOpcion:function(nomOpcion){
        if(this.opciones.contains(nomOpcion))
        {
            switch (nomOpcion) {
                case 'G_HYBRID_MAP':
                        bug.log('seleccionado Híbrido',"BOTONERA_GOOGLE");
                        this.activarBoton(nomOpcion);
                    break;
                case 'G_SATELLITE_MAP':
                        bug.log('seleccionado Satelite',"BOTONERA_GOOGLE");
                        this.activarBoton(nomOpcion);
                    break;
                case 'G_PHYSICAL_MAP':
                        bug.log('seleccionado Terreno',"BOTONERA_GOOGLE");
                        this.activarBoton(nomOpcion);
                    break;
                case 'G_NORMAL_MAP':
                        bug.log('seleccionado Callejero',"BOTONERA_GOOGLE");
                        this.activarBoton(nomOpcion);
                    break;
                default:
                        bug.log("salida por defecto del click de una opcion","BOTONERA_GOOGLE");
                    break;
            }

        }
    },
    addEventMouseOverSegunOpcion:function(nomOpcion){
        if(this.opciones.contains(nomOpcion))
        {
            switch (nomOpcion) {
                case 'G_HYBRID_MAP':
                        bug.log('mouseover Híbrido',"BOTONERA_GOOGLE");
                    break;
                case 'G_SATELLITE_MAP':
                        bug.log('mouseover Satelite',"BOTONERA_GOOGLE");
                    break;
                case 'G_PHYSICAL_MAP':
                        bug.log('mouseover Terreno',"BOTONERA_GOOGLE");
                    break;
                case 'G_NORMAL_MAP':
                        bug.log('mouseover Callejero',"BOTONERA_GOOGLE");
                    break;
                default:
                        bug.log("salida por defecto del click de una opcion","BOTONERA_GOOGLE");
                    break;
            }

        }
    },
    addEventMouseOutSegunOpcion:function(nomOpcion){
        if(this.opciones.contains(nomOpcion))
        {
            switch (nomOpcion) {
                case 'G_HYBRID_MAP':
                        bug.log('mouseout Híbrido',"BOTONERA_GOOGLE");
                    break;
                case 'G_SATELLITE_MAP':
                        bug.log('mouseout Satelite',"BOTONERA_GOOGLE");
                    break;
                case 'G_PHYSICAL_MAP':
                        bug.log('mouseout Terreno',"BOTONERA_GOOGLE");
                    break;
                case 'G_NORMAL_MAP':
                        bug.log('mouseout Callejero',"BOTONERA_GOOGLE");
                    break;
                default:
                        bug.log("salida por defecto del click de una opcion","BOTONERA_GOOGLE");
                    break;
            }

        }
    },
    activarBoton:function(nomOpcion){
        bug.log(nomOpcion,'BOTONERA_GOOGLE');
        //DESACTIVO LOS BOTONES
            this.apagarBotones();
        //ACTIVO EL CORRESPONDIENTE
            $('bgOpcion_'+nomOpcion).setProperty('class','bgOpcion_click');
            bug.log('Activado el boton '+ nomOpcion,'BOTONERA_PRINCIPAL');
            if(this.map){
                bug.log('CAMBIO EL TIPO DEL MAPA BASE','BOTONERAGOOGLE');
                bug.log(jsIO.getTipoServicioGoogle(nomOpcion),'BOTONERAGOOGLE');
                this.map.baseLayer.mapObject.setMapTypeId(jsIO.getTipoServicioGoogle(nomOpcion));
                this.map.baseLayer.type=jsIO.getTipoServicioGoogle(nomOpcion);
            }

    },
    apagarBotones:function(){
        for(var i=0;i<this.opciones.length;i++)
        {
            var nomOpcion=this.opciones[i];
                if($('bgOpcion_'+nomOpcion)){$('bgOpcion_'+nomOpcion).setProperty('class','bgOpcion')}
                bug.log('Apagado el boton '+ nomOpcion,'BOTONERA_PRINCIPAL');
        }
    },
    setMap:function(map){
        this.map=map;
    }
});