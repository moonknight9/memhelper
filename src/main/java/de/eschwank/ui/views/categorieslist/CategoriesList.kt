/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.eschwank.ui.views.categorieslist

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.Notification.Position
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.function.SerializableFunction
import com.vaadin.flow.function.ValueProvider
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import de.eschwank.backend.Category
import de.eschwank.backend.CategoryService
import de.eschwank.backend.Review
import de.eschwank.backend.ReviewService
import de.eschwank.ui.MenuBar
import de.eschwank.ui.common.AbstractEditorDialog
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.ToIntFunction

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 */
@Route(value = "categories", layout = MenuBar::class)
@PageTitle("Categories List")
class CategoriesList : VerticalLayout() {

    private val searchField = TextField("",
            "Search categories")
    private val header = H2("Categories")
    private val grid = Grid<Category>()

    private val form = CategoryEditorDialog(
            BiConsumer { category, operation -> this.saveCategory(category, operation) }, Consumer { this.deleteCategory(it) })

    init {
        initView()

        addSearchBar()
        addContent()

        updateView()
    }

    private fun initView() {
        addClassName("categories-list")
        defaultHorizontalComponentAlignment = FlexComponent.Alignment.STRETCH
    }

    private fun addSearchBar() {
        val viewToolbar = Div()
        viewToolbar.addClassName("view-toolbar")

        searchField.prefixComponent = Icon("lumo", "search")
        searchField.addClassName("view-toolbar__search-field")
        searchField.addValueChangeListener { e -> updateView() }
        searchField.valueChangeMode = ValueChangeMode.EAGER

        val newButton = Button("New category", Icon("lumo", "plus"))
        newButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        newButton.addClassName("view-toolbar__button")
        newButton.addClickListener { e ->
            form.open(Category(),
                    AbstractEditorDialog.Operation.ADD)
        }

        viewToolbar.add(searchField, newButton)
        add(viewToolbar)
    }

    private fun addContent() {
        val container = VerticalLayout()
        container.className = "view-container"
        container.alignItems = FlexComponent.Alignment.STRETCH

        grid.addColumn { it.name }.setHeader("Name").setWidth("8em").isResizable = true
        grid.addColumn { this.getReviewCount(it) }.setHeader("Beverages").width = "6em"
        grid.addColumn(ComponentRenderer(SerializableFunction { this.createEditButton(it) })).flexGrow = 0
        grid.setSelectionMode(SelectionMode.NONE)

        container.add(header, grid)
        add(container)
    }

    private fun createEditButton(category: Category): Button {
        val edit = Button("Edit") {
            form.open(category, AbstractEditorDialog.Operation.EDIT)
        }
        edit.icon = Icon("lumo", "edit")
        edit.addClassName("review__edit")
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        if (CategoryService.instance.undefinedCategory.id == category.id) {
            edit.isEnabled = false
        }
        return edit
    }

    private fun getReviewCount(category: Category): String {
        val reviewsInCategory = ReviewService.instance
                .findReviews(category.name)
        val sum = reviewsInCategory.stream().mapToInt { it.count }.sum()
        return Integer.toString(sum)
    }

    private fun updateView() {
        val categories = CategoryService.instance
                .findCategories(searchField.value)
        grid.setItems(categories)

        if (searchField.value.isNotEmpty()) {
            header.text = "Search for “" + searchField.value + "”"
        } else {
            header.text = "Categories"
        }
    }

    private fun saveCategory(category: Category,
                             operation: AbstractEditorDialog.Operation) {
        CategoryService.instance.saveCategory(category)

        Notification.show(
                "Category successfully " + operation.nameInText + "ed.",
                3000, Position.BOTTOM_START)
        updateView()
    }

    private fun deleteCategory(category: Category) {
        val reviewsInCategory = ReviewService.instance
                .findReviews(category.name)

        reviewsInCategory.forEach { review ->
            review.category = CategoryService.instance.undefinedCategory
            ReviewService.instance.saveReview(review)
        }
        CategoryService.instance.deleteCategory(category)

        Notification.show("Category successfully deleted.", 3000,
                Position.BOTTOM_START)
        updateView()
    }
}
