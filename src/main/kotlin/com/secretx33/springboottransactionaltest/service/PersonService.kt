package com.secretx33.springboottransactionaltest.service

import com.secretx33.springboottransactionaltest.dao.PersonDAO
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

    @Transactional(transactionManager = "transactionalTestTransactionManager")
    fun simpleSyncCreateAndUpdatePerson(): PersonDAO {
        // Create a person with a car and real estate
        val person = PersonDAO(
            name = "John Doe",
            age = 30,
            hobby = "Reading",
        )

//        // Create and associate a car with the person
//        val car = CarDAO(
//            owner = person,
//            model = "Tesla Model 3",
//            releaseYear = 2024,
//        )
//
//        // Create and associate a real estate with the person
//        val realEstate = RealEstateDAO(
//            owner = person,
//            address = "123 Main St",
//            constructionYear = 2020,
//        )

        // Update all entities
//        person.apply {
//            name = "John Smith"
//            age = 31
//            hobby = "Writing"
//        }
//        car.apply {
//            model = "Tesla Model Y"
//            releaseYear = 2025
//        }
//        realEstate.apply {
//            address = "456 Oak Ave"
//            constructionYear = 2021
//        }

        // The entities are already being tracked by JPA, no need to explicitly save them
        return personRepository.save(person)
    }
} 