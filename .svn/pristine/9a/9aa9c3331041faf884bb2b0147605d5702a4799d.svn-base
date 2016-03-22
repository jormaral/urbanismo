package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;

@Name("entidaddeterminacionregimenList")
public class EntidaddeterminacionregimenList
		extends
			EntityQuery<Entidaddeterminacionregimen> {

	private static final String EJBQL = "select entidaddeterminacionregimen from Entidaddeterminacionregimen entidaddeterminacionregimen";

	private static final String[] RESTRICTIONS = {"lower(entidaddeterminacionregimen.valor) like lower(concat(#{entidaddeterminacionregimenList.entidaddeterminacionregimen.valor},'%'))",};

	private Entidaddeterminacionregimen entidaddeterminacionregimen = new Entidaddeterminacionregimen();

	public EntidaddeterminacionregimenList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Entidaddeterminacionregimen getEntidaddeterminacionregimen() {
		return entidaddeterminacionregimen;
	}
}
