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
package de.eschwank.ui.views.reviewslist

import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.Notification.Position
import com.vaadin.flow.component.polymertemplate.EventHandler
import com.vaadin.flow.component.polymertemplate.Id
import com.vaadin.flow.component.polymertemplate.ModelItem
import com.vaadin.flow.component.polymertemplate.PolymerTemplate
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.templatemodel.Encode
import com.vaadin.flow.templatemodel.TemplateModel
import de.eschwank.backend.Review
import de.eschwank.backend.ReviewService
import de.eschwank.ui.MainLayout
import de.eschwank.ui.common.AbstractEditorDialog
import de.eschwank.ui.encoders.LocalDateToStringEncoder
import de.eschwank.ui.encoders.LongToStringEncoder
import de.eschwank.ui.views.reviewslist.ReviewsList.ReviewsModel
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 *
 * Implemented using a simple template.
 */
@Route(value = "", layout = MainLayout::class)
@PageTitle("Review List")
@Tag("reviews-list")
@HtmlImport("frontend://src/views/reviewslist/reviews-list.html")
class ReviewsList : PolymerTemplate<ReviewsModel>() {

    @Id("search")
    private val search: TextField? = null
    @Id("newReview")
    private val addReview: Button? = null
    @Id("header")
    private val header: H2? = null

    private val reviewForm = ReviewEditorDialog(
            BiConsumer { review, operation -> this.saveUpdate(review, operation) }, Consumer { this.deleteUpdate(it) })

    interface ReviewsModel : TemplateModel {
        @Encode(value = LongToStringEncoder::class, path = "id")
        //@Encode(value = LocalDateToStringEncoder::class, path = "date")
        //@Encode(value = LongToStringEncoder::class, path = "category.id")
        fun setReviews(reviews: List<Review>)
    }

    init {
        search!!.placeholder = "Search reviews"
        search.addValueChangeListener { e -> updateList() }
        search.valueChangeMode = ValueChangeMode.EAGER

        addReview!!.addClickListener { e ->
            openForm(Review(),
                    AbstractEditorDialog.Operation.ADD)
        }

        // Set review button and edit button text from Java
        element.setProperty("reviewButtonText", "New review")
        element.setProperty("editButtonText", "Edit")

        updateList()

    }

    fun saveUpdate(review: Review,
                   operation: AbstractEditorDialog.Operation) {
        ReviewService.instance.saveReview(review)
        updateList()
        Notification.show(
                "Beverage successfully " + operation.nameInText + "ed.",
                3000, Position.BOTTOM_START)
    }

    fun deleteUpdate(review: Review) {
        ReviewService.instance.deleteReview(review)
        updateList()
        Notification.show("Beverage successfully deleted.", 3000,
                Position.BOTTOM_START)
    }

    private fun updateList() {
        val reviews = ReviewService.instance
                .findReviews(search!!.value)
        if (search.isEmpty) {
            header!!.text = "Reviews"
            header.add(Span("${reviews.size} in total"))
        } else {
            header!!.text = "Search for “" + search.value + "”"
            if (!reviews.isEmpty()) {
                header.add(Span("${reviews.size} results"))
            }
        }
        model.setReviews(reviews)
    }

    @EventHandler
    private fun edit(@ModelItem review: Review) {
        openForm(review, AbstractEditorDialog.Operation.EDIT)
    }

    private fun openForm(review: Review,
                         operation: AbstractEditorDialog.Operation) {
        // Add the form lazily as the UI is not yet initialized when
        // this view is constructed
        if (reviewForm.element.parent == null) {
            ui.ifPresent { ui -> ui.add(reviewForm) }
        }
        reviewForm.open(review, operation)
    }

}
