package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Souvenir implements Serializable {
    private String sName;

    private LocalDate creationDate;

    private BigDecimal price;

    private Manufacturer manufacturer;

    @Override
    public String toString() {
        return "Name:" + sName + " Creation date:" + creationDate +" Price:"+price+" Manufacturer name:"+manufacturer.getMName()+" Manufacturer country:"+manufacturer.getCountry();
    }
}

