package curveFever;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class ConfigDialogController {

    private static KeyCode[][] playersControls;
    private static Color[] playersColors;
    private static int maxPlayerCount;
    private Set<Color> colorConstraints;

    @FXML
    private GridPane gridPaneOne;

    @FXML
    public void initialize() {
        maxPlayerCount = 6;
        playersColors = new Color[maxPlayerCount];
        playersControls = new KeyCode[maxPlayerCount][2];
        setColorConstraints();

        for(int i=0; i<maxPlayerCount; i++) {
            int elementId = i+1;
            HBox newHBox = new HBox(10);
            newHBox.setId("player" + elementId + "HBox");

            Button button = setupButton(elementId);
            TextField leftTextField = setupTextField(elementId, "left");
            TextField rightTextField = setupTextField(elementId, "right");;
            ColorPicker colorPicker = setupColorPicker(elementId);

            newHBox.getChildren().add(button);
            newHBox.getChildren().add(leftTextField);
            newHBox.getChildren().add(rightTextField);

            newHBox.getChildren().add(colorPicker);
            gridPaneOne.add(newHBox, 0, elementId);
        }

        resetGridPaneChildren();
    }

    private void setColorConstraints() {
        colorConstraints = new HashSet<>();
        colorConstraints.add(Color.WHITE);

    }

    private ColorPicker setupColorPicker(int colorPickerId) {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setId("player" + colorPickerId + "colorPicker");

        colorPicker.setOnHidden(this::onColorPickerHidden);
        return colorPicker;
    }

    private TextField setupTextField(int textFieldId, String direction) {
        TextField leftTextField = new TextField();
        leftTextField.setId(direction + "Player" + textFieldId + "Control");
        leftTextField.setAlignment(Pos.CENTER);
        leftTextField.setDisable(true);

        leftTextField.setOnKeyReleased(this::onTextFieldKeyReleased);
        leftTextField.setOnMouseClicked(this::onTextFieldMouseClicked);

        leftTextField.setPrefWidth(100);
        return leftTextField;
    }

    private Button setupButton(int buttonId) {
        Button button = new Button("Player " + buttonId);
        button.setId("player" + buttonId + "Button");
        button.setPrefWidth(80);

        button.setOnMouseClicked(this::onButtonClicked);
        return button;
    }

    private void resetGridPaneChildren() {
        System.out.println("Clearing Fields");

        gridPaneOne.getChildren().stream()
                .flatMap(hBox -> ((HBox)hBox).getChildren().stream())
                .filter(hBoxChild
                        -> hBoxChild.getClass().equals(TextField.class)
                        || hBoxChild.getClass().equals(ColorPicker.class))
                .forEach(hBoxChild -> {
                    int childId = getPlayerId(hBoxChild);
                    boolean isPlayerConfigNull = (playersControls[childId][0] == null
                            || playersControls[childId][1] == null
                            || playersColors[childId] == null);

                    if(hBoxChild.getClass().equals(TextField.class)) {
                        TextField textField = (TextField)hBoxChild;
                        if (isPlayerConfigNull) {
                            textField.setText("");
                            nullPlayerConfig(childId);
                        }
                    } else if(hBoxChild.getClass().equals(ColorPicker.class)) {
                        ColorPicker colorPicker = (ColorPicker)hBoxChild;
                        if(isPlayerConfigNull) {
                            colorPicker.setValue(Color.WHITE);
                            nullPlayerConfig(childId);
                        }
                    }
                    hBoxChild.setDisable(true);
                });
    }

    private boolean playerControlsContains(KeyCode keyCode) {
        for(int i=0; i<playersControls.length; i++) {
            for (int j=0; j<playersControls[i].length; j++) {
                if(playersControls[i][j] == keyCode) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean playerColorsCheck(Color color) {
        if(colorConstraints.contains(color))
            return true;

        for(int i=0; i<playersColors.length; i++) {
            if(playersColors[i] == null) continue;
            if(playersColors[i].equals(color))
                return true;
        }
        return false;
    }

    private int getPlayerId(Node node) {
        return Integer.parseInt(Arrays.stream(node.getId().split("\\D"))
                .filter(n -> !n.equals(""))
                .findFirst()
                .get()) - 1;
    }

    private void nullPlayerConfig(int playerId) {
        playersControls[playerId][0] = null;
        playersControls[playerId][1] = null;
        playersColors[playerId] = null;
    }

    @FXML
    private void onColorPickerHidden(Event event) {
        ColorPicker colorPicker = (ColorPicker)event.getSource();
        Color color = colorPicker.getValue();
        if(playerColorsCheck(color)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Color unavailable");
            alert.setHeaderText("Wrong Color");
            alert.showAndWait();
            colorPicker.setValue(Color.WHITE);
            return;
        }
        int colorPickerId = getPlayerId(colorPicker);
        playersColors[colorPickerId] = color;

        colorPicker.setDisable(true);
    }

    @FXML
    private void onTextFieldKeyReleased(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        TextField textField = (TextField)keyEvent.getSource();
        if(playerControlsContains(keyEvent.getCode())) {
            textField.setText("Key in use");
            return;
        }

        int playerId = getPlayerId(textField);
        if(textField.getId().contains("left")) {
            playersControls[playerId][0] = keyEvent.getCode();
        } else {
            playersControls[playerId][1] = keyEvent.getCode();
        }

        textField.setText(keyEvent.getCode().toString());
        textField.setDisable(true);
    }

    @FXML
    private void onButtonClicked(MouseEvent e) {
        resetGridPaneChildren();
        Button clickedButton = (Button)e.getSource();
        int clickedButtonId = getPlayerId(clickedButton);
        nullPlayerConfig(clickedButtonId);

        ((HBox)clickedButton.getParent()).getChildren()
                .parallelStream()
                .forEach(node -> {
                    if(node.getClass().equals(TextField.class)) {
                        ((TextField)node).setText("Type a Key");
                    } else if(node.getClass().equals(ColorPicker.class)) {
                        ((ColorPicker)node).setValue(Color.WHITE);
                    }
                    System.out.println(node);
                    node.setDisable(false);
                });
    }

    @FXML
    private void onTextFieldMouseClicked(MouseEvent event) {
        ((TextField)event.getSource()).setText("");
    }

    @FXML
    public void onBorderPaneKeyReleased(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            resetGridPaneChildren();
        }
    }

    private boolean isConfigIncomplete() {
        int playerCount = 0;
        boolean flag1;
        boolean flag2;
        for(int i=0; i<playersControls.length; i++) {
            flag1 = false;
            flag2 = false;

            if(playersControls[i][0] == null) flag1 = true;
            if(playersControls[i][1] == null) flag2 = true;
            if(flag1 != flag2) return true;
            if(!flag1) playerCount++;
        }
        if(playerCount < 2) return true;
        else return false;
    }

    @FXML
    public void onOkayClicked() {
        if(isConfigIncomplete()) {
            System.out.println("Config not complete");
            return;
        }

        System.out.println("--------------------------------------------------------------------");
        for(int i=0; i<playersControls.length; i++) {
            System.out.println("i: " + i);
            for (int j=0; j<playersControls[i].length; j++) {
                System.out.print(playersControls[i][j] + " ");
            }
            System.out.println();
        }


//        GameFacade gameFacade = new GameFacade(Main.widthOfForm, Main.heightOfForm, Main.pressedKeys, Main.gc, playersControls, maxPlayerCount);
        Stage stage = (Stage)gridPaneOne.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClearClicked(){
        playersControls = new KeyCode[maxPlayerCount][2];
        resetGridPaneChildren();
    }

    @FXML
    public void onExitClicked() {
        playersControls = null;
        Stage stage = (Stage)gridPaneOne.getScene().getWindow();
        stage.close();
    }

    public static KeyCode[][] getPlayersControls() {
        return playersControls;
    }

    public static int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public static Color[] getPlayersColors() {
        return playersColors;
    }
}
