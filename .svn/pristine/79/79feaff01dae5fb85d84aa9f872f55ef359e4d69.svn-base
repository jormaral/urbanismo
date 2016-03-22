package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;

@Name("documentoentidadList")
public class DocumentoentidadList extends EntityQuery<Documentoentidad> {

	private static final String EJBQL = "select documentoentidad from Documentoentidad documentoentidad";

	private static final String[] RESTRICTIONS = {};

	private Documentoentidad documentoentidad = new Documentoentidad();

	public DocumentoentidadList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Documentoentidad getDocumentoentidad() {
		return documentoentidad;
	}
}
