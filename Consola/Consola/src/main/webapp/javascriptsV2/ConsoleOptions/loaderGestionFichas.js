var fichasTree;
var arbolDatosFicha;

function initGestionFichas(){
    $('mainColumn').empty();
    //GENERO EL ARBOL
    fichasTree = new MooTreeControl({
        div: 'panelIzdoFicha',
        mode: 'folders',
        treeType: 'planes',
        iconBar: ['javascriptsV2/mooTree2/Planes.gif'],
        grid: true,
        toolbar:true,
        onSelect: function(node, state) {
            onSelectFichaNodeEvent(node,state);
        }
    },{
        text: cargarTextoSegunIdioma('Fichas urbanísticas'),
        open: true,
        progressive: true
    });
   
    new MUI.Panel({
        id: 'tabfichas-panel',
        contentURL: 'Pages/GestionFichas/FichasData.html',
        column: 'mainColumn',
        tabsURL: 'Pages/GestionFichas/FichasTabs.html',
        scrollbars : true,
        padding: {
            top: 0, 
            right: 0, 
            bottom: 0, 
            left: 0
        },
        collapsible: false,
        header: true,
        title:'',
        onContentLoaded: function(){

        }
    });
    
    $('panelIzdoFicha').setStyle('height','98%');//parseInt($('LeftSideColumn').getStyle('height'))  - 14 + 'px');
    $('panelIzdoFicha').setStyle('width','98%'); //parseInt($('LeftSideColumn').getStyle('width')) - 9 + 'px');

    fichasTree.root.load('GestionConsola?wsName=GET_NODOS_AMBITOS&op='+globalConfig.op_configfichas+'&tipo=FICHAS&idioma=es');
    /*
    $('main-panel_headerContent').empty();
    $('botonesFichas').injectInside($('main-panel_headerContent'));
    $('left-panel_headerContent').empty();
    $('botonesFichasArbolPlanes').injectInside($('left-panel_headerContent'));
    */
    
    $('LeftSideColumn').fade(1);
    $('mainColumn').fade(1);    
    
    //deshabilito todos los botones de primeras
    disableControl('addFicha');
    disableControl('editFicha');
    disableControl('delFicha');


    
    $('addFicha').addEvent ('click',newFicha);
    $('editFicha').addEvent ('click',editFicha);
    $('delFicha').addEvent ('click',delFicha);

    
}

function upElementFicha(){
    if ($('upElementFicha').disabled == false){
	
        var nodoRefresh=null;
        switch(arbolDatosFicha.selected.data.tipo){
            case 'determinacionregimenuso':
                nodoRefresh=arbolDatosFicha.root;
                break;
            case 'determinacionregimenacto':
                nodoRefresh=arbolDatosFicha.root;
                break;
            case 'valordetclasfiuso':
                nodoRefresh=arbolDatosFicha.selected.parent;
                break;
            case 'valordetclasfiacto':
                nodoRefresh=arbolDatosFicha.selected.parent;
                break;
            case 'grupo':
                nodoRefresh=arbolDatosFicha.selected.parent.parent;
                break;
            case 'conjuntogrupodeterminacion':
                nodoRefresh=arbolDatosFicha.selected.parent;
                break;
            case 'conjuntogrupo':
                nodoRefresh=arbolDatosFicha.root;
                break;
            case 'grupodeterminacion':
                nodoRefresh=arbolDatosFicha.selected.parent.parent;
                break;
        } 
        var result = runService({
            wsName: "SUBIR_ELEMENTO_FICHA",
            tipo:arbolDatosFicha.selected.data.tipo,
            idElemento : arbolDatosFicha.selected.data.idNode
        });
        if (result.toString().toLowerCase()=='true'){
            if (nodoRefresh){
                nodoRefresh.clear();
                nodoRefresh.load(nodoRefresh.data.load);
                nodoRefresh.select();
            }
        }else{
            new PopupMsg("Error moviendo elemento");
        }
		
    }	    
}

function downElementFicha(){
    if ($('downElementFicha').disabled == false){
	
        var nodoRefresh=null;
        switch(arbolDatosFicha.selected.data.tipo){
            case 'determinacionregimenuso':
                nodoRefresh=arbolDatosFicha.root;
                break;
            case 'determinacionregimenacto':
                nodoRefresh=arbolDatosFicha.root;
                break;
            case 'valordetclasfiuso':
                nodoRefresh=arbolDatosFicha.selected.parent;
                break;
            case 'valordetclasfiacto':
                nodoRefresh=arbolDatosFicha.selected.parent;
                break;
            case 'grupo':
                nodoRefresh=arbolDatosFicha.selected.parent.parent;
                break;
            case 'conjuntogrupodeterminacion':
                nodoRefresh=arbolDatosFicha.selected.parent;
                break;
            case 'conjuntogrupo':
                nodoRefresh=arbolDatosFicha.root;
                break;
            case 'grupodeterminacion':
                nodoRefresh=arbolDatosFicha.selected.parent.parent;
                break;
        }
        var result = runService({
            wsName: "BAJAR_ELEMENTO_FICHA",
            tipo: arbolDatosFicha.selected.data.tipo,
            idElemento : arbolDatosFicha.selected.data.idNode
        });
        if (result.toString().toLowerCase()=='true'){
            if (nodoRefresh){
                nodoRefresh.clear();
                nodoRefresh.load(nodoRefresh.data.load);
                nodoRefresh.select();
                nodoRefresh.toggle(true,true);
            }
        }else{
            new PopupMsg("Error moviendo elemento");
        }
		
    }	    
}

function delElementFicha(){
    if ($('delElementFicha').disabled == false){
	
        var elementName=null;
        var nodoRefresh=null;
        var wsName=null;
        switch(arbolDatosFicha.selected.data.tipo){
            case 'conjuntogrupo':
                elementName='la capa';
                nodoRefresh=arbolDatosFicha.root;
                wsName='DEL_CAPA_FICHA';
                break;
            case 'grupo':
                elementName='el grupo';
                wsName='DEL_GRUPO_FICHA';
                nodoRefresh=arbolDatosFicha.selected.parent.parent;
                break;
            case 'grupodeterminacion':
                elementName='el grupo de determinaciones';
                wsName='DEL_GRUPO_DETERMINACION_FICHA';
                nodoRefresh=arbolDatosFicha.selected.parent.parent;
                break;  
            case 'conjuntogrupodeterminacion':
                elementName='la determinación';
                wsName='DEL_DETERMINACION_GRUPO_FICHA';
                nodoRefresh=arbolDatosFicha.selected.parent;
                break;  
            case 'determinacionclasifuso':
                elementName='la determinación clasficatoria';
                wsName='DEL_DETERMINACION_CLASIF_USO';
                nodoRefresh=arbolDatosFicha.root;
                break;  
            case 'determinacionclasifacto':
                elementName='la determinación clasficatoria';
                wsName='DEL_DETERMINACION_CLASIF_ACTO';
                nodoRefresh=arbolDatosFicha.root;
                break;  
            case 'valordetclasfiuso':
                elementName='el valor de la determinación clasificatoria';
                wsName='DEL_VALOR_DETERMINACION_CLASIF_USO';
                nodoRefresh=arbolDatosFicha.selected.parent;
                break;  
            case 'valordetclasfiacto':
                elementName='el valor de la determinación clasificatoria';
                wsName='DEL_VALOR_DETERMINACION_CLASIF_ACTO';
                nodoRefresh=arbolDatosFicha.selected.parent;
                break;  
            case 'determinacionregimenacto':
                elementName='el régimen de actos';
                wsName='DEL_DETERMINACION_REGIMEN_ACTO';
                nodoRefresh=arbolDatosFicha.root;
                break;  
            case 'determinacionregimenuso':
                elementName='el régimen de usos';
                wsName='DEL_DETERMINACION_REGIMEN_USO';
                nodoRefresh=arbolDatosFicha.root;
                break;  
        }
        if (wsName && confirm("¿Desea borrar " + elementName + " '" + arbolDatosFicha.selected.text + "' ?")){
            var result = runService({
                wsName: wsName,
                idElemento : arbolDatosFicha.selected.data.idNode
            });
            if (result.toString().toLowerCase()=='true'){
                MUI.notification("Elemento eliminado correctamente");
                nodoRefresh.clear();
                nodoRefresh.load(nodoRefresh.data.load);
                nodoRefresh.select();
                nodoRefresh.toggle(true,true);
                arbolDatosFicha.selected=null;
                disableControl('addElementFicha');
                disableControl('editElementFicha');
                disableControl('delElementFicha');
                disableControl('upElementFicha');
                disableControl('downElementFicha');
            }else{
                new PopupMsg("Error eliminado elemento");
            }
        }
		
    }	    
}

function editElementFicha(){
    if ($('editElementFicha').disabled == false){
	
        var contentURL=null;
        var title=null;
        var nodoRefresh=null;
        var ancho=350;
        var alto=195;
        var resizable=false;
        var idBoton=null;
        var data=null;
        switch(arbolDatosFicha.selected.data.tipo){
            case 'conjuntogrupo':
                contentURL='Pages/GestionFichas/formCapa.jsp';
                title='Editar grupo de determinaciones';
                nodoRefresh=arbolDatosFicha.root;
                data={
                    idConjunto:arbolDatosFicha.selected.data.idNode,
                    idioma:'es'
                };
                idBoton='btnGuardarConjunto';
                break;
            case 'grupodeterminacion':
                contentURL='Pages/GestionFichas/formGrupoDeterminacion.jsp';
                title='Editar capa';
                data={
                    idGrupoDeterminacion:arbolDatosFicha.selected.data.idNode,
                    idioma:'es'
                };
                nodoRefresh=arbolDatosFicha.selected.parent.parent;
                idBoton='btnGuardarGrupoDeterminacion';
                break;
        }
        if (contentURL){
            var rndmId='ventanaElementFicha_' + Math.random().toString();
            new MUI.Modal({
                'icon':'styles/images/ficha.png',
                'id':rndmId,
                'title':title,
                'contentURL':contentURL,
                'data':data,
                'modalOverlayClose':false,
                'draggable':true,
                'method':'post',
                'width': ancho,
                'height': alto,
                'maximizable':false,
                'resizable':resizable,
                'container':'mainColumn',
                'onClose':function(){
                    nodoRefresh.clear();
                    nodoRefresh.load(nodoRefresh.data.load);
                    nodoRefresh.select();
                    nodoRefresh.toggle(true,true);
                    arbolDatosFicha.selected=null;
                    disableControl('addElementFicha');
                    disableControl('editElementFicha');
                    disableControl('delElementFicha');
                    disableControl('upElementFicha');
                    disableControl('downElementFicha');
                },
                'onContentLoaded':function(){
                    if (idBoton) $(idBoton).injectTop($(rndmId + '_controls'));
                }
            });
        }
		
    }	   
}

function newElementFicha(){
    if ($('addElementFicha').disabled == false){
	
        var contentURL=null;
        var title=null;
        var nodoRefresh=arbolDatosFicha.root;
        var data=null;
        var ancho=350;
        var alto=195;
        var resizable=false;
        var idBoton=null;
        switch(arbolDatosFicha.selected.data.tipo){
            case 'capas':
                contentURL='Pages/GestionFichas/formCapa.jsp';
                title='Nueva capa';
                data={
                    idFicha:fichasTree.selected.data.idNode,
                    idioma:'es'
                };
                idBoton='btnGuardarConjunto';
                break;
            case 'grupodeterminacionenunciado':
                contentURL='Pages/GestionFichas/formGrupoDeterminacion.jsp';
                title='Nuevo grupo de determinaciones';
                data={
                    idFicha:fichasTree.selected.data.idNode,
                    idConjunto:arbolDatosFicha.selected.parent.data.idNode,
                    idioma:'es'
                };
                idBoton='btnGuardarGrupoDeterminacion';
                break;
            case 'grupoentidad':
                contentURL='Pages/GestionFichas/formGrupo.jsp';
                title='Añadir grupo de entidades';
                nodoRefresh=arbolDatosFicha.selected.parent;
                alto=400;
                resizable=true;
                data={
                    idConjunto:arbolDatosFicha.selected.parent.data.idNode,
                    idFicha:fichasTree.selected.data.idNode,
                    idioma:'es'
                };
                idBoton='btnGuardarGrupoEntidadFicha';
                break;
            case 'grupodeterminacion':
                contentURL='Pages/GestionFichas/formDeterminacion_GrupoDet.jsp';
                title='Añadir determinación';
                nodoRefresh=arbolDatosFicha.selected;
                alto=400;
                resizable=true;
                data={
                    idGrupoDeterminacion:arbolDatosFicha.selected.data.idNode,
                    idFicha:fichasTree.selected.data.idNode,
                    idioma:'es'
                };
                idBoton='btnGuardarDeterminacion_GrupoDet';
                break;
            case 'detclasifuso':
            case 'detclasifacto':
                contentURL='Pages/GestionFichas/formDeterminacionClasificatoria.jsp';
                title='Añadir determinación clasificatoria de ' + 
                (arbolDatosFicha.selected.data.tipo=='detclasifuso'?'usos' : 'actos');
                nodoRefresh=arbolDatosFicha.selected.parent.parent;
                alto=400;
                resizable=true;
                data={
                    idFicha:fichasTree.selected.data.idNode,
                    idioma:'es',
                    tipo: arbolDatosFicha.selected.data.tipo=='detclasifuso'?'uso' : 'acto'
                };
                idBoton='btnGuardarDeterminacionClasificatoria';
                break;
            case 'determinacionclasifuso':
            case 'determinacionclasifacto':
                contentURL='Pages/GestionFichas/formValoresDetClasif.jsp';
                title='Añadir determinación de ' + 
                (arbolDatosFicha.selected.data.tipo=='determinacionclasifuso'?'usos' : 'actos');
                nodoRefresh=arbolDatosFicha.selected;
                alto=400;
                resizable=true;
                data={
                    idDetClasif:arbolDatosFicha.selected.data.idNode,
                    idioma:'es',
                    tipo: arbolDatosFicha.selected.data.tipo=='determinacionclasifuso'?'uso' : 'acto'
                };
                idBoton='btnGuardarValorDeterminacionClasificatoria';
                break;
            case 'detregimenuso':
            case 'detregimenacto':
                contentURL='Pages/GestionFichas/formDeterminacionRegimen.jsp';
                title='Añadir determinación de régimen de ' + 
                (arbolDatosFicha.selected.data.tipo=='detregimenuso'?'uso' : 'acto');
                nodoRefresh=arbolDatosFicha.selected.parent.parent;
                alto=400;
                resizable=true;
                data={
                    idFicha:fichasTree.selected.data.idNode,
                    idioma:'es',
                    tipo: arbolDatosFicha.selected.data.tipo=='detregimenuso'?'uso' : 'acto'
                };
                idBoton='btnGuardarDeterminacionRegimen';
                break;
        }
        if (contentURL){
            var rndmId='ventanaElementFicha_' + Math.random().toString();
            new MUI.Modal({
                'icon':'styles/images/ficha.png',
                'id':rndmId,
                'title':title,
                'contentURL':contentURL,
                'data':data,
                'method':'post',
                'width': ancho,
                'height': alto,
                'maximizable':false,
                'resizable':resizable,
                'modalOverlayClose':false,
                'draggable':true,
                'container':'mainColumn',
                'onClose':function(){
                    nodoRefresh.clear();
                    nodoRefresh.load(nodoRefresh.data.load);
                    nodoRefresh.select();
                    nodoRefresh.toggle(true,true);
                    arbolDatosFicha.selected=null;
                    disableControl('addElementFicha');
                    disableControl('editElementFicha');
                    disableControl('delElementFicha');
                    disableControl('upElementFicha');
                    disableControl('downElementFicha');
                },
                'onContentLoaded':function(){
                    if (idBoton) $(idBoton).injectTop($(rndmId + '_controls'));
                }
            });
        }
		
    }	    
}

function newFicha(){
    if ($('addFicha').disabled == false){
		
        var rndmId='ventanaFicha_' + Math.random().toString();
        new MUI.Modal({
            'icon':'styles/images/ficha.png',
            'id': rndmId,
            'title':'Nueva ficha',
            'contentURL':'Pages/GestionFichas/formFicha.jsp',
            'data':{
                idTramite:fichasTree.selected.data.idNode,
                idioma:'es'
            },
            'method':'post',
            'modalOverlayClose':false,
            'draggable':true,
            'width': 350,
            'height': 195,
            'maximizable':false,
            'resizable':true,
            'container':'mainColumn',
            'onClose':function(){
                fichasTree.selected.clear();
                fichasTree.selected.load(fichasTree.selected.data.load);
                fichasTree.selected.select();
                fichasTree.selected.toggle(true,true);
            },
            'onContentLoaded':function(){
                $('btnGuardarFicha').injectTop($(rndmId + '_controls'));
            }
        });
		
    }            
}

function editFicha(){
    if ($('editFicha').disabled == false){
	
        var rndmId='ventanaFicha_' + Math.random().toString();
        new MUI.Modal({
            'icon':'styles/images/ficha.png',
            'id':rndmId,
            'title':'Editar ficha',
            'contentURL':'Pages/GestionFichas/formFicha.jsp',
            'data':{
                idFicha:fichasTree.selected.data.idNode,
                idioma:'es'
            },
            'method':'post',
            'width': 350,
            'height': 195,
            'maximizable':false,
            'resizable':false,
            'modalOverlayClose':false,
            'draggable':true,
            'container':'mainColumn',
            'onClose':function(){
                fichasTree.selected.parent.clear();
                fichasTree.selected.parent.load(fichasTree.selected.parent.data.load);
                fichasTree.selected.select();
                fichasTree.selected.toggle(true,true);
            },
            'onContentLoaded':function(){
                $('btnGuardarFicha').injectTop($(rndmId + '_controls'));
            }
        });
		
    }	   
}

function delFicha(){
    if ($('delFicha').disabled == false){
	
        if (confirm("¿Desea borrar la ficha '" + fichasTree.selected.text + "' del trámite '" + fichasTree.selected.parent.text + "' ?")){
            var result = runService({
                wsName: 'DEL_FICHA',
                idFicha : fichasTree.selected.data.idNode
            });
            if (result.toString().toLowerCase()=='true'){
                MUI.notification("Ficha eliminada correctamente");
            }else{
                new PopupMsg("Error eliminado ficha");
            }
            fichasTree.selected.parent.clear();
            fichasTree.selected.parent.load(fichasTree.selected.parent.data.load);
            fichasTree.selected.parent.toggle(true,true);
        }
		
    }	    
}

function onSelectFichaNodeEvent(node,state){
    $('panelDchoFichas').empty();
    
    //GENERO LOS TABS
    
    /*new MUI.Panel({
                id: 'main-panel',
                title: '',
                contentURL: 'Pages/GestionFichas/FichasData.html',
                column: 'mainColumn',
                collapsible: false,
                header: true,
                headerToolbox: true,
        		headerToolboxURL: 'Pages/GestionFichas/FichasDataToolBox.html',
        		headerToolboxOnload: function(){
        			//La asignacion de eventos a botones se hace en loaderGestionFichas.js
        		},
                require: {
                    js: ['javascriptsV2/ConsoleOptions/loaderGestionFichas.js'],
                    onload: function(){						
                    	if (initGestionFichas) initGestionFichas();
                    }
                }
            });
            */
            
    disableControl('addFicha');
    disableControl('editFicha');
    disableControl('delFicha');
   
    $('delFicha').set('title','');
    $('addFicha').set('title','');
    $('editFicha').set('title','');

    if (!state){
        return;
    }
    
    if(node.data.tipo=='tramite'){
        enableControl('addFicha');
        $('addFicha').set('title','Crear nueva ficha del trámite seleccionado');
    }
    if(node.data.tipo=='ficha'){
        enableControl('delFicha');
        $('delFicha').set('title','Eliminar ficha seleccionada');
        enableControl('editFicha');
        $('editFicha').set('title','Editar ficha seleccionada');

        $('panelDchoFichas').setStyle('height',(parseInt($('tabfichas-panel').getStyle('height')) - 2)+'px');
        
        new MUI.Column({
            container: 'panelDchoFichas',
            id: 'mainColumnFichas',
            placement: 'main',
            sortable: false
        });	
        new MUI.Panel({
            id: 'tabConfFichas-panel',
            contentURL: 'Pages/GestionFichas/TreeViewerFichas.html',
            column: 'mainColumnFichas',
            scrollbars : true,
            collapsible: false,
            header: true,
            title:'',
            headerToolbox: true,
            headerToolboxURL: 'Pages/GestionFichas/FichasDataToolBox.html',
            onContentLoaded: function(){
                arbolDatosFicha = new MooTreeControl({
                    div: 'treeConfFichas',//mainColumn',
                    mode: 'folders',
                    grid: true,
                    treeType: 'ficha',
                    iconBar: ['javascriptsV2/mooTree2/Fichas.gif'],
                    onSelect: function(node, state) {
                        onSelectFichaElementNodeEvent(node,state);
                    //ajustarPaneles_ABIERTO();
                    }
                },{
                    text: cargarTextoSegunIdioma(node.text),
                    open: true,
                    progressive: true
                });
                arbolDatosFicha.root.data.load='GestionConsola?wsName=GET_GRUPOS_FICHA&op='+globalConfig.op_configfichas+'&idFicha=' + node.data.idNode + '&idioma=es';
                arbolDatosFicha.root.load(arbolDatosFicha.root.data.load);
            },
            headerToolboxOnload: function(){
                $('addElementFicha').addEvent ('click',newElementFicha);
                $('editElementFicha').addEvent ('click',editElementFicha);
                $('delElementFicha').addEvent ('click',delElementFicha);
                $('upElementFicha').addEvent ('click',upElementFicha);
                $('downElementFicha').addEvent ('click',downElementFicha);
                
                disableControl('addElementFicha');
                disableControl('editElementFicha');
                disableControl('delElementFicha');
                disableControl('upElementFicha');
                disableControl('downElementFicha');
            }
        });
    
        mapaPreviewFichas=new MapForFicha({
            idFicha:fichasTree.selected.data.idNode,
            idTramite:fichasTree.selected.parent.data.idNode,
            target:$('panelPreviewFichas')
        }).options.mapa;
        
    }

}

function onSelectFichaElementNodeEvent(node,state){
    disableControl('addElementFicha');
    disableControl('editElementFicha');
    disableControl('delElementFicha');
    disableControl('upElementFicha');
    disableControl('downElementFicha');
	   
    $('addElementFicha').set('title','');
    $('delElementFicha').set('title','');
    $('editElementFicha').set('title','');
    $('upElementFicha').set('title','');
    $('downElementFicha').set('title','');
    //alert(node.data.tipo);
    if (!state){
        return;
    }
    switch(node.data.tipo){
        case 'capas':
            $('addElementFicha').set('title','Crear nueva capa');
            break;
        case 'detclasifuso':
            $('addElementFicha').set('title','Crear nueva determinación clasificatoria de uso');
            break;
        case 'detclasifacto':
            $('addElementFicha').set('title','Crear nueva determinación clasificatoria de acto');
            break;
        case 'detregimenuso':
            $('addElementFicha').set('title','Crear nueva determinación de régimen de uso');
            break;
        case 'detregimenacto':
            $('addElementFicha').set('title','Crear nueva determinación de régimen de acto');
            break;
        case 'grupoentidad':
            $('addElementFicha').set('title','Asignar grupo de entidad');
            break;
        case 'grupodeterminacionenunciado':
            $('addElementFicha').set('title','Crear nuevo grupo de determinación');
            break;
        case 'determinacionregimenuso':
            $('delElementFicha').set('title','Eliminar determinación de régimen de usos');
            $('upElementFicha').set('title','Subir');
            $('downElementFicha').set('title','Bajar');
            break;
        case 'determinacionregimenacto':
            $('delElementFicha').set('title','Eliminar determinación de régimen de actos');
            $('upElementFicha').set('title','Subir');
            $('downElementFicha').set('title','Bajar');
            break;
        case 'valordetclasfiuso':
            $('delElementFicha').set('title','Eliminar valor de determinación clasificatoria de usos');
            $('upElementFicha').set('title','Subir');
            $('downElementFicha').set('title','Bajar');
            break;
        case 'valordetclasfiacto':
            $('delElementFicha').set('title','Eliminar valor de determinación clasificatoria de actos');
            $('upElementFicha').set('title','Subir');
            $('downElementFicha').set('title','Bajar');
            break;
        case 'grupo':
            $('delElementFicha').set('title','Eliminar grupo');
            $('upElementFicha').set('title','Subir');
            $('downElementFicha').set('title','Bajar');
            break;
        case 'conjuntogrupodeterminacion':
            $('delElementFicha').set('title','Eliminar determinación');
            $('upElementFicha').set('title','Subir');
            $('downElementFicha').set('title','Bajar');
            break;
        case 'conjuntogrupo':
            $('delElementFicha').set('title','Eliminar capa');
            $('editElementFicha').set('title','Editar capa');
            $('upElementFicha').set('title','Subir');
            $('downElementFicha').set('title','Bajar');
            break;
        case 'determinacionclasifuso':
            $('delElementFicha').set('title','Eliminar determinación clasificatoria de usos');
            $('addElementFicha').set('title','Crear nueva determinación clasificatoria de usos');
            break;
        case 'determinacionclasifacto':
            $('delElementFicha').set('title','Eliminar determinación clasificatoria de actos');
            $('addElementFicha').set('title','Crear nueva determinación clasificatoria de actos');
            break;
        case 'grupodeterminacion':
            $('delElementFicha').set('title','Eliminar grupo de determinación');
            $('addElementFicha').set('title','Añadir determinación');
            $('upElementFicha').set('title','Subir');
            $('editElementFicha').set('title','Editar grupo de determinación');
            $('downElementFicha').set('title','Bajar');
            break;
    }
    switch(node.data.tipo){
        case 'capas':
        case 'detclasifuso':
        case 'detclasifacto':
        case 'detregimenuso':
        case 'detregimenacto':
        case 'grupoentidad':
        case 'grupodeterminacionenunciado':
            enableControl('addElementFicha');
            //$('addElementFicha').set('class','addElementFicha');
            break;
        case 'determinacionregimenuso':
        case 'determinacionregimenacto':
        case 'valordetclasfiuso':
        case 'valordetclasfiacto':
        case 'grupo':
        case 'conjuntogrupodeterminacion':
            enableControl('delElementFicha');
            enableControl('upElementFicha');
            enableControl('downElementFicha');
            //$('delElementFicha').set('class','delElementFicha');
            //$('upElementFicha').set('class','upElementFicha');
            //$('downElementFicha').set('class','downElementFicha');
            break;
        case 'conjuntogrupo':
            enableControl('delElementFicha');
            enableControl('editElementFicha');
            enableControl('upElementFicha');
            enableControl('downElementFicha');
            //$('delElementFicha').set('class','delElementFicha');
            //$('editElementFicha').set('class','editElementFicha');
            //$('upElementFicha').set('class','upElementFicha');
            //$('downElementFicha').set('class','downElementFicha');
            break;
        case 'determinacionclasifuso':
        case 'determinacionclasifacto':
            enableControl('delElementFicha');
            enableControl('addElementFicha');
            //$('delElementFicha').set('class','delElementFicha');
            //$('addElementFicha').set('class','addElementFicha');
            break;
        case 'grupodeterminacion':
            enableControl('delElementFicha');
            enableControl('addElementFicha');
            enableControl('upElementFicha');
            enableControl('editElementFicha');
            enableControl('downElementFicha');
            //$('delElementFicha').set('class','delElementFicha');
            //$('addElementFicha').set('class','addElementFicha');
            //$('upElementFicha').set('class','upElementFicha');
            //$('editElementFicha').set('class','editElementFicha');
            //$('downElementFicha').set('class','downElementFicha');
            break;
    }
}

