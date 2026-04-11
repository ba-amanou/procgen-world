package com.procgenworld.engine.export;

import java.nio.file.Path;

import com.procgenworld.engine.world.WorldData;

public interface WorldExporter {
    /**
     * Export the world to a PNG file.
     * 
     * @param world the world to render
     * @param outputPath target file path 
     * @return the path of the written file
     * @throws WorldExportException 
     */
    Path exportToPng(WorldData world, Path outputPath) throws WorldExportException;

    Path exportDebughumity(WorldData world, Path outputPath) throws WorldExportException;
}
