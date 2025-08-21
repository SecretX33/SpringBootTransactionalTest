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
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withContext
import org.hibernate.SessionFactory
import org.hibernate.engine.spi.SessionImplementor
import org.slf4j.LoggerFactory
import org.springframework.orm.jpa.EntityManagerHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager
import reactor.core.publisher.Mono
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadLocalRandom

@Service
class PersonService(
    private val personRepository: PersonRepository,
    private val carRepository: CarRepository,
    private val realEstateRepository: RealEstateRepository,
    private val personService2: PersonService2,
    private val sessionFactory: SessionFactory,
    @PersistenceContext private val entityManager: EntityManager,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val sessions = ConcurrentHashMap.newKeySet<WeakReference<Any>>()

    init {
//        GlobalScope.launch {
//            while (isActive) {
//                delay(5000)
//                log.info("Sessions:\n${sessions.joinToString("\n") { " - ${it.get()}" }}")
//            }
//        }
    }

    @Transactional
    fun simpleSyncCreateAndUpdatePerson(): Person {
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
    suspend fun simpleSuspendCreateAndUpdatePerson(): Person = withContext(Dispatchers.IO) {
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

        personRepository.saveAndFlush(person).toDto()
    }

    @Transactional
    suspend fun suspendWithThreadSwitchCreateAndUpdatePerson(): Person = withContext(Dispatchers.IO) {
        // Create a person with a car and real estate
        val person = personRepository.save(
            PersonDAO(
                name = "John Doe",
                age = 30,
                hobby = "Reading",
            )
        )
        forceThreadSwitch()

        // Create and associate a car with the person
        val car = carRepository.save(
            CarDAO(
                owner = person,
                model = "Tesla Model 3",
                releaseYear = 2024,
            )
        )
        forceThreadSwitch()

        person.carDAO = car

        forceThreadSwitch()

        // Create and associate a real estate with the person
        val realEstate = realEstateRepository.save(
            RealEstateDAO(
                owner = person,
                address = "123 Main St",
                constructionYear = 2020,
            )
        )
        forceThreadSwitch()

        person.realEstateDAO = realEstate

        forceThreadSwitch()

        // Update all entities
        person.apply {
            name = "John Smith"
            age = 31
            hobby = "Writing"
        }
        forceThreadSwitch()

        car.apply {
            model = "Tesla Model Y"
            releaseYear = 2025
        }
        forceThreadSwitch()

        realEstate.apply {
            address = "456 Oak Ave"
            constructionYear = 2021
        }
        forceThreadSwitch()

        personRepository.saveAndFlush(person).toDto()
    }

    @Transactional
    suspend fun suspendWithThreadSwitchAndFlushCreateAndUpdatePerson(): Person = withContext(Dispatchers.IO) {
        // Create a person with a car and real estate
        val person = personRepository.saveAndFlush(
            PersonDAO(
                name = "John Doe",
                age = 30,
                hobby = "Reading",
            )
        )
        forceThreadSwitch()

        // Create and associate a car with the person
        val car = carRepository.saveAndFlush(
            CarDAO(
                owner = person,
                model = "Tesla Model 3",
                releaseYear = 2024,
            )
        )
        forceThreadSwitch()

        person.carDAO = car

        forceThreadSwitch()

        // Create and associate a real estate with the person
        val realEstate = realEstateRepository.saveAndFlush(
            RealEstateDAO(
                owner = person,
                address = "123 Main St",
                constructionYear = 2020,
            )
        )
        forceThreadSwitch()

        person.realEstateDAO = realEstate

        forceThreadSwitch()

        // Update all entities
        person.apply {
            name = "John Smith"
            age = 31
            hobby = "Writing"
        }
        forceThreadSwitch()

        car.apply {
            model = "Tesla Model Y"
            releaseYear = 2025
        }
        forceThreadSwitch()

        realEstate.apply {
            address = "456 Oak Ave"
            constructionYear = 2021
        }
        forceThreadSwitch()

        personRepository.saveAndFlush(person).toDto()
    }

    @Transactional
    suspend fun suspendWithNestedSyncTransactionalMethodsCreateAndUpdatePerson(): Person = withContext(Dispatchers.IO) {
        // Create a person with a car and real estate
        val person = PersonDAO(
            name = "John Doe",
            age = 30,
            hobby = "Reading",
        )
        forceThreadSwitch()

        // Create and associate a car with the person
        val car = CarDAO(
            owner = person,
            model = "Tesla Model 3",
            releaseYear = 2024,
        )
        forceThreadSwitch()

        // Create and associate a real estate with the person
        val realEstate = RealEstateDAO(
            owner = person,
            address = "123 Main St",
            constructionYear = 2020,
        )
        forceThreadSwitch()

        saveAllEntitiesSync(
            person = person,
            car = car,
            realEstate = realEstate,
        )

        person.realEstateDAO = realEstate
        person.carDAO = car

        forceThreadSwitch()

        // Update all entities
        person.apply {
            name = "John Smith"
            age = 31
            hobby = "Writing"
        }
        forceThreadSwitch()

        car.apply {
            model = "Tesla Model Y"
            releaseYear = 2025
        }
        forceThreadSwitch()

        realEstate.apply {
            address = "456 Oak Ave"
            constructionYear = 2021
        }
        forceThreadSwitch()

        personRepository.saveAndFlush(person).toDto()
    }

    @Transactional
    fun monoFromSyncCreateAndUpdatePerson(): Mono<Person> = mono(Dispatchers.IO) {
        simpleSyncCreateAndUpdatePerson()
    }

    @Transactional
    fun monoFromSimpleSuspendCreateAndUpdatePerson(): Mono<Person> = mono(Dispatchers.IO) {
        simpleSuspendCreateAndUpdatePerson()
    }

    @Transactional
    fun monoFromSuspendWithThreadSwitchCreateAndUpdatePerson(): Mono<Person> = mono(Dispatchers.IO) {
        suspendWithThreadSwitchCreateAndUpdatePerson()
    }

    @Transactional
    fun monoFromSuspendWithThreadSwitchAndFlushCreateAndUpdatePerson(): Mono<Person> = mono(Dispatchers.IO) {
        suspendWithThreadSwitchAndFlushCreateAndUpdatePerson()
    }

    @Transactional
    fun monoFromSuspendWithNestedSyncCreateAndUpdatePerson(): Mono<Person> = mono(Dispatchers.IO) {
        suspendWithNestedSyncTransactionalMethodsCreateAndUpdatePerson()
    }

    @Transactional
    suspend fun suspendWithAsyncCallsCreateAndUpdatePerson(): Person = withContext(Dispatchers.IO) {
        // Create a person with a car and real estate
        val person = PersonDAO(
            name = "John Doe",
            age = 30,
            hobby = "Reading",
        )
        forceThreadSwitch()

        // Create and associate a car with the person
        val car = CarDAO(
            owner = person,
            model = "Tesla Model 3",
            releaseYear = 2024,
        )
        forceThreadSwitch()

        // Create and associate a real estate with the person
        val realEstate = RealEstateDAO(
            owner = person,
            address = "123 Main St",
            constructionYear = 2020,
        )
        forceThreadSwitch()

        savePersonSync(person)

        forceThreadSwitch()
        awaitAll(
            async {
                delay(ThreadLocalRandom.current().nextInt(1, 10).toLong())
                forceThreadSwitch()
                saveCarSync(car)
            },
            async {
                delay(ThreadLocalRandom.current().nextInt(1, 10).toLong())
                forceThreadSwitch()
                saveRealEstateSync(realEstate)
            },
        )

        person.realEstateDAO = realEstate
        person.carDAO = car

        forceThreadSwitch()

        // Update all entities
        person.apply {
            name = "John Smith"
            age = 31
            hobby = "Writing"
        }
        forceThreadSwitch()

        car.apply {
            model = "Tesla Model Y"
            releaseYear = 2025
        }
        forceThreadSwitch()

        realEstate.apply {
            address = "456 Oak Ave"
            constructionYear = 2021
        }
        forceThreadSwitch()

        personRepository.saveAndFlush(person).toDto()
    }

    @Transactional
    fun simpleSyncCreateAndUpdatePersonLoggingHibernateContext(): Person {
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

        saveSession(getSession())

        return innerSimpleSyncCreateAndUpdatePersonLoggingHibernateContext(person, car)
    }

    @Transactional
    fun innerSimpleSyncCreateAndUpdatePersonLoggingHibernateContext(
        person: PersonDAO,
        car: CarDAO,
    ): Person {
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
        saveSession(getSession())

        return personRepository.saveAndFlush(person).toDto().also {
            saveSession(getSession())
            val session = (TransactionSynchronizationManager.getResource(sessionFactory)!! as EntityManagerHolder).entityManager
            sessions.add(WeakReference(session))
        }
    }

    @Transactional
    suspend fun suspendCreateAndUpdatePersonWithThreadSwitchingLoggingHibernateContext(): Person {
        saveSession(getSession())

        // Create a person with a car and real estate
        val person = personRepository.save(
            PersonDAO(
                name = "John Doe",
                age = 30,
                hobby = "Reading",
            )
        )
        forceThreadSwitch()

        // Create and associate a car with the person
        val car = carRepository.save(
            CarDAO(
                owner = person,
                model = "Tesla Model 3",
                releaseYear = 2024,
            )
        )
        person.carDAO = car

        saveSession(getSession())
        forceThreadSwitch()

        // Create and associate a real estate with the person
        val realEstate = realEstateRepository.save(
            RealEstateDAO(
                owner = person,
                address = "123 Main St",
                constructionYear = 2020,
            )
        )
        person.realEstateDAO = realEstate

        saveSession(getSession())
        forceThreadSwitch()

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

        saveSession(getSession())
        forceThreadSwitch()

        return personRepository.saveAndFlush(person).toDto()
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    suspend fun suspendCreateAndUpdatePersonWithThreadSwitchingAndInnerMethodLoggingHibernateContext(): Person {
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

        saveSession(getSession())
        forceThreadSwitch()

        return personService2.innerSuspendCreateAndUpdatePersonWithThreadSwitchingAndInnerMethodLoggingHibernateContext(person, car)
    }

    @Transactional
    fun saveAllEntitiesSync(
        person: PersonDAO,
        car: CarDAO,
        realEstate: RealEstateDAO,
    ): PersonDAO {
        val newPerson = savePersonSync(person)
        saveCarSync(car)
        saveRealEstateSync(realEstate)
        return newPerson
    }

    @Transactional
    fun savePersonSync(person: PersonDAO): PersonDAO = personRepository.saveAndFlush(person)

    @Transactional
    fun saveCarSync(car: CarDAO): CarDAO = carRepository.saveAndFlush(car)

    @Transactional
    fun saveRealEstateSync(realEstate: RealEstateDAO): RealEstateDAO = realEstateRepository.saveAndFlush(realEstate)

    private fun getSession(): SessionImplementor = entityManager.unwrap(SessionImplementor::class.java)

    private fun saveSession(session: Any) = sessions.add(WeakReference(session))

//    private suspend fun forceThreadSwitch() {
//        val coroutineContext = coroutineContext
//        val thread = Thread.currentThread()
//        suspendCoroutine { cont ->
//            GlobalScope.launch(coroutineContext + Dispatchers.IO) {
//                if (Thread.currentThread() == thread) {
//                    throw IllegalStateException("Thread is still the same")
//                }
//                cont.resume(Unit)
//            }
//        }
//    }

    private suspend fun forceThreadSwitch() = withContext(Dispatchers.IO) {
    }
} 