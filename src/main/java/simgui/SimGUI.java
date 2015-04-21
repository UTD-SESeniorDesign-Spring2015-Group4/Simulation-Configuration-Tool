package simgui;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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

//import java.awt.Color;
//import javax.swing.JTextField;
//import javax.swing.text.DefaultHighlighter;
//import javax.swing.text.Highlighter;

public class SimGUI extends JFrame {

    final String SIMULATION_FILE_NAME = "SimCmdv10.py";
    private JPanel labelPanel, fieldPanel;
    private JTextArea simulationOutput;
    private List<Field> fields;
	
	//final static Color ERROR_COLOR = Color.YELLOW;
	//public Highlighter hilit;
	//public Highlighter.HighlightPainter painter;

    public SimGUI() {
        // Set the window title.
        super("Simulation Configuration");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create global list of fields.
        fields = new ArrayList<Field>();

        // Input fields and their labels.
        JPanel mainPanel = new JPanel(new GridLayout(0, 2));
        labelPanel = new JPanel(new GridLayout(0, 1));
        fieldPanel = new JPanel(new GridLayout(0, 1));
        mainPanel.add(labelPanel, BorderLayout.WEST);
        mainPanel.add(fieldPanel, BorderLayout.CENTER);

        // Buttons.
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton openBtn = new JButton("Open Configuration");
        JButton saveBtn = new JButton("Save Configuration");
        JButton startBtn = new JButton("Start");
        JButton outputBtn = new JButton("Save Output");
        JButton manfBtn = new JButton("Open Manifest File");
        getContentPane().add(topPanel, BorderLayout.NORTH);
        topPanel.add(openBtn);
        topPanel.add(saveBtn);
        topPanel.add(startBtn);
        topPanel.add(outputBtn);
        topPanel.add(manfBtn);

        // Simulation results.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomLabelPanel = new JPanel(new GridLayout(1, 1));
        simulationOutput = new JTextArea(15, 25);
        JLabel outputLabel = new JLabel("Simulation Output: ", JLabel.LEFT);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        bottomLabelPanel.add(outputLabel);
        outputLabel.setLabelFor(simulationOutput);
        simulationOutput.setEditable(false);

        // Setup main window pane.
        JScrollPane mainScrollPane;
        mainScrollPane = new JScrollPane(mainPanel); // scroll pane to hold text fields and their labels.
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16); // increase scroll speed
        getContentPane().add(mainScrollPane, BorderLayout.CENTER);

        // Set up bottom window pane.
        JScrollPane bottomScrollPane;
        bottomScrollPane = new JScrollPane(simulationOutput);
        bottomPanel.add(bottomScrollPane, BorderLayout.CENTER);
        bottomPanel.add(bottomLabelPanel, BorderLayout.WEST);

        // Create static fields.
        setupStaticFields();

        // Create the default fields.
        addField(new Field(Field.FieldType.REQUEST_WAN_SERVICE_TIME));
        addField(new Field(Field.FieldType.REQUEST_WAN_QUEUE_TIME));
        addField(new Field(Field.FieldType.REQUEST_LOADBALANCER_SERVICE_TIME));
        addField(new Field(Field.FieldType.REQUEST_LOADBALANCER_QUEUE_TIME));
        addField(new Field(Field.FieldType.REQUEST_WEB_SERVICE_TIME));
        addField(new Field(Field.FieldType.REQUEST_WEB_QUEUE_TIME));
        addField(new Field(Field.FieldType.REQUEST_MIDDLEWARE_SERVICE_TIME));
        addField(new Field(Field.FieldType.REQUEST_MIDDLEWARE_QUEUE_TIME));
        addField(new Field(Field.FieldType.REQUEST_APPLICATION_SERVICE_TIME));
        addField(new Field(Field.FieldType.REQUEST_APPLICATION_QUEUE_TIME));
        addField(new Field(Field.FieldType.REQUEST_DATABASE_SERVICE_TIME));
        addField(new Field(Field.FieldType.REQUEST_DATABASE_QUEUE_TIME));
        addField(new Field(Field.FieldType.RESPONSE_APPLICATION_SERVICE_TIME));
        addField(new Field(Field.FieldType.RESPONSE_APPLICATION_QUEUE_TIME));
        addField(new Field(Field.FieldType.RESPONSE_MIDDLEWARE_SERVICE_TIME));
        addField(new Field(Field.FieldType.RESPONSE_MIDDLEWARE_QUEUE_TIME));
        addField(new Field(Field.FieldType.RESPONSE_WEB_SERVICE_TIME));
        addField(new Field(Field.FieldType.RESPONSE_WEB_QUEUE_TIME));
        addField(new Field(Field.FieldType.RESPONSE_LOADBALANCER_SERVICE_TIME));
        addField(new Field(Field.FieldType.RESPONSE_LOADBALANCER_QUEUE_TIME));

        // Set up button listeners.
        // Action listener for the Open Manifest File button.
        ActionListener manfBtnListener = new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                List<simgui.Component> components = new ArrayList<Component>();

                // Prompt the user for and read in a Topology Manifest file.
                try {
                    // Hide the main window.
                    setVisible(false);

                    // Display a file chooser.
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter mfstFilter = new FileNameExtensionFilter("manf files (*.manf)", "manf");
                    fileChooser.setFileFilter(mfstFilter);
                    int returnValue = fileChooser.showOpenDialog(null);

                    String fileContent;

                    // Check if the user actually selected a file.
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();

                        // Read in the user-selected file.
                        fileContent = new Scanner(file).useDelimiter("\\Z").next();
                    } else {

                        // The user did not select a file, show the application.
                        setVisible(true);
                        return;
                    }

                    // Make sure that the file had content.
                    if (!fileContent.isEmpty()) {

                        // Create a JSONObject from the json string.
                        JSONObject jsonObject = new JSONObject(fileContent);

                        // Get the JSONArray that contains the components.
                        JSONArray jsonArray = jsonObject.getJSONArray("components");

                        // Create component objects from the components in the JSON.
                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Component component = gson.fromJson(jsonArray.getJSONObject(i).toString(), Component.class);

                            // Add the newly created component to our components list.
                            components.add(component);
                        }

                        // Remove all the fields and add the new ones based on the components.
                        removeAllFields();
                        setupStaticFields();
                        setupComponentFields(components);

                        // Show the application
                        setVisible(true);
                    } else {
                        // The file had no content, show the application.
                        setVisible(true);
                        JOptionPane.showMessageDialog(null, "There were no components in the file.", "File Format Error", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    // Show the application and alert the user that there was a problem reading the file.
                    setVisible(true);
                    JOptionPane.showMessageDialog(null, "There was an error reading in the file.", "File Opening Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

         // Action listener for the open button. Creates a JFile Chooser dialog box and gets the file they user selects.
        ActionListener openBtnListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // Create ArrayList to store elements in.
                ArrayList<Element> elements = new ArrayList<Element>();

                // Display a file chooser.
                JFileChooser openFileChooser = new JFileChooser();
                FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml"); // filter to only show xml files
                openFileChooser.setFileFilter(xmlFilter);
                int returnValue = openFileChooser.showOpenDialog(null);

                // Check if user actually picked a file.
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        // Open file as an XML Document.
                        File openFile = openFileChooser.getSelectedFile();
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(openFile);
                        doc.getDocumentElement().normalize();

                        // Loop through the children nodes of the outer <components> tag.
                        NodeList nodelist = doc.getFirstChild().getChildNodes();
                        for (int i = 0; i < nodelist.getLength(); i++) {
                            // Check if node is an element (which represents the tag).
                            Node node = nodelist.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                elements.add((Element) node);
                            }
                        }

                        // Remove all fields and add new ones based on the xml configuration.
                        removeAllFields();
                        for(Element element : elements) {
                            addField(new Field(element));
                        }
                    } catch(IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "There was an error reading in the file.", "File Opening Error", JOptionPane.ERROR_MESSAGE);
                    } catch(SAXException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "There was an error parsing the file.", "File Parsing Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "I have no clue what this error means but it has something to do with XML parsing.", "File Parsing Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        };

        //action listener for save button.
        ActionListener saveBtnListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // Display a file chooser.
                JFileChooser saveFileChooser = new JFileChooser();
                FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml"); // filter to only show xml files
                saveFileChooser.setFileFilter(xmlFilter);
                int returnValue = saveFileChooser.showSaveDialog(null);

                // Check if user actually picked a file.
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File saveFile = saveFileChooser.getSelectedFile();
                    String fileStr = saveFile.getAbsolutePath();
                    if(!fileStr.endsWith(".xml"))
                        saveFile = new File(fileStr+".xml");
                    saveXML(saveFile, true);
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
                // Prompt user where to save simulation output.
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Output");
                // Add filter to pick only .txt files
                FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("txt file (*.txt)", "txt");
                fileChooser.setFileFilter(xmlFilter);
                int returnValue = fileChooser.showSaveDialog(null);

                // Check if the user actually selected a file.
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        // Write the simulation output to the specified file.
                        File outputFile = fileChooser.getSelectedFile();
                        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(simulationOutput.getText());
                        bw.close();
                    } catch (IOException e) {
                        // Some error happened while writing to file, show the user an error.
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "There was an error writing the simulation output to file.", "File Writing Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        // Add action listeners to their buttons.
        openBtn.addActionListener(openBtnListener);
        saveBtn.addActionListener(saveBtnListener);
        startBtn.addActionListener(startBtnListener);
        outputBtn.addActionListener(outputBtnListener);
        manfBtn.addActionListener(manfBtnListener);

        pack();
        setVisible(true);
    }

    public static void main(String args[]) {
        new SimGUI();
    }

    private void addField(Field field) {
        fields.add(field);
        field.addTo(labelPanel, fieldPanel);
    }

    private void removeAllFields() {
        fields.clear();
        labelPanel.removeAll();
        fieldPanel.removeAll();
        revalidate();
        repaint();
    }

    // Create static fields.
    private void setupStaticFields() {
        addField(new Field(Field.FieldType.SIMULATION_NAME));
        addField(new Field(Field.FieldType.SIMULATION_TYPE));
        addField(new Field(Field.FieldType.NUMBER_USERS));
        addField(new Field(Field.FieldType.WAN_ROUNDTRIP_MS));
        addField(new Field(Field.FieldType.REQUEST_MESSAGE_BYTES));
        addField(new Field(Field.FieldType.RESPONSE_MESSAGE_BYTES));
        addField(new Field(Field.FieldType.THINK_SECONDS));
    }

    private void setupComponentFields(List<Component> components) {
        for(Component component : components) {
            String type = component.getType();
            String name = component.getName();

            if (type.equals("ApplicationServer")) {
                addField(new Field(Field.FieldType.REQUEST_APPLICATION_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.REQUEST_APPLICATION_QUEUE_TIME, name));
                addField(new Field(Field.FieldType.RESPONSE_APPLICATION_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.RESPONSE_APPLICATION_QUEUE_TIME, name));
            }
            else if (type.equals("Client")) {
                //TODO: No behavior specified yet.
            }
            else if (type.equals("DatabaseServer")) {
                addField(new Field(Field.FieldType.REQUEST_DATABASE_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.REQUEST_DATABASE_QUEUE_TIME, name));
            }
            else if (type.equals("Loadbalancer")) {
                addField(new Field(Field.FieldType.REQUEST_LOADBALANCER_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.REQUEST_LOADBALANCER_QUEUE_TIME, name));
                addField(new Field(Field.FieldType.RESPONSE_LOADBALANCER_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.RESPONSE_LOADBALANCER_QUEUE_TIME, name));
            }
            else if (type.equals("MiddlewareServer")) {
                addField(new Field(Field.FieldType.REQUEST_MIDDLEWARE_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.REQUEST_MIDDLEWARE_QUEUE_TIME, name));
                addField(new Field(Field.FieldType.RESPONSE_MIDDLEWARE_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.RESPONSE_MIDDLEWARE_QUEUE_TIME, name));
            }
            else if (type.equals("Wan")) {
                addField(new Field(Field.FieldType.REQUEST_WAN_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.REQUEST_WAN_QUEUE_TIME, name));
            }
            else if (type.equals("WebfrontendServer")) {
                addField(new Field(Field.FieldType.REQUEST_WEB_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.REQUEST_WEB_QUEUE_TIME, name));
                addField(new Field(Field.FieldType.RESPONSE_WEB_SERVICE_TIME, name));
                addField(new Field(Field.FieldType.RESPONSE_WEB_QUEUE_TIME, name));
            }
        }
    }


    public boolean saveXML(File saveFile, boolean stripAttributes) {
        try {
            // Create an empty Document
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Create root <component> element
            Element rootElement = doc.createElement("component");
            doc.appendChild(rootElement);

            // Convert each field to XML and append to <component> element
            for (Field field : fields) {
                Element fieldElement = field.toXML(doc);
                if(stripAttributes) {
                    fieldElement.removeAttribute("name");
                }
                rootElement.appendChild(fieldElement);
            }

            // Write the Document into the file chosen by user.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(saveFile);
            transformer.transform(source, result);
            return true;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "I have no clue what this error means but it has something to do with XML parsing.", "File Parsing Error", JOptionPane.ERROR_MESSAGE);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "I have no clue what this error means but it has something to do with XML parsing.", "File Parsing Error", JOptionPane.ERROR_MESSAGE);
        } catch (TransformerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to write XML to file.", "File Writing Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }


    /**
     * Validates all fields.
     * @return true if all fields are valid, false if any field is invalid.
     */
    public boolean validateFields() {
        // Go through the fields and store any errors returned while validating.
        List<String> labels = new ArrayList<String>();
        List<String> errors = new ArrayList<String>();
		
		//hilit = new DefaultHighlighter();
		//painter = new DefaultHighlighter.DefaultHighlightPainter(ERROR_COLOR);
		
        for(Field field : fields) {
            String error = field.validate();
            if(!error.isEmpty()) {
                labels.add(field.getLabel());
                errors.add(error);
                //field.textField.setHighlighter(hilit);
            }
        }

        if(!errors.isEmpty()) {
            // Build the HTML error message to show to the user.
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<html><body><p>There were some errors in the following fields:</p><br/><table>");
            for(int k = 0; k < errors.size(); k++) {
                htmlBuilder.append(String.format("<tr><td>%s</td><td>%s</td></tr>", labels.get(k), errors.get(k)));
            }
            htmlBuilder.append("</table></body></html>");
            JOptionPane.showMessageDialog(null, htmlBuilder.toString(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }

        return errors.isEmpty();
    }

    //Simulation Output
    public void startSimulation() {
        // Get a temporary file.
        File tempFile = new File(System.getProperty("java.io.tmpdir"), "temp");

        String results = "";
        String errors = "";

        // Validate the fields and return if there were errors;
        boolean valid = validateFields();
        if(!valid) {
            return;
        }

        // Attempt to save the XML file to send to the simulation
        boolean success = saveXML(tempFile, true);
        if (success) { // no errors during save - ie all entries are valid
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
                if (results.isEmpty()) {
                    simulationOutput.setText(errors);
                } else {
                    simulationOutput.setText(results);
                }

                tempFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
                simulationOutput.setText(e.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
                simulationOutput.setText(e.toString());
            }
        }
    }
}