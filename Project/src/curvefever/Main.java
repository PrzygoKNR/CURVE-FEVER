package curvefever;

import curvefever.audio.MusicPlayer;
import curvefever.configdialog.ConfigDialogController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.util.*;

public class Main extends Application {

    private static Set<KeyCode> pressedKeys = new HashSet<KeyCode>();
    public static MusicPlayer musicPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        musicPlayer = new MusicPlayer();
        musicPlayer.shuffleMusicList();
        musicPlayer.playIntro();

        Group root = new Group();
        primaryStage.setTitle("Curve Fever");
        primaryStage.getIcons().add(new Image("file:images/applicationIcon.png"));
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.show();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        primaryStage.setScene(scene);

        // pobieranie aktualnych wymiarów formularza
        Rectangle2D screenSizeFX = Screen.getPrimary().getVisualBounds();

        int widthOfForm = (int) screenSizeFX.getWidth() - CurveFeverConsts.SIZE_OF_SCORE_LIST;
        int heightOfForm = (int) screenSizeFX.getHeight();

        // tworzenie kanwy rysunku o wymiarach formularza

        Canvas canvas = new Canvas(widthOfForm, heightOfForm);
        Canvas canvasForScores = new Canvas(CurveFeverConsts.SIZE_OF_SCORE_LIST, heightOfForm);
        canvasForScores.setLayoutX(widthOfForm);


        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext gcForScores = canvasForScores.getGraphicsContext2D();

        Parent configRoot = FXMLLoader.load(getClass().getResource("configdialog/ConfigDialog.fxml"));
        Stage configStage = new Stage(StageStyle.UTILITY);
        configStage.initOwner(primaryStage);
        configStage.setTitle("Curve Fever");
        configStage.setScene(new Scene(configRoot));
        configStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        configStage.setResizable(false);
        configStage.showAndWait();

        if (ConfigDialogController.getPlayersControls() == null) {
            Platform.exit();
            System.exit(0);
        }

        GameFacade gameFacade = new GameFacade(widthOfForm, heightOfForm, pressedKeys, gc,
                ConfigDialogController.getPlayersControls(), ConfigDialogController.getMaxPlayerNumber(),
                ConfigDialogController.getPlayersColors(), gcForScores);

        gameFacade.refreshScoreList(gcForScores, heightOfForm);


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
        root.getChildren().add(canvasForScores);
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
