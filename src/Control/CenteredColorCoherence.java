package Control;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by rissa on 10/22/2016.
 */
public class CenteredColorCoherence {



    public static void writeToFile(String PATH, String FILENAME) {

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("CenterCCV.txt", true)));

            int[][][] results = computeCCV(PATH, FILENAME, 5);
            for(int i=0; i<results.length; i++) {
                for (int j=0; j<results[0].length; j++){
                    for (int k=0; k<results[0][0].length; k++){
                        pw.print(results[i][j][k] + " ");
                    }
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

    public static int[][][] computeCCV(String PATH, String FILENAME, int TAU) {

        File file = new File(PATH, FILENAME);
        BufferedImage bi;

        int[][][] ccv = new int[2][159][2];
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
                    int temp = map[i][j];
                    if(map[i][j]!=-1) {
                        int n = calculateInnerNeighbor(map, i, j, map[i][j], top, side);
                        if (n >= TAU/100.0*bi.getHeight()*bi.getWidth()) {
                            ccv[0][temp][0] += n;
                        } else {
                            ccv[0][temp][1] += n;
                        }
                        sumi+=n;
                        n = calculateOuterNeighbor(map, i, j, map[i][j], top, side);
                        if (n >= TAU/100.0*bi.getHeight()*bi.getWidth()) {
                            ccv[1][temp][0] += n;
                        } else {
                            ccv[1][temp][1] += n;
                        }
                        sumo+=n;
                    }
                }
            }

//            System.out.println(bi.getHeight()+" "+bi.getWidth()+" "+top+" "+side+" "+sumi+" "+sumo+" "+totalPixels);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ccv;
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
