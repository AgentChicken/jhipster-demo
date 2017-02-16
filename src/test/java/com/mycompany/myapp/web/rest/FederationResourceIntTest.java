package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterdemoApp;

import com.mycompany.myapp.domain.Federation;
import com.mycompany.myapp.repository.FederationRepository;
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
 * Test class for the FederationResource REST controller.
 *
 * @see FederationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterdemoApp.class)
public class FederationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYNDICATES = "AAAAAAAAAA";
    private static final String UPDATED_SYNDICATES = "BBBBBBBBBB";

    @Autowired
    private FederationRepository federationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFederationMockMvc;

    private Federation federation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            FederationResource federationResource = new FederationResource(federationRepository);
        this.restFederationMockMvc = MockMvcBuilders.standaloneSetup(federationResource)
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
    public static Federation createEntity(EntityManager em) {
        Federation federation = new Federation()
                .name(DEFAULT_NAME)
                .syndicates(DEFAULT_SYNDICATES);
        return federation;
    }

    @Before
    public void initTest() {
        federation = createEntity(em);
    }

    @Test
    @Transactional
    public void createFederation() throws Exception {
        int databaseSizeBeforeCreate = federationRepository.findAll().size();

        // Create the Federation

        restFederationMockMvc.perform(post("/api/federations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(federation)))
            .andExpect(status().isCreated());

        // Validate the Federation in the database
        List<Federation> federationList = federationRepository.findAll();
        assertThat(federationList).hasSize(databaseSizeBeforeCreate + 1);
        Federation testFederation = federationList.get(federationList.size() - 1);
        assertThat(testFederation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFederation.getSyndicates()).isEqualTo(DEFAULT_SYNDICATES);
    }

    @Test
    @Transactional
    public void createFederationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = federationRepository.findAll().size();

        // Create the Federation with an existing ID
        Federation existingFederation = new Federation();
        existingFederation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFederationMockMvc.perform(post("/api/federations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFederation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Federation> federationList = federationRepository.findAll();
        assertThat(federationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFederations() throws Exception {
        // Initialize the database
        federationRepository.saveAndFlush(federation);

        // Get all the federationList
        restFederationMockMvc.perform(get("/api/federations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(federation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].syndicates").value(hasItem(DEFAULT_SYNDICATES.toString())));
    }

    @Test
    @Transactional
    public void getFederation() throws Exception {
        // Initialize the database
        federationRepository.saveAndFlush(federation);

        // Get the federation
        restFederationMockMvc.perform(get("/api/federations/{id}", federation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(federation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.syndicates").value(DEFAULT_SYNDICATES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFederation() throws Exception {
        // Get the federation
        restFederationMockMvc.perform(get("/api/federations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFederation() throws Exception {
        // Initialize the database
        federationRepository.saveAndFlush(federation);
        int databaseSizeBeforeUpdate = federationRepository.findAll().size();

        // Update the federation
        Federation updatedFederation = federationRepository.findOne(federation.getId());
        updatedFederation
                .name(UPDATED_NAME)
                .syndicates(UPDATED_SYNDICATES);

        restFederationMockMvc.perform(put("/api/federations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFederation)))
            .andExpect(status().isOk());

        // Validate the Federation in the database
        List<Federation> federationList = federationRepository.findAll();
        assertThat(federationList).hasSize(databaseSizeBeforeUpdate);
        Federation testFederation = federationList.get(federationList.size() - 1);
        assertThat(testFederation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFederation.getSyndicates()).isEqualTo(UPDATED_SYNDICATES);
    }

    @Test
    @Transactional
    public void updateNonExistingFederation() throws Exception {
        int databaseSizeBeforeUpdate = federationRepository.findAll().size();

        // Create the Federation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFederationMockMvc.perform(put("/api/federations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(federation)))
            .andExpect(status().isCreated());

        // Validate the Federation in the database
        List<Federation> federationList = federationRepository.findAll();
        assertThat(federationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFederation() throws Exception {
        // Initialize the database
        federationRepository.saveAndFlush(federation);
        int databaseSizeBeforeDelete = federationRepository.findAll().size();

        // Get the federation
        restFederationMockMvc.perform(delete("/api/federations/{id}", federation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Federation> federationList = federationRepository.findAll();
        assertThat(federationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Federation.class);
    }
}
