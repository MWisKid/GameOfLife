package mvc;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MVC extends JFrame {

    private static final long serialVersionUID = -6167569334213042018L;
    private final int WIDTH = 512;
    private final int HEIGHT = 600;
    private GraphicPanelInner graphicsPanel;
    private ControlPanelInner controlPanel;
    private BottomControlPanel bottomControlPanel;
    private ArrayRunnable arrayRunnable;
    
    public MVC() {
        super();
        this.setTitle("Animation");
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        
        arrayRunnable = new ArrayRunnable(1, 1, graphicsPanel);

        graphicsPanel = new GraphicPanelInner();
        this.add(graphicsPanel, BorderLayout.CENTER);

        controlPanel = new ControlPanelInner(graphicsPanel, arrayRunnable);
        this.add(controlPanel, BorderLayout.WEST);

        bottomControlPanel = new BottomControlPanel();
        this.add(bottomControlPanel, BorderLayout.SOUTH);

        this.setResizable(false);
        graphicsPanel.repaint();
        this.setVisible(true);
        graphicsPanel.setFocusable(true);
        graphicsPanel.requestFocusInWindow();
        
    }
    
    private void loadFromFile(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            ArrayRunnable loadedRunnable = (ArrayRunnable) in.readObject();
            System.out.println("Loaded ArrayRunnable: " + loadedRunnable);
            loadedRunnable.printArray();  
            arrayRunnable = loadedRunnable;  
            graphicsPanel.setRowsColumns(arrayRunnable.getRows(), arrayRunnable.getColumns());
            graphicsPanel.revalidate();  
            graphicsPanel.repaint();  
    
            JOptionPane.showMessageDialog(this, "Data loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new MVC();
        System.out.println("Main thread terminating");
    }

    public class GraphicPanelInner extends JPanel implements MouseMotionListener {
        private static final long serialVersionUID = 7056793999538384084L;
        
        private int rows = 1;  
        private int columns = 1;  
        private boolean linesOn = false; 
        private boolean drawCell = false;

        public GraphicPanelInner() {
            super();
            this.setBackground(Color.YELLOW);
            prepareActionHandlers();
            this.addMouseMotionListener(this);
        }
        
        public void setDrawCell(boolean draw) {
        	this.drawCell = draw;
        }
        
        public void setRowsColumns(int rows, int cols) {
            this.rows = rows;
            this.columns = cols;
            System.out.println("rows: " + rows);
            System.out.println("columns: " + cols);
            revalidate();  
            repaint();  
            }

        public void updateData(ArrayList<Double> xData, ArrayList<Double> yData, double A, double B) {
            repaint(); 
        }
        public void setBackgroundColor(Color color) {
            this.setBackground(color);
            repaint(); 
        }

        public void setLinesOn(boolean visible) {
            this.linesOn = visible;
            repaint(); 
        }

        @Override
        protected void paintComponent(Graphics g) {            
            super.paintComponent(g);  

            int panelWidth = getWidth();
            int panelHeight = getHeight();

            int cellWidth = panelWidth / columns;
            int cellHeight = panelHeight / rows;

            if (linesOn) {
                g.setColor(Color.BLACK);  
                for (int i = 0; i <= rows; i++) {
                    int y = i * cellHeight;
                    g.drawLine(0, y, panelWidth, y);  
                }
                for (int i = 0; i <= columns; i++) {
                    int x = i * cellWidth;
                    g.drawLine(x, 0, x, panelHeight);  
                }
            }

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    int cellValue = arrayRunnable.getArrayCell(row, col);

                    if (cellValue == 1) {
                        g.setColor(Color.RED);  
                        int x = col * cellWidth;
                        int y = row * cellHeight;

                        g.fillOval(x, y ,cellWidth, cellHeight); 
                    }
                }
            }
        }
        
	    private void prepareActionHandlers() {
	        this.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mousePressed(MouseEvent e) {
	            	int x = e.getX();
	                int y = e.getY();
	                System.out.println("Mouse Pressed at (" + x + ", " + y + ")");
	                
	                int cellWidth = getWidth() / columns;
	                int cellHeight = getHeight() / rows;
	                int col = x / cellWidth;
	                int row = y / cellHeight;

	                System.out.println("Mapped to grid cell (" + row + ", " + col + ")");
	                
	                if (drawCell) {
	                    arrayRunnable.updateCell(row, col, 1);  
	                } else {
	                    arrayRunnable.updateCell(row, col, 0);  
	                }
	                
	                repaint();
	            }

	            @Override
	            public void mouseReleased(MouseEvent e) {
	                System.out.println("Mouse Released at (" + e.getX() + ", " + e.getY() + ")");
	                graphicsPanel.requestFocusInWindow();
	            }

	            @Override
	            public void mouseClicked(MouseEvent e) {
	                int cellWidth = getWidth() / columns;
	                int cellHeight = getHeight() / rows;

	                int col = e.getX() / cellWidth;
	                int row = e.getY() / cellHeight;

	                if (drawCell) {
	                    arrayRunnable.updateCell(row, col, 1);  
	                } else {
	                    arrayRunnable.updateCell(row, col, 0);  
	                }

	                repaint();
	            }
	        });

	        this.addMouseMotionListener(new MouseMotionAdapter() {
	            @Override
	            public void mouseDragged(MouseEvent event) {
	            	int x = event.getX();
	                int y = event.getY();
	                
	                int cellWidth = getWidth() / columns;
	                int cellHeight = getHeight() / rows;
	                int col = x / cellWidth;
	                int row = y / cellHeight;
	                
	                if (drawCell) {
	                    arrayRunnable.updateCell(row, col, 1); 
	                } else {
	                    arrayRunnable.updateCell(row, col, 0); 
	                }
	                
	                repaint();
	            }

	            @Override
	            public void mouseMoved(MouseEvent event) {
	                System.out.println("Mouse moved to (" + event.getX() + ", " + event.getY() + ")");
	            }
	        });
	    }
	    @Override
	    public Dimension getPreferredSize() {
	        return new Dimension(WIDTH - 150, HEIGHT - 200);  
	    }

		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
	        int y = e.getY();
	        
	        System.out.println("Mouse dragged at (" + x + ", " + y + ")");
	        
	        int cellWidth = getWidth() / columns;
	        int cellHeight = getHeight() / rows;
	        int col = x / cellWidth;
	        int row = y / cellHeight;
	        
	        System.out.println("Mapped to grid cell (" + row + ", " + col + ")");
	        
	        if (drawCell) {
	            arrayRunnable.updateCell(row, col, 1);  
	        } else {
	            arrayRunnable.updateCell(row, col, 0);  
	        }
	        repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
	        System.out.println("Mouse moved to (" + e.getX() + ", " + e.getY() + ")");
		}
    }

    public class ControlPanelInner extends JPanel  {

        private static final long serialVersionUID = -8776438726683578403L;
        private JLabel gridLabel;
        private JButton setSizeButton;
        private JTextField rowsField;
        private JTextField columnsField;
        private JCheckBox gridCheckBox;
        private JCheckBox drawCheckBox;
        private JCheckBox wrapCheckBox;
        private JButton loadButton;  
        private JButton saveButton;
        private GraphicPanelInner graphicsPanel;
        private ArrayRunnable arrayRunnable;

        public ControlPanelInner(GraphicPanelInner graphicsPanel, ArrayRunnable arrayRunnable) {
        	this.graphicsPanel = graphicsPanel;
            this.arrayRunnable = arrayRunnable;
            prepareComponents();
            setLayout(new FlowLayout(FlowLayout.CENTER)); 
            addComponents();
            setPreferredSize(new Dimension(100, 600)); 
        }

        private void prepareComponents() {
            gridLabel = new JLabel("Grid");
            gridLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            setSizeButton = new JButton("Set Size");
            setSizeButton.addActionListener(e -> {
                System.out.println("Set Size Button clicked.");
                setSizeAction();
            });

            rowsField = new JTextField(7);
            rowsField.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowsField.getPreferredSize().height));
            rowsField.addActionListener(e -> {
                System.out.println("Rows field updated: " + rowsField.getText());
            });
            
            columnsField = new JTextField(7);
            columnsField.setMaximumSize(new Dimension(Integer.MAX_VALUE, columnsField.getPreferredSize().height));
            columnsField.addActionListener(e -> {
                System.out.println("Columns field updated: " + columnsField.getText());
            });

            
            gridCheckBox = new JCheckBox("Grid on");
            gridCheckBox.addActionListener(e -> {
                gridOn();
            });


            drawCheckBox = new JCheckBox("Draw cell");
            drawCheckBox.addActionListener(e -> {
                boolean isSelected = drawCheckBox.isSelected();
                System.out.println("Draw checkbox changed: " + (isSelected ? "Checked" : "Unchecked"));
                graphicsPanel.setDrawCell(isSelected); 
            });
            
            wrapCheckBox = new JCheckBox("Wrap");
            wrapCheckBox.addActionListener(e -> {
                boolean isSelected = wrapCheckBox.isSelected();
                System.out.println("Wrap checkbox changed: " + (isSelected ? "Checked" : "Unchecked"));
                arrayRunnable.updateWrap(isSelected);
            });

            
            loadButton = new JButton("Load");
            loadButton.addActionListener(e -> {
                System.out.println("Load Button clicked.");
                loadFile();
            });

            saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                System.out.println("Save Button clicked.");
                saveFile();
            });
        }
        
        private void setSizeAction() {
        	try {
        		int rows = Integer.parseInt(rowsField.getText());
        		int cols = Integer.parseInt(columnsField.getText());
        		
        		graphicsPanel.setRowsColumns(rows, cols);
        		arrayRunnable.updateArraySize(rows, cols);
        		graphicsPanel.repaint();
        	}
        	catch (NumberFormatException e) {
        		System.out.println();
        	}
        }

        private void gridOn() {
            boolean isSelected = gridCheckBox.isSelected();
            System.out.println("Grid checkbox changed: " + (isSelected ? "Checked" : "Unchecked"));
            graphicsPanel.setLinesOn(isSelected);
            
        }
        
        private void addComponents() {
            this.add(gridLabel);
            this.add(setSizeButton);
            this.add(new JLabel("Rows"));
            this.add(rowsField);
            this.add(new JLabel("Columns"));
            this.add(columnsField);
            
            this.add(gridCheckBox);
            this.add(drawCheckBox);
            this.add(wrapCheckBox);

            this.add(loadButton);
            this.add(saveButton);
            
        }

        private void loadFile() {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("File selected: " + selectedFile.getAbsolutePath());
                ((MVC) SwingUtilities.getWindowAncestor(this)).loadFromFile(selectedFile);
            }
        }


        private void saveFile() {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".ser")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".ser");
                }
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(selectedFile))) {
                    out.writeObject(arrayRunnable);  
                    System.out.println("Saving to file: " + selectedFile.getAbsolutePath());
                    // Debug log to confirm the save operation
                    System.out.println("Saved ArrayRunnable: " + arrayRunnable);
                    arrayRunnable.printArray();  
                    JOptionPane.showMessageDialog(this, "Data saved successfully!");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
                }
            }
        }
        
        public JCheckBox getWrapCheckBox() {
            if (wrapCheckBox == null) {
                wrapCheckBox = new JCheckBox("Wrap");
            }
            return wrapCheckBox;
        }
    }
    
    public class BottomControlPanel extends JPanel {

        private static final long serialVersionUID = -8776438726683578403L;

        private JLabel animationLabel;  
        private JButton startStopButton; 
        private JSlider valueSlider; 
        private Thread modelThread;
        private boolean isRunning; 
        static int delay;

        public BottomControlPanel() {
            animationLabel = new JLabel("Animation");
            startStopButton = new JButton("Start");
            startStopButton.addActionListener(new StartStopButtonListener());
            valueSlider = new JSlider(0, 100, 50); 
            valueSlider.setMajorTickSpacing(20); 
            valueSlider.setMinorTickSpacing(5); 
            valueSlider.setPaintTicks(true); 
            valueSlider.setPaintLabels(true); 
            valueSlider.addChangeListener(new SliderChangeListener());
            valueSlider.setLabelTable(createSliderLabels());
            
            setLayout(new FlowLayout(FlowLayout.CENTER)); 
            this.add(animationLabel);
            this.add(startStopButton);
            this.add(valueSlider);
            delay = 2000 - (valueSlider.getValue() *20);
        }
        
        private Hashtable<Integer, JLabel> createSliderLabels() {
            Hashtable<Integer, JLabel> labels = new Hashtable<>();
            for (int i = 0; i <= 100; i += 20) {
                labels.put(i, new JLabel(String.valueOf(i)));
            }
            return labels;
        }

        private class StartStopButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                	stopModel();
                	startStopButton.setText("Start");
                } else {
                	startModel();
                    startStopButton.setText("Stop");
                }
            }
        }
        
        private void startModel() {
            if (modelThread == null || !modelThread.isAlive()) {
                arrayRunnable.graphicsPanel = graphicsPanel;
                modelThread = new Thread(arrayRunnable);
                modelThread.start();  
                isRunning = true;  
            }
        }

        private void stopModel() {
            isRunning = false;
            arrayRunnable.stop(isRunning);
        }

        private class SliderChangeListener implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
            	int speed = valueSlider.getValue();
                System.out.println("Slider value changed: " + speed);

                delay = 2000 - (speed * 20); 
                System.out.println("New delay: " + delay + "ms");
                }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(WIDTH, 100); 
        }
    }
}