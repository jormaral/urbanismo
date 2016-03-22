package es.mitc.redes.urbanismoenred.consola.filtros;

import org.apache.commons.fileupload.ProgressListener;

/**
 *
 * @author josecarlos.diaz
 */
public class FileUploadListener implements ProgressListener {
	
	public static final String NOMBRE_ATRIBUTO = "FipListeners";

    private volatile long bytesRead = 0L, contentLength = 0L, item = 0L;
    
    private String tramite;

    public FileUploadListener() {
    	super();
    }

    public void update(long aBytesRead, long aContentLength, int anItem) {
    	bytesRead = aBytesRead;
    	contentLength = aContentLength;
    	item = anItem;
    }

    public long getBytesRead() {
    	return bytesRead;
    }

    public long getContentLength() {
    	return contentLength;
    }

    public long getItem() {
    	return item;
    }

	public void setTramite(String tramite) {
		this.tramite = tramite;
	}

	public String getTramite() {
		return tramite;
	}
}
