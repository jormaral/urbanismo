package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;

@Name("operaciondeterminacionList")
public class OperaciondeterminacionList
		extends
			EntityQuery<Operaciondeterminacion> {

	private static final String EJBQL = "select operaciondeterminacion from Operaciondeterminacion operaciondeterminacion";

	private static final String[] RESTRICTIONS = {};

	private Operaciondeterminacion operaciondeterminacion = new Operaciondeterminacion();

	public OperaciondeterminacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Operaciondeterminacion getOperaciondeterminacion() {
		return operaciondeterminacion;
	}
}
