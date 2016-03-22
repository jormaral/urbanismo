package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Boletintramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Name("boletintramiteHome")
public class BoletintramiteHome extends EntityHome<Boletintramite> {

	@In(create = true)
	TramiteHome tramiteHome;

	public void setBoletintramiteIden(Integer id) {
		setId(id);
	}

	public Integer getBoletintramiteIden() {
		return (Integer) getId();
	}

	@Override
	protected Boletintramite createInstance() {
		Boletintramite boletintramite = new Boletintramite();
		return boletintramite;
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

	public Boletintramite getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
