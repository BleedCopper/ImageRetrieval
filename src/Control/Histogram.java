package Control;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Histogram {

    public static double[] computeHistogram(String PATH, String FILENAME) {

        File file = new File(PATH, FILENAME);
        BufferedImage bi;

        double[] origFileHistogram = new double[159];
        try {
            bi = ImageIO.read(file);

            int totalPixels = bi.getHeight() * bi.getWidth();

            for (int i = 0; i < bi.getWidth(); i++) {
                for (int j = 0; j < bi.getHeight(); j++) {

                    double R, G, B;

                    Color c = new Color(bi.getRGB(i, j));

                    R = c.getRed();   //get the 8-bit values of RGB (0-255)
                    G = c.getGreen();
                    B = c.getBlue();

                    cieConvert ColorCIE = new cieConvert();
                    ColorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);

                    origFileHistogram[ColorCIE.IndexOf()]++;

                }
            }

            for (int i = 0; i < origFileHistogram.length; i++) {
                origFileHistogram[i] = origFileHistogram[i] / totalPixels;
                //System.out.print(origFileHistogram[i] + ", ");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return origFileHistogram;
    }
}