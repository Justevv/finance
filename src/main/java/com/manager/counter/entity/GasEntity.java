package com.manager.counter.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gas_counter")
@Data
public class GasEntity extends Counter {
}
