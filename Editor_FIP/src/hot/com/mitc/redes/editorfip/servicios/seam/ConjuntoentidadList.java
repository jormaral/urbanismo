package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Conjuntoentidad;

@Name("conjuntoentidadList")
public class ConjuntoentidadList extends EntityQuery<Conjuntoentidad> {

	private static final String EJBQL = "select conjuntoentidad from Conjuntoentidad conjuntoentidad";

	private static final String[] RESTRICTIONS = {};

	private Conjuntoentidad conjuntoentidad = new Conjuntoentidad();

	public ConjuntoentidadList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Conjuntoentidad getConjuntoentidad() {
		return conjuntoentidad;
	}
}
