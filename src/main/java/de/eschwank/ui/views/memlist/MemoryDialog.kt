package de.eschwank.ui.views.memlist

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import de.eschwank.backend.entities.Note
import java.time.LocalDateTime
import java.util.stream.Collectors


class MemoryDialog(private var note: Note) : Dialog() {

    private val title = TextField("Title:", "Title...")
    private val content = TextArea("Note:", "Type your note here!")
    private val tags = TextField("Tags:", "Tag1;Tag2;Tag3")

    private val saveButton = Button(VaadinIcon.CLOUD_UPLOAD_O.create()) {
        saveNote()
        fireEvent(DialogCloseActionEvent(this, true))
    }

    init {
        isCloseOnEsc = false
        height = "40em"
        width = "70em"

        setComponents()

        checkValidation()
    }

    private fun setComponents() {
        val layout = VerticalLayout()
        layout.setSizeFull()

        setRequired(title)
        setRequired(content)
        content.setSizeFull()

        if (note.isPersistet()) {
            buildNote()
        }

        val header = HorizontalLayout(title, tags)

        val btnLayout = getButtonLayout()
        layout.add(header, content, btnLayout)
        add(layout)
    }

    private fun setRequired(field: TextField) {
        field.isRequired = true
        field.addValueChangeListener { checkValidation() }
        field.valueChangeMode = ValueChangeMode.EAGER
    }

    private fun setRequired(field: TextArea) {
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

    private fun saveNote() {
        note.title = title.value
        note.content = content.value
        saveTags()
        if (note.isPersistet()) {
            note.lastUpdate = LocalDateTime.now()
        }
        note.save()
    }

    private fun buildNote() {
        title.value = note.title
        content.value = note.content
        tags.value = note.tags.stream().collect(Collectors.joining(";"))
    }

    private fun saveTags() {
        if (tags.value != "") {
            note.tags = tags.value.split(";").stream()
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .collect(Collectors.toSet())
        }
    }
}