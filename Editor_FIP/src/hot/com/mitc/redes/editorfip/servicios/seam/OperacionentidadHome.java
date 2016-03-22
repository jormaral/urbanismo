package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;

@Name("operacionentidadHome")
public class OperacionentidadHome extends EntityHome<Operacionentidad> {

	@In(create = true)
	EntidadHome entidadHome;
	@In(create = true)
	OperacionHome operacionHome;

	public void setOperacionentidadIden(Integer id) {
		setId(id);
	}

	public Integer getOperacionentidadIden() {
		return (Integer) getId();
	}

	@Override
	protected Operacionentidad createInstance() {
		Operacionentidad operacionentidad = new Operacionentidad();
		return operacionentidad;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Entidad entidadByIdentidad = entidadHome.getDefinedInstance();
		if (entidadByIdentidad != null) {
			getInstance().setEntidadByIdentidad(entidadByIdentidad);
		}
		Entidad entidadByIdentidadoperadora = entidadHome.getDefinedInstance();
		if (entidadByIdentidadoperadora != null) {
			getInstance().setEntidadByIdentidadoperadora(
					entidadByIdentidadoperadora);
		}
		Operacion operacion = operacionHome.getDefinedInstance();
		if (operacion != null) {
			getInstance().setOperacion(operacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getEntidadByIdentidad() == null)
			return false;
		if (getInstance().getEntidadByIdentidadoperadora() == null)
			return false;
		if (getInstance().getOperacion() == null)
			return false;
		return true;
	}

	public Operacionentidad getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
