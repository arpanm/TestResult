package com.reliance.jpl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jpl.IntegrationTest;
import com.reliance.jpl.domain.Build;
import com.reliance.jpl.repository.BuildRepository;
import com.reliance.jpl.service.dto.BuildDTO;
import com.reliance.jpl.service.mapper.BuildMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BuildResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BuildResourceIT {

    private static final String DEFAULT_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH = "BBBBBBBBBB";

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_UPDATED_BY = 1L;
    private static final Long UPDATED_UPDATED_BY = 2L;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/builds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private BuildMapper buildMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBuildMockMvc;

    private Build build;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Build createEntity(EntityManager em) {
        Build build = new Build()
            .branch(DEFAULT_BRANCH)
            .tag(DEFAULT_TAG)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
        return build;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Build createUpdatedEntity(EntityManager em) {
        Build build = new Build()
            .branch(UPDATED_BRANCH)
            .tag(UPDATED_TAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
        return build;
    }

    @BeforeEach
    public void initTest() {
        build = createEntity(em);
    }

    @Test
    @Transactional
    void createBuild() throws Exception {
        int databaseSizeBeforeCreate = buildRepository.findAll().size();
        // Create the Build
        BuildDTO buildDTO = buildMapper.toDto(build);
        restBuildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildDTO)))
            .andExpect(status().isCreated());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeCreate + 1);
        Build testBuild = buildList.get(buildList.size() - 1);
        assertThat(testBuild.getBranch()).isEqualTo(DEFAULT_BRANCH);
        assertThat(testBuild.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testBuild.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBuild.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testBuild.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testBuild.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createBuildWithExistingId() throws Exception {
        // Create the Build with an existing ID
        build.setId(1L);
        BuildDTO buildDTO = buildMapper.toDto(build);

        int databaseSizeBeforeCreate = buildRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBuilds() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

        // Get all the buildList
        restBuildMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(build.getId().intValue())))
            .andExpect(jsonPath("$.[*].branch").value(hasItem(DEFAULT_BRANCH)))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getBuild() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

        // Get the build
        restBuildMockMvc
            .perform(get(ENTITY_API_URL_ID, build.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(build.getId().intValue()))
            .andExpect(jsonPath("$.branch").value(DEFAULT_BRANCH))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBuild() throws Exception {
        // Get the build
        restBuildMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBuild() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

        int databaseSizeBeforeUpdate = buildRepository.findAll().size();

        // Update the build
        Build updatedBuild = buildRepository.findById(build.getId()).get();
        // Disconnect from session so that the updates on updatedBuild are not directly saved in db
        em.detach(updatedBuild);
        updatedBuild
            .branch(UPDATED_BRANCH)
            .tag(UPDATED_TAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
        BuildDTO buildDTO = buildMapper.toDto(updatedBuild);

        restBuildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, buildDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buildDTO))
            )
            .andExpect(status().isOk());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeUpdate);
        Build testBuild = buildList.get(buildList.size() - 1);
        assertThat(testBuild.getBranch()).isEqualTo(UPDATED_BRANCH);
        assertThat(testBuild.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testBuild.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBuild.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testBuild.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testBuild.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingBuild() throws Exception {
        int databaseSizeBeforeUpdate = buildRepository.findAll().size();
        build.setId(count.incrementAndGet());

        // Create the Build
        BuildDTO buildDTO = buildMapper.toDto(build);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, buildDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buildDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBuild() throws Exception {
        int databaseSizeBeforeUpdate = buildRepository.findAll().size();
        build.setId(count.incrementAndGet());

        // Create the Build
        BuildDTO buildDTO = buildMapper.toDto(build);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buildDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBuild() throws Exception {
        int databaseSizeBeforeUpdate = buildRepository.findAll().size();
        build.setId(count.incrementAndGet());

        // Create the Build
        BuildDTO buildDTO = buildMapper.toDto(build);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBuildWithPatch() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

        int databaseSizeBeforeUpdate = buildRepository.findAll().size();

        // Update the build using partial update
        Build partialUpdatedBuild = new Build();
        partialUpdatedBuild.setId(build.getId());

        partialUpdatedBuild.branch(UPDATED_BRANCH).createdBy(UPDATED_CREATED_BY);

        restBuildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBuild.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuild))
            )
            .andExpect(status().isOk());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeUpdate);
        Build testBuild = buildList.get(buildList.size() - 1);
        assertThat(testBuild.getBranch()).isEqualTo(UPDATED_BRANCH);
        assertThat(testBuild.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testBuild.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBuild.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testBuild.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testBuild.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateBuildWithPatch() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

        int databaseSizeBeforeUpdate = buildRepository.findAll().size();

        // Update the build using partial update
        Build partialUpdatedBuild = new Build();
        partialUpdatedBuild.setId(build.getId());

        partialUpdatedBuild
            .branch(UPDATED_BRANCH)
            .tag(UPDATED_TAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restBuildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBuild.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuild))
            )
            .andExpect(status().isOk());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeUpdate);
        Build testBuild = buildList.get(buildList.size() - 1);
        assertThat(testBuild.getBranch()).isEqualTo(UPDATED_BRANCH);
        assertThat(testBuild.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testBuild.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBuild.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testBuild.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testBuild.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingBuild() throws Exception {
        int databaseSizeBeforeUpdate = buildRepository.findAll().size();
        build.setId(count.incrementAndGet());

        // Create the Build
        BuildDTO buildDTO = buildMapper.toDto(build);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, buildDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(buildDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBuild() throws Exception {
        int databaseSizeBeforeUpdate = buildRepository.findAll().size();
        build.setId(count.incrementAndGet());

        // Create the Build
        BuildDTO buildDTO = buildMapper.toDto(build);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(buildDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBuild() throws Exception {
        int databaseSizeBeforeUpdate = buildRepository.findAll().size();
        build.setId(count.incrementAndGet());

        // Create the Build
        BuildDTO buildDTO = buildMapper.toDto(build);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(buildDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Build in the database
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBuild() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

        int databaseSizeBeforeDelete = buildRepository.findAll().size();

        // Delete the build
        restBuildMockMvc
            .perform(delete(ENTITY_API_URL_ID, build.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Build> buildList = buildRepository.findAll();
        assertThat(buildList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
