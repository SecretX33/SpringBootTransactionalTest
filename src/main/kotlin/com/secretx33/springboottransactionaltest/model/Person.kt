package com.secretx33.springboottransactionaltest.model

import com.secretx33.springboottransactionaltest.dao.PersonDAO
import java.util.UUID

data class Person(
    val id: UUID?,
    val name: String,
    val age: Int,
    val hobby: String?,
    val car: Car?,
    val realEstate: RealEstate?,
)

fun PersonDAO.toDto(): Person = Person(
    id = id,
    name = name,
    age = age,
    hobby = hobby,
    car = carDAO?.toDto(),
    realEstate = realEstateDAO?.toDto(),
)
