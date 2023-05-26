import java.io.IOException;
import java.util.Scanner;

public class Join implements APDU {
  // JOIN: Group//UserId//Name
  @Override
  public void primitiveProtocol(Scanner scanner, ClientHandler clientHandler) throws IOException {
    String groupName = scanner.next();
    Group group = ClientHandler.getGroup(groupName);
    String userId = scanner.next();
    String name = scanner.next();
    if (ClientHandler.addMemberToGroup(userId, groupName)) {
      for (String member : group.getMembers()) {
        ClientHandler user = ClientHandler.clientHandlers.get(member);
        if (!user.equals(userId)) {
          user.writeMessage("WARNING" + Server.DELIMITER + groupName + Server.DELIMITER + "SERVER: " + name + " has entered the chat.");
        } else {
          user.writeMessage("GROUP" + Server.DELIMITER + groupName);
          user.writeMessage("WARNING" + Server.DELIMITER + groupName + Server.DELIMITER + "SERVER: you has entered the chat.");
        }
      }
    }
  }
}
