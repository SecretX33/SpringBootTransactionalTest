package com.secretx33.springboottransactionaltest.controller

import com.secretx33.springboottransactionaltest.model.Person
import com.secretx33.springboottransactionaltest.service.PersonService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/persons")
class PersonController(
    private val personService: PersonService,
) {
    @PostMapping("/1")
    fun simpleSyncCreateAndUpdatePerson(): Person = personService.simpleSyncCreateAndUpdatePerson()

    @PostMapping("/2")
    suspend fun simpleSuspendCreateAndUpdatePerson(): Person = personService.simpleSuspendCreateAndUpdatePerson()

    @PostMapping("/3")
    suspend fun suspendWithThreadSwitchCreateAndUpdatePerson(): Person = personService.suspendWithThreadSwitchCreateAndUpdatePerson()

    @PostMapping("/4")
    suspend fun suspendWithThreadSwitchAndFlushCreateAndUpdatePerson(): Person = personService.suspendWithThreadSwitchAndFlushCreateAndUpdatePerson()

    @PostMapping("/5")
    suspend fun suspendWithNestedSyncTransactionalMethodsCreateAndUpdatePerson(): Person = personService.suspendWithNestedSyncTransactionalMethodsCreateAndUpdatePerson()

    @PostMapping("/6")
    fun monoFromSyncCreateAndUpdatePerson(): Mono<Person> = personService.monoFromSyncCreateAndUpdatePerson()

    @PostMapping("/7")
    fun monoFromSimpleSuspendCreateAndUpdatePerson(): Mono<Person> = personService.monoFromSimpleSuspendCreateAndUpdatePerson()

    @PostMapping("/8")
    fun monoFromSuspendWithThreadSwitchCreateAndUpdatePerson(): Mono<Person> = personService.monoFromSuspendWithThreadSwitchCreateAndUpdatePerson()

    @PostMapping("/9")
    fun monoFromSuspendWithThreadSwitchAndFlushCreateAndUpdatePerson(): Mono<Person> = personService.monoFromSuspendWithThreadSwitchAndFlushCreateAndUpdatePerson()

    @PostMapping("/10")
    fun monoFromSuspendWithNestedSyncCreateAndUpdatePerson(): Mono<Person> = personService.monoFromSuspendWithNestedSyncCreateAndUpdatePerson()

    @PostMapping("/11")
    suspend fun suspendWithAsyncCallsCreateAndUpdatePerson(): Person = personService.suspendWithAsyncCallsCreateAndUpdatePerson()
}