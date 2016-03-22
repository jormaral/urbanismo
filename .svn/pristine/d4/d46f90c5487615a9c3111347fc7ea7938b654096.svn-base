package com.mitc.redes.editorfip.servicios.basicos.diccionario;

import java.util.List;

import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;

public class ServicioBasicoAmbitosTest extends SeamTest {

  @Test
  public void ambitoString() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				String amb = (String) invokeMethod("#{servicioBasicoAmbitos.ambitoString(3222)}");
	        	assert amb.equalsIgnoreCase("ferrol");
			}

		}.run();
	}


  @Test
  public void findAmbitos() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<ParIdentificadorTexto> ambitos = (List<ParIdentificadorTexto>) invokeMethod("#{servicioBasicoAmbitos.findAmbitos()}");
				assert ambitos.size()==8310 && ambitos.get(0).getTexto().equalsIgnoreCase("01 Centro");
			}

		}.run();
	}


  @Test
  public void obtenerAmbitosHijos() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<ParIdentificadorTexto> ambitos = (List<ParIdentificadorTexto>) invokeMethod("#{servicioBasicoAmbitos.obtenerAmbitosHijos(1)}");
				assert ambitos.size()==50;
			}

		}.run();
	}


  @Test
  public void obtenerAmbitosRaices() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<ParIdentificadorTexto> ambitos = (List<ParIdentificadorTexto>) invokeMethod("#{servicioBasicoAmbitos.obtenerAmbitosRaices()}");
				assert ambitos.size()== 1;
			}

		}.run();
	}


 

}
