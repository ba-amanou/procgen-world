package com.procgenworld.engine.heightmap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DefaultHeightMapGeneratorTest {
    
    private HeightMapGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultHeightMapGenerator(HeightMapConfig.defaults());
    }

    @Test
    @DisplayName("Same seed produces identical map")
    void same_seed_produces_identical_map() {
        double[][] map1 = generator.generate(42L, 64, 64);
        double[][] map2 = generator.generate(42L, 64, 64);

        assertThat(map1).isEqualTo(map2);
    }

    @Test
    @DisplayName("Different seeds produce differents maps")
    void differents_seeds_produce_differents_maps() {
        double[][] map1 = generator.generate(42L, 64, 64);
        double[][] map2 = generator.generate(43L, 64, 64);

        assertThat(map1).isNotEqualTo(map2);
    }
    
    @Test
    @DisplayName("Output dimensions match requested size")
    void output_dimensions_match_requested_size() {
        double[][] map = generator.generate(42L, 128, 64);

        assertThat(map.length).isEqualTo(128);
        assertThat(map[0].length).isEqualTo(64);
    }

    @Test
    @DisplayName("All values are normalized between 0 and 1")
    void all_values_are_normalized_between_0_and_1() {
        double[][] map = generator.generate(42L, 64, 64);

        for (double[] column : map) {
            for (double value : column) {
                assertThat(value).isBetween(0.0, 1.0);
            }
        }
    }

    @Test
    @DisplayName("Invalid config throws exception")
    void invalid_config_throws_exception() {
        assertThatThrownBy(() -> new HeightMapConfig(-1.0, 4, 0.5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("scale must be positive");
    }    

}
