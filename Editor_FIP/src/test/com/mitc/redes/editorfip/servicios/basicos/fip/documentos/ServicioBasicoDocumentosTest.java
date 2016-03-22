package com.mitc.redes.editorfip.servicios.basicos.fip.documentos;

import java.util.List;

import javax.faces.model.SelectItem;

import org.jboss.seam.Component;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaDocumentoDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegulacionEspecificaDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;

public class ServicioBasicoDocumentosTest extends SeamTest {

  @Test
  public void grupoDocumentoList() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				System.out.println("grupoDocumentoList");
				List<SelectItem> l = (List<SelectItem>) invokeMethod("#{servicioBasicoDocumentos.grupoDocumentoList()}");
				assert l.size()>0;
			}

		}.run();
	}

  @Test
  public void resultadosBusquedaAvanzadaDocumento() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				System.out.println("resultadosBusquedaAvanzadaDocumento");
				FiltrosDTO filtros = new FiltrosDTO();
				filtros.setTitulo("a");
				filtros.setTipoFiltro("and");
				ServicioBasicoDocumentos sbd = (ServicioBasicoDocumentos) Component.getInstance("servicioBasicoDocumentos", true);
				
				List<BusquedaDocumentoDTO> res = (List<BusquedaDocumentoDTO>) sbd.resultadosBusquedaAvanzadaDocumento(filtros, 759);
				assert res.size()>0;
			}

		}.run();
	}

  @Test
  public void tiposDocumentoList() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				System.out.println("tiposDocumentoList");
				List<SelectItem> l = (List<SelectItem>) invokeMethod("#{servicioBasicoDocumentos.tiposDocumentoList()}");
				assert l.size()>0;
			}

		}.run();
	}
}
