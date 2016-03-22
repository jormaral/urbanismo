package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;

@Name("operaciondeterminacionHome")
public class OperaciondeterminacionHome
		extends
			EntityHome<Operaciondeterminacion> {

	@In(create = true)
	DeterminacionHome determinacionHome;
	@In(create = true)
	OperacionHome operacionHome;

	public void setOperaciondeterminacionIden(Integer id) {
		setId(id);
	}

	public Integer getOperaciondeterminacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Operaciondeterminacion createInstance() {
		Operaciondeterminacion operaciondeterminacion = new Operaciondeterminacion();
		return operaciondeterminacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Determinacion determinacionByIddeterminacion = determinacionHome
				.getDefinedInstance();
		if (determinacionByIddeterminacion != null) {
			getInstance().setDeterminacionByIddeterminacion(
					determinacionByIddeterminacion);
		}
		Determinacion determinacionByIddeterminacionoperadora = determinacionHome
				.getDefinedInstance();
		if (determinacionByIddeterminacionoperadora != null) {
			getInstance().setDeterminacionByIddeterminacionoperadora(
					determinacionByIddeterminacionoperadora);
		}
		Operacion operacion = operacionHome.getDefinedInstance();
		if (operacion != null) {
			getInstance().setOperacion(operacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getDeterminacionByIddeterminacion() == null)
			return false;
		if (getInstance().getDeterminacionByIddeterminacionoperadora() == null)
			return false;
		if (getInstance().getOperacion() == null)
			return false;
		return true;
	}

	public Operaciondeterminacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
