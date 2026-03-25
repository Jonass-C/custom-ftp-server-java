import java.net.Socket;

public class SessaoCliente implements Runnable {

    private static final ServidorLog logger = new ServidorLog();
    private final Socket cliente;

    public SessaoCliente(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {}

}
