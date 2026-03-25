import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServidorFtp {

    private static final ServidorLog logger = new ServidorLog();

    public static void main(String[] args) {
        iniciarServidor();
    }

    public static void iniciarServidor() {
        logger.setupLogger();
        ServerSocket server;
        ExecutorService executor;
        try {
            server = new ServerSocket(2121);
            logger.info("Servidor conectado na porta: " + server.getLocalPort());
            logger.info("Aguardando conexão do cliente...");

            executor = Executors.newFixedThreadPool(2);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Iniciando desligamento gracioso do servidor...");
                try {
                    server.close();
                } catch (IOException e) {
                    logger.error("Erro ao fechar o server.");
                }
                executor.shutdown();
                try {
                    if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                        logger.warning("Transferências demoraram muito. Forçando a parada...");
                        executor.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                }
                logger.info("Servidor encerrado com segurança.");
            }));

            while (true) {
                Socket cliente = server.accept();
                SessaoCliente sessaoCliente = new SessaoCliente(cliente);
                executor.execute(sessaoCliente);
            }
        } catch (IOException e) {
            logger.error("Não foi possível iniciar o servidor: " + e.getMessage());
        }
    }
}