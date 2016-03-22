package com.mitc.redes.editorfip.servicios.seam;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Ambitoaplicacionambito;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Conjuntoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Planentidadordenacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;


@Name("entidadHome")
public class EntidadHome extends EntityHome<Entidad> {

	@In(create = true)
	EntidadHome entidadHome;
	@In(create = true)
	TramiteHome tramiteHome;

	public void setEntidadIden(Integer id) {
		setId(id);
	}

	public Integer getEntidadIden() {
		return (Integer) getId();
	}

	@Override
	protected Entidad createInstance() {
		Entidad entidad = new Entidad();
		return entidad;
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

	public Entidad getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Ambitoaplicacionambito> getAmbitoaplicacionambitos() {
		return getInstance() == null
				? null
				: new ArrayList<Ambitoaplicacionambito>(getInstance()
						.getAmbitoaplicacionambitos());
	}
	public List<Ambitoaplicacionambito> getAmbitoaplicacionambitos_1() {
		return getInstance() == null
				? null
				: new ArrayList<Ambitoaplicacionambito>(getInstance()
						.getAmbitoaplicacionambitos_1());
	}
	public List<Conjuntoentidad> getConjuntoentidadsForIdentidadconjunto() {
		return getInstance() == null ? null : new ArrayList<Conjuntoentidad>(
				getInstance().getConjuntoentidadsForIdentidadconjunto());
	}
	public List<Conjuntoentidad> getConjuntoentidadsForIdentidadconjunto_1() {
		return getInstance() == null ? null : new ArrayList<Conjuntoentidad>(
				getInstance().getConjuntoentidadsForIdentidadconjunto_1());
	}
	public List<Conjuntoentidad> getConjuntoentidadsForIdentidadmiembro() {
		return getInstance() == null ? null : new ArrayList<Conjuntoentidad>(
				getInstance().getConjuntoentidadsForIdentidadmiembro());
	}
	public List<Conjuntoentidad> getConjuntoentidadsForIdentidadmiembro_1() {
		return getInstance() == null ? null : new ArrayList<Conjuntoentidad>(
				getInstance().getConjuntoentidadsForIdentidadmiembro_1());
	}
	public List<Documentoentidad> getDocumentoentidads() {
		return getInstance() == null ? null : new ArrayList<Documentoentidad>(
				getInstance().getDocumentoentidads());
	}
	public List<Documentoentidad> getDocumentoentidads_1() {
		return getInstance() == null ? null : new ArrayList<Documentoentidad>(
				getInstance().getDocumentoentidads_1());
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
	public List<Entidadlin> getEntidadlins() {
		return getInstance() == null ? null : new ArrayList<Entidadlin>(
				getInstance().getEntidadlins());
	}
	public List<Entidadlin> getEntidadlins_1() {
		return getInstance() == null ? null : new ArrayList<Entidadlin>(
				getInstance().getEntidadlins_1());
	}
	public List<Entidadpnt> getEntidadpnts() {
		return getInstance() == null ? null : new ArrayList<Entidadpnt>(
				getInstance().getEntidadpnts());
	}
	public List<Entidadpnt> getEntidadpnts_1() {
		return getInstance() == null ? null : new ArrayList<Entidadpnt>(
				getInstance().getEntidadpnts_1());
	}
	public List<Entidadpol> getEntidadpols() {
		return getInstance() == null ? null : new ArrayList<Entidadpol>(
				getInstance().getEntidadpols());
	}
	public List<Entidadpol> getEntidadpols_1() {
		return getInstance() == null ? null : new ArrayList<Entidadpol>(
				getInstance().getEntidadpols_1());
	}
	public List<Entidad> getEntidadsForIdentidadbase() {
		return getInstance() == null ? null : new ArrayList<Entidad>(
				getInstance().getEntidadsForIdentidadbase());
	}
	public List<Entidad> getEntidadsForIdentidadbase_1() {
		return getInstance() == null ? null : new ArrayList<Entidad>(
				getInstance().getEntidadsForIdentidadbase_1());
	}
	public List<Entidad> getEntidadsForIdentidadoriginal() {
		return getInstance() == null ? null : new ArrayList<Entidad>(
				getInstance().getEntidadsForIdentidadoriginal());
	}
	public List<Entidad> getEntidadsForIdentidadoriginal_1() {
		return getInstance() == null ? null : new ArrayList<Entidad>(
				getInstance().getEntidadsForIdentidadoriginal_1());
	}
	public List<Entidad> getEntidadsForIdpadre() {
		return getInstance() == null ? null : new ArrayList<Entidad>(
				getInstance().getEntidadsForIdpadre());
	}
	public List<Entidad> getEntidadsForIdpadre_1() {
		return getInstance() == null ? null : new ArrayList<Entidad>(
				getInstance().getEntidadsForIdpadre_1());
	}
	public List<Operacionentidad> getOperacionentidadsForIdentidad() {
		return getInstance() == null ? null : new ArrayList<Operacionentidad>(
				getInstance().getOperacionentidadsForIdentidad());
	}
	public List<Operacionentidad> getOperacionentidadsForIdentidad_1() {
		return getInstance() == null ? null : new ArrayList<Operacionentidad>(
				getInstance().getOperacionentidadsForIdentidad_1());
	}
	public List<Operacionentidad> getOperacionentidadsForIdentidadoperadora() {
		return getInstance() == null ? null : new ArrayList<Operacionentidad>(
				getInstance().getOperacionentidadsForIdentidadoperadora());
	}
	public List<Operacionentidad> getOperacionentidadsForIdentidadoperadora_1() {
		return getInstance() == null ? null : new ArrayList<Operacionentidad>(
				getInstance().getOperacionentidadsForIdentidadoperadora_1());
	}
	public List<Planentidadordenacion> getPlanentidadordenacions() {
		return getInstance() == null
				? null
				: new ArrayList<Planentidadordenacion>(getInstance()
						.getPlanentidadordenacions());
	}
	public List<Planentidadordenacion> getPlanentidadordenacions_1() {
		return getInstance() == null
				? null
				: new ArrayList<Planentidadordenacion>(getInstance()
						.getPlanentidadordenacions_1());
	}

}
