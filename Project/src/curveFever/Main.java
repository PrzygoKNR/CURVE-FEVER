package curveFever;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.*;

public class Main extends Application {

    private static Set<KeyCode> pressedKeys = new HashSet<KeyCode>();
    @Override
    public void start(Stage primaryStage) throws Exception{

        Group root = new Group();
        primaryStage.setTitle("Curve Fever");

        Scene scene = new Scene(root, 2000, 1000);
        Canvas canvas = new Canvas(2000, 1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        GameFacade gameFacade = new GameFacade(2, pressedKeys, gc);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                pressedKeys.add(event.getCode());
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                pressedKeys.remove(event.getCode());
            }
        });

        root.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void testKeyboardWork(){
        for(KeyCode code: pressedKeys){
            System.out.print(code);
            System.out.print(" ");
        }
        System.out.print("\n");
    }
}
