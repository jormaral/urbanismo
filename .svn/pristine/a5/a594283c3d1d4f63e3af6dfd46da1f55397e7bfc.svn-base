package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;

@Name("operacionrelacionHome")
public class OperacionrelacionHome extends EntityHome<Operacionrelacion> {

	@In(create = true)
	OperacionHome operacionHome;
	@In(create = true)
	RelacionHome relacionHome;

	public void setOperacionrelacionIden(Integer id) {
		setId(id);
	}

	public Integer getOperacionrelacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Operacionrelacion createInstance() {
		Operacionrelacion operacionrelacion = new Operacionrelacion();
		return operacionrelacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Operacion operacion = operacionHome.getDefinedInstance();
		if (operacion != null) {
			getInstance().setOperacion(operacion);
		}
		Relacion relacion = relacionHome.getDefinedInstance();
		if (relacion != null) {
			getInstance().setRelacion(relacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getOperacion() == null)
			return false;
		if (getInstance().getRelacion() == null)
			return false;
		return true;
	}

	public Operacionrelacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
