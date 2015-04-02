package com.noahbutler.agentyou.utilities.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Noah Butler on 3/11/2015.
 */
public class ImagePHash {
    private int size = 64;
    private int smallerSize = 8;

    public ImagePHash() {
        initCoefficients();
    }

    public ImagePHash(int size, int smallerSize) {
        this.size = size;
        this.smallerSize = smallerSize;

        initCoefficients();
    }

    // Returns a 'binary string' (like. 001010111011100010) which is easy to do
    // a hamming distance on.
    public String getHash(String pathName) throws Exception {
        /* create bitmap from image taken by agent */
        Bitmap img = BitmapFactory.decodeFile(pathName);

        double[][] vals = new double[img.getWidth()][img.getHeight()];

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                vals[x][y] = getBlue(img, x, y);
            }
        }

    /*
     * 3. Compute the DCT. The DCT separates the image into a collection of
     * frequencies and scalars. While JPEG uses an 8x8 DCT, this algorithm
     * uses a 32x32 DCT.
     */
        long start = System.currentTimeMillis();
        double[][] dctVals = applyDCT(vals);

    /*
     * 4. Reduce the DCT. This is the magic step. While the DCT is 32x32,
     * just keep the top-left 8x8. Those represent the lowest frequencies in
     * the picture.
     */
    /*
     * 5. Compute the average value. Like the Average Hash, compute the mean
     * DCT value (using only the 8x8 DCT low-frequency values and excluding
     * the first term since the DC coefficient can be significantly
     * different from the other values and will throw off the average).
     */
        double total = 0;

        for (int x = 0; x < smallerSize; x++) {
            for (int y = 0; y < smallerSize; y++) {
                total += dctVals[x][y];
            }
        }
        total -= dctVals[0][0];

        double avg = total / (double) ((smallerSize * smallerSize) - 1);

    /*
     * 6. Further reduce the DCT. This is the magic step. Set the 64 hash
     * bits to 0 or 1 depending on whether each of the 64 DCT values is
     * above or below the average value. The result doesn't tell us the
     * actual low frequencies; it just tells us the very-rough relative
     * scale of the frequencies to the mean. The result will not vary as
     * long as the overall structure of the image remains the same; this can
     * survive gamma and color histogram adjustments without a problem.
     */
        String hash = "";

        for (int x = 0; x < smallerSize; x++) {
            for (int y = 0; y < smallerSize; y++) {
                if (x != 0 && y != 0) {
                    hash += (dctVals[x][y] > avg ? "1" : "0");
                }
            }
        }

        return hash;
    }

    private Bitmap resize(Bitmap image, int width, int height) {
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

    private static int getBlue(Bitmap img, int x, int y) {
        return (img.getPixel(x, y)) & 0xff;
    }

    // DCT function stolen from
    // http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java

    private double[] c;

    private void initCoefficients() {
        c = new double[size];

        for (int i = 1; i < size; i++) {
            c[i] = 1;
        }
        c[0] = 1 / Math.sqrt(2.0);
    }

    private double[][] applyDCT(double[][] f) {
        int N = size;

        double[][] F = new double[N][N];
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        sum += Math
                                .cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI)
                                * Math.cos(((2 * j + 1) / (2.0 * N)) * v
                                * Math.PI) * (f[i][j]);
                    }
                }
                sum *= ((c[u] * c[v]) / 4.0);
                F[u][v] = sum;
            }
        }
        return F;
    }
}
