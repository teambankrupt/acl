package com.example.acl.domains.profiles.models.enums

import com.example.common.utils.ExceptionUtil
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 7/3/21
 */
enum class BloodGroup(
    var id: Byte,
    @JsonProperty("label") var label: String
) {

    A_POSITIVE(0, "A+"),
    A_NEGATIVE(1, "A-"),
    B_POSITIVE(2, "B+"),
    B_NEGATIVE(3, "B-"),
    AB_POSITIVE(4, "AB+"),
    AB_NEGATIVE(5, "AB-"),
    O_POSITIVE(6, "O+"),
    O_NEGATIVE(7, "O-"),
    UNKNOWN(8, "Unknown");

    companion object {
        @JvmStatic
        fun get(x: Byte?): BloodGroup {
            if (x == null) return UNKNOWN

            for (m: BloodGroup in values()) {
                if (m.id == x)
                    return m
            }
            return UNKNOWN
        }

        @JvmStatic
        fun get(x: String): BloodGroup {
            for (m: BloodGroup in values()) {
                if (m.name == x || m.label == x)
                    return m
            }
            return UNKNOWN
        }

        @JvmStatic
        fun validate(x: Byte): Byte {
            if (values().any { it.id == x }) return x
            else throw ExceptionUtil.notFound(BloodGroup::class.simpleName.toString(), x.toLong())
        }
    }
}
