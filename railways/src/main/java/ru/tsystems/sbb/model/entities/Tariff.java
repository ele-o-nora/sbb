package ru.tsystems.sbb.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Tariff extends AbstractEntity {

    @Column(name = "moment_from")
    private LocalDateTime momentFrom;

    @Column(name = "price_per_ten_leagues")
    private float pricePerTenLeagues;

}
