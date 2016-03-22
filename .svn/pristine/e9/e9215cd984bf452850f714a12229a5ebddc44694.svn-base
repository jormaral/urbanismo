package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentocaso;

@Name("documentocasoList")
public class DocumentocasoList extends EntityQuery<Documentocaso> {

	private static final String EJBQL = "select documentocaso from Documentocaso documentocaso";

	private static final String[] RESTRICTIONS = {};

	private Documentocaso documentocaso = new Documentocaso();

	public DocumentocasoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Documentocaso getDocumentocaso() {
		return documentocaso;
	}
}
