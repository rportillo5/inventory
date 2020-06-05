package com.banderasmusic.rest.inventory.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
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

    public Inventory(Long itm, String pc, String pd, int sc, int rp) {
        itemNumber = itm;
        productCode = pc;
        productDescription = pd;
        startingCount = sc;
        reorderPoint = rp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return getItemNumber().equals(inventory.getItemNumber()) &&
                getProductCode().equals(inventory.getProductCode()) &&
                getProductDescription().equals(inventory.getProductDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemNumber(), getProductCode(), getProductDescription());
    }

}
