public class Main {
    public static void main(String[] args) {
        Picture input = new Picture("input/test.png");
        SeamCarver sc = new SeamCarver(input);

        int seamNum = 100;

        for(int i = 0; i< seamNum; i++){
            int[] seam = sc.findVerticalSeam();
            sc.removeVerticalSeam(seam);
        }

        sc.picture().save("output/out.png");
        System.out.println("saved shrunk image after removing" + seamNum+ "vertical seams");
    }
}
