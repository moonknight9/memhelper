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

import java.util.function.BiConsumer
import java.util.function.Consumer

import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Setter
import com.vaadin.flow.data.validator.StringLengthValidator
import com.vaadin.flow.function.SerializableFunction
import com.vaadin.flow.function.ValueProvider
import de.eschwank.backend.Category
import de.eschwank.backend.CategoryService
import de.eschwank.backend.ReviewService
import de.eschwank.ui.common.AbstractEditorDialog

/**
 * A dialog for editing [Category] objects.
 */
class CategoryEditorDialog(itemSaver: BiConsumer<Category, AbstractEditorDialog.Operation>,
                           itemDeleter: Consumer<Category>) : AbstractEditorDialog<Category>("category", itemSaver, itemDeleter) {

    private val categoryNameField = TextField("Name")

    init {

        addNameField()
    }

    private fun addNameField() {
        formLayout.add(categoryNameField)

        binder.forField(categoryNameField)
                .withConverter<String>({ it.trim { it <= ' ' } }, { it.trim { it <= ' ' } })
                .withValidator(StringLengthValidator(
                        "Category name must contain at least 3 printable characters",
                        3, null))
                .withValidator(
                        { name ->
                            CategoryService.instance.findCategories(name).isEmpty()
                        },
                        "Category name must be unique")
                .bind({ it.name }, { obj, name -> obj.name = name })
    }

    override fun confirmDelete() {
        val reviewCount = ReviewService.instance
                .findReviews(currentItem!!.name).size
        if (reviewCount > 0) {
            openConfirmationDialog("Delete category",
                    "Are you sure you want to delete the “"
                            + currentItem!!.name
                            + "” category? There are " + reviewCount
                            + " reviews associated with this category.",
                    "Deleting the category will mark the associated reviews as “undefined”. " + "You can edit individual reviews to select another category.")
        } else {
            doDelete(currentItem!!)
        }
    }
}
