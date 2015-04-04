/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class SimGUI extends JFrame {

    final String SIMULATION_FILE_NAME = "SimCmdv10.py";
    JTextField[] fields;
    File openFile;
    File saveFile;
    File outputFile;
    Simulationrun currentConfiguration;
    JTextArea simulationOutput;
    JPanel mainPanel;
    JPanel bottomPanel;
    double widthPercent = 0.33;
    double heightPercent = 0.75;
    
    //Make Component Class

    public SimGUI(String[] labels, int[] fieldWidths) {
        super("Simulation Configuration"); // title of window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        currentConfiguration = new Simulationrun();

        JScrollPane mainScrollPane;
        JScrollPane bottomScrollPane;
        mainPanel = new JPanel(new BorderLayout()); // panel to hold text fields and their labels
        JPanel labelPanel = new JPanel(new GridLayout(labels.length, 1)); // panel to hold text field labels
        JPanel fieldPanel = new JPanel(new GridLayout(fieldWidths.length, 1)); // panel to hold text fields
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // panel to hold buttons
        bottomPanel = new JPanel(new BorderLayout()); // panel to hold simulation results
        JPanel bottomLabelPanel = new JPanel(new GridLayout(1, 1)); // panel for simulation results label

        JButton openBtn = new JButton("Open Configuration"); // button to open an XML file
        JButton saveBtn = new JButton("Save Configuration"); // button to save an XML file
        JButton startBtn = new JButton("Start"); // button to start the simulation
        JButton outputBtn = new JButton("Save Output"); // button to save simulation results

        simulationOutput = new JTextArea(15, 25); // text area to hold simulation results
        JLabel outputLabel = new JLabel("Simulation Output: ", JLabel.LEFT); // label for simulation results

        mainPanel.add(labelPanel, BorderLayout.WEST);
        mainPanel.add(fieldPanel, BorderLayout.CENTER);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        fields = new JTextField[fieldWidths.length];

        /*
         * Creating the text fields, setting their width, creating their label, adding label to the label panel, adding text field to text field panel.
         */
        for (int i = 0; i < labels.length; i++) {
            fields[i] = new JTextField();
            fields[i].setColumns(fieldWidths[i]);

            JLabel lab = new JLabel(labels[i], JLabel.LEFT);
            lab.setLabelFor(fields[i]);

            labelPanel.add(lab);

            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.add(fields[i]);
            fieldPanel.add(p);
        }

        mainScrollPane = new JScrollPane(mainPanel); // scroll pane to hold text fields and their labels.
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16); // increase scroll speed
        getContentPane().add(mainScrollPane, BorderLayout.CENTER);

        /*
         * Action listener for the open button. Creates a JFile Chooser dialog box and gets the file they user selects.
         */
        ActionListener openBtnListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser openFileChooser = new JFileChooser();
                FileNameExtensionFilter mfstFilter = new FileNameExtensionFilter("mfst files (*.mfst)", "mfst"); // filter to only show manifest files
                openFileChooser.setFileFilter(mfstFilter);
                int returnValue = openFileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    openFile = openFileChooser.getSelectedFile();

                    currentConfiguration = readMFST(openFile);

                    populateTextFieldsUponFileOpen(currentConfiguration);
                }
            }
        };

        ActionListener saveBtnListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser saveFileChooser = new JFileChooser();
                int returnValue = saveFileChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    saveFile = saveFileChooser.getSelectedFile();
                    String fname = saveFile.getAbsolutePath();

                    if (!fname.endsWith(".xml")) { // if saved file doesn't end with .xml extention, add the extension
                        saveFile = new File(fname + ".xml");
                    }
                    saveXML(saveFile);
                }
            }
        };

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

        topPanel.add(openBtn);
        topPanel.add(saveBtn);
        topPanel.add(startBtn);
        topPanel.add(outputBtn);

        bottomLabelPanel.add(outputLabel);
        outputLabel.setLabelFor(simulationOutput);

        simulationOutput.setEditable(false);

        bottomScrollPane = new JScrollPane(simulationOutput);
        bottomPanel.add(bottomScrollPane, BorderLayout.CENTER);
        bottomPanel.add(bottomLabelPanel, BorderLayout.WEST);

        pack();
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //setSize( (int) (screenSize.width * widthPercent), (int) (screenSize.height * heightPercent));
        setVisible(true);
    }

    public static void main(String args[]) {

        String[] labels = {"Simulation Name",
            "Simulation Type",
            "Number of Users",
            "WAN Roundtrip MS",
            "Request Message Bytes",
            "Response Message Bytes",
            "Think Seconds"};
        
        int[] fieldWidths = {10, // Simulation Name
            10, // Simulation Type
            10, // Number of Users
            10, // WAN Roundtrip MS
            10, // Request Message Bytes
            10, // Response Message Bytes
            10, // Think Seconds
        };

        new SimGUI(labels, fieldWidths);
    }

    /*
     * Name: readXML
     * Input: File
     * Output: Simulationrun
     * Function: Creates a new JAXB object, reads in the XML file, and then converts the XML into a Simulationrun object
     */
    public static Simulationrun readMFST(File inFile) {
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

    /*
     * Name: populateTextFiledUponFileOpen
     * Input: Simulationrun
     * Output: void
     * Function: When the user opens an XML file, after the Simulationrun object is created, the text fields are then populated with the values from the XML file.
     */
    public void populateTextFieldsUponFileOpen(Simulationrun currentConfiguration) {

        fields[0].setText(currentConfiguration.getruntitle());

        fields[1].setText(currentConfiguration.getsimtype());

        fields[2].setText(currentConfiguration.getnumusers());

        fields[3].setText(currentConfiguration.getwanroundtripms());


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

                jaxbMarshaller.marshal(currentConfiguration, saveFile);
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
            String[] values = new String[fields.length];

            for (int i = 0; i < values.length; i++) {
                values[i] = fields[i].getText();
            }

            currentConfiguration = new Simulationrun(values);
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
            };
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
                return error;
            }
        }

        if (!fields[1].getText().equals("infinite") && !fields[1].getText().equals("finite")) { // simulation type is not infinite or finite
            error = 101;
            return error;
        }

        for (int i = 2; i <= 5; i++) {
            int temp = 0;
            try {
                temp = Integer.parseInt(fields[i].getText());
            } catch (Exception e) { // not a number
                error = 100 + i;
                return error;
            }
            if (temp < 0) {
                error = 200 + i; // negative value
                return error;
            }
        }

        for (int i = 5; i <= 6; i++) {
            double temp = 0;
            try {
                temp = Double.parseDouble(fields[i].getText());
            } catch (Exception e) { // not a number
                error = 100 + i;
                return error;
            }
            if (temp < 0) { // negative value
                error = 200 + i;
                return error;
            }
            if ((i % 2) != 0) { // if i is odd 
                if ((temp < 0.0001) || (temp > 10)) { //  service time is not between 0.0001 and 10 inclusive
                    error = 300 + i;
                    return error;
                }
            }
        }
        System.out.println("Error here");
        return error;
    }

    public void startSimulation() {
        String jarDir = "";
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = null;

        String results = "";
        String errors = "";
        int flag;

        tempFile = new File(tempDir + "\\temp.xml");

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