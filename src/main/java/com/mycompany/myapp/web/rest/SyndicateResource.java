package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Syndicate;

import com.mycompany.myapp.repository.SyndicateRepository;
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
 * REST controller for managing Syndicate.
 */
@RestController
@RequestMapping("/api")
public class SyndicateResource {

    private final Logger log = LoggerFactory.getLogger(SyndicateResource.class);

    private static final String ENTITY_NAME = "syndicate";
        
    private final SyndicateRepository syndicateRepository;

    public SyndicateResource(SyndicateRepository syndicateRepository) {
        this.syndicateRepository = syndicateRepository;
    }

    /**
     * POST  /syndicates : Create a new syndicate.
     *
     * @param syndicate the syndicate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new syndicate, or with status 400 (Bad Request) if the syndicate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/syndicates")
    @Timed
    public ResponseEntity<Syndicate> createSyndicate(@RequestBody Syndicate syndicate) throws URISyntaxException {
        log.debug("REST request to save Syndicate : {}", syndicate);
        if (syndicate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new syndicate cannot already have an ID")).body(null);
        }
        Syndicate result = syndicateRepository.save(syndicate);
        return ResponseEntity.created(new URI("/api/syndicates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /syndicates : Updates an existing syndicate.
     *
     * @param syndicate the syndicate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated syndicate,
     * or with status 400 (Bad Request) if the syndicate is not valid,
     * or with status 500 (Internal Server Error) if the syndicate couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/syndicates")
    @Timed
    public ResponseEntity<Syndicate> updateSyndicate(@RequestBody Syndicate syndicate) throws URISyntaxException {
        log.debug("REST request to update Syndicate : {}", syndicate);
        if (syndicate.getId() == null) {
            return createSyndicate(syndicate);
        }
        Syndicate result = syndicateRepository.save(syndicate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, syndicate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /syndicates : get all the syndicates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of syndicates in body
     */
    @GetMapping("/syndicates")
    @Timed
    public List<Syndicate> getAllSyndicates() {
        log.debug("REST request to get all Syndicates");
        List<Syndicate> syndicates = syndicateRepository.findAll();
        return syndicates;
    }

    /**
     * GET  /syndicates/:id : get the "id" syndicate.
     *
     * @param id the id of the syndicate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the syndicate, or with status 404 (Not Found)
     */
    @GetMapping("/syndicates/{id}")
    @Timed
    public ResponseEntity<Syndicate> getSyndicate(@PathVariable Long id) {
        log.debug("REST request to get Syndicate : {}", id);
        Syndicate syndicate = syndicateRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(syndicate));
    }

    /**
     * DELETE  /syndicates/:id : delete the "id" syndicate.
     *
     * @param id the id of the syndicate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/syndicates/{id}")
    @Timed
    public ResponseEntity<Void> deleteSyndicate(@PathVariable Long id) {
        log.debug("REST request to delete Syndicate : {}", id);
        syndicateRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
