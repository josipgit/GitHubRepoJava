package hr.java.web.p315.repository;

import hr.java.web.p315.domain.Upis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpisRepository extends JpaRepository<Upis, Integer> {

    @Query("SELECT u FROM Upis u WHERE u.polaznik.id = :polaznikId")
    List<Upis> findByPolaznikId(@Param("polaznikId") Integer polaznikId);

    @Query("SELECT u FROM Upis u WHERE u.programObrazovanja.id = :programId")
    List<Upis> findByProgramObrazovanjaId(@Param("programId") Integer programId);
}




//package hr.java.web.p315.repository;
//
//import hr.java.web.p315.domain.Upis;
//import java.util.List;
//import java.util.Optional;
//
//public interface UpisRepository {
//    List<Upis> findAll();
//    Optional<Upis> findById(Integer id);
//    Upis save(Upis upis);
//    void deleteById(Integer id);
//    List<Upis> findByPolaznikId(Integer polaznikId);
//    List<Upis> findByProgramObrazovanjaId(Integer programId);
//}