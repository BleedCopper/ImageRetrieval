package Model;

import java.io.File;

/**
 * Created by rissa on 10/20/2016.
 */
public class Image {
    private File f;
    private double[] histogram;
    double[][] quadHistogram;
    double[][] centerHistogram;
    int[][] ccv;
    int[][][] centerCCV;

    private double similarity;

    public Image(File f) {
        this.f = f;
    }

    public File getF() {
        return f;
    }

    public void setF(File f) {
        this.f = f;
    }

    public double[] getHistogram() {
        return histogram;
    }

    public void setHistogram(double[] histogram) {
        this.histogram = histogram;
    }


    public double[][] getQuadHistogram() {
        return quadHistogram;
    }

    public void setQuadHistogram(double[][] quadHistogram) {
        this.quadHistogram = quadHistogram;
    }

    public double[][] getCenterHistogram() {
        return centerHistogram;
    }

    public void setCenterHistogram(double[][] centerHistogram) {
        this.centerHistogram = centerHistogram;
    }

    public int[][] getCcv() {
        return ccv;
    }

    public void setCcv(int[][] ccv) {
        this.ccv = ccv;
    }

    public int[][][] getCenterCCV() {
        return centerCCV;
    }

    public void setCenterCCV(int[][][] centerCCV) {
        this.centerCCV = centerCCV;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity =  similarity;
    }
}
