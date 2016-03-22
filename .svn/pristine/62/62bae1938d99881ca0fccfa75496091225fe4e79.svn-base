var visorPlanBaseTree; //Arbol del visor del plan base
var nodeSelectedPlanBase = null; //tipo de elemento seleccionad (determinacion, info-determinacio, entidad o info-entidad)

//padres, hijos, etc... Datos sobre el elementos seleccionado
var idDetPadrePlanBase = null;
var idEntPadrePlanBase = null;
var idTramitePlanBase = null;

function initPlanBase(){
    
    //var miPlanBase = new FormPlanBase();
	
	visorPlanBaseTree = new MooTreeControl({
        div: 'treePlanBase',
        mode: 'folders',
        treeType: 'planes',
        iconBar: ['javascriptsV2/mooTree2/Planes.gif'],
        grid: true,
        onSelect: function(node, state) {
            if (state) {
                cargarInfoDeNodoPlanBase(node);
            }
        }
    	},{
        text: cargarTextoSegunIdioma('Planes'),
        open: true            
    });      
    
    $('treePlanBase').setStyle('height','95%'); //parseInt($('LeftSideColumn').getStyle('height'))  - 14 + 'px');
    $('treePlanBase').setStyle('width','95%'); //parseInt($('LeftSideColumn').getStyle('width')) - 9 + 'px');

    //Cargo los datos del arbol
    visorPlanBaseTree.root.load('GestionConsola?wsName=GET_NODOS_PLAN_BASE&load=true&idioma='+idioma);
    
    //Agrego eventos a botones
    $('BtnPlanBaseAddSubElement').addEvent('click', onBtnNewPlanBase);
    $('BtnPlanBaseRemoveElement').addEvent('click', onBtnRemovePlanBase);
    $('BtnPlanBaseSaveElement').addEvent('click', onBtnSavePlanBase);
    
    //Deshabilito los botones sobre elementos
    disableControl('BtnPlanBaseAddSubElement');
    disableControl('BtnPlanBaseRemoveElement');
    disableControl('BtnPlanBaseSaveElement');
    
    $('LeftSideColumn').fade(1);
    $('mainColumn').fade(1);	
}

//Al pulsar sobre un nodo se lanza esta funcion
function cargarInfoDeNodoPlanBase(node)
{   
    nodeSelectedPlanBase = node.data.tipo;
	
    switch(node.data.tipo)
    {
        case "determinacion": //Se muestra el formulario y los botones nueva, borrar y guardar
        	enableControl('BtnPlanBaseAddSubElement');
        	enableControl('BtnPlanBaseRemoveElement');
            enableControl('BtnPlanBaseSaveElement');
            
            idDetPadrePlanBase = node.data.idNode;
            idEntPadrePlanBase = null;
            idTramitePlanBase = node.data.idNode;
            
            // Get idTramite
            var result = runService({
                wsName: "GET_TRAMID_DE_DET",
                idDet: idDetPadrePlanBase
            });
            if (isNumeric(result)){
                idTramitePlanBase = result;
            }
            
            //Cargo el panel de determinacion
            MostrarFormularioDeterminacion();
            break;
        case "info-determinacion": //Solo se habilita el boton de anadir hija
        	enableControl('BtnPlanBaseAddSubElement');
        	disableControl('BtnPlanBaseRemoveElement');
            disableControl('BtnPlanBaseSaveElement');
            
            idDetPadrePlanBase = null;
            idEntPadrePlanBase = null;
            idTramitePlanBase = node.data.idNode;
            
            MostrarFormularioVacio();
            break;
        case "entidad": //Se muestra el formulario y los botones nueva, borrar y guardar
        	enableControl('BtnPlanBaseAddSubElement');
        	enableControl('BtnPlanBaseRemoveElement');
            enableControl('BtnPlanBaseSaveElement');
            
            idDetPadrePlanBase = null;
            idEntPadrePlanBase = node.data.idNode;
            idTramitePlanBase = node.data.idNode;
            
            // Get idTramite
            var result = runService({
                wsName: "GET_TRAMID_DE_ENT",
                idEnt: idEntPadrePlanBase
            });
            if (isNumeric(result)){
            	idTramitePlanBase = result;
            }
            
            //Cargo el panel de entidad
            MostrarFormularioEntidad();
            
            break;
        case "info-entidad": //Solo se habilita el boton de anadir hija
        	enableControl('BtnPlanBaseAddSubElement');
        	disableControl('BtnPlanBaseRemoveElement');
            disableControl('BtnPlanBaseSaveElement');
        	
        	idDetPadrePlanBase = null;
            idEntPadrePlanBase = null;
            idTramitePlanBase = node.data.idNode;
            
            MostrarFormularioVacio();
            break;
        default:        	        
	    	//Deshabilito los botones sobre elementos
	    	disableControl('BtnPlanBaseAddSubElement');
	        disableControl('BtnPlanBaseRemoveElement');
	        disableControl('BtnPlanBaseSaveElement');
	        
	        nodeSelectedPlanBase = null;
	        
	        MostrarFormularioVacio();
        	break;
    }               
}

function MostrarFormularioVacio(){
	MUI.updateContent({
		element: $('main-panel'),
		url: 'Pages/PlanBase/DataViewerFormEmptyPlanBase.html',
		//header: false,
		padding: { top: 8, right: 8, bottom: 8, left: 8 },
	});
}

function MostrarFormularioDeterminacion(){
	MUI.updateContent({
		element: $('main-panel'),
		url: 'Pages/PlanBase/DataViewerFormDeterminacionPlanBase.html',
		//header: false,
		padding: { top: 8, right: 8, bottom: 8, left: 8 },
		require: {
			onload: function(){
				
				//Relleno los datos de la determinacion
				runServiceAsyncJson({'wsName' : 'GET_DATA','ID' : idDetPadrePlanBase,'TIPO' : 'determinacion'},
					function(response){
					if (response != null && response.data != null && response.data.length > 0){										
						
						//ocultos
						$('idDet').value = response.data[0]['iden'];
						$('idTramite').value = idTramitePlanBase;
						$('idDetPadre').value = response.data[0]['detPadre'];
						
						$('txtNombre').value = response.data[0]['nombre'];
						$('txtCodigo').value = response.data[0]['codigo'];
						$('txtApartado').value = response.data[0]['apartado'];
						$('txtEtiqueta').value = response.data[0]['etiqueta'];
						$('txtTexto').set('html',response.data[0]['texto']);
						
						//Relleno el select de caracteres
						var selectCaracteres = $('idCaracter');
						var caracterDelElemento = response.data[0]['idCaracter'];
						var optionCaracteres;
						var selectedIndexSelect = -1;
						var result = runServiceJson({wsName: "GET_DATA", TIPO: "CaracterDeterminacion"});
						
						if (result && result.data && selectCaracteres){										
							
							for(var rowidx=0,len=result.data.length; rowidx<len; rowidx++){
		    	                
		    	                var rowValue = result.data[rowidx];
		    	                
		    	                if (caracterDelElemento == rowValue.iden){
		    	                	selectedIndexSelect = rowidx;		    	                	
		    	                }
		    	                
		    	                optionCaracteres = new Element('option',{});
		    	                optionCaracteres.text = rowValue.nombre;
		    	                optionCaracteres.value = rowValue.iden;
		    	                
		    	                try {
		    	                	selectCaracteres.add(optionCaracteres, null); // standards compliant; doesn't work in IE
		    	                  }
		    	                  catch(ex) {
		    	                	  selectCaracteres.add(optionCaracteres); // IE only
		    	                  }
		    	                
		    	                //optionCaracteres.inject(selectCaracteres);    	                
							}
							
							selectCaracteres.selectedIndex = selectedIndexSelect;
							
						} else {
							MUI.notification('No ha sido posible cargar la lista de caracteres');							
				            disableControl('BtnPlanBaseSaveElement');
						}
				    	
				    }else{
				    	MUI.notification('No ha sido posible obtener los datos de la determinaci&oacute;n');
				    }
				});
			}	
		}										
	});
}

function MostrarFormularioEntidad(){
	MUI.updateContent({
		element: $('main-panel'),
		url: 'Pages/PlanBase/DataViewerFormEntidadPlanBase.html',
		//header: false,
		padding: { top: 8, right: 8, bottom: 8, left: 8 },
		require: {
			onload: function(){
				
				//Relleno los datos de la entidad
				runServiceAsyncJson({'wsName' : 'GET_DATA','ID' : idEntPadrePlanBase,'TIPO' : 'entidad'},
					function(response){
					if (response != null && response.data != null && response.data.length > 0){										
						
						//ocultos
						$('idEnt').value = idEntPadrePlanBase;
						$('idTramite').value = idTramitePlanBase;
						$('idEntPadre').value = response.data[0]['entPadre'];
						
						$('txtNombre').value = response.data[0]['nombre'];
						$('txtClave').value = response.data[0]['clave'];
						$('txtCodigo').value = response.data[0]['codigo'];						
						$('txtEtiqueta').value = response.data[0]['etiqueta'];												
				    	
				    }else{
				    	MUI.notification('No ha sido posible obtener los datos de la entidad');
				    }
				});
				
			}	
		}										
	});
}

function onBtnNewPlanBase(){
	//Depende del tipo de elemento seleccionado (determinacion o entidad) se actua
	if ($('BtnPlanBaseAddSubElement').disabled == false && nodeSelectedPlanBase){
		
		var urlPage = undefined;
		
		switch(nodeSelectedPlanBase){
			case "determinacion":
			case "info-determinacion":
				urlPage= 'Pages/PlanBase/DataViewerFormDeterminacionPlanBase.html';								        
				break;
			case "entidad":
			case "info-entidad":
				urlPage= 'Pages/PlanBase/DataViewerFormEntidadPlanBase.html';
				break;
		}
		
		if (urlPage){
			
			visorPlanBaseTree.selected.toggle(false, true);
			
			MUI.updateContent({
				element: $('main-panel'),
				url: urlPage,
				//header: false,
				padding: { top: 8, right: 8, bottom: 8, left: 8 },
				require: {
					onload: function(){
						
						switch(nodeSelectedPlanBase){
						case "determinacion":
						case "info-determinacion":
							
							$('idTramite').value = idTramitePlanBase;
			                $('idDetPadre').value = idDetPadrePlanBase;
							
							//Relleno el select de caracteres
							var selectCaracteres = $('idCaracter');
							var caracterDelElemento = response.data[0]['idCaracter'];
							var optionCaracteres;
							var selectedIndexSelect = -1;
							var result = runServiceJson({wsName: "GET_DATA", TIPO: "CaracterDeterminacion"});
							
							if (result && result.data && selectCaracteres){										
								
								for(var rowidx=0,len=result.data.length; rowidx<len; rowidx++){
			    	                
			    	                var rowValue = result.data[rowidx];
			    	                
			    	                if (caracterDelElemento == rowValue.iden){
			    	                	selectedIndexSelect = rowidx;		    	                	
			    	                }
			    	                
			    	                optionCaracteres = new Element('option',{});
			    	                optionCaracteres.text = rowValue.nombre;
			    	                optionCaracteres.value = rowValue.iden;
			    	                
			    	                try {
			    	                	selectCaracteres.add(optionCaracteres, null); // standards compliant; doesn't work in IE
			    	                  }
			    	                  catch(ex) {
			    	                	  selectCaracteres.add(optionCaracteres); // IE only
			    	                  }
			    	                
			    	                //optionCaracteres.inject(selectCaracteres);    	                
								}
								
								selectCaracteres.selectedIndex = selectedIndexSelect;
								
							} else {
								MUI.notification('No ha sido posible cargar la lista de caracteres');
								disableControl('BtnPlanBaseSaveElement');
							}
							break;
						case "entidad":
						case "info-entidad":
							
							$('idTramite').value = idTramitePlanBase;
			                $('idEntPadre').value = idEntPadrePlanBase;
							break;
						}																								
					}	
				}										
			});
		}				
	}
}

function onBtnRemovePlanBase(){
	//Depende del tipo de elemento seleccionado (determinacion o entidad) se actua
	if ($('BtnPlanBaseRemoveElement').disabled == false && nodeSelectedPlanBase){
		switch(nodeSelectedPlanBase){
		case "determinacion":
		case "info-determinacion":
			deleteDeterminacionPlanBase();
			break;
		case "entidad":
		case "info-entidad":
			deleteEntidadPlanBase();
			break;
		}
	}
}

function deleteDeterminacionPlanBase(){
	if (!confirm('Se borrará la determinación. ¿Desa continuar?')){
        return;
    }

    var req = new Request.JSON({
        url: 'GestionConsola',
        data: {
            idioma: idioma,
            idDet: $('idDet').value,
            wsName: 'BORRAR_DET_PLAN_BASE'
        },
        onSuccess: function(resJson, resText){

        	if (resJson.error){
            	alert('Error borrando la determinación:\n'+resJson.error);
            } else {
            	MUI.notification('Determinaci&oacute;n borrada correctamente');
            	
            	var currNode = visorPlanBaseTree.selected;
                this._decrementarNumHijosNodoArbol(currNode.parent);
                if (currNode) {
                    currNode.remove();
                }
                
                disableControl('BtnPlanBaseAddSubElement');
    	        disableControl('BtnPlanBaseRemoveElement');
    	        disableControl('BtnPlanBaseSaveElement');
    	        
    	        nodeSelectedPlanBase = null;
    	        
    	        MostrarFormularioVacio();
            }       
           
        }.bind(this),
        onFailure: function(){
        	alert('Error borrando los datos de la determinación');
        },
        onCancel: function(){
        	alert('Petición cancelada');
        }
    });

    req.send();
}

function deleteEntidadPlanBase(){
	if (!confirm("Se borrará la entidad. ¿Desa continuar?")){
        return;
    }

	var req = new Request.JSON({
        url: 'GestionConsola',
        data: {
            idioma: this.options.idioma,
            idEnt: $('idEnt').value,
            wsName: 'BORRAR_ENT_PLAN_BASE'
        },
        onSuccess: function(resJson, resText){

        	if (resJson.error){
            	alert('Error borrando la entidad:\n'+resJson.error);
            } else {
            	MUI.notification('Entidad borrada correctamente');
            	
            	var currNode = visorPlanBaseTree.selected;
                this._decrementarNumHijosNodoArbol(currNode.parent);
                if (currNode) {
                    currNode.remove();
                }
                
                disableControl('BtnPlanBaseAddSubElement');
    	        disableControl('BtnPlanBaseRemoveElement');
    	        disableControl('BtnPlanBaseSaveElement');
    	        
    	        nodeSelectedPlanBase = null;
    	        
    	        MostrarFormularioVacio();
            }        	
         
        }.bind(this),
        onFailure: function(){
        	alert('Error borrando los datos de la entidad');
        },
        onCancel: function(){
        	alert('Petici�n cancelada');
        }
    });

    req.send();
}

function onBtnSavePlanBase(){
	//Depende del tipo de elemento seleccionado (determinacion o entidad) se actua
	if ($('BtnPlanBaseSaveElement').disabled == false && nodeSelectedPlanBase){
		switch(nodeSelectedPlanBase){
		case "determinacion":
		case "info-determinacion":
			saveDeterminacionPlanBase();
			break;
		case "entidad":
		case "info-entidad":
			saveEntidadPlanBase();
			break;
		}
	}
}

function saveDeterminacionPlanBase(){
	var req = new Request.JSON({
        url: 'GestionConsola',
        data: {
            idioma: this.options.idioma,
            idDet: $('idDet').value,
            wsName: 'GUARDAR_DET_PLAN_BASE',
            idDetPadre: $('idDetPadre').value,
            idTramite: $('idTramite').value,
            txtNombre: $('txtNombre').value,
            txtCodigo: $('txtCodigo').value,
            txtApartado: $('txtApartado').value,
            txtEtiqueta: $('txtEtiqueta').value,
            txtTexto: $('txtTexto').value,
            idCaracter: $('idCaracter').options[$('idCaracter').selectedIndex].value
        },
        onSuccess: function(res){
        	
        	if (res.error){
            	alert('Error guardando los datos de la determinaci&oacute;n:\n'+res.error);
            } else {
            	MUI.notification('Datos guardados correctamente');
            	           	
                if ($('idDet').value == null || $('idDet').value == ''){
                    //SE ESTA CREANDO UNA DETERMINACION NUEVA

                    // a�adir el nodo en el �rbol a partir de los datos del formulario
                    var currNode = visorPlanBaseTree.selected;
                    if (currNode){
                        var entNode = currNode.insert({
                            data: {
                                idNode: res.affectedId,
                                tipo: 'determinacion'
                            },
                            text: $('txtApartado').value+' '+ $('txtNombre').value,
                            open: false
                        });
                        
                        visorPlanBaseTree.select(entNode);
                        currNode.update(true, true);
                        _incrementarNumHijosNodoArbol(currNode);
                    }                    
                } else {
                    //SE ESTA MODIFICANDO UNA DETERMINACION EXISTENTE
                    // modificar el nombre de la determinaci�n en el �rbol para que refleje los cambios
                    if (visorPlanBaseTree.selected){
                    	visorPlanBaseTree.selected.text =$('txtApartado').value+'.'+ $('txtNombre').value;
                    	visorPlanBaseTree.selected.update();
                    }
                }
            }
            
        }.bind(this),
        onFailure: function(){
        	alert('Error guardando los datos de la determinaci&oacute;n');
        },
        onCancel: function(){
        	alert('Petici�n cancelada');
        }
    });

    req.send();
}

function saveEntidadPlanBase(){
	
	var req = new Request.JSON({
        url: 'GestionConsola',
        data: {
            idioma: this.options.idioma,
            idEnt: $('idEnt').value,
            wsName: 'GUARDAR_ENT_PLAN_BASE',
            idEntPadre: $('idEntPadre').value,
            idTramite: $('idTramite').value,
            txtNombre: $('txtNombre').value,
            txtCodigo: $('txtCodigo').value,
            txtEtiqueta: $('txtEtiqueta').value,
            txtClave: $('txtClave').value
        },
        onSuccess: function(res){

            if (res.error){
            	alert('Error guardando los datos de la entidad:\n'+res.error);
            } else {
            	MUI.notification('Datos guardados correctamente');
            	            	               
                if ($('idEnt').value == null || $('idEnt').value == ''){
                    //SE ESTA CREANDO UNA ENTIDAD NUEVA

                    // anadir el nodo en el arbol a partir de los datos del formulario
                    var currNode = visorPlanBaseTree.selected;
                    if (currNode){
                        var entNode = currNode.insert({
                            data: {
                                idNode: res.affectedId,
                                tipo: 'entidad'
                            },
                            text: $('txtClave').value + ' ' + $('txtNombre').value,
                            open: false
                        });
                        
                        visorPlanBaseTree.select(entNode);
                        currNode.update(true, true);
                        _incrementarNumHijosNodoArbol(currNode);
                    }                                        
                } else {
                    //SE ESTA MODIFICANDO UNA ENTIDAD EXISTENTE
                    // modificar el nombre de la entidad en el arbol para que refleje los cambios
                    if (visorPlanBaseTree.selected){
                    	visorPlanBaseTree.selected.text =$('txtClave').value + ' - ' + $('txtNombre').value;
                    	visorPlanBaseTree.selected.update();
                    }
                }
            }                                    
        }.bind(this),
        onFailure: function(){
            alert('Error guardando los datos de la entidad');
        },
        onCancel: function(){           
            alert('Petici&oacute;n cancelada');
        }
    });

    req.send();
}
