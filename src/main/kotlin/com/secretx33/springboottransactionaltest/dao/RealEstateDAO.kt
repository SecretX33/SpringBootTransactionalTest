package com.secretx33.springboottransactionaltest.dao

import com.secretx33.springboottransactionaltest.dao.base.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "real_estate")
class RealEstateDAO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    var owner: PersonDAO,

    @Column(nullable = false)
    var address: String,

    @Column(nullable = false)
    var constructionYear: Int
) : BaseEntity()