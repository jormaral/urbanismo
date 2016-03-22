package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;

@Name("entidadlinList")
public class EntidadlinList extends EntityQuery<Entidadlin> {

	private static final String EJBQL = "select entidadlin from Entidadlin entidadlin";

	private static final String[] RESTRICTIONS = {};

	private Entidadlin entidadlin = new Entidadlin();

	public EntidadlinList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Entidadlin getEntidadlin() {
		return entidadlin;
	}
}
