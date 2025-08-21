@file:Suppress("DuplicatedCode")
@file:OptIn(DelicateCoroutinesApi::class)

package com.secretx33.springboottransactionaltest.service

import com.secretx33.springboottransactionaltest.dao.CarDAO
import com.secretx33.springboottransactionaltest.dao.PersonDAO
import com.secretx33.springboottransactionaltest.dao.RealEstateDAO
import com.secretx33.springboottransactionaltest.model.Person
import com.secretx33.springboottransactionaltest.model.toDto
import com.secretx33.springboottransactionaltest.repository.CarRepository
import com.secretx33.springboottransactionaltest.repository.PersonRepository
import com.secretx33.springboottransactionaltest.repository.RealEstateRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SuspendService(
    private val personRepository: PersonRepository,
    private val carRepository: CarRepository,
    private val realEstateRepository: RealEstateRepository,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun normalTransactional(): Person {
        log.info("[normalTransactional] current thread: ${Thread.currentThread().name}")

        // Create a person with a car and real estate
        val person = personRepository.save(
            PersonDAO(
                name = "John Doe",
                age = 30,
                hobby = "Reading",
            )
        )

        // Create and associate a car with the person
        val car = carRepository.save(
            CarDAO(
                owner = person,
                model = "Tesla Model 3",
                releaseYear = 2024,
            )
        )
        person.carDAO = car

        // Create and associate a real estate with the person
        val realEstate = realEstateRepository.save(
            RealEstateDAO(
                owner = person,
                address = "123 Main St",
                constructionYear = 2020,
            )
        )
        person.realEstateDAO = realEstate

        // Update all entities
        person.apply {
            name = "John Smith"
            age = 31
            hobby = "Writing"
        }
        car.apply {
            model = "Tesla Model Y"
            releaseYear = 2025
        }
        realEstate.apply {
            address = "456 Oak Ave"
            constructionYear = 2021
        }

        return personRepository.saveAndFlush(person).toDto()
    }

    @Transactional
    suspend fun suspendTransactional(): Person {
        log.info("[suspendTransactional] current thread before suspending: ${Thread.currentThread().name} (${currentCoroutineContext()})")
        delay(200)
        log.info("[suspendTransactional] current thread after resuming: ${Thread.currentThread().name} (${currentCoroutineContext()})")

        // Create a person with a car and real estate
        val person = personRepository.save(
            PersonDAO(
                name = "John Doe",
                age = 30,
                hobby = "Reading",
            )
        )

        // Create and associate a car with the person
        val car = carRepository.save(
            CarDAO(
                owner = person,
                model = "Tesla Model 3",
                releaseYear = 2024,
            )
        )
        person.carDAO = car

        // Create and associate a real estate with the person
        val realEstate = realEstateRepository.save(
            RealEstateDAO(
                owner = person,
                address = "123 Main St",
                constructionYear = 2020,
            )
        )
        person.realEstateDAO = realEstate

        // Update all entities
        person.apply {
            name = "John Smith"
            age = 31
            hobby = "Writing"
        }
        car.apply {
            model = "Tesla Model Y"
            releaseYear = 2025
        }
        realEstate.apply {
            address = "456 Oak Ave"
            constructionYear = 2021
        }

        return personRepository.saveAndFlush(person).toDto()
    }

    @Transactional
    fun transactionalFlow(): Flow<Person> = flow {
        log.info("[transactionalFlow] current thread before suspending: ${Thread.currentThread().name} (${currentCoroutineContext()})")
        delay(200)
        log.info("[transactionalFlow] current thread after resuming: ${Thread.currentThread().name} (${currentCoroutineContext()})")

        // Create a person with a car and real estate
        val person = personRepository.save(
            PersonDAO(
                name = "John Doe",
                age = 30,
                hobby = "Reading",
            )
        )

        // Create and associate a car with the person
        val car = carRepository.save(
            CarDAO(
                owner = person,
                model = "Tesla Model 3",
                releaseYear = 2024,
            )
        )
        person.carDAO = car

        // Create and associate a real estate with the person
        val realEstate = realEstateRepository.save(
            RealEstateDAO(
                owner = person,
                address = "123 Main St",
                constructionYear = 2020,
            )
        )
        person.realEstateDAO = realEstate

        // Update all entities
        person.apply {
            name = "John Smith"
            age = 31
            hobby = "Writing"
        }
        car.apply {
            model = "Tesla Model Y"
            releaseYear = 2025
        }
        realEstate.apply {
            address = "456 Oak Ave"
            constructionYear = 2021
        }

        emit(personRepository.saveAndFlush(person).toDto())
    }

    @Transactional
    suspend fun suspendTransactionalFlow(): Flow<Person> = flow {
        log.info("[suspendTransactionalFlow] current thread before suspending: ${Thread.currentThread().name} (${currentCoroutineContext()})")
        delay(200)
        log.info("[suspendTransactionalFlow] current thread after resuming: ${Thread.currentThread().name} (${currentCoroutineContext()})")

        // Create a person with a car and real estate
        val person = personRepository.save(
            PersonDAO(
                name = "John Doe",
                age = 30,
                hobby = "Reading",
            )
        )

        // Create and associate a car with the person
        val car = carRepository.save(
            CarDAO(
                owner = person,
                model = "Tesla Model 3",
                releaseYear = 2024,
            )
        )
        person.carDAO = car

        // Create and associate a real estate with the person
        val realEstate = realEstateRepository.save(
            RealEstateDAO(
                owner = person,
                address = "123 Main St",
                constructionYear = 2020,
            )
        )
        person.realEstateDAO = realEstate

        // Update all entities
        person.apply {
            name = "John Smith"
            age = 31
            hobby = "Writing"
        }
        car.apply {
            model = "Tesla Model Y"
            releaseYear = 2025
        }
        realEstate.apply {
            address = "456 Oak Ave"
            constructionYear = 2021
        }

        emit(personRepository.saveAndFlush(person).toDto())
    }
} 