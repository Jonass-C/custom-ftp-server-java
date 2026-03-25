import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClienteFtp {

    private static final ServidorLog logger = new ServidorLog();
    private static Socket socket;

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 2121);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String nomeArquivo = "teste.txt";
            long tamanhoArquivo = 1024L;

            dos.writeUTF(nomeArquivo);
            dos.writeLong(tamanhoArquivo);
            dos.flush();

        } catch (IOException e) {
            logger.error("Erro ao conectar no servidor: " + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error("Erro ao fechar o socket do cliente.");
                }
            }
        }
    }

}
