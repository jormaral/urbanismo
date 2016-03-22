package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;

@Name("vinculocasoHome")
public class VinculocasoHome extends EntityHome<Vinculocaso> {

	@In(create = true)
	CasoentidaddeterminacionHome casoentidaddeterminacionHome;

	public void setVinculocasoIden(Integer id) {
		setId(id);
	}

	public Integer getVinculocasoIden() {
		return (Integer) getId();
	}

	@Override
	protected Vinculocaso createInstance() {
		Vinculocaso vinculocaso = new Vinculocaso();
		return vinculocaso;
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
		Casoentidaddeterminacion casoentidaddeterminacionByIdcasovinculado = casoentidaddeterminacionHome
				.getDefinedInstance();
		if (casoentidaddeterminacionByIdcasovinculado != null) {
			getInstance().setCasoentidaddeterminacionByIdcasovinculado(
					casoentidaddeterminacionByIdcasovinculado);
		}
	}

	public boolean isWired() {
		if (getInstance().getCasoentidaddeterminacionByIdcaso() == null)
			return false;
		if (getInstance().getCasoentidaddeterminacionByIdcasovinculado() == null)
			return false;
		return true;
	}

	public Vinculocaso getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
