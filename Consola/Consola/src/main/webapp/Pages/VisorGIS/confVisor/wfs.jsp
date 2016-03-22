<!-- 
    Document   : WFS
    Created on : 01-nov-2011, 17:00:00
    Author     : Alvaro.Montero
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
   
<fieldset>
    <legend>Capa WFS</legend>
    <br/>
    
    <div class="formRow">
	<div id="lblText" class="formLabel">Nombre:</div>
	<div class="formField"><input type="text" class="input" id="WFSinputText" value="" maxlength="50" style="width: 100%" /></div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblUrl" class="formLabel">Url:</div>
	<div class="formField">
		<input type="text" class="input" id="WFSinputUrl" value="" maxlength="200" style="width: 95%" />
		<div id="WFSbotonUrl" title="Introduce una dirección y pulse para cargar las capas " class="botonUrl"></div>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblLayers" class="formLabel">Capas:</div>
	<div class="formField">
        <select id="WFSinputLayers" size="1" style="width: 100%" multiple="true">
            <option value="">Introduce url</option>
       	</select>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblTransparent" class="formLabel">Por Defecto:</div>
	<div class="formField">	
		&nbsp;<div id="lblPorDefectoSi" class="radioLabel">S&iacute;</div> &nbsp;
        <input id="WFSselectPorDefectoSi" type="radio" name="WFSTransparent" class="inputRadio" checked/>
        &nbsp;<div id="lblPorDefectoNo" class="radioLabel">No</div> &nbsp;
        <input id="WFSselectPorDefectoNo" type="radio" name="WFSTransparent" class="inputRadio"/>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblVisibilidad" class="formLabel">Visibilidad:</div>
	<div class="formField">	
		&nbsp;<div id="lblVisibilidadSi" class="radioLabel">S&iacute;</div> &nbsp;
        <input id="WFSselectVisibilidadSi" type="radio" name="WFSVisibilidad" class="inputRadio" checked/>
        &nbsp;<div id="lblVisibilidadNo" class="radioLabel">No</div> &nbsp;
        <input id="WFSselectVisibilidadNo" type="radio" name="WFSVisibilidad" class="inputRadio"/>
	</div>
	<div class="clear"></div>
	</div>	
        
 </fieldset>
