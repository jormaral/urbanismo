package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

@Name("documentoentidadHome")
public class DocumentoentidadHome extends EntityHome<Documentoentidad> {

	@In(create = true)
	DocumentoHome documentoHome;
	@In(create = true)
	EntidadHome entidadHome;

	public void setDocumentoentidadIden(Integer id) {
		setId(id);
	}

	public Integer getDocumentoentidadIden() {
		return (Integer) getId();
	}

	@Override
	protected Documentoentidad createInstance() {
		Documentoentidad documentoentidad = new Documentoentidad();
		return documentoentidad;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Documento documento = documentoHome.getDefinedInstance();
		if (documento != null) {
			getInstance().setDocumento(documento);
		}
		Entidad entidad = entidadHome.getDefinedInstance();
		if (entidad != null) {
			getInstance().setEntidad(entidad);
		}
	}

	public boolean isWired() {
		if (getInstance().getDocumento() == null)
			return false;
		if (getInstance().getEntidad() == null)
			return false;
		return true;
	}

	public Documentoentidad getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
