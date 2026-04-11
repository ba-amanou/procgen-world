package com.procgenworld.engine.noisemap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.procgenworld.engine.noise.PerlinNoise;

public class DefaultNoiseMapGenerator implements NoiseMapGenerator {

    private static final Logger log = LoggerFactory.getLogger(DefaultNoiseMapGenerator.class);

    private final NoiseMapConfig config;

    public DefaultNoiseMapGenerator(NoiseMapConfig config) {
        this.config = config;
    }

    @Override
    public double[][] generate(long seed, int width, int height) {
        log.debug("Generating height map: seed={}, width={}, height={}, config={}",
                seed, width, height, config);

        PerlinNoise noise = new PerlinNoise(seed);
        double[][] map = new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = noise.octave(x / config.scale(), y / config.scale(), config.octaves(), config.persistence());
            }
        }

        return map;
    }
    
}
