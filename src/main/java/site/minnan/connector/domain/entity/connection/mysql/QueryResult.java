package site.minnan.connector.domain.entity.connection.mysql;

import cn.hutool.json.JSONArray;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/***
 * 查询结果
 * @author Minnan on 2021/09/27
 */
@Getter
@Setter
public class QueryResult {

    /**
     * 列名
     */
    private List<String> columnNameList;

    /**
     * 查询数据
     */
    private JSONArray data;

    public QueryResult(List<String> columnNameList, JSONArray data) {
        this.columnNameList = columnNameList;
        this.data = data;
    }
}
