package site.minnan.connector.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.minnan.connector.domain.aggregrate.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    /**
     * 根据用户id查找工程
     *
     * @param id
     * @param pageable
     * @return
     */
    List<Project> findAllByUserId(Integer id, Pageable pageable);

    /**
     * 计算工程数量
     *
     * @param id
     * @return
     */
    Integer countAllByUserId(Integer id);
}
