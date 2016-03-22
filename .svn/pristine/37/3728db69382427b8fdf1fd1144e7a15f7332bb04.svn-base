/*
    Document   : SplashScreen
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/


//GENERA UN OBJECTO QUE SE INCLUIRA EN EL LIGHTBOX CORRESPONDIENTE(en nuestro caso el SqueezeBox)

var SplashScreen=new Class({
   debug:false,
   ancho:700,
   alto:400,
   splash:null,
   contenedor:null,
   cabecera:null,
   zonaAjax:null,
   opciones:['Bienvenido','Entidad','Ayuda','acerca_de'],
   xmlhttp:null,
   initialize:function(debug){
       if(debug!=undefined && debug!=null && debug=='_debug')
       {
           this.debug=true;
       }
       if(!this.debug)
       {
           this.generarEstructuraPrincipal();
           var ventanaSplash=SqueezeBox.initialize({
                    size: {
                        x: this.ancho,
                        y: this.alto
                    },
                    sizeLoading: {
                        x: this.ancho,
                        y: this.alto
                    }
           });
           bug.log('SPLASH GENERADO: '+this.contenedor,'SPLASHSCREEN');
           ventanaSplash.open(this.contenedor,{handler:'adopt'});
          
       }
   },
   generarEstructuraPrincipal:function(){
       //GENERO EL COTENEDOR
            this.contenedor=new Element('div',{
               'id':'splashCotenedor',
               'styles':{
                   'height':this.alto,
                   'width':this.ancho
               }
            });
       //GENERO LA ZONA AJAX DEL SPLASH
            this.zonaAjax=new Element('div',{
                'id':'splashZonaAjax'
            }).injectInside(this.contenedor);

            
       //GENERO LA CABECERA DEL SPLASH
            this.cabecera=new Element('div',{
                'id':'splashCabecera'
            }).injectInside(this.contenedor);
            //GENERO LOS BOTONES DE LAS OPCIONES
                for(var i=0;i<this.opciones.length;i++)
                {
                    //OBTENGO EL NOMBRE DE LA OPCION
                        var nomOpcion=this.opciones[i];
                    //GENERO EL ELEMENTO CORRESPONDIENTE
                        bug.log(nomOpcion,'SPLASHSCREEN');
                        new Element('div',{
                                'id':'splashOpcion_'+nomOpcion,
                                'class':'splashOpcion',
                                'rel':nomOpcion,
                                'html':jsIO.cargarTextoSegunIdioma(nomOpcion),
                                'events':{
                                    'click':function(){this.splash.addEventClickSegunOpcion(this.op)}.bind({splash:this,op:nomOpcion})
                                }
                        }).injectInside(this.cabecera);
                }
            this.loadSection('acercaDe.html');
            //this.leerXMLbyOpcion('bienvenido');
   },
   addEventClickSegunOpcion:function(nomOpcion){
        if(this.opciones.contains(nomOpcion))
        {
            switch (nomOpcion) {
                case 'Bienvenido':
                    bug.log('seleccionado bienvenido',"SPLASHSCREEN");
                        //this.leerXMLbyOpcion('bienvenido');
                        this.loadSection('pages/info/bienvenida.jsp?ayuntamiento='+visor.ambito);
                    break;
                case 'Entidad':
                    bug.log('seleccionado Ayuntamiento',"SPLASHSCREEN");
                        //this.leerXMLbyOpcion('ayuntamiento');
                        this.loadSection('pages/info/ayuntamiento.jsp?ayuntamiento='+visor.ambito);
                    break;
                case 'Ayuda':
                    bug.log('seleccionado Ayuda',"SPLASHSCREEN");
                        //this.leerXMLbyOpcion('ayuda');
                        this.loadSectionIFRAME('pages/info/URBR-ManualUsuarioVisor.pdf');
                    break;
                case 'acerca_de':
                    bug.log('seleccionado acerca_de',"SPLASHSCREEN");
                        //this.leerXMLbyOpcion('acerca');
                        this.loadSection('pages/info/acercaDe.jsp?ayuntamiento='+visor.ambito);
                    break;
                default:
                    bug.log("salida por defecto del click de una opcion","SPLASHSCREEN");
                    break;
            }

        }
   },
   loadSection:function(seccion){
       new Request({
            url: seccion,
            //data:Hash.toQueryString(parametros),
            method:'get',
            async:false,
            onSuccess: function(response){
                if(this.zonaAjax)
                {
                   this.zonaAjax.set('html',response);
                }
            }.bind(this),
            onFailure: function(response){
                bug.log('ERROR.'+response);
                bug.log(this.zonaAjax);
                if(this.zonaAjax)
                {
                    this.zonaAjax.set('html',jsIO.cargarTextoSegunIdioma("ERROR AL CARGAR LA INFORMACION"));
                }
            }.bind(this)
        }).send();
   },
   loadSectionIFRAME:function(seccion){
       this.zonaAjax.set('html',"<iframe src='"+seccion+"' frameborder='0' height='100%' width='100%'></iframe> ");
   },
   leerXMLbyOpcion:function(opcion){
       this.xmlhttp=jsIO.leerXML('XML/acercaDe.xml');
       //se recoge los valores de la seccion que se esta cargando
       var seccion =  this.xmlhttp.getElementsByTagName(opcion);
      
       if ((seccion != null) && (seccion.length!=0)){
              var titulo = this.xmlhttp.getElementsByTagName(opcion+'_titulo');
              var cuerpo = this.xmlhttp.getElementsByTagName(opcion+'_txt');
              if (titulo)
                  this.generarResultado('titulo',opcion+'_titulo',titulo);
              if (cuerpo)
                  this.generarResultado('cuerpo',opcion+'_txt',cuerpo);
              if ((!titulo) && (!cuerpo)) alert(jsIO.cargarTextoSegunIdioma("NO EXISTE CONTENIDO EN ESTA SECCION"));
       }
       
       else alert(jsIO.cargarTextoSegunIdioma("NO EXISTE CONTENIDO EN ESTA SECCION"));
   },
   generarResultado:function(tipo,id,contenido){
       var texto=contenido[0].childNodes[0].nodeValue;
        if (tipo=='titulo'){
            var cab=this.zonaAjax.getElement('h1[id=cab]');
             //cab.injectInside(this.zonaAjax);
             cab.set('html',texto);
             //cab.set('id',id);
       }
        else {
            var txt=this.zonaAjax.getElement('div[id=txt]');
             //txt.injectInside(this.zonaAjax);
             txt.set('html',texto);
             //txt.set('id',id);
        }

    }

})