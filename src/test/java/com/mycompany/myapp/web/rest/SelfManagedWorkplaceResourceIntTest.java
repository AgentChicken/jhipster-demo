package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterdemoApp;

import com.mycompany.myapp.domain.SelfManagedWorkplace;
import com.mycompany.myapp.repository.SelfManagedWorkplaceRepository;
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
 * Test class for the SelfManagedWorkplaceResource REST controller.
 *
 * @see SelfManagedWorkplaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterdemoApp.class)
public class SelfManagedWorkplaceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EMPLOYEES = 1;
    private static final Integer UPDATED_EMPLOYEES = 2;

    private static final String DEFAULT_DELEGATES = "AAAAAAAAAA";
    private static final String UPDATED_DELEGATES = "BBBBBBBBBB";

    @Autowired
    private SelfManagedWorkplaceRepository selfManagedWorkplaceRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSelfManagedWorkplaceMockMvc;

    private SelfManagedWorkplace selfManagedWorkplace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            SelfManagedWorkplaceResource selfManagedWorkplaceResource = new SelfManagedWorkplaceResource(selfManagedWorkplaceRepository);
        this.restSelfManagedWorkplaceMockMvc = MockMvcBuilders.standaloneSetup(selfManagedWorkplaceResource)
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
    public static SelfManagedWorkplace createEntity(EntityManager em) {
        SelfManagedWorkplace selfManagedWorkplace = new SelfManagedWorkplace()
                .name(DEFAULT_NAME)
                .employees(DEFAULT_EMPLOYEES)
                .delegates(DEFAULT_DELEGATES);
        return selfManagedWorkplace;
    }

    @Before
    public void initTest() {
        selfManagedWorkplace = createEntity(em);
    }

    @Test
    @Transactional
    public void createSelfManagedWorkplace() throws Exception {
        int databaseSizeBeforeCreate = selfManagedWorkplaceRepository.findAll().size();

        // Create the SelfManagedWorkplace

        restSelfManagedWorkplaceMockMvc.perform(post("/api/self-managed-workplaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(selfManagedWorkplace)))
            .andExpect(status().isCreated());

        // Validate the SelfManagedWorkplace in the database
        List<SelfManagedWorkplace> selfManagedWorkplaceList = selfManagedWorkplaceRepository.findAll();
        assertThat(selfManagedWorkplaceList).hasSize(databaseSizeBeforeCreate + 1);
        SelfManagedWorkplace testSelfManagedWorkplace = selfManagedWorkplaceList.get(selfManagedWorkplaceList.size() - 1);
        assertThat(testSelfManagedWorkplace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSelfManagedWorkplace.getEmployees()).isEqualTo(DEFAULT_EMPLOYEES);
        assertThat(testSelfManagedWorkplace.getDelegates()).isEqualTo(DEFAULT_DELEGATES);
    }

    @Test
    @Transactional
    public void createSelfManagedWorkplaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = selfManagedWorkplaceRepository.findAll().size();

        // Create the SelfManagedWorkplace with an existing ID
        SelfManagedWorkplace existingSelfManagedWorkplace = new SelfManagedWorkplace();
        existingSelfManagedWorkplace.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSelfManagedWorkplaceMockMvc.perform(post("/api/self-managed-workplaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSelfManagedWorkplace)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SelfManagedWorkplace> selfManagedWorkplaceList = selfManagedWorkplaceRepository.findAll();
        assertThat(selfManagedWorkplaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSelfManagedWorkplaces() throws Exception {
        // Initialize the database
        selfManagedWorkplaceRepository.saveAndFlush(selfManagedWorkplace);

        // Get all the selfManagedWorkplaceList
        restSelfManagedWorkplaceMockMvc.perform(get("/api/self-managed-workplaces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selfManagedWorkplace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].employees").value(hasItem(DEFAULT_EMPLOYEES)))
            .andExpect(jsonPath("$.[*].delegates").value(hasItem(DEFAULT_DELEGATES.toString())));
    }

    @Test
    @Transactional
    public void getSelfManagedWorkplace() throws Exception {
        // Initialize the database
        selfManagedWorkplaceRepository.saveAndFlush(selfManagedWorkplace);

        // Get the selfManagedWorkplace
        restSelfManagedWorkplaceMockMvc.perform(get("/api/self-managed-workplaces/{id}", selfManagedWorkplace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(selfManagedWorkplace.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.employees").value(DEFAULT_EMPLOYEES))
            .andExpect(jsonPath("$.delegates").value(DEFAULT_DELEGATES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSelfManagedWorkplace() throws Exception {
        // Get the selfManagedWorkplace
        restSelfManagedWorkplaceMockMvc.perform(get("/api/self-managed-workplaces/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSelfManagedWorkplace() throws Exception {
        // Initialize the database
        selfManagedWorkplaceRepository.saveAndFlush(selfManagedWorkplace);
        int databaseSizeBeforeUpdate = selfManagedWorkplaceRepository.findAll().size();

        // Update the selfManagedWorkplace
        SelfManagedWorkplace updatedSelfManagedWorkplace = selfManagedWorkplaceRepository.findOne(selfManagedWorkplace.getId());
        updatedSelfManagedWorkplace
                .name(UPDATED_NAME)
                .employees(UPDATED_EMPLOYEES)
                .delegates(UPDATED_DELEGATES);

        restSelfManagedWorkplaceMockMvc.perform(put("/api/self-managed-workplaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSelfManagedWorkplace)))
            .andExpect(status().isOk());

        // Validate the SelfManagedWorkplace in the database
        List<SelfManagedWorkplace> selfManagedWorkplaceList = selfManagedWorkplaceRepository.findAll();
        assertThat(selfManagedWorkplaceList).hasSize(databaseSizeBeforeUpdate);
        SelfManagedWorkplace testSelfManagedWorkplace = selfManagedWorkplaceList.get(selfManagedWorkplaceList.size() - 1);
        assertThat(testSelfManagedWorkplace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSelfManagedWorkplace.getEmployees()).isEqualTo(UPDATED_EMPLOYEES);
        assertThat(testSelfManagedWorkplace.getDelegates()).isEqualTo(UPDATED_DELEGATES);
    }

    @Test
    @Transactional
    public void updateNonExistingSelfManagedWorkplace() throws Exception {
        int databaseSizeBeforeUpdate = selfManagedWorkplaceRepository.findAll().size();

        // Create the SelfManagedWorkplace

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSelfManagedWorkplaceMockMvc.perform(put("/api/self-managed-workplaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(selfManagedWorkplace)))
            .andExpect(status().isCreated());

        // Validate the SelfManagedWorkplace in the database
        List<SelfManagedWorkplace> selfManagedWorkplaceList = selfManagedWorkplaceRepository.findAll();
        assertThat(selfManagedWorkplaceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSelfManagedWorkplace() throws Exception {
        // Initialize the database
        selfManagedWorkplaceRepository.saveAndFlush(selfManagedWorkplace);
        int databaseSizeBeforeDelete = selfManagedWorkplaceRepository.findAll().size();

        // Get the selfManagedWorkplace
        restSelfManagedWorkplaceMockMvc.perform(delete("/api/self-managed-workplaces/{id}", selfManagedWorkplace.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SelfManagedWorkplace> selfManagedWorkplaceList = selfManagedWorkplaceRepository.findAll();
        assertThat(selfManagedWorkplaceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SelfManagedWorkplace.class);
    }
}
