
function chargeTabEntidadesRegimenDirecto(data){
	
	var result = false;
	
	//PANELES HORIZONTALES ARBOL y DOS TABs SIN CABECERA SEGUN SE SELECCIONE VALORES (TEXTO) o DOCUMENTOS (FORMULARIO). DOBLE CLICK ARBOL DOCUMENTO ABRE DOCUMENTO	
	$('panelTramitesEntidadesRegimenDirecto').setStyle('height',(parseInt($('tabTramitesEntidades-panel').getStyle('height')) - 4)+'px');
	$('panelTramitesEntidadesRegimenDirectoData').setStyle('height',(parseInt($('panelTramitesEntidadesRegimenDirecto').getStyle('height')) - parseInt($('panelEntidadesRegimenDirectoheader').getStyle('height')))+'px');
	
	addSplitPanel('panelTramitesEntidadesRegimenDirectoData', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function(){	    	                    					    	                    			
			result = addTree(data, undefined, undefined, undefined, 'fspElement'+'panelTramitesEntidadesRegimenDirectoData', 'folders', 'Normas generales', false, 'valores', ['javascriptsV2/mooTree2/Valores.gif'], '100%', (parseInt($('panelTramitesEntidadesRegimenDirectoData').getStyle('height')) - 250) + 'px', 'tabTramitesEntidadesRegimenDirecto', 
    			function(node, state) {
				
				$('BtnVisorPlanesEntidadesRegimenDirectoDescargar').removeEvents();
				disableControl('BtnVisorPlanesEntidadesRegimenDirectoDescargar');				
				
				//Segun el tipo de nodo seleccionado hay que mostrar un panel u otro
				if (node.data.tipo && $('sspElement'+'panelTramitesEntidadesRegimenDirectoData')){
					
					$('sspElement'+'panelTramitesEntidadesRegimenDirectoData').empty();										
					
					switch(node.data.tipo) {
					case 'regimenespecifico':												
						var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesRegimenDirectoData'+'textArea','class':'centerElement','readonly':'readonly'});    		
			    		toInsert.addClass('infomsgTextArea');
			    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesRegimenDirectoData'));
						
						if (!node.data.texto || node.data.texto.length == 0 || node.data.texto == textoAsociadoVisorPlanes){
			        		node.data.texto = textoAsociadoVisorPlanes;
			        	}
						toInsert.set('html', node.data.texto);
						break;
					case 'documento':
						var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesRegimenDirectoData'+'textArea','class':'centerElement','readonly':'readonly'});    		
			    		toInsert.addClass('infomsgTextArea');
			    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesRegimenDirectoData'));
						
						if (!node.data.comentario || node.data.comentario.length == 0 || node.data.comentario == textoAsociadoVisorPlanes){
			        		node.data.comentario = textoAsociadoVisorPlanes;
			        	}
						toInsert.set('html', node.data.comentario);
						/*
						//Hay que cargar una tabla sobre el div
						for(var colidx=0,len=viewerPlanesTypeRegimenDirectoDocumento.length; colidx<len; colidx++){
	    	                
	    	            	// cmi --> Column Model Item
	    	                var cmi = viewerPlanesTypeRegimenDirectoDocumento[colidx];
	    	                
	    	                var toInsert;
	    	                var input; //lo defino aqui porque se usa fuera de los ifs

    	                	//elemento principal
    	                	toInsert = new Element('div',{
    	                		'class':'formRow'
    	                	});
    	                	//label
    	                	var label = new Element('label',{
    	                        'for': cmi.dataIndex,
    	                        'class':'formLabel',
    	                        'html': initCap(cmi.header.toLowerCase()) + ': '
    	                    });
    	                		
    	                	//label.setStyle('width',labelWidth);
    	                	
    	                	//formField
    	                	var formField = new Element('div',{
    	                		'class':'formField'
    	                	});
    	                	
    	                	if (cmi.dataIndex == 'comentario'){
    	                		//texto
    	                		if (!node.data[cmi.dataIndex] || node.data[cmi.dataIndex].length == 0 || node.data[cmi.dataIndex] == textoAsociadoVisorPlanes){
    	                			node.data[cmi.dataIndex] = textoAsociadoVisorPlanes;
    				        	}
    	                		
        	                	input = new Element('textarea',{
        	                		name: cmi.dataIndex,
        	                		id: cmi.dataIndex,
        	                		'class':'centerElement',
        	                		'readonly':'readonly',
        	                		'html': node.data[cmi.dataIndex]});
        	                	input.addClass('infomsgTextArea');
    	                	} else {
    	                		//input
        	                	input = new Element('label', {
        	                        name: cmi.dataIndex,
        	                        id: cmi.dataIndex,
        	                        'html': node.data[cmi.dataIndex],
        	                        'class': 'imputLabel'
        	                    }); 
    	                	}
    	                	
    	                	
    	                	//input.setStyle('width',parseInt($(sitio).getStyle('width')) - 230 + 'px');
    	                	input.setStyle('width','100%');
    	                	//inserto el inputp en el formField
    	                	input.inject(formField);
    	                	//div clear
    	                	var divClear = new Element('div',{
    	                		'class':'clear'
    	                	});
    	                	
    	                	//inserto el label en el principal
    	                	label.inject(toInsert);
    	                	//inserto el field en el principal
    	                	formField.inject(toInsert);
    	                	//inserto el clear en el principal
    	                	divClear.inject(toInsert);

	    	                toInsert.inject($('sspElement' + 'panelTramitesEntidadesRegimenDirectoData'));
	    	            }*/						
						$('BtnVisorPlanesEntidadesRegimenDirectoDescargar').addEvent('click',function(e){
							window.open('GestionConsola?wsName=GET_DOC_URL&idDoc='+node.data['idNode']+'&idioma='+idioma,'_blank');
						});
						enableControl('BtnVisorPlanesEntidadesRegimenDirectoDescargar');
						break;
					default:
						var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesRegimenDirectoData'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
		    			toInsert.addClass('infomsgTextArea');
		    			toInsert.inject($('sspElement' + 'panelTramitesEntidadesRegimenDirectoData'));
		    			toInsert.set('html', textoAsociadoVisorPlanes);
						break;
					}
				}					
            });
			MUI.rWidth();
		});
	
		if ($('BtnVisorPlanesEntidadesRegimenDirectoExpandir')){
			$('BtnVisorPlanesEntidadesRegimenDirectoExpandir').addEvent('click',  function () { 
				//visorPLANESTree.expand(); ESTO SERIA UN EXPAND ALL
				if (result.selected){
					result.selected.expand();
				}
			});
			$('BtnVisorPlanesEntidadesRegimenDirectoContraer').addEvent('click',  function () { result.collapse(); });
			$('BtnVisorPlanesEntidadesRegimenDirectoCambiarVista').addEvent('click',  function () { 
				if ($('BtnVisorPlanesEntidadesRegimenDirectoCambiarVista').parentNode.hasClass('iconWrapperPushed')){
					//MOSTRAR EN PLANO
					getTreeData('GestionConsola?wsName=GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO&idEnt='+visorPLANESTreeEntidades.selected.data.idNode+'&formato=plano&tipo=regimendirecto&idioma='+idioma, undefined, true, undefined, 
	                		function(data){
							result.root.loadData(data);
							$('sspElement'+'panelTramitesEntidadesRegimenDirectoData').empty();	
						});									
					$('BtnVisorPlanesEntidadesRegimenDirectoCambiarVista').parentNode.removeClass('iconWrapperPushed');
				} else {
					//MOSTRAR EN ARBOL
					getTreeData('GestionConsola?wsName=GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO&idEnt='+visorPLANESTreeEntidades.selected.data.idNode+'&tipo=regimendirecto&idioma='+idioma, undefined, true, undefined, 
	                		function(data){
							result.root.loadData(data);
							$('sspElement'+'panelTramitesEntidadesRegimenDirectoData').empty();	
						});
					$('BtnVisorPlanesEntidadesRegimenDirectoCambiarVista').parentNode.addClass('iconWrapperPushed');
				}
			});
		}
	
	return result;
}

function chargeTabEntidadesUsos(data){
	
	var result = false;
	
	//PANELES HORIZONTALES ARBOL - TEXTO
	$('panelTramitesEntidadesUsos').setStyle('height',(parseInt($('tabTramitesEntidades-panel').getStyle('height')) - 4)+'px');
	$('panelTramitesEntidadesUsosData').setStyle('height',(parseInt($('panelTramitesEntidadesUsos').getStyle('height')) - parseInt($('panelTramitesEntidadesUsosheader').getStyle('height')))+'px');
	addSplitPanel('panelTramitesEntidadesUsosData', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function(){	    	                    					    	                    			
			result = addTree(data, undefined, undefined, undefined, 'fspElement'+'panelTramitesEntidadesUsosData', 'folders', 'Usos', false, 'valores', ['javascriptsV2/mooTree2/Valores.gif'], '100%', (parseInt($('panelTramitesEntidadesUsosData').getStyle('height')) - 250) + 'px', 'tabTramitesEntidadesUsos', 
    			function(node, state) {
					$('BtnVisorPlanesEntidadesUsosDescargar').removeEvents();
					disableControl('BtnVisorPlanesEntidadesUsosDescargar');
				
                    if (state && $('sspElement'+'panelTramitesEntidadesUsosData')) {
                    	
                    	$('sspElement'+'panelTramitesEntidadesUsosData').empty();	
                    	
                    	var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesUsosData'+'textArea','class':'centerElement','readonly':'readonly'});    		
			    		toInsert.addClass('infomsgTextArea');
			    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesUsosData'));
                    	
                    	if (!node.data.texto || node.data.texto.length == 0 || node.data.texto == textoAsociadoVisorPlanes){
			        		node.data.texto = textoAsociadoVisorPlanes;
			        	}
                    	toInsert.set('html', node.data.texto);
			        	
			        	if (node.data.tipo == 'documento'){			        		
							$('BtnVisorPlanesEntidadesUsosDescargar').addEvent('click',function(e){
								window.open('GestionConsola?wsName=GET_DOC_URL&idDoc='+node.data['idNode']+'&idioma='+idioma,'_blank');
							});
							enableControl('BtnVisorPlanesEntidadesUsosDescargar');
			        	}
                    }
            });
			
			var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesUsosData'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesUsosData'));
			
			MUI.rWidth();
		});	
	
		if ($('BtnVisorPlanesEntidadesUsosExpandir')){
			$('BtnVisorPlanesEntidadesUsosExpandir').addEvent('click',  function () { 
				//visorPLANESTree.expand(); ESTO SERIA UN EXPAND ALL
				if (result.selected){
					result.selected.expand();
				}
			});
			$('BtnVisorPlanesEntidadesUsosContraer').addEvent('click',  function () { result.collapse(); });
			$('BtnVisorPlanesEntidadesUsosCambiarVista').addEvent('click',  function () { 
				if ($('BtnVisorPlanesEntidadesUsosCambiarVista').parentNode.hasClass('iconWrapperPushed')){
					//MOSTRAR EN PLANO
					getTreeData('GestionConsola?wsName=GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO&idEnt='+visorPLANESTreeEntidades.selected.data.idNode+'&formato=plano&tipo=uso&idioma='+idioma, undefined, true, undefined,
							function(data){
							result.root.loadData(data);
							$('sspElement'+'panelTramitesEntidadesUsosData').empty();									
						});
					$('BtnVisorPlanesEntidadesUsosCambiarVista').parentNode.removeClass('iconWrapperPushed');
				} else {
					//MOSTRAR EN ARBOL
					getTreeData('GestionConsola?wsName=GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO&idEnt='+visorPLANESTreeEntidades.selected.data.idNode+'&tipo=uso&idioma='+idioma, undefined, true, undefined,
							function(data){
							result.root.loadData(data);
							$('sspElement'+'panelTramitesEntidadesUsosData').empty();									
						});
					$('BtnVisorPlanesEntidadesUsosCambiarVista').parentNode.addClass('iconWrapperPushed');
				}
			});
		}
	
	return result;
}

function chargeTabEntidadesActos(data){
	
	var result = false;
	
	//PANELES HORIZONTALES ARBOL - TEXTO
	$('panelTramitesEntidadesActos').setStyle('height',(parseInt($('tabTramitesEntidades-panel').getStyle('height')) - 4)+'px');
	$('panelTramitesEntidadesActosData').setStyle('height',(parseInt($('panelTramitesEntidadesActos').getStyle('height')) - parseInt($('panelTramitesEntidadesActosheader').getStyle('height')))+'px');
	addSplitPanel('panelTramitesEntidadesActosData', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function(){	    	                    					    	                    			
			result = addTree(data, undefined, undefined, undefined, 'fspElement'+'panelTramitesEntidadesActosData', 'folders', 'Actos', false, 'valores', ['javascriptsV2/mooTree2/Valores.gif'], '100%', (parseInt($('panelTramitesEntidadesActosData').getStyle('height')) - 250) + 'px', 'tabTramitesEntidadesActos', 
    			function(node, state) {
					$('BtnVisorPlanesEntidadesActosDescargar').removeEvents();
					disableControl('BtnVisorPlanesEntidadesActosDescargar');
				
                    if (state && $('sspElement'+'panelTramitesEntidadesActosData')) {
                    	
            			$('sspElement'+'panelTramitesEntidadesActosData').empty();	
                    	
                    	var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesActosData'+'textArea','class':'centerElement','readonly':'readonly'});    		
			    		toInsert.addClass('infomsgTextArea');
			    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesActosData'));
                    	
                    	if (!node.data.texto || node.data.texto.length == 0 || node.data.texto == textoAsociadoVisorPlanes){
			        		node.data.texto = textoAsociadoVisorPlanes;
			        	}
                    	toInsert.set('html', node.data.texto);

			        	if (node.data.tipo == 'documento'){			        		
							$('BtnVisorPlanesEntidadesActosDescargar').addEvent('click',function(e){
								window.open('GestionConsola?wsName=GET_DOC_URL&idDoc='+node.data['idNode']+'&idioma='+idioma,'_blank');
							});
							enableControl('BtnVisorPlanesEntidadesActosDescargar');
			        	}
                    }
            });
			
			var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesActosData'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesActosData'));
			
			MUI.rWidth();
		});	
	
		if ($('BtnVisorPlanesEntidadesActosExpandir')){
			$('BtnVisorPlanesEntidadesActosExpandir').addEvent('click',  function () { 
				//visorPLANESTree.expand(); ESTO SERIA UN EXPAND ALL
				if (result.selected){
					result.selected.expand();
				}
			});
			$('BtnVisorPlanesEntidadesActosContraer').addEvent('click',  function () { result.collapse(); });		
			$('BtnVisorPlanesEntidadesActosCambiarVista').addEvent('click',  function () { 
				if ($('BtnVisorPlanesEntidadesActosCambiarVista').parentNode.hasClass('iconWrapperPushed')){
					//MOSTRAR EN PLANO
					getTreeData('GestionConsola?wsName=GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO&idEnt='+visorPLANESTreeEntidades.selected.data.idNode+'&formato=plano&tipo=acto&idioma='+idioma, undefined, true, undefined,
							function(data){
							result.root.loadData(data);
							$('sspElement'+'panelTramitesEntidadesActosData').empty();									
						});
					$('BtnVisorPlanesEntidadesActosCambiarVista').parentNode.removeClass('iconWrapperPushed');
				} else {
					//MOSTRAR EN ARBOL
					getTreeData('GestionConsola?wsName=GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO&idEnt='+visorPLANESTreeEntidades.selected.data.idNode+'&tipo=acto&idioma='+idioma, undefined, true, undefined,
							function(data){
							result.root.loadData(data);
							$('sspElement'+'panelTramitesEntidadesActosData').empty();									
						});
					$('BtnVisorPlanesEntidadesActosCambiarVista').parentNode.addClass('iconWrapperPushed');
				}
			});
		}
	
	return result;
}

function chargeTabEntidadesEsOperadorPor(data){
	
	var result = false;
    
	//PANELES HORIZONTALES GRID - TEXTO
	$('panelTramitesEntidadesEsOperadorPor').setStyle('height',(parseInt($('tabTramitesEntidades-panel').getStyle('height')) - 4)+'px');
	addSplitPanel('panelTramitesEntidadesEsOperadorPor', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function(){	    	         
		
			var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesEntidadesEsOperadorPor').getStyle('height');
		    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
		
			result = addTable(data, undefined, undefined, viewerTypeEntidadEsOperadoPor, 'fspElement'+'panelTramitesEntidadesEsOperadorPor', true, "100%", tableHeight, 'tabTramitesEntidadesEsOperadorPor',
    			function(info) {
                    if (info && $('sspElement'+'panelTramitesEntidadesEsOperadorPor')) {
                    	var dataRow=info.target.getDataByRow(info.row);
                    	
                    	if (dataRow) {
    			        	
    			        	if (!dataRow.regimen || dataRow.regimen.length == 0 || dataRow.regimen == textoAsociadoVisorPlanes){
    			        		dataRow.regimen = textoAsociadoVisorPlanes;
    			        	}
    			        	$('sspElement' + 'panelTramitesEntidadesEsOperadorPor'+'textArea').set('html', dataRow.regimen);
    			        	
    			        }
                    	
                    }
            });
			
			var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesEsOperadorPor'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesEsOperadorPor'));
			
			MUI.rWidth();
		}); 
	
	return result;
}

function chargeTabEntidadesOperaA(data){
	
	var result = false;
	
	//PANELES HORIZONTALES GRID - TEXTO
	$('panelTramitesEntidadesOperaA').setStyle('height',(parseInt($('tabTramitesEntidades-panel').getStyle('height')) - 4)+'px');
	addSplitPanel('panelTramitesEntidadesOperaA', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function(){	    	            
		
			var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesEntidadesOperaA').getStyle('height');
		    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
		
			result = addTable(data, undefined, undefined, viewerPlanesTypeEntidadOperaA, 'fspElement'+'panelTramitesEntidadesOperaA', true, "100%", tableHeight, 'tabTramitesEntidadesOperaA',
    			function(info) {
                    if (info && $('sspElement'+'panelTramitesEntidadesOperaA')) {
                    	var dataRow=info.target.getDataByRow(info.row);
                    	
                    	if (dataRow) {
    			        	
    			        	if (!dataRow.regimen || dataRow.regimen.length == 0 || dataRow.regimen == textoAsociadoVisorPlanes){
    			        		dataRow.regimen = textoAsociadoVisorPlanes;
    			        	}
    			        	$('sspElement' + 'panelTramitesEntidadesOperaA'+'textArea').set('html', dataRow.regimen);
    			        	
    			        }
                    	
                    }
            });
			
			var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesOperaA'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesOperaA'));
			
			MUI.rWidth();
		}); 
	
	return result;
}

function chargeTabEntidadesPropiedadesAdscripcionOrigen(data){
	
	var result = false;
	
	//PANELES HORISONTALES GRID - TEXTO
	$('panelTramitesEntidadesAdscripcionOrigen').setStyle('height',(parseInt($('tabTramitesEntidades-panel').getStyle('height')) - 4)+'px');
	addSplitPanel('panelTramitesEntidadesAdscripcionOrigen', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function(){	    	 
		
			var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesEntidadesAdscripcionOrigen').getStyle('height');
		    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
		
			result = addTable(data, undefined, undefined, viewerPlanesTypePropiedadesAdscripcionOrigen, 'fspElement'+'panelTramitesEntidadesAdscripcionOrigen', true, "100%", tableHeight, 'tabTramitesEntidadesAdscripcionOrigen',
    			function(info) {
                    if (info && $('sspElement'+'panelTramitesEntidadesAdscripcionOrigen')) {
                    	var dataRow=info.target.getDataByRow(info.row);
                    	
                    	if (dataRow) {
    			        	
    			        	if (!dataRow.texto || dataRow.texto.length == 0 || dataRow.texto == textoAsociadoVisorPlanes){
    			        		dataRow.texto = textoAsociadoVisorPlanes;
    			        	}
    			        	$('sspElement' + 'panelTramitesEntidadesAdscripcionOrigen'+'textArea').set('html', dataRow.texto);    			        
    			        }
                    }
            });
			
			var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesAdscripcionOrigen'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesAdscripcionOrigen'));
			
			MUI.rWidth();
		}); 
	
	return result;
}

function chargeTabEntidadesPropiedadesAdscripcionDestino(data){
	
	var result = false;
	
	//PANELES HORISONTALES GRID - TEXTO
	$('panelTramitesEntidadesAdscripcionDestino').setStyle('height',(parseInt($('tabTramitesEntidades-panel').getStyle('height')) - 4)+'px');
	addSplitPanel('panelTramitesEntidadesAdscripcionDestino', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function(){	  
		
			var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesEntidadesAdscripcionDestino').getStyle('height');
		    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
		
			result = addTable(data, undefined, undefined, viewerPlanesTypePropiedadesAdscripcionDestino, 'fspElement'+'panelTramitesEntidadesAdscripcionDestino', true, "100%", tableHeight, 'tabTramitesEntidadesAdscripcionDestino',
    			function(info) {
                    if (info && $('sspElement'+'panelTramitesEntidadesAdscripcionDestino')) {
                    	var dataRow=info.target.getDataByRow(info.row);
                    	
                		if (dataRow) {
    			        	
    			        	if (!dataRow.texto || dataRow.texto.length == 0 || dataRow.texto == textoAsociadoVisorPlanes){
    			        		dataRow.texto = textoAsociadoVisorPlanes;
    			        	}
    			        	$('sspElement' + 'panelTramitesEntidadesAdscripcionDestino'+'textArea').set('html', dataRow.texto);    			        
    			        }
                    }
            });
			
			var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesAdscripcionDestino'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesAdscripcionDestino'));
			
			MUI.rWidth();
		}); 
	
	return result;
}

function chargeTabEntidadesDocumentos(data){
	
	var result = false;
	
	//PANELES HORIZONTALES GRID - TEXTO - DBL CLICK SOBRE GRID ABRE DOCUMENTO
    $('panelTramitesEntidadesDocumentos').setStyle('height', (parseInt($('tabTramitesEntidades-panel').getStyle('height')) - 4) + 'px');
    addSplitPanel('panelTramitesEntidadesDocumentos', 'HORIZONTAL', undefined, undefined, -1, true, undefined, undefined, 242, true,
		function() {
    	
	    	var supPanelHeight = $('firstPanelSplitPanel'+'panelTramitesEntidadesDocumentos').getStyle('height');
		    var tableHeight = calculateTableHeight(data.data.length, supPanelHeight);
    	
		    result = addTable(data, undefined, undefined, viewerPlanesTypeDocumentosEntidad, 'fspElement' + 'panelTramitesEntidadesDocumentos', true, "100%", tableHeight, 'tabTramitesEntidadesDocumentos',
    			function(info) { //CLICK
    			    if (info && $('sspElement' + 'panelTramitesEntidadesDocumentos')) {
    			        var dataRow = info.target.getDataByRow(info.row);
    			        
    			        if (dataRow) {
    			        	
    			        	if (!dataRow.comentario || dataRow.comentario.length == 0 || dataRow.comentario == textoAsociadoVisorPlanes){
    			        		dataRow.comentario = textoAsociadoVisorPlanes;
    			        	}
    			        	$('sspElement' + 'panelTramitesEntidadesDocumentos'+'textArea').set('html', dataRow.comentario);
    			        	
    			        }
    			        
    			    }
    			},
    			function(info) { //DBL CLICK
    				openGridDocument(info);    			  
    			});
		    
		    var toInsert = new Element('textarea',{'id':'sspElement'+'panelTramitesEntidadesDocumentos'+'textArea','class':'centerElement','class':'centerElement','readonly':'readonly'});    		
    		toInsert.addClass('infomsgTextArea');
    		toInsert.inject($('sspElement' + 'panelTramitesEntidadesDocumentos'));
		    
		    MUI.rWidth();
		});  
    
    return result;
}


function BtnVisorPlanesBuscarEntidades(){
	if (visorPLANESCurrentSearchWindow == undefined){		
		visorPLANESCurrentSearchWindow = function(){ 
			new MUI.Modal({
			id: 'VisorPLANESWindowBusquedaEntidades',
			title: 'Búsqueda de entidades',
			icon: 'styles/images/search_icon.png',			
			contentURL: 'Pages/VisorPLANES/VisorPLANESWindows/VisorPLANESWindowBusquedaEntidades.jsp',
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
				$('BtnVisorPlanesCancelarBuscarEntidades').addEvent('click',function(e){
		        	new Event(e).stop();
		        	$('VisorPLANESWindowBusquedaEntidades').retrieve('instance').close();
		        });
		        $('BtnVisorPlanesBuscarEntidades').addEvent('click',function(e){
		        	new Event(e).stop();
		        	
		        	if (buquedaModificadaVisorPlanes == false)
		        	{
		        		var resultSearch = visorPLANESTreeEntidades.findNext();
		        		if (resultSearch == -2){
		        			alert("No hay más resultados");
		        		} else {
		        			resultadosBusquedaCounter = resultadosBusquedaCounter + 1;
		        			$('BtnVisorPlanesResultadosBuscarEntidades').set('html',resultadosBusquedaCounter+' de '+resultadosBusquedaCount+' resultados');
		        		}		        		
		        	} else {
		        		var idRaizParam = visorPLANESIdTramiteCurentlySelected;
				        var tipoRaizParam = 'tramite';
				        if (visorPLANESTreeEntidades.selected){
				        	var node = visorPLANESTreeEntidades.selected;
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
				            tipoBusqueda: 'entidades',
				            tipo: visorPLANESviewerTreeType,
				            idRaiz: idRaizParam,
				            tipoRaiz: tipoRaizParam,
				            nombreEntidad: $('txtNombre').value,
				            codigoEntidad: $('txtCodigo').value,
				            claveEntidad: $('txtClave').value,
				        },
				        timeout: 5000,
				        async:true,
				        onSuccess: function(result){
				        	enableDisableTree($('treeEntidades'), true);
				        	
				        	if (result.resultado && result.resultado.length > 0){
				        		//Accion cuando la llamada ha devuelto un dato correcto
				        		buquedaModificadaVisorPlanes = false;
				        		visorPLANESTreeEntidades.find(result.resultado);
				        		if (result.total) { 
				        			alert('Se han obtenido '+result.total+' resultados para la búsqueda realizada, para ir recorriendolos sin modificar los parametros de búsqueda vaya pulsando el botón buscar');
				        			$('BtnVisorPlanesResultadosBuscarEntidades').set('html','1 de '+result.total+' resultados');
			        				resultadosBusquedaCounter = 1;
			        				resultadosBusquedaCount = result.total;
			        			}
				        	} else {
				        		alert('No se han obtenido resultados para la búsqueda realizada');
				        	}
				        	
				        	//$('VisorPLANESWindowBusquedaEntidades').retrieve('instance').close();
			        	},
				        onFailure: function(response){
				        	enableDisableTree($('treeEntidades'), true);
				        	alert('Error al realizar la búsqueda');
				        },
				    	onTimeout: function(){
				    		enableDisableTree($('treeEntidades'), true);
				    		alert('No se ha obtenido respuesta del servidor');
				    	}
				        });
				        enableDisableTree($('treeEntidades'), false);
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