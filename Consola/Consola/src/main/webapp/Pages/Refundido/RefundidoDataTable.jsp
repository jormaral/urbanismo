
<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.util.Iterator"
        %>
<%@page import="javax.naming.NamingException"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.refundido.Archivo"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.refundido.Proceso"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario"%>
<%
	if (session.getAttribute("usuario") != null) {		
		InitialContext context;
	    try {
			context = new InitialContext();
			if (session.getAttribute("gestorContextos") == null) {
				session.setAttribute("gestorContextos", context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/GestorContextosRefundidoBean!es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal"));
			}
			if (session.getAttribute("servicioUsuarios") == null) {
				session.setAttribute("servicioUsuarios", context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioUsuarios!es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal"));
			}
			if (session.getAttribute("servicioDiccionario") == null) {
				session.setAttribute("servicioDiccionario", context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioDiccionarios!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal"));
			}
	    } catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		}
		
	    GestorContextosRefundidoLocal gestorContextos = (GestorContextosRefundidoLocal)session.getAttribute("gestorContextos");
	    ServicioUsuariosLocal servicioUsuarios = (ServicioUsuariosLocal)session.getAttribute("servicioUsuarios");
	    ServicioDiccionariosLocal servicioDiccionario = (ServicioDiccionariosLocal)session.getAttribute("servicioDiccionario");
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
	    Proceso[] procesos = gestorContextos.getProcesos((Usuario)session.getAttribute("usuario"));
	    
	    boolean tieneLog = false;
	    boolean tieneFip = false;
	    String inicio = "";
	    String fin = "";
%>
<div class="tablaValidacion" id="contenedorTablaRefundidosRealizados">
	<table style="width:100%; border-width: 1px; border-style: solid; border-color: #EEEEEE;">
    	<tr class="cabeceraTablaValidacion">
    		<td class="filaValidacionCelda" width="30%">&Aacute;mbito</td>
    		<td class="filaValidacionCelda" width="30%">Usuario</td>
    		<td class="filaValidacionCelda" width="200px">Inicio</td>
    		<td class="filaValidacionCelda" width="200px">Fin</td>
    		<td class="filaValidacionCelda" width="70px">Log</td>
    		<td class="filaValidacionCelda" width="70px">Fip</td>
    	</tr>
    	<% for(int i =0; i < procesos.length;i++) {
    		
    		tieneLog = false;
    	    tieneFip = false;
    	    inicio = "";
    	    fin = "";
    		
    		for (Archivo documento : procesos[i].getArchivos()) {
				if (documento.getTipo().equals("LOG")) {
					tieneLog = true;
				} else {
					tieneFip = true;
				}
			}
    		
    		if (procesos[i].getInicio() != null && procesos[i].getInicio() instanceof java.util.Date) 
    			{inicio = sdf.format(procesos[i].getInicio());}
    		
    		if (procesos[i].getFin() != null && procesos[i].getFin() instanceof java.util.Date) 
			{fin = sdf.format(procesos[i].getFin());}
    	%>
    	
	    	<tr <%=((i%2) == 0)?"class=\"filaParValidacion\"":"class=\"filaImparValidacion\""%>>
	    		<td class="filaValidacionCelda" width="30%" style = "border-right: hidden">
	    			<%=servicioDiccionario.getTraduccion(Ambito.class, procesos[i].getAmbito(), "es")%>    			
	    		</td>
	    		<td class="filaValidacionCelda" width="30%">
	    			<%=servicioUsuarios.getUsuario(procesos[i].getUsuario()).getNombre()%>
	    		</td>
	    		<td class="filaValidacionCelda" width="200px" style="text-align: center;">
	    			<%=inicio%>
	    		</td>
	    		<td class="filaValidacionCelda" width="200px" style="text-align: center;">
	    			 <%=fin%>
	    		</td>
	    		<td class="filaValidacionCelda" width="70px">
	    		<% if (tieneLog) { %>
	    			<span class="iconWrapper" id="btnLog-<%=procesos[i].getIden()%>" onclick="window.open('GestionConsola?wsName=GET_ARCHIVO_REFUNDIDO&idProceso=<%=procesos[i].getIden()%>&tipo=LOG&idioma=es','_blank');">
	    				<img class="toolBoxAction" src="styles/images/LOG.png" alt="Descargar log del proceso" title="Descargar log del proceso"/>
	    			</span>
       			<% } else { %>
       				No generado
       			<% } %>
	    		</td>
	    		<td class="filaValidacionCelda" width="70px">
	    		<% if (tieneFip) { %>
	    			<span class="iconWrapper" id="btnLog-<%=procesos[i].getIden()%>" onclick="window.open('GestionConsola?wsName=GET_ARCHIVO_REFUNDIDO&idProceso=<%=procesos[i].getIden()%>&tipo=FIP&idioma=es','_blank');">
	    				<img class="toolBoxAction" src="styles/images/FIP.png" alt="Descargar Fip" title="Descargar Fip"/>
	    			</span>
       			<% } else { %>
       				No generado
       			<% } %>
	    		</td>
	    	</tr>
    	<%} %>
    	</table>
    <br/><br/>
</div>
<% } %> 
