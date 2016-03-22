package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;

@Name("entidadlinHome")
public class EntidadlinHome extends EntityHome<Entidadlin> {

	@In(create = true)
	EntidadHome entidadHome;

	public void setEntidadlinIden(Integer id) {
		setId(id);
	}

	public Integer getEntidadlinIden() {
		return (Integer) getId();
	}

	@Override
	protected Entidadlin createInstance() {
		Entidadlin entidadlin = new Entidadlin();
		return entidadlin;
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
		return true;
	}

	public Entidadlin getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
