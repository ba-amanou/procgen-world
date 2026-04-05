package com.procgenworld.engine.heightmap;

public interface HeightMapGenerator {
    
    /**
     * Generates a normalized height map.
     * 
     * @param seed the generation seed number - same seed produces the same map
     * @param width the width in tiles, must be > 0
     * @param height the height in tiles, must be > 0
     * @return a 2D array of doubles in range [0.0, 1.0], dimensions [width][height]
     */
    double[][] generate(long seed, int width, int height);
}
