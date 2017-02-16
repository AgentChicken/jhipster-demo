package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterdemoApp;

import com.mycompany.myapp.domain.Syndicate;
import com.mycompany.myapp.repository.SyndicateRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SyndicateResource REST controller.
 *
 * @see SyndicateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterdemoApp.class)
public class SyndicateResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WORKPLACES = "AAAAAAAAAA";
    private static final String UPDATED_WORKPLACES = "BBBBBBBBBB";

    private static final String DEFAULT_DELEGATES = "AAAAAAAAAA";
    private static final String UPDATED_DELEGATES = "BBBBBBBBBB";

    @Autowired
    private SyndicateRepository syndicateRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSyndicateMockMvc;

    private Syndicate syndicate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            SyndicateResource syndicateResource = new SyndicateResource(syndicateRepository);
        this.restSyndicateMockMvc = MockMvcBuilders.standaloneSetup(syndicateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Syndicate createEntity(EntityManager em) {
        Syndicate syndicate = new Syndicate()
                .name(DEFAULT_NAME)
                .workplaces(DEFAULT_WORKPLACES)
                .delegates(DEFAULT_DELEGATES);
        return syndicate;
    }

    @Before
    public void initTest() {
        syndicate = createEntity(em);
    }

    @Test
    @Transactional
    public void createSyndicate() throws Exception {
        int databaseSizeBeforeCreate = syndicateRepository.findAll().size();

        // Create the Syndicate

        restSyndicateMockMvc.perform(post("/api/syndicates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(syndicate)))
            .andExpect(status().isCreated());

        // Validate the Syndicate in the database
        List<Syndicate> syndicateList = syndicateRepository.findAll();
        assertThat(syndicateList).hasSize(databaseSizeBeforeCreate + 1);
        Syndicate testSyndicate = syndicateList.get(syndicateList.size() - 1);
        assertThat(testSyndicate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSyndicate.getWorkplaces()).isEqualTo(DEFAULT_WORKPLACES);
        assertThat(testSyndicate.getDelegates()).isEqualTo(DEFAULT_DELEGATES);
    }

    @Test
    @Transactional
    public void createSyndicateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = syndicateRepository.findAll().size();

        // Create the Syndicate with an existing ID
        Syndicate existingSyndicate = new Syndicate();
        existingSyndicate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSyndicateMockMvc.perform(post("/api/syndicates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSyndicate)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Syndicate> syndicateList = syndicateRepository.findAll();
        assertThat(syndicateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSyndicates() throws Exception {
        // Initialize the database
        syndicateRepository.saveAndFlush(syndicate);

        // Get all the syndicateList
        restSyndicateMockMvc.perform(get("/api/syndicates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(syndicate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].workplaces").value(hasItem(DEFAULT_WORKPLACES.toString())))
            .andExpect(jsonPath("$.[*].delegates").value(hasItem(DEFAULT_DELEGATES.toString())));
    }

    @Test
    @Transactional
    public void getSyndicate() throws Exception {
        // Initialize the database
        syndicateRepository.saveAndFlush(syndicate);

        // Get the syndicate
        restSyndicateMockMvc.perform(get("/api/syndicates/{id}", syndicate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(syndicate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.workplaces").value(DEFAULT_WORKPLACES.toString()))
            .andExpect(jsonPath("$.delegates").value(DEFAULT_DELEGATES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSyndicate() throws Exception {
        // Get the syndicate
        restSyndicateMockMvc.perform(get("/api/syndicates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSyndicate() throws Exception {
        // Initialize the database
        syndicateRepository.saveAndFlush(syndicate);
        int databaseSizeBeforeUpdate = syndicateRepository.findAll().size();

        // Update the syndicate
        Syndicate updatedSyndicate = syndicateRepository.findOne(syndicate.getId());
        updatedSyndicate
                .name(UPDATED_NAME)
                .workplaces(UPDATED_WORKPLACES)
                .delegates(UPDATED_DELEGATES);

        restSyndicateMockMvc.perform(put("/api/syndicates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSyndicate)))
            .andExpect(status().isOk());

        // Validate the Syndicate in the database
        List<Syndicate> syndicateList = syndicateRepository.findAll();
        assertThat(syndicateList).hasSize(databaseSizeBeforeUpdate);
        Syndicate testSyndicate = syndicateList.get(syndicateList.size() - 1);
        assertThat(testSyndicate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSyndicate.getWorkplaces()).isEqualTo(UPDATED_WORKPLACES);
        assertThat(testSyndicate.getDelegates()).isEqualTo(UPDATED_DELEGATES);
    }

    @Test
    @Transactional
    public void updateNonExistingSyndicate() throws Exception {
        int databaseSizeBeforeUpdate = syndicateRepository.findAll().size();

        // Create the Syndicate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSyndicateMockMvc.perform(put("/api/syndicates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(syndicate)))
            .andExpect(status().isCreated());

        // Validate the Syndicate in the database
        List<Syndicate> syndicateList = syndicateRepository.findAll();
        assertThat(syndicateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSyndicate() throws Exception {
        // Initialize the database
        syndicateRepository.saveAndFlush(syndicate);
        int databaseSizeBeforeDelete = syndicateRepository.findAll().size();

        // Get the syndicate
        restSyndicateMockMvc.perform(delete("/api/syndicates/{id}", syndicate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Syndicate> syndicateList = syndicateRepository.findAll();
        assertThat(syndicateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Syndicate.class);
    }
}
