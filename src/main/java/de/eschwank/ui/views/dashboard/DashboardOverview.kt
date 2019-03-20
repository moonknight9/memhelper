package de.eschwank.ui.views.dashboard

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import de.eschwank.ui.MenuBar


@Route(value = DashboardOverview.ROUTE, layout = MenuBar::class)
@PageTitle("Dashboard")
class DashboardOverview : VerticalLayout(){
    companion object {
        const val ROUTE = "dashboard"
    }

    init {

    }
}
