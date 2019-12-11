package jbc.timesheet.model;

public enum PayCode {

    WORK_REGULAR (1.0, "Regular" ),
    WORK_HOLIDAY (1.5, "Worked Holiday" ),
    OVERTIME_REGULAR (1.5, "Regular Overtime" ),
    OVERTIME_HOLIDAY (2.0, "Holiday Overtime" ),
    TIMEOFF (0.0, "Leave Without Pay" ),
    TIMEOFF_HOLIDAY ( 0.0, "Holiday" ),
    TIMEOFF_PAID ( 1.0, "Annual Leave" );

    private double rateFactor;
    private String label;

    PayCode(double rateFactor, String label) {
        this.rateFactor = rateFactor;
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public double getRateFactor() {
        return rateFactor;
    }

    public String getLabel() {
        return label;
    }

}
