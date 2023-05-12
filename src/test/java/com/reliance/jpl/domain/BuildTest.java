package com.reliance.jpl.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jpl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BuildTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Build.class);
        Build build1 = new Build();
        build1.setId(1L);
        Build build2 = new Build();
        build2.setId(build1.getId());
        assertThat(build1).isEqualTo(build2);
        build2.setId(2L);
        assertThat(build1).isNotEqualTo(build2);
        build1.setId(null);
        assertThat(build1).isNotEqualTo(build2);
    }
}
