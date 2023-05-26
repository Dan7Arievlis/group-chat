import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Principal {
  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Numero da port do servidor: ");
    int port = scanner.nextInt();
    ServerSocket serverSocket = new ServerSocket(port);
    Server server = new Server(serverSocket);
    server.startServer();

    scanner.close();
  }
}
