package com.mua.mas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Comparator;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {

    private Double lat;
    private Double lon;


}
