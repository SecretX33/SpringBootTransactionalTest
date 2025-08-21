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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hibernate.SessionFactory
import org.hibernate.engine.spi.SessionImplementor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

@Service
class PersonService2(
    private val personRepository: PersonRepository,
    private val carRepository: CarRepository,
    private val realEstateRepository: RealEstateRepository,
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    suspend fun innerSuspendCreateAndUpdatePersonWithThreadSwitchingAndInnerMethodLoggingHibernateContext(
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

        // Throws session not found
//        saveSession(getSession())

        return personRepository.saveAndFlush(person).toDto()
    }

    private fun getSession(): SessionImplementor = entityManager.unwrap(SessionImplementor::class.java)

    private fun saveSession(session: Any) = sessions.add(WeakReference(session))

    private suspend fun forceThreadSwitch() {
        val currentThread = Thread.currentThread()
        repeat(10) {
            delay(1)
            if (Thread.currentThread() != currentThread) return
        }

        while (Thread.currentThread() == currentThread) {
            log.debug("Thread is still the same, launching an async task to attempt to prevent this")
            GlobalScope.launch(Dispatchers.IO) {
                Thread.sleep(1000)
            }
            delay(10)
        }

        val newThread = Thread.currentThread()
        log.info("Thread switch: ${currentThread.name} -> ${newThread.name}")
    }
} 