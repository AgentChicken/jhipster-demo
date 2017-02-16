package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Federation;

import com.mycompany.myapp.repository.FederationRepository;
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
 * REST controller for managing Federation.
 */
@RestController
@RequestMapping("/api")
public class FederationResource {

    private final Logger log = LoggerFactory.getLogger(FederationResource.class);

    private static final String ENTITY_NAME = "federation";
        
    private final FederationRepository federationRepository;

    public FederationResource(FederationRepository federationRepository) {
        this.federationRepository = federationRepository;
    }

    /**
     * POST  /federations : Create a new federation.
     *
     * @param federation the federation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new federation, or with status 400 (Bad Request) if the federation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/federations")
    @Timed
    public ResponseEntity<Federation> createFederation(@RequestBody Federation federation) throws URISyntaxException {
        log.debug("REST request to save Federation : {}", federation);
        if (federation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new federation cannot already have an ID")).body(null);
        }
        Federation result = federationRepository.save(federation);
        return ResponseEntity.created(new URI("/api/federations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /federations : Updates an existing federation.
     *
     * @param federation the federation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated federation,
     * or with status 400 (Bad Request) if the federation is not valid,
     * or with status 500 (Internal Server Error) if the federation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/federations")
    @Timed
    public ResponseEntity<Federation> updateFederation(@RequestBody Federation federation) throws URISyntaxException {
        log.debug("REST request to update Federation : {}", federation);
        if (federation.getId() == null) {
            return createFederation(federation);
        }
        Federation result = federationRepository.save(federation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, federation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /federations : get all the federations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of federations in body
     */
    @GetMapping("/federations")
    @Timed
    public List<Federation> getAllFederations() {
        log.debug("REST request to get all Federations");
        List<Federation> federations = federationRepository.findAll();
        return federations;
    }

    /**
     * GET  /federations/:id : get the "id" federation.
     *
     * @param id the id of the federation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the federation, or with status 404 (Not Found)
     */
    @GetMapping("/federations/{id}")
    @Timed
    public ResponseEntity<Federation> getFederation(@PathVariable Long id) {
        log.debug("REST request to get Federation : {}", id);
        Federation federation = federationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(federation));
    }

    /**
     * DELETE  /federations/:id : delete the "id" federation.
     *
     * @param id the id of the federation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/federations/{id}")
    @Timed
    public ResponseEntity<Void> deleteFederation(@PathVariable Long id) {
        log.debug("REST request to delete Federation : {}", id);
        federationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
