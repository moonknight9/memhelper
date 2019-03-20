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

import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Setter
import com.vaadin.flow.data.converter.StringToIntegerConverter
import com.vaadin.flow.data.validator.DateRangeValidator
import com.vaadin.flow.data.validator.IntegerRangeValidator
import com.vaadin.flow.data.validator.StringLengthValidator
import com.vaadin.flow.function.ValueProvider
import de.eschwank.backend.Category
import de.eschwank.backend.CategoryService
import de.eschwank.backend.Review
import de.eschwank.ui.common.AbstractEditorDialog
import java.time.LocalDate
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * A dialog for editing [Review] objects.
 */
class ReviewEditorDialog(saveHandler: BiConsumer<Review, AbstractEditorDialog.Operation>,
                         deleteHandler: Consumer<Review>) : AbstractEditorDialog<Review>("review", saveHandler, deleteHandler) {

    @Transient
    private val categoryService = CategoryService
            .instance

    private val categoryBox = ComboBox<Category>()
    private val scoreBox = ComboBox<String>()
    private val lastTasted = DatePicker()
    private val beverageName = TextField()
    private val timesTasted = TextField()

    init {

        createNameField()
        createCategoryBox()
        createDatePicker()
        createTimesField()
        createScoreBox()
    }

    private fun createScoreBox() {
        scoreBox.label = "Rating"
        scoreBox.isRequired = true
        scoreBox.isAllowCustomValue = false
        scoreBox.setItems("1", "2", "3", "4", "5")
        formLayout.add(scoreBox)

        binder.forField(scoreBox)
                .withConverter(StringToIntegerConverter(0,
                        "The score should be a number."))
                .withValidator(IntegerRangeValidator(
                        "The tasting count must be between 1 and 5.", 1, 5))
                .bind({ it.score }, { obj, score -> obj.score = score })
    }

    private fun createDatePicker() {
        lastTasted.label = "Last tasted"
        lastTasted.isRequired = true
        lastTasted.max = LocalDate.now()
        lastTasted.min = LocalDate.of(1, 1, 1)
        lastTasted.value = LocalDate.now()
        formLayout.add(lastTasted)

        binder.forField(lastTasted)
                .withValidator({ Objects.nonNull(it) },
                        "The date should be in MM/dd/yyyy format.")
                .withValidator(DateRangeValidator(
                        "The date should be neither Before Christ nor in the future.",
                        LocalDate.of(1, 1, 1), LocalDate.now()))
                .bind({ it.date }, { obj, date -> obj.date = date })

    }

    private fun createCategoryBox() {
        categoryBox.label = "Category"
        categoryBox.isRequired = true
        categoryBox.setItemLabelGenerator { it.name }
        categoryBox.isAllowCustomValue = false
        categoryBox.setItems(categoryService.findCategories(""))
        formLayout.add(categoryBox)

        binder.forField(categoryBox)
                .withValidator({ Objects.nonNull(it) },
                        "The category should be defined.")
                .bind({ it.category }, { obj, category -> obj.category = category })
    }

    private fun createTimesField() {
        timesTasted.label = "Times tasted"
        timesTasted.isRequired = true
        timesTasted.pattern = "[0-9]*"
        timesTasted.isPreventInvalidInput = true
        formLayout.add(timesTasted)

        binder.forField(timesTasted)
                .withConverter(
                        StringToIntegerConverter(0, "Must enter a number."))
                .withValidator(IntegerRangeValidator(
                        "The tasting count must be between 1 and 99.", 1, 99))
                .bind({ it.count }, { obj, count -> obj.count = count })
    }

    private fun createNameField() {
        beverageName.label = "Beverage"
        beverageName.isRequired = true
        formLayout.add(beverageName)

        binder.forField(beverageName)
                .withConverter<String>({ it.trim { it <= ' ' } }, { it.trim { it <= ' ' } })
                .withValidator(StringLengthValidator(
                        "Beverage name must contain at least 3 printable characters",
                        3, null))
                .bind({ it.name }, { obj, name -> obj.name = name })
    }

    override fun confirmDelete() {
        openConfirmationDialog("Delete review",
                "Are you sure you want to delete the review for “" + currentItem!!.name + "”?", "")
    }

}
