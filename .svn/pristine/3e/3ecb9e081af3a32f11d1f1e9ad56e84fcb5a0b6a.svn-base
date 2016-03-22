package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;

@Name("entidaddeterminacionHome")
public class EntidaddeterminacionHome extends EntityHome<Entidaddeterminacion> {

	@In(create = true)
	DeterminacionHome determinacionHome;
	@In(create = true)
	EntidadHome entidadHome;

	public void setEntidaddeterminacionIden(Integer id) {
		setId(id);
	}

	public Integer getEntidaddeterminacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Entidaddeterminacion createInstance() {
		Entidaddeterminacion entidaddeterminacion = new Entidaddeterminacion();
		return entidaddeterminacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Determinacion determinacion = determinacionHome.getDefinedInstance();
		if (determinacion != null) {
			getInstance().setDeterminacion(determinacion);
		}
		Entidad entidad = entidadHome.getDefinedInstance();
		if (entidad != null) {
			getInstance().setEntidad(entidad);
		}
	}

	public boolean isWired() {
		if (getInstance().getDeterminacion() == null)
			return false;
		if (getInstance().getEntidad() == null)
			return false;
		return true;
	}

	public Entidaddeterminacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Casoentidaddeterminacion> getCasoentidaddeterminacions() {
		return getInstance() == null
				? null
				: new ArrayList<Casoentidaddeterminacion>(getInstance()
						.getCasoentidaddeterminacions());
	}
	public List<Casoentidaddeterminacion> getCasoentidaddeterminacions_1() {
		return getInstance() == null
				? null
				: new ArrayList<Casoentidaddeterminacion>(getInstance()
						.getCasoentidaddeterminacions_1());
	}

}
