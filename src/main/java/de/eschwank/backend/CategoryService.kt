package de.eschwank.backend

import java.util.HashMap
import java.util.LinkedHashSet
import java.util.Objects
import java.util.Optional
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors

/**
 * Simple backend service to store and retrieve [Category] instances.
 */
class CategoryService
/**
 * Declared private to ensure uniqueness of this Singleton.
 */
private constructor() {

    private val categories = HashMap<Long, Category>()
    private val nextId = AtomicLong(0)
    private val undefinedCategoryId = AtomicLong(-1)

    /**
     * Returns a dedicated undefined category.
     *
     * @return the undefined category
     */
    val undefinedCategory: Category
        get() = categories[undefinedCategoryId.get()]!!

    /**
     * Helper class to initialize the singleton Service in a thread-safe way and
     * to keep the initialization ordering clear between the two services. See
     * also: https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private object SingletonHolder {
        internal val INSTANCE = createDemoCategoryService()

        private fun createDemoCategoryService(): CategoryService {
            val categoryService = CategoryService()
            val categoryNames = LinkedHashSet(
                    StaticData.BEVERAGES.values)

            categoryNames.forEach { name ->
                val category = categoryService
                        .doSaveCategory(Category(name))
                if (StaticData.UNDEFINED == name) {
                    categoryService.undefinedCategoryId.set(category.id!!)
                }
            }

            return categoryService
        }
    }
    /** This class is not meant to be instantiated.  */

    /**
     * Fetches the categories whose name matches the given filter text.
     *
     * The matching is case insensitive. When passed an empty filter text, the
     * method returns all categories. The returned list is ordered by name.
     *
     * @param filter
     * the filter text
     * @return the list of matching categories
     */
    fun findCategories(filter: String): List<Category> {
        val normalizedFilter = filter.toLowerCase()

        // Make a copy of each matching item to keep entities and DTOs separated
        return categories.values.stream()
                .filter { it.name.toLowerCase().contains(normalizedFilter)}
                .map { Category(it) }
                .sorted { c1, c2 ->
                    c1.name.compareTo(c2.name, ignoreCase = true)
                }.collect(Collectors.toList())
    }

    /**
     * Searches for the exact category whose name matches the given filter text.
     *
     * The matching is case insensitive.
     *
     * @param name
     * the filter text
     * @return an [Optional] containing the category if found, or
     * [Optional.empty]
     * @throws IllegalStateException
     * if the result is ambiguous
     */
    fun findCategoryByName(name: String): Optional<Category> {
        val categoriesMatching = findCategories(name)

        if (categoriesMatching.isEmpty()) {
            return Optional.empty()
        }
        if (categoriesMatching.size > 1) {
            throw IllegalStateException(
                    "Category $name is ambiguous")
        }
        return Optional.of(categoriesMatching[0])
    }

    /**
     * Fetches the exact category whose name matches the given filter text.
     *
     * Behaves like [.findCategoryByName], except that returns a
     * [Category] instead of an [Optional]. If the category can't be
     * identified, an exception is thrown.
     *
     * @param name
     * the filter text
     * @return the category, if found
     * @throws IllegalStateException
     * if not exactly one category matches the given name
     */
    fun findCategoryOrThrow(name: String): Category {
        return findCategoryByName(name)
                .orElseThrow {
                    IllegalStateException(
                            "Category $name does not exist")
                }
    }

    /**
     * Searches for the exact category with the given id.
     *
     * @param id
     * the category id
     * @return an [Optional] containing the category if found, or
     * [Optional.empty]
     */
    fun findCategoryById(id: Long?): Optional<Category> {
        val category = categories[id]
        return Optional.ofNullable(category)
    }

    /**
     * Deletes the given category from the category store.
     *
     * @param category
     * the category to delete
     * @return true if the operation was successful, otherwise false
     */
    fun deleteCategory(category: Category): Boolean {
        if (category.id != null && undefinedCategoryId.get() == category.id!!.toLong()) {
            throw IllegalArgumentException(
                    "Undefined category may not be removed")
        }
        return categories.remove(category.id) != null
    }

    /**
     * Persists the given category into the category store.
     *
     * If the category is already persistent, the saved category will get
     * updated with the name of the given category object. If the category is
     * new (i.e. its id is null), it will get a new unique id before being
     * saved.
     *
     * @param dto
     * the category to save
     */
    fun saveCategory(dto: Category) {
        doSaveCategory(dto)
    }

    private fun doSaveCategory(dto: Category): Category {
        var entity: Category? = categories[dto.id]

        if (entity == null) {
            // Make a copy to keep entities and DTOs separated
            entity = Category(dto)
            if (dto.id == null) {
                entity.id = nextId.incrementAndGet()
            }
            categories[entity.id!!] = entity
        } else if (undefinedCategoryId.get() == dto.id!!.toLong() && entity.name != dto.name) {
            throw IllegalArgumentException(
                    "Undefined category may not be renamed")
        } else {
            entity.name = dto.name
        }
        return entity
    }

    companion object {

        /**
         * Gets the unique instance of this Singleton.
         *
         * @return the unique instance of this Singleton
         */
        val instance: CategoryService
            get() = SingletonHolder.INSTANCE
    }

}
