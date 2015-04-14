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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// These require java-json (http://www.json.org/java/).
// This requires json (https://code.google.com/p/google-gson/).

public class SimGUI extends JFrame {

    final String SIMULATION_FILE_NAME = "SimCmdv10.py";
    JTextField[] fields;
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

    public SimGUI(List components, int[] fieldWidths, List cmpts) {
        super("Simulation Configuration"); // title of window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JScrollPane mainScrollPane;
        JScrollPane bottomScrollPane;
        mainPanel = new JPanel(new GridLayout()); // panel to hold text fields and their labels
        JPanel labelPanel = new JPanel(new GridLayout(components.size(), 1)); // panel to hold text field labels
        JPanel fieldPanel = new JPanel(new GridLayout(components.size(), 1)); // panel to hold text fields

        JPanel defaultLabel = new JPanel(new GridLayout(cmpts.size(), 1));
        JPanel defaultField = new JPanel(new GridLayout(cmpts.size(), 1));

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

        if(!cmpts.isEmpty()){
            mainPanel = new JPanel(new GridLayout(2, 1));
            mainPanel.add(defaultLabel, BorderLayout.WEST);
            mainPanel.add(defaultField, BorderLayout.CENTER);
        }

        mainPanel.add(labelPanel, BorderLayout.WEST);
        mainPanel.add(fieldPanel, BorderLayout.CENTER);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        fields = new JTextField[fieldWidths.length];

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
        ArrayList<String> timeSec = new ArrayList<>();

        timeSec.add("Request WAN Service Time Seconds");
        timeSec.add("Request WAN Queue Time Seconds");
        timeSec.add("Request Load Balance Service Time Seconds");
        timeSec.add("Request Load Balance Queue Time Seconds");
        timeSec.add("Request Web Service Time Seconds");
        timeSec.add("Request Web Queue Time Seconds");
        timeSec.add("Request MiddleWare Service Time Seconds");
        timeSec.add("Request MiddleWare Queue Time Seconds");
        timeSec.add("Request Application Service Time Seconds");
        timeSec.add("Request Application Queue Time Seconds");
        timeSec.add("Request Database Service Time Seconds");
        timeSec.add("Request Database Queue Time Seconds");
        timeSec.add("Response Application Service Time Seconds");
        timeSec.add("Response Application Queue Time Seconds");
        timeSec.add("Response MiddleWare Service Time Seconds");
        timeSec.add("Response Web Service Time Seconds");
        timeSec.add("Response Web Queue Time Seconds");
        timeSec.add("Response Load Balance Service Time Seconds");
        timeSec.add("Response Load Balance Queue Time Seconds");

        for (int i = 0; i < components.size(); i++) {
            fields[i] = new JTextField();
            fields[i].setColumns(components.size());
            // checks what the source of the mouse click was, i dont think this is working at all
            if(mouseClick == manfBtn){
                // This format should work they way I think it will.
                for(int j = 0; j < timeSec.size(); j++){
                    JLabel lab = new JLabel(timeSec.get(j) + " (" + components.get(i).toString() + ")", JLabel.LEFT);
                    lab.setLabelFor(fields[i]);
                    labelPanel.add(lab);

                    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    p.add(fields[i]);
                    fieldPanel.add(p);
                }
            }
            else {
                JLabel lab = new JLabel(components.get(i).toString(), JLabel.LEFT);
                lab.setLabelFor(fields[i]);
                labelPanel.add(lab);

                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
                p.add(fields[i]);
                fieldPanel.add(p);
            }
        }

        for (int i = 0; i < cmpts.size(); i++) {
            fields[i] = new JTextField();
            fields[i].setColumns(cmpts.size());
            JLabel lab = new JLabel(cmpts.get(i).toString(), JLabel.LEFT);
            lab.setLabelFor(fields[i]);
            defaultLabel.add(lab);

            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.add(fields[i]);
            defaultField.add(p);
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

                ArrayList<String> cmpts = new ArrayList<>();

                cmpts.add("Simulation Name");
                cmpts.add("Simulation Type");
                cmpts.add("Number of Users");
                cmpts.add("WAN Roundtrip MS");
                cmpts.add("Request Message Bytes");
                cmpts.add("Response Message Bytes");
                cmpts.add("Think Seconds");

                int[] fieldWidths = new int[components.size()+cmpts.size()];
                for (int i = 0; i < components.size(); i++) {
                    fieldWidths[i] = 20;
                }

                new SimGUI(components, fieldWidths, cmpts);
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
                            component.appendChild(doc.createTextNode(fields[i].getText()));
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

        ArrayList<String> cpts = new ArrayList<>();
        ArrayList<String> cmpts = new ArrayList<>();

        cpts.add("Simulation Name");
        cpts.add("Simulation Type");
        cpts.add("Number of Users");
        cpts.add("WAN Roundtrip MS");
        cpts.add("Request Message Bytes");
        cpts.add("Response Message Bytes");
        cpts.add("Think Seconds");
        cpts.add("Request WAN Service Time Seconds");
        cpts.add("Request WAN Queue Time Seconds");
        cpts.add("Request Load Balance Service Time Seconds");
        cpts.add("Request Load Balance Queue Time Seconds");
        cpts.add("Request Web Service Time Seconds");
        cpts.add("Request Web Queue Time Seconds");
        cpts.add("Request MiddleWare Service Time Seconds");
        cpts.add("Request MiddleWare Queue Time Seconds");
        cpts.add("Request Application Service Time Seconds");
        cpts.add("Request Application Queue Time Seconds");
        cpts.add("Request Database Service Time Seconds");
        cpts.add("Request Database Queue Time Seconds");
        cpts.add("Response Application Service Time Seconds");
        cpts.add("Response Application Queue Time Seconds");
        cpts.add("Response MiddleWare Service Time Seconds");
        cpts.add("Response Web Service Time Seconds");
        cpts.add("Response Web Queue Time Seconds");
        cpts.add("Response Load Balance Service Time Seconds");
        cpts.add("Response Load Balance Queue Time Seconds");

        int[] fieldWidths = {20, // Simulation Name
                10, // Simulation Type
                10, // Number of Users
                10, // WAN Roundtrip MS
                10, // Request Message Bytes
                10, // Response Message Bytes
                10, // Think Seconds
                10, // Request WAN Service Time Seconds
                10, // Request WAN Queue Time Seconds
                10, // Request Load Balance Service Time Seconds
                10, // Request Load Balance Queue Time Seconds
                10, // Request Web Service Time Seconds
                10, // Request Web Queue Time Seconds
                10, // Request MiddleWare Service Time Seconds
                10, // Request MiddleWare Queue Time Seconds
                10, // Request Application Service Time Seconds
                10, // Request Application Queue Time Seconds
                10, // Request Database Service Time Seconds
                10, // Request Database Queue Time Seconds
                10, // Response Application Service Time Seconds
                10, // Response Application Queue Time Seconds
                10, // Response MiddleWare Service Time Seconds
                10, // Response MiddleWare Queue Time Seconds
                10, // Response Web Service Time Seconds
                10, // Response Web Queue Time Seconds
                10, // Response Load Balance Service Time Seconds
                10 // Response Load Balance Queue Time Second
        };

        new SimGUI(cpts, fieldWidths, cmpts);
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

    public void populateTextFieldsUponFileOpen(Simulationrun currentConfiguration) {

        fields[0].setText(currentConfiguration.getruntitle());

        fields[1].setText(currentConfiguration.getsimtype());

        fields[2].setText(currentConfiguration.getnumusers());

        fields[3].setText(currentConfiguration.getwanroundtripms());

        fields[4].setText(currentConfiguration.getrequestmsgbytes());

        fields[5].setText(currentConfiguration.getresponsemsgbytes());

        fields[6].setText(currentConfiguration.getthinksecs());

        fields[7].setText(currentConfiguration.getreqwansecs());
        fields[8].setText(currentConfiguration.getreqwanquesecs());

        fields[9].setText(currentConfiguration.getreqlbsecs());
        fields[10].setText(currentConfiguration.getreqlbquesecs());

        fields[11].setText(currentConfiguration.getreqwebsecs());
        fields[12].setText(currentConfiguration.getreqwebquesecs());

        fields[13].setText(currentConfiguration.getreqmidsecs());
        fields[14].setText(currentConfiguration.getreqmidquesecs());

        fields[15].setText(currentConfiguration.getreqappsecs());
        fields[16].setText(currentConfiguration.getreqappquesecs());

        fields[17].setText(currentConfiguration.getreqdbsecs());
        fields[18].setText(currentConfiguration.getreqdbquesecs());

        fields[19].setText(currentConfiguration.getrspappsecs());
        fields[20].setText(currentConfiguration.getrspappquesecs());

        fields[21].setText(currentConfiguration.getrspmidsecs());
        fields[22].setText(currentConfiguration.getrspmidquesecs());

        fields[23].setText(currentConfiguration.getrspwebsecs());
        fields[24].setText(currentConfiguration.getrspwebquesecs());

        fields[25].setText(currentConfiguration.getrsplbsecs());
        fields[26].setText(currentConfiguration.getrsplbquesecs());
    }

    public int saveXML(File tempFile) {
        int flag = createConfiguration();
        return flag;
    }

    public int createConfiguration() {
        int flag = 0;
        int result = validateFields();
        if (result == 0) { // valid configuration
            String[] values = new String[fields.length];

            for (int i = 0; i < values.length; i++) {
                values[i] = fields[i].getText();
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
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getText().equals("")) { // empty field
                error = -1;
                System.out.println("empty field");
                return error;
            }
        }

        if (!fields[1].getText().equals("infinite") && !fields[1].getText().equals("finite")) { // simulation type is not infinite or finite
            error = 101;
            System.out.println("Simulation Type not finite or infinite");
            return error;
        }

        for (int i = 2; i <= 5; i++) {
            int temp = 0;
            try {
                temp = Integer.parseInt(fields[i].getText());
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
                temp = Double.parseDouble(fields[i].getText());
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
        return error;
    }

    //Simulation Output
    public void startSimulation() {
        String jarDir = "";
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = null;

        String results = "";
        String errors = "";
        int flag;

        tempFile = new File(tempDir + "\\temp");

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