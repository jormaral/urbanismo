<%@page import="javax.naming.NamingException"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal" %>
<%@ page import="es.mitc.redes.urbanismoenred.data.validacion.Proceso" %>
<%@page import="es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario"%>
<%
if (session.getAttribute("usuario") != null) {
	if (session.getAttribute("ServicioResultadosValidacionLocal") == null) {
		InitialContext context;
	    try {
			context = new InitialContext();
			session.setAttribute("ServicioResultadosValidacionLocal", context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioResultadosValidacionBean!es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal"));
		} catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		}
	}

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	ServicioResultadosValidacionLocal servicioResultadosValidacionLocal = (ServicioResultadosValidacionLocal)session.getAttribute("ServicioResultadosValidacionLocal");
	List<Proceso> fipsRealizados = servicioResultadosValidacionLocal.getProcesos((Usuario)session.getAttribute("usuario"));

	int counter = -1;
	Proceso actual = null;
    String inicio = "";
    String fin = "";
    String consolidado = "";
%>

<div class="tablaValidacion" id="contenedorTablaValidacionesRealizadas">
	<table style="width:100%; border-width: 1px; border-style: solid; border-color: #EEEEEE;">
    	<tr class="cabeceraTablaValidacion">
    		<td class="filaValidacionCelda" width="50%">Código FIP</td>
    		<td class="filaValidacionCelda" width="200px">Inicio</td>
    		<td class="filaValidacionCelda" width="200px">Fin</td>
    		<td class="filaValidacionCelda" width="200px">Consolidado</td>
    		<td class="filaValidacionCelda" width="70px">Log</td>
    		<td class="filaValidacionCelda" width="70px">Fip</td>
    	</tr>
    	<% for (Iterator<Proceso> i = fipsRealizados.iterator( ); i.hasNext( ); ) {
    		counter++;
    		actual = i.next( );
    	    inicio = "";
    	    fin = "";
    	    consolidado="";
    		if (actual.getInicio() != null && actual.getInicio() instanceof java.util.Date) 
    			{inicio = sdf.format(actual.getInicio());}
    		
    		if (actual.getFin() != null && actual.getFin() instanceof java.util.Date) 
				{fin = sdf.format(actual.getFin());}
    		
    		if (actual.getConsolidado() != null && actual.getConsolidado() instanceof java.util.Date) 
				{consolidado = sdf.format(actual.getConsolidado());}
    	%>
    	
	    	<tr <%=((counter%2) == 0)?"class=\"filaParValidacion\"":"class=\"filaImparValidacion\""%>>
	    		<td class="filaValidacionCelda" width="50%">
	    			<%=actual.getIdfip()%>    			
	    		</td>
	    		<td class="filaValidacionCelda" width="200px" style="text-align: center;">
	    			<%=inicio%>
	    		</td>
	    		<td class="filaValidacionCelda" width="200px" style="text-align: center;">
	    			 <%=fin%>
	    		</td>
	    		<td class="filaValidacionCelda" width="200px" style="text-align: center;">
	    			 <%=consolidado%>
	    		</td>
	    		<td class="filaValidacionCelda" width="70px">
	    			<span class="iconWrapper" id="btnInforme-<%=actual.getIden()%>" onclick="window.open('ActionServletValidacionFIP?wsName=DESCARGAR_INFORME_VALIDACION&idProceso=<%=actual.getIden()%>','_blank');">
	    				<img class="toolBoxAction" src="styles/images/pdf18x18.png" alt="Descargar informe del proceso" title="Descargar informe del proceso"/>
	    			</span>
	    		</td>
	    		<td class="filaValidacionCelda" width="70px">
	    			<span class="iconWrapper" id="btnFIP-<%=actual.getIden()%>" onclick="window.open('ActionServletValidacionFIP?wsName=DESCARGAR_FIP&idProceso=<%=actual.getIden()%>','_blank');">
	    				<img class="toolBoxAction" src="styles/images/FIP.png" alt="Descargar Fip" title="Descargar Fip"/>
	    			</span>
	    		</td>
	    	</tr>
    	<%} %>
    	</table>
    <br/><br/>
</div>
<% } %> 