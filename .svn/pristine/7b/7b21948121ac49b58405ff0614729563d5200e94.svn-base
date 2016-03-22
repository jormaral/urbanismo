package com.mitc.redes.editorfip.servicios.basicos.fip.operaciones;

import org.jboss.seam.Component;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;

public class ServicioBasicoOperacionesTest extends SeamTest {

  @Test
  public void createOperacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				
				ServicioBasicoOperaciones sbd = (ServicioBasicoOperaciones) Component.getInstance("servicioBasicoOperaciones", true);
				Operacion operacion = sbd.findOperacion(6150);
				operacion.setIden(-1);
				sbd.createOperacion(operacion);
			
				assert true;
			}

		}.run();
	}

  @Test
  public void createOperaciondeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoOperaciones sbd = (ServicioBasicoOperaciones) Component.getInstance("servicioBasicoOperaciones", true);
				ServicioBasicoDeterminaciones sbdet = (ServicioBasicoDeterminaciones) Component.getInstance("servicioBasicoDeterminaciones", true);
				Determinacion det1 = sbdet.buscarDeterminacion(31);
				Determinacion det2 = sbdet.buscarDeterminacion(32);
				Operacion operacion = sbd.findOperacion(6150);
				Operaciondeterminacion opd = new Operaciondeterminacion();
				opd.setOperacion(operacion);
				opd.setDeterminacionByIddeterminacion(det1);
				opd.setDeterminacionByIddeterminacionoperadora(det2);
				
				sbd.createOperaciondeterminacion(opd);
			
				assert true;
			}

		}.run();
	}

  @Test
  public void createOperacionentidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoOperaciones sbd = (ServicioBasicoOperaciones) Component.getInstance("servicioBasicoOperaciones", true);
				ServicioBasicoEntidades sbdet = (ServicioBasicoEntidades) Component.getInstance("servicioBasicoEntidades", true);
				Entidad det1 = sbdet.buscarEntidad(31);
				Entidad det2 = sbdet.buscarEntidad(32);
				Operacion operacion = sbd.findOperacion(6150);
				Operacionentidad opd = new Operacionentidad();
				opd.setOperacion(operacion);
				opd.setEntidadByIdentidad(det1);
				opd.setEntidadByIdentidadoperadora(det2);
				
				sbd.createOperacionentidad(opd);
			}

		}.run();
	}

  @Test
  public void editOperacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoOperaciones sbd = (ServicioBasicoOperaciones) Component.getInstance("servicioBasicoOperaciones", true);
				Operacion operacion = sbd.findOperacion(6150);
				sbd.editOperacion(operacion);
			
				assert true;
			}

		}.run();
	}

  @Test
  public void editOperaciondeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void editOperacionentidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void findAllOperacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void findAllOperaciondeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void findAllOperacionentidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void findOperacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void findOperaciondeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void findOperacionentidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void obtenerListaOperacionDeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void obtenerListaOperacionDeterminacionDTO() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void obtenerListaOperacionEntidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void obtenerListaOperacionEntidadDTO() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void obtenerOperacionDeterminacionDTO() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void obtenerOperacionEntidadDTO() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void removeOperacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void removeOperaciondeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void removeOperacionentidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void resultadosBusquedaAvanzadaOperacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void tipoOperacionDetId() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void tipoOperacionDetNombre() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void tipoOperacionDeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void tipoOperacionEntId() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void tipoOperacionEntidad() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}

  @Test
  public void tipoOperacionEntidadNombre() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				// TODO
				// Rellenar con el test de este metodo
			}

		}.run();
		throw new RuntimeException("Test not implemented");
	}
}
