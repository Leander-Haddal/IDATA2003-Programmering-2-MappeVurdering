package no.ntnu.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerSetupView extends VBox {
    private final VBox fieldsBox = new VBox(5);
    private final Button addBtn    = new Button("➕ Add Player");
    private final Button removeBtn = new Button("➖ Remove Player");
    private final Button startBtn  = new Button("Start Game");

    public PlayerSetupView() {
        setSpacing(10);
        setPadding(new Insets(20));
        getChildren().addAll(
            new Label("Enter player names:"),
            fieldsBox,
            new HBox(10, addBtn, removeBtn),
            startBtn
        );
        IntStream.rangeClosed(1, 4).forEach(i -> addPlayerField());
        addBtn   .setOnAction(e -> addPlayerField());
        removeBtn.setOnAction(e -> removePlayerField());
    }

    private void addPlayerField() {
        int next = fieldsBox.getChildren().size() + 1;
        TextField tf = new TextField();
        tf.setPromptText("Player " + next);
        fieldsBox.getChildren().add(tf);
        updateButtons();
    }

    private void removePlayerField() {
        if (!fieldsBox.getChildren().isEmpty()) {
            fieldsBox.getChildren().remove(fieldsBox.getChildren().size() - 1);
        }
        updateButtons();
    }

    private void updateButtons() {
        int count = fieldsBox.getChildren().size();
        removeBtn.setDisable(count <= 2);
        addBtn   .setDisable(count >= 8);
    }

    public List<String> getPlayerNames() {
        return fieldsBox.getChildren().stream()
            .map(n -> ((TextField)n))
            .map(tf -> {
                String t = tf.getText().trim();
                return t.isEmpty() ? tf.getPromptText() : t;
            })
            .collect(Collectors.toList());
    }

    public Button getStartButton() {
        return startBtn;
    }
}
