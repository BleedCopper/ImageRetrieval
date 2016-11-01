package View;

import Control.Histogram;
import Model.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    private File fsearch;
    private File fdir;

    public GUI() {
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
                        Histogram histogram = new Histogram();
                        double[] origFileHistogram = histogram.computeHistogram(fdir.getPath(), fsearch.getName());

                        ArrayList<Model.Image> list = new ArrayList<Image>();
                        for (File child : dirListing) {

                            if (!child.getName().endsWith("jpg")) continue;
                            if (child.getName().equals(fsearch.getName())) continue;

                            if (colorHistogramMethodRadioButton.isSelected()) {
                                double[] testingFileHistogram = histogram.computeHistogram(fdir.getPath(), child.getName());
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
                                img.setSimilarity(answer);
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
    }
}
