package src;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java src.Main <vertical>");
            return;
        }

        int vertical = Integer.parseInt(args[0]);

        Picture input = new Picture("input/input.png");
        SeamCarver sc = new SeamCarver(input);

        for (int i = 0; i < vertical; i++) {
            int[] seam = sc.findVerticalSeam();
            sc.removeVerticalSeam(seam);
        }

        sc.picture().save("output/out.png");
        System.out.println("Saved image after removing " + vertical + " vertical seams.");
    }
}
