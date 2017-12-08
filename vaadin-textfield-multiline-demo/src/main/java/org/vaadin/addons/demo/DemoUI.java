package org.vaadin.addons.demo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.textfieldmultiline.TextFieldMultiline;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("TextFieldMultiselect Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @WebServlet(
        value = "/*",
        asyncSupported = true)
    @VaadinServletConfiguration(
        productionMode = false,
        ui = DemoUI.class,
        widgetset = "org.vaadin.addons.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        // Initialize our new UI component
        final TextFieldMultiline component = new TextFieldMultiline();
        component.setCaption("Caption");
        component.setInputPrompt("Enter multiline here");
        // component.setEnabled(true);

        // Initialize our new UI component
        final TextFieldMultiline disabledComponent = new TextFieldMultiline();
        disabledComponent.setCaption("Caption (disabled)");
        disabledComponent.setInputPrompt("Enter multiline here");
        List<String> list = new ArrayList<>();
        list.add("ABC");
        list.add("DEF");
        disabledComponent.setValue(list);
        disabledComponent.setEnabled(false);

        Button btnClear = new Button("clear");
        btnClear.addClickListener(e -> component.clear());

        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.addComponent(component);
        hLayout.addComponent(btnClear);
        hLayout.setSpacing(true);
        hLayout.setComponentAlignment(btnClear, Alignment.BOTTOM_RIGHT);
        layout.addComponent(hLayout);
        layout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
        layout.addComponent(disabledComponent);
        layout.setComponentAlignment(disabledComponent, Alignment.MIDDLE_CENTER);
        setContent(layout);

    }

}
