package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Propiedadrelacion;

@Name("propiedadrelacionList")
public class PropiedadrelacionList extends EntityQuery<Propiedadrelacion> {

	private static final String EJBQL = "select propiedadrelacion from Propiedadrelacion propiedadrelacion";

	private static final String[] RESTRICTIONS = {"lower(propiedadrelacion.valor) like lower(concat(#{propiedadrelacionList.propiedadrelacion.valor},'%'))",};

	private Propiedadrelacion propiedadrelacion = new Propiedadrelacion();

	public PropiedadrelacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Propiedadrelacion getPropiedadrelacion() {
		return propiedadrelacion;
	}
}
