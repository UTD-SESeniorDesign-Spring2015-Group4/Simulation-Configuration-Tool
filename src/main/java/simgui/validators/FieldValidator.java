package simgui.validators;

/**
 * Created by jmdarling on 4/15/15.
 */
public interface FieldValidator {

    /**
     * Validates a value.
     * @param value The value to validate.
     * @return null if valid, a String describing the error if invalid
     */
    public String validate(String value);
}
