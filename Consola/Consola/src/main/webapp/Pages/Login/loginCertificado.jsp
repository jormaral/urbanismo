<!-- 
    Document   : index
    Created on : 01-nov-2011, 17:00:00
    Author     : Alvaro.Montero
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div id="formLoginCert"> 
    <span class="iconWrapper" style="position: absolute;top: 335px;height: 50px;left: 190px;"><img id="loginBtnBackCert" class="buttonImage" src="styles/images/regresar.png" alt="Volver" title="Volver a la pantalla anterior"/></span>
    <span class="iconWrapper"  style="position: absolute;top: 335px;height: 50px;left: 420px;"><img id="loginBtnSubmitCert" class="buttonImage" src="styles/images/validar.png" alt="Acceder" title="Acceder a la aplicaci&oacute;n"/></span>
    <div id="formLoginDNI">
        <applet id="ValidacionDNIe" code="ValidacionDNIe" archive="ValidacionDNI.jar" width="300"></applet>
    </div>    
</div>