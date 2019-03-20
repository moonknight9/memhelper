package de.eschwank.ui.views.todolist

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import de.eschwank.ui.MenuBar

@Route(value = ToDoList.ROUTE, layout = MenuBar::class)
@PageTitle("TODOs")
class ToDoList : VerticalLayout() {
    companion object {
        const val ROUTE = "todos"
    }

    init {
        add(H1("TODO... ;)"))
    }
}
