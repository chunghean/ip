package bingusdingus.gui;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/** Displays a message and its associated profile picture. */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    /** Creates a dialog box containing the supplied message and profile picture. */
    public DialogBox(String s, Image i) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(s);
        displayPicture.setImage(i);
        displayPicture.setClip(new Circle(45, 45, 45));
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    /** Returns a dialog box displaying a user's message.
     *
     * @param message the user's message.
     * @param profilePicture the profile picture to display.
     * @return the configured user dialog box.
     */
    public static DialogBox getUserDialog(String message, Image profilePicture) {
        return new DialogBox(message, profilePicture);
    }

    /** Returns a dialog box displaying Bingus Dingus's message.
     *
     * @param message Bingus Dingus's message.
     * @param profilePicture the profile picture to display.
     * @return the configured Bingus Dingus dialog box.
     */
    public static DialogBox getBingusDingusDialog(String message, Image profilePicture) {
        var dialogBox = new DialogBox(message, profilePicture);
        dialogBox.flip();
        return dialogBox;
    }
}
