package Control;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by rissa on 10/21/2016.
 */
public class ColorCoherence {
    public static int[][] computeCCV(String PATH, String FILENAME, int TAU) {

        File file = new File(PATH, FILENAME);
        BufferedImage bi;

        int[][] ccv = new int[159][2];
        try {
            bi = ImageIO.read(file);

            int[][] map = new int[bi.getWidth()][bi.getHeight()];

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

                    map[i][j] = ColorCIE.IndexOf();
                }
            }

            for (int i = 0; i < bi.getWidth(); i++) {
                for (int j = 0; j < bi.getHeight(); j++) {
                    int temp = map[i][j];
                    if(map[i][j]!=-1) {
                        int n = calculateNeighbor(map, i, j, map[i][j]);
                        if (n >= TAU/100.0*bi.getHeight()*bi.getWidth()) {
                            ccv[temp][0] += n;
                        } else {
                            ccv[temp][1] += n;
                        }
                    }
                }
            }

//            System.out.println(sum+" "+bi.getHeight()*bi.getWidth());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ccv;
    }

    private static int calculateNeighbor(int[][] map, int i, int j, double check) {

        if(i<0||i>=map.length ||j<0||j>=map[0].length||map[i][j]!=check){
            return 0;
        }
        else{
            map[i][j]=-1;
            return calculateNeighbor(map, i+1, j, check)+calculateNeighbor(map, i-1, j, check)+calculateNeighbor(map, i, j+1, check)+calculateNeighbor(map, i, j-1, check)
                    +calculateNeighbor(map, i+1, j+1, check)+calculateNeighbor(map, i+1, j-1, check)+calculateNeighbor(map, i-1, j+1, check)+calculateNeighbor(map, i-1, j-1, check)+1;
        }
    }

}
