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

	HTML resetButtonImage = new HTML();
	VTextField textField = new VTextField();
	VTextArea textArea = new VTextArea();
	
	// Entered values
	public String[] values;
	
	/** For internal use only. May be removed or replaced in the future. */
	public boolean enableDebug;
	
	public VTextFieldMultiline() {

		resetButtonImage.setStyleName("resetbuttonfortextfieldmultiline-resetbutton");

		textArea.setVisible(false);
		textArea.setRows(5);
		
		final VVerticalLayout layout = new VVerticalLayout();
		
		textField.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				if (!textField.isReadOnly()) {
					layout.setHeight((textField.getElement().getClientHeight() + 2) + "px");
					layout.setWidth((textField.getElement().getClientWidth() + 2) + "px");
					textArea.setWidth((textField.getElement().getClientWidth() + 2) + "px");
					
					textField.setVisible(false);
					textArea.setVisible(true);
					textArea.setFocus(true);
					textArea.setCursorPos(textArea.getText().length());
				}
			}
			
		});
		
		layout.add(textField);
		layout.add(textArea);
		layout.add(resetButtonImage);
		initWidget(layout);
		
		// CSS class-name should not be v- prefixed
		setStyleName("vaadin-textfield-multiline");

		// State is set to widget in MyComponentConnector
		
	}

}