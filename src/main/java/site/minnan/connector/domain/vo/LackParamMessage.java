package site.minnan.connector.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 缺失参数时的消息
 */
@Data
@AllArgsConstructor
public class LackParamMessage {

    private String field;

    private String message;
}
