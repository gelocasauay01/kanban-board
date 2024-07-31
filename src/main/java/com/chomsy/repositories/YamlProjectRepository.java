package com.chomsy.repositories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.chomsy.models.Project;
import com.chomsy.models.ProjectColumn;

public class YamlProjectRepository implements ProjectRepository {
    private final static String TITLE_PROPERTY = "title";
    private final static String COLUMNS_PROPERTY = "columns";
    private final static String TASKS_PROPERTY = "tasks";
    public final static String DEFAULT_PATH = "projects";
    
    private Yaml yaml;
    private String saveDirectoryPath;

    public YamlProjectRepository(String saveDirectoryPath) {
        this.yaml = new Yaml(new Constructor(Project.class, new LoaderOptions()));
        this.saveDirectoryPath = saveDirectoryPath;
    }

    public YamlProjectRepository() {
        this.yaml = new Yaml(new Constructor(Project.class, new LoaderOptions()));
        this.saveDirectoryPath = DEFAULT_PATH;
    }

    @Override
    public Optional<Project> getProject(String filePath) {
        Path path = Paths.get(filePath);
        try(InputStream inputStream = Files.newInputStream(path)) {
            Project found = yaml.load(inputStream);
            return Optional.ofNullable(found);
        } catch(IOException io) {
            io.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Project> getAllProjects() {
        List<File> projectFiles = getYamlFromSaveDirectory();
        List<Project> projects = new LinkedList<>();
        for(File file : projectFiles) {
            getProject(file.getAbsolutePath()).ifPresent(project -> projects.add(project));
        }
        return projects;
    }

    private List<File> getYamlFromSaveDirectory() {
        final String YAML_EXTENSION = ".yaml";
        File saveFolder = new File(saveDirectoryPath);
        return Arrays.stream(saveFolder.listFiles())
            .filter(file -> file.isFile() && file.getName().endsWith(YAML_EXTENSION))
            .collect(Collectors.toList());
    }

    @Override
    public void saveProject(Project project) {
        createDirectoryIfNotExist(saveDirectoryPath);
        StringWriter writer = new StringWriter();
        yaml.dump(convertProjectToMap(project), writer);    
        writeFile(writer.toString(), getSavePath(project.getTitle()));
    }

    private void createDirectoryIfNotExist(String path) {
        File directory = new File(path);
        if(!directory.exists()) {
            directory.mkdirs();
        }
    }

    private String getSavePath(String projectTitle) {
        return String.format("%s/%s.yaml", saveDirectoryPath, projectTitle);
    }

    private Map<String, Object> convertProjectToMap(Project project) {
        Map<String, Object> projectMap = new LinkedHashMap<>();
        projectMap.put(TITLE_PROPERTY, project.getTitle());
        projectMap.put(COLUMNS_PROPERTY, convertColumnsToMap(project.getColumns()));
        return projectMap;
    }

    private List<Map<String, Object>> convertColumnsToMap(List<ProjectColumn> columns) {
        List<Map<String, Object>> columnMaps = new LinkedList<>();
        for(ProjectColumn currentColumn : columns) {
            Map<String,Object> columnMap = new LinkedHashMap<>();
            columnMap.put(TITLE_PROPERTY, currentColumn.getTitle());
            columnMap.put(TASKS_PROPERTY, currentColumn.getTasks());
            columnMaps.add(columnMap);
        }
        return columnMaps;
    }
    
    private void writeFile(String content, String fileName) {
        try(FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch(IOException io) {
            io.printStackTrace();
        }
    }
    
}