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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sun.net.util.IPAddressUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static Client client;
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
    private Stage primaryStage;
    private Scene scene;
    private Main main;


    public Controller() {
        Client.setController(this);
    }

    public static void setClient(Client cl) {
        client = cl;
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

    void setStageAndSceneAndMain(Stage stage, Scene scene, Main main) {
        this.primaryStage = stage;
        this.scene = scene;
        this.main = main;

    }

    @FXML
    private void submit() {
        try {
            String userNameValue = username.getText();
            int portValue = Integer.parseInt(port.getText());
            if (!IPAddressUtil.isIPv4LiteralAddress(ip.getText())) {
                throw new InvalidFormatException();
            }
            String ipValue = ip.getText();
            main.startClientThread(userNameValue, ipValue, portValue);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainChat.fxml"));
            try {
                Parent root = loader.load();
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Chat");
            } catch (IOException e) {
                e.printStackTrace();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
    public void submitButtonAction(KeyEvent event) {
        submit();
    }

    public void displayMessage(String string) {
        chatTextArea.appendText(string);
    }

    @FXML
    public void quitMenuAction() {
        System.exit(0);
    }
}
