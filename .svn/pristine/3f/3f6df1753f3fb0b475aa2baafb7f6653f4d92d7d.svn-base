/*
 * Copyright 2011 red.es
 * Autores: Arnaiz Consultores.
 *
 ** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
 * sean aprobadas por la Comision Europea- versiones
 * posteriores de la EUPL (la <<Licencia>>);
 * Solo podra usarse esta obra si se respeta la Licencia.
 * Puede obtenerse una copia de la Licencia en:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 * Salvo cuando lo exija la legislacion aplicable o se acuerde.
 * por escrito, el programa distribuido con arreglo a la
 * Licencia se distribuye <<TAL CUAL>>,
 * SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
 * ni implicitas.
 * Vease la Licencia en el idioma concreto que rige
 * los permisos y limitaciones que establece la Licencia.
 */
package es.mitc.redes.urbanismoenred.servicios.ficha;

import es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntodeterminaciongrupo;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntogrupo;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Determinacionclasifacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Determinacionclasifuso;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Ficha;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Grupodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Grupodeterminaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Regimenesacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Regimenesuso;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Valoresclasifacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Valoresclasifuso;
import javax.ejb.Local;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioFichaLocal {
    <T> T get(Class<T> clase, Object identificador);
    Boolean up(Class<?> clase, Object identificador)  throws ExcepcionFicha;
    Boolean down(Class<?> clase, Object identificador)  throws ExcepcionFicha;
    Ficha[] getFichaPorTramite(int idTramite);
    Conjuntogrupo[] getGruposFromFicha(int idFicha);
    Conjuntodeterminaciongrupo[] getGruposFromConjunto(int idConjunto);
    Grupodeterminacion[] getGruposDeterminacionFromFichaConjunto(int idFicha, int idConjunto);
    Grupodeterminaciondeterminacion[] getDeterminacionFromGrupoDeterminacion(int idGrupoDet);
    Determinacionclasifuso[] getDeterminacionesClasificatoriasUso(int idFicha);
    Determinacionclasifacto[] getDeterminacionesClasificatoriasActo(int idFicha);
    Valoresclasifacto[] getValoresFromDetClasifActo(int idFicha, int idDeterminacion);
    Valoresclasifuso[] getValoresFromDetClasifUso(int idFicha, int idDeterminacion);
    Regimenesuso[] getDeterminacionesRegimenUso(int idFicha);
    Regimenesacto[] getDeterminacionesRegimenActo(int idFicha);
    Ficha guardarFicha(int idTramite,int idFicha,String nombreFicha,String xPath,String ruta) throws ExcepcionFicha;
    boolean delFicha(long idFicha) throws ExcepcionFicha;
    boolean delConjunto(long idConjunto) throws ExcepcionFicha;
    boolean delGrupo(long idConjuntoDeterminacionGrupo) throws ExcepcionFicha;
    boolean delGrupoDeterminacion(long idGrupoDeterminacion) throws ExcepcionFicha;
    boolean delDeterminacionGrupo(long idGrupodeterminaciondeterminacion) throws ExcepcionFicha;
    boolean delDeterminacionClasificatoriaUso(long idDetClasif) throws ExcepcionFicha;
    boolean delDeterminacionClasificatoriaActo(long idDetClasif) throws ExcepcionFicha;
    boolean delValorDetClasifActo(long idValorDetClasif) throws ExcepcionFicha;
    boolean delValorDetClasifUso(long idValorDetClasif) throws ExcepcionFicha;
    boolean delDeterminacionRegimenUso(long idRegimenUso) throws ExcepcionFicha;
    boolean delDeterminacionRegimenActo(long idRegimenActo) throws ExcepcionFicha;
    Conjuntogrupo guardarConjuntoGrupo(long idFicha,long idConjunto,String nombreGrupo,long ordenGrupo,boolean regimen_directo,boolean actos,boolean usos) throws ExcepcionFicha;
    Grupodeterminacion guardarGrupoDeterminacion(long idGrupoDeterminacion,long idFicha,long idConjunto,String nombreGrupo,long ordenGrupo) throws ExcepcionFicha;
    Conjuntodeterminaciongrupo guardarGrupo(long idConjunto,long idDeterminacionGrupo,long ordenGrupo) throws ExcepcionFicha;
    Grupodeterminaciondeterminacion guardarGrupoDeterminacionDeterminacion(long idGrupoDeterminacion,long idDeterminacion,long orden,boolean regEsp) throws ExcepcionFicha;
    Determinacionclasifuso guardarDetClasifUso(long idFicha, long idDeterminacion) throws ExcepcionFicha;
    Determinacionclasifacto guardarDetClasifActo(long idFicha, long idDeterminacion) throws ExcepcionFicha;
    Regimenesuso guardarRegimenUso(long idFicha, long idDeterminacion, long orden) throws ExcepcionFicha;
    Regimenesacto guardarRegimenActo(long idFicha, long idDeterminacion, long orden) throws ExcepcionFicha;
    Valoresclasifuso guardarValorDetClasifUso(long idDeterminacionClasif,long idDeterminacion,long orden) throws ExcepcionFicha;
    Valoresclasifacto guardarValorDetClasifActo(long idDeterminacionClasif,long idDeterminacion,long orden) throws ExcepcionFicha;
}
