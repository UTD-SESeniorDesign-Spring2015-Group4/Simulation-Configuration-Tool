package simgui.validators;

/**
 * Created by jmdarling on 4/15/15.
 */
public class RangeFieldValidator implements FieldValidator {
    private double rangeStart;
    private double rangeEnd;

    /**
     * Constructor.
     *
     * @param rangeStart The beginning of the range to check (inclusive).
     * @param rangeEnd The end of the range to check (inclusive).
     */
    public RangeFieldValidator(double rangeStart, double rangeEnd) {
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }

    @Override
    public String validate(String value) {
        double temp = 0;
        try {
            temp = Double.parseDouble(value);
        } catch (Exception e) { // not a number
            return "Value must be a number.";
        }
        if (temp < rangeStart || temp > rangeEnd) {
            return "Value must between " + rangeStart + " and " +rangeEnd + ".";
        }
        return null;
    }
}
