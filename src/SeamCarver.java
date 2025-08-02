package src;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("null picture");
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()){
            throw new IllegalArgumentException("out of bounds");
        }

        Color left = picture.get((x==0) ? width() - 1 : x-1 , y);
        Color right = picture.get((x== width() - 1) ? 0: x+1 , y);
        Color up    = picture.get(x, (y == 0) ? height() - 1 : y - 1);
        Color down  = picture.get(x, (y == height() - 1) ? 0 : y + 1);

        int dx = gradientSquared(left, right);
        int dy = gradientSquared(up, down);

        return dx + dy;
    }

    private int gradientSquared(Color c1, Color c2){
        int r = c1.getRed() - c2.getRed();
        int g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        return r*r + g*g + b*b;
    }

    public int[] findVerticalSeam() {
        int w = width();
        int h = height();

        double[][] energy = new double[h][w];
        double[][] distTo = new double[h][w];
        int[][] edgeTo = new int[h][w];

        //fill energy matrix
        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                energy[i][j] = energy(j,i);
            }
        }

        //initialize top row
        for (int i = 0; i < w; i++){
            distTo[0][i] = energy[0][i];
        }

        //dp - compute lowest path energy to each pixel
        for (int y = 1; y < h; y++){
            for (int x = 0; x < w; x++){
                distTo[y][x] = Double.POSITIVE_INFINITY;

                for (int dx = -1; dx <=1; dx++){
                    int prevX = x + dx;
                    if (prevX >=0 && prevX <w){
                        double newDist = distTo[y-1][prevX] + energy[y][x];
                        if (newDist < distTo[y][x]) {
                            distTo[y][x] = newDist;
                            edgeTo[y][x] = prevX;
                        }
                    }
                }
            }
        }

        //get x in bottom row w minimal total energy
        double minEn = Double.POSITIVE_INFINITY;
        int minX = 0;
        for (int x = 0; x < w; x++) {
            if (distTo[h-1][x] < minEn) {
                minEn = distTo[h-1][x];
                minX = x;
            }
        }

        //trace seam path
        int[] seam = new int[h];
        seam[h-1] = minX;
        for (int y = h-2; y>=0; y--){
            seam[y] = edgeTo[y + 1][seam[y + 1]];
        }
        return seam;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("seam is null");
        if (width() <= 1 || seam.length != height())
            throw new IllegalArgumentException("invalid seam length");
        
        for (int y = 0; y < height(); y++){
            if (seam[y] < 0 || seam[y] >= width())
            throw new IllegalArgumentException("seam ele out of bounds" );
        if (y > 0 && Math.abs(seam[y] - seam[y - 1]) > 1)
            throw new IllegalArgumentException("seam not connectedow ");
        }

        Picture newPic = new Picture(width()-1, height());

        for (int y = 0; y <height(); y++){
            int colSkip = seam[y];
            for(int x = 0; x< colSkip; x++){
                newPic.set(x,y, picture.get(x,y));
            }
            for(int x = colSkip +1; x< width(); x++){
                newPic.set(x -1,y, picture.get(x,y));
            }
        }
        this.picture = newPic;
    }

    private Picture transposePicture(Picture p) {
        int w = p.width();
        int h = p.height();
        Picture transposed = new Picture(h,w);
        for(int y = 0; y<h; y++){
            for (int x =0; x<w; x++){
                transposed.set(y,x, p.get(x,y));
            }
        }
        return transposed;
    }

    public int[] findHorizontalSeam() {
        this.picture = transposePicture(this.picture);
        int[] seam = findVerticalSeam();
        this.picture = transposePicture(this.picture);
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null seam");
        if (height() <= 1 || seam.length != width())
            throw new IllegalArgumentException("invalid seam length");
    
        for (int x = 0; x < width(); x++) {
            if (seam[x] < 0 || seam[x] >= height())
                throw new IllegalArgumentException("seam out of bounds at column " + x);
            if (x > 0 && Math.abs(seam[x] - seam[x - 1]) > 1)
                throw new IllegalArgumentException("seam not connected at column " + x);
        }
        this.picture = transposePicture(this.picture);
        removeVerticalSeam(seam);
        this.picture = transposePicture(this.picture);
    }
    
    

        
} 
