
function ChargeTabDeterminacionesDatosGenerales(data){		
	
	var result = false;
	
	//PANELES HORIZONTALES FORM - TEXTO
    $('panelTramitesDeterminacionesDatosGenerales').setStyle('height', (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px');    
    
    addSplitPanel('panelTramitesDeterminacionesDatosGenerales', 'HORIZONTAL', undefined, undefined, -1, true, 'Definición', undefined, 200, true,
		function() {	    
    
    		result = addForm(data, undefined, undefined, viewerPlanesTypeDeterminacion, 'fspElement' + 'panelTramitesDeterminacionesDatosGenerales', '100px', undefined);
    	
    		data = data.data[0];
    		
    		var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesDeterminacionesDatosGenerales'+'textArea','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		
    		if (data['texto'] != undefined && $('sspElement' + 'panelTramitesDeterminacionesDatosGenerales')) {
    			if (data['texto'].length == 0 || data['texto'] == textoAsociadoVisorPlanes){
    				data['texto'] = textoAsociadoVisorPlanes;    				
	        	}
    			
    			//inserto un textArea    			    			    			
				toInsert.inject($('sspElement' + 'panelTramitesDeterminacionesDatosGenerales'));
	        	$('sspElement'+'panelTramitesDeterminacionesDatosGenerales'+'textArea').set('html', data['texto']);	        		        	
		    }    		    		    		
		});          
    
    return result;
}

function ChargeTabDeterminacionesValoresReferencia(data){

	//UN SOLO PANEL GRID	
	var supPanelHeight = (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px';
    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);        
	
    var result = addTable(data, undefined, undefined, viewerPlanesTypeValoresReferencia, 'panelTramitesDeterminacionesValoresReferencia', true, "100%", tableHeight, 'tabTramitesDeterminacionesValoresReferencia', undefined);    
    
    return result;
}

function ChargeTabDeterminacinoesRegulacionesEspecificas(data){
	
	var result = false;
	
	//PANELES HORIZONTALES ARBOL - TEXTO
    $('panelTramitesDeterminacionesRegulacionesEspecificas').setStyle('height', (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px');
    addSplitPanel('panelTramitesDeterminacionesRegulacionesEspecificas', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function() {
		    result = addTree(data, undefined, undefined, undefined, 'fspElement' + 'panelTramitesDeterminacionesRegulacionesEspecificas', 'folders', 'Régimen', false, undefined, undefined, '100%', (parseInt($('panelTramitesDeterminacionesRegulacionesEspecificas').getStyle('height')) - 250) + 'px', 'tabTramitesDeterminacionesRegulacionesEspecificas',
    			function(node, state) {
    			    if (state && $('sspElement' + 'panelTramitesDeterminacionesRegulacionesEspecificas'+'textArea')) {
    			    	
			        	if (!node.data.comment || node.data.comment.length == 0 || node.data.comment == textoAsociadoVisorPlanes){
			        		node.data.comment = textoAsociadoVisorPlanes;
			        	}
			        	$('sspElement'+'panelTramitesDeterminacionesRegulacionesEspecificas'+'textArea').set('html', node.data.comment);
    			    }    			        			    
    			    
    			});		    
		    
        	var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesDeterminacionesRegulacionesEspecificas'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesDeterminacionesRegulacionesEspecificas'));    				    
		    
		    MUI.rWidth();
		});
    
    return result;
}

function chargeTabDeterminacionesDocumentos(data){
	
	var result = false;
	
	//PANELES HORIZONTALES GRID - TEXTO - DBL CLICK SOBRE GRID ABRE DOCUMENTO
    $('panelTramitesDeterminacionesDocumentos').setStyle('height', (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px');
    addSplitPanel('panelTramitesDeterminacionesDocumentos', 'HORIZONTAL', undefined, undefined, -1,true, undefined, undefined, 242, true,
		function() {
    	
	    	var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesDeterminacionesDocumentos').getStyle('height');
		    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
    	
		    result = addTable(data, undefined, undefined, viewerPlanesTypeDocumentosDeterminacion, 'fspElement' + 'panelTramitesDeterminacionesDocumentos', true, "100%", tableHeight, 'tabTramitesDeterminacionesDocumentos',
    			function(info) { //CLICK
    			    if (info && $('sspElement' + 'panelTramitesDeterminacionesDocumentos')) {
    			        var dataRow = info.target.getDataByRow(info.row);
    			        
			        	if (dataRow) {
    			        	
    			        	if (!dataRow.comentario || dataRow.comentario.length == 0 || dataRow.comentario == textoAsociadoVisorPlanes){
    			        		dataRow.comentario = textoAsociadoVisorPlanes;
    			        	}
    			        	$('sspElement' + 'panelTramitesDeterminacionesDocumentos'+'textArea').set('html', dataRow.comentario);
    			        	
    			        }
			        	
    			    }
    			},
    			function(info) { //DBL CLICK
    				openGridDocument(info);
    			});
		    
		    var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesDeterminacionesDocumentos'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesDeterminacionesDocumentos'));
		    
		    MUI.rWidth();
		});
    
    return result;
}

function chargeTabDeterminacionesEsOperadorPor(data){
	
	var result = false;
	
	//PANELES HORIZONTALES GRID - TEXTO
    $('panelTramitesDeterminacionesEsOperadoPor').setStyle('height', (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px');
    addSplitPanel('panelTramitesDeterminacionesEsOperadoPor', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true, 
		function() {
    		
	    	var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesDeterminacionesEsOperadoPor').getStyle('height');
		    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
    	
		    result = addTable(data, undefined, undefined, viewerPlanesTypeDeterminacionEsOperadoPor, 'fspElement' + 'panelTramitesDeterminacionesEsOperadoPor', true, "100%", tableHeight, 'tabTramitesDeterminacionesEsOperadoPor',
    			function(info) {
    			    if (info && $('sspElement' + 'panelTramitesDeterminacionesEsOperadoPor')) {
    			        var dataRow = info.target.getDataByRow(info.row);
    			        
			        	if (dataRow) {
    			        	
    			        	if (!dataRow.regimen || dataRow.regimen.length == 0 || dataRow.regimen == textoAsociadoVisorPlanes){
    			        		dataRow.regimen = textoAsociadoVisorPlanes;
    			        	}
    			        	$('sspElement' + 'panelTramitesDeterminacionesEsOperadoPor'+'textArea').set('html', dataRow.regimen);
    			        	
    			        }

    			    }
    			});
		    
		    var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesDeterminacionesEsOperadoPor'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesDeterminacionesEsOperadoPor'));
		    
		    MUI.rWidth();
		});
	
	return result;
}

function chargeTabDeterminacionesOperaA(data){
	
	var result = false;
	
	//PANELES HORIZONTALES GRID - TEXTO
    $('panelTramitesDeterminacionesOperaA').setStyle('height', (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px');
    addSplitPanel('panelTramitesDeterminacionesOperaA', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function() {
    	
	    	var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesDeterminacionesOperaA').getStyle('height');
		    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
		    
		    result = addTable(data, undefined, undefined, viewerPlanesTypeDeterminacionOperaA, 'fspElement' + 'panelTramitesDeterminacionesOperaA', true, "100%", tableHeight, 'tabTramitesDeterminacionesOperaA',
    			function(info) {
    			    if (info && $('sspElement' + 'panelTramitesDeterminacionesOperaA')) {
    			        var dataRow = info.target.getDataByRow(info.row);
    			        
    			        if (dataRow) {
    			        	
    			        	if (!dataRow.regimen || dataRow.regimen.length == 0 || dataRow.regimen == textoAsociadoVisorPlanes){
    			        		dataRow.regimen = textoAsociadoVisorPlanes;
    			        	}
    			        	$('sspElement' + 'panelTramitesDeterminacionesOperaA'+'textArea').set('html', dataRow.regimen);
    			        	
    			        }
    			        
    			    }
    			});
		    
		    var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesDeterminacionesOperaA'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesDeterminacionesOperaA'));
		    
		    MUI.rWidth();
		});
	
	return result;
}

function chargeTabDeterminacionesAplicaciones(data){
	
	var result = false;
	
	//PANELES HORIZONTALES ARBOL - TEXTO
    $('panelTramitesDeterminacionesAplicaciones').setStyle('height', (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px');
    addSplitPanel('panelTramitesDeterminacionesAplicaciones', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function() {
		    result = addTree(data, undefined, undefined, undefined, 'fspElement' + 'panelTramitesDeterminacionesAplicaciones', 'folders', 'Planes y Entidades', false, 'valores', ['javascriptsV2/mooTree2/Valores.gif'], '100%', (parseInt($('panelTramitesDeterminacionesAplicaciones').getStyle('height')) - 250) + 'px', 'tabTramitesDeterminacionesAplicaciones',
    			function(node, state) {
    			    if (state && $('sspElement' + 'panelTramitesDeterminacionesAplicaciones')) {
    			    	
			        	if (!node.data.texto || node.data.texto.length == 0 || node.data.texto == textoAsociadoVisorPlanes){
			        		node.data.texto = textoAsociadoVisorPlanes;
			        	}
			        	$('sspElement' + 'panelTramitesDeterminacionesAplicaciones'+'textArea').set('html', node.data.texto);
    			    	
    			    }
    			});
		    
		    var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesDeterminacionesAplicaciones'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesDeterminacionesAplicaciones'));
		    
		    MUI.rWidth();
		});
    
    return result;
}

function chargeTabDeterminacionesGruposAplicacion(data){
	
	var supPanelHeight = (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px';
    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
	
	var result =  addTable(data, undefined, undefined, viewerPlanesTypeGruposAplicacion, 'panelTramitesDeterminacionesGruposAplicacion', true, '100%', tableHeight, 'tabTramitesDeterminacionesGruposAplicacion', undefined);
	
	return result;
}

function chargeTabDeterminacionesReguladoras(data){
	
	var result = false;
	
	//TABLA - TEXTO
    $('panelTramitesDeterminacionesDeterminacionesReguladoras').setStyle('height', (parseInt($('tabTramitesDeterminaciones-panel').getStyle('height')) - 4) + 'px');
    addSplitPanel('panelTramitesDeterminacionesDeterminacionesReguladoras', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function() {
    	
	    	var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesDeterminacionesDeterminacionesReguladoras').getStyle('height');
		    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
    	
		    result = addTable(dataOperaDeterminacionesReguladoras, undefined, undefined, viewerPlanesTypeDetReguladoras, 'fspElement' + 'panelTramitesDeterminacionesDeterminacionesReguladoras', true, "100%", tableHeight, 'tabTramitesDeterminacionesDeterminacionesReguladoras',
    			function(info) {
    			    if (info && $('sspElement' + 'panelTramitesDeterminacionesDeterminacionesReguladoras')) {
    			        var dataRow = info.target.getDataByRow(info.row);
    			        
    			        if (dataRow) {
    			        	
    			        	if (!dataRow.texto || dataRow.texto.length == 0 || dataRow.texto == textoAsociadoVisorPlanes){
    			        		dataRow.texto = textoAsociadoVisorPlanes;
    			        	}
    			        	$('sspElement' + 'panelTramitesDeterminacionesDeterminacionesReguladoras'+'textArea').set('html', dataRow.texto);    			        	
    			        }    			        
    			    }
    			});
		    
		    var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesDeterminacionesDeterminacionesReguladoras'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesDeterminacionesDeterminacionesReguladoras'));
		    
		    MUI.rWidth();
		});
	
	return result;
}

function BtnVisorPlanesBuscarDeterminaciones(){
	if (visorPLANESCurrentSearchWindow == undefined){		
		visorPLANESCurrentSearchWindow = function(){ 
			new MUI.Modal({
			id: 'VisorPLANESWindowBusquedaDeterminaciones',
			title: 'Búsqueda de determinaciones',
			icon: 'styles/images/search_icon.png',			
			contentURL: 'Pages/VisorPLANES/VisorPLANESWindows/VisorPLANESWindowBusquedaDeterminaciones.jsp',
			width: 600,
			height: 250,
			maximizable: false,
			resizable: false,
			scrollbars: false,
			draggable: true,
			onContentLoaded: function(){
				buquedaModificadaVisorPlanes = true; //Para que se inicie el parametro de busqueda
				
				$('txtNombre').focus();
				
		        //accion del boton buscar, hay que seleccionar el nodo inicio de la busqueda
				$('BtnVisorPlanesCancelarBuscarDeterminaciones').addEvent('click',function(e){
		        	new Event(e).stop();
		        	$('VisorPLANESWindowBusquedaDeterminaciones').retrieve('instance').close();
		        });
		        $('BtnVisorPlanesBuscarDeterminaciones').addEvent('click',function(e){
		        	new Event(e).stop();	
		        	
		        	if (buquedaModificadaVisorPlanes == false)
		        	{
		        		var resultSearch = visorPLANESTreeDeterminaciones.findNext();
		        		if (resultSearch == -2){
		        			alert("No hay más resultados");
		        		} else {
		        			resultadosBusquedaCounter = resultadosBusquedaCounter + 1;
		        			$('BtnVisorPlanesResultadosBuscarDeterminaciones').set('html',resultadosBusquedaCounter+' de '+resultadosBusquedaCount+' resultados');
		        		}		        		
		        	} else {
		        		var idRaizParam = visorPLANESIdTramiteCurentlySelected;
				        var tipoRaizParam = 'tramite';
				        if (visorPLANESTreeDeterminaciones.selected){
				        	var node = visorPLANESTreeDeterminaciones.selected;
				        	while(!node.data.tipo && node.parent){
				        		node = node.parent;
				        	}
				        	
				        	if (node.data.tipo){
				        		idRaizParam = node.data.idNode;
				        		tipoRaizParam = node.data.tipo;
				        	}
				        }
			        	
				        var req = new Request.JSON({
				        url: 'GestionConsola',
				        data: {
				            wsName: 'SEARCH_ON_TREE',
				            tipoBusqueda: 'determinaciones',
				            tipo: visorPLANESviewerTreeType,
				            idRaiz: idRaizParam,
				            tipoRaiz: tipoRaizParam,
				            nombreDeterminacion: $('txtNombre').value,
				            codigoDeterminacion: $('txtCodigo').value,
				            apartadoDeterminacion: $('txtApartado').value,
				        },
				        timeout: 5000,
				        async:true,
				        onSuccess: function(result){
				        	enableDisableTree($('treeDeterminaciones'), true);
				        	
				        	if (result.resultado && result.resultado.length > 0){
				        		//Accion cuando la llamada ha devuelto un dato correcto
				        		buquedaModificadaVisorPlanes = false;
				        		visorPLANESTreeDeterminaciones.find(result.resultado);
				        		if (result.total)  { 
				        			alert('Se han obtenido '+result.total+' resultados para la búsqueda realizada, para ir recorriendolos sin modificar los parametros de búsqueda vaya pulsando el botón buscar');
				        			$('BtnVisorPlanesResultadosBuscarDeterminaciones').set('html','1 de '+result.total+' resultados');
			        				resultadosBusquedaCounter = 1;
			        				resultadosBusquedaCount = result.total;
			        			}
				        	} else {
				        		alert('No se han obtenido resultados para la búsqueda realizada');				        		
				        	}
				        	
				        	//$('VisorPLANESWindowBusquedaDeterminaciones').retrieve('instance').close();
			        	},
				        onFailure: function(response){
				        	enableDisableTree($('treeDeterminaciones'), true);
				        	alert('Error al realizar la búsqueda');
				        },
				    	onTimeout: function(){
				    		enableDisableTree($('treeDeterminaciones'), true);
				    		alert('No se ha obtenido respuesta del servidor');
				    	}
				        });
				        enableDisableTree($('treeDeterminaciones'), false);
				        req.send();
		        	}			        			        
		        });
			},
			onCloseComplete: function(){
				visorPLANESCurrentSearchWindow = undefined;
			}			
		});
		}; //Fin var window
		
		visorPLANESCurrentSearchWindow(); //Muestro la ventana
		
	} //Fin if
}