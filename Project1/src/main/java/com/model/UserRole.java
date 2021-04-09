package com.model;

public enum UserRole {
    EMPLOYEE(1),FINANCE_MANAGER(2);
    private final int value;
    public int getValue() {
        return value;
    }
    private UserRole(int value){
        this.value=value;
    }

    public static UserRole getRole(String val){
        if(val.equalsIgnoreCase(EMPLOYEE.toString())){
            return EMPLOYEE;
        }
        else if(val.equalsIgnoreCase(FINANCE_MANAGER.toString())){
            return FINANCE_MANAGER;
        }
        return null;
    }
}
