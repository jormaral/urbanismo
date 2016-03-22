package com.mitc.redes.editorfip.servicios;

import javax.ejb.Local;

@Local
public interface RememberPass {

	public void remember();
	
	public boolean isMostrarPopUp();
	public void setMostrarPopUp(boolean mostrarPopUp);
	public String getCampo();
	public void setCampo(String campo);
	public boolean isByUsername();
	public void setByUsername(boolean byUsername);
}
