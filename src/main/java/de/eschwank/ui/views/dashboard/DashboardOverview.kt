package de.eschwank.ui.views.dashboard

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import de.eschwank.backend.entities.Note
import de.eschwank.ui.MenuBar


@Route(value = DashboardOverview.ROUTE, layout = MenuBar::class)
@PageTitle("Dashboard")
class DashboardOverview : VerticalLayout(){
    companion object {
        const val ROUTE = "dashboard"
    }

    init {
        //TODO replace this call
        val tags = Note.all()?.stream()?.map { it.tags }?.reduce{ t: Set<String>, u: Set<String> -> t.union(u) }
        tags?.ifPresent {
            it.forEach { add(Button(it){ /*OPEN TAG TABLE*/ }) }
        }
    }
}
