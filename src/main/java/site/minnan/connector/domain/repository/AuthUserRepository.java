package site.minnan.connector.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.minnan.connector.domain.aggregrate.AuthUser;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {

    Optional<AuthUser> getAuthUserByUsername(String username);
}
