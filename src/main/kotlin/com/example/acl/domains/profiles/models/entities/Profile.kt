package com.example.acl.domains.profiles.models.entities

import com.example.acl.domains.profiles.models.enums.BloodGroup
import com.example.acl.domains.profiles.models.enums.Gender
import com.example.acl.domains.profiles.models.enums.MaritalStatus
import com.example.acl.domains.profiles.models.enums.Religion
import com.example.auth.entities.User
import com.example.coreweb.domains.base.entities.BaseEntity
import com.example.coreweb.domains.contacts.models.entities.Contact
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "profiles", schema = "acl")
class Profile : BaseEntity() {

    @Column(nullable = false)
    lateinit var birthday: Instant

    var photo: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    lateinit var gender: Gender

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    var bloodGroup: BloodGroup? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    var maritalStatus: MaritalStatus? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    var religion: Religion? = null

    @OneToOne
    lateinit var user: User
}
