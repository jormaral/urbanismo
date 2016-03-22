package com.mitc.redes.editorfip.servicios.basicos.fip.unidades;

import java.util.List;

import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadDTO;

public class ServicioBasicoUnidadesTest extends SeamTest {

    
  @Test
  public void c_obtenerListaUnidadesTramite() throws Exception {
	  new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List <UnidadDTO> listaUnidades = (List <UnidadDTO>) invokeMethod("#{servicioBasicoUnidades.obtenerListaUnidadesTramite(1)}");
	        	assert listaUnidades.size()==8;	
			}

		}.run();
  }

  @Test
  public void b_obtenerUnidadDeDeterminacion() throws Exception {
	  new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				UnidadDTO unidad = (UnidadDTO) invokeMethod("#{servicioBasicoUnidades.obtenerUnidadDeDeterminacion(4628)}");
	        	assert !unidad.getAbreviatura().equalsIgnoreCase("");
	        	
			}

		}.run();
  }
  
  //Hay que comprobar el método, no deja ejecutar el test
  @Test
  public void d_borrarUnidadDeDeterminacion() throws Exception {
	  new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				int borrado = (Integer) invokeMethod("#{servicioBasicoUnidades.borrarUnidadDeDeterminacion(4628)}");
	        	assert borrado > 0;
			}

		}.run();
  }
}
