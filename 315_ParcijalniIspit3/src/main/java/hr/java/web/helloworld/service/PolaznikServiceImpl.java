package hr.java.web.helloworld.service;

import hr.java.web.helloworld.domain.Polaznik;
import hr.java.web.helloworld.dto.PolaznikDTO;
import hr.java.web.helloworld.repository.PolaznikRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PolaznikServiceImpl implements PolaznikService {

    private final PolaznikRepository polaznikRepository;

    @Override
    public List<Polaznik> findAll() {
        return polaznikRepository.findAll();
    }

    @Override
    public Optional<Polaznik> findById(Integer id) {
        return polaznikRepository.findById(id);
    }

    @Override
    public Polaznik save(PolaznikDTO polaznikDTO) {
        Polaznik polaznik = new Polaznik();
        polaznik.setIme(polaznikDTO.getIme());
        polaznik.setPrezime(polaznikDTO.getPrezime());
        return polaznikRepository.save(polaznik);
    }

    @Override
    public void deleteById(Integer id) {
        polaznikRepository.deleteById(id);
    }
}