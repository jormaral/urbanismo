package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;

@Name("determinaciongrupoentidadList")
public class DeterminaciongrupoentidadList
		extends
			EntityQuery<Determinaciongrupoentidad> {

	private static final String EJBQL = "select determinaciongrupoentidad from Determinaciongrupoentidad determinaciongrupoentidad";

	private static final String[] RESTRICTIONS = {};

	private Determinaciongrupoentidad determinaciongrupoentidad = new Determinaciongrupoentidad();

	public DeterminaciongrupoentidadList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Determinaciongrupoentidad getDeterminaciongrupoentidad() {
		return determinaciongrupoentidad;
	}
}
