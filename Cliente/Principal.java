import java.io.IOException;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Principal extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader;
    try {
      loader = new FXMLLoader(getClass().getResource("Main.fxml"));
      loader.setController(new Controller());

      Parent root = loader.load();
      Scene scene = new Scene(root);

      scene.getStylesheets().add("styles.css");
      stage.setResizable(false);

      stage.setOnCloseRequest(event -> {
        try {
          System.exit(0);
        } catch(Exception e) {}
      });

      stage.setScene(scene);
      stage.show();
    } catch (RuntimeException e) {
      System.err.println(e.getMessage());
    }
  }

  public static void main(String[] args) throws UnknownHostException, IOException {
    launch(args);
  }
}
