package com.procgenworld.engine.export;

import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.procgenworld.engine.biome.Biome;

public class DefaultWorldExporter implements WorldExporter  {

    private static final Logger log = LoggerFactory.getLogger(DefaultWorldExporter.class);

    @Override
    public Path exportToPng(Biome[][] biomeGrid, Path outputPath) {
        int width = biomeGrid.length;
        int height = biomeGrid[0].length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y,toRgb(biomeGrid[x][y]));
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

    private int toRgb(Biome biome) {
        return switch (biome) {
            case DEEP_WATER -> 0x0B3D66; // dark blue
            case OCEAN      -> 0x1A6BA0; // deep blue
            case BEACH      -> 0xF5E6A3; // sandy yellow
            case PLAINS     -> 0x7EC850; // light green
            case FOREST     -> 0x2D6A2D; // dark green
            case MOUNTAIN   -> 0x8B7355; // brown-grey
            case SNOW_PEAKS -> 0xF0F0F0; // near white
        };
    }
}
