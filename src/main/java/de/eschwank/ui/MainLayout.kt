/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.eschwank.ui

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.RouterLayout

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 *
 * //@HtmlImport("frontend://styles/shared-styles.html")
 */
//@PWA(name = "Beverage Buddy", shortName = "BevBuddy")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
class MainLayout : Div(), RouterLayout//H2 title = new H2("AmazingApp");
//add(title);
//title.addClassName("main-layout__title");
/*RouterLink reviews = new RouterLink(null, ReviewsList.class);
        reviews.add(new Icon(VaadinIcon.LIST), new Text("Reviews"));
        reviews.addClassName("main-layout__nav-item");
        // Only show as active for the exact URL, but not for sub paths
        reviews.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink categories = new RouterLink(null, CategoriesList.class);
        categories.add(new Icon(VaadinIcon.ARCHIVES), new Text("Categories"));
        categories.addClassName("main-layout__nav-item");

        RouterLink memories = new RouterLink(null, MemoryList.class);
        memories.add(new Icon(VaadinIcon.BOOKMARK), new Text("Memories"));
        memories.addClassName("main-layout__nav-item");

        RouterLink todos = new RouterLink(null, ToDoList.class);
        memories.add(new Icon(VaadinIcon.CLIPBOARD_CHECK), new Text("TODOs"));
        memories.addClassName("main-layout__nav-item");

        Div navigation = new Div(reviews, categories, memories, todos);
        navigation.addClassName("main-layout__nav");

        Div header = new Div(title, navigation);
        header.addClassName("main-layout__header");
        add(header);*///addClassName("main-layout");
/*@Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes");
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
    }*/

