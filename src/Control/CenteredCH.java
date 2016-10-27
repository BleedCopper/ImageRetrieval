package Control;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by rissa on 10/24/2016.
 */
public class CenteredCH {
    public static double[][] computeCH(String PATH, String FILENAME) {

        File file = new File(PATH, FILENAME);
        BufferedImage bi;

        double[][] ch = new double[2][159];
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

            double fraction = bi.getWidth()*1.0/bi.getHeight();
            double x = Math.sqrt((totalPixels/2)/fraction);

            int top = (int) ((bi.getHeight() - (x))/2);
            int side = (int) ((bi.getWidth() - (x*fraction))/2);

            int sumo=0, sumi=0;
            for (int i = 0; i < bi.getWidth(); i++) {
                for (int j = 0; j < bi.getHeight(); j++) {
                    if((i>=0&&i<=side)||(i>=map.length-side&&i<=map.length)||(j>=0&&j<=top)||(j>=map[0].length-top&&j<=map[0].length)){
                        ch[0][map[i][j]]++;
                    }
                    else{
                        ch[1][map[i][j]]++;
                    }
                }
            }

            for (int i = 0; i < ch[0].length; i++) {
                ch[0][i] = ch[0][i] / totalPixels/2;
                ch[1][i] = ch[1][i] / totalPixels/2;
            }

            System.out.println(bi.getHeight()+" "+bi.getWidth()+" "+top+" "+side+" "+sumi+" "+sumo+" "+totalPixels);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ch;
    }

    private static int calculateInnerNeighbor(int[][] map, int i, int j, double check, int top, int side) {

        if((i>=-1&&i<=side)||(i>=map.length-side&&i<=map.length)||(j>=-1&&j<=top)||(j>=map[0].length-top&&j<=map[0].length)||map[i][j]!=check){
            return 0;
        }
        else{
            map[i][j]=-1;
            return calculateInnerNeighbor(map, i+1, j, check, top, side)+calculateInnerNeighbor(map, i-1, j, check, top, side)+calculateInnerNeighbor(map, i, j+1, check, top, side)+calculateInnerNeighbor(map, i, j-1, check, top, side)
                    +calculateInnerNeighbor(map, i+1, j+1, check, top, side)+calculateInnerNeighbor(map, i+1, j-1, check, top, side)+calculateInnerNeighbor(map, i-1, j+1, check, top, side)+calculateInnerNeighbor(map, i-1, j-1, check, top, side)+1;
        }
    }

    private static int calculateOuterNeighbor(int[][] map, int i, int j, double check, int top, int side) {

        if(!((i>=-1&&i<=side)||(i>=map.length-side&&i<=map.length)||(j>=-1&&j<=top)||(j>=map[0].length-top&&j<=map[0].length))||i<0||i>=map.length||j<0||j>=map[0].length||map[i][j]!=check){
            return 0;
        }
        else{
            map[i][j]=-1;
            return calculateOuterNeighbor(map, i+1, j, check, top, side)+calculateOuterNeighbor(map, i-1, j, check, top, side)+calculateOuterNeighbor(map, i, j+1, check, top, side)+calculateOuterNeighbor(map, i, j-1, check, top, side)
                    +calculateOuterNeighbor(map, i+1, j+1, check, top, side)+calculateOuterNeighbor(map, i+1, j-1, check, top, side)+calculateOuterNeighbor(map, i-1, j+1, check, top, side)+calculateOuterNeighbor(map, i-1, j-1, check, top, side)+1;
        }
    }
}
