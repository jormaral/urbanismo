package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;

@Name("documentodeterminacionList")
public class DocumentodeterminacionList
		extends
			EntityQuery<Documentodeterminacion> {

	private static final String EJBQL = "select documentodeterminacion from Documentodeterminacion documentodeterminacion";

	private static final String[] RESTRICTIONS = {};

	private Documentodeterminacion documentodeterminacion = new Documentodeterminacion();

	public DocumentodeterminacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Documentodeterminacion getDocumentodeterminacion() {
		return documentodeterminacion;
	}
}
