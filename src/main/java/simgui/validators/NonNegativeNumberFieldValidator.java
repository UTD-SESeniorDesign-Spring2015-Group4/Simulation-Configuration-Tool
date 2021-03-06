package simgui.validators;

public class NonNegativeNumberFieldValidator implements FieldValidator {
    @Override
    public String validate(String value) {
        int temp = 0;
        try {
            temp = Integer.parseInt(value);
        } catch (Exception e) { // not a number
            return "Value must be an integer.";
        }
        if (temp < 0) {
            return "Value must be non-negative.";
        }
        return "";
    }
}
