package Control;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by rissa on 10/24/2016.
 */
public class QuadHistogram {

    public static void writeToFile(String PATH, String FILENAME) {

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("QuadH.txt", true)));

            double[][] results = computeHistogram(PATH, FILENAME);
            for(int i=0; i<results.length; i++) {
                for (int j=0; j<results[0].length; j++){
                    pw.print(results[i][j] + " ");
                }
            }

            pw.println();

            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static double[][] computeHistogram(String PATH, String FILENAME) {

        File file = new File(PATH, FILENAME);
        BufferedImage bi;

        double[][] origFileHistogram = new double[4][159];
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

                    if(i<bi.getWidth()/2){
                        if(j<bi.getHeight()/2){
                            origFileHistogram[0][ColorCIE.IndexOf()]++;
                        }
                        else{
                            origFileHistogram[2][ColorCIE.IndexOf()]++;
                        }
                    }
                    else{
                        if(j<bi.getHeight()/2){
                            origFileHistogram[1][ColorCIE.IndexOf()]++;
                        }
                        else{
                            origFileHistogram[3][ColorCIE.IndexOf()]++;
                        }
                    }

                }
            }

            for (int i = 0; i < origFileHistogram.length; i++) {
                origFileHistogram[0][i] = origFileHistogram[0][i] / totalPixels/4;
                origFileHistogram[1][i] = origFileHistogram[1][i] / totalPixels/4;
                origFileHistogram[2][i] = origFileHistogram[2][i] / totalPixels/4;
                origFileHistogram[3][i] = origFileHistogram[3][i] / totalPixels/4;
                //System.out.print(origFileHistogram[i] + ", ");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return origFileHistogram;
    }
}
