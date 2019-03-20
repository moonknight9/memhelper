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
import java.util.function.Consumer

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.shared.Registration

/**
 * A generic dialog for confirming or cancelling an action.
 *
 * @param <T>
 * The type of the action's subject
</T> */
internal class ConfirmationDialog<T : Serializable> : Dialog() {

    private val titleField = H3()
    private val messageLabel = Div()
    private val extraMessageLabel = Div()
    private val confirmButton = Button()
    private val cancelButton = Button("Cancel")
    private var registrationForConfirm: Registration? = null
    private var registrationForCancel: Registration? = null

    /**
     * Constructor.
     */
    init {
        isCloseOnEsc = true
        isCloseOnOutsideClick = false

        confirmButton.addClickListener { close() }
        confirmButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        confirmButton.isAutofocus = true
        cancelButton.addClickListener { close() }
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)

        val buttonBar = HorizontalLayout(confirmButton,
                cancelButton)
        buttonBar.className = "buttons confirm-buttons"

        val labels = Div(messageLabel, extraMessageLabel)
        labels.className = "confirm-text"

        titleField.className = "confirm-title"

        add(titleField, labels, buttonBar)
    }

    /**
     * Opens the confirmation dialog.
     *
     * The dialog will display the given title and message(s), then call
     * `confirmHandler` if the Confirm button is clicked, or
     * `cancelHandler` if the Cancel button is clicked.
     *
     * @param title
     * The title text
     * @param message
     * Detail message (optional, may be empty)
     * @param additionalMessage
     * Additional message (optional, may be empty)
     * @param actionName
     * The action name to be shown on the Confirm button
     * @param isDisruptive
     * True if the action is disruptive, such as deleting an item
     * @param item
     * The subject of the action
     * @param confirmHandler
     * The confirmation handler function
     * @param cancelHandler
     * The cancellation handler function
     */
    fun open(title: String, message: String, additionalMessage: String,
             actionName: String, isDisruptive: Boolean, item: T,
             confirmHandler: Consumer<T>, cancelHandler: Runnable?) {
        titleField.text = title
        messageLabel.text = message
        extraMessageLabel.text = additionalMessage
        confirmButton.text = actionName

        val cancelAction = cancelHandler ?: NO_OP

        if (registrationForConfirm != null) {
            registrationForConfirm!!.remove()
        }
        registrationForConfirm = confirmButton
                .addClickListener { confirmHandler.accept(item) }
        if (registrationForCancel != null) {
            registrationForCancel!!.remove()
        }
        registrationForCancel = cancelButton
                .addClickListener {
                    cancelAction.run {}
                }
        this.addOpenedChangeListener { e ->
            if (!e.isOpened) {
                cancelAction.run {}
            }
        }
        confirmButton.removeThemeVariants(ButtonVariant.LUMO_ERROR)
        if (isDisruptive) {
            confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR)
        }
        open()
    }

    companion object {

        private val NO_OP = { }
    }
}
