package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Conjuntoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

@Name("conjuntoentidadHome")
public class ConjuntoentidadHome extends EntityHome<Conjuntoentidad> {

	@In(create = true)
	EntidadHome entidadHome;

	public void setConjuntoentidadIden(Integer id) {
		setId(id);
	}

	public Integer getConjuntoentidadIden() {
		return (Integer) getId();
	}

	@Override
	protected Conjuntoentidad createInstance() {
		Conjuntoentidad conjuntoentidad = new Conjuntoentidad();
		return conjuntoentidad;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Entidad entidadByIdentidadconjunto = entidadHome.getDefinedInstance();
		if (entidadByIdentidadconjunto != null) {
			getInstance().setEntidadByIdentidadconjunto(
					entidadByIdentidadconjunto);
		}
		Entidad entidadByIdentidadmiembro = entidadHome.getDefinedInstance();
		if (entidadByIdentidadmiembro != null) {
			getInstance().setEntidadByIdentidadmiembro(
					entidadByIdentidadmiembro);
		}
	}

	public boolean isWired() {
		if (getInstance().getEntidadByIdentidadconjunto() == null)
			return false;
		if (getInstance().getEntidadByIdentidadmiembro() == null)
			return false;
		return true;
	}

	public Conjuntoentidad getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
