/*
 * Copyright 2011 red.es
 * Autores: Arnaiz Consultores.
 *
 ** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
 * sean aprobadas por la Comision Europea- versiones
 * posteriores de la EUPL (la <<Licencia>>);
 * Solo podra usarse esta obra si se respeta la Licencia.
 * Puede obtenerse una copia de la Licencia en:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 * Salvo cuando lo exija la legislacion aplicable o se acuerde.
 * por escrito, el programa distribuido con arreglo a la
 * Licencia se distribuye <<TAL CUAL>>,
 * SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
 * ni implicitas.
 * Vease la Licencia en el idioma concreto que rige
 * los permisos y limitaciones que establece la Licencia.
 */
package es.mitc.redes.urbanismoenred.servicios.configuracion;

import es.mitc.redes.urbanismoenred.utils.configuracion.RedesConfig;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Arnaiz Consultores
 */
@Stateless(name = "ConfiguradorVisor")
public class ConfiguracionVisorBean implements ConfiguracionVisorLocal {

    private static final Logger log = Logger.getLogger(ConfiguracionVisorBean.class);

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.configuracion.ConfiguracionVisorLocal#getLayerConfigDeAmbito(es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario,
     * int)
     */
    @Override
    public InputStream getPerfiles() {
        String directorio = RedesConfig.getREDES_PATH() + Textos.getTexto("rutas", "ConfigVisorPath");

        File dir = new File(directorio);

        File file = new File(dir, "perfiles.xml");


        if (!file.exists()) {
            log.error("El archivo de configuración perfiles.xml (" + file.getAbsolutePath() + ")" + " no existe");
            return null;
        }
        InputStream configFIS;
        try {
            configFIS = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            log.error("No se encuentra el fichero " + file.toString(), ex);
            return null;
        }
        log.info("Archivo devuelto: " + file.getAbsolutePath());
        return configFIS;
    }

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.configuracion.ConfiguracionVisorLocal#getLayerConfigDeAmbito(es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario,
     * int)
     */
    @Override
    public InputStream getLayerConfigDeAmbito(int idAmbito) {
        if (idAmbito < 0) {
            log.error("El ámbito pasado es inválido (" + idAmbito + ")");
            return null;
        }

        File configFile = getConfigFileDeAmbito(idAmbito);
        if (configFile == null || !configFile.exists()) {
            if (configFile != null) {
                log.warn("El archivo de configuración " + configFile.getName() + "(" + configFile.getAbsolutePath() + ")" + " no existe");
            }
            return null;
        }
        InputStream configFIS;
        try {
            configFIS = new FileInputStream(configFile);
        } catch (FileNotFoundException ex) {
            log.error("No se encuentra el fichero " + configFile.toString(), ex);
            return null;
        }
        log.info("Archivo devuelto: " + configFile.getAbsolutePath());
        return configFIS;
    }

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.servicios.configuracion.ConfiguracionVisorLocal#guardarConfiguracion(es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario,
     * int, java.lang.String)
     */
    @Override
    public boolean guardarConfiguracion(int idAmbito, String configFileContent) throws IOException {
        if (idAmbito < 0) {
            log.error("El ámbito pasado es inválido (" + idAmbito + ")");
            return false;
        }
        File configFile = this.getConfigFileDeAmbito(idAmbito);
        if (configFile != null) {
            FileWriter configFileWriter = new FileWriter(configFile, false);
            configFileWriter.write(configFileContent);
            configFileWriter.flush();
            configFileWriter.close();
            return true;
        } else {
            return false;
        }
    }

    private File getConfigFileDeAmbito(int idAmbito) {
        log.info("Petición de fichero de configuración de capas para ámbito " + idAmbito);

        try {
            String directorio = RedesConfig.getREDES_PATH() + Textos.getTexto("rutas", "ConfigVisorPath");

            File dir = new File(directorio);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    log.error("No se ha podido crear el directorio de configuración en la ruta " + directorio);
                    return null;
                }

            }
            File file = new File(dir, "capas_" + idAmbito + ".xml");
            if (!file.exists()) {
                file = new File(dir, "capas_default.xml");
            }
            return file;
        } catch (Exception e) {
            log.error("Error al leer el fichero de configuración de visor", e);
        }
        return null;
    }
}
