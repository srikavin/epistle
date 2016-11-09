package infuzion.chat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static boolean skip = false;
    private static String name;
    private static String ip;
    private static int port;
    private static Controller controller;

    public static void main(String[] args) throws IOException {
        if (args.length >= 3) { //[name] [ip] [port]
            name = args[0];
            ip = args[1];
            port = Integer.parseInt(args[2]);
            skip = true;
            launch();
        } else {
            launch();
        }
    }

    public static void setController(Controller controller) {
        Main.controller = controller;
    }

    public void startClientThread(String name, String ip, int port) throws IOException {
        new Thread(new Client(ip, port, name)).start();
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!skip) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/connectionOptions.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Controller.setStageAndSceneAndMain(primaryStage, scene, this);
            primaryStage.setTitle("Connection Settings");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainChat.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Controller.setStageAndSceneAndMain(primaryStage, scene, this);
            controller.connect(name, ip, port);
            primaryStage.setTitle("Connection Settings");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
