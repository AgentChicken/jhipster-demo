package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.SelfManagedWorkplace;

import com.mycompany.myapp.repository.SelfManagedWorkplaceRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SelfManagedWorkplace.
 */
@RestController
@RequestMapping("/api")
public class SelfManagedWorkplaceResource {

    private final Logger log = LoggerFactory.getLogger(SelfManagedWorkplaceResource.class);

    private static final String ENTITY_NAME = "selfManagedWorkplace";
        
    private final SelfManagedWorkplaceRepository selfManagedWorkplaceRepository;

    public SelfManagedWorkplaceResource(SelfManagedWorkplaceRepository selfManagedWorkplaceRepository) {
        this.selfManagedWorkplaceRepository = selfManagedWorkplaceRepository;
    }

    /**
     * POST  /self-managed-workplaces : Create a new selfManagedWorkplace.
     *
     * @param selfManagedWorkplace the selfManagedWorkplace to create
     * @return the ResponseEntity with status 201 (Created) and with body the new selfManagedWorkplace, or with status 400 (Bad Request) if the selfManagedWorkplace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/self-managed-workplaces")
    @Timed
    public ResponseEntity<SelfManagedWorkplace> createSelfManagedWorkplace(@RequestBody SelfManagedWorkplace selfManagedWorkplace) throws URISyntaxException {
        log.debug("REST request to save SelfManagedWorkplace : {}", selfManagedWorkplace);
        if (selfManagedWorkplace.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new selfManagedWorkplace cannot already have an ID")).body(null);
        }
        SelfManagedWorkplace result = selfManagedWorkplaceRepository.save(selfManagedWorkplace);
        return ResponseEntity.created(new URI("/api/self-managed-workplaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /self-managed-workplaces : Updates an existing selfManagedWorkplace.
     *
     * @param selfManagedWorkplace the selfManagedWorkplace to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated selfManagedWorkplace,
     * or with status 400 (Bad Request) if the selfManagedWorkplace is not valid,
     * or with status 500 (Internal Server Error) if the selfManagedWorkplace couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/self-managed-workplaces")
    @Timed
    public ResponseEntity<SelfManagedWorkplace> updateSelfManagedWorkplace(@RequestBody SelfManagedWorkplace selfManagedWorkplace) throws URISyntaxException {
        log.debug("REST request to update SelfManagedWorkplace : {}", selfManagedWorkplace);
        if (selfManagedWorkplace.getId() == null) {
            return createSelfManagedWorkplace(selfManagedWorkplace);
        }
        SelfManagedWorkplace result = selfManagedWorkplaceRepository.save(selfManagedWorkplace);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, selfManagedWorkplace.getId().toString()))
            .body(result);
    }

    /**
     * GET  /self-managed-workplaces : get all the selfManagedWorkplaces.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of selfManagedWorkplaces in body
     */
    @GetMapping("/self-managed-workplaces")
    @Timed
    public List<SelfManagedWorkplace> getAllSelfManagedWorkplaces() {
        log.debug("REST request to get all SelfManagedWorkplaces");
        List<SelfManagedWorkplace> selfManagedWorkplaces = selfManagedWorkplaceRepository.findAll();
        return selfManagedWorkplaces;
    }

    /**
     * GET  /self-managed-workplaces/:id : get the "id" selfManagedWorkplace.
     *
     * @param id the id of the selfManagedWorkplace to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the selfManagedWorkplace, or with status 404 (Not Found)
     */
    @GetMapping("/self-managed-workplaces/{id}")
    @Timed
    public ResponseEntity<SelfManagedWorkplace> getSelfManagedWorkplace(@PathVariable Long id) {
        log.debug("REST request to get SelfManagedWorkplace : {}", id);
        SelfManagedWorkplace selfManagedWorkplace = selfManagedWorkplaceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(selfManagedWorkplace));
    }

    /**
     * DELETE  /self-managed-workplaces/:id : delete the "id" selfManagedWorkplace.
     *
     * @param id the id of the selfManagedWorkplace to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/self-managed-workplaces/{id}")
    @Timed
    public ResponseEntity<Void> deleteSelfManagedWorkplace(@PathVariable Long id) {
        log.debug("REST request to delete SelfManagedWorkplace : {}", id);
        selfManagedWorkplaceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
