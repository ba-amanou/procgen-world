package com.procgenworld.engine;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.procgenworld.engine.biome.Biome;
import com.procgenworld.engine.biome.BiomeResolver;
import com.procgenworld.engine.biome.DefaultBiomeResolver;
import com.procgenworld.engine.export.DefaultWorldExporter;
import com.procgenworld.engine.export.WorldExporter;
import com.procgenworld.engine.noisemap.DefaultNoiseMapGenerator;
import com.procgenworld.engine.noisemap.NoiseMapConfig;
import com.procgenworld.engine.noisemap.NoiseMapGenerator;
import com.procgenworld.engine.world.WorldData;

public class VisualWordTest {
    
    public static void main(String[] args) {
        long seed = 41L;
        int width = 512;
        int height = 512;

        NoiseMapConfig config = new NoiseMapConfig(200.0, 6, 0.5);

        NoiseMapGenerator heightGen = new DefaultNoiseMapGenerator(config);
        double[][] heightMap = heightGen.generate(seed, width, height);

        NoiseMapGenerator humidityGen = new DefaultNoiseMapGenerator(config);
        double[][] humidityMap = humidityGen.generate(seed + 42L, width, height);

        BiomeResolver resolver = new DefaultBiomeResolver();
        Biome[][] biomeGrid = new Biome[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                biomeGrid[x][y] = resolver.resolve(heightMap[x][y], humidityMap[x][y]);
            }
        }

        WorldExporter exporter = new DefaultWorldExporter();
        Path outputWorld = Path.of("world_seed" + seed + ".png");
        Path outputHumidity = Path.of("world_seed" + seed + "_humidity.png");

        WorldData world = new WorldData(biomeGrid, heightMap, humidityMap);
        exporter.exportToPng(world, outputWorld);
        exporter.exportDebughumity(world, outputHumidity);


        System.out.println("Generated: " + outputWorld.toAbsolutePath());
        System.out.println("Generated: " + outputHumidity.toAbsolutePath());

        Map<Biome, Long> distribution = Arrays.stream(biomeGrid)
            .flatMap(Arrays::stream)
            .collect(Collectors.groupingBy(b -> b, Collectors.counting()));

        long total = (long) width * height;
        distribution.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> System.out.printf("%-12s : %5.1f%%%n",
                    e.getKey(), e.getValue() * 100.0 / total));
    }


}
