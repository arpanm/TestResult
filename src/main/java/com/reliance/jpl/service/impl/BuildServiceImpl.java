package com.reliance.jpl.service.impl;

import com.reliance.jpl.domain.Build;
import com.reliance.jpl.repository.BuildRepository;
import com.reliance.jpl.service.BuildService;
import com.reliance.jpl.service.dto.BuildDTO;
import com.reliance.jpl.service.mapper.BuildMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Build}.
 */
@Service
@Transactional
public class BuildServiceImpl implements BuildService {

    private final Logger log = LoggerFactory.getLogger(BuildServiceImpl.class);

    private final BuildRepository buildRepository;

    private final BuildMapper buildMapper;

    public BuildServiceImpl(BuildRepository buildRepository, BuildMapper buildMapper) {
        this.buildRepository = buildRepository;
        this.buildMapper = buildMapper;
    }

    @Override
    public BuildDTO save(BuildDTO buildDTO) {
        log.debug("Request to save Build : {}", buildDTO);
        Build build = buildMapper.toEntity(buildDTO);
        build = buildRepository.save(build);
        return buildMapper.toDto(build);
    }

    @Override
    public BuildDTO update(BuildDTO buildDTO) {
        log.debug("Request to update Build : {}", buildDTO);
        Build build = buildMapper.toEntity(buildDTO);
        build = buildRepository.save(build);
        return buildMapper.toDto(build);
    }

    @Override
    public Optional<BuildDTO> partialUpdate(BuildDTO buildDTO) {
        log.debug("Request to partially update Build : {}", buildDTO);

        return buildRepository
            .findById(buildDTO.getId())
            .map(existingBuild -> {
                buildMapper.partialUpdate(existingBuild, buildDTO);

                return existingBuild;
            })
            .map(buildRepository::save)
            .map(buildMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BuildDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Builds");
        return buildRepository.findAll(pageable).map(buildMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BuildDTO> findOne(Long id) {
        log.debug("Request to get Build : {}", id);
        return buildRepository.findById(id).map(buildMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Build : {}", id);
        buildRepository.deleteById(id);
    }
}
