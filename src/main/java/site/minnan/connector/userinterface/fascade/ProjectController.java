package site.minnan.connector.userinterface.fascade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.connector.application.service.ProjectService;
import site.minnan.connector.domain.vo.ListQueryVO;
import site.minnan.connector.domain.vo.project.ProjectVO;
import site.minnan.connector.userinterface.dto.ListQueryDTO;
import site.minnan.connector.userinterface.dto.project.AddProjectDTO;
import site.minnan.connector.userinterface.response.ResponseEntity;

import javax.validation.Valid;

/**
 * 工程控制器
 *
 * @author Minnan on 2021/09/28
 */
@RestController
@RequestMapping("connector/project")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * 添加工程
     *
     * @param dto
     * @return
     */
    @PostMapping("addProject")
    public ResponseEntity<?> addProject(@RequestBody @Valid AddProjectDTO dto) {
        projectService.addProject(dto);
        return ResponseEntity.success();
    }

    /**
     * 查询工程列表
     *
     * @return
     */
    @PostMapping("getProjectList")
    public ResponseEntity<ListQueryVO<ProjectVO>> getProjectList(@RequestBody @Valid ListQueryDTO dto) {
        ListQueryVO<ProjectVO> vo = projectService.getProjectList(dto);
        return ResponseEntity.success(vo);
    }
}
