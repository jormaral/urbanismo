/*
    Document   : Pie
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/

var JobStyler=new Class({
    Implements: [Options],
    borderZone:null,
        borderColor:null,
        borderOpacity:null,
        borderWidth:null,
    fillZone:null,
    textZone:null,
    options:{
        target:null,
        type:'base'
    },
    initialize:function(options){
        this.setOptions(options);
        this.generarEstructuraPrincipal();
    },
    generarEstructuraPrincipal:function(){
        switch (this.options.type) {
            case 'base':
                //COLOR DEL BORDE
                    this.borderZone=new Element('div',{
                            'id':'jobStyler_borderZone',
                            'class':'jobStyler_borderZone'
                    }).injectInside(this.options.target);
                    //GENERO
                        this.borderColor=new Element('div',{
                                'id':'jobStyler_borderColor',
                                'class':'jobStyler_borderColor'
                        }).injectInside(this.borderZone);
                        this.borderOpacity=new Element('div',{
                                'id':'jobStyler_borderOpacity',
                                'class':'jobStyler_borderWidth'
                        }).injectInside(this.borderZone);
                        this.borderWidth=new Element('div',{
                                'id':'jobStyler_borderWidth',
                                'class':'jobStyler_borderWidth'
                        }).injectInside(this.borderZone);


                //COLOR DE RELLENO
                    this.fillZone=new Element('div',{
                            'id':'jobStyler_fillZone',
                            'class':'jobStyler_fillZone'
                    }).injectInside(this.options.target);
                //TEXTO DEL FEATURE
                    this.textZone=new Element('div',{
                            'id':'jobStyler_textZone',
                            'class':'jobStyler_textZone'
                    }).injectInside(this.options.target);
                break;
            default:
                break;
        }
          
    }
});