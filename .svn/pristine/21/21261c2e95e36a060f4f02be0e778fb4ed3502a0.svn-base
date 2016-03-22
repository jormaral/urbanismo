package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;

@Name("operacionentidadList")
public class OperacionentidadList extends EntityQuery<Operacionentidad> {

	private static final String EJBQL = "select operacionentidad from Operacionentidad operacionentidad";

	private static final String[] RESTRICTIONS = {};

	private Operacionentidad operacionentidad = new Operacionentidad();

	public OperacionentidadList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Operacionentidad getOperacionentidad() {
		return operacionentidad;
	}
}
