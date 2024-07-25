package com.chomsy.views;

import com.chomsy.controllers.ProjectController;
import com.chomsy.interfaces.Observer;
import com.chomsy.models.Project;
import com.chomsy.models.ProjectColumn;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.stream.Collectors;

public class KanbanBoard extends VBox implements Observer<Project>{

    private Project project;

    public KanbanBoard(Project project) {
        super();
        this.project = project;
        this.project.subscribe(this);
        buildWidget();
    }

    private void buildWidget() {
        setStyle("-fx-padding: 2em");
        getChildren().addAll(
            createHeading(),
            createColumns()
        );
    }

    private Node createHeading() {
        BorderPane headingContainer = new BorderPane();
        headingContainer.setStyle("-fx-padding: 1em 0 1em 0");
        headingContainer.setLeft(createProjectTitle());
        headingContainer.setRight(createActionButtons());
        return headingContainer;
    }

    private Node createProjectTitle() {
        String title = project.getTitle();
        Label label = new Label(title);
        label.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-font-size: 2em; "
        );
        return label;
    }

    private Node createActionButtons() {
        final int SPACE_BETWEEN = 5;
        HBox actions = new HBox(SPACE_BETWEEN);
        actions.getChildren().addAll(createSaveButton(), createAddColumnButton());
        return actions;
    }

    private Button createSaveButton() {
        final String SAVE_BUTTON_DISPLAY_TEXT = "Save Project";
        Button saveProject = new Button(SAVE_BUTTON_DISPLAY_TEXT);
        saveProject.setStyle(
            "-fx-text-fill: white;" + 
            "-fx-background-color:orange;" +
            "-fx-border: none;" +
            "-fx-padding: 1em;" +
            "-fx-font-weight: bold;"
        );
        saveProject.setOnAction(e -> {
            ProjectController.getInstance().saveProject();
        });
        return saveProject;
    }

    private Button createAddColumnButton() {
        final String ADD_COLUMN_BUTTON_DISPLAY_TEXT = "Add Column";
        final String DIALOG_DISPLAY_TEXT = "New Column Title";
        Button addColumn = new Button(ADD_COLUMN_BUTTON_DISPLAY_TEXT);
        addColumn.setStyle(
            "-fx-text-fill: white; " +
            "-fx-background-color:orange; " +
            "-fx-border: none; " +
            "-fx-padding: 1em; " +
            "-fx-font-weight: bold; "
        );
        addColumn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText(DIALOG_DISPLAY_TEXT);
            dialog.showAndWait()
                .ifPresent(newColumn -> {
                    project.addColumn(newColumn);
                });
        });
        return addColumn;
    }

    private HBox createColumns() {
        HBox columnContainer = new HBox(20);
        columnContainer.getChildren().addAll(
            project
                .getColumns()
                .stream()
                .map(this::createKanbanColumn)
                .collect(Collectors.toList())
        );
        VBox.setVgrow(columnContainer, Priority.ALWAYS);
        return columnContainer;
    }

    private KanbanColumn createKanbanColumn(ProjectColumn column) {
        EventHandler<? super MouseEvent> onLabelMouseClicked = e -> {
            if(e.getButton() == MouseButton.SECONDARY) {
                promptRemoveColumn(column);
            }
        };
        KanbanColumn kanbanColumn = new KanbanColumn(column, onLabelMouseClicked);
        return kanbanColumn;
    }

    private void promptRemoveColumn(ProjectColumn column) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remove Column");
        alert.setHeaderText(String.format("Are you sure you want to remove %s?", column.getTitle()));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            project.removeColumn(column);
        } 
    }

    @Override
    public void update(Project payload) {
        getChildren().clear();
        buildWidget();
    }
}
