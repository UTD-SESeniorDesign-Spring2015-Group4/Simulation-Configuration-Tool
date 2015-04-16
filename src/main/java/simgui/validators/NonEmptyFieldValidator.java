package simgui.validators;

public class NonEmptyFieldValidator implements FieldValidator{
    @Override
    public String validate(String value) {
        if(value == null || value.isEmpty())
            return "Value cannot be empty.";
        return "";
    }
}
