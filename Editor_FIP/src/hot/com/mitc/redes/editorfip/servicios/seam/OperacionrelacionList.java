package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionrelacion;

@Name("operacionrelacionList")
public class OperacionrelacionList extends EntityQuery<Operacionrelacion> {

	private static final String EJBQL = "select operacionrelacion from Operacionrelacion operacionrelacion";

	private static final String[] RESTRICTIONS = {};

	private Operacionrelacion operacionrelacion = new Operacionrelacion();

	public OperacionrelacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Operacionrelacion getOperacionrelacion() {
		return operacionrelacion;
	}
}
