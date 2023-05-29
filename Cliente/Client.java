import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.scene.layout.VBox;

public class Client {
  public static final String DELIMITER = "¨";
  private Controller controller;

  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private Map<String, Chat> chats;

  private String userId;
  private String userName;
  private String currentChat = "";

  public Client(Socket socket, String userName, Controller controller) {
    this.controller = controller;
    try {
      this.socket = socket;
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.userName = userName;
      this.chats = new HashMap<>();

      this.userId = "" + System.currentTimeMillis();
      bufferedWriter.write(Client.DELIMITER + this.userId + Client.DELIMITER + this.userName);
      bufferedWriter.newLine();
      bufferedWriter.flush();

    } catch (IOException e) {
      closeEverything(socket, bufferedReader, bufferedWriter);
    }
  }

  public String getUserName() {
    return userName;
  }

  public String getCurrentChat() {
    return currentChat;
  }

  public void setCurrentChat(String currentGroup) {
    this.currentChat = currentGroup;
  }

  public Chat getChat(String chatName) {
    return chats.get(chatName);
  }

  public void recieveMessageFromServer(VBox vBox, VBox vBoxGroups) {
    new Thread(() -> extracted(vBox)).start();
  }

  private void extracted(VBox vBox) {
    Scanner scanner = null;
    try {
      while (socket.isConnected()) {
        scanner = new Scanner(bufferedReader.readLine());
        scanner.useDelimiter(Client.DELIMITER);
        String protocol = scanner.next();

        this.controller.addLabel(protocol, scanner);
      }
    } catch (IOException e) {
      e.printStackTrace();
      closeEverything(socket, bufferedReader, bufferedWriter);
    }
  }

  public void sendMessageToServer(String messageToSend) {
    try {
      bufferedWriter.write(messageToSend);
      bufferedWriter.newLine();
      bufferedWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
      closeEverything(socket, bufferedReader, bufferedWriter);
    }
  }

  public void sendMessageToChat(String messageToSend, String date) {
    // SEND: Protocol//Group//UserId//Name//Message//Date
    String message = "SEND" + Client.DELIMITER + this.currentChat + Client.DELIMITER +
        this.userId + Client.DELIMITER + this.userName + Client.DELIMITER +
        messageToSend + Client.DELIMITER + date;
    sendMessageToServer(message);
  }

  public void searchChat(String chatName) {
    // JOIN: Protocol//Group//UserId//Name
    String message = "JOIN" + Client.DELIMITER + chatName + Client.DELIMITER + this.userId
        + Client.DELIMITER + this.userName;
    sendMessageToServer(message);
  }

  public void leaveChat(Chat chat) {
    // LEAVE: Protocol//Group//UserId//Name
    String message = "LEAVE" + Client.DELIMITER + chat.toString() + Client.DELIMITER +
        this.userId + Client.DELIMITER + this.userName;
    sendMessageToServer(message);
    removeChat(chat);
    if (chats.keySet().isEmpty()) {
      selectChat(null);
    } else if(!chats.keySet().isEmpty() && chat.equals(this.currentChat)) {
      String newChat = (String) chats.keySet().toArray()[0];
      selectChat(newChat);
    }
  }

  public void addChat(Chat chat) {
    chats.put(chat.getName(), chat);
  }

  public void removeChat(Chat chat) {
    chats.remove(chat.getName());
  }

  public VBox getChatVBox(String chat) {
    if(chats.get(chat) == null)
      return null;
    return chats.get(chat).getVBox();
  }

  public void selectChat(String newGroup) {
    if(this.currentChat == null) 
      this.currentChat = "";
    
    if (!this.currentChat.equals(newGroup)) {
      this.currentChat = newGroup;

      controller.setScrollPaneContent(currentChat);
      if(this.getChat(this.currentChat) != null)
        this.getChat(this.currentChat).setNotificationLabelVisible(false);
    }
  }

  public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
}
