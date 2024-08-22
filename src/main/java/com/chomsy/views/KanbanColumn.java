package com.chomsy.views;

import com.chomsy.controllers.ProjectController;
import com.chomsy.interfaces.Observer;
import com.chomsy.models.ProjectColumn;

import java.util.List;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;

public class KanbanColumn extends VBox implements Observer<ProjectColumn>{
    private final ProjectColumn projectColumn;
    private final EventHandler<? super MouseEvent> onLabelMouseClicked;

    public KanbanColumn(ProjectColumn projectColumn, EventHandler<? super MouseEvent> onLabelMouseClicked) {
        super();
        this.projectColumn = projectColumn;
        this.projectColumn.subscribe(this);
        this.onLabelMouseClicked = onLabelMouseClicked;        
        buildWidget();
    }

    private Node createHeading() {
        Label heading = new Label(projectColumn.getTitle());
        heading.setOnMouseClicked(onLabelMouseClicked);
        heading.setStyle(
            "-fx-background-color: orange; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 1em"
        );

        Button add = createAddButton(projectColumn);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(heading);
        borderPane.setRight(add);
        return borderPane;
    }

    private Button createAddButton(ProjectColumn projectColumn) {
        Button add = new Button("+");
        add.setStyle(
            "-fx-border: none; " +
            "-fx-background-color: transparent; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: orange; " +
            "-fx-font-size: 2em"
        );
        add.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("New Task Description");
            dialog.showAndWait()
                .ifPresent(newTask -> {
                    ProjectController.getInstance().addTask(newTask, projectColumn);
                });
        });
        return add;
    }

    private ScrollPane createTaskCards() {
        VBox taskCardContainer = new VBox();
        List<TaskCard> taskCards = projectColumn
            .getTasks()
            .stream()
            .map(task -> {
                TaskCard taskCard = new TaskCard(task);
                taskCard.setOnMouseClicked(e -> {
                    if(e.getButton() == MouseButton.SECONDARY) {
                        promptRemoveTask(task);
                    }
                });
                return taskCard;
            })
            .collect(Collectors.toList());
        
        taskCardContainer.getChildren().addAll(taskCards);
        ScrollPane scrollPane = new ScrollPane(taskCardContainer);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private void promptRemoveTask(String task) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remove Task");
        alert.setHeaderText(String.format("Are you sure you want to remove %s?", task));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            projectColumn.removeTask(task);
        } 
    }

    private void buildWidget() {
        HBox.setHgrow(this, Priority.ALWAYS);
        setStyle(
            "-fx-border-color: orange; " +
            "-fx-border-radius: 1em; " +
            "-fx-border-width: 0.5em"
        );
        getChildren().addAll(createHeading(), createTaskCards());
    }

    @Override
    public void update(ProjectColumn payload) {
        getChildren().clear();
        buildWidget();
    }
}
