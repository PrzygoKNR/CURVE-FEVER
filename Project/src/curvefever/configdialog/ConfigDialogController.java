package curvefever.configdialog;

import curvefever.Main;
import curvefever.languages.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
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

        try {
            borderPane.getScene().getWindow().sizeToScene();
        } catch (NullPointerException e) {
            System.out.println(e);
            System.out.println("BorderPane not loaded yet");
        }
    }

    @FXML
    private void onLanguageComboBoxKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onLanguageComboBoxHidden();
        }
    }

    private void setLanguage() {
        InterfaceLanguages interfaceLanguages = languageComboBox.getValue();
        switch (interfaceLanguages) {
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

            default:
                displayAlert("Something went wrong", "Language unavailable");
                break;
        }
    }

    @FXML
    public void onMaxPlayerNumberButtonMouseClicked() {
        String errorMessage = interfaceLanguage.wrongNumber();
        try {
            maxPlayerNumber = Integer.parseInt(maxPlayerNumberTextField.getText().trim());
            if (maxPlayerNumber < 1) {
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

        Stage stage = (Stage) borderPane.getScene().getWindow();
        System.out.println(maxPlayerNumber);
        setupWindowComponents();
        stage.sizeToScene();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (stage.getHeight() > (screenSize.height / 2)) {
            stage.setHeight(screenSize.height / 2);
        }
    }

    @FXML
    public void onMaxPlayerNumberKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            onMaxPlayerNumberButtonMouseClicked();
    }

    @FXML
    public void onMaxPlayerNumberTextFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            onMaxPlayerNumberButtonMouseClicked();
    }

    private void setupWindowComponents() {
        gridPaneOne.getChildren().clear();
        playersColors = new Color[maxPlayerNumber];
        playersControls = new KeyCode[maxPlayerNumber][2];

        for (int i = 0; i < maxPlayerNumber; i++) {
            int elementId = i + 1;
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
    }

    private void setColorConstraints() {
        colorConstraints = new HashSet<>();
        colorConstraints.add(Color.WHITE);
        //wiecej kolorow nie jest dozwolonych dla graczy
    }

    private boolean playerColorsContains(Color color) {
        for (Color tempColor : playersColors) {
            if (tempColor == null) continue;
            if (tempColor.equals(color))
                return true;
        }
        return false;
    }

    private Color generateUniqueColor() {
        Random random = new Random();
        Color color;

        do {
            color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
        } while (playerColorsContains(color) || colorConstraints.contains(color));

        return color;
    }

    @FXML
    private void onColorPickerKeyPressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        if (keyCode.equals(KeyCode.UP) || keyCode.equals(KeyCode.DOWN)
                || keyCode.equals(KeyCode.LEFT) || keyCode.equals(KeyCode.RIGHT)) {
            ColorPicker colorPicker = ((ColorPicker) keyEvent.getSource());
            ((ColorPicker) keyEvent.getSource()).setValue(generateUniqueColor());
            colorPicker.requestFocus();
            keyEvent.consume();
        } else if (keyCode.equals(KeyCode.ENTER)) {
            onColorPickerHidden(keyEvent);
        } else if (keyCode.equals(KeyCode.SPACE)) {
            keyEvent.consume();
        }
    }

    private ColorPicker setupColorPicker(int colorPickerId) {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setId("player" + colorPickerId + "colorPicker");
        colorPicker.setValue(generateUniqueColor());

        colorPicker.setOnKeyPressed(this::onColorPickerKeyPressed);
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

        button.setOnAction(this::onButtonClicked);
        button.setOnKeyPressed(this::onButtonKeyPressed);
        return button;
    }

    private void resetGridPaneChildren() {
        System.out.println("Clearing Fields");

        gridPaneOne.getChildren().stream()
                .flatMap(hBox -> ((HBox) hBox).getChildren().stream())
                .forEach(hBoxChild -> {
                    if (hBoxChild.getClass().equals(Button.class)) {
                        hBoxChild.setDisable(false);
                        return;
                    }
                    int childId = getPlayerId(hBoxChild);
                    boolean isPlayerConfigNull = (playersControls[childId][0] == null
                            || playersControls[childId][1] == null
                            || playersColors[childId] == null);

                    if (hBoxChild.getClass().equals(TextField.class)) {
                        TextField textField = (TextField) hBoxChild;
                        if (isPlayerConfigNull) {
                            textField.setText("");
                            nullPlayerConfig(childId);
                        }
                    } else if (hBoxChild.getClass().equals(ColorPicker.class)) {
                        if (isPlayerConfigNull) {
                            nullPlayerConfig(childId);
                        }
                    }
                    hBoxChild.setDisable(true);
                });
    }

    private boolean playerControlsContains(KeyCode keyCode) {
        for (int i = 0; i < playersControls.length; i++) {
            for (int j = 0; j < playersControls[i].length; j++) {
                if (playersControls[i][j] == keyCode) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean playerColorsCheck(Color color) {
        if (colorConstraints.contains(color))
            return true;

        for (int i = 0; i < playersColors.length; i++) {
            if (playersColors[i] == null) continue;
            if (playersColors[i].equals(color))
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
        ColorPicker colorPicker = (ColorPicker) event.getSource();
        Color color = colorPicker.getValue();
        if (playerColorsCheck(color)) {
            displayAlert(interfaceLanguage.colorUnavailable(), interfaceLanguage.wrongColor());
            colorPicker.setValue(Color.WHITE);
            return;
        }
        int colorPickerId = getPlayerId(colorPicker);
        playersColors[colorPickerId] = color;

        colorPicker.setDisable(true);
        powaznaNazwaPowaznejFunkcji((HBox) ((ColorPicker) event.getSource()).getParent());
    }

    @FXML
    private void onTextFieldKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        System.out.println(keyEvent.getCode());
        TextField textField = (TextField) keyEvent.getSource();
        if (playerControlsContains(keyEvent.getCode())) {
            textField.setText(interfaceLanguage.keyInUse());
            return;
        }

        int playerId = getPlayerId(textField);
        if (textField.getId().contains("left")) {
            playersControls[playerId][0] = keyEvent.getCode();
        } else {
            playersControls[playerId][1] = keyEvent.getCode();
        }

        textField.setText(keyEvent.getCode().toString());
        textField.setDisable(true);
        powaznaNazwaPowaznejFunkcji((HBox) ((TextField) keyEvent.getSource()).getParent());
    }

    @FXML
    private void onButtonClicked(ActionEvent e) {
        resetGridPaneChildren();
        Button clickedButton = (Button) e.getSource();
        int clickedButtonId = getPlayerId(clickedButton);
        nullPlayerConfig(clickedButtonId);
        HBox hBox = ((HBox) clickedButton.getParent());
        hBox.getChildren()
                .parallelStream()
                .forEach(node -> {
                    if (node.getClass().equals(TextField.class)) {
                        ((TextField) node).setText(interfaceLanguage.typeAKey());
                    }
                    System.out.println(node);
                    node.setDisable(false);
                });
        clickedButton.setDisable(true);
    }

    @FXML
    private void onButtonKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onButtonClicked(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
        }

    }

    private void powaznaNazwaPowaznejFunkcji(HBox hBox) {
        if (hBox.getChildren().parallelStream().allMatch(Node::isDisabled)) {
            hBox.getChildren()
                    .stream()
                    .filter(node ->
                            node.getClass()
                                    .equals(Button.class))
                    .forEach(node -> node.setDisable(false));
        }
    }

    @FXML
    private void onTextFieldMouseClicked(MouseEvent event) {
        ((TextField) event.getSource()).setText("");
    }

    @FXML
    public void onBorderPaneKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            resetGridPaneChildren();
        }
    }

    private boolean isConfigIncomplete() {
        int playerAmount = 0;
        boolean flag1;
        boolean flag2;
        for (int i = 0; i < playersControls.length; i++) {
            flag1 = false;
            flag2 = false;

            if (playersControls[i][0] == null) flag1 = true;
            if (playersControls[i][1] == null) flag2 = true;
            if (flag1 != flag2) return true;
            if (!flag1) playerAmount++;
        }
        if (playerAmount < 2) return true;
        else return false;
    }

    @FXML
    public void onOkayKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            onOkayClicked();
    }

    @FXML
    public void onOkayClicked() {
        if (isConfigIncomplete()) {
            System.out.println("Config not complete");
            return;
        }

        System.out.println("--------------------------------------------------------------------");
        for (int i = 0; i < playersControls.length; i++) {
            System.out.println("i: " + i);
            for (int j = 0; j < playersControls[i].length; j++) {
                System.out.print(playersControls[i][j] + " ");
            }
            System.out.println();
        }
<<<<<<< HEAD:Project/src/curvefever/configdialog/ConfigDialogController.java
=======

>>>>>>> 0cffb1a13905b3a84ae56bdbb5b234555f8b5d42:Project/src/curveFever/configDialog/ConfigDialogController.java
        Main.musicPlayer.play();
        Stage stage = (Stage) gridPaneOne.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClearClicked() {
        playersControls = new KeyCode[maxPlayerNumber][2];
        resetGridPaneChildren();
    }

    @FXML
    public void onExitClicked() {
        playersControls = null;
        Stage stage = (Stage) gridPaneOne.getScene().getWindow();
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
