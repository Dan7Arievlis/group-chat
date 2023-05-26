import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller implements Initializable {
  @FXML
  private Button button_send;
  @FXML
  private TextArea ta_message;
  @FXML
  private VBox vbox_message;
  @FXML
  private ScrollPane sp_message;
  @FXML
  private Label lbl_userName;

  //
  @FXML
  private Button button_search;
  @FXML
  private Label lbl_chatTitle;
  @FXML
  private VBox vbox_group;
  @FXML
  private TextField tf_group;
  @FXML
  private ScrollPane sp_group;

  public static Client client;
  private Map<String, Layout> layouts;

  public Controller() {
    this.layouts = new HashMap<>();
    layouts.put("CHAT", new ChatLayout());
    layouts.put("GROUP", new GroupLayout());
    layouts.put("WARNING", new WarningLayout());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      loginSettings();

      client = new Client(new Socket(LoginScreen.serverIp, Integer.parseInt(LoginScreen.serverPort)),
          LoginScreen.userName, this);
    } catch (IOException e) {
      e.printStackTrace();
    }

    lbl_userName.setText(LoginScreen.userName);
    lbl_chatTitle.setAlignment(Pos.CENTER);

    sp_message.fitToWidthProperty().set(true);
    sp_group.fitToWidthProperty().set(true);

    vbox_message.heightProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        sp_message.setVvalue((Double) newValue);
      }
    });

    VBox vBox = this.vbox_message;
    client.recieveMessageFromServer(vBox, vbox_group);
    //
    vbox_group.heightProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        sp_group.setVvalue(0);
      }
    });

    ta_message.setWrapText(true);

    // acao do botao enviar
    button_send.setOnAction(event -> {
      String messageToSend = ta_message.getText();
      if (!messageToSend.isEmpty() && !client.getCurrentChat().isEmpty()) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageToSend);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(0, 5, 0, 5));
        // TODO: set style ou set id

        // add data e alinhar na hbox
        LocalTime now = LocalTime.now();
        Label dateLabel = new Label(String.format("%d:%d", now.getHour(), now.getMinute()));
        dateLabel.setAlignment(Pos.CENTER_RIGHT);
        dateLabel.setPadding(new Insets(0, 5, 0, 5));

        // hBox.getChildren().add(nameLabel);
        hBox.getChildren().add(textFlow);
        hBox.getChildren().add(dateLabel);

        VBox vBoxChat = client.getChatVBox(client.getCurrentChat());
        if (vBoxChat == null)
          vBoxChat = vbox_message;
        vBoxChat.getChildren().add(hBox);
        client.getChat(client.getCurrentChat()).notification(this);

        client.sendMessageToChat(messageToSend, now.toString());
        ta_message.clear();
      }
    });

    // acao do botao buscar
    button_search.setOnAction(event -> {
      String groupName = tf_group.getText();
      if (!groupName.isEmpty()) {
        client.searchChat(groupName);
        tf_group.clear();
      }
    });
  }

  public ScrollPane getScrollPane() {
    return this.sp_message;
  }

  public void setScrollPaneContent(String chat) {
    if (chat == null) {
      sp_message.setContent(vbox_message);
      setChatTitleLabel("Servidor conectado");
      return;
    }
    sp_message.setContent(Controller.client.getChat(chat).getVBox());
    setChatTitleLabel(chat);
  }

  public VBox getVBoxGroup() {
    return vbox_group;
  }

  public void setChatTitleLabel(String title) {
    this.lbl_chatTitle.setText(title);
  }

  public void sortChatLogs(HBox hBoxToTop) {
    if (hBoxToTop == null)
      return;

    vbox_group.getChildren().remove(hBoxToTop);
    vbox_group.getChildren().add(0, hBoxToTop);
  }

  private String loginSettings() throws RuntimeException {
    new LoginScreen().start();
    String userName = LoginScreen.userName;
    if (userName.isEmpty())
      throw new RuntimeException("Nome de usuario invalido");

    return userName;
  }

  public void addLabel(String protocol, Scanner scanner) {
    Platform.runLater(() -> {
      this.layouts.get(protocol).EditLayout(scanner, this);
    });
  }
}
