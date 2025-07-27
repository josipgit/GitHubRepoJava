package hr.java.web.p315.service;

import hr.java.web.p315.domain.ProgramObrazovanja;
import hr.java.web.p315.dto.ProgramObrazovanjaDTO;
import java.util.List;
import java.util.Optional;

public interface ProgramObrazovanjaService {
    List<ProgramObrazovanja> findAll();
    Optional<ProgramObrazovanja> findById(Integer id);
    ProgramObrazovanja save(ProgramObrazovanjaDTO programDTO);
    void deleteById(Integer id);
}