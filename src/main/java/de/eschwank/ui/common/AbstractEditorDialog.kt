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
package de.eschwank.ui.common

import java.io.Serializable
import java.util.function.BiConsumer
import java.util.function.Consumer

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.BinderValidationStatus
import com.vaadin.flow.shared.Registration

/**
 * Abstract base class for dialogs adding, editing or deleting items.
 *
 * Subclasses are expected to
 *
 *  * add, during construction, the needed UI components to
 * [.getFormLayout] and bind them using [.getBinder], as well
 * as
 *  * override [.confirmDelete] to open the confirmation dialog with
 * the desired message (by calling
 * [.openConfirmationDialog].
 *
 *
 * @param <T>
 * the type of the item to be added, edited or deleted
</T> */
abstract class AbstractEditorDialog<T : Serializable>
/**
 * Constructs a new instance.
 *
 * @param itemType
 * The readable name of the item type
 * @param itemSaver
 * Callback to save the edited item
 * @param itemDeleter
 * Callback to delete the edited item
 */
protected constructor(private val itemType: String,
                      private val itemSaver: BiConsumer<T, Operation>, private val itemDeleter: Consumer<T>) : Dialog() {

    private val titleField = H3()
    private val saveButton = Button("Save")
    private val cancelButton = Button("Cancel")
    private val deleteButton = Button("Delete")
    private var registrationForSave: Registration? = null

    /**
     * Gets the form layout, where additional components can be added for
     * displaying or editing the item's properties.
     *
     * @return the form layout
     */
    protected val formLayout = FormLayout()
    private val buttonBar = HorizontalLayout(saveButton,
            cancelButton, deleteButton)

    /**
     * Gets the binder.
     *
     * @return the binder
     */
    protected val binder = Binder<T>()
    /**
     * Gets the item currently being edited.
     *
     * @return the item currently being edited
     */
    protected var currentItem: T? = null
        private set

    private val confirmationDialog = ConfirmationDialog<T>()

    /**
     * The operations supported by this dialog. Delete is enabled when editing
     * an already existing item.
     */
    enum class Operation private constructor(val nameInTitle: String, val nameInText: String,
                                             val isDeleteEnabled: Boolean) {
        ADD("New", "add", false), EDIT("Edit", "edit", true)
    }

    init {

        initTitle()
        initFormLayout()
        initButtonBar()
        isCloseOnEsc = true
        isCloseOnOutsideClick = false
    }

    private fun initTitle() {
        add(titleField)
    }

    private fun initFormLayout() {
        formLayout.setResponsiveSteps(FormLayout.ResponsiveStep("0", 1),
                FormLayout.ResponsiveStep("25em", 2))
        val div = Div(formLayout)
        div.addClassName("has-padding")
        add(div)
    }

    private fun initButtonBar() {
        saveButton.isAutofocus = true
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        cancelButton.addClickListener { e -> close() }
        deleteButton.addClickListener { e -> deleteClicked() }
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR)
        buttonBar.className = "buttons"
        buttonBar.isSpacing = true
        add(buttonBar)
    }

    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item
     * The item to edit; it may be an existing or a newly created
     * instance
     * @param operation
     * The operation being performed on the item
     */
    fun open(item: T, operation: Operation) {
        currentItem = item
        titleField.text = operation.nameInTitle + " " + itemType
        if (registrationForSave != null) {
            registrationForSave!!.remove()
        }
        registrationForSave = saveButton
                .addClickListener { e -> saveClicked(operation) }
        binder.readBean(currentItem)

        deleteButton.isEnabled = operation.isDeleteEnabled
        open()
    }

    private fun saveClicked(operation: Operation) {
        val isValid = binder.writeBeanIfValid(currentItem!!)

        if (isValid) {
            itemSaver.accept(currentItem!!, operation)
            close()
        } else {
            val status = binder.validate()
        }
    }

    private fun deleteClicked() {
        if (confirmationDialog.element.parent == null) {
            ui.ifPresent { ui -> ui.add(confirmationDialog) }
        }
        confirmDelete()
    }

    protected abstract fun confirmDelete()

    /**
     * Opens the confirmation dialog before deleting the current item.
     *
     * The dialog will display the given title and message(s), then call
     * [.deleteConfirmed] if the Delete button is clicked.
     *
     * @param title
     * The title text
     * @param message
     * Detail message (optional, may be empty)
     * @param additionalMessage
     * Additional message (optional, may be empty)
     */
    protected fun openConfirmationDialog(title: String, message: String,
                                         additionalMessage: String) {
        confirmationDialog.open(title, message, additionalMessage, "Delete",
                true, currentItem!!, Consumer { this.deleteConfirmed(it) }, null)
    }

    /**
     * Removes the `item` from the backend and close the dialog.
     *
     * @param item
     * the item to delete
     */
    protected fun doDelete(item: T) {
        itemDeleter.accept(item)
        close()
    }

    private fun deleteConfirmed(item: T) {
        doDelete(item)
    }
}
