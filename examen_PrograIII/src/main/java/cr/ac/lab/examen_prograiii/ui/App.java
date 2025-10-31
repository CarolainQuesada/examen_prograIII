package cr.ac.lab.examen_prograiii.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/cr/ac/lab/examen_prograiii/main.fxml"));
        Scene scene = new Scene(fxml.load());
        stage.setTitle("Cliente GitHub - Examen Progra III");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}