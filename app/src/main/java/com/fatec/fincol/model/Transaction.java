package com.fatec.fincol.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Transaction implements Comparable<Transaction>{

    private Type type;
    private Timestamp date;
    private Double value;
    private String stringLocation;
    private String description;
    private boolean fixed;
    private boolean repeat;
    private int repeatTime;

    public Transaction(Type type, Timestamp date, Double value, String stringLocation, String description, boolean fixed, boolean repeat, int repeatTime) {
        this.type = type;
        this.date = date;
        this.value = value;
        this.stringLocation = stringLocation;
        this.description = description;
        this.fixed = fixed;
        this.repeat = repeat;
        this.repeatTime = repeatTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getStringLocation() {
        return stringLocation;
    }

    public void setStringLocation(String stringLocation) {
        this.stringLocation = stringLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public int getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(int repeatTime) {
        this.repeatTime = repeatTime;
    }

    @Override
    public int compareTo(Transaction t) {
        if (getDate() == null || t.getDate() == null) {
            return 0;
        }
        return getDate().compareTo(t.getDate());
    }


    public enum Type {
        Expense("Expense"), Income("Income");

        private String name;

        private static final Map<String, Transaction.Type> ENUM_MAP;

        Type (String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        // Build an immutable map of String name to enum pairs.
        // Any Map impl can be used.

        static {
            Map<String, Transaction.Type> map = new ConcurrentHashMap<String, Transaction.Type>();
            for (Transaction.Type instance : Transaction.Type.values()) {
                map.put(instance.getName(),instance);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }

        public static Transaction.Type get (String name) {
            return ENUM_MAP.get(name);
        }
    }



    @Override
    public String toString() {
        return "Transaction{" +
                "type=" + type +
                ", date=" + date +
                ", value=" + value +
                ", stringLocation='" + stringLocation + '\'' +
                ", description='" + description + '\'' +
                ", fixed=" + fixed +
                ", repeat=" + repeat +
                ", repeatTime=" + repeatTime +
                '}';
    }
}
