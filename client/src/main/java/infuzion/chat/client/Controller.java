package infuzion.chat.client;

import com.sun.media.sound.InvalidFormatException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static Client client;
    private static Stage primaryStage;
    private static Scene scene;
    private static Main main;
    @FXML
    private TextField username;
    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private Button submit;
    @FXML
    private Button okButton;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private Button chatButton;
    @FXML
    private TextField inputField;
    private Controller controller;


    public Controller() {
        Client.setController(this);
        Main.setController(this);
    }

    public static void setClient(Client cl) {
        client = cl;
    }

    static void setStageAndSceneAndMain(Stage stage, Scene scene, Main main) {
        primaryStage = stage;
        Controller.scene = scene;
        Controller.main = main;

    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void submit() {
        try {
            String userNameValue = username.getText();
            int portValue = Integer.parseInt(port.getText());
            UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS + UrlValidator.ALLOW_ALL_SCHEMES) {
                @Override
                public boolean isValid(String string) {
                    return super.isValid(string) || super.isValid("http://" + string);
                }
            };

            if (!urlValidator.isValid(ip.getText())) {
                throw new InvalidFormatException();
            }

            String ipValue = ip.getText();
            if (!connect(userNameValue, ipValue, portValue)) {
                throw new InvalidFormatException("unable to connect");
            }
        } catch (NumberFormatException | InvalidFormatException e) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/incorrectSettings.fxml"));
            try {
                Parent root = loader.load();
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Incorrect Settings");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public boolean connect(String name, String ip, int port) {
        try {
            main.startClientThread(name, ip, port);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainChat.fxml"));
        try {
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Chat");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @FXML
    public void okButtonClicked() {
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connection Settings");
    }

    @FXML
    private void chatButtonClicked() {
        client.sendMessage(inputField.getText());
        inputField.clear();
    }

    @FXML
    public void inputFieldAction(ActionEvent event) {
        chatButtonClicked();
    }

    public void displayMessage(String string) {
        chatTextArea.appendText(string);
    }

    @FXML
    public void quitMenuAction() {
        System.exit(0);
    }
}
