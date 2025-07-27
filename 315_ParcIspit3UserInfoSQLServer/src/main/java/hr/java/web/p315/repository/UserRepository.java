package hr.java.web.p315.repository;

import hr.java.web.p315.domain.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {
    public Optional<UserInfo> findByUsername(String username);
}
