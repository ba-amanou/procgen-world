package com.procgenworld.engine.biome;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultBiomeResolverTest {

    private BiomeResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new DefaultBiomeResolver();
    }

    // ── DEEP_WATER ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("height strictly below 0.35 resolves to DEEP_WATER")
    void below_deep_water_threshold_returns_deep_water() {
        assertThat(resolver.resolve(0.0,  0.5)).isEqualTo(Biome.DEEP_WATER);
        assertThat(resolver.resolve(0.20, 0.0)).isEqualTo(Biome.DEEP_WATER);
        assertThat(resolver.resolve(0.34, 1.0)).isEqualTo(Biome.DEEP_WATER);
    }

    @Test
    @DisplayName("humidity has no effect in DEEP_WATER zone")
    void humidity_irrelevant_for_deep_water() {
        assertThat(resolver.resolve(0.20, 0.0)).isEqualTo(Biome.DEEP_WATER);
        assertThat(resolver.resolve(0.20, 1.0)).isEqualTo(Biome.DEEP_WATER);
    }

    // ── OCEAN ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("height in [0.35, 0.45[ resolves to OCEAN")
    void in_ocean_band_returns_ocean() {
        assertThat(resolver.resolve(0.35, 0.5)).isEqualTo(Biome.OCEAN);
        assertThat(resolver.resolve(0.40, 0.0)).isEqualTo(Biome.OCEAN);
        assertThat(resolver.resolve(0.44, 1.0)).isEqualTo(Biome.OCEAN);
    }

    @Test
    @DisplayName("humidity has no effect in OCEAN zone")
    void humidity_irrelevant_for_ocean() {
        assertThat(resolver.resolve(0.40, 0.0)).isEqualTo(Biome.OCEAN);
        assertThat(resolver.resolve(0.40, 1.0)).isEqualTo(Biome.OCEAN);
    }

    // ── BEACH ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("height in [0.45, 0.46[ resolves to BEACH")
    void in_beach_band_returns_beach() {
        assertThat(resolver.resolve(0.45, 0.5)).isEqualTo(Biome.BEACH);
        assertThat(resolver.resolve(0.45, 0.0)).isEqualTo(Biome.BEACH);
        assertThat(resolver.resolve(0.456, 1.0)).isEqualTo(Biome.BEACH);
    }

    @Test
    @DisplayName("humidity has no effect in BEACH zone")
    void humidity_irrelevant_for_beach() {
        assertThat(resolver.resolve(0.45, 0.0)).isEqualTo(Biome.BEACH);
        assertThat(resolver.resolve(0.45, 1.0)).isEqualTo(Biome.BEACH);
    }

    // ── PLAINS / FOREST ───────────────────────────────────────────────────────

    @Test
    @DisplayName("mid elevation with humidity below 0.5 resolves to PLAINS")
    void mid_elevation_dry_returns_plains() {
        assertThat(resolver.resolve(0.50, 0.0 )).isEqualTo(Biome.PLAINS);
        assertThat(resolver.resolve(0.57, 0.49)).isEqualTo(Biome.PLAINS);
        assertThat(resolver.resolve(0.65, 0.49)).isEqualTo(Biome.PLAINS);
    }

    @Test
    @DisplayName("mid elevation with humidity >= 0.5 resolves to FOREST")
    void mid_elevation_humid_returns_forest() {
        assertThat(resolver.resolve(0.50, 0.5 )).isEqualTo(Biome.FOREST);
        assertThat(resolver.resolve(0.57, 0.75)).isEqualTo(Biome.FOREST);
        assertThat(resolver.resolve(0.65, 1.0 )).isEqualTo(Biome.FOREST);
    }

    // ── MOUNTAIN ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("height in ]0.65, 0.69] resolves to MOUNTAIN")
    void in_mountain_band_returns_mountain() {
        assertThat(resolver.resolve(0.651, 0.0)).isEqualTo(Biome.MOUNTAIN);
        assertThat(resolver.resolve(0.67,  0.5)).isEqualTo(Biome.MOUNTAIN);
        assertThat(resolver.resolve(0.69,  1.0)).isEqualTo(Biome.MOUNTAIN);
    }

    @Test
    @DisplayName("humidity has no effect in MOUNTAIN zone")
    void humidity_irrelevant_for_mountain() {
        assertThat(resolver.resolve(0.67, 0.0)).isEqualTo(Biome.MOUNTAIN);
        assertThat(resolver.resolve(0.67, 1.0)).isEqualTo(Biome.MOUNTAIN);
    }

    // ── SNOW_PEAKS ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("height strictly above 0.70 resolves to SNOW_PEAKS")
    void above_snow_threshold_returns_snow_peaks() {
        assertThat(resolver.resolve(0.71, 0.0)).isEqualTo(Biome.SNOW_PEAKS);
        assertThat(resolver.resolve(0.85, 0.5)).isEqualTo(Biome.SNOW_PEAKS);
        assertThat(resolver.resolve(1.0,  1.0)).isEqualTo(Biome.SNOW_PEAKS);
    }

    @Test
    @DisplayName("humidity has no effect in SNOW_PEAKS zone")
    void humidity_irrelevant_for_snow_peaks() {
        assertThat(resolver.resolve(0.85, 0.0)).isEqualTo(Biome.SNOW_PEAKS);
        assertThat(resolver.resolve(0.85, 1.0)).isEqualTo(Biome.SNOW_PEAKS);
    }

    // ── FRONTIÈRES EXACTES ────────────────────────────────────────────────────

    @Test
    @DisplayName("exact threshold values resolve to the correct upper biome")
    void exact_thresholds_resolve_correctly() {
        assertThat(resolver.resolve(0.35, 0.5)).isEqualTo(Biome.OCEAN);
        assertThat(resolver.resolve(0.45, 0.5)).isEqualTo(Biome.BEACH);
        assertThat(resolver.resolve(0.50, 0.0)).isEqualTo(Biome.PLAINS);
        assertThat(resolver.resolve(0.50, 0.5)).isEqualTo(Biome.FOREST);
    }
}
