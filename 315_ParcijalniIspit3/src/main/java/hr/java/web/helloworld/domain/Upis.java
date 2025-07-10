package hr.java.web.helloworld.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Upis") // Eksplicitno ime tablice
public class Upis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUpis") // Eksplicitno ime kolone
    private Integer id;

    @ManyToOne // više Upis zapisa može referencirati jednog Polaznik
    @JoinColumn(name = "PolaznikID", nullable = false) // Eksplicitno ime FK kolone
    private Polaznik polaznik;

    @ManyToOne  // Više Upis zapisa može biti povezano sa jednim ProgramObrazovanja
    @JoinColumn(name = "ProgramObrazovanjaID", nullable = false) // Eksplicitno ime FK kolone
    private ProgramObrazovanja programObrazovanja;
}
