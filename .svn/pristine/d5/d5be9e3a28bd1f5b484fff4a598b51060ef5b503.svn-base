package com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.jboss.seam.Component;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.AbstractSeamTest.ComponentTest;
import org.testng.annotations.Test;

import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegulacionEspecificaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;

public class ServicioBasicoDeterminacionesTest extends SeamTest {

  @Test
  public void buscarDeterminacionPorTramiteYOrden() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				int id = (Integer) invokeMethod("#{servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(752, '01.01')}");
	        	assert id>0;
			}

		}.run();
	}

  @Test
  public void buscarRegulacionEspecifica() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				RegulacionEspecificaDTO dto = (RegulacionEspecificaDTO) invokeMethod("#{servicioBasicoDeterminaciones.buscarRegulacionEspecifica(23932)}");
				System.out.println("dsadsadsa" + dto.getTexto());
	        	assert dto.getTexto().contains("Es el que, obligatoriamente, debe acompa");
			}

		}.run();
	}

  @Test
  public void caracterString() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				String dto = (String) invokeMethod("#{servicioBasicoDeterminaciones.caracterString(18)}");
	        	assert dto.equalsIgnoreCase("Unidad");
			}

		}.run();
	}

  @Test
  public void editarRegulacionespecifica() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoDeterminaciones sbd = (ServicioBasicoDeterminaciones) Component.getInstance("servicioBasicoDeterminaciones", true);
				RegulacionEspecificaDTO dto = (RegulacionEspecificaDTO) invokeMethod("#{servicioBasicoDeterminaciones.buscarRegulacionEspecifica(23932)}");
				
				boolean res = (Boolean) sbd.editarRegulacionespecifica(dto);
				assert res;
			}

		}.run();
	}

  @Test
  public void findByBaseGrupo() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				Determinacion det = (Determinacion) invokeMethod("#{servicioBasicoDeterminaciones.findByBaseGrupo(1, '7000000076')}");
	        	assert det.getNombre().equalsIgnoreCase("Operador (lineal)");
			}

		}.run();
	}

  @Test
  public void findByNombreTramite() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				Determinacion det = (Determinacion) invokeMethod("#{servicioBasicoDeterminaciones.findByNombreTramite(1, 'Sistemas')}");
				assert det!=null;
			}

		}.run();
	}

  @Test
  public void findCaracterTexto() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<Object[]> lista = (List<Object[]>) invokeMethod("#{servicioBasicoDeterminaciones.findCaracterTexto()}");
				assert lista.size()>0;
			}

		}.run();
	}

  @Test
  public void findCaracterTextoSelectItem() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<SelectItem> lista = (List<SelectItem>) invokeMethod("#{servicioBasicoDeterminaciones.findCaracterTextoSelectItem()}");
				assert lista.size()>0;
			}

		}.run();
	}

  @Test
  public void getArrayGruposAplicacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				Determinacion[] lista = (Determinacion[]) invokeMethod("#{servicioBasicoDeterminaciones.getArrayGruposAplicacion(4640)}");
				assert lista.length==0;
			}

		}.run();
	}

  @Test
  public void getArrayReguladoras() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				Determinacion[] lista = (Determinacion[]) invokeMethod("#{servicioBasicoDeterminaciones.getArrayReguladoras(4640)}");
				assert lista.length==0;
			}

		}.run();
	}

  @Test
  public void getArrayValoresReferencia() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				Determinacion[] lista = (Determinacion[]) invokeMethod("#{servicioBasicoDeterminaciones.getArrayValoresReferencia(4640)}");
				assert lista.length==0;
			}

		}.run();
	}

  @Test
  public void getDeterminacionesHijasDeDet() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<ParIdentificadorTexto> lista = (List<ParIdentificadorTexto>) invokeMethod("#{servicioBasicoDeterminaciones.getDeterminacionesHijasDeDet(35120237)}");
				assert lista.size()>0;
			}

		}.run();
	}

  @Test
  public void getDeterminacionesRaices() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<ParIdentificadorTexto> lista = (List<ParIdentificadorTexto>) invokeMethod("#{servicioBasicoDeterminaciones.getDeterminacionesRaices(752)}");
				assert lista.size()>0;
			}
		}.run();
	}

  @Test
  public void getIdentificadorCaracter() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				int id = (Integer) invokeMethod("#{servicioBasicoDeterminaciones.getIdentificadorCaracter('Unidad')}");
				assert id>0;
			}

		}.run();
	}

  @Test
  public void getListDeterminacionRegimenDeTramite() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<DeterminacionDTO> l = (List<DeterminacionDTO>) invokeMethod("#{servicioBasicoDeterminaciones.getListDeterminacionRegimenDeTramite(752)}");
				assert l.size()>0;
			}

		}.run();
	}

  @Test
  public void getListValoresReferenciaDeDeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<DeterminacionDTO> l = (List<DeterminacionDTO>) invokeMethod("#{servicioBasicoDeterminaciones.getListValoresReferenciaDeDeterminacion(192378)}");
				assert l.size()>0;
			}

		}.run();
	}

  @Test
  public void getRegulacionespecificaHijas() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<ParIdentificadorTexto> l = (List<ParIdentificadorTexto>) invokeMethod("#{servicioBasicoDeterminaciones.getRegulacionespecificaHijas(36)}");
				assert l.size()==0;
			}

		}.run();
	}

  @Test
  public void getRegulacionespecificaRaices() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				List<ParIdentificadorTexto> l = (List<ParIdentificadorTexto>) invokeMethod("#{servicioBasicoDeterminaciones.getRegulacionespecificaRaices(36)}");
				assert l.size()==0;
			}

		}.run();
	}

  @Test
  public void grupoDocumentoNombre() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				String l = (String) invokeMethod("#{servicioBasicoDeterminaciones.grupoDocumentoNombre(1)}");
				assert l.equalsIgnoreCase("Textos Normativos Propios");
			}

		}.run();
	}

  @Test
  public void guardarDeterminacionReguladoraDeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				invokeMethod("#{servicioBasicoDeterminaciones.guardarDeterminacionReguladoraDeterminacion(36,42)}");
				assert true;
			}

		}.run();
	}

  @Test
  public void guardarGrupoAplicacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				invokeMethod("#{servicioBasicoDeterminaciones.guardarGrupoAplicacion(36, 42)}");
				assert true;
			}

		}.run();
	}

  @Test
  public void guardarUnidadDeDeterminacion() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				invokeMethod("#{servicioBasicoDeterminaciones.guardarUnidadDeDeterminacion(36, 42)}");
				assert true;
			}

		}.run();
	}

  @Test
  public void guardarValoresReferencia() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoDeterminaciones sbd = (ServicioBasicoDeterminaciones) Component.getInstance("servicioBasicoDeterminaciones", true);
				List<Integer> idValoresReferencia = new ArrayList<Integer>();
				idValoresReferencia.add(42);
				sbd.guardarValoresReferencia(36, idValoresReferencia);
				assert true;
			}

		}.run();
	}

  @Test
  public void nextCodigo() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				String l = (String) invokeMethod("#{servicioBasicoDeterminaciones.nextCodigo(752)}");
				assert !(l.equalsIgnoreCase(""));
			}

		}.run();
	}

  @Test
  public void obtenerArrayDocumentoDeterminacionDTO() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				DocumentoDTO[] l = (DocumentoDTO[]) invokeMethod("#{servicioBasicoDeterminaciones.obtenerArrayDocumentoDeterminacionDTO(36)}");
				assert l.length==0;
			}

		}.run();
	}


  @Test
  public void tipoDocumentoNombre() throws Exception {

		new ComponentTest() {

			@Override
			protected void testComponents() throws Exception {
				ServicioBasicoDeterminaciones sbd = (ServicioBasicoDeterminaciones) Component.getInstance("servicioBasicoDeterminaciones", true);
				String res = sbd.tipoDocumentoNombre(4);
				assert !(res.equalsIgnoreCase(""));
			}

		}.run();
	}
}
