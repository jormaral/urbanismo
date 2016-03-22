package com.mitc.redes.editorfip.servicios;

import javax.ejb.Local;

@Local
public interface Authenticator {

	public boolean authenticate();
	
}
