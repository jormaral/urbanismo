package es.mitc.redes.urbanismoenred.utils.configuracion;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author javier.molina
 */
public class JobLogger {

    private static Hashtable<String, Logger> m_loggers = new Hashtable<String, Logger>();
    private static String m_filename = RedesConfig.getREDES_PATH() + File.separator + "var" + File.separator + "logs" + File.separator;

    public static synchronized void logMessage(String jobName, String message) {
        Logger l = getJobLogger(jobName);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String fecha=sdf.format(cal.getTime());
        l.info(fecha+": "+message);
    }

    private static synchronized Logger getJobLogger(String jobName) {
        Logger logger = m_loggers.get(jobName);
        if (logger == null) {
            Layout layout = new PatternLayout();
            logger = Logger.getLogger(jobName);
            m_loggers.put(jobName, logger);
            try {
                File file = new File(m_filename);
                file = new File(m_filename + jobName + ".log");
                FileAppender appender = new FileAppender(layout, file.getAbsolutePath(), false);
                logger.removeAllAppenders();  
                logger.addAppender(appender);
            } catch (Exception e) {
                System.out.println("Error generando fichero de Log. " + e);
            }
        }
        return logger;
    }
}
