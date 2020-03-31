package Main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class mainGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    Stage stage;
    String pathToFile;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("MacroFlow");
        var small = new Image("icons/icon-16.png");
        pathToFile = small.getUrl();

        //add scenes
        Platform.setImplicitExit(false);
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);

        var map = Controller.map;
        map.addScreen(Controller.Scenes.editor, "macroEditor.fxml");
        map.addScreen(Controller.Scenes.main, "macroMain.fxml");
        map.init(Controller.Scenes.main);
        primaryStage.getIcons().addAll(new Image("icons/icon-128.png"),
                new Image("icons/icon-96.png"),
                new Image("icons/icon-72.png"),
                new Image("icons/icon-64.png"),
                new Image("icons/icon-48.png"),
                new Image("icons/icon-32.png"),
                new Image("icons/icon-24.png"),
                small);
        primaryStage.setScene(map.main);
        // primaryStage.setOnCloseRequest(e -> handleExit());
        primaryStage.show();
    }

    private void addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // set up a system tray icon.
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            URL imageLoc = new URL(
                    pathToFile
            );
            java.awt.Image image = ImageIO.read(imageLoc);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            // if the user selects the default menu item (which includes the app name),
            // show the main app stage.
            java.awt.MenuItem openItem = new java.awt.MenuItem("Macro Flow");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));


            // the convention for tray icons seems to be to set the default icon for opening
            // the application stage in a bold font.
            java.awt.Font defaultFont = java.awt.Font.decode(null);

            java.awt.CheckboxMenuItem startListen = new java.awt.CheckboxMenuItem("Listen");
            startListen.addItemListener(event -> Platform.runLater(() -> {
                boolean res = Main.main.startListening();
                startListen.setState(res);
            }));

            // to really exit the application, the user must go to the system tray icon
            // and select the exit option, this will shutdown JavaFX and remove the
            // tray icon (removing the tray icon will also shut down AWT).
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                Main.main.close();
                Platform.exit();
                tray.remove(trayIcon);
            });

            // setup the popup menu for the application.
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(startListen);
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);
        } catch (java.awt.AWTException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

    private void handleExit() {
        Main.main.close();
        Platform.exit();
        System.exit(0);
    }
}
