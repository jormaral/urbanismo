package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;

@Name("entidadpntList")
public class EntidadpntList extends EntityQuery<Entidadpnt> {

	private static final String EJBQL = "select entidadpnt from Entidadpnt entidadpnt";

	private static final String[] RESTRICTIONS = {};

	private Entidadpnt entidadpnt = new Entidadpnt();

	public EntidadpntList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Entidadpnt getEntidadpnt() {
		return entidadpnt;
	}
}
