/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.dal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.comunes.EncriptacionCodigoTramite;
import es.mitc.redes.urbanismoenred.servicios.refundido.ClsDatos;

/**
 * Session Bean implementation class GestorConsultasBean
 * 
 * @author Arnaiz Consultores
 */
@Stateless(name = "GestorConsultas")
public class GestorConsultasBean implements GestorConsultasLocal {
	
	private static final Logger log = Logger.getLogger(GestorConsultasBean.class);
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	private String[][] gDependencias;

    /**
     * Default constructor. 
     */
    public GestorConsultasBean() {
    }
 
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorConsultasLocal#esDesarrollo(int)
     */
    @Override
	public boolean esDesarrollo(int idTramite) {	
		return em.createNamedQuery("Tramite.esDesarrollo")
				.setParameter("idTramite", idTramite)
				.setParameter("idTipoOperacionPlan", ClsDatos.ID_TIPOOPERACIONPLAN_DESARROLLO)
				.getResultList().size() >0;
	}

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.dal.GestorConsultasLocal#generarCodigoTramite(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite)
     */
    @Override
	public String generarCodigoTramite(Tramite tramite)
			throws ExcepcionPersistencia {
		StringBuffer sb = new StringBuffer();
        String format = String.format("%%0%dd", 6);
        try{
            // ámbito
        	sb.append(String.format(format, tramite.getPlan().getIdambito()));
            
            // Plan
        	sb.append(tramite.getPlan().getCodigo());

            // Tipo de trámite
        	sb.append(String.format(format, tramite.getIdtipotramite()));

            // Iteracion
        	format = String.format("%%0%dd", 2);
        	sb.append(String.format(format, tramite.getIteracion()));
        	
        	return EncriptacionCodigoTramite.getEncoded(sb.toString()).toUpperCase();
        } catch (Exception e){
        	throw new ExcepcionPersistencia("No se ha logrado calcular el código del trámite."  + e.getMessage());
        }
	}
    
	/**
	 * 
	 * @param tabla
	 * @param idTramite
	 * @param longitudCodigo
	 * @return
	 */
	private String getSiguienteCodigo(String tabla, int idTramite, int longitudCodigo){
        long maxCod=0;
        try {
            // Se ignora el primer carácter, ya que es posible que, al igual que con
            //  las determinaciones, dicho carácter sea usado para que, en los
            //  ficheros FIP, se puedan distinguir entidades de diferentes
            //  características.
            String s="Select Max(SubStr(LPad(Trim(codigo)," + longitudCodigo +
                    ",'0'),2," + (longitudCodigo-1) + ")) " +
                    "From "+ tabla +" "+
                    "Where idtramite=" + idTramite;
            Object resultado = em.createNativeQuery(s).getSingleResult();
            
            // Si el trámite está vacío devuelve un valor nulo
            if (resultado != null){
                maxCod=Integer.parseInt(resultado.toString());
            }         
        } catch (Exception e){
           log.warn("Fallo al generar código. " + e.getMessage() + ". Código 23007.", e );
        }
        
        String format = String.format("%%0%dd", longitudCodigo);
        
        return String.format(format, ++maxCod);
    }
    
    /*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorConsultasLocal#getSiguienteCodigoDeterminacion(int)
	 */
    @Override
	public String getSiguienteCodigoDeterminacion(int idTramite) {
		return getSiguienteCodigo("planeamiento.Determinacion", idTramite, ClsDatos.LONG_CODIGO_DETERMINACION);
	}

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorConsultasLocal#getSiguienteCodigoEntidad(int)
     */
    @Override
	public String getSiguienteCodigoEntidad(int idTramite) {
		return getSiguienteCodigo("planeamiento.Entidad", idTramite, ClsDatos.LONG_CODIGO_ENTIDAD);
	}
	
	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorConsultasLocal#getSiguienteCodigoPlan()
     */
	@Override
	public String getSiguienteCodigoPlan() {
		String format = String.format("%%0%dd", ClsDatos.LONG_CODIGO_PLAN);
	
        return String.format(format, 
        		Integer.parseInt(em.createNativeQuery("Select Cast(Max(Trim(codigo)) As text) From planeamiento.Plan")
        				.getSingleResult().toString()) +1);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@PostConstruct
    private void inicializar() {
    	// Averigua las dependencias entre tablas de planeamiento
        // Dependencias entre tablas: nDependencias y 3 columnas:
        //      0=TablaPrincipal
        //      1=TablaDependiente
        //      2=Campo
       
       String  s = "Select Cast(c1.relname As text) As C1, " + 
            "Cast(c2.relname As text) As C2, Cast(a.attname As text) As C3 " +
            "From pg_catalog.pg_depend d1, pg_catalog.pg_constraint cns , " +
            "pg_catalog.pg_attribute a, pg_catalog.pg_class c1, " +
            "pg_catalog.pg_namespace ns1, pg_catalog.pg_class c2, " +
            "pg_catalog.pg_namespace ns2 " +
            "Where d1.objid=cns.oid " +
            "And Upper(Trim(c1.relname)) In (Select Upper(Trim(relname)) " +
            "From pg_class Where relnamespace=(Select oid " +
            "From pg_namespace " +
            "Where Upper(Trim(nspname))='PLANEAMIENTO') " +
            "And Upper(Trim(relkind))='R') " +
            "And Upper(Trim(ns1.nspname))='PLANEAMIENTO' " +
            "And c1.relnamespace=ns1.oid " +
            "And d1.refobjid=c1.oid  " +
            "And Upper(Trim(d1.deptype))='A'  " +
            "And Upper(Trim(cns.contype))='F'  " +
            "And d1.refobjid=a.attrelid " +
            "And d1.refobjsubid=a.attnum " +
            "And cns.confrelid=c2.oid " +
            "And c2.relnamespace=ns2.oid " +
            "Order By ns1.nspname, c1.relname, ns2.nspname, c2.relname, a.attname ";
       
       List<Object[]> lista = em.createNativeQuery(s).getResultList();
       
       int nDep = lista.size();
       gDependencias = new String[nDep][3];
       Iterator<Object[]> it = lista.iterator();
       int i = 0;
       while (it.hasNext()){
           Object[] obj = it.next();
           gDependencias[i][0]=obj[0].toString();
           gDependencias[i][1]=obj[1].toString();
           gDependencias[i][2]=obj[2].toString();
           i++;
       }
    }
	
    private String numeroArabigoDeRomano(String romano){
        int numero=-1;
        int iValor;
        int iValorAnt;
        int i;
        char c;

        try{
            if(romano.length()==0){
                return "0";
            }

            numero = 0;
            iValorAnt=0;
            for (i=romano.length();i>0;i--){
                c=romano.charAt(i-1);
                iValor=valorDeCaracterRomano(String.valueOf(c));
                if(iValor==-1){
                    return "0";
                }
                if(iValor<iValorAnt){
                    numero=numero-iValor;
                }
                else{
                    numero=numero+iValor;
                }
                iValorAnt=iValor;
            }
        } catch(Exception e){
            log.error("Fallo en numeroArabigoDeRomano. " + e + ". Código 23016." );
            return "0";
        }

        return String.valueOf(numero);
    }
	
	/**
     * 
     * @param arabigo
     * @return
     */
    private String numeroRomanoDeArabigo(int arabigo){
        int i; // 1
        int v; // 5
        int x; // 10
        int l; // 50
        int c; // 100
        int d; // 500
        int m; // 1000
        int q; // 5000
        int h; // 10000
        String s="";
        int n;

        try{
            // 10 Miles
            h=arabigo/10000;
            arabigo=arabigo-h*10000;

            // 5 Miles
            q=arabigo/5000;
            arabigo=arabigo-q*5000;

            // 1 Miles
            m=arabigo/1000;
            arabigo=arabigo-m*1000;

            // Quinientos
            d=arabigo/500;
            arabigo=arabigo-d*500;

            // Centenas
            c=arabigo/100;
            arabigo=arabigo-c*100;

            // Cincuentenas
            l=arabigo/50;
            arabigo=arabigo-l*50;

            // Decenas
            x=arabigo/10;
            arabigo=arabigo-x*10;

            // Quintas
            v=arabigo/5;
            arabigo=arabigo-v*5;

            // Unidades
            i=arabigo;

            // Calcula la representación en caracteres romanos.
            s="";
            if(h>0){
                for(n=1;n<=h;n++){
                    s=s+"H";
                }
            }

            if(q>0){
                for(n=1;n<=q;n++){
                    s=s+"Q";
                }
            }

            if(m>0){
                if(m<=3){
                    for(n=1;n<=m;n++){
                        s=s+"M";
                    }
                }else {
                    s=s+"MQ";
                }
            }

            if(d>0){
                for(n=1;n<=d;n++){
                    s=s+"D";
                }
            }

            if(c>0){
                if(c<=3){
                    for(n=1;n<=c;n++){
                        s=s+"C";
                    }
                }else {
                    s=s+"CD";
                }
            }

            if(l>0 && x<=3){
                for(n=1;n<=l;n++){
                    s=s+"L";
                }
            }

            if(x>0){
                if(x<=3){
                    for(n=1;n<=x;n++){
                        s=s+"X";
                    }
                }else {
                    if(l==0){
                        s=s+"XL";
                    } else{
                        s=s+"XC";
                    }
                }
            }

            if(v==1){
                if(i<=3){
                    s=s+"V";
                    for(n=1;n<=i;n++){
                        s=s+"I";
                    }
                }else {
                    s=s+"IX";
                }
            } else {
                if(i<=3){
                    for(n=1;n<=i;n++){
                        s=s+"I";
                    }
                }else {
                    s=s+"IV";
                }    
            }
            
        } catch(Exception e){
            log.error("Fallo en numeroRomanoDeArabigo. " + e + ". Código 23034." );
            return "";
        }

        return s;
    }
	
	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorConsultasLocal#obtenerAmbitoAplicacionPorTramite(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite)
     */
	@SuppressWarnings("unchecked")
	@Override
	public Entidad obtenerAmbitoAplicacionPorTramite(Tramite tramite) {

		List<Entidad> ambitosAplicacion = em.createNamedQuery("Entidad.obtenerAmbitoAplicacion")
				.setParameter("idTramite", tramite.getIden())
				.setParameter("codigoAmbitoAplicacion", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION)
				.getResultList();

		if (ambitosAplicacion.size() > 0) {
			if (ambitosAplicacion.size() > 1) {
				log.warn("Se han encontrado varias entidades ámbito de aplicación para el trámite " + tramite.getCodigofip());
			}
			
			return ambitosAplicacion.get(0);
		} else {
			ambitosAplicacion = em.createNamedQuery("Entidad.obtenerAmbitoAplicacionDerivado")
					.setParameter("idTramite", tramite.getIden())
					.setParameter("codigoAmbitoAplicacion", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION)
					.getResultList();
			
			if (ambitosAplicacion.size() > 0) {
				if (ambitosAplicacion.size() > 1) {
					log.warn("Se han encontrado varias entidades ámbito de aplicación para el trámite " + tramite.getCodigofip());
				}
				
				return ambitosAplicacion.get(0);
			}
		}
        return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorConsultasLocal#obtenerDeterminacionGrupoDeEntidadesPorTramite(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Determinacion obtenerDeterminacionGrupoDeEntidadesPorTramite(
			Tramite tramite) {
		log.debug("Obteniendo determinacion grupo de entidades por tramite...");
        List<Determinacion> determinaciones = em.createNamedQuery("Determinacion.obtenerPorTramiteYCaracter")
        		.setParameter("idTramite", tramite.getIden())
        		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
        		.getResultList();
        if (determinaciones.size() > 0) {
        	log.debug("Determinación obtenida: " + determinaciones.get(0).getIden());
            // Se toma la primera, aunque puede haber más, debido a que se van
            //  reasignando las determinaciones desde un trámite hasta otro.
            return determinaciones.get(0);
        } else {
        	log.debug("Obteniendo cualquier determinacion grupo de entidades por trámite");
        	// Si no se ha encontrado en el trámite en cuestión, es porque el trámite
            //  está usando la determinación 'Grupo de Entidades' de otro 
            //  trámite, que puede ser del plan base.
            // Por lo tanto, se toma cualquier determinación de caracter 'GRUPODEENTIDADES'
            //  que está aplicada en alguna de las entidades del trámite.
        	determinaciones = em.createNamedQuery("Determinacion.obtenerCualquieraPorTramiteYCaracter").setParameter("idTramite", tramite.getIden())
            		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
            		.getResultList();
            
            if (determinaciones.size() > 0) {
            	log.debug("Determinación obtenida: " + determinaciones.get(0).getIden());
                // Se toma la primera, aunque puede haber más
                return determinaciones.get(0);
            }
        }
        
        log.warn("No se ha encontrado determinación grupo de entidades");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorConsultasLocal#obtenerDeterminacionGrupoEquivalentePorEntidad(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Determinacion obtenerDeterminacionGrupoEquivalentePorEntidad(Entidad entidad, Tramite tramite, List<Integer> listaIdTramitesBase){
		log.debug("obtenerDeterminacionGrupoEquivalentePorEntidad");
        try{
        	Determinacion grupo=(Determinacion) em.createNamedQuery("Casoentidaddeterminacion.obtenerDeterminacionGrupo").setParameter("idEntidad", entidad.getIden())
            		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
            		.getSingleResult();
        	
        	Determinacion grupoEntidades= obtenerDeterminacionGrupoDeEntidadesPorTramite(tramite);

            if(grupoEntidades!=null){
            	if (listaIdTramitesBase.contains(grupo.getTramite().getIden())){
                    return grupo;
                }
          
                List<Determinacion> determinaciones = em.createNamedQuery("Opciondeterminacion.buscarporDetYBaseValorRef")
                			.setParameter("idDeterminacion", grupoEntidades.getIden())
                			.setParameter("idBase", grupo.getDeterminacionByIddeterminacionbase().getIden())
                			.getResultList();

                if (determinaciones.size() >= 1){
                    return determinaciones.get(0);
                } else {
                	// Si no se ha encontrado la determinación equivalente a través del vínculo
                    //  con la determinación base, se busca por el nombre y caracter.
                    determinaciones = em.createNamedQuery("Determinacion.obtenerPorTramiteCaracterYNombre")
                    		.setParameter("idTramite", tramite.getIden())
                    		.setParameter("nombre", grupo.getNombre())
                    		.setParameter("idCaracter",grupo.getIdcaracter()).getResultList();
                    if (determinaciones.size()>=1){
                        return determinaciones.get(0);
                    }
                    
                    // Si tampoco se encuentra, se devuelve la determinación base
                    return grupo.getDeterminacionByIddeterminacionbase();
                }
            } else {
            	log.warn("No se ha encontrado grupo de entidades para el trámite " + tramite.getCodigofip());
            }
        } catch (NoResultException nre) {
        	log.warn("No se ha encontrado Determinación Grupo de Entidades para Entidad: " + entidad.getIden()+ " código " + entidad.getCodigo() + " y carácter " + ClsDatos.ID_CARACTER_GRUPODEENTIDADES);
        } catch(NonUniqueResultException nure){
            log.error("Se ha encontrado más de una Determinación Grupo de Entidades para Entidad: " + entidad.getIden()+ " código " + entidad.getCodigo() + " y carácter " + ClsDatos.ID_CARACTER_GRUPODEENTIDADES );
        }

        return null;
    }

	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorConsultasLocal#obtenerDocumentos(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite[])
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> obtenerDocumentos(Tramite[] tramites) {
		String texto;
		List<Object[]> datos;
		List<Object> fila;
		List<Object[]> resultado = new ArrayList<Object[]>();
		for(Tramite tramite : tramites) {
  
            texto= ClsDatos.TEXTO_APORTADAS + "["+tramite.getPlan().getCodigo()+"] " +tramite.getPlan().getNombre();
            
            datos = em.createNamedQuery("Documento.obtenerParaRefundido")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            
            if (datos.size() >0) {
            	for(Object[] array : datos) {
            		fila = new ArrayList<Object>();
            		fila.add(tramite.getIden());
            		fila.add(texto);
            		fila.add(tramite.getPlan().getCodigo());
            		for(Object columna : array) {
            			if (columna != null) {
            				fila.add(columna);
            			} else {
            				fila.add("");
            			}
            		}
            		resultado.add(fila.toArray());
            	}
            }
        }
		return resultado;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorConsultasLocal#obtenerUltimoApartadoDeterminacion(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion, int, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String obtenerUltimoApartadoDeterminacion(Determinacion detPadre, int idTramite, boolean incrementar){
        Query query;
        // Debido a que se ha constatado la imposibilidad de ordenar correctamente los
        //  apartados de forma que se pueda establecer cuál es el último, se opta por
        //  generar el siguiente apartado de todos los leídos hasta que se obtenga uno
        //  que no exista en el listado original.
        // Si el parámetro 'incrementar==false' se devuelve el máximo apartado que
        //  devuelve la consulta, aunque puede no ser el máximo buscado.
        
        if (detPadre==null){
        	query = em.createNamedQuery("Determinacion.obtenerUltimoApartado")
         		   .setParameter("idTramite", idTramite);
        } else {
            query = em.createNamedQuery("Determinacion.obtenerUltimoApartadoConPadre")
         		   .setParameter("idTramite", idTramite).setParameter("idPadre", detPadre.getIden());
        }
        List<String> lista = query.getResultList();
        
        // Por defecto se devuelve el apartado "1"
        String apartado = "1";
		if (lista.size()==0){
             apartado="";
             if (incrementar==true){
                 apartado = siguienteApartadoDeterminacion(apartado);
             }
             return apartado;
             
        } else {
             for (String apt: lista){
                 apartado = siguienteApartadoDeterminacion(apt);
                 if (!lista.contains(apartado)){
                     return apartado;
                 }
             }
        }
        
        // Si no se ha conseguido crear un nuevo apartado a partir de los leídos, se
        //  crea uno nuevo como si fuera el primero.
        if (incrementar==true){
            apartado= siguienteApartadoDeterminacion(apartado);
        }
        return apartado;
    }

	/**
	 * Analiza los caracteres que componen el apartado para, obviando
	 * los signos de puntuación y tomando solamente las letras y
	 * los números, devolver una cadena compuesta por los mismos
	 * caracteres pero incrementando el último en 1.
	 * Dada una cadena, se debe calcular un valor que la identifique
	 * unívocamente, y después de incrementar dicho valor, hay que
	 * construir una cadena que sea su representación.
	 * Se van a intentar identificar tres tipos de caracteres que
	 * puedan ser incrementados:
	 *       1. Una letras suelta (a-z y A-Z)
	 *       2. Números romanos
	 *       3. Números arábigos
	 * Empezando por la derecha, se intenta localizar el primer paquete de
	 * caracteres de cada uno de esos tres tipos. El que resulte estar más a la
	 * derecha, será el que se incremente.
	 * 
	 * @param aptdo
	 * @return
	 */
	private String siguienteApartadoDeterminacion(String aptdo){
		// LOW jgarzon Método no optimizado.
		
        int pos1;
        int pos2;
        int pos3;
        int pos;
        int i;
        int j;
        int n;
        String aptdoTMP=" ";
        String c="ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        String ch;
        String s1="";
        String s2="";
        String s3="";
        int flag;
        int criterio;
        String s;

        try{
            // Si el apartado es una cadena vacía, por defecto se devuelve "1"
            s=aptdo;
            s=s.trim();
            if (s.trim().equalsIgnoreCase("")){
                s="1";
                return s;
            }
            
            aptdoTMP=" " + aptdo.toUpperCase() + " ";

            // 1. Intenta localizar una letra suelta. La condición es que, a ambos lados,
            //  no tenga ninguna otra letra y, además, que no sea un nº romano.
            pos1=-1;
            for (i=aptdoTMP.length()-1;i>0;i--){
                ch=String.valueOf(aptdoTMP.charAt(i-1));
                if (c.contains(ch)){
                    ch=String.valueOf(aptdoTMP.charAt(i));
                    if(!c.contains(ch)){
                        ch=String.valueOf(aptdoTMP.charAt(i-2));
                        if(!c.contains(ch)){
                            pos1=i;
                            s1=String.valueOf(aptdoTMP.charAt(i-1));
                            break;
                        }
                    }
                }
            }

            // 2. Intenta localizar un número romano. Se hace un doble bucle para empezar
            //  con los de un único carácter, después con los de dos, y así sucesivamente
            //  hasta la longitud completa de la cadena.
            pos2=-1;
            for (j=1; j<=aptdoTMP.length()-2;j++){
                flag=0;
                for (i=aptdoTMP.length()-j;i>0;i--){
                    ch=aptdoTMP.substring(i-1, i-1+j);
                    n=Integer.parseInt(numeroArabigoDeRomano(ch));
                    if (n>0){
                        if(!(c.contains(aptdoTMP.substring(i-2, i-2+j)) &&
                                Integer.parseInt(numeroArabigoDeRomano(aptdoTMP.substring(i-2, i-2+j)))==0)){
                            if(!(c.contains(aptdoTMP.substring(i, i+j)) &&
                                    Integer.parseInt(numeroArabigoDeRomano(aptdoTMP.substring(i, i+j)))==0)){
                                s2=ch;
                                pos2=i;
                                flag=1;
                            }
                        }
                    } else{
                        if (pos2>-1 && flag==1){
                            break;
                        }
                    }
                }
            }

            // 3. Intenta localizar un número arábigo, con los mismos criterios que se
            //  han considerado para los romanos.
            pos3=-1;
            for (j=1; j<=aptdoTMP.length()-2;j++){
                flag=0;
                for (i=aptdoTMP.length()-j;i>0;i--){
                    ch=aptdoTMP.substring(i-1, i-1+j);
                    try{
                        n=Integer.parseInt(ch);
                        if (n>0 && flag==0 & (i==(pos3-1) || pos3==-1 )){
                            s3=ch;
                            pos3=i;
                            flag=1;
                            break;
                        }
                    } catch(Exception e){
                        if (pos3>-1 && flag==1){
                            break;
                        }
                    }
                }
            }

            // Selecciona cuál de los paquetes se va a incrementar.
            pos=0;
            criterio=0;
            if(pos1>pos){
                pos=pos1;
                criterio=1;
            }
            if(pos2>pos){
                pos=pos2;
                criterio=2;
            }
            if(pos3>pos){
                pos=pos3;
                criterio=3;
            }

            if (pos==0){
                // No se ha encontrado ninguna secuencia de caracteres que pueda ser incrementada
                //  Al apartado original, se le agrega '.1'
                return aptdo + ".1";
            }

            switch (criterio){
                case 1:
                    // Letra suelta
                    n=-1;
                    for (i=0; i<c.length();i++){
                        if(c.substring(i, i+1).equals(s1)){
                            if(i==c.length()-1){
                                s1="AA";
                            } else{
                                s1=c.substring(i+1, i+2);
                            }
                            // Si la letra original es minúscula, la letra calculada
                            //  se restaura a ese estado.
                            ch=aptdoTMP.substring(pos-1, pos);
                            if(!ch.equals(ch.toUpperCase())){
                                s1=s1.toLowerCase();
                            }
                            break;
                        }
                    }
                    aptdoTMP=aptdoTMP.substring(0,pos-1) + s1 + aptdoTMP.substring(pos);
                    break;

                case 2:
                    // Número romano
                    n=Integer.parseInt(numeroArabigoDeRomano(s2));
                    i=s2.length();
                    n=n+1;
                    s2=numeroRomanoDeArabigo(n);
                    s1=aptdoTMP.substring(pos+i-1);
                    aptdoTMP=aptdoTMP.substring(0,pos-1) + s2 + s1;
                    break;

                case 3:
                    // Numero arábigo
                    n=Integer.parseInt(s3)+1;
                    i=String.valueOf(n).length();
                    aptdoTMP=aptdoTMP.substring(0,pos-1) + String.valueOf(n) + aptdoTMP.substring(pos+i-1);
                    break;

            }
            aptdo=aptdoTMP.trim();

        } catch (Exception e){
            log.error("Fallo en siguienteApartadoDeterminacion. " + e + ". Código 23015." );
            return aptdo + ".1";
        }

        return aptdo;
    }

	private int valorDeCaracterRomano(String c){

        try{
           if (c.equalsIgnoreCase("I")){
               return 1;
           }

           if (c.equalsIgnoreCase("V")){
               return 5;
           }

           if (c.equalsIgnoreCase("X")){
               return 10;
           }

           if (c.equalsIgnoreCase("L")){
               return 50;
           }

           if (c.equalsIgnoreCase("C")){
               return 100;
           }

           if (c.equalsIgnoreCase("D")){
               return 500;
           }

           if (c.equalsIgnoreCase("M")){
               return 1000;
           }

           if (c.equalsIgnoreCase("Q")){
               return 5000;
           }

           if (c.equalsIgnoreCase("H")){
               return 10000;
           }
        } catch (Exception e){
            log.error("Fallo en valorDeCaracterRomano. " + e + ". Código 23017." );
        }

       return -1;
    }

}
