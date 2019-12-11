package jbc.timesheet.controller.util;

public enum ActionType {
    DEFAULT,
    PERSIST,
    MERGE,
    DELETE,
    LOGIN,
    LIST,
    VIEW,
    SEARCH;

    private String actionName;
    ActionType() {
        this.actionName = this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
