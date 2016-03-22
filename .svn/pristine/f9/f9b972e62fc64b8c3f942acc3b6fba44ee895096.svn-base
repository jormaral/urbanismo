package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planshp;

@Name("planshpHome")
public class PlanshpHome extends EntityHome<Planshp> {

	@In(create = true)
	PlanHome planHome;

	public void setPlanshpIden(Integer id) {
		setId(id);
	}

	public Integer getPlanshpIden() {
		return (Integer) getId();
	}

	@Override
	protected Planshp createInstance() {
		Planshp planshp = new Planshp();
		return planshp;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Plan plan = planHome.getDefinedInstance();
		if (plan != null) {
			getInstance().setPlan(plan);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Planshp getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
