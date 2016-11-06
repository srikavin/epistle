package infuzion.chat.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application{
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter your name.");
            String name = in.nextLine();
            System.out.println("Nice name: " + name);
            new Thread(new Client("127.0.0.1", 7763, name)).start();
        } else {
            new Thread(new Client(args[0], Integer.valueOf(args[1]), args[2])).start();
        }

        launch(args);
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
        StackPane stackPane = new StackPane();

        primaryStage.setTitle("123");

        Scene scene = new Scene(stackPane, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
