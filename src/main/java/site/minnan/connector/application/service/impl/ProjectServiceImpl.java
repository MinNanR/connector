package site.minnan.connector.application.service.impl;

import cn.hutool.core.collection.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.minnan.connector.application.service.ProjectService;
import site.minnan.connector.domain.aggregrate.AuthUser;
import site.minnan.connector.domain.aggregrate.Project;
import site.minnan.connector.domain.repository.ProjectRepository;
import site.minnan.connector.domain.vo.ListQueryVO;
import site.minnan.connector.domain.vo.project.ProjectVO;
import site.minnan.connector.infrastructure.context.PrincipalContext;
import site.minnan.connector.userinterface.dto.ListQueryDTO;
import site.minnan.connector.userinterface.dto.project.AddProjectDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Minnan on 2021/09/28
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * 添加工程
     *
     * @param dto
     */
    @Override
    public void addProject(AddProjectDTO dto) {
        AuthUser user = PrincipalContext.getUser();
        Project project = Project.builder()
                .name(dto.getName())
                .userId(user.getId())
                .build();
        projectRepository.save(project);
    }

    /**
     * 查询工程列表
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<ProjectVO> getProjectList(ListQueryDTO dto) {
        AuthUser user = PrincipalContext.getUser();
        PageRequest page = PageRequest.of(dto.getPageIndex() - 1, dto.getPageSize());
        Integer totalCount = projectRepository.countAllByUserId(user.getId());
        List<Project> list = totalCount == 0 ? ListUtil.empty() : projectRepository.findAllByUserId(user.getId(), page);
        List<ProjectVO> voList = list.stream().map(ProjectVO::of).collect(Collectors.toList());
        return new ListQueryVO<>(voList, totalCount);
    }
}
