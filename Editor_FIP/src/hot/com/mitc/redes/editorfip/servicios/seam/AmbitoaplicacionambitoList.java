package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Ambitoaplicacionambito;

@Name("ambitoaplicacionambitoList")
public class AmbitoaplicacionambitoList
		extends
			EntityQuery<Ambitoaplicacionambito> {

	private static final String EJBQL = "select ambitoaplicacionambito from Ambitoaplicacionambito ambitoaplicacionambito";

	private static final String[] RESTRICTIONS = {};

	private Ambitoaplicacionambito ambitoaplicacionambito = new Ambitoaplicacionambito();

	public AmbitoaplicacionambitoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Ambitoaplicacionambito getAmbitoaplicacionambito() {
		return ambitoaplicacionambito;
	}
}
