package site.minnan.connector.application.service;

import site.minnan.connector.domain.vo.ListQueryVO;
import site.minnan.connector.domain.vo.project.ProjectVO;
import site.minnan.connector.userinterface.dto.ListQueryDTO;
import site.minnan.connector.userinterface.dto.project.AddProjectDTO;

public interface ProjectService {

    /**
     * 添加工程
     *
     * @param dto
     */
    void addProject(AddProjectDTO dto);

    /**
     * 查询工程列表
     *
     * @param dto
     * @return
     */
    ListQueryVO<ProjectVO> getProjectList(ListQueryDTO dto);
}
