package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;

@Name("documentoshpList")
public class DocumentoshpList extends EntityQuery<Documentoshp> {

	private static final String EJBQL = "select documentoshp from Documentoshp documentoshp";

	private static final String[] RESTRICTIONS = {"lower(documentoshp.hoja) like lower(concat(#{documentoshpList.documentoshp.hoja},'%'))",};

	private Documentoshp documentoshp = new Documentoshp();

	public DocumentoshpList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Documentoshp getDocumentoshp() {
		return documentoshp;
	}
}
