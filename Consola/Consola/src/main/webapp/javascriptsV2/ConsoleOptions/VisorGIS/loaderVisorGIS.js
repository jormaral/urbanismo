var treeManagerConfiguradorVisor;
var idAmbitoCurrentlySelected = -1;

function initVisorGIS(){
	
	//Muestro los ambitos que el usuario puede seleccionar
	response = runServiceJson({'wsName' : 'GET_AMBITOS_CONFIGURACION'});	
	
	if (response != null && response.data != null && response.data.length > 0){
				
		//deshabilito todos los botones
		//disableControl('configuradorVisorBtnAddLayerGoogle');
		//disableControl('configuradorVisorBtnAddLayerKml');
		disableControl('configuradorVisorBtnAddLayerWms');
		//disableControl('configuradorVisorBtnAddLayerWfs');
		disableControl('configuradorVisorBtnDeleteLayer');
		disableControl('configuradorVisorBtnAddFolder');
		disableControl('configuradorVisorBtnSaveChanges');
		disableControl('configuradorVisorBtnSaveElementChanges');
		disableControl('configuradorVisorBtnCancelElementChanges');
		
		//Agrego eventos para cada boton fijo de la botonera del arbol
		$$('#botoneraAccionesConfiguradorVisor img').each(function(n){
	        n.addEvent('click', function(boton){
	        	
	        	if (treeManagerConfiguradorVisor){
	        		switch (boton.target.get('id')) {		
		            	case 'configuradorVisorBtnAddLayerGoogle':
		            		if ($('configuradorVisorBtnAddLayerGoogle').disabled == false){
		            			enableControl('configuradorVisorBtnSaveElementChanges');
		            			enableControl('configuradorVisorBtnCancelElementChanges');
		            			treeManagerConfiguradorVisor.generarVentana('GOOGLE',treeManagerConfiguradorVisor);
		            		}
		            		break;
		            	case 'configuradorVisorBtnAddLayerKml':
		            		if ($('configuradorVisorBtnAddLayerKml').disabled == false){
		            			enableControl('configuradorVisorBtnSaveElementChanges');
		            			enableControl('configuradorVisorBtnCancelElementChanges');
		            			treeManagerConfiguradorVisor.generarVentana('KML',treeManagerConfiguradorVisor);
		            		}
		            		break;
		            	case 'configuradorVisorBtnAddLayerWms':
		            		if ($('configuradorVisorBtnAddLayerWms').disabled == false){
		            			enableControl('configuradorVisorBtnSaveElementChanges');
		            			enableControl('configuradorVisorBtnCancelElementChanges');
		            			treeManagerConfiguradorVisor.generarVentana('WMS',treeManagerConfiguradorVisor);
		            		}
		            		break;
		            	case 'configuradorVisorBtnAddLayerWfs':
		            		if ($('configuradorVisorBtnAddLayerWfs').disabled == false){	
		            			enableControl('configuradorVisorBtnSaveElementChanges');
		            			enableControl('configuradorVisorBtnCancelElementChanges');
		            			treeManagerConfiguradorVisor.generarVentana('WFS',treeManagerConfiguradorVisor);
		            		}
		            		break;
		            	case 'configuradorVisorBtnDeleteLayer':
		            		if ($('configuradorVisorBtnDeleteLayer').disabled == false){
		            			treeManagerConfiguradorVisor.borrarCapa(null);
		            		}
		            		//Borra una carpeta del arbol
		            		break;
		            	case 'configuradorVisorBtnAddFolder':
		            		if ($('configuradorVisorBtnAddFolder').disabled == false){
		            			enableControl('configuradorVisorBtnSaveElementChanges');
		            			enableControl('configuradorVisorBtnCancelElementChanges');
		            			treeManagerConfiguradorVisor.generarVentana('FOLDER',treeManagerConfiguradorVisor);
		            		}
		            		//anado una nueva carpeta al arbol
		            		break;
		            	case 'configuradorVisorBtnSaveChanges':		   
		            		if ($('configuradorVisorBtnSaveChanges').disabled == false){
		            			treeManagerConfiguradorVisor.saveArbolCapas();
		            		}
		            		//guardo los datos de los cambios sobre el arbol
		            		break;
	        		}
	        	}	        	
	        });
	    });	
		
		//Agrego el evento de cancelar la accion actual
		$('configuradorVisorBtnCancelElementChanges').addEvent('click', function(boton){
			if ($('configuradorVisorBtnCancelElementChanges').disabled == false){
				treeManagerConfiguradorVisor.cancelarVentana();
    		}
		});			
		
		var select = $('configuradorVisorSelectAmbitos');
		var option;
		if (response && response.data && select){
			
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
			
            //Agrego el resto de elementos
			for(var rowidx=0,len=response.data.length; rowidx<len; rowidx++){
                
                var rowValue = response.data[rowidx];
                
                option = new Element('option',{});
                option.text = rowValue.nombre;
                option.value = rowValue.idAmbito;
                
                try {
                	select.add(option, null); // standards compliant; doesn't work in IE
                  }
                  catch(ex) {
                	  select.add(option); // IE only
                  }
                
                //option.inject(select);    	                
			}
			
		} else {
			MUI.notification('No ha sido posible cargar la lista de tipos de tr&aacute;mites');
		}
		
		select.addEvent('change',function(e){
			 var idSelected = $('configuradorVisorSelectAmbitos').options[$('configuradorVisorSelectAmbitos').selectedIndex].value;
			 
			 if (idAmbitoCurrentlySelected != idSelected){
				 
				 idAmbitoCurrentlySelected = idSelected;
			 
				 $('treeConfiguradorVisor').empty();
				 
				 if (idSelected >=0){
		     		//Cargo el manejador de los eventos del arbol que tambien carga el arbol
		     		treeManagerConfiguradorVisor = new Configurador(idSelected,['GOOGLE','WMS','WFS','KML'],'treeConfiguradorVisor');
		     		
		     		//Dejo deshabilitado todo hasta que se seleccione un nodo del arbol
		     		//disableControl('configuradorVisorBtnAddLayerGoogle');
		     		//disableControl('configuradorVisorBtnAddLayerKml');
		     		disableControl('configuradorVisorBtnAddLayerWms');
		     		//disableControl('configuradorVisorBtnAddLayerWfs');
		     		disableControl('configuradorVisorBtnDeleteLayer');
		     		disableControl('configuradorVisorBtnAddFolder');
		     		disableControl('configuradorVisorBtnSaveChanges');
				 }	
			}
		});				
		
		/* /Si solo hay una se carga directamente
		if (response.data.length == 1){
			$('configuradorVisorBtn'+response.data[0]['idAmbito']).style.display = "none";
			$('treeConfiguradorVisor').empty();
			//Cargo el manejador de los eventos del arbol que tambien carga el arbol
    		treeManagerConfiguradorVisor = new Configurador(response.data[0]['idAmbito'],['GOOGLE','WMS','WFS','KML'],'treeConfiguradorVisor');
		}*/
				
		$('LeftSideColumn').fade(1);
		$('mainColumn').fade(1);
		
	} else {
		 MUI.notification('No tiene permisos de configuraci&oacute;n sobre ning&uacute;n &aacute;mbito');
	} 		
}