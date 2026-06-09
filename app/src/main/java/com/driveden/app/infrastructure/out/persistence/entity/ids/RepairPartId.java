package com.driveden.app.infrastructure.out.persistence.entity.ids;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RepairPartId implements Serializable {

    @Column(name = "repair_id")
    private Long repairId;

    @Column(name = "part_id")
    private Long partId;
}
