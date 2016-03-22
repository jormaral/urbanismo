package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Boletintramite;

@Name("boletintramiteList")
public class BoletintramiteList extends EntityQuery<Boletintramite> {

	private static final String EJBQL = "select boletintramite from Boletintramite boletintramite";

	private static final String[] RESTRICTIONS = {"lower(boletintramite.numero) like lower(concat(#{boletintramiteList.boletintramite.numero},'%'))",};

	private Boletintramite boletintramite = new Boletintramite();

	public BoletintramiteList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Boletintramite getBoletintramite() {
		return boletintramite;
	}
}
