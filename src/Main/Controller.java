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
    static Controller map;
    HashMap<Scenes, Couple> screenMap = new HashMap<Scenes, Couple>();
    public Scene main;

    static class Couple {
        Parent parent;
        Object controller;

        Couple(Parent par, Object cotr) {
            parent = par;
            controller = cotr;
        }
    }

    public void init(Scenes main) {
        this.main = new Scene(screenMap.get(main).parent);
    }

    protected void addScreen(Scenes name, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Object controller = addController(name);
        loader.setController(controller);
        Parent root = loader.load();
        screenMap.put(name, new Couple(root, controller));
    }

    Object addController(Scenes scene) {
        switch (scene) {
            case editor:
                return new Editor();
            case main:
                return new Main();
        }
        return null;
    }

    protected void removeScreen(Scenes name) {
        screenMap.remove(name);
    }

    protected void activate(Scenes name) {
        main.setRoot(screenMap.get(name).parent);
        active = name;
    }

}

