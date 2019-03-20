package de.eschwank.ui.views.memlist

import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.NativeButton
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import de.eschwank.backend.entities.Note
import de.eschwank.ui.MenuBar

import java.time.format.DateTimeFormatter

@Route(value = MemoryList.ROUTE, layout = MenuBar::class)
@PageTitle("Memories")
class MemoryList : VerticalLayout() {
    companion object {
        const val ROUTE = "memories"
    }

    private val noteGrid = Grid<Note>()

    init {
        add(NativeButton("New Note", ComponentEventListener {
            val memoryDialog = MemoryDialog()
            memoryDialog.addDialogCloseActionListener { noteGrid.dataProvider.refreshAll() }
            memoryDialog.open()
        }))
        add(buildNoteGrid())
    }

    private fun buildNoteGrid(): Grid<Note> {
        noteGrid.dataProvider = DataProvider.fromCallbacks<Note>({ Note.all()?.stream() }, { Note.all()?.size ?: 0 })

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss")

        noteGrid.addColumn { it.title }.setHeader("Title")
        noteGrid.addColumn { it.content }.setHeader("Note")
        noteGrid.addColumn { formatter.format(it.creationTimeStamp) }.setHeader("Created")
        noteGrid.addColumn { it.tags }.setHeader("Tags")

        //TODO open Dlg
        // Disable selection: will receive only click events instead
        noteGrid.setSelectionMode(Grid.SelectionMode.NONE)

        noteGrid.addItemClickListener {
            val memoryDialog = MemoryDialog(it.item)
            memoryDialog.addDialogCloseActionListener { noteGrid.dataProvider.refreshAll() }
            memoryDialog.open()
        }
        noteGrid.dataProvider.refreshAll()

        noteGrid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS)
        return noteGrid
    }
}
