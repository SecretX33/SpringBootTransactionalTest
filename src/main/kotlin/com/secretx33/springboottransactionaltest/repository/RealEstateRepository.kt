package com.secretx33.springboottransactionaltest.repository

import com.secretx33.springboottransactionaltest.dao.RealEstateDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RealEstateRepository : JpaRepository<RealEstateDAO, UUID>