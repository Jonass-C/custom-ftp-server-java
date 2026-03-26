import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
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

            String nome = new File(nomeArquivo).getName();

            File upload = new File("uploads");
            if (!upload.exists()) {
                upload.mkdir();
            }

            File arquivoDestino = new File(upload, nome);
            try (FileOutputStream fos = new FileOutputStream(arquivoDestino)) {
                byte[] buffer = new byte[4096];
                long totalLidos = 0;
                int lidosNoCiclo;

                while (totalLidos < tamanhoArquivo) {
                    int bytesParaLer = (int) Math.min(buffer.length, tamanhoArquivo - totalLidos);
                    lidosNoCiclo = dis.read(buffer, 0, bytesParaLer);

                    if (lidosNoCiclo == -1) {
                        throw new IOException("Fim inesperado do stream de dados.");
                    }

                    fos.write(buffer, 0, lidosNoCiclo);
                    totalLidos += lidosNoCiclo;
                }
                logger.info("Transferência concluída: " + nome + " salvo com sucesso!");
            }

        } catch (SocketTimeoutException e) {
            logger.warning("Cliente " + ipCliente + " desconectado por inatividade!");
        } catch (IOException e) {
            logger.warning("Cliente " + ipCliente + " falhou! Motivo: " +  e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                cliente.close();
            } catch (IOException e) {
                logger.error("Erro ao fechar o socket do cliente.");
            }
        }
    }

}
