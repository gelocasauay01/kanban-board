package com.chomsy.views;

import com.chomsy.models.ProjectColumn;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.stream.Collectors;


public class KanbanColumn extends VBox {
    private ProjectColumn projectColumn;

    public KanbanColumn(ProjectColumn projectColumn) {
        super();
        this.projectColumn = projectColumn;
        
        HBox.setHgrow(this, Priority.ALWAYS);
        setStyle("-fx-border-color: orange; -fx-border-radius: 1em; -fx-border-width: 0.5em");

        getChildren().addAll(createHeading(this.projectColumn.getTitle()), createTaskCards(this.projectColumn.getTasks()));
    }

    private static BorderPane createHeading(String title) {
        Label heading = new Label(title);
        heading.setStyle("-fx-background-color: orange; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 1em");

        Button add = new Button("+");
        add.setStyle("-fx-border: none; -fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: orange; -fx-font-size: 2em");
        add.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("New Task Description");
            dialog.showAndWait()
                .ifPresent(newTask -> {
                    System.out.println(newTask);
                });;
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(heading);
        borderPane.setRight(add);
        return borderPane;
    }

    private static VBox createTaskCards(List<String> tasks) {
        final int SPACE_BETWEEN_CARDS = 5;
        VBox taskCards = new VBox(SPACE_BETWEEN_CARDS);
        taskCards.getChildren().addAll(tasks.stream().map(TaskCard::new).collect(Collectors.toList()));
        return taskCards;
    }
}
