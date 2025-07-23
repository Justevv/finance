package com.manager.counter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "counter")
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Counter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "counter_generator")
    @SequenceGenerator(name = "counter_generator", sequenceName = "counter_seq", allocationSize = 1)
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private double price;
    private double value;
    private double paid;
}
