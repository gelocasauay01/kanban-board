package com.chomsy.models;

import java.util.List;

import com.chomsy.interfaces.Observable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Project extends Observable<Project>{
    private String title;
    private List<ProjectColumn> columns;

    public void addColumn(String title) {
        columns.add(new ProjectColumn(title));
        notifyListeners(this);
    }

    public void removeColumn(ProjectColumn column) {
        columns.remove(column);
        notifyListeners(this);
    }
}
