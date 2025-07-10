package hr.java.web.helloworld.service;

import hr.java.web.helloworld.domain.ProgramObrazovanja;
import hr.java.web.helloworld.dto.ProgramObrazovanjaDTO;
import java.util.List;
import java.util.Optional;

public interface ProgramObrazovanjaService {
    List<ProgramObrazovanja> findAll();
    Optional<ProgramObrazovanja> findById(Integer id);
    ProgramObrazovanja save(ProgramObrazovanjaDTO programDTO);
    void deleteById(Integer id);
}