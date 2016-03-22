package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;

@Name("entidaddeterminacionregimenHome")
public class EntidaddeterminacionregimenHome
		extends
			EntityHome<Entidaddeterminacionregimen> {

	@In(create = true)
	CasoentidaddeterminacionHome casoentidaddeterminacionHome;
	@In(create = true)
	DeterminacionHome determinacionHome;
	@In(create = true)
	OpciondeterminacionHome opciondeterminacionHome;

	public void setEntidaddeterminacionregimenIden(Integer id) {
		setId(id);
	}

	public Integer getEntidaddeterminacionregimenIden() {
		return (Integer) getId();
	}

	@Override
	protected Entidaddeterminacionregimen createInstance() {
		Entidaddeterminacionregimen entidaddeterminacionregimen = new Entidaddeterminacionregimen();
		return entidaddeterminacionregimen;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Casoentidaddeterminacion casoentidaddeterminacionByIdcaso = casoentidaddeterminacionHome
				.getDefinedInstance();
		if (casoentidaddeterminacionByIdcaso != null) {
			getInstance().setCasoentidaddeterminacionByIdcaso(
					casoentidaddeterminacionByIdcaso);
		}
		Casoentidaddeterminacion casoentidaddeterminacionByIdcasoaplicacion = casoentidaddeterminacionHome
				.getDefinedInstance();
		if (casoentidaddeterminacionByIdcasoaplicacion != null) {
			getInstance().setCasoentidaddeterminacionByIdcasoaplicacion(
					casoentidaddeterminacionByIdcasoaplicacion);
		}
		Determinacion determinacion = determinacionHome.getDefinedInstance();
		if (determinacion != null) {
			getInstance().setDeterminacion(determinacion);
		}
		Opciondeterminacion opciondeterminacion = opciondeterminacionHome
				.getDefinedInstance();
		if (opciondeterminacion != null) {
			getInstance().setOpciondeterminacion(opciondeterminacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getCasoentidaddeterminacionByIdcaso() == null)
			return false;
		return true;
	}

	public Entidaddeterminacionregimen getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Regimenespecifico> getRegimenespecificos() {
		return getInstance() == null ? null : new ArrayList<Regimenespecifico>(
				getInstance().getRegimenespecificos());
	}
	public List<Regimenespecifico> getRegimenespecificos_1() {
		return getInstance() == null ? null : new ArrayList<Regimenespecifico>(
				getInstance().getRegimenespecificos_1());
	}

}
