package com.secretx33.springboottransactionaltest.repository

import com.secretx33.springboottransactionaltest.dao.CarDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CarRepository : JpaRepository<CarDAO, UUID>