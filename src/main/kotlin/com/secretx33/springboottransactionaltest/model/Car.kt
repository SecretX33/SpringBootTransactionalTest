package com.secretx33.springboottransactionaltest.model

import com.secretx33.springboottransactionaltest.dao.CarDAO
import java.util.UUID

data class Car(
    val id: UUID?,
    val model: String,
    val releaseYear: Int,
)

fun CarDAO.toDto(): Car = Car(
    id = id,
    model = model,
    releaseYear = releaseYear,
)