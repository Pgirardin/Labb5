import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FractalExplorer
{
    private int dimensionDisplay;
    private JImageDisplay image;
    private FractalGenerator generator;
    private Rectangle2D.Double compRange;

    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }

    public FractalExplorer(int size) {
        dimensionDisplay = size;
        generator = new Mandelbrot();
        compRange = new Rectangle2D.Double();
        generator.getInitialRange(compRange);
        image = new JImageDisplay(dimensionDisplay, dimensionDisplay);

    }

    public void createAndShowGUI()
    {
        image.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractals");

        myFrame.add(image, BorderLayout.CENTER);
        JButton resetButton = new JButton("reset");

        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);

        MouseHandler click = new MouseHandler();
        image.addMouseListener(click);

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox myComboBox = new JComboBox();

        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);

        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        myFrame.add(myPanel, BorderLayout.NORTH);

        JButton saveButton = new JButton("save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);

        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);


        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);

    }

    private void drawFractal()
    {
        for (int x=0; x<dimensionDisplay; x++){
            for (int y=0; y<dimensionDisplay; y++){

                double xCoord = generator.getCoord(compRange.x,
                        compRange.x + compRange.width, dimensionDisplay, x);
                double yCoord = generator.getCoord(compRange.y,
                        compRange.y + compRange.height, dimensionDisplay, y);

                int iteration = generator.numIterations(xCoord, yCoord);

                if (iteration == -1){
                    image.drawPixel(x, y, 0);
                }

                else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    image.drawPixel(x, y, rgbColor);
                }

            }
        }
        image.repaint();
    }
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();

            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                generator = (FractalGenerator) mySource.getSelectedItem();
                generator.getInitialRange(compRange);
                drawFractal();

            }
            else if (command.equals("reset")) {
                generator.getInitialRange(compRange);
                drawFractal();
            }
            else if (command.equals("save")) {

                JFileChooser myFileChooser = new JFileChooser();

                FileFilter extensionFilter = new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
                myFileChooser.setAcceptAllFileFilterUsed(false);

                int userSelection = myFileChooser.showSaveDialog(image);

                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    try {
                        BufferedImage displayImage = image.getImage();
                        ImageIO.write(displayImage, "png", file);
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(image, exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else return;
            }
        }
    }

    private class MouseHandler extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            double xCoord = generator.getCoord(compRange.x, compRange.x + compRange.width, dimensionDisplay, x);

            int y = e.getY();
            double yCoord = generator.getCoord(compRange.y, compRange.y + compRange.height, dimensionDisplay, y);

            generator.recenterAndZoomRange(compRange, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }

}