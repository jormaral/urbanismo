package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;

@Name("opciondeterminacionHome")
public class OpciondeterminacionHome extends EntityHome<Opciondeterminacion> {

	@In(create = true)
	DeterminacionHome determinacionHome;

	public void setOpciondeterminacionIden(Integer id) {
		setId(id);
	}

	public Integer getOpciondeterminacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Opciondeterminacion createInstance() {
		Opciondeterminacion opciondeterminacion = new Opciondeterminacion();
		return opciondeterminacion;
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
		Determinacion determinacionByIddeterminacionvalorref = determinacionHome
				.getDefinedInstance();
		if (determinacionByIddeterminacionvalorref != null) {
			getInstance().setDeterminacionByIddeterminacionvalorref(
					determinacionByIddeterminacionvalorref);
		}
	}

	public boolean isWired() {
		if (getInstance().getDeterminacionByIddeterminacion() == null)
			return false;
		if (getInstance().getDeterminacionByIddeterminacionvalorref() == null)
			return false;
		return true;
	}

	public Opciondeterminacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Entidaddeterminacionregimen> getEntidaddeterminacionregimens() {
		return getInstance() == null
				? null
				: new ArrayList<Entidaddeterminacionregimen>(getInstance()
						.getEntidaddeterminacionregimens());
	}
	public List<Entidaddeterminacionregimen> getEntidaddeterminacionregimens_1() {
		return getInstance() == null
				? null
				: new ArrayList<Entidaddeterminacionregimen>(getInstance()
						.getEntidaddeterminacionregimens_1());
	}

}
