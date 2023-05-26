import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class WarningLayout implements Layout {

  @Override
  public HBox EditLayout(Scanner scanner, Controller controller) {
    String groupChat = scanner.next();
    Chat chat = Controller.client.getChat(groupChat);
    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER);
    hBox.setPadding(new Insets(10, 5, 5, 10));

    Text text = new Text(scanner.next());
    TextFlow textFlow = new TextFlow(text);
    // TODO: set style ou set id

    hBox.getChildren().add(textFlow);

    chat.notification(controller);

    chat.getVBox().getChildren().add(hBox);

    return hBox;
  }
}
