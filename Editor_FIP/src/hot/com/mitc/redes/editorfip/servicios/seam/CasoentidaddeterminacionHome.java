package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentocaso;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;

@Name("casoentidaddeterminacionHome")
public class CasoentidaddeterminacionHome
		extends
			EntityHome<Casoentidaddeterminacion> {

	@In(create = true)
	EntidaddeterminacionHome entidaddeterminacionHome;

	public void setCasoentidaddeterminacionIden(Integer id) {
		setId(id);
	}

	public Integer getCasoentidaddeterminacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Casoentidaddeterminacion createInstance() {
		Casoentidaddeterminacion casoentidaddeterminacion = new Casoentidaddeterminacion();
		return casoentidaddeterminacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Entidaddeterminacion entidaddeterminacion = entidaddeterminacionHome
				.getDefinedInstance();
		if (entidaddeterminacion != null) {
			getInstance().setEntidaddeterminacion(entidaddeterminacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getEntidaddeterminacion() == null)
			return false;
		return true;
	}

	public Casoentidaddeterminacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Documentocaso> getDocumentocasos() {
		return getInstance() == null ? null : new ArrayList<Documentocaso>(
				getInstance().getDocumentocasos());
	}
	public List<Documentocaso> getDocumentocasos_1() {
		return getInstance() == null ? null : new ArrayList<Documentocaso>(
				getInstance().getDocumentocasos_1());
	}
	public List<Entidaddeterminacionregimen> getEntidaddeterminacionregimensForIdcaso() {
		return getInstance() == null
				? null
				: new ArrayList<Entidaddeterminacionregimen>(getInstance()
						.getEntidaddeterminacionregimensForIdcaso());
	}
	public List<Entidaddeterminacionregimen> getEntidaddeterminacionregimensForIdcaso_1() {
		return getInstance() == null
				? null
				: new ArrayList<Entidaddeterminacionregimen>(getInstance()
						.getEntidaddeterminacionregimensForIdcaso_1());
	}
	public List<Entidaddeterminacionregimen> getEntidaddeterminacionregimensForIdcasoaplicacion() {
		return getInstance() == null
				? null
				: new ArrayList<Entidaddeterminacionregimen>(getInstance()
						.getEntidaddeterminacionregimensForIdcasoaplicacion());
	}
	public List<Entidaddeterminacionregimen> getEntidaddeterminacionregimensForIdcasoaplicacion_1() {
		return getInstance() == null
				? null
				: new ArrayList<Entidaddeterminacionregimen>(getInstance()
						.getEntidaddeterminacionregimensForIdcasoaplicacion_1());
	}
	public List<Vinculocaso> getVinculocasosForIdcaso() {
		return getInstance() == null ? null : new ArrayList<Vinculocaso>(
				getInstance().getVinculocasosForIdcaso());
	}
	public List<Vinculocaso> getVinculocasosForIdcaso_1() {
		return getInstance() == null ? null : new ArrayList<Vinculocaso>(
				getInstance().getVinculocasosForIdcaso_1());
	}
	public List<Vinculocaso> getVinculocasosForIdcasovinculado() {
		return getInstance() == null ? null : new ArrayList<Vinculocaso>(
				getInstance().getVinculocasosForIdcasovinculado());
	}
	public List<Vinculocaso> getVinculocasosForIdcasovinculado_1() {
		return getInstance() == null ? null : new ArrayList<Vinculocaso>(
				getInstance().getVinculocasosForIdcasovinculado_1());
	}

}
