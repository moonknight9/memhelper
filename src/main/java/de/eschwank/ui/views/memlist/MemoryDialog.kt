package de.eschwank.ui.views.memlist

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import de.eschwank.backend.entities.Note
import java.time.LocalDateTime


class MemoryDialog(private var note: Note) : Dialog() {

    init {
        isCloseOnEsc = false

        setComponents()
    }

    private fun setComponents() {
        val layout = VerticalLayout()

        val title = TextField("Title:", note.title, "Title...")
        title.isRequired = true

        val content = TextField("Note:", note.content, "Type your note here!")
        content.isRequired = true

        layout.add(title, content)

        val btnLayout = getButtonLayout(title, content)
        add(layout, btnLayout)
    }

    private fun getButtonLayout(title: TextField, content: TextField): HorizontalLayout {
        val btnLayout = HorizontalLayout()
        val confirmButton = Button(VaadinIcon.CLOUD_UPLOAD_O.create()) {
            saveNote(title, content)
            fireEvent(DialogCloseActionEvent(this, true))
        }
        val cancelButton = Button(VaadinIcon.EXIT_O.create()) {
            fireEvent(DialogCloseActionEvent(this, true))
        }

        btnLayout.add(confirmButton, cancelButton)
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