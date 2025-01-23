package com.secretx33.springboottransactionaltest.controller

import com.secretx33.springboottransactionaltest.model.Person
import com.secretx33.springboottransactionaltest.service.PersonService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/persons")
class PersonController(
    private val personService: PersonService,
) {
    @PostMapping("/1")
    fun simpleSyncCreateAndUpdatePerson(): Person = personService.simpleSyncCreateAndUpdatePerson()
}