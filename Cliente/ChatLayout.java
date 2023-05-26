import java.time.LocalTime;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ChatLayout implements Layout {
  @Override
  public HBox EditLayout(Scanner scanner, Controller controller) {
    String groupChat = scanner.next();
    Chat chat = Controller.client.getChat(groupChat);
    scanner.next();
    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setPadding(new Insets(5, 5, 5, 10));

    // add nome do usuario a alinhar na hbox
    Label nameLabel = new Label(scanner.next());
    nameLabel.setAlignment(Pos.CENTER_LEFT);
    nameLabel.setPadding(new Insets(0, 5, 0, 5));

    Text text = new Text(scanner.next());
    TextFlow textFlow = new TextFlow(text);
    // TODO: set style ou set id

    // add data e alinhar na hbox
    LocalTime date = LocalTime.parse(scanner.next());
    Label dateLabel = new Label(String.format("%d:%d", date.getHour(), date.getMinute()));
    dateLabel.setAlignment(Pos.CENTER_RIGHT);
    dateLabel.setPadding(new Insets(0, 5, 0, 5));

    hBox.getChildren().add(nameLabel);
    hBox.getChildren().add(textFlow);
    hBox.getChildren().add(dateLabel);
    
    chat.getVBox().getChildren().add(hBox);

    chat.notification(controller);
  
    return hBox;
  }
}
