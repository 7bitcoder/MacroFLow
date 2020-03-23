package Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Controller {
    enum Scenes {
        main, editor
    }

    Scenes active = Scenes.editor;
    static Stage primaryStage;
    static Controller map = new Controller();
    HashMap<Scenes, Parent> screenMap = new HashMap<Scenes, Parent>();
    public Scene main;

    private Controller() {
    }


    public void init(Scenes main) {
        this.main = new Scene(screenMap.get(main));
    }

    protected void addScreen(Scenes name, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        loader.setController(getController(name));
        Parent root = loader.load();
        screenMap.put(name, root);
    }

    Object getController(Scenes scene) {
        switch (scene) {
            case editor:
                return Editor.editor;
            case main:
                return Main.main;
        }
        return null;
    }

    protected void removeScreen(Scenes name) {
        screenMap.remove(name);
    }

    protected void activate(Scenes name) {
        main.setRoot(screenMap.get(name));
        active = name;
    }

}

