<!-- 
    Document   : WMS
    Created on : 01-nov-2011, 17:00:00
    Author     : Alvaro.Montero
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
<fieldset>
    <legend id="legend">Nueva capa WMS</legend>
    <br/>
    
    <div class="formRow">
	<div id="lblText" class="formLabel">Nombre*:</div>
	<div class="formField"><input type="text" class="input" id="WMSinputText" value="" maxlength="50" style="width: 100%" /></div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblUrl" class="formLabel">Url*:</div>
	<div class="formField"> <!-- http://www1.sedecatastro.gob.es/Cartografia/WMS/ServidorWMS.aspx -->
		<input type="text" class="input" id="WMSinputUrl" value="" maxlength="200" style="width: 95%" />
		<div id="WMSbotonUrl" title="Introduce una dirección y pulse para cargar las capas" class="botonUrl"></div>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblLayers" class="formLabel">Capas*:</div>
	<div class="formField">
        <select id="WMSinputLayers" style="width: 100%" size="1" multiple="multiple">
            <option value="">No existen capas</option>
       	</select>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblTransparent" class="formLabel">Transparente:</div>
	<div class="formField">	
		&nbsp;<div id="lblTransparentSi" class="radioLabel">S&iacute;</div> &nbsp;
        <input id="WMSselectTransparentSi" type="radio" name="WMSTransparent" class="inputRadio" checked/>
        &nbsp;<div id="lblTransparentNo" class="radioLabel">No</div> &nbsp;
        <input id="WMSselectTransparentNo" type="radio" name="WMSTransparent" class="inputRadio"/>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblFormat" class="formLabel">Formato*:</div>
	<div class="formField">
        <select id="WMSinputFormat" style="width: 100%">
            <option value="">No existen formatos</option>
       	</select>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblSingleTile" class="formLabel">Entilado:</div>
	<div class="formField">	
		&nbsp;<div id="lblSingleTileSi" class="radioLabel">S&iacute;</div> &nbsp;
        <input id="WMSselectSingleTileSi" type="radio" name="WMSSingleTile" class="inputRadio" checked/>
        &nbsp;<div id="lblSingleTileNo" class="radioLabel">No</div> &nbsp;
        <input id="WMSselectSingleTileNo" type="radio" name="WMSSingleTile" class="inputRadio"/>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblVisibilidad" class="formLabel">Visibilidad:</div>
	<div class="formField">	
		&nbsp;<div id="lblVisibilidadSi" class="radioLabel">S&iacute;</div> &nbsp;
        <input id="WMSselectVisibilidadSi" type="radio" name="WMSVisibilidad" class="inputRadio"/>
        &nbsp;<div id="lblVisibilidadNo" class="radioLabel">No</div> &nbsp;
        <input id="WMSselectVisibilidadNo" type="radio" name="WMSVisibilidad" class="inputRadio" checked/>
	</div>
	<div class="clear"></div>
	</div>
	
	<!-- Si la proyeccion es compatible con google por defecto es SI, sino deberia de ser por defecto NO -->
	<div class="formRow">
	<div id="lblStandar" class="formLabel">Peticiones en coordenadas geográficas WGS84:</div>
	<div class="formField">	
		&nbsp;<div id="lblStandarSi" class="radioLabel">S&iacute;</div> &nbsp;
        <input id="WMSselectStandarSi" type="radio" name="WMSStandar" class="inputRadio" checked/>
        &nbsp;<div id="lblStandarNo" class="radioLabel">No</div> &nbsp;
        <input id="WMSselectStandarNo" type="radio" name="WMSStandar" class="inputRadio"/>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblFilter" class="formLabel">Filtrar sobre el &aacute;mbito seleccionado:</div>
	<div class="formField">	
		&nbsp;<div id="lblFilterSi" class="radioLabel">S&iacute;</div> &nbsp;
        <input id="WMSselectFilterSi" type="radio" name="WMSFilter" class="inputRadio"/>
        &nbsp;<div id="lblFilterNo" class="radioLabel">No</div> &nbsp;
        <input id="WMSselectFilterNo" type="radio" name="WMSFilter" class="inputRadio" checked/>
	</div>
	<div class="clear"></div>
	</div>			
        
 </fieldset>

