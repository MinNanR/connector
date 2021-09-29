package site.minnan.connector.userinterface.dto.project;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 添加工程参数
 *
 * @author Minnan on 2021/09/28
 */
@Data
public class AddProjectDTO {

    /**
     * 工程名称
     */
    @NotEmpty(message = "工程名称不能为空")
    private String name;
}
