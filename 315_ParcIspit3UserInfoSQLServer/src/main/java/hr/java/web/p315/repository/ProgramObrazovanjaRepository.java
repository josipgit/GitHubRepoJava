package hr.java.web.p315.repository;

import hr.java.web.p315.domain.ProgramObrazovanja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramObrazovanjaRepository extends JpaRepository<ProgramObrazovanja, Integer> {
}




//package hr.java.web.p315.repository;
//
//import hr.java.web.p315.domain.ProgramObrazovanja;
//import java.util.List;
//import java.util.Optional;
//
//public interface ProgramObrazovanjaRepository {
//    List<ProgramObrazovanja> findAll();
//    Optional<ProgramObrazovanja> findById(Integer id);
//    ProgramObrazovanja save(ProgramObrazovanja program);
//    void deleteById(Integer id);
//}