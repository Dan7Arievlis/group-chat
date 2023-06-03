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
    hBox.setStyle("-fx-padding: 5 5 5 5;");

    Chat chat = new Chat(groupChat, vBoxSettings(controller.getScrollPane()));
    Controller.client.addChat(chat);

    Button deleteButton = new Button("Deletar");
    deleteButton.setOnAction(event -> {
      Controller.client.leaveChat(chat);
      controller.getVBoxGroup().getChildren().remove(controller.getVBoxGroup().getChildren().indexOf(hBox));
    });
    deleteButton.setStyle("-fx-background-color: lightgray;" + 
                            "-fx-font-weight: bold;");

    deleteButton.setOnMouseEntered(event -> {
      deleteButton.setStyle("-fx-background-color: ff0000;" + 
                            "-fx-font-weight: bold;");
    });

    deleteButton.setOnMouseExited(event -> {
      deleteButton.setStyle("-fx-background-color: lightgray;" + 
                            "-fx-font-weight: bold;");
    });

    Label chatLabel = new Label(groupChat);
    chatLabel.setAlignment(Pos.CENTER_LEFT);
    chatLabel.setPadding(new Insets(5, 5, 5, 5));
    chatLabel.setStyle("-fx-padding: 5 10 5 10;" + 
                       "-fx-font-weight: 700;");

    Label newLabel = new Label("new");
    newLabel.setVisible(false);
    newLabel.setAlignment(Pos.CENTER_RIGHT);
    newLabel.setPadding(new Insets(3, 3, 3, 3));
    newLabel.setStyle("-fx-background-color: limegreen;" + 
                      "-fx-font-weight: bold;" + 
                      "-fx-text-fill: white;");

    chat.setNotificationLabel(newLabel);

    hBox.setOnMouseClicked(event -> {
      if (event.getButton() == MouseButton.PRIMARY) {
        if (event.getClickCount() == 2 || event.getClickCount() == 1) {
          Controller.client.selectChat(groupChat);
        }
      }
    });

    hBox.setOnMouseEntered(event -> {
      hBox.setStyle("-fx-padding: 5 5 5 5;" + 
                    "-fx-background-color: lightgray;");
    });

    hBox.setOnMouseExited(event -> {
      hBox.setStyle("-fx-padding: 5 5 5 5;" + 
                    "-fx-background-color: transparent;");
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
