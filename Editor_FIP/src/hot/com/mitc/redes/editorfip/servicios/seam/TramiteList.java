package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Name("tramiteList")
public class TramiteList extends EntityQuery<Tramite> {

	private static final String EJBQL = "select tramite from Tramite tramite";

	private static final String[] RESTRICTIONS = {
			"lower(tramite.codigofip) like lower(concat(#{tramiteList.tramite.codigofip},'%'))",
			"lower(tramite.comentario) like lower(concat(#{tramiteList.tramite.comentario},'%'))",
			"lower(tramite.nombre) like lower(concat(#{tramiteList.tramite.nombre},'%'))",
			"lower(tramite.numeroacuerdo) like lower(concat(#{tramiteList.tramite.numeroacuerdo},'%'))",
			"lower(tramite.texto) like lower(concat(#{tramiteList.tramite.texto},'%'))",};

	private Tramite tramite = new Tramite();

	public TramiteList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tramite getTramite() {
		return tramite;
	}
}
