package com.procgenworld.engine.noise;

import java.util.Random;

/**
 * Implementation of the classic Perlin Noise algorithm for 2D coherent noise generation.
 * Deterministic: the same seed always produces the same output.
 */
public final class PerlinNoise {
    
    /** Standard Permutation table size. Historical constant - do not change. */
    private static final int TABLE_SIZE = 256;

    /** Doubled permutation table to avoid index out of bounds during corner lookups. */
    private final int[] permutations;

    public PerlinNoise(long seed){
        this.permutations = buildPermutationTable(seed);
    }

    /**
     * Generates a noise value at position(x, y) using multiple octaves.
     * Each octave double the frequency and halves the amplitude,
     * adding progressively finer detail to the result.
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @param octaves number of noise layers, more = more details
     * @param persistence amplitude multiplier per octave (0.0-1.0)
     * @return noise value normalized to [0.0, 1.0]
     */
    public double octave(double x, double y, int octaves, double persistence) {
        double total = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;
        double maxValue = 0.0; // used for normalization

        for (int i = 0; i < octaves; i++) {
            total += noise(x * frequency, y * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2.0; // each octave doubles the frequency
        }

        // Normalize to [0.0, 1.0]
        return (total / maxValue + 1.0) / 2.0;
    }    

    /** Builds a permutation table shuffled deterministically from the given seed. */
    private int[] buildPermutationTable(long seed) {
        int[] source = new int[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            source[i] = i;
        }

        //Fisher-Yates shuffle
        //random order but reproductible with the same seed
        Random random = new Random(seed);
        for (int i = TABLE_SIZE - 1; i > 0 ; i--) {
            int j = random.nextInt(i+1);
            int tmp = source[i];
            source[i] = source[j];
            source[j] = tmp;
        }

        //doubled to avoid ArrayOutOfBounds later
        int[] doubled = new int[TABLE_SIZE * 2];
        for (int i = 0; i < TABLE_SIZE * 2; i++) {
            doubled[i] = source[i % TABLE_SIZE];
        }
        return doubled;
    }

    /** 
     * Computes raw Perlin Noise at (x,y)
     * Finds the unit square containing the point, computes gradients at its
     * four corners, then interpolates between them.
     */
    private double noise(double x, double y) {
        // 1. Unit Square containing (x,y), wrapped to [0,255]
        int xi = (int) Math.floor(x) & (TABLE_SIZE - 1);
        int yi = (int) Math.floor(y) & (TABLE_SIZE - 1);

        // 2. relative position (xf, yf) inside the unit square, in [0.0, 1.0]
        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        // 3. Smoothed position - eliminates grid artifaces at square boundaries
        double u = fade(xf);
        double v = fade(yf);

        // 4. Hash of 4 corners (aa, ba, ab, bb) of the unit square via double lookup
        int aa = permutations[permutations[xi] + yi];
        int ba = permutations[permutations[xi + 1] + yi];
        int ab = permutations[permutations[xi] + yi + 1];
        int bb = permutations[permutations[xi + 1] + yi + 1];

        // 5. Gradients at each corner, dotted with distance vector from corner to point
        double gradAA = grad(aa, xf, yf);
        double gradBA = grad(ba, xf - 1.0, yf);
        double gradAB = grad(ab, xf,       yf - 1.0);
        double gradBB = grad(bb, xf - 1.0, yf - 1.0);

        // 6. Billinear interpolation: horizontal first, then vertical
        double lerpX1 = lerp(u, gradAA, gradBA);
        double lerpX2 = lerp(u, gradAB, gradBB);
        double result = lerp(v, lerpX1, lerpX2);

        return result;
    }

    /**
     * Smooth fade curve: 6t^5 - 15t^4 + 10t^3
     * Zero first and second derivatives at t=0 and t=1 - ensure seamless transitions
     * between unit squares. Defined by Ken Perlin in his 2002 SIGGRAPH paper.
     */
    private double fade(double t) {
        double t3 = t * t * t;
        double t4 = t3 * t;
        double t5 = t4 * t;

        return 6*t5 - 15*t4 + 10*t3;
    }

    /**
     * Select one of 4 gradient directions based on the hash,
     * then returns the dot product with the distance vector (x,y).
     * Using 4 directions is sufficient for smooth 2D results.
     */
    private double grad(int hash, double x,double y) {
        return switch (hash & 3) {
            case 0 -> x + y;
            case 1 -> -x + y;
            case 2 -> x - y;
            case 3 -> -x - y;
            default -> 0.0;
        };
    }

    /** Linear interpolation between a and b, t in [0.0, 1.0] */
    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }



}
