package site.minnan.connector.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.minnan.connector.domain.aggregrate.DatabaseConnection;

import java.awt.print.Pageable;
import java.util.List;

public interface DatabaseConnectionRepository extends JpaRepository<DatabaseConnection, Integer> {

    /**
     * 根据项目id查询连接
     *
     * @param projectId 项目id
     * @return
     */
    List<DatabaseConnection> findAllByProjectId(Integer projectId);
}
