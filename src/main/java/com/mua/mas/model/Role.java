package com.mua.mas.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    Teacher("Teacher"),
    Student("Student"),
    CR("CR"),
    Pending("Pending");

    String role;

    Role(String role){
        this.role=role;
    }

    @JsonValue
    public String getRole() {
        return role;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Role fromValue(String role) {
        for (Role e : values()) {
            if (e.role.equalsIgnoreCase(role)) {
                return e;
            }
        }
        return null;
    }

}
