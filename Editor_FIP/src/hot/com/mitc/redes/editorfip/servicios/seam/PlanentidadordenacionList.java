package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planentidadordenacion;

@Name("planentidadordenacionList")
public class PlanentidadordenacionList
		extends
			EntityQuery<Planentidadordenacion> {

	private static final String EJBQL = "select planentidadordenacion from Planentidadordenacion planentidadordenacion";

	private static final String[] RESTRICTIONS = {};

	private Planentidadordenacion planentidadordenacion = new Planentidadordenacion();

	public PlanentidadordenacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Planentidadordenacion getPlanentidadordenacion() {
		return planentidadordenacion;
	}
}
