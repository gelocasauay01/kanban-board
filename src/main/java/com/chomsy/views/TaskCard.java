package com.chomsy.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.List;

public class TaskCard extends StackPane{
    private String description;

    public TaskCard(String description) {
        this.description = description;
        Pane translucentBackground = new Pane();
        GridPane actions = new GridPane();
        Button previous = new Button("<");
        Label label = new Label(this.description);
        Button next = new Button(">");

        for(Button btn : List.of(previous, next)) {
            btn.setStyle("-fx-border: none; -fx-background-color: transparent;");
        }

        translucentBackground.setStyle("-fx-background-color: orange; -fx-opacity: 0.5");
        label.setStyle("-fx-padding: 1em");
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.setPrefWidth(200);
        
        actions.setAlignment(Pos.CENTER);
        actions.addRow(0, previous, label, next);
        this.getChildren().addAll(translucentBackground, actions);
    }
}
