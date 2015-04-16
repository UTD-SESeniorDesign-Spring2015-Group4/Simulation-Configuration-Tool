package simgui.validators;

/**
 * Created by jmdarling on 4/15/15.
 */
public class ExactStringFieldValidator implements FieldValidator{
    private String[] validStrings;


    public ExactStringFieldValidator(String[] validStrings) {
        this.validStrings = validStrings;
    }

    @Override
    public String validate(String value) {
        for (String validString : validStrings) {
            if (value.equals(validString)) {
                return null;
            }
        }


        String validStringList = "";
        for (int k = 0; k < validStrings.length; k++) {
            validStringList += validStrings[k];
            if(k != validStrings.length - 1) {
                validStringList += ", ";
            }
        }
        return "Value must be one of these values: " + validStringList + ".";
    }
}
