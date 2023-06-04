import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
  public static Map<String, ClientHandler> clientHandlers = new HashMap<>();
  public static Map<String, Group> groups = new ConcurrentHashMap<>();
  
  private Socket socket;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;

  private String clientUserName;
  private String userId;

  public ClientHandler(Socket socket) {
    try {
      this.socket = socket;
      this.inputStream = new ObjectInputStream(socket.getInputStream());
      this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      closeEverything(socket, inputStream, outputStream);
    }
  }

  public String getClientUserName() {
    return clientUserName;
  }

  public void setClientUserName(String clientUserName) {
    this.clientUserName = clientUserName;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public static Group getGroup(String groupName) {
    if (groups.get(groupName) == null)
      groups.put(groupName, new Group(groupName));
    return groups.get(groupName);
  }

  public static boolean addMemberToGroup(String userId, String groupName) {
    if (groups.get(groupName) == null) {
      groups.put(groupName, new Group(groupName));
    }
    return groups.get(groupName).addMember(userId);
  }

  @Override
  public void run() {
    Scanner nameScanner = null;
    try {
      String nameString = (String) inputStream.readObject();
      nameScanner = new Scanner(nameString);
      nameScanner.useDelimiter(Server.DELIMITER);
      Server.protocols.get("CREATE").primitiveProtocol(nameScanner, this);

      while (socket.isConnected()) {
        String message = (String) inputStream.readObject();
        try (Scanner scanner = new Scanner(message)) {
          scanner.useDelimiter(Server.DELIMITER);

          Server.protocols.get(scanner.next()).primitiveProtocol(scanner, this);
        } catch (IOException e) {
          closeEverything(socket, inputStream, outputStream);
          break;
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      closeEverything(socket, inputStream, outputStream);
    } finally {
      if (nameScanner != null)
        nameScanner.close();
    }
  }

  public void writeMessage(String messageToSend) throws IOException {
    outputStream.writeObject(messageToSend);
  }

  public void closeEverything(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
    for (Group group : groups.values()) {
      if (group.getMembers().contains(this.userId)) {
        try {
          group.removeMember(group.toString(), this.userId);
        } catch (IOException e) {}
      }
    }
    clientHandlers.remove(this.userId);

    try {
      if (socket != null)
        socket.close();

      if (inputStream != null)
        inputStream.close();

      if (outputStream != null)
        outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public int hashCode() {
    return userId.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return userId.equals(obj);
  }

  @Override
  public String toString() {
    return this.userId;
  }
}
