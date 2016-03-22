package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Name("determinacionHome")
public class DeterminacionHome extends EntityHome<Determinacion> {

	@In(create = true)
	DeterminacionHome determinacionHome;
	@In(create = true)
	TramiteHome tramiteHome;

	public void setDeterminacionIden(Integer id) {
		setId(id);
	}

	public Integer getDeterminacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Determinacion createInstance() {
		Determinacion determinacion = new Determinacion();
		return determinacion;
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

	public Determinacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Determinaciongrupoentidad> getDeterminaciongrupoentidadsForIddeterminacion() {
		return getInstance() == null
				? null
				: new ArrayList<Determinaciongrupoentidad>(getInstance()
						.getDeterminaciongrupoentidadsForIddeterminacion());
	}
	public List<Determinaciongrupoentidad> getDeterminaciongrupoentidadsForIddeterminacion_1() {
		return getInstance() == null
				? null
				: new ArrayList<Determinaciongrupoentidad>(getInstance()
						.getDeterminaciongrupoentidadsForIddeterminacion_1());
	}
	public List<Determinaciongrupoentidad> getDeterminaciongrupoentidadsForIddeterminaciongrupo() {
		return getInstance() == null
				? null
				: new ArrayList<Determinaciongrupoentidad>(getInstance()
						.getDeterminaciongrupoentidadsForIddeterminaciongrupo());
	}
	public List<Determinaciongrupoentidad> getDeterminaciongrupoentidadsForIddeterminaciongrupo_1() {
		return getInstance() == null
				? null
				: new ArrayList<Determinaciongrupoentidad>(
						getInstance()
								.getDeterminaciongrupoentidadsForIddeterminaciongrupo_1());
	}
	public List<Determinacion> getDeterminacionsForIddeterminacionbase() {
		return getInstance() == null ? null : new ArrayList<Determinacion>(
				getInstance().getDeterminacionsForIddeterminacionbase());
	}
	public List<Determinacion> getDeterminacionsForIddeterminacionbase_1() {
		return getInstance() == null ? null : new ArrayList<Determinacion>(
				getInstance().getDeterminacionsForIddeterminacionbase_1());
	}
	public List<Determinacion> getDeterminacionsForIddeterminacionoriginal() {
		return getInstance() == null ? null : new ArrayList<Determinacion>(
				getInstance().getDeterminacionsForIddeterminacionoriginal());
	}
	public List<Determinacion> getDeterminacionsForIddeterminacionoriginal_1() {
		return getInstance() == null ? null : new ArrayList<Determinacion>(
				getInstance().getDeterminacionsForIddeterminacionoriginal_1());
	}
	public List<Determinacion> getDeterminacionsForIdpadre() {
		return getInstance() == null ? null : new ArrayList<Determinacion>(
				getInstance().getDeterminacionsForIdpadre());
	}
	public List<Determinacion> getDeterminacionsForIdpadre_1() {
		return getInstance() == null ? null : new ArrayList<Determinacion>(
				getInstance().getDeterminacionsForIdpadre_1());
	}
	public List<Documentodeterminacion> getDocumentodeterminacions() {
		return getInstance() == null
				? null
				: new ArrayList<Documentodeterminacion>(getInstance()
						.getDocumentodeterminacions());
	}
	public List<Documentodeterminacion> getDocumentodeterminacions_1() {
		return getInstance() == null
				? null
				: new ArrayList<Documentodeterminacion>(getInstance()
						.getDocumentodeterminacions_1());
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
	public List<Entidaddeterminacion> getEntidaddeterminacions() {
		return getInstance() == null
				? null
				: new ArrayList<Entidaddeterminacion>(getInstance()
						.getEntidaddeterminacions());
	}
	public List<Entidaddeterminacion> getEntidaddeterminacions_1() {
		return getInstance() == null
				? null
				: new ArrayList<Entidaddeterminacion>(getInstance()
						.getEntidaddeterminacions_1());
	}
	public List<Opciondeterminacion> getOpciondeterminacionsForIddeterminacion() {
		return getInstance() == null
				? null
				: new ArrayList<Opciondeterminacion>(getInstance()
						.getOpciondeterminacionsForIddeterminacion());
	}
	public List<Opciondeterminacion> getOpciondeterminacionsForIddeterminacion_1() {
		return getInstance() == null
				? null
				: new ArrayList<Opciondeterminacion>(getInstance()
						.getOpciondeterminacionsForIddeterminacion_1());
	}
	public List<Opciondeterminacion> getOpciondeterminacionsForIddeterminacionvalorref() {
		return getInstance() == null
				? null
				: new ArrayList<Opciondeterminacion>(getInstance()
						.getOpciondeterminacionsForIddeterminacionvalorref());
	}
	public List<Opciondeterminacion> getOpciondeterminacionsForIddeterminacionvalorref_1() {
		return getInstance() == null
				? null
				: new ArrayList<Opciondeterminacion>(getInstance()
						.getOpciondeterminacionsForIddeterminacionvalorref_1());
	}
	public List<Operaciondeterminacion> getOperaciondeterminacionsForIddeterminacion() {
		return getInstance() == null
				? null
				: new ArrayList<Operaciondeterminacion>(getInstance()
						.getOperaciondeterminacionsForIddeterminacion());
	}
	public List<Operaciondeterminacion> getOperaciondeterminacionsForIddeterminacion_1() {
		return getInstance() == null
				? null
				: new ArrayList<Operaciondeterminacion>(getInstance()
						.getOperaciondeterminacionsForIddeterminacion_1());
	}
	public List<Operaciondeterminacion> getOperaciondeterminacionsForIddeterminacionoperadora() {
		return getInstance() == null
				? null
				: new ArrayList<Operaciondeterminacion>(
						getInstance()
								.getOperaciondeterminacionsForIddeterminacionoperadora());
	}
	public List<Operaciondeterminacion> getOperaciondeterminacionsForIddeterminacionoperadora_1() {
		return getInstance() == null
				? null
				: new ArrayList<Operaciondeterminacion>(
						getInstance()
								.getOperaciondeterminacionsForIddeterminacionoperadora_1());
	}

}
