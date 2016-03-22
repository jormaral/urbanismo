package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentocaso;

@Name("documentocasoHome")
public class DocumentocasoHome extends EntityHome<Documentocaso> {

	@In(create = true)
	CasoentidaddeterminacionHome casoentidaddeterminacionHome;
	@In(create = true)
	DocumentoHome documentoHome;

	public void setDocumentocasoIden(Integer id) {
		setId(id);
	}

	public Integer getDocumentocasoIden() {
		return (Integer) getId();
	}

	@Override
	protected Documentocaso createInstance() {
		Documentocaso documentocaso = new Documentocaso();
		return documentocaso;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Casoentidaddeterminacion casoentidaddeterminacion = casoentidaddeterminacionHome
				.getDefinedInstance();
		if (casoentidaddeterminacion != null) {
			getInstance().setCasoentidaddeterminacion(casoentidaddeterminacion);
		}
		Documento documento = documentoHome.getDefinedInstance();
		if (documento != null) {
			getInstance().setDocumento(documento);
		}
	}

	public boolean isWired() {
		if (getInstance().getCasoentidaddeterminacion() == null)
			return false;
		if (getInstance().getDocumento() == null)
			return false;
		return true;
	}

	public Documentocaso getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
