import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
  public static Map<String, ClientHandler> clientHandlers = new HashMap<>();
  public static Map<String, Group> groups = new ConcurrentHashMap<>();
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;

  private String clientUserName;
  private String userId;

  public ClientHandler(Socket socket) {
    try {
      this.socket = socket;
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    } catch (IOException e) {
      closeEverything(socket, bufferedReader, bufferedWriter);
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
      nameScanner = new Scanner(bufferedReader.readLine());
      nameScanner.useDelimiter(Server.DELIMITER);
      Server.protocols.get("CREATE").primitiveProtocol(nameScanner, this);

      while (socket.isConnected()) {
        try (Scanner scanner = new Scanner(bufferedReader.readLine())) {
          scanner.useDelimiter(Server.DELIMITER);
          // TODO: Scanner
          Server.protocols.get(scanner.next()).primitiveProtocol(scanner, this);
        } catch (IOException e) {
          closeEverything(socket, bufferedReader, bufferedWriter);
          break;
        }
      }
    } catch (IOException e) {
      closeEverything(socket, bufferedReader, bufferedWriter);
    } finally {
      if (nameScanner != null)
        nameScanner.close();
    }
  }

  public void writeMessage(String messageToSend) throws IOException {
    try (Scanner scanner = new Scanner(messageToSend)) {
      bufferedWriter.write(messageToSend);
      bufferedWriter.newLine();
      bufferedWriter.flush();
    }
  }

  public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

      if (bufferedReader != null)
        bufferedReader.close();

      if (bufferedWriter != null)
        bufferedWriter.close();
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
