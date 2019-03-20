package de.eschwank.backend

import java.io.Serializable
import java.time.LocalDate

/**
 * Represents a beverage review.
 */
class Review : Serializable {

    var id: Long? = null
    /**
     * Gets the value of score
     *
     * @return the value of score
     */
    /**
     * Sets the value of score
     *
     * @param score
     * new value of Score
     */
    var score: Int = 0
    /**
     * Gets the value of name
     *
     * @return the value of name
     */
    /**
     * Sets the value of name
     *
     * @param name
     * new value of name
     */
    var name: String? = null
    /**
     * Gets the value of date
     *
     * @return the value of date
     */
    /**
     * Sets the value of date
     *
     * @param date
     * new value of date
     */
    var date: LocalDate? = null
    /**
     * Gets the value of category
     *
     * @return the value of category
     */
    /**
     * Sets the value of category
     *
     * @param category
     * new value of category
     */
    var category: Category? = null
    /**
     * Gets the value of count
     *
     * @return the value of count
     */
    /**
     * Sets the value of count
     *
     * @param count
     * new value of count
     */
    var count: Int = 0

    /**
     * Default constructor.
     */
    constructor() {
        reset()
    }

    /**
     * Constructs a new instance with the given data.
     *
     * @param score
     * Review score
     * @param name
     * Name of beverage reviewed
     * @param date
     * Last review date
     * @param category
     * Category of beverage
     * @param count
     * Times tasted
     */
    constructor(score: Int, name: String?, date: LocalDate?, category: Category?,
                count: Int) {
        this.score = score
        this.name = name
        this.date = date
        this.category = Category(category!!)
        this.count = count
    }

    /**
     * Copy constructor.
     *
     * @param other
     * The instance to copy
     */
    constructor(other: Review) : this(other.score, other.name, other.date,
            other.category, other.count) {
        this.id = other.id
    }

    /**
     * Resets all fields to their default values.
     */
    fun reset() {
        this.id = null
        this.score = 1
        this.name = ""
        this.date = LocalDate.now()
        this.category = null
        this.count = 1
    }

    override fun toString(): String {
        // Must use getters instead of direct member access,
        // to make it work with proxy objects generated by the view model
        return ("Review{" + "id=" + id + ", score=" + score + ", name="
                + name + ", category=" + category + ", date="
                + date + ", count=" + count + '}'.toString())
    }

}
