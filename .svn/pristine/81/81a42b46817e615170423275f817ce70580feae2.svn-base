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
    initialize:function(map){
        this.map=map;
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
        }).injectInside($(this.map.div),'top');
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
            new Element('div',{
                'id':'bgOpcion_'+nomOpcion,
                'class':'bgOpcion',
                'alt':jsIO.cargarTextoSegunIdioma(nomOpcion),
                'html':jsIO.cargarTextoSegunIdioma(nomOpcion),
                'events':{
                    'click':function(){
                        this.bg.addEventClickSegunOpcion(this.op)
                        }.bind({
                        bg:this,
                        op:nomOpcion
                    }),
                    'mouseover':function(){
                        this.bg.addEventMouseOverSegunOpcion(this.op)
                        }.bind({
                        bg:this,
                        op:nomOpcion
                    }),
                    'mouseout':function(){
                        this.bg.addEventMouseOutSegunOpcion(this.op)
                        }.bind({
                        bg:this,
                        op:nomOpcion
                    })
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
                    this.activarBoton(nomOpcion);
                    break;
                case 'G_SATELLITE_MAP':
                    this.activarBoton(nomOpcion);
                    break;
                case 'G_PHYSICAL_MAP':
                    this.activarBoton(nomOpcion);
                    break;
                case 'G_NORMAL_MAP':
                    this.activarBoton(nomOpcion);
                    break;
                default:
                    break;
            }

        }
    },
    addEventMouseOverSegunOpcion:function(nomOpcion){
        if(this.opciones.contains(nomOpcion))
        {
            switch (nomOpcion) {
                case 'G_HYBRID_MAP':
                    break;
                case 'G_SATELLITE_MAP':
                    break;
                case 'G_PHYSICAL_MAP':
                    break;
                case 'G_NORMAL_MAP':
                    break;
                default:
                    break;
            }

        }
    },
    addEventMouseOutSegunOpcion:function(nomOpcion){
        if(this.opciones.contains(nomOpcion))
        {
            switch (nomOpcion) {
                case 'G_HYBRID_MAP':
                    break;
                case 'G_SATELLITE_MAP':
                    break;
                case 'G_PHYSICAL_MAP':
                    break;
                case 'G_NORMAL_MAP':
                    break;
                default:
                    break;
            }

        }
    },
    activarBoton:function(nomOpcion){
        //DESACTIVO LOS BOTONES
        this.apagarBotones();
        //ACTIVO EL CORRESPONDIENTE
        if ($('bgOpcion_'+nomOpcion)){
            $('bgOpcion_'+nomOpcion).setProperty('class','bgOpcion_click');
            if(this.map){
                this.map.baseLayer.mapObject.setMapTypeId(jsIO.getTipoServicioGoogle(nomOpcion));
                this.map.baseLayer.type=jsIO.getTipoServicioGoogle(nomOpcion);
            }
        }

    },
    apagarBotones:function(){
        for(var i=0;i<this.opciones.length;i++)
        {
            var nomOpcion=this.opciones[i];
            if($('bgOpcion_'+nomOpcion)){
                $('bgOpcion_'+nomOpcion).setProperty('class','bgOpcion')
                }
        }
    }
});