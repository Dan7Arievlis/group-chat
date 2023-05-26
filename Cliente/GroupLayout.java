import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GroupLayout implements Layout {

  @Override
  public HBox EditLayout(Scanner scanner, Controller controller) {
    String groupChat = scanner.next();
    HBox hBox = new HBox();
    hBox.prefHeight(30);
    hBox.prefWidth(247);
    Chat chat = new Chat(groupChat, vBoxSettings(controller.getScrollPane()));
    Controller.client.addChat(chat);

    Button deleteButton = new Button("Deletar");
    deleteButton.setOnAction(event -> {
      Controller.client.leaveChat(chat);
      controller.getVBoxGroup().getChildren().remove(controller.getVBoxGroup().getChildren().indexOf(hBox));
    });
    // TODO: style botao

    Label chatLabel = new Label(groupChat);
    chatLabel.setAlignment(Pos.CENTER_LEFT);
    chatLabel.setPadding(new Insets(5, 5, 5, 10));

    Label newLabel = new Label("new");
    newLabel.setAlignment(Pos.CENTER_RIGHT);
    newLabel.setPadding(new Insets(5, 5, 5, 10));
    newLabel.setVisible(false);
    // TODO: style label

    chat.setNotificationLabel(newLabel);

    hBox.setOnMouseClicked(event -> {
      if (event.getButton() == MouseButton.PRIMARY) {
        if (event.getClickCount() == 2 || event.getClickCount() == 1) {
          Controller.client.selectChat(groupChat);
        }
      }
    });

    hBox.getChildren().add(deleteButton);
    hBox.getChildren().add(chatLabel);
    hBox.getChildren().add(newLabel);

    controller.getVBoxGroup().getChildren().add(0, hBox);
    chat.setGroupHBox(hBox);

    // switch chats
    Controller.client.selectChat(groupChat);
   
    return hBox;
  }

  private VBox vBoxSettings(ScrollPane sp_message) {
    // TODO: add group hBox
    VBox chatVBox = new VBox();
    chatVBox.prefWidth(526);
    chatVBox.prefHeight(381);
    chatVBox.heightProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        sp_message.setVvalue((Double) newValue);
      }
    });

    return chatVBox;
  }
}
