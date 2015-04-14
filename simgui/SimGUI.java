package simgui;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

// These require java-json (http://www.json.org/java/).
// This requires json (https://code.google.com/p/google-gson/).

public class SimGUI extends JFrame {

    final String SIMULATION_FILE_NAME = "SimCmdv10.py";
    ArrayList<JTextField> fields;
    static File openFile;
    File saveFile;
    File outputFile;
    String currentConfiguration;
    JTextArea simulationOutput;
    JPanel mainPanel;
    JPanel bottomPanel;
    double widthPercent = 0.33;
    double heightPercent = 0.75;
    Simulationrun currentConfig;
    Object mouseClick;
    Map<JTextField, Component> requestServiceFieldMap, requestQueueFieldMap, responseServiceFieldMap, responseQueueFieldMap;

    // Will be set to true if we have read in a custom topology configuration, false otherwise.
    boolean customConfig = true;

    //Make simgui.Component Class
    //Use an array list to read all of the component names, and values (maybe types too)
    //traverse through arraylist with for or for each loop
    //Update GUI to reflect the changes (make jtext of arr[1, 2, etc]

    // The location of the json file we will be importing.
    /*
    Take note of the structure of the JSON file as this is quite important. You can see the the outermost element of the
    file is an array. You can tell this because it is surrounded by curly braces ('{' and '}'). Inside of that object,
    we have a key ("components") that is pointing to an array. You can tell it is an array as it is surrounded by square
    brackets ('[' and ']'). Inside this array we have more objects each with 4 fields for "id" "name" "type" "connections".
    This structure will be quite important to understand when we start parsing it out.
     */

    public SimGUI(List<Component> components) {
        super("Simulation Configuration"); // title of window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JScrollPane mainScrollPane;
        JScrollPane bottomScrollPane;
        mainPanel = new JPanel(new GridLayout(0, 2)); // panel to hold text fields and their labels
        JPanel labelPanel = new JPanel(new GridLayout(0, 1)); // panel to hold text field labels
        JPanel fieldPanel = new JPanel(new GridLayout(0, 1)); // panel to hold text fields

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // panel to hold buttons
        bottomPanel = new JPanel(new BorderLayout()); // panel to hold simulation results
        JPanel bottomLabelPanel = new JPanel(new GridLayout(1, 1)); // panel for simulation results label

        JButton openBtn = new JButton("Open Configuration"); // button to open an XML file
        JButton saveBtn = new JButton("Save Configuration"); // button to save an XML file
        JButton startBtn = new JButton("Start"); // button to start the simulation
        JButton outputBtn = new JButton("Save Output"); // button to save simulation results
        JButton manfBtn = new JButton("Open Manifest File"); // button to open manf file

        simulationOutput = new JTextArea(15, 25); // text area to hold simulation results
        JLabel outputLabel = new JLabel("Simulation Output: ", JLabel.LEFT); // label for simulation results

        mainPanel.add(labelPanel, BorderLayout.WEST);
        mainPanel.add(fieldPanel, BorderLayout.CENTER);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        fields = new ArrayList<JTextField>();
        requestServiceFieldMap = new HashMap<JTextField, Component>();
        requestQueueFieldMap = new HashMap<JTextField, Component>();
        responseServiceFieldMap = new HashMap<JTextField, Component>();
        responseQueueFieldMap = new HashMap<JTextField, Component>();

        /*
         * Creating the text fields, setting their width, creating their label, adding label to the label panel, adding text field to text field panel.
         */

        /* Cameron this is the part that I added in, but it is not working right.
            My logic is that if the manf button is clicked it will display
            the request and response labels then the component name.
            This is what I understood from both your example and Jon's example.
            So basically it should display Request WAN Service Time Seconds (Cameron)
                                            Request WAN Queue Time Seconds (Cameron)
                                            so and and so forth.
            I did this because it Jon sent me this
            "Request WAN Service Time Seconds (<insert component name here>)"
            So that is what I understood.
            He also said this "It didn't look like each component had the two required fields either, have you fixed that?"
            But I didn't get a chance to clarify, so no I dont think I have this done.
            I'm going to ask Jon if he wanted the request and response stuff listed out like the original java program
            but each component name will be displayed next to one.
            Because that's going to be a shit load of lines.
            It's 6am and I'm exhausted, so I'm really not making any progress.
         */

        ArrayList<String> defaultLabels = new ArrayList<>();

        defaultLabels.add("Simulation Name");
        defaultLabels.add("Simulation Type");
        defaultLabels.add("Number of Users");
        defaultLabels.add("WAN Roundtrip MS");
        defaultLabels.add("Request Message Bytes");
        defaultLabels.add("Response Message Bytes");
        defaultLabels.add("Think Seconds");

        if(components == null) {
            defaultLabels.add("Request WAN Service Time Seconds");
            defaultLabels.add("Request WAN Queue Time Seconds");
            defaultLabels.add("Request Load Balance Service Time Seconds");
            defaultLabels.add("Request Load Balance Queue Time Seconds");
            defaultLabels.add("Request Web Service Time Seconds");
            defaultLabels.add("Request Web Queue Time Seconds");
            defaultLabels.add("Request MiddleWare Service Time Seconds");
            defaultLabels.add("Request MiddleWare Queue Time Seconds");
            defaultLabels.add("Request Application Service Time Seconds");
            defaultLabels.add("Request Application Queue Time Seconds");
            defaultLabels.add("Request Database Service Time Seconds");
            defaultLabels.add("Request Database Queue Time Seconds");
            defaultLabels.add("Response Application Service Time Seconds");
            defaultLabels.add("Response Application Queue Time Seconds");
            defaultLabels.add("Response MiddleWare Service Time Seconds");
            defaultLabels.add("Response MiddleWare Queue Time Seconds");
            defaultLabels.add("Response Web Service Time Seconds");
            defaultLabels.add("Response Web Queue Time Seconds");
            defaultLabels.add("Response Load Balance Service Time Seconds");
            defaultLabels.add("Response Load Balance Queue Time Seconds");
            customConfig = false;
        }

        for(int i = 0; i < defaultLabels.size(); i++){
            JLabel lab = new JLabel(defaultLabels.get(i), JLabel.LEFT);
            labelPanel.add(lab);

            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField field = new JTextField();
            field.setColumns(20);
            fields.add(field);
            p.add(field);
            fieldPanel.add(p);
        }
        if(components != null) {
            for (int i = 0; i < components.size(); i++) {
                Component component = components.get(i);
                String type = component.getType();
                // Create Request Service time field
                JLabel labService = new JLabel(component.getRequestServiceFieldLabel(), JLabel.LEFT);
                labelPanel.add(labService);

                JPanel pService = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JTextField fieldService = new JTextField();
                fieldService.setColumns(20);
                fields.add(fieldService);
                requestServiceFieldMap.put(fieldService, component);
                pService.add(fieldService);
                fieldPanel.add(pService);

                // Create Request Queue time field
                JLabel labQueue = new JLabel(component.getRequestQueueFieldLabel(), JLabel.LEFT);
                labelPanel.add(labQueue);

                JPanel pQueue = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JTextField fieldQueue = new JTextField();
                fieldQueue.setColumns(20);
                fields.add(fieldQueue);
                requestQueueFieldMap.put(fieldQueue, component);
                pQueue.add(fieldQueue);
                fieldPanel.add(pQueue);

                if(type.equals("ApplicationServer") || type.equals("MiddlewareServer") || type.equals("WebfrontendServer") || type.equals("Loadbalancer")) {
                    // Create Response Service time field
                    JLabel labRspService = new JLabel(component.getResponseServiceFieldLabel(), JLabel.LEFT);
                    labelPanel.add(labRspService);

                    JPanel pRspService = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JTextField fieldRspService = new JTextField();
                    fieldRspService.setColumns(20);
                    fields.add(fieldRspService);
                    responseServiceFieldMap.put(fieldRspService, component);
                    pRspService.add(fieldRspService);
                    fieldPanel.add(pRspService);

                    // Create Response Queue time field
                    JLabel labRspQueue = new JLabel(component.getResponseQueueFieldLabel(), JLabel.LEFT);
                    labelPanel.add(labRspQueue);

                    JPanel pRspQueue = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JTextField fieldRspQueue = new JTextField();
                    fieldRspQueue.setColumns(20);
                    fields.add(fieldRspQueue);
                    responseQueueFieldMap.put(fieldRspQueue, component);
                    pRspQueue.add(fieldRspQueue);
                    fieldPanel.add(pRspQueue);
                }
            }
        }



        mainScrollPane = new JScrollPane(mainPanel); // scroll pane to hold text fields and their labels.
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16); // increase scroll speed
        getContentPane().add(mainScrollPane, BorderLayout.CENTER);

        // Action listener for the manf button
        ActionListener manfBtnListener = new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                mouseClick = actionEvent.getSource();
                List<simgui.Component> components = new ArrayList<>();
                setVisible(false);
                String json = readFile("");
                System.out.println("The file contained: \n" + json);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(json);
                } catch (Exception e) {
                    System.err.println("Failed to convert the JSON to a JSONObject.");
                    e.printStackTrace();
                    jsonObject = new JSONObject();
                }

                JSONArray jsonArray;
                try {
                    jsonArray = jsonObject.getJSONArray("components");
                } catch (Exception e) {
                    System.err.println("Failed to get the JSONArray with key \"components\" from the JSONObject");
                    e.printStackTrace();
                    jsonArray = new JSONArray();
                }

                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    simgui.Component component;

                    try {
                        component = gson.fromJson(jsonArray.getJSONObject(i).toString(), simgui.Component.class);
                    } catch (Exception e) {
                        System.err.println("Failed to convert the JSON at index " + i + " to a Component.");
                        e.printStackTrace();
                        component = new simgui.Component();
                    }

                    // Add the newly created components to our component list.
                    components.add(component);

                }

                new SimGUI(components);
            }
        };

        /*
         * Action listener for the open button. Creates a JFile Chooser dialog box and gets the file they user selects.
         */
        ActionListener openBtnListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser openFileChooser = new JFileChooser();
                FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml"); // filter to only show xml files
                openFileChooser.setFileFilter(xmlFilter);
                int returnValue = openFileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    openFile = openFileChooser.getSelectedFile();

                    currentConfig = readXML(openFile);

                    populateTextFieldsUponFileOpen(currentConfig);
                }
            }
        };

        // TODO: put this back the way it was using saveXML
        //action listener for save button.
        ActionListener saveBtnListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                System.out.println("\nConfiguration: ");

                JFileChooser saveFileChooser = new JFileChooser();
                int returnValue = saveFileChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    saveFile = saveFileChooser.getSelectedFile();
                    String fname = saveFile.getAbsolutePath();

                    try {

                        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                        // root elements
                        Document doc = docBuilder.newDocument();
                        Element rootElement = doc.createElement("component");
                        doc.appendChild(rootElement);

                        for (int i = 0; i < components.size(); i++) {
                            // component elements

                            Element component = doc.createElement(components.get(i).toString().replaceAll("\\s",""));
                            component.appendChild(doc.createTextNode(fields.get(i).getText()));
                            rootElement.appendChild(component);
                        }

                        // write the content into xml file
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult(new File(fname));

                        transformer.transform(source, result);

                        System.out.println("File saved!");

                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (TransformerConfigurationException e) {
                        e.printStackTrace();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //Action listener for start button.
        ActionListener startBtnListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                startSimulation();
            }
        };

        ActionListener outputBtnListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser saveOutputFileChooser = new JFileChooser();
                saveOutputFileChooser.setDialogTitle("Save Output");
                int returnValue = saveOutputFileChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    outputFile = saveOutputFileChooser.getSelectedFile();
                    String fname = outputFile.getAbsolutePath();

                    if (!fname.endsWith(".txt")) { // if saved file doesn't end with .txt extention, add the extension
                        outputFile = new File(fname + ".txt");
                    }
                    try {
                        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(simulationOutput.getText());
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        openBtn.addActionListener(openBtnListener);
        saveBtn.addActionListener(saveBtnListener);
        startBtn.addActionListener(startBtnListener);
        outputBtn.addActionListener(outputBtnListener);
        manfBtn.addActionListener(manfBtnListener);

        topPanel.add(openBtn);
        topPanel.add(saveBtn);
        topPanel.add(startBtn);
        topPanel.add(outputBtn);
        topPanel.add(manfBtn);

        bottomLabelPanel.add(outputLabel);
        outputLabel.setLabelFor(simulationOutput);

        simulationOutput.setEditable(false);

        bottomScrollPane = new JScrollPane(simulationOutput);
        bottomPanel.add(bottomScrollPane, BorderLayout.CENTER);
        bottomPanel.add(bottomLabelPanel, BorderLayout.WEST);

        pack();
        setVisible(true);
    }

    public static void main(String args[]) {
        new SimGUI(null);
    }

    public static Simulationrun readXML(File inFile) {
        try {

            // create new jaxb context
            JAXBContext jaxbContext = JAXBContext.newInstance(Simulationrun.class);

            // create new Unmarshaller  to convert xml to java object
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // create configuration object from unmarshaller
            Simulationrun readConfiguration = (Simulationrun) jaxbUnmarshaller.unmarshal(inFile);

            return readConfiguration;

        } catch (JAXBException e) { // catch if input
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a string containing the contents of the specified file.
     *
     * @param strFile The location of the file that will have its contents returned.
     * @return A string containing the contents of the specified file.
     */
    private static String readFile(String strFile) {
        try {
            JFileChooser openFileChooser = new JFileChooser();
            FileNameExtensionFilter mfstFilter = new FileNameExtensionFilter("manf files (*.manf)", "manf"); // filter to only show manifest files
            openFileChooser.setFileFilter(mfstFilter);
            int returnValue = openFileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                openFile = openFileChooser.getSelectedFile();
                strFile = openFile.getAbsolutePath();
            }
            strFile = new Scanner(new File(strFile)).useDelimiter("\\Z").next();
            return strFile;
        } catch (Exception e) {
            System.err.println("Failed to open the file. Make sure that mJsonFileLoc is pointing to the right location!.");
            e.printStackTrace();
            return "";
        }
    }

    // TODO: Make this work with our dynamic fields
    public void populateTextFieldsUponFileOpen(Simulationrun currentConfiguration) {

        fields.get(0).setText(currentConfiguration.getruntitle());

        fields.get(1).setText(currentConfiguration.getsimtype());

        fields.get(2).setText(currentConfiguration.getnumusers());

        fields.get(3).setText(currentConfiguration.getwanroundtripms());

        fields.get(4).setText(currentConfiguration.getrequestmsgbytes());

        fields.get(5).setText(currentConfiguration.getresponsemsgbytes());

        fields.get(6).setText(currentConfiguration.getthinksecs());

        fields.get(7).setText(currentConfiguration.getreqwansecs());
        fields.get(8).setText(currentConfiguration.getreqwanquesecs());

        fields.get(9).setText(currentConfiguration.getreqlbsecs());
        fields.get(10).setText(currentConfiguration.getreqlbquesecs());

        fields.get(11).setText(currentConfiguration.getreqwebsecs());
        fields.get(12).setText(currentConfiguration.getreqwebquesecs());

        fields.get(13).setText(currentConfiguration.getreqmidsecs());
        fields.get(14).setText(currentConfiguration.getreqmidquesecs());

        fields.get(15).setText(currentConfiguration.getreqappsecs());
        fields.get(16).setText(currentConfiguration.getreqappquesecs());

        fields.get(17).setText(currentConfiguration.getreqdbsecs());
        fields.get(18).setText(currentConfiguration.getreqdbquesecs());

        fields.get(19).setText(currentConfiguration.getrspappsecs());
        fields.get(20).setText(currentConfiguration.getrspappquesecs());

        fields.get(21).setText(currentConfiguration.getrspmidsecs());
        fields.get(22).setText(currentConfiguration.getrspmidquesecs());

        fields.get(23).setText(currentConfiguration.getrspwebsecs());
        fields.get(24).setText(currentConfiguration.getrspwebquesecs());

        fields.get(25).setText(currentConfiguration.getrsplbsecs());
        fields.get(26).setText(currentConfiguration.getrsplbquesecs());
    }

    public int saveXML(File saveFile) {
        int flag = createConfiguration();
        try {
            if (flag == 0) {
                // create new jaxb context
                JAXBContext jaxbContext = JAXBContext.newInstance(Simulationrun.class);

                // create new marshaller to convert java object into a xml file
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                // formatted output
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                jaxbMarshaller.marshal(currentConfig, saveFile);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return flag;
    }


    public int createConfiguration() {
        int flag = 0;
        int result = validateFields();
        if (result == 0) { // valid configuration
            String[] values = new String[fields.size()];

            for (int i = 0; i < values.length; i++) {
                values[i] = fields.get(i).getText();
            }

            if (customConfig) {
                currentConfig = new Simulationrun(fields, requestServiceFieldMap, requestQueueFieldMap, responseServiceFieldMap, responseQueueFieldMap);
            } else {
                currentConfig = new Simulationrun(values);
            }
        } else {
            flag = 1;
            switch (result) {
                case -1:
                    JOptionPane.showMessageDialog(null, "All fields must be filled", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 101:
                    JOptionPane.showMessageDialog(null, "Type of simulation must be 'finite' or 'infinite'", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 102:
                    JOptionPane.showMessageDialog(null, "Number of Users must be a whole number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 103:
                    JOptionPane.showMessageDialog(null, "Round trip MS must be a whole number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 104:
                    JOptionPane.showMessageDialog(null, "Request Message Bytes must be a whole number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 105:
                    JOptionPane.showMessageDialog(null, "Response Message Bytes must be a decimal number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 106:
                    JOptionPane.showMessageDialog(null, "Think seconds must be a decimal number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 202:
                    JOptionPane.showMessageDialog(null, "Number of Users must be a positive number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 203:
                    JOptionPane.showMessageDialog(null, "WAN Roundtrip MS must be a positive number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 204:
                    JOptionPane.showMessageDialog(null, "Request Message Bytes must be a positive number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 205:
                    JOptionPane.showMessageDialog(null, "Response Message Bytes must be a positive number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 206:
                    JOptionPane.showMessageDialog(null, "Think seconds must be a positive number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                case 999:
                    JOptionPane.showMessageDialog(null, "Service and Queue Response Times must be a positive number", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Error in text fields", "Error " + result, JOptionPane.WARNING_MESSAGE);
                    break;
            }
            ;
        }
        return flag;
    }

    /*
     * Error codes:
     * 0: Success
     * -1: There is an empty field
     * 101: Simulation type is not "infinite" or "finite"
     *
     * * X denotes a position 02 through 26
     * 1XX: Entered value is not a number
     * 2XX: Value is negative
     * 3XX: Value of service time is not between 0.0001 and 10 inclusive
     */
    public int validateFields() {
        int error = 0;
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).getText().equals("")) { // empty field
                error = -1;
                System.out.println("empty field");
                return error;
            }
        }

        if (!fields.get(1).getText().equals("infinite") && !fields.get(1).getText().equals("finite")) { // simulation type is not infinite or finite
            error = 101;
            System.out.println("Simulation Type not finite or infinite");
            return error;
        }

        for (int i = 2; i <= 5; i++) {
            int temp = 0;
            try {
                temp = Integer.parseInt(fields.get(i).getText());
            } catch (Exception e) { // not a number
                error = 100 + i;
                System.out.println("not a number");
                return error;
            }
            if (temp < 0) {
                error = 200 + i; // negative value
                System.out.println("negative value");
                return error;
            }
        }

        for (int i = 5; i <= 6; i++) {
            double temp = 0;
            try {
                temp = Double.parseDouble(fields.get(i).getText());
            } catch (Exception e) { // not a number
                error = 100 + i;
                System.out.println("trouble in fields [5] and [6]");
                return error;
            }
            if (temp < 0) { // negative value
                error = 200 + i;
                System.out.println("negative on this line");
                return error;
            }
            if ((i % 2) != 0) { // if i is odd
                if ((temp < 0.0001) || (temp > 10)) { //  service time is not between 0.0001 and 10 inclusive
                    error = 300 + i;
                    System.out.println("odd or service time is not between 0.0001 and 10 inclusive ");
                    return error;
                }
            }
        }

        for(int i = 7; i<fields.size(); i++)
        {
            int temp = 0;
            try {
                temp = Integer.parseInt(fields.get(i).getText());
            } catch (Exception e) { // not a number
                error = 999;
                System.out.println("not a number");
                return error;
            }
            if (temp < 0) {
                error = 999; // negative value
                System.out.println("negative value");
                return error;
            }
        }
        return error;
    }

    //Simulation Output
    public void startSimulation() {
        String jarDir = "";
        File tempFile = new File(System.getProperty("java.io.tmpdir"), "temp");

        String results = "";
        String errors = "";
        int flag;

        flag = saveXML(tempFile);
        if (flag == 0) { // no errors during save - ie all entries are valid
            try {
                Runtime r = Runtime.getRuntime();
                Process p = r.exec("python " + SIMULATION_FILE_NAME + " " + tempFile.getPath());

                BufferedReader resultReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                p.waitFor();

                String s;
                while ((s = resultReader.readLine()) != null) {
                    results += s + "\n";
                }
                while ((s = errorReader.readLine()) != null) {
                    errors += s + "\n";
                }
                if (results == "") { // error executing simulation
                    simulationOutput.setText(errors);
                } else {
                    simulationOutput.setText(results);
                }

                tempFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}