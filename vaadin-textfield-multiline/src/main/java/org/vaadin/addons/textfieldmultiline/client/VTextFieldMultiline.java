package org.vaadin.addons.textfieldmultiline.client;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Composite;
import com.vaadin.client.ui.Field;
import com.vaadin.client.ui.VTextArea;
import com.vaadin.client.ui.VTextField;
import com.vaadin.client.ui.VVerticalLayout;

// Extend any GWT Widget
public class VTextFieldMultiline extends Composite implements Field {

	VTextField textField = new VTextField();
	VTextArea textArea = new VTextArea();
	VVerticalLayout layout  = new VVerticalLayout();;

	// Entered values
	public String[] values;
	
	/** For internal use only. May be removed or replaced in the future. */
	public boolean enableDebug;
	
	public VTextFieldMultiline() {
		
		textArea.setVisible(false);
		textArea.setRows(5);
		
		textField.addStyleName("v-widget");
		textField.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				if (!textField.isReadOnly()) {
					layout.setHeight((textField.getElement().getClientHeight() + 2) + "px");
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
		initWidget(layout);
		
		// CSS class-name should not be v- prefixed
		setStyleName("vaadin-textfield-multiline");

		// State is set to widget in MyComponentConnector
		
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		layout.setWidth(width);
		textField.setWidth(width);
		textArea.setWidth(width);
	}

	@Override
	public void setHeight(String height) {
		super.setHeight(height);
		layout.setHeight(height);
		textField.setHeight(height);
	}
}