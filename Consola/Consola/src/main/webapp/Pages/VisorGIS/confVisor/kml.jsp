<!-- 
    Document   : KML
    Created on : 01-nov-2011, 17:00:00
    Author     : Alvaro.Montero
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
<fieldset>
    <legend>Capa KML</legend>
    <br/>
    
    <div class="formRow">
	<div id="lblText" class="formLabel">Nombre:</div>
	<div class="formField"><input type="text" class="input" id="KMLinputText" value="" maxlength="50" style="width: 100%" /></div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblUrl" class="formLabel">Url:</div>
	<div class="formField"><input type="text" class="input" id="KMLinputUrl" value="" maxlength="200" style="width: 100%" /></div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblPorDefecto" class="formLabel">Por defecto:</div>
	<div class="formField">	
		&nbsp;<div id="lblPorDefectoSi" class="radioLabel">S&iacute;</div> &nbsp;
        <input id="KMLselectPorDefectoSi" type="radio" name="KMLporDefecto" class="inputRadio" checked/>
        &nbsp;<div id="lblPorDefectoNo" class="radioLabel">No</div> &nbsp;
        <input id="KMLselectPorDefectoNo" type="radio" name="KMLporDefecto" class="inputRadio"/>
	</div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div id="lblVisibilidad" class="formLabel">Visibilidad:</div>
	<div class="formField">	
		&nbsp;<div id="lblVisibilidadSi" class="radioLabel">S&iacute;</div> &nbsp;
        <input id="KMLselectVisibilidadSi" type="radio" name="KMLVisibilidad" class="inputRadio" checked/>
        &nbsp;<div id="lblVisibilidadNo" class="radioLabel">No</div> &nbsp;
        <input id="KMLselectVisibilidadNo" type="radio" name="KMLVisibilidad" class="inputRadio"/>
	</div>
	<div class="clear"></div>
	</div>
        
 </fieldset>
