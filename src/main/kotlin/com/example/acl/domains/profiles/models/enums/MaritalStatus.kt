package com.example.acl.domains.profiles.models.enums

import com.example.common.utils.ExceptionUtil
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 7/3/21
 */
enum class MaritalStatus(
    var id: Byte,
    @JsonProperty("label") var label: String
) {

    MARRIED(0, "Married"),
    UNMARRIED(1, "Unmarried"),
    DIVORCED(2, "Divorced"),
    SEPARATED(3, "Separated"),
    WIDOWER(4, "Widower"),
    WIDOW(5, "Widow"),
    OTHERS(6, "Others");

    companion object {
        @JvmStatic
        fun get(x: Byte): MaritalStatus {
            for (g: MaritalStatus in values()) {
                if (g.id == x)
                    return g
            }
            return OTHERS
        }

        @JvmStatic
        fun get(x: String): MaritalStatus {
            for (g: MaritalStatus in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return OTHERS
        }

        @JvmStatic
        fun validate(x: Byte): Byte {
            if (values().any { it.id == x }) return x
            else throw ExceptionUtil.notFound(MaritalStatus::class.simpleName.toString(), x.toLong())
        }
    }
}
