package mvc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.SwingUtilities;
import mvc.MVC.GraphicPanelInner;
import mvc.MVC.BottomControlPanel;

public class ArrayRunnable implements Runnable, Serializable {

    private static final long serialVersionUID = -8776438726683578403L;

    private int[][] array; 
    private int[][] newArray; 
    private boolean wrap;
    private boolean isRunning;
    public GraphicPanelInner graphicsPanel;  

    public ArrayRunnable(int rows, int columns, GraphicPanelInner graphicsPanel) {
        this.array = new int[rows][columns];
        this.newArray = new int[rows][columns];
        this.wrap = false;
        this.graphicsPanel = graphicsPanel;  
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();  

        // Debugging: Check if the array is correctly read
        System.out.println("Array after deserialization (before any updates):");
        printArray(); 
        if (array == null || newArray == null) {
            System.out.println("Reinitializing arrays...");
            array = new int[5][5];  
            newArray = new int[5][5];  
        }
    }

    public void loadArray(int[][] loadedArray) {
        System.out.println("Before loading new array:");
        printArray(array);

        this.array = loadedArray;  
        this.newArray = new int[loadedArray.length][loadedArray[0].length]; 

//        // Debug: Check if the array is correctly loaded
//        System.out.println("After loading new array:");
//        printArray(array);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Repainting graphics panel after loading new array.");
                graphicsPanel.repaint();  
            }
        });
    }



    public void updateArraySize(int rows, int columns) {
        array = new int[rows][columns];
        newArray = new int[rows][columns]; 
    }
    
    public int getRows() {
        return array.length; 
    }

    public int getColumns() {
        return array[0].length;
    }
    
    public void printArray() {
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                System.out.print(array[row][col] + " ");
            }
            System.out.println(); 
        }
    }


    public synchronized void updateCell(int row, int col, int value) {
        if (row >= 0 && row < array.length && col >= 0 && col < array[0].length) {
            array[row][col] = value;  
        }
    }

    public int getArrayCell(int row, int col) {
        if (row >= 0 && row < array.length && col >= 0 && col < array[0].length) {
            return array[row][col];
        }
        return 0;
    }

    public void updateWrap(boolean wrap) {
        this.wrap = wrap;  
    }

    public int countNeighbors(int row, int col, boolean wrap) {
        int aliveCount = 0;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;  

                int neighborRow = row + dr;
                int neighborCol = col + dc;

                //System.out.println("Checking neighbor at (" + neighborRow + ", " + neighborCol + ")");

                if (wrap) {
                    neighborRow = (neighborRow + array.length) % array.length;
                    neighborCol = (neighborCol + array[0].length) % array[0].length;
                } else {
                    if (neighborRow < 0 || neighborRow >= array.length || neighborCol < 0 || neighborCol >= array[0].length) {
                        continue;  
                    }
                }

                // Debug: Check if the neighbor is alive
                if (array[neighborRow][neighborCol] == 1) {
                    aliveCount++;
//                    System.out.println("Neighbor at (" + neighborRow + ", " + neighborCol + ") is alive.");
                }
            }
        }

//        System.out.println("Alive neighbors for (" + row + ", " + col + "): " + aliveCount);
        return aliveCount;
    }

    public void updateGeneration(boolean wrap) {
//        // Debug: Before updating the grid
//        System.out.println("Updating generation...");
//        printArray(array); 

        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[0].length; col++) {
                int aliveNeighbors = countNeighbors(row, col, wrap);

                if (array[row][col] == 1) {  // If the cell is alive
                    if (aliveNeighbors == 2 || aliveNeighbors == 3) {
                        newArray[row][col] = 1;  
                    } else {
                        newArray[row][col] = 0; 
                    }
                } else {  // If the cell is dead
                    if (aliveNeighbors == 3) {
                        newArray[row][col] = 1;  
                    } else {
                        newArray[row][col] = 0; 
                    }
                }
            }
        }

//        // Debug: Print the new array before swapping
//        System.out.println("New Array before swapping:");
//        printArray(newArray);  
        
        int[][] temp = array;
        array = newArray;
        newArray = temp;

//        // Debug: Print the array after swapping
//        System.out.println("Array after generation update:");
//        printArray(array);
    }
    
    public void printArray(int[][] arr) {
        for (int row = 0; row < arr.length; row++) {
            for (int col = 0; col < arr[row].length; col++) {
                System.out.print(arr[row][col] + " ");
            }
            System.out.println();  
            }
    }
    
    public void stop(boolean x) {
    	isRunning = x;
    }

    @Override
    public void run() {
    	isRunning = true;
    	while (isRunning) {              
    		updateGeneration(wrap);  
            graphicsPanel.repaint();  // Update the graphics panel on the EDT
            try {
                Thread.sleep(mvc.MVC.BottomControlPanel.delay);  
                } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}