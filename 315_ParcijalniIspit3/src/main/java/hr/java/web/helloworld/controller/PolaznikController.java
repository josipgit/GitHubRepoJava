package hr.java.web.helloworld.controller;

import hr.java.web.helloworld.domain.Polaznik;
import hr.java.web.helloworld.dto.PolaznikDTO;
import hr.java.web.helloworld.service.PolaznikService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/polaznici")
@AllArgsConstructor
public class PolaznikController {

    private final PolaznikService polaznikService;

    @GetMapping
    public List<Polaznik> getAllPolaznici() {
        return polaznikService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Polaznik> getPolaznikById(@PathVariable Integer id) {
        Optional<Polaznik> polaznik = polaznikService.findById(id);
        return polaznik.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Polaznik createPolaznik(@RequestBody PolaznikDTO polaznikDTO) {
        return polaznikService.save(polaznikDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePolaznik(@PathVariable Integer id) {
        polaznikService.deleteById(id);
    }
}