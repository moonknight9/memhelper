package de.eschwank.ui.views.memlist

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.NativeButton
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import de.eschwank.backend.entities.Note


class MemoryDialog : Dialog {

    private var note = Note()

    private val title = TextField("Title:")
    private val content = TextField("Note:")

    constructor() : super()

    constructor(note: Note) : this() {
        this.note = note
        title.value = note.title
        content.value = note.content
    }

    init {
        val layout = VerticalLayout()

        title.isRequired = true
        content.isRequired = true

        layout.add(title, content)

        val btnLayout = HorizontalLayout()
        val confirmButton = NativeButton("Confirm") {
            note.title = title.value
            note.content = content.value
            note.save()
            close()
        }
        val cancelButton = NativeButton("Cancel") {
            close()
        }
        btnLayout.add(confirmButton, cancelButton)
        add(layout, btnLayout)
    }
}