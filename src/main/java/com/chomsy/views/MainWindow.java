package com.chomsy.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

import com.chomsy.controllers.ProjectController;
import com.chomsy.interfaces.Observer;
import com.chomsy.repositories.YamlProjectRepository;

public class MainWindow extends VBox implements Observer<ProjectController> {
    private ProjectController controller;

    public MainWindow() {
        this.controller = ProjectController.getInstance();
        controller.subscribe(this);
        buildWidget();
    }

    private void buildWidget() {
        Node content = Objects.nonNull(controller.getProject()) ? new KanbanBoard(controller.getProject()) : createInitialDisplay();
        VBox.setVgrow(content, Priority.ALWAYS);
        getChildren().addAll(createMenuBar(), content);
    }

    private Label createInitialDisplay() {
        Label initialDisplay = new Label("Open or create a project.");
        initialDisplay.setAlignment(Pos.CENTER);
        initialDisplay.setStyle(
            "-fx-font-weight: bold; " +
            "-fx-font-size: 2em; "
        );
        initialDisplay.setMaxWidth(Double.MAX_VALUE);
        initialDisplay.setMaxHeight(Double.MAX_VALUE);
        return initialDisplay;
    }

    private MenuBar createMenuBar() {
        Menu menu = new Menu("Project");
        menu.getItems().addAll(createOpenMenuItem(), createNewMenuItem());
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    private MenuItem createOpenMenuItem() {
        MenuItem open = new MenuItem("Open");
        open.setOnAction(e -> {
            FileChooser fileChooser = createFileChooser();
            File file = fileChooser.showOpenDialog(null);
            if(Objects.nonNull(file)) {
                controller.setProjectFromFilePath(file.getAbsolutePath());
            }
        });
        return open;
    }

    private MenuItem createNewMenuItem() {
        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("New Project Title");
            Optional<String> title = inputDialog.showAndWait();
            title.ifPresent(value -> controller.createNewProject(value));
        });
        return newItem;
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        setExtensionFilters(fileChooser);
        setInitialDirectory(fileChooser);
        return fileChooser;
    }

    private void setExtensionFilters(FileChooser fileChooser) {
        String fileTypeDescription = "YAML File (*.yaml, *.yml)";
        String[] fileExtensions = { "*.yaml", "*.yml"};
        fileChooser.getExtensionFilters().add((new ExtensionFilter(fileTypeDescription, fileExtensions)));
    }

    private void setInitialDirectory(FileChooser fileChooser) {
        File defaultDirectory = new File(YamlProjectRepository.DEFAULT_PATH);
        if(!defaultDirectory.exists()) {
            defaultDirectory.mkdir();
        }
        fileChooser.setInitialDirectory(defaultDirectory);
    } 
    @Override
    public void update(ProjectController payload) {
        getChildren().clear();
        buildWidget();
    }
}
