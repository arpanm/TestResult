package com.reliance.jpl.service;

import com.reliance.jpl.service.dto.BuildDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.reliance.jpl.domain.Build}.
 */
public interface BuildService {
    /**
     * Save a build.
     *
     * @param buildDTO the entity to save.
     * @return the persisted entity.
     */
    BuildDTO save(BuildDTO buildDTO);

    /**
     * Updates a build.
     *
     * @param buildDTO the entity to update.
     * @return the persisted entity.
     */
    BuildDTO update(BuildDTO buildDTO);

    /**
     * Partially updates a build.
     *
     * @param buildDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BuildDTO> partialUpdate(BuildDTO buildDTO);

    /**
     * Get all the builds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BuildDTO> findAll(Pageable pageable);

    /**
     * Get the "id" build.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BuildDTO> findOne(Long id);

    /**
     * Delete the "id" build.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
