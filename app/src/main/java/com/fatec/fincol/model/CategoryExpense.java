package com.fatec.fincol.model;

public class CategoryExpense {

    private String name;
    private float value;
    private float percent;

    public CategoryExpense(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "CategoryExpense{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
