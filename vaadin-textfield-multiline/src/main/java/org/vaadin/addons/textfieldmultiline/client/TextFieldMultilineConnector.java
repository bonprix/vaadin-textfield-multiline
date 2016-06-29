package org.vaadin.addons.textfieldmultiline.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addons.textfieldmultiline.TextFieldMultiline;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.client.ui.SimpleManagedLayout;
import com.vaadin.client.ui.VFilterSelect.FilterSelectSuggestion;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.combobox.ComboBoxConstants;
import com.vaadin.ui.TextField;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@Connect(TextFieldMultiline.class)
public class TextFieldMultilineConnector extends AbstractFieldConnector implements Paintable {

	// ServerRpc is used to send events to server. Communication implementation
	// is automatically created here
	TextFieldMultilineServerRpc rpc = RpcProxy.create(TextFieldMultilineServerRpc.class, this);

	public TextFieldMultilineConnector() {

		getWidget().textArea.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(com.google.gwt.event.dom.client.BlurEvent event) {
				getWidget().textArea.setVisible(false);
				getWidget().textField.setVisible(true);
				
				setValuesFromTextArea();
				
				setTextFieldString(true);
			}
		});

	}
	
	private void setValuesFromTextArea() {
		String[] values = new String[0];
		
		String enteredString = getWidget().textArea.getValue();
		if (!enteredString.isEmpty()) {
			List<String> entered = Arrays.asList(enteredString.split("\\n+|\\t+"));
			Iterator<String> it = entered.iterator();
			while(it.hasNext()) {
				String next = it.next();
				if (next.isEmpty()) {
					it.remove();
				}
			}
			
			values = entered.toArray(new String[0]);
		}
		
		getWidget().values = values;
	}
	
	private void setTextFieldString(boolean sendToServer) {
		String[] values = getWidget().values;
				
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i] + "\n");
		}
		getWidget().textArea.setValue(sb.toString());
		
		sb = new StringBuffer();
		if (values.length > 0) {
			sb.append("(" + values.length + ") ");
			for (int i = 0; i < values.length; i++) {
				sb.append(values[i]);
				if (i+1 < values.length) {
					sb.append(", ");
				}
			}
		} 
		getWidget().textField.updateFieldContent(sb.toString());
		debug("textField: " + sb.toString());
		
		if (sendToServer) {
			debug("entered: " + values);
			rpc.sendEnteredValues(values);
		}
	}

	// We must implement getWidget() to cast to correct type 
	// (this will automatically create the correct widget type)
	@Override
	public VTextFieldMultiline getWidget() {
		return (VTextFieldMultiline) super.getWidget();
	}

	// We must implement getState() to cast to correct type
	@Override
	public TextFieldMultilineState getState() {
		return (TextFieldMultilineState) super.getState();
	}

	// Whenever the state changes in the server-side, this method is called
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
	}
	
	private void debug(String string) {
		if (getWidget().enableDebug) {
			VConsole.error(string);
		}
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		debug("updateFromUIDL");
		
		// Inverse logic here to make the default case (text input enabled)
        // work without additional UIDL messages
		boolean isEnabled = uidl
                .hasAttribute(TextFieldMultilineConstants.ATTR_ENABLED)
                && uidl.getBooleanAttribute(TextFieldMultilineConstants.ATTR_ENABLED);
        getWidget().textField.setEnabled(isEnabled);
        
        boolean isReadonly = uidl
                .hasAttribute(TextFieldMultilineConstants.ATTR_READ_ONLY)
                && uidl.getBooleanAttribute(TextFieldMultilineConstants.ATTR_READ_ONLY);
        getWidget().textField.setReadOnly(isReadonly);

        if (uidl.hasAttribute(TextFieldMultilineConstants.ATTR_INPUTPROMPT)) {
            // input prompt changed from server
        	getWidget().textField.setInputPrompt(uidl
                    .getStringAttribute(TextFieldMultilineConstants.ATTR_INPUTPROMPT));
        } else {
        	getWidget().textField.setInputPrompt("");
        }
        
		getWidget().textField.updateFieldContent(getWidget().textField.getValue());
		
		// Set the value from server on repaint
		final UIDL options = uidl.getChildUIDL(0);

        List<String> values = new ArrayList<String>();

        for (final Iterator<?> i = options.getChildIterator(); i.hasNext();) {
            final UIDL optionUidl = (UIDL) i.next();
            values.add(optionUidl.getStringAttribute("value"));
        }
        getWidget().values = values.toArray(new String[values.size()]);
        
        setTextFieldString(false);
	}

}
