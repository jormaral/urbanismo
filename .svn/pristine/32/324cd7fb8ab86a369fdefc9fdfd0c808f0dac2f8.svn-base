package com.mitc.redes.editorfip.servicios.basicos.fip.documentos;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;

public class ServicioBasicoOpcionDeterminacionTest extends SeamTest {

  @Test
  public void crearValorRefenciaDeterminacionPadre() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoOpcionDeterminacion sbod = (ServicioBasicoOpcionDeterminacion) Component.getInstance("servicioBasicoOpcionDeterminacion", true);
				ServicioBasicoDeterminaciones sbd = (ServicioBasicoDeterminaciones) Component.getInstance("servicioBasicoDeterminaciones", true);
				System.out.println("crearValorRefenciaDeterminacionPadre");
				Determinacion det = sbd.buscarDeterminacion(36);
				
				assert sbod.crearValorRefenciaDeterminacionPadre(det, 42);
			}

		}.run();
	}

  @Test
  public void removeByDeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoOpcionDeterminacion sbod = (ServicioBasicoOpcionDeterminacion) Component.getInstance("servicioBasicoOpcionDeterminacion", true);
				sbod.removeByDeterminacion(36);
				assert true;
			}

		}.run();
	}
}
