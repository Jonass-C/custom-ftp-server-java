import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServidorLog {

    private static final Logger log = Logger.getLogger(ServidorLog.class.getName());

    public void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("servidor.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            log.addHandler(fileHandler);
            log.setLevel(Level.INFO);
            log.setUseParentHandlers(true);
        } catch (IOException e) {
            System.err.println("Erro ao inicializar log: " + e.getMessage());
        }
    }

    public void info(String mensagem) {
        log.info(mensagem);
    }
    public void warning(String mensagem) {
        log.warning(mensagem);
    }
    public void error(String mensagem) {
        log.severe(mensagem);
    }

}
