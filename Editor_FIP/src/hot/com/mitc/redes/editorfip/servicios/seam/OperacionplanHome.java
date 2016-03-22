package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionplan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;

@Name("operacionplanHome")
public class OperacionplanHome extends EntityHome<Operacionplan> {

	@In(create = true)
	PlanHome planHome;

	public void setOperacionplanIden(Integer id) {
		setId(id);
	}

	public Integer getOperacionplanIden() {
		return (Integer) getId();
	}

	@Override
	protected Operacionplan createInstance() {
		Operacionplan operacionplan = new Operacionplan();
		return operacionplan;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Plan planByIdplanoperado = planHome.getDefinedInstance();
		if (planByIdplanoperado != null) {
			getInstance().setPlanByIdplanoperado(planByIdplanoperado);
		}
		Plan planByIdplanoperador = planHome.getDefinedInstance();
		if (planByIdplanoperador != null) {
			getInstance().setPlanByIdplanoperador(planByIdplanoperador);
		}
	}

	public boolean isWired() {
		if (getInstance().getPlanByIdplanoperador() == null)
			return false;
		return true;
	}

	public Operacionplan getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
