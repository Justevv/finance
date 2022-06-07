package com.manager.counter.entity;

public enum CounterType {
    GAS("gas"),
    WATER("water"),
    ELECTRICITY("electricity");


    private String counterType;

    CounterType(String counterType) {
        this.counterType = counterType;
    }

    public String getCounterType() {
        return counterType;
    }

    @Override
    public String toString() {
        return counterType + "";
    }

}

