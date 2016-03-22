var globalVisorPreview=null;
var contextoMapaPreviewFicha=null;
function initPreviewVisor(){
    //GENERO EL ARBOL
    MUI.Windows.indexLevel=8000;
    var tramitesPreviewVisorTree = new MooTreeControl({
        div: 'panelIzdoPreviewVisor',
        mode: 'folders',
        treeType: 'planes',
        iconBar: ['javascriptsV2/mooTree2/Planes.gif'],
        grid: true,
        toolbar:false,
        onSelect: function(node, state) {
            onSelectTramiteNodeEvent(node,state);
        }
    },{
        text: cargarTextoSegunIdioma('Trámites'),
        open: true,
        progressive: true
    });

    
    $('panelIzdoPreviewVisor').setStyle('height','98%');//parseInt($('LeftSideColumn').getStyle('height'))  - 14 + 'px');
    $('panelIzdoPreviewVisor').setStyle('width','98%'); //parseInt($('LeftSideColumn').getStyle('width')) - 9 + 'px');

    tramitesPreviewVisorTree.root.load('GestionConsola?wsName=GET_NODOS_AMBITOS&op='+globalConfig.op_consola+'&tipo=TODOS&idioma=es');

    $('LeftSideColumn').fade(1);
    $('mainColumn').fade(1);    

    //Agrego los eventos
    $('previewVisorBtnZoomIn').addEvent('click', function(){
        if (globalVisorPreview){
            if (globalVisorPreview.controlZoom){
                $(globalVisorPreview.map.viewPortDiv).setStyle('cursor', 'url(styles/images/cursores/zoomin.cur),move');
                globalVisorPreview.controlPan.deactivate();
                globalVisorPreview.clickUrbr.deactivate();
                globalVisorPreview.controlZoom.out = false;
                globalVisorPreview.controlZoom.activate();
            }
        }
    });
    $('previewVisorBtnZoomOut').addEvent('click', function(){
        if (globalVisorPreview){
            if (globalVisorPreview.controlZoom){
                $(globalVisorPreview.map.viewPortDiv).setStyle('cursor', 'url(styles/images/cursores/zoomout.cur),move');
                globalVisorPreview.controlPan.deactivate();
                globalVisorPreview.clickUrbr.deactivate();
                globalVisorPreview.controlZoom.out = true;
                globalVisorPreview.controlZoom.activate();
            }
        }
    });
    $('previewVisorBtnPan').addEvent('click', function(){
        if (globalVisorPreview){
            if (globalVisorPreview.controlPan){
                $(globalVisorPreview.map.viewPortDiv).setStyle('cursor', 'url(styles/images/cursores/hand.cur),move');
                globalVisorPreview.controlZoom.deactivate();
                globalVisorPreview.clickUrbr.deactivate();
                globalVisorPreview.controlPan.activate();
            }
        }
    });
    $('previewVisorBtnCapas').addEvent('click', function(){
        if (globalVisorPreview){
            if ($('auxCapas')){
                var ventana=new MUI.Window({
                    'icon':'styles/images/layers.png',
                    'id':'ventanaCapasPreviewVisor',
                    'title':'Capas',
                    'content':$('auxCapas'),
                    'width': 400,
                    'height': 250,
                    'storeOnClose':true,
                    'maximizable':false,
                    'resizable':true,
                    'container':$('panelPreviewVisor'),
                    'resizeLimit': {
                        'x': [400, 2500], 
                        'y': [250, 2000]
                    }
                });
                MUI.blurAll();
                MUI.focusWindow(ventana);
                $('auxCapas').setStyle('display', 'block');
            }
            
        }
    });
    $('previewVisorBtnVistaPosterior').addEvent('click', function(){
        if (globalVisorPreview){
            if (globalVisorPreview.navHis && globalVisorPreview.navHis.next){
            }
            globalVisorPreview.navHis.next.trigger()
        }
    });
    $('previewVisorBtnVistaAnterior').addEvent('click', function(){
        if (globalVisorPreview){
            if (globalVisorPreview.navHis && globalVisorPreview.navHis.previous){
            }
            globalVisorPreview.navHis.previous.trigger()
        }
    });
    $('previewVisorBtnInfo').addEvent('click', function(){
        if (globalVisorPreview){
            $(globalVisorPreview.map.viewPortDiv).setStyle('cursor', 'url(styles/images/cursores/question.cur),move');
            globalVisorPreview.controlZoom.deactivate();
            globalVisorPreview.controlPan.deactivate();
            globalVisorPreview.clickUrbr.activate();
        }
    });
    
}

function onSelectTramiteNodeEvent(node,state){
    if (globalVisorPreview){
        var wmc = new OpenLayers.Format.WMC();
        contextoMapaPreviewFicha=wmc.toContext(globalVisorPreview.map);
        delete globalVisorPreview;
    }
    globalVisorPreview=null;
    $('panelPreviewVisor').empty();
    if (!state){
        return;
    }
    
    if(node.data.tipo=='tramite'){
        var perfil=runService({
            wsName:'GET_PERFILES_VISOR',
            'idTramite':node.data.idNode
        });
        globalVisorPreview=new previewVisor('panelPreviewVisor');
        globalVisorPreview.setPerfil(jsIO.leerXMLFromString(perfil),node.data.idNode);  
    }

}