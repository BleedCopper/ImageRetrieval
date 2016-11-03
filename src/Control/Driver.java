package Control;

import java.io.File;

public class Driver {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        // Code to write
        String PATH = "C:\\Users\\Avril_PC\\Desktop\\images";

//        Histogram h = new Histogram();
        File dir = new File(PATH);
        File[] dirListing = dir.listFiles();

        for (File child : dirListing) {

            if (!child.getName().endsWith("jpg")) continue;

            Histogram.computeHistogramWriteToFile(PATH, child.getName());
            QuadHistogram.writeToFile(PATH, child.getName());
            CenteredCH.writeToFile(PATH, child.getName());
            ColorCoherence.writeToFile(PATH, child.getName());
            CenteredColorCoherence.writeToFile(PATH, child.getName());

        }

        // Code to read
        /*String PATH = "C:\\Users\\Avril_PC\\Desktop\\images\\";
        Histogram h = new Histogram();
        double[] test = h.readHistogram(PATH, "1006.jpg");

        for(int i=0; i<159; i++) {
            System.out.print(test[i] + " ");
        }*/


/*        cieConvert cieConvert = new cieConvert();
        cieConvert.initLuvIndex();
        cieConvert.getSimilarityMatrix(0.2);*/


//        String PATH = "C:/Users/rissa/Desktop/images/";
//        String FILENAME = "0.jpg";
//
//        Scanner sc = new Scanner(System.in);
//        int origFile = sc.nextInt();
//
//        FILENAME = origFile + ".jpg";
//
//        Histogram histogram = new Histogram();
//        double[] origFileHistogram = histogram.computeHistogram(PATH, FILENAME);
//
//        File dir = new File(PATH);
//        File[] dirListing = dir.listFiles();
//        if (dirListing != null) {
//
//            double currHighest = -1;
//            String highestFilename = "";
//
//            for (File child : dirListing) {
//
//                if (!child.getName().endsWith("jpg")) continue;
//                if (child.getName().equals(FILENAME)) continue;
//
//                double[] testingFileHistogram = histogram.computeHistogram(PATH, child.getName());
//                int counter = 0;
//                double answer = 0;
//
//                for (int i = 0; i < 159; i++) {
//                    if (origFileHistogram[i] > 0.005) {
//                        counter++;
//                        answer += (1 - Math.abs(origFileHistogram[i] - testingFileHistogram[i]) / Math.max(origFileHistogram[i], testingFileHistogram[i]));
//                    }
//                }
//
//                answer /= counter;
//                if (answer > currHighest) {
//                    currHighest = answer;
//                    highestFilename = child.getName();
//                }
//            }
//            System.out.println(currHighest);
//            System.out.println(highestFilename);




 //       }
    }

}
