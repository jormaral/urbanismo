package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;

@Name("entidadpolList")
public class EntidadpolList extends EntityQuery<Entidadpol> {

	private static final String EJBQL = "select entidadpol from Entidadpol entidadpol";

	private static final String[] RESTRICTIONS = {};

	private Entidadpol entidadpol = new Entidadpol();

	public EntidadpolList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Entidadpol getEntidadpol() {
		return entidadpol;
	}
}
