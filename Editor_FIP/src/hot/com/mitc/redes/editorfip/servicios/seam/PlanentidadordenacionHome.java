package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planentidadordenacion;

@Name("planentidadordenacionHome")
public class PlanentidadordenacionHome
		extends
			EntityHome<Planentidadordenacion> {

	@In(create = true)
	EntidadHome entidadHome;
	@In(create = true)
	PlanHome planHome;

	public void setPlanentidadordenacionIden(Integer id) {
		setId(id);
	}

	public Integer getPlanentidadordenacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Planentidadordenacion createInstance() {
		Planentidadordenacion planentidadordenacion = new Planentidadordenacion();
		return planentidadordenacion;
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
		Plan plan = planHome.getDefinedInstance();
		if (plan != null) {
			getInstance().setPlan(plan);
		}
	}

	public boolean isWired() {
		if (getInstance().getEntidad() == null)
			return false;
		if (getInstance().getPlan() == null)
			return false;
		return true;
	}

	public Planentidadordenacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
