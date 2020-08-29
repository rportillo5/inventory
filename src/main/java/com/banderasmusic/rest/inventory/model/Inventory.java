package com.banderasmusic.rest.inventory.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@ApiModel(description = "Using Lombok annotations")
@EqualsAndHashCode
public class Inventory {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long itemNumber;
    @NotBlank
    private String productCode;
    @Size(min=10, message="Enter at least 10 Characters...")
    private String productDescription;
    @Positive
    private int startingCount;
    @Min(value = 5)
    private int reorderPoint;

    public Inventory() {
    }
}
