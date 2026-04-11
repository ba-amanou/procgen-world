package com.procgenworld.engine.export;

import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.procgenworld.engine.biome.Biome;
import com.procgenworld.engine.world.WorldData;

public class DefaultWorldExporter implements WorldExporter  {

    private static final Logger log = LoggerFactory.getLogger(DefaultWorldExporter.class);

    @Override
    public Path exportToPng(WorldData world, Path outputPath) {
        return exportImage(world.width(), world.height(), 
            (x,y) -> {
                int rgb =  biomeColor(world.biomes()[x][y], world.heightMap()[x][y]);
                return isBorderPixel(world.biomes(),x,y) ? darken(rgb, 0.7) : rgb;
            }, outputPath);
    }

    @Override
    public Path exportDebughumity(WorldData world, Path outputPath) throws WorldExportException {
        return exportImage(world.width(), world.height(), 
            (x,y) -> {
                return interpolate(0xFFFFFF, 0x1A3A8A, world.humidityMap()[x][y]);
            }, outputPath);
    }

    private Path exportImage(int width, int height, PixelRenderer renderer, Path outputPath) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, renderer.render(x,y));
            }
        }

        try {
            ImageIO.write(image,"png", outputPath.toFile());     
            log.info("World exported to {}", outputPath);
        } catch (IOException e) {
            throw new WorldExportException("Failed to write PNG to " + outputPath, e);
        }

        return outputPath;
    }

    @FunctionalInterface
    private interface PixelRenderer {
        int render(int x, int y);
    }

    private int biomeColor(Biome biome, double height) {
        return switch (biome) {
            case DEEP_WATER -> interpolate(0x0B2A4A, 0x1A5A8A, remap(height, 0.0,  0.35));
            case OCEAN      -> interpolate(0x1A5A8A, 0x2E86AB, remap(height, 0.35, 0.45));
            case BEACH      -> interpolate(0xE8D48B, 0xF5E6A3, remap(height, 0.45, 0.50));
            case PLAINS     -> interpolate(0x5A9E35, 0x8FCC55, remap(height, 0.50, 0.65));
            case FOREST     -> interpolate(0x1E4D1E, 0x3A7A3A, remap(height, 0.50, 0.65));
            case MOUNTAIN   -> interpolate(0x6B5A3E, 0xA08060, remap(height, 0.65, 0.70));
            case SNOW_PEAKS -> interpolate(0xD8D8D8, 0xFFFFFF, remap(height, 0.70, 1.0));
        };
    }

    private double remap(double value, double min, double max) {
        if (max <= min) return 0.0;
        return Math.clamp((value - min) / (max - min), 0.0, 1.0);
    }

    private int interpolate(int hexFrom, int hexTo, double t) {
        int r = lerp((hexFrom >> 16) & 0xFF, (hexTo >> 16) & 0xFF, t);
        int g = lerp((hexFrom >>  8) & 0xFF, (hexTo >>  8) & 0xFF, t);
        int b = lerp( hexFrom        & 0xFF,  hexTo        & 0xFF, t);
        return (r << 16) |(g << 8) | b;
    }

    private int lerp(int a, int b, double t) {
        return (int) Math.round(a + (b - a) * t);
    }

    private boolean isBorderPixel(Biome[][] biomes, int x, int y) {
        int width = biomes.length;
        int height = biomes[0].length;
        Biome current = biomes[x][y];
        if (x > 0 && biomes[x-1][y] != current)         return true;
        if (x < width-1 && biomes[x+1][y] != current)   return true;
        if (y > 0 && biomes[x][y-1] != current)         return true;
        if (y < height-1 && biomes[x][y+1] != current)  return true;
        return false;
    }

    private int darken(int rgb, double factor) {
        int r = Math.clamp((int)(((rgb >> 16) & 0xFF) * factor), 0, 255);
        int g = Math.clamp((int)(((rgb >> 8) & 0xFF) * factor), 0, 255);
        int b = Math.clamp((int)((rgb       & 0xFF) * factor), 0, 255);
        return (r << 16) | (g << 8) | b;
    }
}
