package com.manager.counter.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "gas_counter")
@Data
public class GasEntity extends Counter {
}
