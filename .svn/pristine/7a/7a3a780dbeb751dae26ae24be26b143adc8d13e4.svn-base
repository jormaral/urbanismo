package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;

@Name("casoentidaddeterminacionList")
public class CasoentidaddeterminacionList
		extends
			EntityQuery<Casoentidaddeterminacion> {

	private static final String EJBQL = "select casoentidaddeterminacion from Casoentidaddeterminacion casoentidaddeterminacion";

	private static final String[] RESTRICTIONS = {"lower(casoentidaddeterminacion.nombre) like lower(concat(#{casoentidaddeterminacionList.casoentidaddeterminacion.nombre},'%'))",};

	private Casoentidaddeterminacion casoentidaddeterminacion = new Casoentidaddeterminacion();

	public CasoentidaddeterminacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Casoentidaddeterminacion getCasoentidaddeterminacion() {
		return casoentidaddeterminacion;
	}
}
