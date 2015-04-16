package simgui.validators;

/**
 * Created by jmdarling on 4/15/15.
 */
public class NonEmptyFieldValidator implements FieldValidator{
    @Override
    public String validate(String value) {
        if(value == null || value.isEmpty())
            return "Value cannot be empty.";
        return null;
    }
}
