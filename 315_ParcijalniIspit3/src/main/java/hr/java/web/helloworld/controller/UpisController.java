package hr.java.web.helloworld.controller;

import hr.java.web.helloworld.domain.Upis;
import hr.java.web.helloworld.dto.UpisDTO;
import hr.java.web.helloworld.service.UpisService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/upisi")
@AllArgsConstructor
public class UpisController {

    private final UpisService upisService;

    @GetMapping
    public List<Upis> getAllUpisi() {
        return upisService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Upis> getUpisById(@PathVariable Integer id) {
        Optional<Upis> upis = upisService.findById(id);
        return upis.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Upis createUpis(@RequestBody UpisDTO upisDTO) {
        return upisService.save(upisDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUpis(@PathVariable Integer id) {
        upisService.deleteById(id);
    }

    @GetMapping("/polaznik/{polaznikId}")
    public List<Upis> getUpisiByPolaznik(@PathVariable Integer polaznikId) {
        return upisService.findByPolaznikId(polaznikId);
    }

    @GetMapping("/program/{programId}")
    public List<Upis> getUpisiByProgram(@PathVariable Integer programId) {
        return upisService.findByProgramObrazovanjaId(programId);
    }
}