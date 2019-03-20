package de.eschwank.ui

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.AppLayoutMenu
import com.vaadin.flow.component.applayout.AppLayoutMenuItem
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.ParentLayout
import com.vaadin.flow.router.RouterLayout
import de.eschwank.ui.views.dashboard.DashboardOverview
import de.eschwank.ui.views.memlist.MemoryList
import de.eschwank.ui.views.todolist.ToDoList

@ParentLayout(MainLayout::class)
class MenuBar : Div(), RouterLayout {
    init {
        val appLayout = AppLayout()
        appLayout.element.style.set("position", "inherit")
        appLayout.setBranding(H3("Amazing App"))
        val menu = appLayout.createMenu()
        menu.addMenuItems(
                AppLayoutMenuItem(VaadinIcon.LIST.create(), "Reviews", ""),
                AppLayoutMenuItem(VaadinIcon.ARCHIVES.create(), "Categories", "categories"),
                AppLayoutMenuItem(VaadinIcon.DASHBOARD.create(), "Dashboard", DashboardOverview.ROUTE),
                AppLayoutMenuItem(VaadinIcon.BOOKMARK.create(), "Memories", MemoryList.ROUTE),
                AppLayoutMenuItem(VaadinIcon.CLIPBOARD_CHECK.create(), "TODOs", ToDoList.ROUTE)
        )
        add(appLayout)
    }
}
