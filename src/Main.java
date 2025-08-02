package src;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java src.Main <vertical> <horizontal>");
            return;
        }

        int vertical = Integer.parseInt(args[0]);
        int horizontal = Integer.parseInt(args[1]);

        Picture input = new Picture("input/input.png");
        SeamCarver sc = new SeamCarver(input);

        // validate against image dimensions
        int maxVertical = sc.width() - 1;
        int maxHorizontal = sc.height() - 1;

        if (vertical > maxVertical) {
            System.out.println("Reducing vertical seams from " + vertical + " to " + maxVertical);
            vertical = maxVertical;
        }

        if (horizontal > maxHorizontal) {
            System.out.println("Reducing horizontal seams from " + horizontal + " to " + maxHorizontal);
            horizontal = maxHorizontal;
        }

        // remove vertical seams
        for (int i = 0; i < vertical; i++) {
            int[] seam = sc.findVerticalSeam();
            sc.removeVerticalSeam(seam);
        }

        // remove horizontal seams
        for (int i = 0; i < horizontal; i++) {
            int[] seam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(seam);
        }

        sc.picture().save("output/out.png");
        System.out.println("Saved image after removing " + vertical + " vertical and " + horizontal + " horizontal seams.");
    }
}
