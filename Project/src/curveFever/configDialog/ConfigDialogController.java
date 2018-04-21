package curveFever.configDialog;

import curveFever.languages.*;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;


public class ConfigDialogController {

    private static KeyCode[][] playersControls;
    private static Color[] playersColors;
    private static int maxPlayerNumber;
    private Set<Color> colorConstraints;
    private ConfigGUILanguage interfaceLanguage;
    private int dialogWidth;
    private int buttonWidth;
    private int textFieldWidth;

    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPaneOne;
    @FXML
    private Label maxNumberLabel;
    @FXML
    private Button maxNumberOkButton;
    @FXML
    private TextField maxPlayerNumberTextField;
    @FXML
    private ComboBox<InterfaceLanguages> languageComboBox;
    @FXML
    private ObservableList<InterfaceLanguages> languageComboList;
    @FXML
    private Button okayButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        languageComboList.addAll(InterfaceLanguages.values());
        languageComboBox.setValue(InterfaceLanguages.ENGLISH);
        onLanguageComboBoxHidden();
        setColorConstraints();
    }

    @FXML
    public void onLanguageComboBoxHidden() {
        setLanguage();
        setupWindowComponents();
    }

    private void setLanguage() {
        InterfaceLanguages interfaceLanguages = languageComboBox.getValue();
        switch(interfaceLanguages) {
            case ARABIC:
                interfaceLanguage = new ConfigGUILanguageARB();
                dialogWidth = 500;
                buttonWidth = 90;
                textFieldWidth = 100;
                break;

            case ENGLISH:
                interfaceLanguage = new ConfigGUILanguageENG();
                dialogWidth = 500;
                buttonWidth = 90;
                textFieldWidth = 100;
                break;

            case IGBO:
                interfaceLanguage = new ConfigGUILanguage("language-Igbo.txt", InterfaceLanguages.IGBO);
                dialogWidth = 550;
                buttonWidth = 150;
                textFieldWidth = 100;
                break;

            case POLISH:
                interfaceLanguage = new ConfigGUILanguage("language-Polish.txt", InterfaceLanguages.POLISH);
                dialogWidth = 540;
                buttonWidth = 90;
                textFieldWidth = 120;
                break;
        }
    }

    @FXML
    public void onMaxPlayerNumberButtonMouseClicked() {
        String errorMessage = interfaceLanguage.wrongNumber();
        try {
            maxPlayerNumber = Integer.parseInt(maxPlayerNumberTextField.getText().trim());
            if(maxPlayerNumber < 1) {
                errorMessage = interfaceLanguage.numberOutOfBound();
                throw new IndexOutOfBoundsException("Number outside of bounds");
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayAlert(interfaceLanguage.wrongNumber(), errorMessage);
            maxPlayerNumberTextField.setText("1");
            return;
        }
        okayButton.setDisable(false);
        clearButton.setDisable(false);

        Stage stage = (Stage)borderPane.getScene().getWindow();
        stage.setHeight(150 + maxPlayerNumber *35);
        stage.setWidth(dialogWidth);

        System.out.println(maxPlayerNumber);
        setupWindowComponents();
    }

    @FXML
    public void onMaxPlayerNumberTextFieldKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER))
            onMaxPlayerNumberButtonMouseClicked();
    }

    private void setupWindowComponents() {
        gridPaneOne.getChildren().clear();
        playersColors = new Color[maxPlayerNumber];
        playersControls = new KeyCode[maxPlayerNumber][2];

        for(int i = 0; i< maxPlayerNumber; i++) {
            int elementId = i+1;
            HBox newHBox = new HBox(10);
            newHBox.setId("player" + elementId + "HBox");

            Button button = setupButton(elementId);
            TextField leftTextField = setupTextField(elementId, "left");
            TextField rightTextField = setupTextField(elementId, "right");
            ColorPicker colorPicker = setupColorPicker(elementId);

            newHBox.getChildren().add(button);
            newHBox.getChildren().add(leftTextField);
            newHBox.getChildren().add(rightTextField);
            newHBox.getChildren().add(colorPicker);
            gridPaneOne.add(newHBox, 0, elementId);
        }

        setConstantElementsText();
    }

    private void setConstantElementsText() {
        okayButton.setText(interfaceLanguage.ok());
        clearButton.setText(interfaceLanguage.clear());
        exitButton.setText(interfaceLanguage.exit());
        maxNumberLabel.setText(interfaceLanguage.setMaxNumberOfPlayer());
        maxNumberOkButton.setText(interfaceLanguage.ok());

        try {
            Stage stage = (Stage)borderPane.getScene().getWindow();
            stage.setTitle(interfaceLanguage.dialogTitle());
            stage.setWidth(dialogWidth);
        } catch (Exception e) {
            System.out.println("Couldn't get the stage");
        }
    }

    private void setColorConstraints() {
        colorConstraints = new HashSet<>();
        colorConstraints.add(Color.WHITE);
        //more colors not allowed for players
    }

    private ColorPicker setupColorPicker(int colorPickerId) {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setId("player" + colorPickerId + "colorPicker");

        colorPicker.setOnHidden(this::onColorPickerHidden);
        colorPicker.setDisable(true);
        return colorPicker;
    }

    private TextField setupTextField(int textFieldId, String direction) {
        TextField textField = new TextField();
        textField.setId(direction + "Player" + textFieldId + "Control");
        textField.setAlignment(Pos.CENTER);
        textField.setDisable(true);

        textField.setOnKeyReleased(this::onTextFieldKeyReleased);
        textField.setOnMouseClicked(this::onTextFieldMouseClicked);

        textField.setPrefWidth(textFieldWidth);
        return textField;
    }

    private Button setupButton(int buttonId) {
        Button button = new Button(interfaceLanguage.player() + " " + buttonId);
        button.setId("player" + buttonId + "Button");
        button.setPrefWidth(buttonWidth);

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
            displayAlert(interfaceLanguage.colorUnavailable(), interfaceLanguage.wrongColor());
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
            textField.setText(interfaceLanguage.keyInUse());
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
                        ((TextField)node).setText(interfaceLanguage.typeAKey());
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
        int playerAmount = 0;
        boolean flag1;
        boolean flag2;
        for(int i=0; i<playersControls.length; i++) {
            flag1 = false;
            flag2 = false;

            if(playersControls[i][0] == null) flag1 = true;
            if(playersControls[i][1] == null) flag2 = true;
            if(flag1 != flag2) return true;
            if(!flag1) playerAmount++;
        }
        if(playerAmount < 2) return true;
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

        Stage stage = (Stage)gridPaneOne.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClearClicked(){
        playersControls = new KeyCode[maxPlayerNumber][2];
        resetGridPaneChildren();
    }

    @FXML
    public void onExitClicked() {
        playersControls = null;
        Stage stage = (Stage)gridPaneOne.getScene().getWindow();
        stage.close();
    }

    private void displayAlert(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public static KeyCode[][] getPlayersControls() {
        return playersControls;
    }

    public static int getMaxPlayerNumber() {
        return maxPlayerNumber;
    }

    public static Color[] getPlayersColors() {
        return playersColors;
    }
}
