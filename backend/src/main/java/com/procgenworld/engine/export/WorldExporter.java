package com.procgenworld.engine.export;

import java.nio.file.Path;

import com.procgenworld.engine.biome.Biome;

public interface WorldExporter {
    /**
     * Export a 2D biome grid to a PNG file.
     * 
     * @param biomeGrid the resolved biome map, dimensions [width][height]
     * @param outputPath target file path 
     * @return the path of the written file
     * @throws WorldExportException 
     */
    Path exportToPng(Biome[][] biomeGrid, Path outputPath) throws WorldExportException;
}
