package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;

@Name("regimenespecificoHome")
public class RegimenespecificoHome extends EntityHome<Regimenespecifico> {

	@In(create = true)
	EntidaddeterminacionregimenHome entidaddeterminacionregimenHome;
	@In(create = true)
	RegimenespecificoHome regimenespecificoHome;

	public void setRegimenespecificoIden(Integer id) {
		setId(id);
	}

	public Integer getRegimenespecificoIden() {
		return (Integer) getId();
	}

	@Override
	protected Regimenespecifico createInstance() {
		Regimenespecifico regimenespecifico = new Regimenespecifico();
		return regimenespecifico;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Entidaddeterminacionregimen entidaddeterminacionregimen = entidaddeterminacionregimenHome
				.getDefinedInstance();
		if (entidaddeterminacionregimen != null) {
			getInstance().setEntidaddeterminacionregimen(
					entidaddeterminacionregimen);
		}
	}

	public boolean isWired() {
		if (getInstance().getEntidaddeterminacionregimen() == null)
			return false;
		return true;
	}

	public Regimenespecifico getDefinedInstance() {
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
