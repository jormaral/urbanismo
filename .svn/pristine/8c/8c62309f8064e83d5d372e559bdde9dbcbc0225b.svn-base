package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Ambitoaplicacionambito;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

@Name("ambitoaplicacionambitoHome")
public class AmbitoaplicacionambitoHome
		extends
			EntityHome<Ambitoaplicacionambito> {

	@In(create = true)
	EntidadHome entidadHome;

	public void setAmbitoaplicacionambitoIden(Integer id) {
		setId(id);
	}

	public Integer getAmbitoaplicacionambitoIden() {
		return (Integer) getId();
	}

	@Override
	protected Ambitoaplicacionambito createInstance() {
		Ambitoaplicacionambito ambitoaplicacionambito = new Ambitoaplicacionambito();
		return ambitoaplicacionambito;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Entidad entidad = entidadHome.getDefinedInstance();
		if (entidad != null) {
			getInstance().setEntidad(entidad);
		}
	}

	public boolean isWired() {
		if (getInstance().getEntidad() == null)
			return false;
		return true;
	}

	public Ambitoaplicacionambito getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
