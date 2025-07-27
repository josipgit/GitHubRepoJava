package hr.java.web.p315.service;

import hr.java.web.p315.domain.Polaznik; // Import domenskog objekta Polaznik
import hr.java.web.p315.dto.PolaznikDTO; // Import DTO objekta za prijenos podataka
import hr.java.web.p315.repository.PolaznikRepository; // Import repozitorijskog sučelja
import lombok.AllArgsConstructor; // Lombok anotacija za generiranje konstruktora
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service; // Spring anotacija za označavanje servisne klase

import java.util.List; // Import za rad s listama
import java.util.Optional; // Import za Optional tip podatka

@Service // Označava da je ova klasa Spring servis (komponenta poslovne logike)
@AllArgsConstructor // Lombok generira konstruktor koji injektira sve potrebne zavisnosti
public class PolaznikServiceImpl implements PolaznikService { // Implementacija PolaznikService sučelja

    private final PolaznikRepository polaznikRepository; // Injektiranje repozitorija kroz konstruktor
    private final PasswordEncoder passwordEncoder; // za hash-iranje passworda kad ubacujemo novog polaznika, autowired preko konstrukora (npr. @AllArgsConstructor)

    @Override // Implementacija metode iz sučelja PolaznikService
    public List<Polaznik> findAll() { // Metoda za dohvat svih polaznika
        return polaznikRepository.findAll(); // Poziv JPA repozitorija za dohvat svih zapisa
    }

    @Override // Implementacija metode iz sučelja PolaznikService
    public Optional<Polaznik> findById(Integer id) { // Metoda za dohvat polaznika po ID-u
        return polaznikRepository.findById(id); // Poziv JPA repozitorija za dohvat po ID-u
    }

//    @Override // Implementacija metode iz sučelja
//    public Polaznik save(PolaznikDTO polaznikDTO) { // Metoda za spremanje novog polaznika
//        Polaznik polaznik = new Polaznik(); // Stvaranje novog domenskog objekta
//        polaznik.setIme(polaznikDTO.getIme()); // Postavljanje imena iz DTO-a
//        polaznik.setPrezime(polaznikDTO.getPrezime()); // Postavljanje prezimena iz DTO-a
//        return polaznikRepository.save(polaznik); // Spremanje objekta u bazu i vraćanje spremljenog entiteta
//    }

    @Override
    public Polaznik save(PolaznikDTO polaznikDTO) {
        Polaznik polaznik = new Polaznik();
        polaznik.setIme(polaznikDTO.getIme());
        polaznik.setPrezime(polaznikDTO.getPrezime());
        polaznik.setUsername(polaznikDTO.getUsername());
//        polaznik.setPassword(polaznikDTO.getPassword()); // Ako dolazi hashiran iz frontenda
        // Ako želiš da backend hashira lozinku, dodajes PasswordEncoder ovdje ispod:
        polaznik.setPassword(passwordEncoder.encode(polaznikDTO.getPassword()));

        return polaznikRepository.save(polaznik);
    }

//    @Override // Implementacija metode iz sučelja PolaznikService
//    public Optional<Polaznik> update(Integer id, PolaznikDTO polaznikDTO) { // Nova metoda za ažuriranje polaznika
//        return polaznikRepository.findById(id) // Pronalaženje polaznika po ID-u
//                .map(existingPolaznik -> { // Ako polaznik postoji, izvrši ažuriranje
//                    existingPolaznik.setIme(polaznikDTO.getIme()); // Ažuriranje imena
//                    existingPolaznik.setPrezime(polaznikDTO.getPrezime()); // Ažuriranje prezimena
//                    return polaznikRepository.save(existingPolaznik); // Spremanje promjena i vraćanje ažuriranog entiteta
//                });
//    }

    @Override // Implementacija metode iz sučelja PolaznikService
    public Optional<Polaznik> update(Integer id, PolaznikDTO polaznikDTO) { // Nova metoda za ažuriranje polaznika
        return polaznikRepository.findById(id) // Pronalaženje polaznika po ID-u
                .map(existingPolaznik -> { // Ako polaznik postoji, izvrši ažuriranje
                    existingPolaznik.setIme(polaznikDTO.getIme()); // Ažuriranje imena
                    existingPolaznik.setPrezime(polaznikDTO.getPrezime()); // Ažuriranje prezimena
                    existingPolaznik.setUsername(polaznikDTO.getUsername()); // Ažuriranje korisničkog imena
                    existingPolaznik.setPassword(polaznikDTO.getPassword()); // Ažuriranje lozinke (hashirane ili čiste)

                    return polaznikRepository.save(existingPolaznik); // Spremanje promjena i vraćanje ažuriranog entiteta
                });
    }

//    @Override
//    public Optional<Polaznik> update(Integer id, PolaznikDTO polaznikDTO) {
//        return Optional.empty();
//    }

    @Override // Implementacija metode iz sučelja PolaznikService
    public void deleteById(Integer id) { // Metoda za brisanje polaznika
        polaznikRepository.deleteById(id); // Poziv JPA repozitorija za brisanje po ID-u
    }
}