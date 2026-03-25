import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SessaoCliente implements Runnable {

    private static final ServidorLog logger = new ServidorLog();
    private final Socket cliente;

    public SessaoCliente(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {
        String ipCliente = cliente.getInetAddress().getHostAddress();
        try {
            logger.info("Nova conexão realizada!");
            logger.info("Cliente conectado no IP: " + ipCliente);
            cliente.setSoTimeout(60000);

            DataInputStream dis = new DataInputStream(cliente.getInputStream());
            String nomeArquivo = dis.readUTF();
            long tamanhoArquivo = dis.readLong();
            logger.info("Recebendo arquivo: " +  nomeArquivo + " - Tamanho: " + tamanhoArquivo + " bytes");

        } catch (SocketTimeoutException e) {
            logger.warning("Cliente " + ipCliente + " desconectado por inatividade!");
        } catch (IOException e) {
            logger.warning("Cliente " + ipCliente + " desconectou de forma abrupta!");
        } finally {
            try {
                cliente.close();
            } catch (IOException e) {
                logger.error("Erro ao fechar o socket do cliente.");
            }
        }
    }

}
