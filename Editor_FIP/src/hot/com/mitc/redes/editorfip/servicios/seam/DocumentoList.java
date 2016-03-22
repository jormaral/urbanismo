package com.mitc.redes.editorfip.servicios.seam;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;

@Name("documentoList")
public class DocumentoList extends EntityQuery<Documento> {

	private static final String EJBQL = "select documento from Documento documento";

	private static final String[] RESTRICTIONS = {
			"lower(documento.archivo) like lower(concat(#{documentoList.documento.archivo},'%'))",
			"lower(documento.comentario) like lower(concat(#{documentoList.documento.comentario},'%'))",
			"lower(documento.nombre) like lower(concat(#{documentoList.documento.nombre},'%'))",};

	private Documento documento = new Documento();

	public DocumentoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Documento getDocumento() {
		return documento;
	}
}
