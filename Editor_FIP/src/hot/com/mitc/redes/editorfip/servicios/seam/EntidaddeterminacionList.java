package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;

@Name("entidaddeterminacionList")
public class EntidaddeterminacionList extends EntityQuery<Entidaddeterminacion> {

	private static final String EJBQL = "select entidaddeterminacion from Entidaddeterminacion entidaddeterminacion";

	private static final String[] RESTRICTIONS = {};

	private Entidaddeterminacion entidaddeterminacion = new Entidaddeterminacion();

	public EntidaddeterminacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Entidaddeterminacion getEntidaddeterminacion() {
		return entidaddeterminacion;
	}
}
