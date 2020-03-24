package Main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class mainGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MacroFlow");
        //add scenes
        var map = Controller.map;
        map.addScreen(Controller.Scenes.editor, "../gui/macroEditor.fxml");
        map.addScreen(Controller.Scenes.main, "../gui/macroMain.fxml");
        map.init(Controller.Scenes.main);
        primaryStage.setScene(map.main);
        primaryStage.setOnCloseRequest(e -> handleExit());
        primaryStage.show();
    }

    private void handleExit() {
        Main.main.close();
        Platform.exit();
        System.exit(0);
    }
}
