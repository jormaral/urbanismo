<!-- 
    Document   : index
    Created on : 01-nov-2011, 17:00:00
    Author     : Alvaro.Montero    
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 

	String nombre = "", codigo = "", apartado = "";

	if (session.getAttribute("buscar.determinacion.nombre") != null) 
	{ nombre = session.getAttribute("buscar.determinacion.nombre").toString(); }
	
	if (session.getAttribute("buscar.determinacion.codigo") != null) 
	{ codigo = session.getAttribute("buscar.determinacion.codigo").toString(); }
	
	if (session.getAttribute("buscar.determinacion.apartado") != null) 
	{ apartado = session.getAttribute("buscar.determinacion.apartado").toString(); }
%>

<div class="form">
<form id="VisorPLANESBusquedaDeterminaciones" action="#" method="get">

	<br/>

	<div class="formRow">
	<div class="formLabel" style='width: 100px;'>Nombre:</div>
	<div class="formField"><input type="text" class="input" id="txtNombre" value="<%=nombre%>" maxlength="100" style="width: 95%" onchange="buquedaModificadaVisorPlanes=true;"/></div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div class="formLabel" style='width: 100px;'>C&oacute;digo:</div>
	<div class="formField"><input type="text" class="input" id="txtCodigo" value="<%=codigo%>" maxlength="100" style="width: 95%" onchange="buquedaModificadaVisorPlanes=true;"/></div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div class="formLabel" style='width: 100px;'>Apartado:</div>
	<div class="formField"><input type="text" class="input" id="txtApartado" value="<%=apartado%>" maxlength="100" style="width: 95%" onchange="buquedaModificadaVisorPlanes=true;"/></div>
	<div class="clear"></div>
	</div>	
	
	<br/><br/>
	
	<div class="buttonRow">
	<div class="formField" style="width:100%;">
		<input type="submit" id="BtnVisorPlanesCancelarBuscarDeterminaciones" class="button" value="Cancelar" style="float:left;"/>
		<input type="submit" id="BtnVisorPlanesBuscarDeterminaciones" class="button" value="Buscar" style="float:right;"/>
		<CENTER><label id="BtnVisorPlanesResultadosBuscarDeterminaciones"></label></CENTER>
	</div>
	<div class="clear"></div>
	</div>
		
</form>
</div>