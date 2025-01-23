package com.secretx33.springboottransactionaltest.model

import com.secretx33.springboottransactionaltest.dao.RealEstateDAO
import java.util.UUID

data class RealEstate(
    val id: UUID?,
    val address: String,
    val constructionYear: Int,
)

fun RealEstateDAO.toDto(): RealEstate = RealEstate(
    id = id,
    address = address,
    constructionYear = constructionYear,
)