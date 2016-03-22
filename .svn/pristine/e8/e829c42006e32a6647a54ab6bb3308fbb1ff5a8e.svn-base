package com.mitc.redes.editorfip.servicios.basicos.fip.entidades;

import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaEntidadDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

public class ServicioBasicoEntidadesTest extends SeamTest{


  @Test
  public void buscarEntidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoEntidades sbd = (ServicioBasicoEntidades) Component.getInstance("servicioBasicoEntidades", true);
				Entidad res = sbd.buscarEntidad(10);
			
				assert res!=null;
			}

		}.run();
	}

  @Test
  public void getEntidadesCUHijas() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoEntidades sbd = (ServicioBasicoEntidades) Component.getInstance("servicioBasicoEntidades", true);
				List<ParIdentificadorTexto> res = sbd.getEntidadesCUHijas(2586);
			
				assert res.size()>0;
			}

		}.run();
	}

  @Test
  public void getEntidadesCURaices() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoEntidades sbd = (ServicioBasicoEntidades) Component.getInstance("servicioBasicoEntidades", true);
				List<ParIdentificadorTexto> res = sbd.getEntidadesCURaices(752);
			
				assert res.size()>0;
			}

		}.run();
	}


  @Test
  public void getEntidadesRaices() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoEntidades sbd = (ServicioBasicoEntidades) Component.getInstance("servicioBasicoEntidades", true);
				List<ParIdentificadorTexto> res = sbd.getEntidadesRaices(752);
			
				assert res.size()>0;
			}

		}.run();
	}

  @Test
  public void nextCodigo() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoEntidades sbd = (ServicioBasicoEntidades) Component.getInstance("servicioBasicoEntidades", true);
				String res = sbd.nextCodigo("752");
			
				assert (res!=null && !res.equals(""));
			}

		}.run();
	}


  @Test
  public void obtenerOrdenNuevaEntidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoEntidades sbd = (ServicioBasicoEntidades) Component.getInstance("servicioBasicoEntidades", true);
				int res = sbd.obtenerOrdenNuevaEntidad(752, 0);
			
				assert res>0;
			}

		}.run();
	}

  @Test
  public void tieneEntidadTramite() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoEntidades sbd = (ServicioBasicoEntidades) Component.getInstance("servicioBasicoEntidades", true);
				boolean res = sbd.tieneEntidadTramite(752);
			
				assert res;
			}

		}.run();
	}
}
