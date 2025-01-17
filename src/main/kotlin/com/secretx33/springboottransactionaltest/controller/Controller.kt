package com.secretx33.springboottransactionaltest.controller

import com.secretx33.springboottransactionaltest.dao.PersonDAO
import com.secretx33.springboottransactionaltest.service.PersonService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/persons")
class Controller(
    private val personService: PersonService,
) {
    @PostMapping("/1")
    fun simpleSyncCreateAndUpdatePerson(): PersonDAO = personService.simpleSyncCreateAndUpdatePerson()
}