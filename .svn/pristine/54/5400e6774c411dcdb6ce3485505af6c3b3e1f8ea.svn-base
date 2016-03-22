<%--
    Document   : configVisor
    Created on : 04-feb-2010, 8:48:44
    Author     : alvaro.martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
	import="es.mitc.redes.urbanismoenred.servicios.seguridad.Ambito"
	import="es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol"
    import="es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario"
    %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<div id="wrapperConfigVisor">
    <div id="selecAmbitoConfigVisor">
        <h3>Seleccione el ámbito a configurar</h3>
        <ul>
            <%
            String idioma = "es";
            
            Usuario usuario = (Usuario)session.getAttribute("usuario");
            
            if (usuario != null) {
            	Ambito[] ambitos = usuario.getAmbitos(TipoRol.CONFIGURADOR.getCodigo());
            	int idx = 0;
           		for (Ambito ambito : ambitos) {
                	String cssClass = "";
	                if (idx % 2 == 0) {
	                    cssClass = "even";
	                }
	            %><li id="amb-<%=ambito.getCodigo()%>" class="<%=cssClass%>" ><%=ambito.getNombre()%></li><%
	                idx++;
	            }
            } %>

        </ul>

    </div>
</div>
