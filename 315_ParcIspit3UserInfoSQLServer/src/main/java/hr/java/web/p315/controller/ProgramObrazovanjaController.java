package hr.java.web.p315.controller;

import hr.java.web.p315.domain.ProgramObrazovanja;
import hr.java.web.p315.dto.ProgramObrazovanjaDTO;
import hr.java.web.p315.service.ProgramObrazovanjaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/programi")
@AllArgsConstructor
public class ProgramObrazovanjaController {

    private final ProgramObrazovanjaService programService;

    @GetMapping
    public List<ProgramObrazovanja> getAllProgrami() {
        return programService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramObrazovanja> getProgramById(@PathVariable Integer id) {
        Optional<ProgramObrazovanja> program = programService.findById(id);
        return program.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProgramObrazovanja createProgram(@RequestBody ProgramObrazovanjaDTO programDTO) {
        return programService.save(programDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProgram(@PathVariable Integer id) {
        programService.deleteById(id);
    }
}