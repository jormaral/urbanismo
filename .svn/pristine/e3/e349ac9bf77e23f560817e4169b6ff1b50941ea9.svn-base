package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;

@Name("entidadpolHome")
public class EntidadpolHome extends EntityHome<Entidadpol> {

	@In(create = true)
	EntidadHome entidadHome;

	public void setEntidadpolIden(Integer id) {
		setId(id);
	}

	public Integer getEntidadpolIden() {
		return (Integer) getId();
	}

	@Override
	protected Entidadpol createInstance() {
		Entidadpol entidadpol = new Entidadpol();
		return entidadpol;
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

	public Entidadpol getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
