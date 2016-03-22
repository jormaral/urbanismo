package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionplan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planentidadordenacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planshp;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Name("planHome")
public class PlanHome extends EntityHome<Plan> {

	@In(create = true)
	PlanHome planHome;

	public void setPlanIden(Integer id) {
		setId(id);
	}

	public Integer getPlanIden() {
		return (Integer) getId();
	}

	@Override
	protected Plan createInstance() {
		Plan plan = new Plan();
		return plan;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Plan getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Operacionplan> getOperacionplansForIdplanoperado() {
		return getInstance() == null ? null : new ArrayList<Operacionplan>(
				getInstance().getOperacionplansForIdplanoperado());
	}
	public List<Operacionplan> getOperacionplansForIdplanoperado_1() {
		return getInstance() == null ? null : new ArrayList<Operacionplan>(
				getInstance().getOperacionplansForIdplanoperado_1());
	}
	public List<Operacionplan> getOperacionplansForIdplanoperador() {
		return getInstance() == null ? null : new ArrayList<Operacionplan>(
				getInstance().getOperacionplansForIdplanoperador());
	}
	public List<Operacionplan> getOperacionplansForIdplanoperador_1() {
		return getInstance() == null ? null : new ArrayList<Operacionplan>(
				getInstance().getOperacionplansForIdplanoperador_1());
	}
	public List<Planentidadordenacion> getPlanentidadordenacions() {
		return getInstance() == null
				? null
				: new ArrayList<Planentidadordenacion>(getInstance()
						.getPlanentidadordenacions());
	}
	public List<Planentidadordenacion> getPlanentidadordenacions_1() {
		return getInstance() == null
				? null
				: new ArrayList<Planentidadordenacion>(getInstance()
						.getPlanentidadordenacions_1());
	}
	public List<Plan> getPlansForIdpadre() {
		return getInstance() == null ? null : new ArrayList<Plan>(getInstance()
				.getPlansForIdpadre());
	}
	public List<Plan> getPlansForIdpadre_1() {
		return getInstance() == null ? null : new ArrayList<Plan>(getInstance()
				.getPlansForIdpadre_1());
	}
	public List<Plan> getPlansForIdplanbase() {
		return getInstance() == null ? null : new ArrayList<Plan>(getInstance()
				.getPlansForIdplanbase());
	}
	public List<Plan> getPlansForIdplanbase_1() {
		return getInstance() == null ? null : new ArrayList<Plan>(getInstance()
				.getPlansForIdplanbase_1());
	}
	public List<Planshp> getPlanshps() {
		return getInstance() == null ? null : new ArrayList<Planshp>(
				getInstance().getPlanshps());
	}
	public List<Planshp> getPlanshps_1() {
		return getInstance() == null ? null : new ArrayList<Planshp>(
				getInstance().getPlanshps_1());
	}
	public List<Tramite> getTramites() {
		return getInstance() == null ? null : new ArrayList<Tramite>(
				getInstance().getTramites());
	}
	public List<Tramite> getTramites_1() {
		return getInstance() == null ? null : new ArrayList<Tramite>(
				getInstance().getTramites_1());
	}

}
