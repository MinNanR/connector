package site.minnan.connector.domain.vo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import site.minnan.connector.domain.aggregrate.Project;

/***
 * 工程展示数据
 * @author Minnan on 2021/09/28
 */
@Getter
@Setter
@AllArgsConstructor
public class ProjectVO {

    private Integer id;

    private String name;

    public static ProjectVO of(Project project) {
        return new ProjectVO(project.getId(), project.getName());
    }
}
