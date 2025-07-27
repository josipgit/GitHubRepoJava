package hr.java.web.p315.service;

import hr.java.web.p315.domain.Polaznik; // Import domenskog objekta
import hr.java.web.p315.dto.PolaznikDTO; // Import DTO objekta
import java.util.List; // Import za rad s listama
import java.util.Optional; // Import za Optional tip

public interface PolaznikService {
    List<Polaznik> findAll(); // Metoda za dohvat svih polaznika
    Optional<Polaznik> findById(Integer id); // Metoda za dohvat polaznika po ID-u
    Polaznik save(PolaznikDTO polaznikDTO); // Metoda za spremanje novog polaznika
    Optional<Polaznik> update(Integer id, PolaznikDTO polaznikDTO); // Nova metoda za ažuriranje polaznika
    void deleteById(Integer id); // Metoda za brisanje polaznika
}