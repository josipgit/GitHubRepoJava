package hr.java.web.p315.service;

import hr.java.web.p315.domain.Polaznik;
import hr.java.web.p315.domain.ProgramObrazovanja;
import hr.java.web.p315.domain.Upis;
import hr.java.web.p315.dto.UpisDTO;
import hr.java.web.p315.repository.PolaznikRepository;
import hr.java.web.p315.repository.ProgramObrazovanjaRepository;
import hr.java.web.p315.repository.UpisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpisServiceImpl implements UpisService {

    private final UpisRepository upisRepository;
    private final PolaznikRepository polaznikRepository;
    private final ProgramObrazovanjaRepository programRepository;

    @Override
    public List<Upis> findAll() {
        return upisRepository.findAll();
    }

    @Override
    public Optional<Upis> findById(Integer id) {
        return upisRepository.findById(id);
    }

    @Override
    public Upis save(UpisDTO upisDTO) {
        Optional<Polaznik> polaznik = polaznikRepository.findById(upisDTO.getPolaznikId());
        Optional<ProgramObrazovanja> program = programRepository.findById(upisDTO.getProgramObrazovanjaId());

        if (polaznik.isEmpty() || program.isEmpty()) {
            throw new IllegalArgumentException("Polaznik ili program obrazovanja ne postoji");
        }

        Upis upis = new Upis();
        upis.setPolaznik(polaznik.get());
        upis.setProgramObrazovanja(program.get());
        return upisRepository.save(upis);
    }

    @Override
    public void deleteById(Integer id) {
        upisRepository.deleteById(id);
    }

    @Override
    public List<Upis> findByPolaznikId(Integer polaznikId) {
        return upisRepository.findByPolaznikId(polaznikId);
    }

    @Override
    public List<Upis> findByProgramObrazovanjaId(Integer programId) {
        return upisRepository.findByProgramObrazovanjaId(programId);
    }
}