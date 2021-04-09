package com.model;

public enum ReimbursementType {
    LODGING(1),TRAVEL(2),FOOD(3),OTHER(4);
    private final int value;
    public int getValue() {
        return value;
    }
    private ReimbursementType(int value){
        this.value=value;
    }

    public static ReimbursementType getType(String val){
        if(val.equalsIgnoreCase(LODGING.toString())){
            return LODGING;
        }
        else if(val.equalsIgnoreCase(TRAVEL.toString())){
            return TRAVEL;
        }
        else if(val.equalsIgnoreCase(FOOD.toString())){
            return FOOD;
        }
        else if(val.equalsIgnoreCase(OTHER.toString())){
            return OTHER;
        }
        return null;
    }

}
