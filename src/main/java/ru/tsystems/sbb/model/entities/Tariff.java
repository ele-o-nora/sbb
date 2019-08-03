package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Tariff extends AbstractEntity {

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "price_per_ten_leagues")
    private float pricePerTenLeagues;

}
