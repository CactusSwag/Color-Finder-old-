import java.lang.Object;
import java.util.ArrayList;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.io.File;
import java.io.FileWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.*;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.lang.StringBuilder;
import java.awt.event.InputEvent;


public class App {
    public static void main(String[] args) throws Exception {
        screenShot();
        colorCoords();
        fileReader();
        coordFinder();
        clickingListX();
        clickingListY();
        mouseMoving();
    }

    public static void screenShot() throws IOException {
        
        try {
            // Create Robot screenCap
            Robot screenCap = new Robot();
    
            // Get screen size
            Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    
            // Get screenshot from screen size
            BufferedImage tmpImg = screenCap.createScreenCapture(screenSize);

            // Prints buffered image to png
            File outputFile = new File("save.png");
            ImageIO.write(tmpImg, "png", outputFile);
        }

        catch(Exception e) {
            System.out.println("Fail 1");
        }
    }

    public static void colorCoords() throws IllegalArgumentException, IOException, FileNotFoundException {
        try {
            // Reads from screenshot, gets height, width of screenshot, creates array
            BufferedImage image = ImageIO.read(new File("save.png"));
            int height = image.getHeight();
            int width = image.getWidth();
            String colors;
            String coords;
            ArrayList<String> colorArray = new ArrayList<String>();
            ArrayList<String> coordArray = new ArrayList<String>();

            // Searches screenshot for pixels RGB values, makes array of RGB values
            for(int i=0; i<width; i++) {
                for(int j=0; j<height; j++) {
                    Color color = new Color(image.getRGB(i, j));
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    colors = red + ", " + green + ", " + blue;
                    coords = i + ", " + j;
                    colorArray.add(colors);
                    coordArray.add(coords);
                }
            }

            // Writes array to output file txt
            FileWriter colorWrite = new FileWriter("colorsRGB.txt");
            for (String str: colorArray) {
                colorWrite.write(str + System.lineSeparator());
            }
            colorWrite.close();

            FileWriter coordWrite = new FileWriter("coordsRGB.txt");
            for (String str: coordArray) {
                coordWrite.write(str + System.lineSeparator());
            }
            coordWrite.close();
        }
        
        catch(Exception e) {
            System.out.println("Fail 2");
        }

    }

    public static void fileReader() throws FileNotFoundException, IOException {
        // Creates scanner for colorsRGB and sets RGB value to look for
        Scanner colorsScan = new Scanner(new File("colorsRGB.txt"));
        String RGB = "231, 201, 93";
    
        // Sets integers to count
        int totalCount = 0;
        int trueCount = 0;
        int line = 0;
        ArrayList<Integer> lineArray = new ArrayList<Integer>();

        // Checks each line if = to RGB value, keeps count
        while (colorsScan.hasNextLine()) {
            String curLine = colorsScan.nextLine();
            if (RGB.equals(curLine)) {
                trueCount++;
                line = totalCount - trueCount;
                trueCount = 0;
                lineArray.add(line);
                line = 0;
            }
            totalCount++;
        }

        // Writes array to output file txt
        FileWriter lineWrite = new FileWriter("lines.txt");
        for (int str: lineArray) {
            lineWrite.write(str + System.lineSeparator());
        }
        lineWrite.close();
    }

    public static void coordFinder() throws FileNotFoundException, IOException, IllegalArgumentException {
        // Setup scanner, variables, and array list
        Scanner lineScan = new Scanner(new File("lines.txt"));
        String convert = "";
        int lineNum = 0;
        String lineFinder = "";
        ArrayList<String> coordsLine = new ArrayList<String>();

        // Converts lines.txt from string to int
        while (lineScan.hasNextLine()) {
            convert = lineScan.nextLine();
            lineNum = Integer.parseInt(convert);

            // Scans coordsRGB.txt by skipping the number from lines.txt + 1, adds to array
            try (Stream<String> lines = Files.lines(Paths.get("coordsRGB.txt"))) {
                lineFinder = lines.skip(lineNum + 1).findFirst().get();
                coordsLine.add(lineFinder);
            }
        }

        // Writes array to file
        FileWriter coordsLineWrite = new FileWriter("coordList.txt");
        for (String str: coordsLine) {
            coordsLineWrite.write(str + System.lineSeparator());
        }
        coordsLineWrite.close();
    }

    public static void clickingListX() throws FileNotFoundException, IOException {
        
        // Sets up scanner, variables, array
        Scanner clickScanX = new Scanner(new File("coordList.txt"));
        String readListX = "";
        String listX = "";
        char check = ',';
        int commaNum = 0;
        int numDeletes = 0;
        ArrayList<String> clickXArray = new ArrayList<String>();

        // Deletes everything after comma (keeps X coord only), adds to array
        while (clickScanX.hasNextLine()) {
            readListX = clickScanX.nextLine();
            for (int i = 1; i < readListX.length(); i++) {
                if (readListX.charAt(i) == check) {
                    commaNum = i;
                    listX = readListX;
                    numDeletes = commaNum;
                    while (numDeletes < readListX.length()) {
                        StringBuilder newX = new StringBuilder(listX);
                        newX.deleteCharAt(commaNum);
                        listX = newX.toString();
                        numDeletes++;
                    }
                    clickXArray.add(listX);
                }                
                
                // Writes array to file
                FileWriter clickXWrite = new FileWriter("clickXList.txt");
                for (String str: clickXArray) {
                    clickXWrite.write(str + System.lineSeparator());
                }
                clickXWrite.close();
            }
        }
    }

    public static void clickingListY() throws FileNotFoundException, IOException {
        
        // Sets up scanner, variables, array
        Scanner clickScanY = new Scanner(new File("coordList.txt"));
        String readListY = "";
        String listY = "";
        char check = ',';
        ArrayList<String> clickYArray = new ArrayList<String>();

        // Deletes everything before comma (keeps Y coord only), adds to array
        while (clickScanY.hasNextLine()) {
            readListY = clickScanY.nextLine();
            for (int i = 0; i < readListY.length(); i++) {
                if (readListY.charAt(i) == check) {
                    listY = readListY;
                    StringBuilder newY = new StringBuilder(listY);
                    newY.deleteCharAt(i);
                    listY = newY.toString();
                    for (int j = 0; j <= i; j++) {
                        StringBuilder newNewY = new StringBuilder(listY);
                        newNewY.deleteCharAt(i - j);
                        listY = newNewY.toString();
                    }
                    clickYArray.add(listY);
                }
            
                // Writes array to file
                FileWriter clickYWrite = new FileWriter("clickYList.txt");
                for (String str: clickYArray) {
                    clickYWrite.write(str + System.lineSeparator());
                }
                clickYWrite.close();
            }
        }
    }

    public static void mouseMoving() throws FileNotFoundException, IOException {
        
        // Sets up scanners, variables
        Scanner mouseX = new Scanner(new File("clickXList.txt"));
        Scanner mouseY = new Scanner(new File("clickYList.txt"));
        int xMouseCoord = 0;
        int yMouseCoord = 0;
        String convertX = "";
        String convertY = "";
        
        try {

        // Creates robot
        Robot mouseMover = new Robot();

            // Translates file values to integers, moves mouse to them, clicks
            while (mouseX.hasNextLine()) {
                convertX = mouseX.nextLine();
                xMouseCoord = Integer.parseInt(convertX);
                convertY = mouseY.nextLine();
                yMouseCoord = Integer.parseInt(convertY);
                System.out.println(xMouseCoord + "," + yMouseCoord);
                mouseMover.mouseMove(xMouseCoord, yMouseCoord);
                mouseMover.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                mouseMover.delay(100);
                mouseMover.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                mouseMover.delay(100);
            }
        }

        catch(Exception e) {
            System.out.println("Fail 3");
        }
    }
}