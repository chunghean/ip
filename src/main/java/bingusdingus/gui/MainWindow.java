package bingusdingus.gui;

import bingusdingus.BingusDingus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private BingusDingus bingusDingus;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.jpg"));
    private Image bingusDingusImage = new Image(this.getClass().getResourceAsStream("/images/BingusDingus.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the BingusDingus instance */
    public void setBingusDingus(BingusDingus b) {
        bingusDingus = b;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing BingusDingus's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = bingusDingus.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBingusDingusDialog(response, bingusDingusImage)
        );
        userInput.clear();
    }
}
