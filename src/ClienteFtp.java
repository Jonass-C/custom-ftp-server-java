import javax.swing.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClienteFtp {

    private static final ServidorLog logger = new ServidorLog();
    private static Socket socket;

    public static void main(String[] args) {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Selecione o arquivo para enviar");
        seletor.setMultiSelectionEnabled(true);

        if (seletor.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File[] arquivos = seletor.getSelectedFiles();

            for (File arquivo : arquivos) {
                FileInputStream fis = null;

                try {
                    socket = new Socket("localhost", 2121);
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    fis = new FileInputStream(arquivo);
                    logger.info("Conectado! Preparando para enviar: " + arquivo.getName());

                    dos.writeUTF(arquivo.getName());
                    dos.writeLong(arquivo.length());

                    byte[] buffer = new byte[4096];
                    int lidosNoCiclo;
                    while ((lidosNoCiclo = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, lidosNoCiclo);
                    }
                    dos.flush();
                    logger.info("Arquivo enviado com sucesso para o servidor!");

                } catch (IOException e) {
                    logger.error("Erro ao conectar no servidor: " + e.getMessage());
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException e) {
                        logger.error("Erro ao fechar os recursos do cliente.");
                    }
                }
            }
        } else {
            logger.warning("Envio cancelado pelo usuário.");
        }
    }

}
