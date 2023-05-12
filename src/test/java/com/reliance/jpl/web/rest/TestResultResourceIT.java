package com.reliance.jpl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jpl.IntegrationTest;
import com.reliance.jpl.domain.TestResult;
import com.reliance.jpl.repository.TestResultRepository;
import com.reliance.jpl.service.dto.TestResultDTO;
import com.reliance.jpl.service.mapper.TestResultMapper;
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
 * Integration tests for the {@link TestResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestResultResourceIT {

    private static final Float DEFAULT_PAGE_LOAD = 1F;
    private static final Float UPDATED_PAGE_LOAD = 2F;

    private static final Float DEFAULT_TIME_TO_INTERACTIVE = 1F;
    private static final Float UPDATED_TIME_TO_INTERACTIVE = 2F;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_UPDATED_BY = 1L;
    private static final Long UPDATED_UPDATED_BY = 2L;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/test-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TestResultMapper testResultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestResultMockMvc;

    private TestResult testResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestResult createEntity(EntityManager em) {
        TestResult testResult = new TestResult()
            .pageLoad(DEFAULT_PAGE_LOAD)
            .timeToInteractive(DEFAULT_TIME_TO_INTERACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
        return testResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestResult createUpdatedEntity(EntityManager em) {
        TestResult testResult = new TestResult()
            .pageLoad(UPDATED_PAGE_LOAD)
            .timeToInteractive(UPDATED_TIME_TO_INTERACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
        return testResult;
    }

    @BeforeEach
    public void initTest() {
        testResult = createEntity(em);
    }

    @Test
    @Transactional
    void createTestResult() throws Exception {
        int databaseSizeBeforeCreate = testResultRepository.findAll().size();
        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);
        restTestResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testResultDTO)))
            .andExpect(status().isCreated());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeCreate + 1);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
        assertThat(testTestResult.getPageLoad()).isEqualTo(DEFAULT_PAGE_LOAD);
        assertThat(testTestResult.getTimeToInteractive()).isEqualTo(DEFAULT_TIME_TO_INTERACTIVE);
        assertThat(testTestResult.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestResult.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testTestResult.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTestResult.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createTestResultWithExistingId() throws Exception {
        // Create the TestResult with an existing ID
        testResult.setId(1L);
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        int databaseSizeBeforeCreate = testResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestResults() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get all the testResultList
        restTestResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageLoad").value(hasItem(DEFAULT_PAGE_LOAD.doubleValue())))
            .andExpect(jsonPath("$.[*].timeToInteractive").value(hasItem(DEFAULT_TIME_TO_INTERACTIVE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getTestResult() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        // Get the testResult
        restTestResultMockMvc
            .perform(get(ENTITY_API_URL_ID, testResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testResult.getId().intValue()))
            .andExpect(jsonPath("$.pageLoad").value(DEFAULT_PAGE_LOAD.doubleValue()))
            .andExpect(jsonPath("$.timeToInteractive").value(DEFAULT_TIME_TO_INTERACTIVE.doubleValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTestResult() throws Exception {
        // Get the testResult
        restTestResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestResult() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();

        // Update the testResult
        TestResult updatedTestResult = testResultRepository.findById(testResult.getId()).get();
        // Disconnect from session so that the updates on updatedTestResult are not directly saved in db
        em.detach(updatedTestResult);
        updatedTestResult
            .pageLoad(UPDATED_PAGE_LOAD)
            .timeToInteractive(UPDATED_TIME_TO_INTERACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
        TestResultDTO testResultDTO = testResultMapper.toDto(updatedTestResult);

        restTestResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
        assertThat(testTestResult.getPageLoad()).isEqualTo(UPDATED_PAGE_LOAD);
        assertThat(testTestResult.getTimeToInteractive()).isEqualTo(UPDATED_TIME_TO_INTERACTIVE);
        assertThat(testTestResult.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestResult.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTestResult.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestResult.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestResultWithPatch() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();

        // Update the testResult using partial update
        TestResult partialUpdatedTestResult = new TestResult();
        partialUpdatedTestResult.setId(testResult.getId());

        partialUpdatedTestResult.timeToInteractive(UPDATED_TIME_TO_INTERACTIVE).createdOn(UPDATED_CREATED_ON).updatedOn(UPDATED_UPDATED_ON);

        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestResult))
            )
            .andExpect(status().isOk());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
        assertThat(testTestResult.getPageLoad()).isEqualTo(DEFAULT_PAGE_LOAD);
        assertThat(testTestResult.getTimeToInteractive()).isEqualTo(UPDATED_TIME_TO_INTERACTIVE);
        assertThat(testTestResult.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestResult.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTestResult.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTestResult.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateTestResultWithPatch() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();

        // Update the testResult using partial update
        TestResult partialUpdatedTestResult = new TestResult();
        partialUpdatedTestResult.setId(testResult.getId());

        partialUpdatedTestResult
            .pageLoad(UPDATED_PAGE_LOAD)
            .timeToInteractive(UPDATED_TIME_TO_INTERACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestResult))
            )
            .andExpect(status().isOk());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
        TestResult testTestResult = testResultList.get(testResultList.size() - 1);
        assertThat(testTestResult.getPageLoad()).isEqualTo(UPDATED_PAGE_LOAD);
        assertThat(testTestResult.getTimeToInteractive()).isEqualTo(UPDATED_TIME_TO_INTERACTIVE);
        assertThat(testTestResult.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestResult.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTestResult.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestResult.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestResult() throws Exception {
        int databaseSizeBeforeUpdate = testResultRepository.findAll().size();
        testResult.setId(count.incrementAndGet());

        // Create the TestResult
        TestResultDTO testResultDTO = testResultMapper.toDto(testResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testResultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestResult in the database
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestResult() throws Exception {
        // Initialize the database
        testResultRepository.saveAndFlush(testResult);

        int databaseSizeBeforeDelete = testResultRepository.findAll().size();

        // Delete the testResult
        restTestResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, testResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestResult> testResultList = testResultRepository.findAll();
        assertThat(testResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
