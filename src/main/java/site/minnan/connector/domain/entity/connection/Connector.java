package site.minnan.connector.domain.entity.connection;

import java.util.Date;

/**
 * 连接接口
 *
 * @author Minnan on 2021/09/15
 */
public interface Connector {

    /**
     * 获得链接对象
     *
     * @return
     */
    Object getConnector();

    /**
     * 关闭连接
     */
    void closeConnector();

    /**
     * 确认连接未关闭，否则重新连接
     */
    void confirmConnected();

    /**
     * 执行命令
     *
     * @return
     */
    Object execute(String command);

//    /**
//     * 长时间未使用则关闭数据库连接
//     * @param date 最后一次使用在此时间前的连接需要关闭
//     */
//    void autoClose(Date date);
}
