package com.procgenworld.engine.noisemap;

/**
 * Immutable configuration for height map generation.
 * Controls the visual appearance of the generated terrain.
 * 
 * @param scale       zoom level — higher = more zoomed out, smoother terrain
 * @param octaves     number of noise layers — more = more detail
 * @param persistence amplitude multiplier per octave — higher = rougher terrain
 */
public record NoiseMapConfig(double scale, int octaves, double persistence) {
    
    public NoiseMapConfig {
        if (scale <= 0)                         throw new IllegalArgumentException("scale must be positive");
        if (octaves < 1)                        throw new IllegalArgumentException("octave muse be >= 1");
        if (persistence <=0 || persistence >=1) throw new IllegalArgumentException("persistence must be in (0.0,1.0)");
    }

    public static NoiseMapConfig defaults() {
        return new NoiseMapConfig(20.0, 4, 0.5);
    }
}
