package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionplan;

@Name("operacionplanList")
public class OperacionplanList extends EntityQuery<Operacionplan> {

	private static final String EJBQL = "select operacionplan from Operacionplan operacionplan";

	private static final String[] RESTRICTIONS = {};

	private Operacionplan operacionplan = new Operacionplan();

	public OperacionplanList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Operacionplan getOperacionplan() {
		return operacionplan;
	}
}
