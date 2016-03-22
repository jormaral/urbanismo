package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

@Name("entidadList")
public class EntidadList extends EntityQuery<Entidad> {

	private static final String EJBQL = "select entidad from Entidad entidad";

	private static final String[] RESTRICTIONS = {
			"lower(entidad.clave) like lower(concat(#{entidadList.entidad.clave},'%'))",
			"lower(entidad.codigo) like lower(concat(#{entidadList.entidad.codigo},'%'))",
			"lower(entidad.etiqueta) like lower(concat(#{entidadList.entidad.etiqueta},'%'))",
			"lower(entidad.nombre) like lower(concat(#{entidadList.entidad.nombre},'%'))",};

	private Entidad entidad = new Entidad();

	public EntidadList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Entidad getEntidad() {
		return entidad;
	}
}
