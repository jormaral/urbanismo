<!-- 
    Document   : index
    Created on : 01-nov-2011, 17:00:00
    Author     : Alvaro.Montero    
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 

	String nombre = "", codigo = "", fechaDesde = "", fechaHasta = "";

	if (session.getAttribute("buscar.plan.nombre") != null) 
	{ nombre = session.getAttribute("buscar.plan.nombre").toString(); }
	
	if (session.getAttribute("buscar.plan.codigo") != null) 
	{ codigo = session.getAttribute("buscar.plan.codigo").toString(); }
	
	if (session.getAttribute("buscar.plan.desde") != null) 
	{ fechaDesde = session.getAttribute("buscar.plan.desde").toString(); }
	
	if (session.getAttribute("buscar.plan.hasta") != null) 
	{ fechaHasta = session.getAttribute("buscar.plan.hasta").toString(); }
%>

<div class="form">
<form id="VisorPLANESBusquedaPlanes" action="#" method="get">
	
	<br/>
	
	<div class="formRow">
	<div class="formLabel" style='width: 100px;'>Nombre del plan:</div>
	<div class="formField"><input type="text" class="input" id="txtNombre" value="<%=nombre%>" maxlength="100" style="width: 95%" onchange="buquedaModificadaVisorPlanes=true;"/></div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div class="formLabel" style='width: 100px;'>C&oacute;digo del plan:</div>
	<div class="formField"><input type="text" class="input" id="txtCodigoPlan" value="<%=codigo%>" maxlength="100" style="width: 95%" onchange="buquedaModificadaVisorPlanes=true;"/></div>
	<div class="clear"></div>
	</div>
	
	<div class="formRow">
	<div class="formLabel" style='width: 100px;'>Fecha del tr&aacute;mite:</div>
	<div id="dateImputWrapper" class="formField">
		<input id="txtFechaTramiteDesdeVisible" class="input" value="<%=fechaDesde%>" name="txtFechaTramiteDesdeVisible" type="text" data-meiomask="fixed.date" onchange="buquedaModificadaVisorPlanes=true;"/>
		<input id="txtFechaTramiteDesde" class="input" value="<%=fechaDesde%>" name="txtFechaTramiteDesde" type="hidden" onchange="buquedaModificadaVisorPlanes=true;"/>
		&nbsp;&nbsp;
		<input id="txtFechaTramiteHastaVisible" class="input" value="<%=fechaHasta%>" name="txtFechaTramiteHastaVisible" type="text" data-meiomask="fixed.date" onchange="buquedaModificadaVisorPlanes=true;"/>
		<input id="txtFechaTramiteHasta" class="input" value="<%=fechaHasta%>" name="txtFechaTramiteHasta" type="hidden" onchange="buquedaModificadaVisorPlanes=true;"/>
	</div>	
	<div class="clear"></div>
	</div>
	
	<br/>
	
	<div class="buttonRow">
	<div class="formField" style="width:100%;">
		<input type="submit" id="BtnVisorPlanesCancelarBuscarPlanes" class="button" value="Cancelar" style="float:left;"/>
		<input type="submit" id="BtnVisorPlanesBuscarPlanes" class="button" value="Buscar" style="float:right;"/>
		<CENTER><label id="BtnVisorPlanesResultadosBuscarPlanes"></label></CENTER>		
	</div>
	<div class="clear"></div>
	</div>
		
</form>
</div>