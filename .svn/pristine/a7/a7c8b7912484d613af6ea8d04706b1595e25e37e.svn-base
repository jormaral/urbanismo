<!-- 
    Document   : index
    Created on : 01-nov-2011, 17:00:00
    Author     : Alvaro.Montero    
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 

	String nombre = "", codigo = "", clave = "";

	if (session.getAttribute("buscar.entidad.nombre") != null) 
	{ nombre = session.getAttribute("buscar.entidad.nombre").toString(); }
	
	if (session.getAttribute("buscar.entidad.codigo") != null) 
	{ codigo = session.getAttribute("buscar.entidad.codigo").toString(); }
	
	if (session.getAttribute("buscar.entidad.clave") != null) 
	{ clave = session.getAttribute("buscar.entidad.clave").toString(); }
%>

<div class="form">
<form id="VisorPLANESBusquedaEntidades" action="#" method="get">

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
	<div class="formLabel" style='width: 100px;'>Clave:</div>
	<div class="formField"><input type="text" class="input" id="txtClave" value="<%=clave%>" maxlength="100" style="width: 95%" onchange="buquedaModificadaVisorPlanes=true;"/></div>
	<div class="clear"></div>
	</div>	
	
	<br/><br/>
	
	<div class="buttonRow">
	<div class="formField" style="width:100%;">
		<input type="submit" id="BtnVisorPlanesCancelarBuscarEntidades" class="button" value="Cancelar" style="float:left;"/>
		<input type="submit" id="BtnVisorPlanesBuscarEntidades" class="button" value="Buscar" style="float:right;"/>
		<CENTER><label id="BtnVisorPlanesResultadosBuscarEntidades"></label></CENTER>
	</div>
	<div class="clear"></div>
	</div>
		
</form>
</div>