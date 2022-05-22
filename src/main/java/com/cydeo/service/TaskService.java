package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id);
    List<TaskDTO> listAllTasks();
    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);

    int totalNonCompletedTask(String projectCode);

    int totalCompletedTask(String projectCode);

    void deleteByProject(ProjectDTO project);

    void completeByProject(ProjectDTO convert);

    List<TaskDTO> listAllTasksByStatusIsNot(Status complete);

    void updateStatus(TaskDTO task);

    Object listAllTasksByStatus(Status status);
}
