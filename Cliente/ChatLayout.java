import java.time.LocalTime;
import java.util.Scanner;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ChatLayout implements Layout {
  @Override
  public HBox EditLayout(Scanner scanner, Controller controller) {
    String groupChat = scanner.next();
    Chat chat = Controller.client.getChat(groupChat);
    
    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setStyle("-fx-padding: 5 5 5 15;");
    
    VBox messageVBox = new VBox();
    messageVBox.setStyle("-fx-padding: 5 5 5 5;" + 
                         "-fx-background-color: fbfbfb;" + 
                         "-fx-border-radius: 20;");

    // add nome do usuario a alinhar na hbox
    Label nameLabel = new Label(scanner.next());
    nameLabel.setAlignment(Pos.CENTER_LEFT);
    nameLabel.setStyle("-fx-font-weight: bold;");

    Text text = new Text(scanner.next());
    TextFlow textFlow = new TextFlow(text);

    // add data e alinhar na hbox
    LocalTime date = LocalTime.parse(scanner.next());
    Label dateLabel = new Label(String.format("%02d:%02d", date.getHour(), date.getMinute()));
    dateLabel.setAlignment(Pos.CENTER_RIGHT);
    dateLabel.setStyle("-fx-font-weight: 200;");

    messageVBox.getChildren().add(nameLabel);
    messageVBox.getChildren().add(textFlow);
    messageVBox.getChildren().add(dateLabel);
    hBox.getChildren().add(messageVBox);
    
    chat.getVBox().getChildren().add(hBox);

    chat.notification(controller);
  
    return hBox;
  }
}
