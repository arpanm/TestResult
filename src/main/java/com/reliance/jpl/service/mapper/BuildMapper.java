package com.reliance.jpl.service.mapper;

import com.reliance.jpl.domain.Build;
import com.reliance.jpl.service.dto.BuildDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Build} and its DTO {@link BuildDTO}.
 */
@Mapper(componentModel = "spring")
public interface BuildMapper extends EntityMapper<BuildDTO, Build> {}
