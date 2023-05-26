import java.io.IOException;
import java.util.Scanner;

public interface APDU {

  /*
   V CREATE: Protocol//UserId//Name
   V SEND: Protocol//Group//UserId//Name//Message//Date
   V JOIN: Protocol//Group//UserId//Name
   V LEAVE: Protocol//Group//UserId//Name
   */
  public void primitiveProtocol(Scanner scanner, ClientHandler clientHandler) throws IOException;
}
