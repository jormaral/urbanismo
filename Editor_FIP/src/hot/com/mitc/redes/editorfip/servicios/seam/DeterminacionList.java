package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;

@Name("determinacionList")
public class DeterminacionList extends EntityQuery<Determinacion> {

	private static final String EJBQL = "select determinacion from Determinacion determinacion";

	private static final String[] RESTRICTIONS = {
			"lower(determinacion.apartado) like lower(concat(#{determinacionList.determinacion.apartado},'%'))",
			"lower(determinacion.codigo) like lower(concat(#{determinacionList.determinacion.codigo},'%'))",
			"lower(determinacion.etiqueta) like lower(concat(#{determinacionList.determinacion.etiqueta},'%'))",
			"lower(determinacion.nombre) like lower(concat(#{determinacionList.determinacion.nombre},'%'))",
			"lower(determinacion.texto) like lower(concat(#{determinacionList.determinacion.texto},'%'))",};

	private Determinacion determinacion = new Determinacion();

	public DeterminacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Determinacion getDeterminacion() {
		return determinacion;
	}
}
