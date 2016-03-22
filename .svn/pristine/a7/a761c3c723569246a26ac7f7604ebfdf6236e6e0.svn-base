/*
    Document   : JobWindow
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/

//MooTools, <http://mootools.net>, My Object Oriented (JavaScript) Tools. Copyright (c) 2006-2009 Valerio Proietti, <http://mad4milk.net>, MIT Style License.
//JobWindow, Mi ventana flotante. Copyright (c) 2006-2010 Jorge Bodas, MIT Style License.

var JobWindow = new Class({
    Implements: Events,
    //ESTRUCTURA
        contenedor:null,
        cabecera:null,
            titulo:null,
        botonera:null,
            btnClose:null,
            btnMaximize:null,
            btnMinimize:null,
        zonaActiva:null,
            verBotoneraZonaActiva:false,
            botoneraZonaActiva:null,
        pie:null,
            status:null,
            tirador:null,
    //OPCIONES
        mostrarBtnClose:true,
        mostrarBtnMaximize:false,
        MostrarBtnMinimize:false,
        resizable:false,
        textTitulo:'-',
        id:'-',
        statusText:'-',
        animacion:false,
    //ESTILOS
        width:300,
        height:300,
        left:100,
        top:200,
        //bgColor:'#dddddd',
        padding:8,

    initialize:function(titulo,resizable,ancho,alto,left,top,animacion,verBotoneraZonaActiva){
        if(titulo){this.textTitulo=titulo}
        if(resizable){this.resizable=resizable}
        if(ancho){this.width=ancho}
        if(alto){this.height=alto}
        if(left){this.left=left}
        if(top){this.top=top}
        if(animacion){this.animacion=animacion}
        if(verBotoneraZonaActiva){this.verBotoneraZonaActiva=verBotoneraZonaActiva}
        if(!$('jwContenedor_'+this.textTitulo)) //SI NO EXISTE LA VENTANA
        {
            this.generarEstructuraPrincipal();
            this.addDragProperty();
            //this.reAjustarDocumentBody();
        }
    },
    generarEstructuraPrincipal:function(){
        //GENERO EL CONTENEDOR
                var leftAleatorio=Math.floor(Math.random()*1011)*(-1);
                var topAleatorio=Math.floor(Math.random()*1011);
                bug.log(leftAleatorio,'JOBWINDOW');
                bug.log(topAleatorio,'JOBWINDOW');
                this.contenedor=new Element('div',{
                    'id':'jwContenedor_'+this.textTitulo,
                    'class':'jwContenedor',
                    'styles':{
                        'position':'absolute',
                        'left':this.left,
                        'top':this.top,
                        'width':this.width+(this.padding*2),
                        'height':this.height+(this.padding*2),
                        //'background-image':'url(styles/images/JobWindow_zonaActiva.png)',//this.bgColor,
                        'z-index':60000,
                        'border':'0px solid #cccccc',
                        'box-shadow':'1px 1px 35px -3px #484747',
                        'background-color':'#CBCBCB'
                    }
                });
                if(this.animacion)
                {
                    this.contenedor.setStyles({
                        'left':-500,
                        'top':-500
                    });
                    this.contenedor.injectInside($(document.body));
                    this.contenedor.set('morph',{
                       duration:400,
                       transition:Fx.Transitions.Sine.easeOut
                    });
                    this.contenedor.morph({
                        'left':this.left,
                        'top':this.top
                    });
                }
                else
                {
                    this.contenedor.injectInside($(document.body));
                }
                jsIO.curveBordersToObject('jwContenedor_'+this.textTitulo);
        //GENERO LA CABECERA
            this.generarCabecera();
        //GENERO EL PIE
            //this.generarPie();
        //GENERO LA ZONA ACTIVA
            this.generarZonaActiva();

        //AJUSTAR ESTILOS
            var elems=$('jwContenedor_'+this.textTitulo).getElements('div');
            if(elems && elems[0]){
                elems[0].setStyles({
                    'background-color':'#CBCBCB',
                    'opacity':0.9,
                    'background-image':''
                });
                var subElems=elems[0].getElements('div');
                    var pieElems=subElems[1].getElements('div');
                        if(pieElems[0]){
                            pieElems[0].setStyles({
                                'background-color':'#CBCBCB',
                                'opacity':0.9,
                                'background-image':''
                            });
                        }
                        if(pieElems[1]){
                            pieElems[1].setStyles({
                                'background-color':'#CBCBCB',
                                'opacity':0.9,
                                'background-image':''
                            });
                        }
                        if(pieElems[2]){
                            pieElems[2].setStyles({
                                'background-color':'#CBCBCB',
                                'opacity':0.9,
                                'background-image':''
                            });
                        }
            }
    },
    generarCabecera:function(){
        //GENERO EL ELEMENTO DE LA PROPIA CABCERA
            this.cabecera=new Element('div',{
               'id':'jwCabecera_'+this.textTitulo,
               'class':'jwCabecera',
               'styles':{
                   'z-index':60003
               }
            }).injectInside(this.contenedor);
            jsIO.curveBordersToObject('jwCabecera_'+this.textTitulo,{
                                  tl: {radius: 7},
                                  tr: {radius: 7},
                                  bl: {radius: 7},
                                  br: {radius: 7},
                                  antiAlias: true
                                });
        //GENERO LOS COMPONENTES DE LA CABECERA
            //TITULO
                this.titulo=new Element('div',{
                    'id':'jwTitulo_'+this.textTitulo,
                    'class':'jwTitulo',
                    'html':jsIO.cargarTextoSegunIdioma(this.textTitulo)
                }).injectInside(this.cabecera);
            //BOTONERA
                this.botonera=new Element('div',{
                    'id':'jwBotonera_'+this.textTitulo,
                    'class':'jwBotonera',
                   'styles':{
                       'z-index':600010
                   }
                }).injectInside(this.contenedor);
                if(this.mostrarBtnClose)
                {
                    this.btnClose=new Element('div',{
                        'id':'jwBotonera_BtnClose_'+this.textTitulo,
                        'class':'jwBotonera_BtnClose',
                        'events':{
                            'click':function(evento){
                                bug.log("boton cierre seleccionado","JOBWINDOW");
                                if(this.zonaActiva.getElement('div[id=auxCapas]')){visor.ocultarArbolCapas();}
                                this.contenedor.destroy()
                                if(visor)
                                {
                                    visor.cabecera.apagarBoton(this.textTitulo);
                                }
                                this.fireEvent("close");
                            }.bind(this),
                            'mouseover':function(){}.bind(this),
                            'mouseout':function(){}.bind(this)
                        }
                    }).injectInside(this.botonera);
                }
                if(this.mostrarBtnMaximize)
                {
                    this.btnMaximize=new Element('div',{
                        'id':'jwBotonera_BtnMaximize_'+this.textTitulo,
                        'class':'jwBotonera_BtnMaximize',
                        'events':{
                            'click':function(){}.bind(this),
                            'mouseover':function(){}.bind(this),
                            'mouseout':function(){}.bind(this)
                        }
                    }).injectInside(this.botonera);
                }
                if(this.mostrarBtnMinimize)
                {
                    this.btnMinimize=new Element('div',{
                        'id':'jwBotonera_BtnMinimize_'+this.textTitulo,
                        'class':'jwBotonera_BtnMinimize',
                        'events':{
                            'click':function(){}.bind(this),
                            'mouseover':function(){}.bind(this),
                            'mouseout':function(){}.bind(this)
                        }
                    }).injectInside(this.botonera);
                }

    },
    generarPie:function(){
        //GENERO EL ELEMENTO DE LA PROPIA CABCERA
            this.pie=new Element('div',{
               'id':'jwPie_'+this.textTitulo,
               'class':'jwPie',
               'styles':{
                   'z-index':60003
               }
            }).injectInside(this.contenedor);

            $('jwPie_'+this.textTitulo).setStyles({
                    'border-radius':'7px'
                })
        //GENERO LOS COMPONENTES DE LA CABECERA
            //TITULO
//                this.status=new Element('div',{
//                    'id':'jwPie_status_'+this.textTitulo,
//                    'class':'jwPie_status',
//                    'html':this.status
//                }).injectInside(this.pie);
            //BOTONERA
                if(this.resizable)
                {
                    this.tirador=new Element('div',{
                        'id':'jwPie_tirador_'+this.textTitulo,
                        'class':'jwPie_tirador'
                    }).injectInside(this.pie);
                    this.addResizeProperty();
                }

    },
    generarZonaActiva:function(){
        var altoBotoneraZonaActiva=0;
        if(this.verBotoneraZonaActiva)
        {
            this.botoneraZonaActiva=new Element('div',{
               'id':'jwZonaActiva_botonera',
               'class':'botoneraZonaActiva'
            }).injectInside(this.contenedor);
            altoBotoneraZonaActiva=this.botoneraZonaActiva.getHeight();
        }
        this.zonaActiva=new Element('div',{
            'id':'jwZonaActiva_'+this.textTitulo,
            'class':'jwZonaActiva',
            'styles':{
                'position':'relative',
                'float':'left',
                'left':16,
                'top':0,//this.cabecera.getHeight()+8,
                'width':this.contenedor.getWidth()-this.padding*4,
                'height':this.contenedor.getHeight()-this.padding*2-25-altoBotoneraZonaActiva,
                'background-color':'transparent',
                'overflow':'auto',
                'z-index':60002
            }
        }).injectInside(this.contenedor);

    },
    addDragProperty:function(){
        if(this.contenedor){
            var drag=this.contenedor.makeDraggable({
                handle:this.cabecera,
                //handle:this.contenedor,
                //container:$(document.body),
                //stopPropagation:true,
                //prevenDefault :true,
                limit:{x:100,y:100},
                onBeforeStart :function(element, droppable, event){
                    bug.log("La ventana "+this.textTitulo+' va a empezar a moverse(todavia no se ha movido)','JOBWINDOW');
                    //APLICO UNA TRANSPARENCIA A LA VENTANA
                        this.contenedor.setOpacity(0.9);
                    //GENERO EL ELEMENTO DE AYUDA AL DESPLAZAMIENTO
                        this.cargarElementoAuxiliar();
                     //SI LA ZONA ACTIVA CONTIENE UN IFRAME LO OCULTO HASTA QUE SE  TERMINE DE MOVER LA VENTANA
                        if($('jwIframe_'+this.textTitulo))
                        {
                            $('jwIframe_'+this.textTitulo).setOpacity(0);
                        }
                     //CAMBIO EL FONDO DE LA CABECERA
                }.bind(this),
                onStart:function(el,event){
                    bug.log("La ventana "+this.textTitulo+' comienza a moverse','JOBWINDOW');
                }.bind(this),
                onDrag:function(el,event){

                    //bug.log("La ventana "+this.textTitulo+' se estÃ¡ moviendo en la posicion vertical '+event.event.screenY,'JOBWINDOW');
                    bug.log(event.event.screenY);
                    if ((el.getStyle('top')).toInt()<=0)
                    {
                        el.setStyle('top',0);
                    }   //drag.stop();

                }.bind(this),
                onCancel:function(el,droppable,event){
                    bug.log("La ventana "+this.textTitulo+' ha cancelado el movimiento','JOBWINDOW');
                    //APLICO UNA TRANSPARENCIA A LA VENTANA
                        this.contenedor.setOpacity(1);
                    //ELLIMINO EL ELEMENTO DE AYUDA AL DESPLAZAMIENTO
                        this.destroyElementoAuxiliar();
                    //SI LA ZONA ACTIVA CONTIENE UN IFRAME LO OCULTO HASTA QUE SE  TERMINE DE MOVER LA VENTANA
                        if($('jwIframe_'+this.textTitulo))
                        {
                            $('jwIframe_'+this.textTitulo).setOpacity(1);
                        }
                    //event.stopPropagation();
                }.bind(this),
                onComplete:function(el,droppable,event){

                    bug.log("La ventana "+this.textTitulo+' ha dejado de moverse','JOBWINDOW');
                     
                    //APLICO UNA TRANSPARENCIA A LA VENTANA
                        this.contenedor.setOpacity(1);
                    //ELLIMINO EL ELEMENTO DE AYUDA AL DESPLAZAMIENTO
                        this.destroyElementoAuxiliar();
                    //SI LA ZONA ACTIVA CONTIENE UN IFRAME LO OCULTO HASTA QUE SE  TERMINE DE MOVER LA VENTANA
                        if($('jwIframe_'+this.textTitulo))
                        {
                            $('jwIframe_'+this.textTitulo).setOpacity(1);
                        }
                     if ((el.getStyle('top')).toInt()<=0)
                    {
                        el.setStyle('top',0);
                    }
                     //drag.attach();
                    //event.stopPropagation();
                }.bind(this)
            });
        }
    },
    addResizeProperty:function(){
        if(this.contenedor){
            this.contenedor.makeResizable({
                handle:this.tirador,
                //limit :{x:100,y:200},
                onBeforeStart :function(element, droppable, event){
                    bug.log("La ventana "+this.textTitulo+' va a empezar a reescalarse(todavia no se ha movido)','JOBWINDOW');
                    this.cargarElementoAuxiliar();
                }.bind(this),
                onStart:function(el){
                    bug.log("La ventana "+this.textTitulo+' comienza a escalarse','JOBWINDOW');
                }.bind(this),
                onDrag:function(el){
                    bug.log("La ventana "+this.textTitulo+' se estÃ¡ escalando','JOBWINDOW');

                }.bind(this),
                onComplete:function(el){
                    bug.log("La ventana "+this.textTitulo+' ha dejado de escalarse','JOBWINDOW');
                    this.destroyElementoAuxiliar();
                    this.actualizarEstadoVentana(el);
                }.bind(this)
            });
        }
    },
    actualizarEstadoVentana:function(el){
                    var ancho=el.getWidth().toInt();
                    var alto=el.getHeight().toInt();
                    var left=el.getCoordinates().left;
                    var top=el.getCoordinates().top;
                    $('aux').adopt(this.zonaActiva);
                    this.contenedor.destroy();
                    this.initialize(this.textTitulo,true,ancho,alto,left,top);
                    this.zonaActiva.adopt($('aux').getElements('*'));
                    //jsIO.curveBordersToObject('jwContenedor_'+this.textTitulo);
    },
    reAjustarDocumentBody:function(){
//        $(document.body).setStyles({
//            'height':'101%',
//            'width':'101%'
//        });
        $(document.body).setStyles({
            'height':'100%',
            'width':'100%'
        });
    },
    loadIframe:function(url){
        new Element('iframe',{
            'id':'jwIframe_'+this.textTitulo,
            'src':url,
            'marginheight':0,
            'marginwidth':0,
            'frameborder':0,
            'scrolling':'auto',
            'height':'100%',//this.zonaActiva.getHeight(),
            'width':'100%',//this.zonaActiva.getWidth(),
            'allowTransparency':'true',
            'styles':{
                'top':0,
                'left':0,
                'background-color':'transparent'
            }
        }).injectInside(this.zonaActiva);
    },
    loadHTML:function(html){
        this.zonaActiva.set('html',html);
    },
    loadAJAX:function(url,accion){
        new Request({
            url: url,
            //data:Hash.toQueryString(parametros),
            async:false,
            evalScripts:true,
            onSuccess: accion,
            onFailure: function(response){
                bug.log('ERROR.'+response);
                bug.log(this.zonaAjax);
                if(this.zonaAjax)
                {
                    this.zonaAjax.set('html',jsIO.cargarTextoSegunIdioma("ERROR AL CARGAR LA INFORMACIÃ“N"));
                }
                error();
            }.bind(this)
        }).send();
    },
    cargarElementoAuxiliar:function(){
        if(!$('jwAuxMove'))
        {
            var jwAuxMove=new Element('div',{
                'id':'jwAuxMove',
                'styles':{
                    'left':0,//this.cabecera.getPosition().x,
                    'top':0,//this.cabecera.getPosition().y,
                    'height':'100%',
                    'width':'100%',
                    'background-color':'#dddddd',
                    'z-index':60005
                }
            }).injectInside(this.contenedor);
            jwAuxMove.setOpacity(0.1);
            jsIO.curveBordersToObject('jwAuxMove');
        }
    },
    destroyElementoAuxiliar:function(){
        if($('jwAuxMove'))
        {
            $('jwAuxMove').destroy();
        }
    },
    loadZonaActiva:function(response){
                if(this.zonaActiva)
                {
                    this.zonaActiva.set('html',response);
                }
                //traduce al idioma deseado los elementos con la clase traducible
                if (this.zonaActiva){
                    this.zonaActiva.getElements('.traducible').each(function(item){
                        //item.set('html',jsIO.cargarTextoSegunIdioma(item.get('html')));
                    });
                }
    }
});