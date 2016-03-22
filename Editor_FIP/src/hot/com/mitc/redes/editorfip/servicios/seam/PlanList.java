package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;

@Name("planList")
public class PlanList extends EntityQuery<Plan> {

	private static final String EJBQL = "select plan from Plan plan";

	private static final String[] RESTRICTIONS = {
			"lower(plan.codigo) like lower(concat(#{planList.plan.codigo},'%'))",
			"lower(plan.nombre) like lower(concat(#{planList.plan.nombre},'%'))",
			"lower(plan.texto) like lower(concat(#{planList.plan.texto},'%'))",};

	private Plan plan = new Plan();

	public PlanList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Plan getPlan() {
		return plan;
	}
}
