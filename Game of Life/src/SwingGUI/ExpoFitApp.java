package SwingGUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;

public class ExpoFitApp extends JFrame {
private static final long serialVersionUID = -6167569334213042018L;
private final int WIDTH = 512;
private final int HEIGHT = 600;
private GraphicPanelInner graphicsPanel;
private ControlPanelInner controlPanel;
public ExpoFitApp() {
super();
this.setTitle("Exponential Fit");
this.setSize(WIDTH, HEIGHT);
this.setLocationRelativeTo(null);
this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLayout(new BorderLayout(5, 5));
graphicsPanel = new GraphicPanelInner();
this.add(graphicsPanel, BorderLayout.CENTER);
controlPanel = new ControlPanelInner(graphicsPanel);
this.add(controlPanel, BorderLayout.WEST);
this.setResizable(false);
graphicsPanel.repaint();
this.setVisible(true);
graphicsPanel.setFocusable(true);
graphicsPanel.requestFocus();
}
public static void main(String[] args) {
new ExpoFitApp();
System.out.println("Main thread terminating");
}
public class GraphicPanelInner extends JPanel {
private static final long serialVersionUID = 7056793999538384084L;
private ArrayList<Double> xData;
private ArrayList<Double> yData;
private double coefficientA;
private double coefficientB;
public GraphicPanelInner() {
super();
this.setBackground(Color.WHITE);
this.xData = new ArrayList<>();
this.yData = new ArrayList<>();
this.coefficientA = 1.0;
this.coefficientB = 0.1;
this.addMouseMotionListener(new MouseMotionAdapter() {
});
}
public void updateData(ArrayList<Double> xData, ArrayList<Double> yData,
double A, double B) {
this.xData = xData;
this.yData = yData;
this.coefficientA = A;
this.coefficientB = B;
repaint();
}
@Override
protected void paintComponent(Graphics g) {
super.paintComponent(g);
if (xData.isEmpty() || yData.isEmpty()) {
return;
}
Graphics2D g2d = (Graphics2D) g;
int width = getWidth();
int height = getHeight();
int padding = 20;
double xMin = xData.stream().min(Double::compare).orElse(0.0) -
padding;
double xMax = xData.stream().max(Double::compare).orElse(1.0) +
padding;
double yMin = yData.stream().min(Double::compare).orElse(0.0) -
padding;
double yMax = yData.stream().max(Double::compare).orElse(1.0) +
padding;
double xScale = (width - 2 * padding) / (xMax - xMin);
double yScale = (height - 2 * padding) / (yMax - yMin);
g2d.setColor(Color.LIGHT_GRAY);
for (int i = 0; i <= 10; i++) {
int x = padding + (int) (i * (width - 2 * padding) / 10.0);
g2d.drawLine(x, padding, x, height - padding); // Vertical lines
int y = height - padding - (int) (i * (height - 2 * padding) /
10.0);
g2d.drawLine(padding, y, width - padding, y); // Horizontal lines
}
g2d.setColor(Color.BLUE);
for (int i = 0; i < xData.size() - 1; i++) {
double x1 = xData.get(i);
double y1 = yData.get(i);
double x2 = xData.get(i + 1);
double y2 = yData.get(i + 1);
int plotX1 = (int) ((x1 - xMin) * xScale) + padding;
int plotY1 = height - (int) ((y1 - yMin) * yScale) - padding;
int plotX2 = (int) ((x2 - xMin) * xScale) + padding;
int plotY2 = height - (int) ((y2 - yMin) * yScale) - padding;
g2d.drawLine(plotX1, plotY1, plotX2, plotY2);
}
}
@Override
	public Dimension getPreferredSize() {
		return new Dimension(50, 50);
	}	
}
public class ControlPanelInner extends JPanel {
	private static final long serialVersionUID = -8776438726683578403L;
	private JButton loadButton;
	private JButton calculateButton;
	private JTextField fileNameField;
	private JTextField coefficientAField;
	private JTextField coefficientBField;
	private JTextArea dataPointsArea;
	private JScrollPane scrollableTextArea;
	private GraphicPanelInner graphicsPanel;
	private ArrayList<Double> xData;
	private ArrayList<Double> yData;
	public ControlPanelInner(GraphicPanelInner graphicsPanel) {
this.graphicsPanel = graphicsPanel;
this.xData = new ArrayList<>();
this.yData = new ArrayList<>();
prepareComponents();
setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
addComponents();
setPreferredSize(new Dimension(200, 600));
}
private void prepareComponents() {
	loadButton = new JButton("Load");
	loadButton.addActionListener(e -> loadFile());
	calculateButton = new JButton("Calculate");
	calculateButton.addActionListener(e -> calculateExponentialFit());
	fileNameField = new JTextField(7);
	fileNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE,
	fileNameField.getPreferredSize().height));
	coefficientAField = new JTextField(7);
	coefficientAField.setMaximumSize(new Dimension(Integer.MAX_VALUE,
	coefficientAField.getPreferredSize().height));
	coefficientBField = new JTextField(7);
	coefficientBField.setMaximumSize(new Dimension(Integer.MAX_VALUE,
	coefficientBField.getPreferredSize().height));
	dataPointsArea = new JTextArea(5, 15);
	dataPointsArea.setLineWrap(true);
	dataPointsArea.setWrapStyleWord(true);
	scrollableTextArea = new JScrollPane(dataPointsArea);
	scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
	);
	scrollableTextArea.setPreferredSize(new Dimension(200, 150));
}
private void addComponents() {
this.add(loadButton);
this.add(fileNameField);
this.add(calculateButton);
this.add(new JLabel("Coefficient A:"));
this.add(coefficientAField);
this.add(new JLabel("Coefficient B:"));
this.add(coefficientBField);
this.add(scrollableTextArea);
}
private void loadFile() {
JFileChooser fileChooser = new JFileChooser();
if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
String fileName = fileChooser.getSelectedFile().getAbsolutePath();
fileNameField.setText(fileName);
loadData(fileName);
}
}
private void loadData(String fileName) {
xData.clear();
yData.clear();
dataPointsArea.setText("");
try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
{
String line;
while ((line = br.readLine()) != null) {
String[] parts = line.split(",");
if (parts.length == 2) {
xData.add(Double.parseDouble(parts[0].trim()));
yData.add(Double.parseDouble(parts[1].trim()));
dataPointsArea.append(parts[0].trim() + "," +
parts[1].trim() + "\n");
}
}
graphicsPanel.updateData(xData, yData, 1.0, 0.1);
} catch (IOException | NumberFormatException e) {
JOptionPane.showMessageDialog(this, "Error loading data: " +
e.getMessage());
}
}
private void calculateExponentialFit() {
exponentialFit fitCalculator = new exponentialFit();
double[] coefficients = fitCalculator.calculate(xData, yData);
DecimalFormat df = new DecimalFormat("#.#####");
coefficientAField.setText(df.format(coefficients[0]));
coefficientBField.setText(df.format(coefficients[1]));
graphicsPanel.updateData(xData, yData, coefficients[0],
coefficients[1]);
}
@Override
public Dimension getPreferredSize() {
return new Dimension(100, 500); // Adjust this as needed
}
}
}