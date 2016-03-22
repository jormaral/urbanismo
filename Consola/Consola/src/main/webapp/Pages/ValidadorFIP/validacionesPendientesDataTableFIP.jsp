<%@page import="javax.naming.NamingException"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<%@ page import="es.mitc.redes.urbanismoenred.servicios.validacion.ValidacionFipLocal" %>
<%@ page import="es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion" %>
<%@ page import="es.mitc.redes.urbanismoenred.servicios.validacion.Estado" %>
<% 
	if (session.getAttribute("ServicioValidacion") == null) {
		InitialContext context;
	    try {
			context = new InitialContext();
			session.setAttribute("ServicioValidacion", context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioValidacionFIP!es.mitc.redes.urbanismoenred.servicios.validacion.ValidacionFipLocal"));
		} catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		}
	}

	ValidacionFipLocal servicioValidacion = (ValidacionFipLocal)session.getAttribute("ServicioValidacion");
	ContextoValidacion[] fipsPendientes = servicioValidacion.getFipPendientes((String)session.getAttribute("login"));
%>

<div class="tablaValidacion" id="contenedorTablaValidacionesPendientes">
	<table style="width:100%; border-width: 1px; border-style: solid; border-color: #EEEEEE;">
    	<tr class="cabeceraTablaValidacion">
    		<td class="filaValidacionCelda" width="20%">Código FIP</td>
    		<td class="filaValidacionCelda" width="30%">Plan</td>
    		<td class="filaValidacionCelda" width="250px">Estado</td>
    		<td class="filaValidacionCelda" width="250px">Progreso</td>
    		<td class="filaValidacionCelda" width="250px">Informe</td>
    		<td class="filaValidacionCelda" width="25%">Acción</td>
    		<td class="filaValidacionCelda" width="100px"></td>
    	</tr>
    	<% 
    	ContextoValidacion ctxtActual = null;
    	for(int i =0; i < fipsPendientes.length;i++) {
    		ctxtActual = fipsPendientes[i];
    	%>
	    	<tr <%=((i%2) == 0)?"class=\"filaParValidacion\"":"class=\"filaImparValidacion\""%>>
	    		<td class="filaValidacionCelda" width="20%">
	    			<div id="codigoFIP-<%=ctxtActual.getCodigoFip()%>"><%=ctxtActual.getCodigoFip()%></div>	    			
	    		</td>
	    		<td class="filaValidacionCelda" width="30%">
	    			<%=ctxtActual.getParametro(ContextoValidacion.NOMBRE_PLAN) %>
	    		</td>
	    		<td class="filaValidacionCelda" width="250px" id="estado-<%=ctxtActual.getCodigoFip()%>" style="text-align: center;">
	    			<%=ctxtActual.getEstado().getDescripcion()%>
	    		</td>
	    		<td class="filaValidacionCelda" width="250px" id="progreso-<%=ctxtActual.getCodigoFip()%>" style="text-align: center;">
	    			No iniciado.
	    		</td>
	    		<td class="filaValidacionCelda" width="250px" id="informe-<%=ctxtActual.getCodigoFip()%>" style="text-align: center;">
	    			<div id="informeND-<%=ctxtActual.getCodigoFip()%>"
	    			<% if (ctxtActual.getEstado() == Estado.VALIDADO ||
					    			ctxtActual.getEstado() == Estado.VALIDACION_ERRONEA
					    			) { %>
					    	style="position:absolute; visibility: hidden;"		
					    <%}%>
	    				>No disponible.</div>
	    			<div id="informeImg-<%=ctxtActual.getCodigoFip()%>"
	    				<% if (!(ctxtActual.getEstado() == Estado.VALIDADO ||
					    			ctxtActual.getEstado() == Estado.VALIDACION_ERRONEA)
					    			) { %>
					    	style="visibility: hidden; width: 100%;"		
					    <%}  else {%>
					    	style="width: 100%;" 	
					    <%} %>
					    			>
	    				<a href="ActionServletValidacionFIP?wsName=DESCARGAR_INFORME_VALIDACION&tramite=<%=ctxtActual.getCodigoFip()%>"
	    					target = "_blank">
	    					<img id="imagenInformeImg-<%=ctxtActual.getCodigoFip()%>" src="styles/images/pdf32x32.png" class="displayedCentred"
	    					<% if (!(ctxtActual.getEstado() == Estado.VALIDADO ||
					    			ctxtActual.getEstado() == Estado.VALIDACION_ERRONEA)
					    			) { %>
								    	style="display: none;"		
								    <%}  else {%>
								    	style="display: block;"	
								    <%} %> />
	    				</a>
	    			</div>	    			
	    		</td>
	    		<td class="filaValidacionCelda" width="25%" align="right">
	    		<div>
	    			<form id="formSubir<%=ctxtActual.getCodigoFip()%>"
						method="post"
						action="ActionServletValidacionFIP"
						target="frameValidacion"						
						onsubmit="setTimeout(function(){obtenerProgresoSubida('<%= ctxtActual.getCodigoFip()%>');},2000);"						
						enctype="multipart/form-data"
						>
		    			<input type="hidden"
					    	value="<%=ctxtActual.getCodigoFip()%>"
					        name="tramite"
					        id="tramiteSubir-<%=ctxtActual.getCodigoFip()%>" />
		    			<input type="file"
		                       name="txtFile"
		                       id="txtFile-<%=ctxtActual.getCodigoFip()%>" />
	                    <input type="hidden"
					    	value="UPLOAD_FIP_FILE"
					    	id="wsNameSubir-<%=ctxtActual.getCodigoFip()%>"
					        name="wsName"
					        <% if (ctxtActual.getEstado() == Estado.CONSOLIDADO ||
					    			ctxtActual.getEstado() == Estado.SUBIENDO ||
					    			ctxtActual.getEstado() == Estado.DESPLEGANDO ||
					    			ctxtActual.getEstado() == Estado.DESPLEGADO ||
					    			ctxtActual.getEstado() == Estado.GUARDANDO  ||
					    			ctxtActual.getEstado() == Estado.VALIDANDO) { %>
					    	disabled
					    	<% } %>
					         />
					     <input type="submit" 
					    	value="Subir" 
					    	id="subir-<%=ctxtActual.getCodigoFip()%>" 
					    	name="subir"
					    	<% if (ctxtActual.getEstado() == Estado.CONSOLIDADO ||
					    			ctxtActual.getEstado() == Estado.SUBIENDO ||
					    			ctxtActual.getEstado() == Estado.DESPLEGANDO ||
					    			ctxtActual.getEstado() == Estado.DESPLEGADO ||
					    			ctxtActual.getEstado() == Estado.GUARDANDO  ||
					    			ctxtActual.getEstado() == Estado.VALIDANDO) { %>
					    	disabled
					    	<% } %>
					    	/>
				    </form>
				 </div>
	    		</td>
	    		<td class="filaValidacionCelda" width="100px">
	    		 <div>
				    <form id="formValidar<%=ctxtActual.getCodigoFip()%>"
						method="post"
						action="ActionServletValidacionFIP"
						target="frameValidacion">
		    			<input type="hidden"
					    	value="<%=ctxtActual.getCodigoFip()%>"
					        name="tramite"
					        id="tramiteValidar-<%=ctxtActual.getCodigoFip()%>" />
	                    <input type="hidden"
					    	value="START_VALIDATION"
					        name="wsName"
					        id="wsNameValidar-<%=ctxtActual.getCodigoFip()%>" />
					     <input type="submit" 
					    	value="Validar" 
					    	id="validar-<%=ctxtActual.getCodigoFip()%>" 
					    	<% if (ctxtActual.getEstado() != Estado.GUARDADO)
					    			 { %>
					    	disabled
					    	<% } %>
					    	name="validar"/>
				    </form>
				 </div>
	    		</td>
	    	</tr>
    	<%} %>
    	</table>
    <br/><br/>
</div>
<iframe id="frameValidacionID" name="frameValidacion" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
