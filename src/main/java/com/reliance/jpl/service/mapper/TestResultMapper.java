package com.reliance.jpl.service.mapper;

import com.reliance.jpl.domain.Build;
import com.reliance.jpl.domain.TestResult;
import com.reliance.jpl.service.dto.BuildDTO;
import com.reliance.jpl.service.dto.TestResultDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TestResult} and its DTO {@link TestResultDTO}.
 */
@Mapper(componentModel = "spring")
public interface TestResultMapper extends EntityMapper<TestResultDTO, TestResult> {
    @Mapping(target = "build", source = "build", qualifiedByName = "buildId")
    TestResultDTO toDto(TestResult s);

    @Named("buildId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BuildDTO toDtoBuildId(Build build);
}
