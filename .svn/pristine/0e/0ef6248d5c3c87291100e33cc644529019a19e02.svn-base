package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;

@Name("entidadpntHome")
public class EntidadpntHome extends EntityHome<Entidadpnt> {

	@In(create = true)
	EntidadHome entidadHome;

	public void setEntidadpntIden(Integer id) {
		setId(id);
	}

	public Integer getEntidadpntIden() {
		return (Integer) getId();
	}

	@Override
	protected Entidadpnt createInstance() {
		Entidadpnt entidadpnt = new Entidadpnt();
		return entidadpnt;
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

	public Entidadpnt getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
