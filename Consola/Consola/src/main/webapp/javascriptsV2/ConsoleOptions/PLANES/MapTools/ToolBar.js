var ToolBar = new Class({
    initialize: function (map,headerElement,idContainer) {
        var div = null;
        var span = null;
        MUI.Windows.indexLevel=8000;
        div = new Element('div', {
            'class': 'panel-header-toolbox'
        }).injectInside(headerElement);
        div = new Element('div', {
            'class': 'toolbox divider'
        }).injectInside(div);
        
        this.div=div;
        this.controlZoom = new OpenLayers.Control.ZoomBox({
            alwaysZoom: true
        });
        this.controlPan = new OpenLayers.Control.DragPan({
            enableKinetic: true
        });
        
        span = new Element('span', {
            'class': 'iconWrapper'
        }).injectInside(div);                
        new Element('div',
        {
            'id': 'toolZoomIn',
            'title': 'ZoomIn',
            'class': 'toolBoxAction',
            'type': OpenLayers.Control.TYPE_TOOL,
            'events': {
                'click': function () {
                    this.controlPan.deactivate();
                    this.controlZoom.out = false;
                    this.controlZoom.activate();
                    $(map.viewPortDiv).setStyle('cursor','url(styles/images/cursores/zoomin.cur),move');
                } .bind(this)
            }
        }).injectInside(span);
        
        span = new Element('span', {
            'class': 'iconWrapper'
        }).injectInside(div);
        new Element('div',
        {
            'id': 'toolZoomOut',
            'title': 'ZoomOut',
            'class': 'toolBoxAction',
            'type': OpenLayers.Control.TYPE_TOOL,
            'events': {
                'click': function () {
                    this.controlPan.deactivate();
                    this.controlZoom.out = true;
                    this.controlZoom.activate();
                    $(map.viewPortDiv).setStyle('cursor','url(styles/images/cursores/zoomout.cur),move');
                } .bind(this)
            }
        }).injectInside(span);
        
        span = new Element('span', {
            'class': 'iconWrapper'
        }).injectInside(div);
        new Element('div',
        {
            'id': 'toolPan',
            'title': 'Pan',
            'class': 'toolBoxAction',
            'type': OpenLayers.Control.TYPE_TOOL,
            'events': {
                'click': function () {
                    this.controlZoom.deactivate();
                    this.controlPan.activate();
                    $(map.viewPortDiv).setStyle('cursor','url(styles/images/cursores/hand.cur),move');
                } .bind(this)
            }
        }).injectInside(span);
        
        span = new Element('span', {
            'class': 'iconWrapper'
        }).injectInside(div);
        new Element('div',
        {
            'id': 'toolLayers',
            'title': 'Capas',
            'class': 'toolBoxAction',
            'type': OpenLayers.Control.TYPE_TOOL,
            'events': {
                'click': function () {
                    var divArbolCapas=new Element('div');
                    
                    new MUI.Window({
                        'icon':'styles/images/layers.png',
                        'id':'ventanaCapasVisorEntidades',
                        'title':'Capas',
                        'content':divArbolCapas,
                        'width': 350,
                        'height': 150,
                        'maximizable':false,
                        'resizable':true,
                        'container':idContainer,
                        'resizeLimit': {'x': [300, 2500], 'y': [150, 2000]}
                    });
                    
                    Array.each(map.layers, function(capa, index){
                        var divCapa=new Element('div',{
                            'style':'position:relative'
                        }).injectTop(divArbolCapas);
                        new Element('br').injectAfter(divCapa);
                        new Element('input',{
                            'type':'checkbox',
                            'checked':capa.getVisibility(),
                            'style':'float:left;',
                            events: {
                                click: function(e) {
                                    capa.setVisibility(this.checked);
                                }
                            }
                        }).injectInside(divCapa);
                        new Element('div',{
                            'html': ' ' + capa.name + ' ',
                            'style':'float:left;font-size:11px;line-height: 13px;padding-left: 2px;padding-right: 2px;'
                        }).injectInside(divCapa);
                        var divSlider=new Element('div',{'class':'sliderAreaOpacity'}).injectInside(divCapa);
                        new Element('div',{'class':'sliderKnobOpacity'}).injectInside(divSlider);
                        new Slider($(divSlider),divSlider.getElement('.sliderKnobOpacity'),{
                           range:[0,100],
                           initialStep:capa.opacity*100,
                           onChange:function (value){
                               if (value) capa.setOpacity(value/100);
                           }
                        });
                    });
                } .bind(this)
            }
        }).injectInside(span);
        
        map.addControls([this.controlZoom,this.controlPan]);
    },
    addButton:function (divButton){
        var span=new Element('span');
        span.set('class', 'iconWrapper');
        divButton.set('class', 'toolBoxAction');
        divButton.injectInside(span);
        span.injectInside(this.div);
    }
});