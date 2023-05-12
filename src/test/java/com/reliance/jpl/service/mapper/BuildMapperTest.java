package com.reliance.jpl.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuildMapperTest {

    private BuildMapper buildMapper;

    @BeforeEach
    public void setUp() {
        buildMapper = new BuildMapperImpl();
    }
}
