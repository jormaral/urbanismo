package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Boletintramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Name("tramiteHome")
public class TramiteHome extends EntityHome<Tramite> {

	@In(create = true)
	PlanHome planHome;

	public void setTramiteIden(Integer id) {
		setId(id);
	}

	public Integer getTramiteIden() {
		return (Integer) getId();
	}

	@Override
	protected Tramite createInstance() {
		Tramite tramite = new Tramite();
		return tramite;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Plan plan = planHome.getDefinedInstance();
		if (plan != null) {
			getInstance().setPlan(plan);
		}
	}

	public boolean isWired() {
		if (getInstance().getPlan() == null)
			return false;
		return true;
	}

	public Tramite getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Boletintramite> getBoletintramites() {
		return getInstance() == null ? null : new ArrayList<Boletintramite>(
				getInstance().getBoletintramites());
	}
	public List<Boletintramite> getBoletintramites_1() {
		return getInstance() == null ? null : new ArrayList<Boletintramite>(
				getInstance().getBoletintramites_1());
	}
	public List<Determinacion> getDeterminacions() {
		return getInstance() == null ? null : new ArrayList<Determinacion>(
				getInstance().getDeterminacions());
	}
	public List<Determinacion> getDeterminacions_1() {
		return getInstance() == null ? null : new ArrayList<Determinacion>(
				getInstance().getDeterminacions_1());
	}
	public List<Documento> getDocumentos() {
		return getInstance() == null ? null : new ArrayList<Documento>(
				getInstance().getDocumentos());
	}
	public List<Documento> getDocumentos_1() {
		return getInstance() == null ? null : new ArrayList<Documento>(
				getInstance().getDocumentos_1());
	}
	public List<Entidad> getEntidads() {
		return getInstance() == null ? null : new ArrayList<Entidad>(
				getInstance().getEntidads());
	}
	public List<Entidad> getEntidads_1() {
		return getInstance() == null ? null : new ArrayList<Entidad>(
				getInstance().getEntidads_1());
	}
	public List<Operacion> getOperacions() {
		return getInstance() == null ? null : new ArrayList<Operacion>(
				getInstance().getOperacions());
	}
	public List<Operacion> getOperacions_1() {
		return getInstance() == null ? null : new ArrayList<Operacion>(
				getInstance().getOperacions_1());
	}
	public List<Relacion> getRelacionsForIdtramiteborrador() {
		return getInstance() == null ? null : new ArrayList<Relacion>(
				getInstance().getRelacionsForIdtramiteborrador());
	}
	public List<Relacion> getRelacionsForIdtramiteborrador_1() {
		return getInstance() == null ? null : new ArrayList<Relacion>(
				getInstance().getRelacionsForIdtramiteborrador_1());
	}
	public List<Relacion> getRelacionsForIdtramitecreador() {
		return getInstance() == null ? null : new ArrayList<Relacion>(
				getInstance().getRelacionsForIdtramitecreador());
	}
	public List<Relacion> getRelacionsForIdtramitecreador_1() {
		return getInstance() == null ? null : new ArrayList<Relacion>(
				getInstance().getRelacionsForIdtramitecreador_1());
	}

}
