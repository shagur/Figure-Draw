package figuredraw;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FigureGUI extends JFrame implements Serializable {

    private JButton redButton = new JButton("Red");
    private JButton greenButton = new JButton("Green");
    private JButton blueButton = new JButton("Blue");
    private JButton rectangleButton = new JButton("Rectangle");
    private JButton circleButton = new JButton("Circle");
    private JButton exitButton = new JButton("Exit");
    private JTextArea listArea = new JTextArea(20, 20);

    PressedButtonListener redButtonListener = new PressedButtonListener();
    PressedButtonListener greenButtonListener = new PressedButtonListener();
    PressedButtonListener blueButtonListener = new PressedButtonListener();
    PressedButtonListener rectangleButtonListener = new PressedButtonListener();
    PressedButtonListener circleButtonListener = new PressedButtonListener();
    PressedButtonListener exitButtonListener = new PressedButtonListener();

    //JPanel to display figures
    private FiguresPanel figuresPanel;

    //enum to represent action status, null by default
    private enum Action {RECTANGLE, CIRCLE, NULL};
    private Action action = Action.NULL;

    Graphics graphics;
    private Color color = Color.WHITE;

    private Rectangle rectangle;
    private Circle circle;

    private String filename = "figures.dat";
    private GregorianCalendar date;

    //array that holds the string representations of figures for redrawing
    private ArrayList<String> stringRepresentations = new ArrayList<String>();

    //constructor
    public FigureGUI() {

        super("Figure GUI");

        Font arial = new Font("Arial", Font.BOLD, 20);
        redButton.setFont(arial);
        greenButton.setFont(arial);
        blueButton.setFont(arial);
        rectangleButton.setFont(arial);
        circleButton.setFont(arial);
        exitButton.setFont(arial);

        //formatted date to show on bottom of figures panel
        date = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = dateFormat.format(date.getTime());

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 4));//JPanel to hold buttons
        JPanel componentsPanel = new JPanel(new GridLayout(0, 3));//JPanel to hold the three components

        figuresPanel = new FiguresPanel(formattedDate);
        figuresPanel.addMouseListener(figuresPanel);

        buttonsPanel.add(redButton);
        buttonsPanel.add(greenButton);
        buttonsPanel.add(blueButton);
        buttonsPanel.add(rectangleButton);
        buttonsPanel.add(circleButton);
        buttonsPanel.add(exitButton);

        redButton.addActionListener(redButtonListener);
        greenButton.addActionListener(greenButtonListener);
        blueButton.addActionListener(blueButtonListener);
        rectangleButton.addActionListener(rectangleButtonListener);
        circleButton.addActionListener(circleButtonListener);
        exitButton.addActionListener(exitButtonListener);

        componentsPanel.add(figuresPanel);
        componentsPanel.add(buttonsPanel);
        componentsPanel.add(listArea);
        componentsPanel.setBackground(Color.gray);
        add(componentsPanel);

    }

    //drawFigure method which takes a string representation of a figure and uses it to draw the figure
    public void drawFigure(String stringObject, Graphics g){

        int x, y, w, h;
        Color color = Color.BLACK;
        String[] stringArray = stringObject.split(" ");

        //withdraws relevant data and draws shape
        if(stringArray[0].compareTo("1") == 0) {
            x = Integer.parseInt(stringArray[1]);
            y = Integer.parseInt(stringArray[2]);
            w = Integer.parseInt(stringArray[3]);
            h = Integer.parseInt(stringArray[4]);
            if(Integer.parseInt(stringArray[5]) == 0) {
                color = Color.RED;
            }else if(Integer.parseInt(stringArray[5]) == 1) {
                color = Color.GREEN;
            }else if(Integer.parseInt(stringArray[5]) == 2) {
                color = Color.BLUE;
            }
            this.rectangle= new Rectangle(color);
            this.rectangle.draw(g, x, y, w, h);//draws shape using data
            listArea.append(this.rectangle.toString());//uses toString operator to append shape's string representation
        }
        else if(stringObject.split(" ")[0].compareTo("2") == 0){
            x = Integer.parseInt(stringArray[1]);
            y = Integer.parseInt(stringArray[2]);
            w = Integer.parseInt(stringArray[3]);
            h = Integer.parseInt(stringArray[4]);
            if(Integer.parseInt(stringArray[5]) == 0) {
                color = Color.RED;
            }else if(Integer.parseInt(stringArray[5]) == 1) {
                color = Color.GREEN;
            }else if(Integer.parseInt(stringArray[5]) == 2) {
                color = Color.BLUE;
            }
            this.circle= new Circle(color);
            this.circle.draw(g, x, y, w,h);//draws shape using data
            listArea.append(this.circle.toString());//uses toString operator to append shape's string representation
        }

    }

    //if figures.dat file exists uses the string representations to populate figures panel
    public void populateFigures(Graphics g){

        ObjectInputStream input = null;
        FileInputStream inputFile = null;

        try {
            inputFile = new FileInputStream(filename);
            input = new ObjectInputStream(inputFile);
            @SuppressWarnings("unchecked")
            ArrayList<String> figureObject = (ArrayList<String>) input.readObject();
            //loop through figureObjects, assign to stringRepresentations arraylist, and draw figures in figures panel
            for(int i = 0; i < figureObject.size(); i++) {
                this.stringRepresentations.add(figureObject.get(i));
                drawFigure(figureObject.get(i), g);
            }
            inputFile.close();
        } catch ( IOException | ClassNotFoundException e) {
            System.out.println("Error: IOException");
        }

    }

    //FiguresPanel class that extends JPanel and implements methods for drawing/displaying figures
    //and serializing the content of the figures and the text area to a file 'figures' upon exiting
    private class FiguresPanel extends JPanel implements MouseListener {

        //x and y hold mouse click coordinates
        //mouseClick holds first and second mouse click coordinates
        private int x;
        private int y;
        private String date;
        private int count;
        private int[][] mouseClick;

        public FiguresPanel(String formattedDate) {//constructor
            x = 0;
            y = 0;
            count = 0;
            mouseClick = new int[2][2];
            date = formattedDate;
        }

        //overrided paint component method
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawString(date, 10, this.getHeight() - 20);
            populateFigures(g);
        }

        //overrided mouseClicked method
        //draws intended figures based on the locations clicked on the figures panel
        @Override
        public void mouseClicked(MouseEvent event) {

            String figureRepresentation ="";

            x = event.getX();
            y = event.getY();

            mouseClick[count][0] = x;
            mouseClick[count++][1] = y;

            if(count == 2)
                switch(action) {

                    case RECTANGLE:
                        rectangle = new Rectangle(color);
                        graphics = figuresPanel.getGraphics();
                        rectangle.draw(graphics, mouseClick[0][0], mouseClick[0][1],
                                mouseClick[1][0] - mouseClick[0][0], mouseClick[1][1] - mouseClick[0][1]);
                        //assign figure representation and add to list area
                        figureRepresentation += "Rectangle [x=" + mouseClick[0][0]
                                + ", y=" + mouseClick[0][1]
                                + ", Width=" + (mouseClick[1][0]- mouseClick[0][0])
                                + ", Height=" + (mouseClick[1][1]- mouseClick[0][1])
                                + ", Color=" + color + " ]\n";
                        listArea.append(figureRepresentation);
                        //add string representation to the arraylist for storage
                        stringRepresentations.add("1 " + mouseClick[0][0]
                                + " " + mouseClick[0][1] + " "
                                + (mouseClick[1][0] - mouseClick[0][0]) + " "
                                + (mouseClick[1][1] - mouseClick[0][1] + " "
                                + this.getColorCode(color)));
                        count = 0;//reset to zero clicks
                        break;
                    case CIRCLE:
                        // process the mouse click for circle object
                        circle = new Circle(color);
                        graphics = figuresPanel.getGraphics();
                        circle.draw(graphics, mouseClick[0][0],
                                mouseClick[0][1],
                                mouseClick[1][0]- mouseClick[0][0],
                                mouseClick[1][1] - mouseClick[0][1]);
                        //circle's string representation is collected
                        figureRepresentation += "Circle [x=" + mouseClick[0][0]
                                + ", y=" + mouseClick[0][1]
                                + ", radius=" + (mouseClick[1][0]- mouseClick[0][0])/2
                                + ", Color=" + color + " ]\n";
                        listArea.append(figureRepresentation);
                        //circle object representation is stored in array list
                        stringRepresentations.add("2 "+ mouseClick[0][0]
                                + " " + mouseClick[0][1] + " "
                                + (mouseClick[1][0]- mouseClick[0][0])+ " "
                                + (mouseClick[1][1] - mouseClick[0][1]) + " "
                                + this.getColorCode(color));
                        count = 0;//reset to zero clicks
                        break;
                    case NULL:
                        count = 0;//reset to zero clicks
                        break;
                }
        }

        //assigns numeric representation to each color and returns the correct one
        public int getColorCode(Color color){
            if(color == Color.RED){
                return 0;
            }else if(color == Color.GREEN){
                return 1;
            }else if(color == Color.BLUE){
                return 2;
            }
            return -1;
        }

        //not used
        @Override
        public void mouseEntered(MouseEvent arg0) {
        }
        //not used
        @Override
        public void mouseExited(MouseEvent arg0) {
        }
        //not used
        @Override
        public void mousePressed(MouseEvent arg0) {
        }
        //not used
        @Override
        public void mouseReleased(MouseEvent arg0) {
        }
    } //end FiguresPanel

    //implements listeners for the button panel
    private class PressedButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == rectangleButton) {
                action = Action.RECTANGLE;
            }
            else if (event.getSource() == circleButton) {
                action = Action.CIRCLE;
            }
            else if (event.getSource() == redButton) {
                color = Color.RED;
            }
            else if (event.getSource() == greenButton) {
                color = Color.GREEN;
            }
            else if (event.getSource() == blueButton) {
                color = Color.BLUE;
            }
            else if (event.getSource() == exitButton) {//upon exiting write data to figures file
                FileOutputStream outputfile = null;
                try {
                    outputfile = new FileOutputStream(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ObjectOutputStream output = null;
                try {
                    output = new ObjectOutputStream(outputfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    output.writeObject(stringRepresentations);
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(ABORT);
            }
        }
    }

    //main method
    public static void main(String[] args) {

        FigureGUI window = new FigureGUI();
        window.setSize(1200, 400);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
