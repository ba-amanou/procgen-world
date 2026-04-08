package com.procgenworld.engine.biome;

public class DefaultBiomeResolver implements BiomeResolver {

    private static final double DEEP_WATER_THRESHOLD = 0.35;
    private static final double OCEAN_THRESHOLD      = 0.45;
    private static final double BEACH_THRESHOLD      = 0.50;
    private static final double MOUNTAIN_THRESHOLD   = 0.65;
    private static final double SNOW_PEAKS_THRESHOLD = 0.70;

    private static final double HUMIDITY_THRESHOLD = 0.5;
    
    @Override
    public Biome resolve(double height, double humidity) {
        if (height < DEEP_WATER_THRESHOLD)  return Biome.DEEP_WATER;
        if (height < OCEAN_THRESHOLD)       return Biome.OCEAN;
        if (height < BEACH_THRESHOLD)       return Biome.BEACH;
        if (height > SNOW_PEAKS_THRESHOLD)  return Biome.SNOW_PEAKS;
        if (height > MOUNTAIN_THRESHOLD)    return Biome.MOUNTAIN;

        return humidity >= HUMIDITY_THRESHOLD ? Biome.FOREST : Biome.PLAINS;
    }
}
