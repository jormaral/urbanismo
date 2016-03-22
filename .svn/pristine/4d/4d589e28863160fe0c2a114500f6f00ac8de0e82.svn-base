package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;

@Name("vectorrelacionList")
public class VectorrelacionList extends EntityQuery<Vectorrelacion> {

	private static final String EJBQL = "select vectorrelacion from Vectorrelacion vectorrelacion";

	private static final String[] RESTRICTIONS = {};

	private Vectorrelacion vectorrelacion = new Vectorrelacion();

	public VectorrelacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vectorrelacion getVectorrelacion() {
		return vectorrelacion;
	}
}
