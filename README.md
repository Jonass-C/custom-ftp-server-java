# 🚀 Custom Java FTP Server

Um servidor de transferência de arquivos (FTP) construído do zero utilizando **Java Sockets** e fluxos binários (DataStreams), sem bibliotecas externas.  
Este projeto demonstra a implementação de uma arquitetura Cliente-Servidor robusta para transferência segura de arquivos em rede local (LAN).

## ✨ Funcionalidades Arquiteturais

* **Transferência Binária (Chunking):** Leitura e escrita otimizada em blocos de 4KB, permitindo o tráfego de arquivos pesados sem sobrecarregar a JVM.
* **Múltiplas Conexões Simultâneas:** Utilização de `ExecutorService` (Thread Pool) para múltiplos uploads paralelos.
* **Envio em Lote (Batch Upload):** Capacidade de enviar múltiplos arquivos de uma vez para o servidor.
* **Graceful Shutdown:** Padrão de encerramento em duas fases (*Two-Phase Termination*), garantindo que arquivos em transferência não sejam corrompidos caso o servidor seja desligado.
* **Segurança contra Path Traversal:** Validação do nome dos arquivos para impedir ataques de manipulação de diretórios.
* **Interface Gráfica (Swing):** Uso de `JFileChooser` para seleção visual dos arquivos.

## 🛠️ Tecnologias Utilizadas
* **100% Core Java** (I/O Streams, Sockets TCP/IP, Concurrency, Swing para UI)

## 💻 Como Executar

### 1. Compile o projeto
O comando abaixo compila todos os arquivos `.java` da pasta `src` e coloca os binários em uma pasta chamada `out`.

```bash
javac src/*.java -d out/
```

### 2. Iniciando o Servidor
Abra o terminal na máquina que receberá os arquivos (Máquina Host) e execute a classe principal do servidor. Ele passará a escutar na porta `2121`.

```bash
java -cp out/ ServidorFtp
```
*Nota: Os arquivos recebidos serão salvos automaticamente em uma pasta chamada `uploads/` criada na raiz de execução.*

### 3. Iniciando o Cliente (Upload)
Na máquina remetente (ou em outro terminal da mesma máquina para testes locais), compile o passo 1 e execute o cliente:

```bash
java -cp out/ ClienteFtp
```
Uma janela do sistema operacional será aberta. Selecione o(s) arquivo(s) desejado(s) e acompanhe o log de transferência pelo terminal!

### 🌍 Testando em Máquinas Diferentes (Ex: Host <-> Máquina Virtual)
Para transferir arquivos entre dois computadores na mesma rede:
1. Descubra o IP da máquina Servidora (ex: `192.168.1.15`).
2. No código fonte do `ClienteFtp.java`, altere a linha de conexão substituindo `"localhost"` pelo IP do servidor:
   `socket = new Socket("192.168.1.15", 2121);`
3. Recompile as classes e rode o Cliente!