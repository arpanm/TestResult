package com.reliance.jpl.web.rest;

import com.reliance.jpl.repository.BuildRepository;
import com.reliance.jpl.service.BuildService;
import com.reliance.jpl.service.dto.BuildDTO;
import com.reliance.jpl.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.reliance.jpl.domain.Build}.
 */
@RestController
@RequestMapping("/api")
public class BuildResource {

    private final Logger log = LoggerFactory.getLogger(BuildResource.class);

    private static final String ENTITY_NAME = "build";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BuildService buildService;

    private final BuildRepository buildRepository;

    public BuildResource(BuildService buildService, BuildRepository buildRepository) {
        this.buildService = buildService;
        this.buildRepository = buildRepository;
    }

    /**
     * {@code POST  /builds} : Create a new build.
     *
     * @param buildDTO the buildDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new buildDTO, or with status {@code 400 (Bad Request)} if the build has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/builds")
    public ResponseEntity<BuildDTO> createBuild(@RequestBody BuildDTO buildDTO) throws URISyntaxException {
        log.debug("REST request to save Build : {}", buildDTO);
        if (buildDTO.getId() != null) {
            throw new BadRequestAlertException("A new build cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BuildDTO result = buildService.save(buildDTO);
        return ResponseEntity
            .created(new URI("/api/builds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /builds/:id} : Updates an existing build.
     *
     * @param id the id of the buildDTO to save.
     * @param buildDTO the buildDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated buildDTO,
     * or with status {@code 400 (Bad Request)} if the buildDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the buildDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/builds/{id}")
    public ResponseEntity<BuildDTO> updateBuild(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BuildDTO buildDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Build : {}, {}", id, buildDTO);
        if (buildDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, buildDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!buildRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BuildDTO result = buildService.update(buildDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, buildDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /builds/:id} : Partial updates given fields of an existing build, field will ignore if it is null
     *
     * @param id the id of the buildDTO to save.
     * @param buildDTO the buildDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated buildDTO,
     * or with status {@code 400 (Bad Request)} if the buildDTO is not valid,
     * or with status {@code 404 (Not Found)} if the buildDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the buildDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/builds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BuildDTO> partialUpdateBuild(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BuildDTO buildDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Build partially : {}, {}", id, buildDTO);
        if (buildDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, buildDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!buildRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BuildDTO> result = buildService.partialUpdate(buildDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, buildDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /builds} : get all the builds.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of builds in body.
     */
    @GetMapping("/builds")
    public ResponseEntity<List<BuildDTO>> getAllBuilds(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Builds");
        Page<BuildDTO> page = buildService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /builds/:id} : get the "id" build.
     *
     * @param id the id of the buildDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the buildDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/builds/{id}")
    public ResponseEntity<BuildDTO> getBuild(@PathVariable Long id) {
        log.debug("REST request to get Build : {}", id);
        Optional<BuildDTO> buildDTO = buildService.findOne(id);
        return ResponseUtil.wrapOrNotFound(buildDTO);
    }

    /**
     * {@code DELETE  /builds/:id} : delete the "id" build.
     *
     * @param id the id of the buildDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/builds/{id}")
    public ResponseEntity<Void> deleteBuild(@PathVariable Long id) {
        log.debug("REST request to delete Build : {}", id);
        buildService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
