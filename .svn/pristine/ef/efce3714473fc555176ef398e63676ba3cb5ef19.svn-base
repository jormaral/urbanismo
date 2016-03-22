package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;

@Name("determinaciongrupoentidadHome")
public class DeterminaciongrupoentidadHome
		extends
			EntityHome<Determinaciongrupoentidad> {

	@In(create = true)
	DeterminacionHome determinacionHome;

	public void setDeterminaciongrupoentidadIden(Integer id) {
		setId(id);
	}

	public Integer getDeterminaciongrupoentidadIden() {
		return (Integer) getId();
	}

	@Override
	protected Determinaciongrupoentidad createInstance() {
		Determinaciongrupoentidad determinaciongrupoentidad = new Determinaciongrupoentidad();
		return determinaciongrupoentidad;
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
		Determinacion determinacionByIddeterminaciongrupo = determinacionHome
				.getDefinedInstance();
		if (determinacionByIddeterminaciongrupo != null) {
			getInstance().setDeterminacionByIddeterminaciongrupo(
					determinacionByIddeterminaciongrupo);
		}
	}

	public boolean isWired() {
		if (getInstance().getDeterminacionByIddeterminacion() == null)
			return false;
		return true;
	}

	public Determinaciongrupoentidad getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
