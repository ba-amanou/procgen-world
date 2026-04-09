package com.procgenworld.engine.world;

import java.util.Objects;

import com.procgenworld.engine.biome.Biome;

public record WorldData(
    Biome[][] biomes, 
    double[][] heightMap,
    double[][] humidityMap
) {
    public WorldData {
        Objects.requireNonNull(biomes, "biomes must not be null");
        Objects.requireNonNull(heightMap, "heightMap must not be null");
        Objects.requireNonNull(humidityMap, "humidityMap must not be null");
    }

    public int width() { return biomes.length; }
    public int height() { return biomes[0].length; }
}

