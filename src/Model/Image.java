package Model;

import java.io.File;

/**
 * Created by rissa on 10/20/2016.
 */
public class Image {
    private File f;
    private double[] histogram;
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

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity * 10000;
    }
}
