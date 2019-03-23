package de.eschwank.ui.views.memlist

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import de.eschwank.backend.entities.Note
import java.time.LocalDateTime


class MemoryDialog(private var note: Note) : Dialog() {

    private val title = TextField("Title:", "Title...")
    private val content = TextField("Note:", "Type your note here!")

    private val saveButton = Button(VaadinIcon.CLOUD_UPLOAD_O.create()) {
        saveNote(title, content)
        fireEvent(DialogCloseActionEvent(this, true))
    }

    init {
        isCloseOnEsc = false

        setComponents()

        checkValidation()
    }

    private fun setComponents() {
        val layout = VerticalLayout()

        setRequired(title)
        setRequired(content)

        if (note.isPersistet()) {
            title.value = note.title
            content.value = note.content
        }

        layout.add(title, content)

        val btnLayout = getButtonLayout()
        add(layout, btnLayout)
    }

    private fun setRequired(field: TextField) {
        field.isRequired = true
        field.addValueChangeListener { checkValidation() }
        field.valueChangeMode = ValueChangeMode.EAGER
    }

    private fun checkValidation() {
        saveButton.isEnabled = !(title.isInvalid || title.value == "" || content.isInvalid || content.value == "")
    }

    private fun getButtonLayout(): HorizontalLayout {
        val btnLayout = HorizontalLayout()

        val cancelButton = Button(VaadinIcon.EXIT_O.create()) {
            fireEvent(DialogCloseActionEvent(this, true))
        }

        btnLayout.add(saveButton, cancelButton)
        if (note.isPersistet()) {
            val deleteButton = Button(VaadinIcon.BAN.create()) {
                note.delete()
                fireEvent(DialogCloseActionEvent(this, true))
            }
            btnLayout.add(deleteButton)
        }
        return btnLayout
    }

    private fun saveNote(title: TextField, content: TextField) {
        note.title = title.value
        note.content = content.value
        if (note.isPersistet()) {
            note.lastUpdate = LocalDateTime.now()
        }
        note.save()
    }
}