import java.util.Scanner;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class WarningLayout implements Layout {

  @Override
  public HBox EditLayout(Scanner scanner, Controller controller) {
    String groupChat = scanner.next();
    Chat chat = Controller.client.getChat(groupChat);
    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER);
    hBox.setStyle("-fx-padding: 5 20 5 20;");

    VBox messageVBox = new VBox();
    messageVBox.setStyle("-fx-padding: 5 5 5 5;" + 
                         "-fx-background-color: aaaaaa;" + 
                         "-fx-border-radius: 10px;");

    Text text = new Text(scanner.next());
    TextFlow textFlow = new TextFlow(text);
    text.setStyle("-fx-font-weight: 600;");

    messageVBox.getChildren().add(textFlow);
    hBox.getChildren().add(messageVBox);

    chat.notification(controller);

    chat.getVBox().getChildren().add(hBox);

    return hBox;
  }
}
