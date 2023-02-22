package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manufacturer implements Serializable {
    private String mName;

    private String country;

    @Override
    public String toString() {
        return "Name:" + mName + " Country:" + country;
    }
}
