package de.eschwank.backend

import java.time.LocalDate
import java.util.ArrayList
import java.util.HashMap
import java.util.Random
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors
import java.util.stream.Stream

import de.eschwank.ui.encoders.LocalDateToStringEncoder

/**
 * Simple backend service to store and retrieve [Review] instances.
 */
class ReviewService
/**
 * Declared private to ensure uniqueness of this Singleton.
 */
private constructor() {

    private val reviews = HashMap<Long, Review>()
    private val nextId = AtomicLong(0)

    /**
     * Helper class to initialize the singleton Service in a thread-safe way and
     * to keep the initialization ordering clear between the two services. See
     * also: https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private object SingletonHolder {
        internal val INSTANCE = createDemoReviewService()

        private val randomDate: LocalDate
            get() {
                val minDay = LocalDate.of(1930, 1, 1).toEpochDay()
                val maxDay = LocalDate.now().toEpochDay()
                val randomDay = ThreadLocalRandom.current().nextLong(minDay,
                        maxDay)
                return LocalDate.ofEpochDay(randomDay)
            }

        private fun createDemoReviewService(): ReviewService {
            val reviewService = ReviewService()
            val r = Random()
            val reviewCount = 20 + r.nextInt(30)
            val beverages = ArrayList(StaticData.BEVERAGES.entries)

            for (i in 0 until reviewCount) {
                val review = Review()
                val beverage = beverages[r.nextInt(StaticData.BEVERAGES.size)]
                val category = CategoryService.instance
                        .findCategoryOrThrow(beverage.value)

                review.name = beverage.key
                val testDay = randomDate
                review.date = testDay
                review.score = 1 + r.nextInt(5)
                review.category = category
                review.count = 1 + r.nextInt(15)
                reviewService.saveReview(review)
            }

            return reviewService
        }
    }
    /** This class is not meant to be instantiated.  */

    /**
     * Fetches the reviews matching the given filter text.
     *
     * The matching is case insensitive. When passed an empty filter text, the
     * method returns all categories. The returned list is ordered by name.
     *
     * @param filter
     * the filter text
     * @return the list of matching reviews
     */
    fun findReviews(filter: String): List<Review> {
        val normalizedFilter = filter.toLowerCase()

        return reviews.values.stream().filter { review -> filterTextOf(review).contains(normalizedFilter) }
                .sorted { r1, r2 -> r2.id!!.compareTo(r1.id!!) }
                .collect(Collectors.toList())
    }

    private fun filterTextOf(review: Review): String {
        val dateConverter = LocalDateToStringEncoder()
        // Use a delimiter which can't be entered in the search box,
        // to avoid false positives
        val filterableText = Stream
                .of(review.name,
                        if (review.category == null)
                            StaticData.UNDEFINED
                        else
                            review.category!!.name,
                        review.score.toString(),
                        review.count.toString(),
                        dateConverter.encode(review.date))
                .collect(Collectors.joining("\t"))
        return filterableText.toLowerCase()
    }

    /**
     * Deletes the given review from the review store.
     *
     * @param review
     * the review to delete
     * @return true if the operation was successful, otherwise false
     */
    fun deleteReview(review: Review): Boolean {
        return reviews.remove(review.id) != null
    }

    /**
     * Persists the given review into the review store.
     *
     * If the review is already persistent, the saved review will get updated
     * with the field values of the given review object. If the review is new
     * (i.e. its id is null), it will get a new unique id before being saved.
     *
     * @param dto
     * the review to save
     */
    fun saveReview(dto: Review) {
        var entity: Review? = reviews[dto.id]
        var category = dto.category

        if (category != null) {
            // The case when the category is new (not persisted yet, thus
            // has null id) is not handled here, because it can't currently
            // occur via the UI.
            // Note that Category.UNDEFINED also gets mapped to null.
            category = CategoryService.instance
                    .findCategoryById(category.id).orElse(null)
        }
        if (entity == null) {
            // Make a copy to keep entities and DTOs separated
            entity = Review(dto)
            if (dto.id == null) {
                entity.id = nextId.incrementAndGet()
            }
            reviews[entity.id!!] = entity
        } else {
            entity.score = dto.score
            entity.name = dto.name
            entity.date = dto.date
            entity.count = dto.count
        }
        entity.category = category
    }

    companion object {

        /**
         * Gets the unique instance of this Singleton.
         *
         * @return the unique instance of this Singleton
         */
        val instance: ReviewService
            get() = SingletonHolder.INSTANCE
    }
}
