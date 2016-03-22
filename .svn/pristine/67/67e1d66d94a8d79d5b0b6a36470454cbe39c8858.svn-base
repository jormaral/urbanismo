package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;

@Name("regimenespecificoList")
public class RegimenespecificoList extends EntityQuery<Regimenespecifico> {

	private static final String EJBQL = "select regimenespecifico from Regimenespecifico regimenespecifico";

	private static final String[] RESTRICTIONS = {
			"lower(regimenespecifico.nombre) like lower(concat(#{regimenespecificoList.regimenespecifico.nombre},'%'))",
			"lower(regimenespecifico.texto) like lower(concat(#{regimenespecificoList.regimenespecifico.texto},'%'))",};

	private Regimenespecifico regimenespecifico = new Regimenespecifico();

	public RegimenespecificoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Regimenespecifico getRegimenespecifico() {
		return regimenespecifico;
	}
}
