import java.io.IOException;
import java.util.Scanner;

public class Leave implements APDU {
  // LEAVE: Group//UserId//Name
  @Override
  public void primitiveProtocol(Scanner scanner, ClientHandler clientHandler) throws IOException {
    String groupName = scanner.next();
    String userId = scanner.next();
    ClientHandler.groups.get(groupName).removeMember(groupName, userId);
  }
}
