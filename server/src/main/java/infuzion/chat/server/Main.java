package infuzion.chat.server;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{
    Button button = new Button("Click me!");

    public static void main(String args[]) throws IOException {
        if(args.length == 0){
            new Thread(new Server(7763)).start();
        } else {
            new Thread(new Server(Integer.valueOf(args[0]))).start();
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StackPane stackPane = new StackPane();

        stage.setTitle("123");

        button.setOnAction((ActionEvent event) -> {
            if(event.getSource().equals(button)){
                System.out.println("clicked");
            }
        });
        stackPane.getChildren().add(button);

        Scene scene = new Scene(stackPane, 300, 300);
        stage.setScene(scene);
        stage.show();
    }
}
