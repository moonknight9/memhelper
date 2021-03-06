package de.eschwank.backend

import java.io.Serializable
import java.util.Objects

/**
 * Represents a beverage category.
 */
class Category : Serializable {

    var id: Long? = null

    /**
     * Gets the value of name
     *
     * @return the value of name
     */
    /**
     * Sets the value name
     *
     * @param name
     * new value of name
     */
    var name = ""

    constructor() {}

    constructor(name: String) {
        this.name = name
    }

    constructor(other: Category) {
        Objects.requireNonNull(other)
        this.name = other.name
        this.id = other.id
    }

    override fun toString(): String {
        // Must use getters instead of direct member access,
        // to make it work with proxy objects generated by the view model
        return "Category{$id:$name}"
    }

    override fun hashCode(): Int {
        return if (id == null) {
            super.hashCode()
        } else id!!.hashCode()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj !is Category) {
            return false
        }
        val other = obj as Category?
        if (id == null) {
            if (other!!.id != null)
                return false
        } else if (id != other!!.id)
            return false
        return true
    }
}
