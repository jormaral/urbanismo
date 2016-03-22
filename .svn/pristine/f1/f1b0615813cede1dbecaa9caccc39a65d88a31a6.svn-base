package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentocaso;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Name("documentoHome")
public class DocumentoHome extends EntityHome<Documento> {

	@In(create = true)
	DocumentoHome documentoHome;
	@In(create = true)
	TramiteHome tramiteHome;

	public void setDocumentoIden(Integer id) {
		setId(id);
	}

	public Integer getDocumentoIden() {
		return (Integer) getId();
	}

	@Override
	protected Documento createInstance() {
		Documento documento = new Documento();
		return documento;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Tramite tramite = tramiteHome.getDefinedInstance();
		if (tramite != null) {
			getInstance().setTramite(tramite);
		}
	}

	public boolean isWired() {
		if (getInstance().getTramite() == null)
			return false;
		return true;
	}

	public Documento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Documentocaso> getDocumentocasos() {
		return getInstance() == null ? null : new ArrayList<Documentocaso>(
				getInstance().getDocumentocasos());
	}
	public List<Documentocaso> getDocumentocasos_1() {
		return getInstance() == null ? null : new ArrayList<Documentocaso>(
				getInstance().getDocumentocasos_1());
	}
	public List<Documentodeterminacion> getDocumentodeterminacions() {
		return getInstance() == null
				? null
				: new ArrayList<Documentodeterminacion>(getInstance()
						.getDocumentodeterminacions());
	}
	public List<Documentodeterminacion> getDocumentodeterminacions_1() {
		return getInstance() == null
				? null
				: new ArrayList<Documentodeterminacion>(getInstance()
						.getDocumentodeterminacions_1());
	}
	public List<Documentoentidad> getDocumentoentidads() {
		return getInstance() == null ? null : new ArrayList<Documentoentidad>(
				getInstance().getDocumentoentidads());
	}
	public List<Documentoentidad> getDocumentoentidads_1() {
		return getInstance() == null ? null : new ArrayList<Documentoentidad>(
				getInstance().getDocumentoentidads_1());
	}
	public List<Documento> getDocumentos() {
		return getInstance() == null ? null : new ArrayList<Documento>(
				getInstance().getDocumentos());
	}
	public List<Documento> getDocumentos_1() {
		return getInstance() == null ? null : new ArrayList<Documento>(
				getInstance().getDocumentos_1());
	}
	public List<Documentoshp> getDocumentoshps() {
		return getInstance() == null ? null : new ArrayList<Documentoshp>(
				getInstance().getDocumentoshps());
	}
	public List<Documentoshp> getDocumentoshps_1() {
		return getInstance() == null ? null : new ArrayList<Documentoshp>(
				getInstance().getDocumentoshps_1());
	}

}
