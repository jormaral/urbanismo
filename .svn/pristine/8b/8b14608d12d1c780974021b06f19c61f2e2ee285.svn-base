
function addForm(data, id, tipo, columns, sitio, labelWidth, idTabContainer){
	
	var dev = false;
	
	if ($(sitio)){
		
		if (!columns){
	    	
	    	columns = runServiceJson({
		        wsName: 'GET_COLUMNMODEL',
		        ID: id,
		        TIPO: tipo
		    });
		}
	    	
    	if (columns){
    		
    		if (!data) {
            	data = getFormData(id,tipo);
            }
    		
    	    if (data){
    	    	
    	    	data = data.data[0];
    	    	
    	    	//Crea el elemento donde se guarda el formulario
    	        if($(sitio).getElements('div')){
    	            var formEl = new Element('div',{
    	                'id': 'formDatosGenerales'
    	            });
    	            $(sitio).grab(formEl);
    	            
    	            /*
    	    	 	<div class="formRow">
    				<div class="formLabel">Nombre:</div>
    				<div class="formField"><input type="text" class="input" id="txtNombre" value="" maxlength="50" /></div>
    				<div class="clear"></div>
    				</div>
    	    	 * */

    	            // generar los campos del formulario segun las columnas devueltas por el GET_COLUMNMODEL
    	            for(var colidx=0,len=columns.length; colidx<len; colidx++){
    	                
    	            	// cmi --> Column Model Item
    	                var cmi = columns[colidx];

    	                // campo identificador --> campo oculto
    	                var oculto = (cmi.dataIndex.toLowerCase() == "id" || cmi.dataIndex.toLowerCase() == "iden");
    	                
    	                var toInsert;
    	                var input; //lo defino aqui porque se usa fuera de los ifs
    	                
    	                if (cmi.hidden == false){
    	                	if (!oculto){
        	                	//elemento principal
        	                	toInsert = new Element('div',{
        	                		'class':'formRow'
        	                	});
        	                	//label
        	                	var label = new Element('label',{
        	                        'for': cmi.dataIndex,
        	                        'class':'formLabel',
        	                        'html': initCap(cmi.header) + ': '
        	                    });
        	                	if (labelWidth){
        	                		label.setStyle('width',labelWidth);
        	                	}
        	                	
        	                	//formField
        	                	var formField = new Element('div',{
        	                		'class':'formField'
        	                	});
        	                	//input
        	                	input = new Element('label', {
        	                        name: cmi.dataIndex,
        	                        id: cmi.dataIndex,
        	                        'html': data[cmi.dataIndex],
        	                        'class': 'imputLabel'
        	                    }); 
        	                	
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
        	                }
        	                else{
        	                	input = new Element('input', {
        	                        type: 'hidden',
        	                        name: cmi.dataIndex,
        	                        id: cmi.dataIndex,
        	                        value: value,
        	                        'class': 'input',
        	                        'readonly': 'readonly'
        	                    });
        	                	input.setStyle('width',parseInt($(sitio).getStyle('width')) - 230 + 'px');
        	                	toInsert = input;
        	                }

        	                toInsert.inject(formEl);
    	                }    	                    	                    	                   	                    	                  
    	            } //fin del bucle de elementos
    	            
    	            dev = true; 
    	            
    	            if(idTabContainer && $(idTabContainer)){
	    	    		$(idTabContainer).setStyle('display', '');
	    	    	}

    	        } else {
    	            alert('no tiene elementos - ' + sitio);
    	        }
    	    	
    	    } //fin si hay data
    	} //fin si hay columnas    	
		
	}
    
    return dev;
}

function getFormData(id, tipo, async, idTabContainer, accion){
	var dev = undefined;
	var response;
	
	if (async){
		
		runServiceAsyncJson({'wsName' : 'GET_DATA','ID' : id,'TIPO' : tipo},
				function(response){
				if (response != null && response.data != null && response.data.length > 0){										
					
					if (accion){
            			accion(response);
            		}
					
			    	if(idTabContainer && $(idTabContainer)){
			    		$(idTabContainer).setStyle('display', '');
			    	} 
			    	
			    }else{
			    	
			    	if (accion){
            			accion(undefined);
            		}
			    	
			    }
			});
		
	} else {
		
		response = runServiceJson({'wsName' : 'GET_DATA','ID' : id,'TIPO' : tipo});	
		
		if (response != null && response.data != null && response.data.length > 0){
			dev = response;
			if(idTabContainer && $(idTabContainer)){
	    		$(idTabContainer).setStyle('display', '');
	    	} 
		}
	}				
	
	return dev;
}

function addTable(data, id, tipo, columns, sitio, mostrarCabecera, ancho, alto, idTabContainer, accionClick, accionDblClick ){
	
	var dataGrid = undefined;
	
	if ($(sitio)){
		
		if (!columns){
	    	
	    	columns = runServiceJson({
		        wsName: 'GET_COLUMNMODEL',
		        ID: id,
		        TIPO: tipo
		    });
		}
	    	
    	if (columns){
    		
    		if (!data) {
            	data = getTableData(id,tipo);
            }
    		
    	    if (data){
    	    	
    	        dataGrid= new omniGrid(sitio,{
    	            columnModel:columns,
    	            pagination:false,
    	            serverSort:false,
    	            showHeader: mostrarCabecera,
    	            alternaterows: true,
    	            sortHeader:true,
    	            resizeColumns:true,
    	            multipleSelection:false,
    	            sortOn: null,
    	            sortBy: 'ASC',
    	            width: ancho,
    	            height: alto
    	        });
    	        
    	        if (accionClick){
    	            dataGrid.addEvent('click',accionClick);
    	        }
    	        
    	        if (accionDblClick){
    	            dataGrid.addEvent('dblclick',accionDblClick);
    	        } else {
    	        	if (accionClick){
        	            dataGrid.addEvent('dblclick',accionClick);
        	        }
    	        }   	        
    	        
    	        dataGrid.setData(data);
    	        
    	        if(idTabContainer && $(idTabContainer)){
    	    		$(idTabContainer).setStyle('display', '');
    	    	} 
    	    	
    	    } //fin si hay data
    	} //fin si hay columnas    	
		
	} //fin si hay sitio
    
    return dataGrid;
}

function getTableData(id, tipo, async, idTabContainer, accion){
	var dev = undefined;
	var response;
	
	if (async){
		
		runServiceAsyncJson({'wsName' : 'GET_DATA','ID' : id,'TIPO' : tipo},
				function(response){
				if (response != null && response.data != null && response.data.length > 0){
					
					if (accion){
            			accion(response);
            		}
					
			    	if(idTabContainer && $(idTabContainer)){
			    		$(idTabContainer).setStyle('display', '');
			    	}    	    		
			    } else{
			    	
			    	if (accion){
            			accion(undefined);
            		}
			    	
			    }				
			});
		
	} else {
		
		response = runServiceJson({'wsName' : 'GET_DATA','ID' : id,'TIPO' : tipo});	
		
		if (response != null && response.data != null && response.data.length > 0){
			dev = response;
			if(idTabContainer && $(idTabContainer)){
	    		$(idTabContainer).setStyle('display', '');
	    	} 
		}
	}				
	
	return dev;
}

/*
Property: addTree
	Add a MooTree control to the page

Parameters:
	data - Data to charge on the tree, if is defined, the "wsName", "paramName" and "id" parameters will be ignored
	wsName - name of the ws to chage the data
	paramName - name of the param id to charge de data
	id - id of the data to charge on the tree
	sitio - where to insert the tree
	modo - mode folder or file of the tree
	textoRaiz - root text of the tree
	abierto - if the tree will be charged expanded or collapsed		
	idContainerTab - tab that contains de tree (optional)
	accion - action on select a tree node

Returns:
	data en xml format
*/
function addTree(data, wsName, paramName, id, sitio, modo, textoRaiz, abierto, treeType, icons, ancho, alto, idTabContainer, accion) {	
	
	var tree = undefined;
	
	if (!data){ // si no hay datos trato de cargarlos
	    data = getTreeData('GestionConsola?wsName='+wsName+'&'+paramName+'='+id+'&idioma='+idioma);	    	    
	}
	
	if (data && $(sitio)){
    	tree = new MooTreeControl({
            div: sitio,
            mode: modo,
            treeType: treeType,
            iconBar: icons,
            grid: true,
            onSelect: function(node, state) {
            	if (tree && tree.enabled == true && state && accion){
            		accion(node,state);
            	}            		            		
            } //fin onSelect
        },{
            text: cargarTextoSegunIdioma(textoRaiz),
            open: abierto
        });
	    
    	$(sitio).setStyle('width',ancho);
	    $(sitio).setStyle('height', alto);	    	    	    
	    
    	tree.root.loadData(data);
    	
		dev = true;
    	
    	if(idTabContainer && $(idTabContainer)){
    		$(idTabContainer).setStyle('display', '');
    	}
    } //fin if data y sitio
	
	return tree;
}


/*
Property: getTreeData
	Return XML to charge a tree

Parameters:
	url - where to charge the data
	vars - array of variables

Returns:
	data en xml format
*/
function getTreeData(url,vars, async, idTabContainer, accion)
{
	var dev = undefined;
	
	if (async) {async = true;} else {async = false;}			
	
    new Request({
    	method: 'GET',
        url: url,
        noCache: true,
        async: async,
        onSuccess: function(text, xml){
            if (xml != null && xml.documentElement != null && xml.documentElement.childNodes != null && xml.documentElement.childNodes.length > 0){
        		dev = xml;
        		          		
        		if(idTabContainer && $(idTabContainer)){
            		$(idTabContainer).setStyle('display', '');
        		}
        		
        		if (accion){
        			accion(xml);
        		}
            } else{
		    	
		    	if (accion){
        			accion(undefined);
        		}
		    	
		    }
        },
        onFailure: function(response){
            //alert('ERROR, failure on asyncRequest ' + this.response.text);
        },
    	onTimeout: function(){
    		//alert('ERROR, timeout on asyncRequest ' + this.response.text);
    	}
    }).send(url, vars || '');
    
    return dev;
}

/*
Property: loadDataOnSelect
	Load the data parameter on the select with the id parameter

Parameters:
	idSelect - id of the select to load
	result - data to load
	includeIntialText - if include a text on index 0
	selected - element select after load
	errorText - error text to show on error (MUI.notification)
*/
function loadDataOnSelect(idSelect,result,includeIntialText,selected,errorText){
	var dev = false;
	var select = $(idSelect);
	if (select != null){
		
		var option;
		var selectedIndexSelect = 0;
		
		if (result && result.data && select){
			
			dev = true;
			
			if (includeIntialText){
				//Agrego un elemento base
				option = new Element('option',{});
                option.text = 'seleccione una opción';
                option.value = -1;
                
                try {
	            	select.add(option, null); // standards compliant; doesn't work in IE
	              }
	              catch(ex) {
	            	  select.add(option); // IE only
	              }
			}
			
			for(var rowidx=0,len=result.data.length; rowidx<len; rowidx++){
	            
	            var rowValue = result.data[rowidx];
	            
	            option = new Element('option',{});
	            option.text = rowValue.nombre;
	            option.value = rowValue.iden;
	            
	            if (rowValue.nemo){
	            	option.nemo = rowValue.nemo;
	            }
	            
	            try {
	            	select.add(option, null); // standards compliant; doesn't work in IE
	              }
	              catch(ex) {
	            	  select.add(option); // IE only
	              }
	            
	            //option.inject(select);    	                
			}
			
			for(var rowidx=0,len=select.options.length; rowidx<len; rowidx++){
	            
	            var rowValue = select.options[rowidx];
	            
	            if (selected && (selected == rowValue.value || selected == rowValue.text)){
	            	select.selectedIndex = rowidx;
	            	break;
            	}
			}
			
		} else {
			MUI.notification(errorText);
		}
	}
	return dev;
}

function addSplitPanel(sitio, modo, titlePrimerPanel, urlPrimerPanel, tamanoPrimerPanel, scrollbarsPrimerPanel, titleSegundoPanel, urlSegundoPanel, tamanoSegundoPanel, scrollbarsSegundoPanel, accion){

	dev = false;	
	
	var urlTabPrimerPanel = undefined;
	var urlTabSegundoPanel = undefined;
	
	if (!urlPrimerPanel){
		urlPrimerPanel = 'Pages/VisorPLANES/Tramites/FirstSplitPanel.html';
	} else {
		if (urlPrimerPanel.length && urlPrimerPanel.length == 2){
			urlTabPrimerPanel = urlPrimerPanel[1];
			urlPrimerPanel = urlPrimerPanel[0];
		}
	}
	if (!urlSegundoPanel){
		urlSegundoPanel = 'Pages/VisorPLANES/Tramites/SecondSplitPanel.html';
	} else {
		if (urlSegundoPanel.length && urlSegundoPanel.length == 2){
			urlTabSegundoPanel = urlSegundoPanel[1];
			urlSegundoPanel = urlSegundoPanel[0];
		}
	}
	
	switch (modo) {
	    case 'HORIZONTAL':
	    	
	    	new MUI.Column({
	        	container: sitio,
	            id: 'mainColumnSplitPanel'+sitio,
	            placement: 'main'
	        });
	    	
	    	new MUI.Panel({
	    		id: 'firstPanelSplitPanel'+sitio,
	    		contentURL: urlPrimerPanel,
	    		tabsURL: urlTabPrimerPanel,
	    		column: 'mainColumnSplitPanel'+sitio,
	    		scrollbars : scrollbarsPrimerPanel,
	    		title: titlePrimerPanel,
    			header: titlePrimerPanel || urlTabPrimerPanel,
    			collapsible: false,
    			onContentLoaded: function(){
					if ($('firstPanelSplitPanel'+sitio+'_pad')){
						$('firstPanelSplitPanel'+sitio+'_pad').setStyle('padding','2px');
						//agrego DIV de contenido
						var toInsert = new Element('div',{'id':'fspElement'+sitio,'class':'centerElement'});
						toInsert.setStyle('height','95%');
						toInsert.inject($('firstPanelSplitPanel'+sitio+'_pad'));							
					}
					
					//Obligo a ajustar el panel superior para poder agrandar el panel de datos superior
		    		var supPanelHeight = (parseInt($('mainColumnSplitPanel'+sitio).getStyle('height')) - (tamanoSegundoPanel + 4)) + 'px';
		    		$('firstPanelSplitPanel'+sitio).setStyle('height',supPanelHeight);
		    		
		    		if (titleSegundoPanel && tamanoSegundoPanel){
			    		tamanoSegundoPanel = tamanoSegundoPanel - 30;
			    	}
			    	
			    	new MUI.Panel({
			    		id: 'secondPanelSplitPanel'+sitio,
			    		contentURL: urlSegundoPanel,
			    		tabsURL: urlTabSegundoPanel,
			    		column: 'mainColumnSplitPanel'+sitio,
			    		height: tamanoSegundoPanel,
			    		scrollbars : scrollbarsSegundoPanel,
			    		title: titleSegundoPanel,
			    		header: titleSegundoPanel || urlTabSegundoPanel,
			    		collapsible: false,
						require: {		        	
							onload: function(){
								if ($('secondPanelSplitPanel'+sitio+'_pad')){
									$('secondPanelSplitPanel'+sitio+'_pad').setStyle('padding','2px');
									//agrego DIV de contenido
									var toInsert = new Element('div',{'id':'sspElement'+sitio,'class':'centerElement'});
									toInsert.setStyle('height','95%');
									toInsert.inject($('secondPanelSplitPanel'+sitio+'_pad'));
									
									setTimeout(accion,200);
								}
							}
				        }
			    	});
				}
	    	});  		    	
	    		
	    	dev = true;
	        break;
	    case 'VERTICAL': //*************************************************************************************************
	    	
	    	var anchoPrincipal = $(sitio).getStyle('width');
	    	
	    	new MUI.Column({
	        	container: sitio,
	            id: 'leftColumnSplitPanel'+sitio,
	            placement: 'left',
	            width: tamanoPrimerPanel,
	            resizeLimit: [tamanoPrimerPanel-100, parseInt(anchoPrincipal) - 40], //tamanoPrimerPanel+100],
	            sortable: false,
	            isCollapsed: false
	        });
	        
	        new MUI.Column({
	        	container: sitio,
	            id: 'mainColumnSplitPanel'+sitio,
	            placement: 'main',
	            resizeLimit: [tamanoPrimerPanel-100, parseInt(anchoPrincipal) - 40], //tamanoPrimerPanel+100],
	            sortable: false,
	            isCollapsed: false
	        });	
	        
	        new MUI.Panel({
	    		id: 'firstPanelSplitPanel'+sitio,
	    		contentURL: urlPrimerPanel,
	    		column: 'leftColumnSplitPanel'+sitio,
	    		collapsible: false,
	    		scrollbars : scrollbarsPrimerPanel,
	    		title: titlePrimerPanel,
    			header: titlePrimerPanel,
    			onContentLoaded: function(){
					if ($('firstPanelSplitPanel'+sitio+'_pad')){
						$('firstPanelSplitPanel'+sitio+'_pad').setStyle('padding','2px');
						//agrego DIV de contenido
						var toInsert = new Element('div',{'id':'fspElement'+sitio,'class':'centerElement'});
						toInsert.setStyle('height','95%');
						toInsert.inject($('firstPanelSplitPanel'+sitio+'_pad'));
					}
					
					new MUI.Panel({
			    		id: 'secondPanelSplitPanel'+sitio,
			    		contentURL: urlSegundoPanel,
			    		column: 'mainColumnSplitPanel'+sitio,
			    		height: tamanoSegundoPanel,
			    		scrollbars : scrollbarsSegundoPanel,
		    	        collapsible: false,
		    	        title: titleSegundoPanel,
			    		header: titleSegundoPanel,
						require: {		        	
							onload: function(){
								if ($('secondPanelSplitPanel'+sitio+'_pad')){
									$('secondPanelSplitPanel'+sitio+'_pad').setStyle('padding','2px');
									//agrego DIV de contenido
									var toInsert = new Element('div',{'id':'sspElement'+sitio,'class':'centerElement'});
									toInsert.setStyle('height','95%');
									toInsert.inject($('secondPanelSplitPanel'+sitio+'_pad'));
									
									setTimeout(accion,500);
								}
							}
				        }
			    	});
				}
	    	});
	    		    	
	    	dev = true;
	        break;    
	    default:
	        break;
	}		
	
	return dev;
}

function openGridDocument(info){
	var dataRow = info.target.getDataByRow(info.row);
    
    window.open('GestionConsola?wsName=GET_DOC_URL&idDoc='+dataRow.iden+'&idioma='+idioma,'_blank');
}

/*
 Property:  calculateTableHeight
 	Calcula el tama�o de una tabla en funci�n del numero de elemento que tenga. Si el tama�o es menor que el del panel donde se va a insertar, devuelve el mismo tama�o que el panel
*/
function calculateTableHeight(numElements, panelSize){

    var tableHeight = '0px';
	
    if (numElements > 0){
		tableHeight = (parseInt((numElements + 2) * 24)) + 'px';								
	}
	
	if (parseInt(panelSize) > parseInt(tableHeight)){ //si tras el calculo el tama�o del grid no llega a los bordes, lo igualo.
		tableHeight = panelSize;
	}
	
	return tableHeight;
}

/*
Property: getProgressLoadTabStatus
	Show progress gif on idTabProgress while the countVariable doesn't get the tabsNumber

Parameters:
	tabsNumber - numero de tabs que han de cargarse
	countVariable - variable que se va modificando a medida que se completa la carga de tabs
	idTabProgress - id del tab que muestra el gif de progreso
	initial - indica si es la primera llamada o no
	idDivTree - indica el id del div que contiene el arbol que hay que deshabilitar

Returns:
	
*/
function getProgressLoadTabStatus(tabsNumber, idTabProgress, type, initial, idDivTree){

	if (idTabProgress && $(idTabProgress)){
		
		if(initial){
			if (idDivTree && $(idDivTree)){ enableDisableTree($(idDivTree), false); }			
		}		
		$(idTabProgress).setStyle('display', 'block');
		
		(function(){
			
			var countVariable = 0;
			
			switch (type) {
				case 'plan':
					countVariable = countPlanesTabLoad;
					break;
				case 'tramite':
					countVariable = countTramitesTabLoad;
					break;
				case 'determinacion':
					countVariable = countTramitesDeterminacionesTabLoad;
					break;
				case 'entidad':
					countVariable = countTramitesEntidadesTabLoad;
					break;
			}
			
			if (countVariable < tabsNumber){
				window.setTimeout(function () {getProgressLoadTabStatus(tabsNumber, idTabProgress, type, false, idDivTree);}, 2000);
			}
			else{
				switch (type) {
				case 'plan':
					countVariable = countPlanesTabLoad;
					break;
				case 'tramite':
					countTramitesTabLoad = 0;
					break;
				case 'determinacion':
					countTramitesDeterminacionesTabLoad = 0;
					break;
				case 'entidad':
					countTramitesEntidadesTabLoad = 0;
					break;
				}			
				$(idTabProgress).setStyle('display', 'none');
				if (idDivTree && $(idDivTree)){ enableDisableTree($(idDivTree), true); }
				MUI.rWidth();
			}
			
			}).delay(20, this);				
	}
}
