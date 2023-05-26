import java.io.IOException;
import java.util.Scanner;

public class Create implements APDU {

  // CREATE: UserId//Name
  @Override
  public void primitiveProtocol(Scanner scanner, ClientHandler clientHandler) throws IOException {
    String userId = scanner.next();
    String userName = scanner.next();
    clientHandler.setUserId(userId);
    clientHandler.setClientUserName(userName);
    ClientHandler.clientHandlers.put(userId, clientHandler);
  }
}
