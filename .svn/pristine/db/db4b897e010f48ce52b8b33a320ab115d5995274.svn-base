package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Propiedadrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;

@Name("relacionHome")
public class RelacionHome extends EntityHome<Relacion> {

	@In(create = true)
	TramiteHome tramiteHome;

	public void setRelacionIden(Integer id) {
		setId(id);
	}

	public Integer getRelacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Relacion createInstance() {
		Relacion relacion = new Relacion();
		return relacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Tramite tramiteByIdtramiteborrador = tramiteHome.getDefinedInstance();
		if (tramiteByIdtramiteborrador != null) {
			getInstance().setTramiteByIdtramiteborrador(
					tramiteByIdtramiteborrador);
		}
		Tramite tramiteByIdtramitecreador = tramiteHome.getDefinedInstance();
		if (tramiteByIdtramitecreador != null) {
			getInstance().setTramiteByIdtramitecreador(
					tramiteByIdtramitecreador);
		}
	}

	public boolean isWired() {
		if (getInstance().getTramiteByIdtramitecreador() == null)
			return false;
		return true;
	}

	public Relacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Operacionrelacion> getOperacionrelacions() {
		return getInstance() == null ? null : new ArrayList<Operacionrelacion>(
				getInstance().getOperacionrelacions());
	}
	public List<Operacionrelacion> getOperacionrelacions_1() {
		return getInstance() == null ? null : new ArrayList<Operacionrelacion>(
				getInstance().getOperacionrelacions_1());
	}
	public List<Propiedadrelacion> getPropiedadrelacions() {
		return getInstance() == null ? null : new ArrayList<Propiedadrelacion>(
				getInstance().getPropiedadrelacions());
	}
	public List<Propiedadrelacion> getPropiedadrelacions_1() {
		return getInstance() == null ? null : new ArrayList<Propiedadrelacion>(
				getInstance().getPropiedadrelacions_1());
	}
	public List<Vectorrelacion> getVectorrelacions() {
		return getInstance() == null ? null : new ArrayList<Vectorrelacion>(
				getInstance().getVectorrelacions());
	}
	public List<Vectorrelacion> getVectorrelacions_1() {
		return getInstance() == null ? null : new ArrayList<Vectorrelacion>(
				getInstance().getVectorrelacions_1());
	}

}
