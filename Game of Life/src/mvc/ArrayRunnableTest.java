package mvc;

import java.io.*;
import javax.swing.*;

public class ArrayRunnableTest {

//    public static void main(String[] args) {
//        try {
//            // Step 1: Create a new ArrayRunnable with 3 rows and 3 columns
//            ArrayRunnable arrayRunnable = new ArrayRunnable(3, 3);
//            
//            // Step 2: Modify some cells in the grid
//            arrayRunnable.setValueAt(0, 0, 1);  // Top-left cell
//            arrayRunnable.setValueAt(1, 1, 1);  // Center cell
//            arrayRunnable.setValueAt(2, 2, 1);  // Bottom-right cell
//            
//            // Step 3: Save the state of the ArrayRunnable to a file
//            File saveFile = new File("grid.ser");
//            arrayRunnable.saveToFile(saveFile);
//            System.out.println("Grid saved to file.");
//
//            // Step 4: Load the grid state from the file
//            ArrayRunnable loadedArrayRunnable = ArrayRunnable.loadFromFile(saveFile);
//            System.out.println("Grid loaded from file.");
//
//            // Step 5: Check the grid values of the loaded ArrayRunnable
//            for (int row = 0; row < loadedArrayRunnable.getRows(); row++) {
//                for (int col = 0; col < loadedArrayRunnable.getColumns(); col++) {
//                    System.out.println("Cell (" + row + "," + col + "): " + loadedArrayRunnable.getValueAt(row, col));
//                }
//            }
//
//            // Step 6: Verify the loaded data matches what was saved
//            assert loadedArrayRunnable.getValueAt(0, 0) == 1 : "Test failed at (0, 0)";
//            assert loadedArrayRunnable.getValueAt(1, 1) == 1 : "Test failed at (1, 1)";
//            assert loadedArrayRunnable.getValueAt(2, 2) == 1 : "Test failed at (2, 2)";
//            System.out.println("Test passed! The grid is correctly loaded and saved.");
//            
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
