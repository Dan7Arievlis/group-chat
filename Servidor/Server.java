import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Server {
  public static final String DELIMITER = "¨";

  ServerSocket serverSocket;
  public static Map<String, APDU> protocols = new HashMap<>();

  public Server(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;

    protocols.put("CREATE", new Create());
    protocols.put("SEND", new Send());
    protocols.put("JOIN", new Join());
    protocols.put("LEAVE", new Leave());
  }

  public void startServer() {
    try {
      System.out.println("Server started!");

      // shutdown server
      new Thread(() -> {
        try (Scanner scanner = new Scanner(System.in)) {
          while (!serverSocket.isClosed()) {
            String action = scanner.nextLine();
            if (action.compareToIgnoreCase("exit") == 0 ||
                action.compareToIgnoreCase("close") == 0) {
              throw new IOException();
            }

            if (action.compareToIgnoreCase("users") == 0) {
              if (ClientHandler.clientHandlers != null) {
                System.out.print("[");
                Iterator<ClientHandler> users = ClientHandler.clientHandlers.values().iterator();
                while (users.hasNext()) {
                  System.out.print(users.next().getClientUserName());
                  if(users.hasNext())
                    System.out.print(", ");
                }
                System.out.println("]");
              }
            }

            if (action.compareToIgnoreCase("groups") == 0) {
              if (ClientHandler.groups != null) {
                System.out.print("[");
                Iterator<String> groups = ClientHandler.groups.keySet().iterator();
                while(groups.hasNext()) {
                  System.out.print(groups.next());
                  if(groups.hasNext())
                    System.out.print(", ");
                }
                System.out.println("]");

                for (Entry<String, Group> group : ClientHandler.groups.entrySet()) {
                  System.out.println("Group " + group.getValue() + ":" + group.getValue().getMembers());
                }
              }
            }
          }
        } catch (Exception e) {
          closeServerSocket();
        }
      }).start();

      while (!serverSocket.isClosed()) {
        Socket socket = serverSocket.accept();
        System.out.println("A new client has connected!");
        ClientHandler clientHandler = new ClientHandler(socket);

        Thread thread = new Thread(clientHandler);
        thread.start();
      }
    } catch (Exception e) {
      closeServerSocket();
    }
  }

  public void closeServerSocket() {
    try {
      serverSocket.close();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }
}
