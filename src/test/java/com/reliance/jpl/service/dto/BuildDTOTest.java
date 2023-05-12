package com.reliance.jpl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jpl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BuildDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BuildDTO.class);
        BuildDTO buildDTO1 = new BuildDTO();
        buildDTO1.setId(1L);
        BuildDTO buildDTO2 = new BuildDTO();
        assertThat(buildDTO1).isNotEqualTo(buildDTO2);
        buildDTO2.setId(buildDTO1.getId());
        assertThat(buildDTO1).isEqualTo(buildDTO2);
        buildDTO2.setId(2L);
        assertThat(buildDTO1).isNotEqualTo(buildDTO2);
        buildDTO1.setId(null);
        assertThat(buildDTO1).isNotEqualTo(buildDTO2);
    }
}
