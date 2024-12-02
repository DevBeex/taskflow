package com.trello.trello.constants;

public class EntityConstant {
    public enum RoleName {
        USER,
        ADMIN;
        @Override
        public String toString() {
            return name();
        }
    }

    public enum StatusName {
        ACTIVE,
        INACTIVE;

        @Override
        public String toString() {
            return name();
        }
    }
}
