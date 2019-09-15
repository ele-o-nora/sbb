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

/**
 * Base tariff used for ticket pricing.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Tariff extends AbstractEntity {

    /**
     * Timestamp that indicates when this tariff was created.
     */
    @Column(name = "moment_from", nullable = false, unique = true)
    private LocalDateTime momentFrom;

    /**
     * Specific sum that should be paid for each ten leagues travelled.
     */
    @Column(name = "price_per_ten_leagues", nullable = false)
    private float pricePerTenLeagues;

}
