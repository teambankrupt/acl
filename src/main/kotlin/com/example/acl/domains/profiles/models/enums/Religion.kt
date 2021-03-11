package com.example.acl.domains.profiles.models.enums

import com.example.common.utils.ExceptionUtil
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 7/3/21
 */
enum class Religion(
    var id: Byte,
    @JsonProperty("name_en") var label: String
) {

    ISLAM(1, "Islam"),
    HINDU(2, "Hinduism"),
    BUDDHIST(3, "Buddha"),
    CHRISTIAN(4, "Christian"),
    OTHER(5, "Others");

    companion object {

        @JvmStatic
        fun get(x: Byte): Religion {
            for (g: Religion in values()) {
                if (g.id == x)
                    return g
            }
            return OTHER
        }

        @JvmStatic
        fun get(x: String): Religion {
            for (g: Religion in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return OTHER
        }

        @JvmStatic
        fun validate(x: Byte): Byte {
            if (values().any { it.id == x }) return x
            else throw ExceptionUtil.notFound(Religion::class.simpleName.toString(), x.toLong())
        }
    }
}
