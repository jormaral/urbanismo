<%-- 
    Document   : busqueda.jsp
    Created on : 18-dic-2009, 9:04:56
    Author     : Francisco.Morata
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

        <div id="zonaBusquedas" class="zonaBusquedas">
                <!--ZONA DE CATASTRO-->
                <div id="block_catastro"  class="block_catastro"></div>
                <div id="contenedor_catastro"  class="contenedor_catastro">
                    <div id="lblCatastro" class="traducible">CATASTRO</div>
                    <span id="lblProvincia" class="traducible">PROVINCIA</span>
                        <select id="select_provincia" class="selectProvincia"></select>
                    <span id="lblMunicipio" class="traducible">MUNICIPIO</span>
                        <select id="select_municipio" class="selectMunicipio"></select>
                    <span id="lblTipoBusq" class="traducible">TIPO BUSQ.</span>
                        <select id="selectTipoBusq" class="selectTipoBusq">
                            <option value="direccion" class="traducible">Por Dirección</option>
                            <option value="refCat" class="traducible">Por Ref. Catastral</option>
                            <option value="polparc" class="traducible">Por Pol./Parc.</option>
                        </select>
                    <div id="contenedorBusqDireccion">
                            <span id="lblDireccion" class="traducible">DIRECCIÓN</span>
                            <select id="selectTipoVia" class="selectTipoVia">
                                    <option value="CL" selected>CALLE</option>
                                    <option value="AV">AVENIDA</option>

                                    <option value="PZ">PLAZA</option>
                                    <option value="PS">PASEO</option>
                                    <option value="CR">CARRETERA, CARRERA</option>
                                    <option value="AC">ACCESO</option>
                                    <option value="AG">AGREGADO</option>
                                    <option value="AL">ALDEA, ALAMEDA</option>

                                    <option value="AN">ANDADOR</option>
                                    <option value="AR">AREA, ARRABAL</option>
                                    <option value="AY">ARROYO</option>
                                    <option value="AU">AUTOPISTA</option>
                                    <option value="BJ">BAJADA</option>
                                    <option value="BL">BLOQUE</option>

                                    <option value="BR">BARRANCO</option>
                                    <option value="BQ">BARRANQUIL</option>
                                    <option value="BO">BARRIO</option>
                                    <option value="CY">CALEYA</option>
                                    <option value="CJ">CALLEJA, CALLEJON</option>
                                    <option value="CZ">CALLIZO</option>

                                    <option value="CM">CAMINO, CARMEN</option>
                                    <option value="CP">CAMPA, CAMPO</option>
                                    <option value="CA">CAÑADA</option>
                                    <option value="CS">CASERIO</option>
                                    <option value="CH">CHALET</option>
                                    <option value="CI">CINTURON</option>

                                    <option value="CG">COLEGIO, CIGARRAL</option>
                                    <option value="CN">COLONIA</option>
                                    <option value="CO">CONCEJO, COLEGIO</option>
                                    <option value="CU">CONJUNTO</option>
                                    <option value="CT">CUESTA, COSTANILLA</option>
                                    <option value="DE">DETRAS</option>

                                    <option value="DP">DIPUTACION</option>
                                    <option value="DS">DISEMINADOS</option>
                                    <option value="ED">EDIFICIOS</option>
                                    <option value="EN">ENTRADA, ENSANCHE</option>
                                    <option value="ES">ESCALINATA</option>
                                    <option value="ES">ESPALDA</option>

                                    <option value="EX">EXPLANADA</option>
                                    <option value="EM">EXTRAMUROS</option>
                                    <option value="ER">EXTRARRADIO</option>
                                    <option value="FC">FERROCARRIL</option>
                                    <option value="FN">FINCA</option>
                                    <option value="GL">GLORIETA</option>

                                    <option value="GV">GRAN VIA</option>
                                    <option value="GR">GRUPO</option>
                                    <option value="HT">HUERTA, HUERTO</option>
                                    <option value="JR">JARDINES</option>
                                    <option value="LD">LADO, LADERA</option>
                                    <option value="LA">LAGO</option>

                                    <option value="LG">LUGAR</option>
                                    <option value="MA">MALECON</option>
                                    <option value="MZ">MANZANA</option>
                                    <option value="MS">MASIAS</option>
                                    <option value="MC">MERCADO</option>
                                    <option value="MT">MONTE</option>

                                    <option value="ML">MUELLE</option>
                                    <option value="MN">MUNICIPIO</option>
                                    <option value="PM">PARAMO</option>
                                    <option value="PQ">PARROQUIA, PARQUE</option>
                                    <option value="PI">PARTICULAR</option>
                                    <option value="PD">PARTIDA</option>

                                    <option value="PU">PASADIZO</option>
                                    <option value="PJ">PASAJE, PASADIZO</option>
                                    <option value="PC">PLACETA</option>
                                    <option value="PB">POBLADO</option>
                                    <option value="PL">POLIGONO</option>
                                    <option value="PR">PROLONGACION, CONTINUAC.</option>

                                    <option value="PT">PUENTE</option>
                                    <option value="QT">QUINTA</option>
                                    <option value="RA">RACONADA</option>
                                    <option value="RM">RAMAL</option>
                                    <option value="RB">RAMBLA</option>
                                    <option value="RC">RINCON, RINCONA</option>

                                    <option value="RD">RONDA</option>
                                    <option value="RP">RAMPA</option>
                                    <option value="RR">RIERA</option>
                                    <option value="RU">RUA</option>
                                    <option value="SA">SALIDA</option>
                                    <option value="SN">SALON</option>

                                    <option value="SC">SECTOR</option>
                                    <option value="SD">SENDA</option>
                                    <option value="SL">SOLAR</option>
                                    <option value="SU">SUBIDA</option>
                                    <option value="TN">TERRENOS</option>
                                    <option value="TO">TORRENTE</option>

                                    <option value="TR">TRAVESIA</option>
                                    <option value="UR">URBANIZACION</option>
                                    <option value="VA">VALLE</option>
                                    <option value="VR">VEREDA</option>
                                    <option value="VI">VIA</option>
                                    <option value="VD">VIADUCTO</option>

                                    <option value="VL">VIAL</option>
                                    <option value="--">--</option>

                            </select>
                                <input id="select_direccion" ></>
                            <span id="lblNumero"  class="traducible">NÚMERO</span>
                                <input id="select_numero" class="select_numero">
                    </div>
                    <div id="contenedorBusqCatastro">
                        <span id="lblRefCat"  class="traducible">REF. CATASTRAL</span>
                        <input id="select_refCat" class="select_refCat">
                    </div>
                    <div id="contenedorBusqPolParc">
                        <span id="lblPoligono"  class="traducible">POLÍGONO</span>
                        <input id="select_poligono" class="select_poligono">
                        <span id="lblParcela"  class="traducible">PARCELA</span>
                        <input id="select_parcela" class="select_parcela">
                    </div>
                    <div id="btnEnviarCatastro"></div>
                </div>

                <!--ZONA DERECHA-->
                <div id="contenedor_google" class="contenedor_google" >
                     <span id="lblGoogle"  class="traducible">CALLEJERO DE GOOGLE</span>
                     <input id="select_google" class="select_google">
                        <div id="btnEnviarGoogle"></div>
                </div>
                <div id="contenedor_plan_entidad" class="contenedor_plan_entidad" >
                     <span id="lblPlanEntidad"  class="traducible">PLANES Y ENTIDADES</span>
                     <span id="lblAmbitos"  class="traducible">AMBITOS</span>
                        <select id="select_ambitos" class="select_ambitos">
                            <option value="-">-</option>
                        </select>
                        <div id="btnEnviarAmbito"></div>
                     <span id="lblPlan"  class="traducible">PLAN</span>
                     <input id="select_plan" class="select_plan">
                        <div id="btnEnviarPlan"></div>
                     <span id="lblEntidad"  class="traducible">ENTIDAD</span>
                     <input id="select_entidad" class="select_entidad">
                     <input type="checkbox" id="chkCombinado"/>
                     <div id="lblCombinado">COMBINADO</div>
                        <div id="btnEnviarEntidad"></div>
                </div>


                <div id="filtros">
                    <div id="filtroZoom" class="filtro traducible">ZOOM</div>
                    <div id="filtroFicha" class="filtro traducible">
                        <div id="lblFiltroFicha">FICHA</div>
                        <div id="selectTipoFichaParaFiltro"></div>
                    </div>
                    <div id="filtroCaptura" class="filtro traducible">CAPTURA</div>
                </div>
                <div id="resultados"></div>

        </div>
