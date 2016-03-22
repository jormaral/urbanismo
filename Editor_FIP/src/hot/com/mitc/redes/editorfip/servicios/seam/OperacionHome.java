package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Name("operacionHome")
public class OperacionHome extends EntityHome<Operacion> {

	@In(create = true)
	TramiteHome tramiteHome;

	public void setOperacionIden(Integer id) {
		setId(id);
	}

	public Integer getOperacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Operacion createInstance() {
		Operacion operacion = new Operacion();
		return operacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Tramite tramite = tramiteHome.getDefinedInstance();
		if (tramite != null) {
			getInstance().setTramite(tramite);
		}
	}

	public boolean isWired() {
		if (getInstance().getTramite() == null)
			return false;
		return true;
	}

	public Operacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Operaciondeterminacion> getOperaciondeterminacions() {
		return getInstance() == null
				? null
				: new ArrayList<Operaciondeterminacion>(getInstance()
						.getOperaciondeterminacions());
	}
	public List<Operaciondeterminacion> getOperaciondeterminacions_1() {
		return getInstance() == null
				? null
				: new ArrayList<Operaciondeterminacion>(getInstance()
						.getOperaciondeterminacions_1());
	}
	public List<Operacionentidad> getOperacionentidads() {
		return getInstance() == null ? null : new ArrayList<Operacionentidad>(
				getInstance().getOperacionentidads());
	}
	public List<Operacionentidad> getOperacionentidads_1() {
		return getInstance() == null ? null : new ArrayList<Operacionentidad>(
				getInstance().getOperacionentidads_1());
	}
	public List<Operacionrelacion> getOperacionrelacions() {
		return getInstance() == null ? null : new ArrayList<Operacionrelacion>(
				getInstance().getOperacionrelacions());
	}
	public List<Operacionrelacion> getOperacionrelacions_1() {
		return getInstance() == null ? null : new ArrayList<Operacionrelacion>(
				getInstance().getOperacionrelacions_1());
	}

}
