package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;

@Name("operacionList")
public class OperacionList extends EntityQuery<Operacion> {

	private static final String EJBQL = "select operacion from Operacion operacion";

	private static final String[] RESTRICTIONS = {"lower(operacion.texto) like lower(concat(#{operacionList.operacion.texto},'%'))",};

	private Operacion operacion = new Operacion();

	public OperacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Operacion getOperacion() {
		return operacion;
	}
}
