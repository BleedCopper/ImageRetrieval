package View;

import Control.*;
import Model.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by rissa on 10/19/2016.
 */
public class GUI {
    private JPanel panel1;
    private JPanel pcontent;
    private JPanel pmenu;
    private JScrollPane spane;
    private JButton searchButton;
    private JRadioButton colorHistogramMethodRadioButton;
    private JRadioButton CHWithPerceptualSimilarityRadioButton;
    private JRadioButton histogramRefinementWithColorRadioButton;
    private JRadioButton CHWithCenteringRefinementRadioButton;
    private JSpinner spinner1;
    private JButton btnfile;
    private JButton btndirectory;
    private JPanel panImages;
    private JPanel panList;
    private JRadioButton quadCHRadioButton;
    private JRadioButton CCVWithCenteringRefinementRadioButton;

    private File fsearch;
    private File fdir;

    static HashMap<String, Image> images;

    private double[][] simMatrix;
    private cieConvert cieConvert;
    public GUI() {

        cieConvert = new cieConvert();
        cieConvert.initLuvIndex();
        simMatrix = cieConvert.getSimilarityMatrix(0.2);

        fsearch = null;
        fdir = null;
        spinner1.setModel(new SpinnerNumberModel(10, 1, 50, 1));
        panList.setLayout(new GridLayout(0, 6));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fsearch != null && fdir != null) {
                    File[] dirListing = fdir.listFiles();
                    if (dirListing != null) {
//                        Histogram histogram = new Histogram();
//                        ColorCoherence colorCoherence = new ColorCoherence()
                        double[] origFileHistogram = images.get(fsearch.getName()).getHistogram();
                        double[][] origFileQuadHistogram = images.get(fsearch.getName()).getQuadHistogram();
                        double[][] origFileCenterHistogram = images.get(fsearch.getName()).getCenterHistogram();
                        int[][] origFileCCV = images.get(fsearch.getName()).getCcv();
                        int[][][] origFileCenterCCV = images.get(fsearch.getName()).getCenterCCV();

                        ArrayList<Model.Image> list = new ArrayList<Image>();
                        for (File child : dirListing) {

                            if (!child.getName().endsWith("jpg")) continue;
                            if (child.getName().equals(fsearch.getName())) continue;

                            if (colorHistogramMethodRadioButton.isSelected()) {
                                double[] testingFileHistogram = images.get(child.getName()).getHistogram();
                                int counter = 0;
                                double answer = 0;

                                for (int i = 0; i < 159; i++) {
                                    if (origFileHistogram[i] > 0.005) {
                                        counter++;
                                        answer += (1 - Math.abs(origFileHistogram[i] - testingFileHistogram[i]) /
                                                       Math.max(origFileHistogram[i], testingFileHistogram[i]));
                                    }
                                }

                                answer /= counter;

                                Image img = new Image(child);
                                img.setSimilarity(answer*10000);
                                list.add(img);
                            }

                            if(CHWithPerceptualSimilarityRadioButton.isSelected()){


                                double[] testingFileHistogram = images.get(child.getName()).getHistogram();
                                double simExact = 0;
                                double simColor = 0;


                                double answer = 0;

                                for (int i = 0; i < 159; i++) {

                                    if (origFileHistogram[i] > 0.005) {
                                        double simPerCol = 0;
                                        simExact = (1 - Math.abs(origFileHistogram[i] - testingFileHistogram[i]) / Math.max(origFileHistogram[i], testingFileHistogram[i]));

                                        for(int j=0; j<159; j++)
                                            simPerCol += (1-Math.abs(origFileHistogram[i] - testingFileHistogram[j])/
                                                    Math.max(origFileHistogram[i], testingFileHistogram[i])) * simMatrix[i][j];

                                        simColor = simExact*(1+simPerCol);
                                        answer+= simColor * origFileHistogram[i];
                                    }

                                }

                                Image img = new Image(child);
                                img.setSimilarity(answer*10000);
                                list.add(img);
                            }
                            if(histogramRefinementWithColorRadioButton.isSelected()){
                                int[][] testingFileCCV = images.get(child.getName()).getCcv();

                                double answer = 0;

                                for (int i = 0; i < 159; i++) {
                                    answer+=(Math.abs(origFileCCV[i][0]-testingFileCCV[i][0])+Math.abs(origFileCCV[i][1]-testingFileCCV[i][1]));
                                }

                                Image img = new Image(child);
                                img.setSimilarity(1-answer);
                                list.add(img);
                            }
                            if(CHWithCenteringRefinementRadioButton.isSelected()){
                                double[][] testingFileCenterHistogram = images.get(child.getName()).getCenterHistogram();

                                double fin=0;
                                for (int j =0; j<2; j++){

                                    int counter = 0;
                                    double answer = 0;
                                    for (int i = 0; i < 159; i++) {
                                        if (origFileQuadHistogram[j][i] > 0.005) {
                                            counter++;
                                            answer += (1 - Math.abs(origFileCenterHistogram[j][i] - testingFileCenterHistogram[j][i]) / Math.max(origFileCenterHistogram[j][i], testingFileCenterHistogram[j][i]));
                                        }
                                    }

                                    answer /= counter;
                                    fin+=answer;
                                }

                                Image img = new Image(child);
                                img.setSimilarity(fin*10000);
                                list.add(img);
                            }
                            if(CCVWithCenteringRefinementRadioButton.isSelected()){
                                int[][][] testingFileCenterCCV = images.get(child.getName()).getCenterCCV();

                                double answer = 0;

                                for (int i = 0; i < 159; i++) {
                                    answer+=(Math.abs(origFileCenterCCV[0][i][0]-testingFileCenterCCV[0][i][0])+Math.abs(origFileCenterCCV[0][i][1]-testingFileCenterCCV[0][i][1]));
                                    answer+=(Math.abs(origFileCenterCCV[1][i][0]-testingFileCenterCCV[1][i][0])+Math.abs(origFileCenterCCV[1][i][1]-testingFileCenterCCV[1][i][1]));
                                }

                                Image img = new Image(child);
                                img.setSimilarity(1-answer);
                                list.add(img);
                            }
                            if(quadCHRadioButton.isSelected()){
                                double[][] testingFileQuadHistogram = images.get(child.getName()).getQuadHistogram();

                                double fin=0;
                                for (int j =0; j<4; j++){

                                    int counter = 0;
                                    double answer = 0;
                                    for (int i = 0; i < 159; i++) {
                                        if (origFileQuadHistogram[j][i] > 0.005) {
                                            counter++;
                                            answer += (1 - Math.abs(origFileQuadHistogram[j][i] - testingFileQuadHistogram[j][i]) / Math.max(origFileQuadHistogram[j][i], testingFileQuadHistogram[j][i]));
                                        }
                                    }

                                    answer /= counter;
                                    fin+=answer;
                                }

                                Image img = new Image(child);
                                img.setSimilarity(fin*10000);
                                list.add(img);
                            }
                        }

                        panList.removeAll();
                        Collections.sort(list, new Comparator<Image>() {
                            @Override
                            public int compare(Image o1, Image o2) {
                                return (int) (o1.getSimilarity() - o2.getSimilarity());
                            }
                        });

                        int size = (int) spinner1.getValue();
                        int i = 0;
                        while (size > 0) {
                            panList.add(new JLabel(new ImageIcon(list.get(list.size() - 1 - i).getF().getPath())));
                            System.out.println(list.get(list.size() - 1 - i).getSimilarity());
                            i++;
                            size--;
                        }
                        panel1.repaint();
                        panel1.revalidate();
                    }
                }
            }
        });

        btnfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();
                f.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
                int num = f.showOpenDialog(panel1);
                if (num == JFileChooser.APPROVE_OPTION) {
                    File file = f.getSelectedFile();
                    fsearch = file;
                    //This is where a real application would open the file.
                    btnfile.setText(file.getName());
                }
            }
        });

        btndirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();

                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int num = f.showOpenDialog(panel1);
                if (num == JFileChooser.APPROVE_OPTION) {
                    File file = f.getSelectedFile();
                    fdir = file;
                    //This is where a real application would open the file.
                    btndirectory.setText(file.getAbsolutePath());

                    File[] dirListing = file.listFiles();
                    if (dirListing != null) {

                        for (File child : dirListing) {

                            if (!child.getName().endsWith("jpg")) continue;

                            JLabel lbl = new JLabel(new ImageIcon(child.getPath()));
                            panList.add(lbl);
                        }
                        panImages.revalidate();
                        panImages.repaint();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(1300, 1000));
        frame.setVisible(true);
        String PATH="C:\\Users\\rissa\\Desktop\\images";

        images = new HashMap<>();
        File file = new File(PATH);

        try {
            BufferedReader hbr = new BufferedReader(new InputStreamReader(new FileInputStream("Histogram.txt")));
            BufferedReader ccvbr = new BufferedReader(new InputStreamReader(new FileInputStream("CCV.txt")));
            BufferedReader centerccvbr = new BufferedReader(new InputStreamReader(new FileInputStream("CenterCCV.txt")));
            BufferedReader centerhbr = new BufferedReader(new InputStreamReader(new FileInputStream("CenterH.txt")));
            BufferedReader quadhbr = new BufferedReader(new InputStreamReader(new FileInputStream("QuadH.txt")));
            String line;

            for (int i=0; i<file.listFiles().length; i++){
                line = hbr.readLine();

                Image img = new Image(new File(PATH,line));
                images.put(img.getF().getName(), img);

                line=hbr.readLine();
                String[] stringArrayOfHistogramValues = line.split(" ");
                double[] fileHistogram = new double[159];
                int z=0;
                for(String str: stringArrayOfHistogramValues) {
                    fileHistogram[z] = Double.parseDouble(str);
                    z++;
                }
                img.setHistogram(fileHistogram);

                line=ccvbr.readLine();
                String[] arr = line.split(" ");
                int[][] ccv = new int[159][2];
                for (int j=0; j<159; j++){
                    for (int k=0; k<2; k++){
                        ccv[j][k]=Integer.parseInt(arr[(j*2)+k]);
                    }
                }
                img.setCcv(ccv);

                line=centerccvbr.readLine();
                arr = line.split(" ");
                int[][][] cenccv = new int[2][159][2];

                for (int x=0; x<2; x++) {
                    for (int j = 0; j < 159; j++) {
                        for (int k = 0; k < 2; k++) {
                            cenccv[x][j][k] = Integer.parseInt(arr[x*159+((j * 2) + k)]);
                        }
                    }
                }
                img.setCenterCCV(cenccv);

                line = centerhbr.readLine();
                arr = line.split(" ");
                double[][] ch = new double[2][159];
                for (int j=0; j<2; j++){
                    for (int k=0; k<159; k++){
                        ch[j][k]=Double.parseDouble(arr[(j*159)+k]);
                    }
                }
                img.setCenterHistogram(ch);

                line = quadhbr.readLine();
                arr = line.split(" ");
                double[][] qh = new double[4][159];
                for (int j=0; j<4; j++){
                    for (int k=0; k<159; k++){
                        qh[j][k]=Double.parseDouble(arr[(j*159)+k]);
                    }
                }
                img.setQuadHistogram(qh);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
