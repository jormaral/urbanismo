package com.mitc.redes.editorfip.servicios.basicos.fip.unidades;

import java.util.List;

import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadDTO;

public class ServicioBasicoCrearUnidadesTest extends SeamTest {

 
  @Test
  public void a_crearUnidad() throws Exception {
	  new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				int creada = (Integer) invokeMethod("#{servicioBasicoUnidades.crearUnidad(4628, 'Prueba', 'Unidad de prueba')}");
	        	assert creada > 0;	
			}

		}.run();
  }
  
}
