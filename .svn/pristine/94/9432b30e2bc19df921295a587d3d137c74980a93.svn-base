package com.mitc.redes.editorfip.servicios.basicos.fip.entidades;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;

public class ServicioBasicoCondicionesUrbanisticasTest extends SeamTest {

  @Test
  public void actualizarCU() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				Entidaddeterminacionregimen edr = new Entidaddeterminacionregimen();
				List<CondicionUrbanisticaSimplificadaDTO> cuSimplList = new ArrayList<CondicionUrbanisticaSimplificadaDTO> ();
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				
				cuSimplList = sbd.listadoTodasCUSimplificadaDeEntidad(139066);
				int res = sbd.actualizarCU(cuSimplList.get(0));
				assert res>0;
			}

		}.run();
	}

  @Test
  public void borrarRegimen() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				boolean res = sbd.borrarRegimen(35132525);
				assert !res;
			}

		}.run();
	}
  
  @Test
  public void crearCUGrupoAplicacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {	
				List<CondicionUrbanisticaSimplificadaDTO> cuSimplList = new ArrayList<CondicionUrbanisticaSimplificadaDTO> ();
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				
				cuSimplList = sbd.listadoTodasCUSimplificadaDeEntidad(139066);
				int res = sbd.crearCUGrupoAplicacion(cuSimplList.get(0));
				
				assert res>0;
			}
		}.run();
	}

  @Test
  public void entidadesSinGrupoAplicacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				int entidades = (Integer) invokeMethod("#{servicioBasicoCondicionesUrbanisticas.entidadesSinGrupoAplicacion(1)}");
				System.out.println(entidades);
				assert entidades==0;
			}

		}.run();
	}

  @Test
  public void nombreDeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				String nombre = sbd.nombreDeterminacion(18);
				
				assert nombre.equalsIgnoreCase("Sistema de Servicios Urbanos");
			}

		}.run();
	}

  @Test
  public void nombreEntidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				String nombre = sbd.nombreEntidad(10);
				assert nombre.contains("Zona de Especial Protecci");
			}

		}.run();
	}

  @Test
  public void obtenerDeterminacionesAplicablesComoCU() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				List<DeterminacionDTO> nombre = sbd.obtenerDeterminacionesAplicablesComoCU(10);
				System.out.println("obtenerDeterminacionesAplicablesComoCU" + nombre.size());
				assert nombre.size()==0;
			}

		}.run();
	}

  @Test
  public void obtenerIdDeterminacionGrupoAplicacionDeTramite() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				int res = sbd.obtenerIdDeterminacionGrupoAplicacionDeTramite(752);
				System.out.println("obtenerIdDeterminacionGrupoAplicacionDeTramite" + res);
				assert res!=0;
			}

		}.run();
	}

  @Test
  public void obtenerVRdeCUdeEntidadAplicadaAGA() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				int res = sbd.obtenerVRdeCUdeEntidadAplicadaAGA(10);
				System.out.println("obtenerVRdeCUdeEntidadAplicadaAGA" + res);
				assert res>0;
			}

		}.run();
	}

  @Test
  public void resultadosBusquedaAvanzadaCondicion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				FiltrosDTO filtros = new FiltrosDTO();
				filtros.setTipoFiltro("or");
				List<CondicionUrbanisticaSimplificadaDTO> res = sbd.resultadosBusquedaAvanzadaCondicion(filtros, 752);
				System.out.println("resultadosBusquedaAvanzadaCondicion" + res.size());
				assert res.size()!=0;
			}

		}.run();
	}

  @Test
  public void tieneAsignadaEntidadGrupoAplicacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoCondicionesUrbanisticas sbd = (ServicioBasicoCondicionesUrbanisticas) Component.getInstance("servicioBasicoCondicionesUrbanisticas", true);
				
				boolean res = sbd.tieneAsignadaEntidadGrupoAplicacion(10);
				System.out.println("tieneAsignadaEntidadGrupoAplicacion" + res);
				assert res;
			}

		}.run();
	}
}
