package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Propiedadrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;

@Name("propiedadrelacionHome")
public class PropiedadrelacionHome extends EntityHome<Propiedadrelacion> {

	@In(create = true)
	RelacionHome relacionHome;

	public void setPropiedadrelacionIden(Integer id) {
		setId(id);
	}

	public Integer getPropiedadrelacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Propiedadrelacion createInstance() {
		Propiedadrelacion propiedadrelacion = new Propiedadrelacion();
		return propiedadrelacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Relacion relacion = relacionHome.getDefinedInstance();
		if (relacion != null) {
			getInstance().setRelacion(relacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getRelacion() == null)
			return false;
		return true;
	}

	public Propiedadrelacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
