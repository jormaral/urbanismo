package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;

@Name("vectorrelacionHome")
public class VectorrelacionHome extends EntityHome<Vectorrelacion> {

	@In(create = true)
	RelacionHome relacionHome;

	public void setVectorrelacionIden(Integer id) {
		setId(id);
	}

	public Integer getVectorrelacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Vectorrelacion createInstance() {
		Vectorrelacion vectorrelacion = new Vectorrelacion();
		return vectorrelacion;
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

	public Vectorrelacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
