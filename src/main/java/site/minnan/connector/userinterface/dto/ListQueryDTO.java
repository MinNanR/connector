package site.minnan.connector.userinterface.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 列表查询参数
 *
 * @author Minnan on 2021/09/28
 */
@Data
public class ListQueryDTO {

    /**
     * 每页显示数量
     */
    @NotNull(message = "每页显示数量不能为空")
    private Integer pageSize;

    /**
     * 页码
     */
    @NotNull(message = "页码不能为空")
    private Integer pageIndex;
}
