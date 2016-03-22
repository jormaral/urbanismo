<%-- 
    Document   : selectRole
    Created on : 27-oct-2009, 13:17:31
    Author     : Alvaro.Martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="javax.naming.NamingException"
		import="javax.naming.InitialContext"
		import="es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal"
		import="es.mitc.redes.urbanismoenred.servicios.seguridad.Rol"%>
        
<%
	
	if (session.getAttribute("ServicioUsuarios") == null) {
		InitialContext context;
		try {
			context = new InitialContext();
			session.setAttribute("ServicioUsuarios", context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioUsuarios"));
		} catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		}
	}
	
	ServicioUsuariosLocal servicioUsuarios = (ServicioUsuariosLocal)session.getAttribute("ServicioUsuarios");
	
	Rol[] roles = servicioUsuarios.getRoles();
	
%>
<div id="selectrole_wrapper">
    <form id="selectrole_form" name="selectrole_form" action="#">
        <h3><label for="selectrole_idRol">Elija un rol</label></h3>
        <select id="selectrole_idRol" name="idRol" size="6">
            <%
                for (Rol rol : roles) {
            %>
            <option value="<%= rol.getCodigo()%>"><%= rol.getNombre()%></option>
            <% }%>
        </select>

        <h3><label for="selectrole_idAmbito">Elija un ámbito</label></h3>
        <input type="text" name="selectrole_idAmbito" id="selectrole_idAmbito" value="" />
    </form>
    <div id="selectrole_buttonpanel">
        <button class="buttonSmall" type="button" id="selectrole_btnAddRol"><img src="styles/images/add.png" alt="Añadir rol"/>
            <span>Añadir rol</span>
        </button>
    </div>
    <div class="clear"></div>
</div>
