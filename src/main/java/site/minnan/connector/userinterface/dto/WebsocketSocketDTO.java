package site.minnan.connector.userinterface.dto;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/***
 * websocket前端传参
 * @author Minnan on 2021/09/27
 */

public class WebsocketSocketDTO {

    /**
     * 操作码，具体试接口商定
     */
    @Getter
    @Setter
    private Integer statusCode;

    /**
     * 参数
     */
    @Setter
    private JSONObject data;

    /**
     * 获取参数
     *
     * @param paramName 参数名称
     * @param clazz     参数类型
     * @param <T>
     * @return
     */
    public <T> T get(String paramName, Class<T> clazz) {
        return data.get(paramName, clazz);
    }

    /**
     * 获取list类型参数
     *
     * @param paramName 参数名称
     * @param clazz     list的泛型参数
     * @param <T>
     * @return
     */
    public <T> List<T> getList(String paramName, Class<T> clazz) {
        JSONArray jsonArray = data.getJSONArray(paramName);
        if (jsonArray == null) {
            return Collections.emptyList();
        }
        return jsonArray.toList(clazz);
    }

    public static WebsocketSocketDTO parseParam(String text) {
        return JSONUtil.toBean(text, WebsocketSocketDTO.class);
    }
}
