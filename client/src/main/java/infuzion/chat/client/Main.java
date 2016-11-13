package infuzion.chat.client;

import infuzion.chat.client.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    public static final String version = "v2.5";
    public static ConnectionListController connectionListController;
    public static AboutDialogController aboutDialogController;
    public static ConnectionOptionsController connectionOptionsController;
    public static IncorrectSettingsController incorrectSettingsController;
    public static MainChatController mainChatController;
    private static boolean skip = false;
    private static String name;
    private static String ip;
    private static int port;
    private static File serverFile;

    public static void main(String[] args) throws IOException {
        serverFile = new File("servers.dat");
        serverFile.createNewFile();
        launch();
    }

    public static Client startClientThread(String name, String ip, int port) throws IOException {
        Client client = new Client(ip, port, name);
        new Thread(client).start();
        return client;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            primaryStage.initStyle(StageStyle.UNIFIED);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/incorrectSettings.fxml"));
            Scene incorrectSettingsScene = new Scene(loader.load());

            incorrectSettingsController = loader.getController();
            Stage errorStage = new Stage();
            incorrectSettingsScene.setFill(Color.TRANSPARENT);
            errorStage.setScene(incorrectSettingsScene);
            incorrectSettingsController.setStage(errorStage);

            loader = new FXMLLoader(getClass().getResource("/aboutDialog.fxml"));
            Scene aboutScene = new Scene(loader.load());

            aboutDialogController = loader.getController();
            Stage aboutStage = new Stage();
            aboutScene.setFill(Color.TRANSPARENT);
            aboutStage.setScene(aboutScene);
            aboutDialogController.setStage(aboutStage);

            loader = new FXMLLoader(getClass().getResource("/connectionList.fxml"));
            Scene connectionList = new Scene(loader.load());
            connectionListController = loader.getController();
            connectionListController.setServersFile(serverFile);
            connectionListController.init(primaryStage);
            primaryStage.setScene(connectionList);
            primaryStage.show();

            loader = new FXMLLoader(getClass().getResource("/connectionOptions.fxml"));
            Scene connectionOptions = new Scene(loader.load());
            Stage connectionOptionsStage = new Stage(StageStyle.UNIFIED);
            connectionOptionsStage.setScene(connectionOptions);
            connectionOptionsController = loader.getController();
            connectionOptionsController.setStage(connectionOptionsStage);
            connectionOptionsController.init(connectionListController);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
