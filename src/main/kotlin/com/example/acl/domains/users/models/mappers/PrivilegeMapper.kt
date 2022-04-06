package com.example.acl.domains.users.models.mappers

import com.example.acl.domains.users.models.dtos.PrivilegeDto
import com.example.auth.entities.Privilege
import com.example.auth.entities.UrlAccess
import com.example.auth.enums.AccessLevels
import org.springframework.stereotype.Component

@Component
class PrivilegeMapper {

	fun map(privilege: Privilege): PrivilegeDto {
		val dto = PrivilegeDto()
		dto.id = privilege.id
		dto.createdAt = privilege.createdAt
		dto.updatedAt = privilege.updatedAt
		dto.lastUpdated = privilege.updatedAt

		dto.label = privilege.label
		dto.name = privilege.name
		dto.description = privilege.description

		dto.urlsAllAccess = privilege.urlAccesses
			.filter { it.accessLevel == AccessLevels.ALL }
			.map { it.url }
		dto.urlsReadAccess = privilege.urlAccesses
			.filter { it.accessLevel == AccessLevels.READ }
			.map { it.url }
		dto.urlsCreateAccess = privilege.urlAccesses
			.filter { it.accessLevel == AccessLevels.CREATE }
			.map { it.url }
		dto.urlsUpdateAccess = privilege.urlAccesses
			.filter { it.accessLevel == AccessLevels.UPDATE }
			.map { it.url }
		dto.urlsDeleteAccess = privilege.urlAccesses
			.filter { it.accessLevel == AccessLevels.DELETE }
			.map { it.url }
		return dto
	}

	fun map(dto: PrivilegeDto, exPrivilege: Privilege?): Privilege {
		var privilege = exPrivilege
		if (privilege == null) privilege = Privilege()

		privilege.label = dto.label
		privilege.name = dto.name.replace(" ", "_").toUpperCase()
		privilege.description = dto.description

		val accesses: MutableList<UrlAccess> = dto.urlsAllAccess.map { UrlAccess(AccessLevels.ALL, it) }.toMutableList()
		accesses.addAll(dto.urlsReadAccess.map { UrlAccess(AccessLevels.READ, it) }.toMutableList())
		accesses.addAll(dto.urlsCreateAccess.map { UrlAccess(AccessLevels.CREATE, it) }.toMutableList())
		accesses.addAll(dto.urlsUpdateAccess.map { UrlAccess(AccessLevels.UPDATE, it) }.toMutableList())
		accesses.addAll(dto.urlsDeleteAccess.map { UrlAccess(AccessLevels.DELETE, it) }.toMutableList())

		privilege.urlAccesses = accesses

		return privilege
	}

}
