function initConsolidador()
{	
	if ($('numProcesos').value == '0'){
		$('tituloConsolidador').set('html','<h2 class="title-panel-header">No se encuentran tr&aacute;mites pendientes de consolidar</h2>');
		$('tablaConsolidacion').setStyle('display','none');
	}
	else {
		//$("btnRConsolidar").addEvent("click",onbtnRConsolidarClick);
	}
	
	$('mainColumn').fade(1);
}


// Selecciona el trámite para consolidar
function onbtnRConsolidarClick(idProceso, idTramite){
	
	if (idTramite){
		if(confirm("¿Consolidar el trámite: " + idTramite + "?")){
           var respuesta = runService({
                'wsName':"CONSOLIDAR_TRAMITES",
                "idTramite":idProceso
            });
           if(respuesta == "OK"){
        	   disableControl("btnRConsolidar-"+idProceso);
        	   new PopupMsg("Proceso de consolidación finalizado correctamente",
               {
                 type: 'ok',
                 closable: true
               });                 
            } else {
            	new PopupMsg("Proceso de consolidación finalizado con errores",
                {
            		type: 'ok',
                    closable: true
                });
            }           
        }
	}
}
