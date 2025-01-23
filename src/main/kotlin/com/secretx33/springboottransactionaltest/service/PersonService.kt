package com.secretx33.springboottransactionaltest.service

import com.secretx33.springboottransactionaltest.dao.CarDAO
import com.secretx33.springboottransactionaltest.dao.PersonDAO
import com.secretx33.springboottransactionaltest.dao.RealEstateDAO
import com.secretx33.springboottransactionaltest.repository.CarRepository
import com.secretx33.springboottransactionaltest.repository.PersonRepository
import com.secretx33.springboottransactionaltest.repository.RealEstateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PersonService(
    private val personRepository: PersonRepository,
    private val carRepository: CarRepository,
    private val realEstateRepository: RealEstateRepository,
) {

    @Transactional
    fun simpleSyncCreateAndUpdatePerson(): PersonDAO {
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

        // Save all updates
        return personRepository.saveAndFlush(person)
    }
} 