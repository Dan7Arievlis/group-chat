import java.io.IOException;
import java.util.Scanner;

public class Send implements APDU {

  // SEND: Group//UserId//Name//Message//Date
  @Override
  public void primitiveProtocol(Scanner scanner, ClientHandler clientHandler) throws IOException {
    String groupName = scanner.next();
    Group group = ClientHandler.groups.get(groupName);
    String userId = scanner.next();
    for (String member : group.getMembers()) {
      clientHandler = ClientHandler.clientHandlers.get(member);
      if(!clientHandler.equals(userId)) {
        String messageToSend = "CHAT" + Server.DELIMITER + groupName + Server.DELIMITER + scanner.nextLine();
        clientHandler.writeMessage(messageToSend);
      }
    }
  }
}
