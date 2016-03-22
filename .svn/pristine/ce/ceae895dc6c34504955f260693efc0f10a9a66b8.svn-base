package com.mitc.redes.editorfip.servicios.basicos.diccionario;

import java.util.List;

import org.hibernate.validator.AssertTrue;
import org.hibernate.validator.AssertTrueValidator;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;

public class ServicioBasicoPlanesTest extends SeamTest {

	@Test
	public void findPlanesBase() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<Object[]> lista = (List<Object[]>) invokeMethod("#{servicioBasicoPlanes.findPlanesBase()}");
				Object [] objeto = lista.get(0);
				int iden = (Integer) objeto[0];
				String nombre = (String) objeto[1];
	        	assert lista.size()==1 && iden== 1 && nombre.contains("Directiva de Sistematizaci");
	        	
			}

		}.run();
	}

	@Test
	public void instrumentoString() throws Exception{

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				String instrumento = (String) invokeMethod("#{servicioBasicoPlanes.instrumentoString(1)}");
				
				assert instrumento.contains("n de Errores de PG/NS");
			}

		}.run();

	}

	@Test
	public void obtenerInstrumetos() throws Exception{

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<Object[]> lista = (List<Object[]>) invokeMethod("#{servicioBasicoPlanes.obtenerInstrumetos()}");
				
				assert lista.size()==153;
				
			}

		}.run();

	}

	@Test
	public void obtenerListaInstrumetos() throws Exception{

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<ParIdentificadorTexto> instrumentos= (List<ParIdentificadorTexto>) invokeMethod("#{servicioBasicoPlanes.obtenerListaInstrumetos()}");
				assert instrumentos.size()==153;
			}

		}.run();

	}

	@Test
	public void obtenerListaTipoTramites() throws Exception{

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<ParIdentificadorTexto> tiposTramite= (List<ParIdentificadorTexto>) invokeMethod("#{servicioBasicoPlanes.obtenerListaTipoTramites()}");
				assert tiposTramite.size()==18;
			}

		}.run();

	}

	@Test
	public void obtenerPlanesVigentesDeAmbito() throws Exception{

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<Object[]> lista = (List<Object[]>) invokeMethod("#{servicioBasicoPlanes.obtenerPlanesVigentesDeAmbito(3961)}");
				Object[] objeto = lista.get(0);
				int iden = (Integer)objeto[0];
				String nombre = (String) objeto[1];
				assert lista.size()==2 && iden == 2736 && nombre.equalsIgnoreCase("Plan prerefundido de <Herrera> 2011-12-01");
				
			}

		}.run();

	}

	@Test
	public void obtenerTipoTramites() throws Exception{

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<Object[]> lista = (List<Object[]>) invokeMethod("#{servicioBasicoPlanes.obtenerTipoTramites()}");
				assert lista.size()==18;
			}

		}.run();

	}


	@Test
	public void tipoTramiteString() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				String nombre = (String) invokeMethod("#{servicioBasicoPlanes.tipoTramiteString(5)}");
				nombre.equalsIgnoreCase("Acuerdo Cumpl. Condiciones");
			}

		}.run();

	}
}
