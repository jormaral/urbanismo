package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planshp;

@Name("planshpList")
public class PlanshpList extends EntityQuery<Planshp> {

	private static final String EJBQL = "select planshp from Planshp planshp";

	private static final String[] RESTRICTIONS = {};

	private Planshp planshp = new Planshp();

	public PlanshpList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Planshp getPlanshp() {
		return planshp;
	}
}
