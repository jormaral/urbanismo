package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;

@Name("opciondeterminacionList")
public class OpciondeterminacionList extends EntityQuery<Opciondeterminacion> {

	private static final String EJBQL = "select opciondeterminacion from Opciondeterminacion opciondeterminacion";

	private static final String[] RESTRICTIONS = {};

	private Opciondeterminacion opciondeterminacion = new Opciondeterminacion();

	public OpciondeterminacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Opciondeterminacion getOpciondeterminacion() {
		return opciondeterminacion;
	}
}
