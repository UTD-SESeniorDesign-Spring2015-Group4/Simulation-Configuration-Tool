package simgui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import simgui.validators.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Field {

    /**
     * Enum value for field type.
     */
    public enum FieldType {
        SIMULATION_NAME("Simulation Name", "runtitle", new FieldValidator[] {}),
        SIMULATION_TYPE("Simulation Type", "simtype", new FieldValidator[] {new ExactStringFieldValidator(new String[] {"infinite", "finite"})}),
        NUMBER_USERS("Number of Users", "numusers", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        WAN_ROUNDTRIP_MS("Wan Roundtrip MS", "wanroundtripms", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        REQUEST_MESSAGE_BYTES("Request Message Bytes", "requestmsgbytes", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        RESPONSE_MESSAGE_BYTES("Response Message Bytes", "responsemsgbytes", new FieldValidator[] {}),
        THINK_SECONDS("Think Seconds", "thinksecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        REQUEST_LOADBALANCER_SERVICE_TIME("Request Load Balance Service Time Seconds", "reqlbsecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        RESPONSE_LOADBALANCER_SERVICE_TIME("Response Load Balance Service Time Seconds(%s)", "rsplbsecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        REQUEST_LOADBALANCER_QUEUE_TIME("Request Load Balance Queue Time Seconds(%s)", "reqlbquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        RESPONSE_LOADBALANCER_QUEUE_TIME("Response Load Balance Queue Time Seconds(%s)", "rsplbquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        REQUEST_WEB_SERVICE_TIME("Request Web Service Time Seconds(%s)", "reqwebsecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        RESPONSE_WEB_SERVICE_TIME("Response Web Service Time Seconds(%s)", "rspwebsecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        REQUEST_WEB_QUEUE_TIME("Request Web Queue Time Seconds(%s)", "recwebquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        RESPONSE_WEB_QUEUE_TIME("Response Web Queue Time Seconds(%s)", "rspwebquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        REQUEST_MIDDLEWARE_SERVICE_TIME("Request MiddleWare Service Time Seconds(%s)", "reqmidsecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        RESPONSE_MIDDLEWARE_SERVICE_TIME("Response MiddleWare Service Time Seconds(%s)", "rspmidsecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        REQUEST_MIDDLEWARE_QUEUE_TIME("Request MiddleWare Queue Time Seconds(%s)", "reqmidquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        RESPONSE_MIDDLEWARE_QUEUE_TIME("Response MiddleWare Queue Time Seconds(%s)", "rspmidquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        REQUEST_APPLICATION_SERVICE_TIME("Request Application Service Time Seconds(%s)", "reqappsecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        RESPONSE_APPLICATION_SERVICE_TIME("Response Application Service Time Seconds(%s)", "rspappsecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        REQUEST_APPLICATION_QUEUE_TIME("Request Application Queue Time Seconds(%s)", "reqappquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        RESPONSE_APPLICATION_QUEUE_TIME("Response Application Queue Time Seconds(%s)", "rspappquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        REQUEST_DATABASE_SERVICE_TIME("Request Database Service Time Seconds(%s)", "reqdbsecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        REQUEST_DATABASE_QUEUE_TIME("Request Database Queue Time Seconds(%s)", "reqdbquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()}),
        REQUEST_WAN_SERVICE_TIME("Request WAN Service Time Seconds(%s)", "reqwansecs", new FieldValidator[] {new RangeFieldValidator(0.0001, 10)}),
        REQUEST_WAN_QUEUE_TIME("Request WAN Queue Time Seconds(%s)", "reqwanquesecs", new FieldValidator[] {new NonNegativeNumberFieldValidator()});

        public final String label;
        public final String xmlTag;
        public final FieldValidator[] validators;

        private FieldType(String label, String xmlTag, FieldValidator[] validators) {
            this.label = label;
            this.xmlTag = xmlTag;
            this.validators = validators;
        }

        public static FieldType fromXMLTag(String tag) {
            FieldType[] types = values();
            for (FieldType type : types) {
                if (type.xmlTag.equalsIgnoreCase(tag))
                    return type;
            }
            throw new IllegalArgumentException("This tag does not have an associated FieldType");
        }
    };

    /**
     * The type of the field.
     */
    private FieldType fieldType;

    /**
     * The label for the field.
     */
    private JLabel label;

    /**
     * The field itself.
     */
    private JTextField textField;

    /**
     * XML representation of the field.
     */
    private String xmlTag;

    /**
     * Optional name for the field. Only used with request/response time fields.
     */
    private String name;

    /**
     * List of validators that are applicable to the field.
     */
    private List<FieldValidator> validators;

    public Field(FieldType fieldType) {
        this(fieldType, "");
    }

    private Field(FieldType fieldType, String name) {
        // Set the field type.
        this.fieldType = fieldType;
        this.name = name;

        // Create a text field.
        this.textField = new JTextField(20);

        // Add common validators.
        this.validators = new ArrayList<FieldValidator>();
        this.validators.add(new NonEmptyFieldValidator());

        String labelText = String.format(fieldType.label, name);
        label = new JLabel(labelText);
        xmlTag = fieldType.xmlTag;
        for (FieldValidator validator : fieldType.validators) {
            validators.add(validator);
        }
    }

    public Field(Element element) {
        this(FieldType.fromXMLTag(element.getTagName()), element.getAttribute("name"));
    }

    public String getValue() {
        return textField.getText();
    }

    public Element toXML(Document doc) {
        Element element = doc.createElement(fieldType.xmlTag);
        if (!name.isEmpty()) {
            element.setAttribute("name", name);
        }
        return element;
    }

    public String validate() {
        for (FieldValidator validator : validators) {
            String error = validator.validate(getValue());
            if (!error.isEmpty()) {
                return error;
            }
        }
        return "";
    }

    public void addTo(JPanel labelPanel, JPanel fieldPanel) {

    }
}
