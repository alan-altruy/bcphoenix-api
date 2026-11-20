
package com.bcphoenix.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "seasons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Season {

    @Id
    private Integer id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;
}
