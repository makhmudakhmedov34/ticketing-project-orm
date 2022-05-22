package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;

    private final ProjectRepository projectRepository;

    private final UserService userService;

    private final UserMapper userMapper;

    private final TaskService taskService;

    public ProjectServiceImpl(ProjectMapper projectMapper, ProjectRepository projectRepository, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectMapper = projectMapper;
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return projectMapper.convertToDTO(projectRepository.findByProjectCode(code));
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return projectRepository.findAll().stream().map(projectMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO findById(String id) {
        return null;
    }

    @Override
    public void save(ProjectDTO project) {

        if (project.getProjectStatus() == null) {
            project.setProjectStatus(Status.OPEN);
        }
        projectRepository.save(projectMapper.convertToEntity(project));
    }

    @Override
    public void update(ProjectDTO dto) {
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());

        Project convertedProject = projectMapper.convertToEntity(dto);

        convertedProject.setId(project.getId());

        convertedProject.setProjectStatus(project.getProjectStatus());

        projectRepository.save(convertedProject);
    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode()+"-"+project.getId());

        projectRepository.save(project);

        taskService.deleteByProject(projectMapper.convertToDTO(project));

    }

    @Override
    public void complete(String code) {

        Project project = projectRepository.findByProjectCode(code);

        project.setProjectStatus(Status.COMPLETE);

        projectRepository.save(project);

        taskService.completeByProject(projectMapper.convertToDTO(project));
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        UserDTO currentUserDto = userService.finByUserName("harold@manager.com");

       List<Project> list = projectRepository.findAllByAssignedManager(userMapper.convertToEntity(currentUserDto));




        return list.stream().map(p -> {
            ProjectDTO obj = projectMapper.convertToDTO(p);
            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(p.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTask(p.getProjectCode()));

            return obj;

        }).collect(Collectors.toList());
    }


}
