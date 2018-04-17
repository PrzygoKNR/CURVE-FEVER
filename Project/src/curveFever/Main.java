package curveFever;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

import java.util.*;

public class Main extends Application {

    static Set<KeyCode> pressedKeys = new HashSet<KeyCode>();

    static int widthOfForm;
    static int heightOfForm;
    static GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        primaryStage.setTitle("Curve Fever");
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.show();

        Scene scene = new Scene(root, 2000, 1000);
        primaryStage.setScene(scene);
//        primaryStage.show();

        // pobieranie aktualnych wymiarów formularza

        widthOfForm = (int)primaryStage.getWidth();
        heightOfForm = (int)primaryStage.getHeight();

        // tworzenie kanwy rysunku o wymiarach formularza

        Canvas canvas = new Canvas(widthOfForm, heightOfForm);
        gc = canvas.getGraphicsContext2D();


        Parent configRoot = FXMLLoader.load(getClass().getResource("ConfigDialog.fxml"));
        Stage configStage = new Stage();
        configStage.setTitle("Hello World");
        configStage.setScene(new Scene(configRoot , 500, 500));
        configStage.showAndWait();


        GameFacade gameFacade = new GameFacade(widthOfForm, heightOfForm, pressedKeys, gc,
                ConfigDialogController.getPlayersControls(), ConfigDialogController.getMaxPlayerCount());

        // Obsługa zdarzeń
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                pressedKeys.add(event.getCode());
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                pressedKeys.remove(event.getCode());
            }
        });

        //dodawanie canvas oraz pokazywanie formularza
        root.getChildren().add(canvas);
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void testKeyboardWork() {
        for (KeyCode code : pressedKeys) {
            System.out.print(code);
            System.out.print(" ");
        }
        System.out.print("\n");
    }

}
