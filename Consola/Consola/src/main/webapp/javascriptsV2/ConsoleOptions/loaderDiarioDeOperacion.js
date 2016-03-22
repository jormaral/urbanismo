var infoSelectedDiarioOperaciones;

function initDiarioDeOperacion(){
    try{ 
    	//Oculto los elementos
        $("txtImput").setStyle('display','none');
        $("dateImputWrapper").setStyle('display','none');
        $("searchButtonDiaOp").setStyle('display','none');    
        
        $$('input').each(function(input){
			if (input.get('data-meiomask') != null){
				input.meiomask(input.get('data-meiomask'));
			}					
		});
    	
    	//Events
    	$("searchButtonDiaOp").addEvent('click', onSearch);
        $("campo").addEvent('change', addOption);
        
        //Setup calendar (Esto crea un button calendar)
        new Calendar({ dateImput: 'd/m/Y' }, { classes: ['dashboard'],
        	direction: -1,
            navigation: 2,
            offset: 1,
            days: ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'], 
            months: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'] 
        });
        
        $("buttonCalendar").setStyles({
            'position':'relative',
            'top':(Browser.Engine.gecko?'-4px': (Browser.Engine.webkit?'-2px':(Browser.Engine.trident?(Browser.Engine.version==6?'5px':'-1px'):'')))
        });
        
        //Por defecto esta desactivado
        //disableControl('btnDiarioOperacionesDescargar');
        //$('btnDiarioOperacionesDescargar').addEvent('click', showLogFromDiario);
        
        $('mainColumn').fade(1);
    }catch(exception){
        alert("Problema al cargar Diario De Operacion: "+exception);
    }
}

//Muestra o oculta elementos segun el tipo de busqueda seleccionada
function addOption(){

	var selectedCampo = $("campo").getSelected().get('text')[0];
	
	switch(selectedCampo)
	{
		case "Usuario":
			$("dateImputWrapper").setStyle('display','none');	        
	        
	        var users = runServiceJson({'wsName':'GET_LISTA_USUARIOS'});
	        
	        if(!users){
	            alert("Error al obtener la lista de usuarios del sistema");
	        }
	        else{
	        	$('txtImput').empty(); //elimino lo que hubiera
	        	
	        	for(var i=0, length=users.data.length; i<length; i++){
	                var opelem = new Element('option',{
	                    value: users.data[i].idUsuario,
	                    html: users.data[i].nombreUsuario
	                });
	                $("txtImput").grab(opelem);
	            }
	        	
	        	$("txtImput").setStyle('display','inline');
	        	$("searchButtonDiaOp").setStyle('display','inline');
	        }
		  break;
		case "Subsistema":
			$("dateImputWrapper").setStyle('display','none');
			
	        var subs = runServiceJson({'wsName':'GET_LISTA_SUBSISTEMAS'});
	        
	        if(!subs){
	            alert("Error al obtener la lista de subsistemas");
	        }        
	        else{
	        	$('txtImput').empty(); //elimino lo que hubiera
	        	
	        	for(var i=0, length=subs.data.length; i<length; i++){
	                var opelem = new Element('option',{
	                    value: subs.data[i].iden,
	                    html: subs.data[i].nombre
	                });
	                $("txtImput").grab(opelem);
	            }
	        	
	        	$("txtImput").setStyle('display','inline');
	        	$("searchButtonDiaOp").setStyle('display','inline');
	        }
			break;
		case "Fecha":			
	        $("txtImput").setStyle('display','none');
	        
	        $("dateImput").set('text',"");
	        
	        $("dateImputWrapper").setStyle('display','inline');
	        $("searchButtonDiaOp").setStyle('display','inline');
			break;
		default:
			$("txtImput").setStyle('display','none');
	    	$("dateImputWrapper").setStyle('display','none');
	    	$("searchButtonDiaOp").setStyle('display','none');
		  break;
	}		
}

//Realiza la busqueda de elementos
function onSearch(){
	
	var selectedCampo = $("campo").getSelected().get('text')[0];
	var valor;
	
	switch(selectedCampo)
	{
		case "Usuario":
		case "Subsistema":
			valor = $('txtImput').getSelected().get('value')[0];
		  break;
		case "Fecha":
			var fechaTag = $("dateImput");
	        if (fechaTag.get('value') ==""){
	        	alert("Debe seleccionar una fecha de búsqueda");	        	
	        } else {	        	
	        	valor = fechaTag.get('value');
	        }	        
			break;
		default:
			alert("Debe seleccionar un criterio de busqueda");
		  break;
	}
	   
  //Genero el grid    
    grid = new omniGrid('panelResultadosDiaOp',{
        url:'GestionConsola?wsName=GET_DIA_OP&valor='+valor+'&idioma=es'+'&campo='+selectedCampo,
        columnModel: operationDailyGridColumns,
        pagination:true,
        page:1,
        perPage:20,
        serverSort:false,
        showHeader: true,
        alternaterows: true,
        sortHeader:true,
        resizeColumns:true,
        multipleSelection:false,
        width: '100%',
        height: parseInt(parseInt($('mainColumn').getStyle('height')) - parseInt($('menuBusquedaDiaOp').getStyle('height')) - 30)
    });

    //Manejamos el evento del doble click para iniciar la descarga del log si existe
    if (grid){
        grid.addEvent('dblclick', checkThereIsDocuments);
    }
}

function checkThereIsDocuments(info){
	var dataRow = info.target.getDataByRow(info.row);
    
    //Si hay documentos genera un formulario para descargarlos
	//TODO ¿Tengo que preguntar por que parametro?
    if(dataRow.documentos > 0){    	
    	enableControl('btnDiarioOperacionesDescargar');
    	infoSelectedDiarioOperaciones = info;
    } else {
    	disableControl('btnDiarioOperacionesDescargar');
    	infoSelectedDiarioOperaciones = null;
    }
}

//Al pulsar sobre el grid se muestra el log from diario
function showLogFromDiario(){
	if (infoSelectedDiarioOperaciones){
		//lanzo la descarga con los datos de la fila seleccionada.
	}
	/*
    var dataRow = info.target.getDataByRow(info.row);
    var idDiario = dataRow.iden;
    
    //Si hay documentos genera un formulario para descargarlos
    if(dataRow.documentos > 0){
        
    	var data = runService({
            wsName: 'GET_TYPE_FILES',
            idDiario: idDiario
        });
    	
        var files = JSON.decode(data);
        
        var divWindow = new Element('div',{
            id:'divWindow'
        });
        
        var h3 = new Element('h3',{
            html: 'Seleccione el documento para descargar'
        });
        
        divWindow.grab(h3);
        for(var i = 0,length = files.length; i<length; i++){
            divWindow.grab(new Element('br'));
            var divFile = Element('div',{
                html: "Descarga el archivo: "+files[i],
                id: "file"+files[i],
                'class': 'divlink'
            });
            divFile.addEvent('click',function(){
                window.open('GestionConsola?wsName=DOWNLOAD_FILE&idDiario='+this.diario+'&type='+this.type);
            }.bind({
                diario:idDiario,
                type:files[i]
            }));
            divWindow.grab(divFile);
        }
        
        SqueezeBox.initialize({
            size: {
                x: 300,
                y: 250
            }
        });
        
        SqueezeBox.open(divWindow, {
            handler: 'adopt'
        });       
    }*/
}