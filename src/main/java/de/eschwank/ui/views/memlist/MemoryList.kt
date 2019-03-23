package de.eschwank.ui.views.memlist

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
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
        add(Button(VaadinIcon.NOTEBOOK.create()) { openNoteDlg() })
        add(buildNoteGrid())
    }

    private fun buildNoteGrid(): Grid<Note> {
        // TODO avoid double database calls
        noteGrid.dataProvider = DataProvider.fromCallbacks<Note>({
            Note.all(it.offset, it.limit)?.stream()
        }, {
            Note.all(it.offset, it.limit)?.stream()?.count()?.toInt() ?: 0
        })

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss")

        noteGrid.addColumn { it.title }.setHeader("Title")
        noteGrid.addColumn { it.content }.setHeader("Note")
        noteGrid.addColumn { formatter.format(it.creationTimeStamp) }.setHeader("Created")
        noteGrid.addColumn { formatter.format(it.lastUpdate) }.setHeader("Updated")
        noteGrid.addColumn { it.tags }.setHeader("Tags")

        // Disable selection: will receive only click events instead
        noteGrid.setSelectionMode(Grid.SelectionMode.NONE)

        noteGrid.addItemClickListener { openNoteDlg(it.item) }

        noteGrid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS)
        return noteGrid
    }

    private fun openNoteDlg(note: Note = Note()) {
        val memoryDialog = MemoryDialog(note)
        memoryDialog.addDialogCloseActionListener {
            noteGrid.dataProvider.refreshAll()
            memoryDialog.close()
        }
        memoryDialog.open()
    }
}
