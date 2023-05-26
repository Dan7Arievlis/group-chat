import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chat {
  private String chatName;
  private VBox chatVBox;
  private HBox groupHBox;
  private Label notification;

  public Chat(String chatName, VBox chatPane) {
    this.chatName = chatName;
    this.chatVBox = chatPane;
  }

  public void toggleGroupChat() {
    // chatPane.setDisable(!chatPane.isDisabled());
    chatVBox.setVisible(!chatVBox.isVisible());
  }

  public VBox getVBox() {
    return chatVBox;
  }

  public void setGroupHBox(HBox groupHBox) {
    this.groupHBox = groupHBox;
  }

  public String getName() {
    return this.chatName;
  }

  public Label getNotificationLabel() {
    return notification;
  }

  public void setNotificationLabel(Label notification) {
    this.notification = notification;
  }

  public void setNotificationLabelVisible(boolean value) {
    this.notification.setVisible(value);
  }

  public void notification(Controller controller) {
    controller.sortChatLogs(groupHBox);

    if (!this.equals(Controller.client.getCurrentChat()))
      this.setNotificationLabelVisible(true);
  }

  @Override
  public int hashCode() {
    return this.chatName.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return this.chatName.equals(obj);
  }

  @Override
  public String toString() {
    return this.chatName;
  }
}
