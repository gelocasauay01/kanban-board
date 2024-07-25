package com.chomsy.views;

import com.chomsy.controllers.ProjectController;
import com.chomsy.models.TransferDirection;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;

public class TaskCard extends StackPane{
    @Getter
    private String description;

    public TaskCard(String description) {
        this.description = description;
        buildWidget();
    }

    public void buildWidget() {
        setStyle("-fx-padding: 0.5em;");
        Node translucentBackground = createTranslucentBackground();
        Node actions = createActions();
        getChildren().addAll(translucentBackground, actions);
    }

    private void setButtonStyle(Button button) {
        button.setStyle(
            "-fx-border: none; " +
            "-fx-background-color: transparent;"
        );
    }

    private Node createActions() {
        GridPane actions = new GridPane();
        actions.setAlignment(Pos.CENTER);
        actions.addRow(0, createPreviousButton(), createDescription(), createNextButton());
        return actions;
    }

    private Node createTranslucentBackground() {
        Pane translucentBackground = new Pane();
        translucentBackground.setStyle(
            "-fx-background-color: orange; " +
            "-fx-opacity: 0.5; " +
            "-fx-border-radius: 1em; "
        );
        return translucentBackground;
    }

    private Button createPreviousButton() {
        final String BUTTON_SYMBOL = "◂";
        return createActionButton(BUTTON_SYMBOL, TransferDirection.LEFT);
    }

    private Button createNextButton() {
        final String BUTTON_SYMBOL = "▸";
        return createActionButton(BUTTON_SYMBOL, TransferDirection.RIGHT);
    }

    private Button createActionButton(String symbol, TransferDirection direction) {
        ProjectController controller = ProjectController.getInstance();
        Button button = new Button(symbol);
        setButtonStyle(button);
        button.setOnAction(e -> {
            controller.transferTask(description, direction);
        });
        return button;
    }

    private Label createDescription() {
        Label label = new Label(description);
        label.setStyle("-fx-padding: 1em");
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.setPrefWidth(200);
        return label;
    }
}
