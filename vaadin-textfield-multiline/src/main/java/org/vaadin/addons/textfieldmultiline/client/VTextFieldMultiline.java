package org.vaadin.addons.textfieldmultiline.client;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.vaadin.client.ui.Field;
import com.vaadin.client.ui.VTextArea;
import com.vaadin.client.ui.VTextField;
import com.vaadin.client.ui.VVerticalLayout;

// Extend any GWT Widget
public class VTextFieldMultiline extends Composite implements Field {

    VTextField textField = new VTextField();
    VTextArea textArea = new VTextArea();
    VVerticalLayout layout = new VVerticalLayout();
    HTML resetButtonImage = new HTML();

    // Entered values
    public String[] values;

    /** For internal use only. May be removed or replaced in the future. */
    public boolean enableDebug;

    public VTextFieldMultiline() {
        this.resetButtonImage.setStyleName("resetbuttonfortextfieldmultiline-resetbutton");

        this.textArea.setVisible(false);
        this.textArea.setRows(5);

        this.textField.addStyleName("v-widget");
        this.textField.addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(final FocusEvent event) {
                if (!VTextFieldMultiline.this.textField.isReadOnly()) {
                    VTextFieldMultiline.this.layout.setHeight((VTextFieldMultiline.this.textField.getElement()
                        .getClientHeight() + 2) + "px");
                    VTextFieldMultiline.this.textArea.setWidth((VTextFieldMultiline.this.textField.getElement()
                        .getClientWidth() + 2) + "px");

                    VTextFieldMultiline.this.textField.setVisible(false);
                    VTextFieldMultiline.this.textArea.setVisible(true);
                    VTextFieldMultiline.this.textArea.setFocus(true);
                    VTextFieldMultiline.this.textArea.setCursorPos(VTextFieldMultiline.this.textArea.getText()
                        .length());
                }
            }

        });

        this.layout.add(this.textField);
        this.layout.add(this.textArea);
        this.layout.add(this.resetButtonImage);
        initWidget(this.layout);

        // CSS class-name should not be v- prefixed
        setStyleName("vaadin-textfield-multiline");

        // State is set to widget in MyComponentConnector

    }

    @Override
    public void setWidth(final String width) {
        super.setWidth(width);
        this.layout.setWidth(width);
        this.textField.setWidth(width);
        this.textArea.setWidth(width);
    }

    @Override
    public void setHeight(final String height) {
        super.setHeight(height);
        this.layout.setHeight(height);
        this.textField.setHeight(height);
    }
}