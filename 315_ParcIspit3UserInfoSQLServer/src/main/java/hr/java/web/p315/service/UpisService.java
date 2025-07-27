package hr.java.web.p315.service;

import hr.java.web.p315.domain.Upis;
import hr.java.web.p315.dto.UpisDTO;
import java.util.List;
import java.util.Optional;

public interface UpisService {
    List<Upis> findAll();
    Optional<Upis> findById(Integer id);
    Upis save(UpisDTO upisDTO);
    void deleteById(Integer id);
    List<Upis> findByPolaznikId(Integer polaznikId);
    List<Upis> findByProgramObrazovanjaId(Integer programId);
}