package hr.java.web.p315.controller;

import hr.java.web.p315.domain.Polaznik; // Import domenskog objekta Polaznik
import hr.java.web.p315.dto.PolaznikDTO; // Import DTO (Data Transfer Object) za Polaznik
import hr.java.web.p315.service.PolaznikService; // Import servisnog sučelja
import lombok.AllArgsConstructor; // Lombok anotacija za generiranje konstruktora sa svim argumentima
import org.springframework.http.ResponseEntity; // Spring klasa za HTTP odgovore
import org.springframework.web.bind.annotation.*; // Spring anotacije za REST kontroler

import java.util.List; // Import za rad s listama
import java.util.Optional; // Import za Optional tip podatka

@RestController // Označava da je ova klasa Spring REST kontroler
@RequestMapping("polaznici") // Osnovni URL putanja za sve metode u kontroleru
@AllArgsConstructor // Lombok generira konstruktor sa svim potrebnim dependency-ima
public class PolaznikController {

    private final PolaznikService polaznikService; // Injektiranje servisnog sloja

    @GetMapping // HTTP GET zahtjev za dohvat svih polaznika
    public List<Polaznik> getAllPolaznici() { // Metoda vraća listu svih polaznika
        return polaznikService.findAll(); // Poziv servisne metode
    }

    @GetMapping("/{id}") // HTTP GET zahtjev sa varijablom u putanji (ID polaznika)
    public ResponseEntity<Polaznik> getPolaznikById(@PathVariable Integer id) { // Metoda vraća polaznika po ID-u
        Optional<Polaznik> polaznik = polaznikService.findById(id); // Dohvat polaznika preko servisa
        return polaznik.map(ResponseEntity::ok) // Ako polaznik postoji, vrati 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // Ako ne postoji, vrati 404 Not Found
    }

    @PostMapping // HTTP POST zahtjev za kreiranje novog polaznika
    public Polaznik createPolaznik(@RequestBody PolaznikDTO polaznikDTO) { // Metoda prima DTO objekt u tijelu zahtjeva
        return polaznikService.save(polaznikDTO); // Poziv servisne metode za spremanje
    }

    @PutMapping("/{id}") // HTTP PUT zahtjev za ažuriranje postojećeg polaznika
    public ResponseEntity<Polaznik> updatePolaznik(@PathVariable Integer id, @RequestBody PolaznikDTO polaznikDTO) { // Metoda prima ID i DTO
        Optional<Polaznik> updatedPolaznik = polaznikService.update(id, polaznikDTO); // Poziv servisne metode za ažuriranje
        return updatedPolaznik.map(ResponseEntity::ok) // Ako ažuriranje uspije, vrati 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // Ako polaznik ne postoji, vrati 404 Not Found
    }

    @DeleteMapping("/{id}") // HTTP DELETE zahtjev za brisanje polaznika
    public void deletePolaznik(@PathVariable Integer id) { // Metoda prima ID polaznika
        polaznikService.deleteById(id); // Poziv servisne metode za brisanje
    }
}

/*

U Spring Boot-u, kad radimo REST API, želimo frontend (ili klijente poput Postman-a,
React aplikacije ili mobilne aplikacije) povezati s backend-om kako bi mogle raditi CRUD operacije nad podacima.
Za to koristimo kontrolere s anotacijama koje određuju vrstu HTTP zahtjeva.

HTTP metode:
POST → koristi se za Create (stvaranje novog zapisa)
GET → koristi se za Read (čitanje podataka)
PUT → koristi se za Update (ažuriranje podataka)
DELETE → koristi se za Delete (brisanje podataka)

Spring Boot nudi anotacije koje mapiraju HTTP metode na metode u kontroleru:
@PostMapping – mapira POST zahtjev na metodu (koristi se za Create)
@GetMapping – mapira GET zahtjev na metodu (koristi se za Read)
@PutMapping – mapira PUT zahtjev na metodu (koristi se za Update)
@DeleteMapping – mapira DELETE zahtjev na metodu (koristi se za Delete)

 */