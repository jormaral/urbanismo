package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;

@Name("relacionList")
public class RelacionList extends EntityQuery<Relacion> {

	private static final String EJBQL = "select relacion from Relacion relacion";

	private static final String[] RESTRICTIONS = {};

	private Relacion relacion = new Relacion();

	public RelacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Relacion getRelacion() {
		return relacion;
	}
}
