package hr.java.web.helloworld.repository;

import hr.java.web.helloworld.domain.Polaznik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolaznikRepository extends JpaRepository<Polaznik, Integer> {
}


//package hr.java.web.helloworld.repository;
//
//import hr.java.web.helloworld.domain.Polaznik;
//import java.util.List;
//import java.util.Optional;
//
//public interface PolaznikRepository {
//    List<Polaznik> findAll();
//    Optional<Polaznik> findById(Integer id);
//    Polaznik save(Polaznik polaznik);
//    void deleteById(Integer id);
//}