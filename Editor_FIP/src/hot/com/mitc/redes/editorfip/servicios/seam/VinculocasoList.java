package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;

@Name("vinculocasoList")
public class VinculocasoList extends EntityQuery<Vinculocaso> {

	private static final String EJBQL = "select vinculocaso from Vinculocaso vinculocaso";

	private static final String[] RESTRICTIONS = {};

	private Vinculocaso vinculocaso = new Vinculocaso();

	public VinculocasoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vinculocaso getVinculocaso() {
		return vinculocaso;
	}
}
