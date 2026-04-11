package com.procgenworld.engine.noise;

public class PerlinNoiseVisualTest {
    
    public static void main(String[] args) {
        PerlinNoise noise = new PerlinNoise(42);

        int width = 50;
        int height = 80;

        String chars = " .:=+*#%@";

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                double value = noise.octave(x / 20.0, y / 20.0, 4, 0.5);

                int index = (int) ( value * (chars.length() -1));
                System.out.print(chars.charAt(index));
            }
            System.out.println();
            
        }
    }
}
