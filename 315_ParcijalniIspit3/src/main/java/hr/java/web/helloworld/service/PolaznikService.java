package hr.java.web.helloworld.service;

import hr.java.web.helloworld.domain.Polaznik;
import hr.java.web.helloworld.dto.PolaznikDTO;
import java.util.List;
import java.util.Optional;

public interface PolaznikService {
    List<Polaznik> findAll();
    Optional<Polaznik> findById(Integer id);
    Polaznik save(PolaznikDTO polaznikDTO);
    void deleteById(Integer id);
}