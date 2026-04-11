package com.procgenworld.engine.noise;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PerlinNoiseTest {
    
    private PerlinNoise noise;

    @BeforeEach
    void setUp() {
        noise = new PerlinNoise(42L);
    }

    @Test
    @DisplayName("Same seed produces identical values")
    void same_seed_should_produces_identical_values() {
        PerlinNoise noise1 = new PerlinNoise(42L);
        PerlinNoise noise2 = new PerlinNoise(42L);

        assertThat(noise1.octave(1.5, 2.3, 4, 0.5))
            .isEqualTo(noise2.octave(1.5, 2.3, 4, 0.5));
    }

    @Test
    @DisplayName("Different seeds produce different values")
    void different_seed_should_produce_different_values() {
        PerlinNoise other = new PerlinNoise(43L);

        assertThat(noise.octave(1.5, 2.3, 4, 0.5))
            .isNotEqualTo(other.octave(1.5, 2.3, 4, 0.5));
    }

    @Test
    @DisplayName("Octave output is normalized to [0.0, 1.0]")
    void octave_output_should_be_normalized() {
        // Sample a grid of points and verify all values are in range
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                double value = noise.octave(x * 0.1, y * 0.1, 4, 0.5);
                assertThat(value)
                    .as("Value at (%d, %d) should be in [0.0, 1.0]", x, y)
                    .isBetween(0.0, 1.0);
            }
        }
    }

    @Test
    @DisplayName("Adjacent points have similar values (spatial coherence)")
    void adjacent_points_should_have_similar_values() {
        double v1 = noise.octave(0.0, 0.0, 4, 0.5);
        double v2 = noise.octave(0.01, 0.0, 4, 0.5);

        // Two very close points should not differ by more than 0.1
        assertThat(Math.abs(v1 - v2)).isLessThan(0.1);
    }

    @Test
    @DisplayName("Distant points can have very different values")
    void distant_points_can_differ() {
        // Not guaranteed, but with seed 42 these points happen to differ
        // this test documents the non-trivial nature of the output
        double v1 = noise.octave(0.3, 0.7, 4, 0.5);
        double v2 = noise.octave(50.3, 73.7, 4, 0.5);

        assertThat(v1).isNotCloseTo(v2, within(0.01));
    }

    @Test
    @DisplayName("Adding octaves changes the output value")
    void adding_octaves_changes_output() {
        double x = 1.3, y = 2.7;

        double with1Octave = noise.octave(x, y, 1, 0.5);
        double with2Octaves = noise.octave(x, y, 2, 0.5);
        double with4Octaves = noise.octave(x, y, 4, 0.5);

        assertThat(with1Octave).isNotEqualTo(with2Octaves);
        assertThat(with2Octaves).isNotEqualTo(with4Octaves);
    }

    @Test
    @DisplayName("Higher persistence increases amplitude contribution")
    void higher_persistence_increases_variation() {
        double sum1 = sumGridDifferences(4, 0.2); // low persistence
        double sum2 = sumGridDifferences(4, 0.8); // high persistence

        assertThat(sum2).isGreaterThan(sum1);
    }

    private double sumGridDifferences(int octaves, double persistence) {
        double sum = 0.0;
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                double v1 = noise.octave(x * 0.1, y * 0.1, octaves, persistence);
                double v2 = noise.octave((x + 1) * 0.1, y * 0.1, octaves, persistence);
                sum += Math.abs(v1 - v2);
            }
        }
        return sum;
    }
}
