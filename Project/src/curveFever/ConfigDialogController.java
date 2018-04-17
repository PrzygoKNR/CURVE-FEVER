package curveFever;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Arrays;

public class ConfigDialogController {

    private static KeyCode[][] playersControls;
    private static int maxPlayerCount;

    @FXML
    private GridPane gridPaneOne;

    @FXML
    public void initialize() {
        maxPlayerCount = 6;
        playersControls = new KeyCode[maxPlayerCount][2];

        for(int i=0; i<maxPlayerCount; i++) {
            int elementId = i+1;
            HBox newHbox = new HBox(10);
            newHbox.setId("player" + elementId + "HBox");

            Button newButton = setupButton(elementId);
            TextField leftTextField = setupTextField(elementId, "left");
            TextField rightTextField = setupTextField(elementId, "right");;

            newHbox.getChildren().add(newButton);
            newHbox.getChildren().add(leftTextField);
            newHbox.getChildren().add(rightTextField);
            gridPaneOne.add(newHbox, 0, elementId);
        }

        clearTextFields();
    }

    private TextField setupTextField(int textFieldId, String direction) {
        TextField leftTextField = new TextField();
        leftTextField.setId(direction + "Player" + textFieldId + "Control");
        leftTextField.setAlignment(Pos.CENTER);
        leftTextField.setDisable(true);

        leftTextField.setOnKeyReleased(event -> onTextFieldKeyReleased(event));
        leftTextField.setOnMouseClicked(event -> onTextFieldMouseClicked(event));
        leftTextField.setPrefWidth(100);
        return leftTextField;
    }

    private Button setupButton(int buttonId) {
        Button newButton = new Button("Player " + buttonId);
        newButton.setId("player" + buttonId + "Button");
        newButton.setPrefWidth(80);

        newButton.setOnMouseClicked(this::onButtonClicked);
        return newButton;
    }

    private void clearTextFields() {
        System.out.println("Clearing Fields");

        gridPaneOne.getChildren().forEach(node -> {
            ((HBox)node).getChildren().forEach(node1 -> {
                if(node1.getClass().toString().equals("class javafx.scene.control.TextField")) {
                    TextField textField = ((TextField)node1);
                    int textFieldId = getPlayerId(textField);
                    if(playersControls[textFieldId][0] == null || playersControls[textFieldId][1] == null) {
                        textField.setText("");
                        playersControls[textFieldId][0] = null;
                        playersControls[textFieldId][1] = null;
                    }
                    node1.setDisable(true);
                }
            });
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

    private int getPlayerId(TextField textField) {
        return Integer.parseInt(Arrays.asList(textField.getId().split("\\D")).stream().filter(n -> !n.equals("")).findFirst().get()) - 1;
    }

    @FXML
    public void onTextFieldKeyReleased(KeyEvent keyEvent) {
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
    public void onButtonClicked(MouseEvent e) {
        clearTextFields();
        HBox hBox = (HBox)((Button)e.getSource()).getParent();
        hBox.getChildren().forEach(node -> {
            if(node.getClass().toString().equals("class javafx.scene.control.TextField")) {
                System.out.println(node);
                node.setDisable(false);
                ((TextField)node).setText("Type a Key");
            }
        });
    }

    @FXML
    public void onTextFieldMouseClicked(MouseEvent event) {
        ((TextField)event.getSource()).setText("");
    }

    @FXML
    public void onBorderPaneKeyReleased(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            clearTextFields();
        }
    }

    private boolean isConfigIncomplete() {
        int playerCount = 0;
        boolean flag1 = false;
        boolean flag2 = false;
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
        clearTextFields();
    }

    @FXML
    public void onExitClicked() {
        Stage stage = (Stage)gridPaneOne.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    public static KeyCode[][] getPlayersControls() {
        return playersControls;
    }

    public static int getMaxPlayerCount() {
        return maxPlayerCount;
    }
}
