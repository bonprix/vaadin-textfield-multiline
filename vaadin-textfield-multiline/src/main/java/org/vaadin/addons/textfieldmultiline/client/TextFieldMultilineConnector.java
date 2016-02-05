package org.vaadin.addons.textfieldmultiline.client;

import java.util.Arrays;

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
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;
import com.vaadin.ui.TextField;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@Connect(TextFieldMultiline.class)
public class TextFieldMultilineConnector extends AbstractFieldConnector {

	// ServerRpc is used to send events to server. Communication implementation
	// is automatically created here
	TextFieldMultilineServerRpc rpc = RpcProxy.create(TextFieldMultilineServerRpc.class, this);

	public TextFieldMultilineConnector() {
		
		// To receive RPC events from server, we register ClientRpc implementation 
		registerRpc(TextFieldMultilineClientRpc.class, new TextFieldMultilineClientRpc() {

			@Override
			public void setInputPrompt(String inputPrompt) {
				getWidget().textField.setInputPrompt(inputPrompt);
				getWidget().textField.updateFieldContent(getWidget().textField.getValue());
			}

		});

		getWidget().textArea.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(com.google.gwt.event.dom.client.BlurEvent event) {
				getWidget().textArea.setVisible(false);
				getWidget().textField.setVisible(true);
				
				String[] entered = new String[0];
				
				String enteredString = getWidget().textArea.getValue();
				if (!enteredString.isEmpty()) {
					entered = enteredString.split("\\n+");
				}
				
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < entered.length; i++) {
					sb.append(entered[i] + "\n");
				}
				getWidget().textArea.setValue(sb.toString());
				
				sb = new StringBuffer();
				if (entered.length > 0) {
					sb.append("(" + entered.length + ") ");
					for (int i = 0; i < entered.length; i++) {
						sb.append(entered[i]);
						if (i+1 < entered.length) {
							sb.append(", ");
						}
					}
				} 
				getWidget().textField.updateFieldContent(sb.toString());
				debug("textField: " + sb.toString());
				
				debug("entered: " + entered);
				rpc.sendEnteredValues(entered);
			}
		});
//		// We choose listed for mouse clicks for the widget
//		getWidget().addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				final MouseEventDetails mouseDetails = MouseEventDetailsBuilder
//						.buildMouseEventDetails(event.getNativeEvent(),
//								getWidget().getElement());
//				
//				// When the widget is clicked, the event is sent to server with ServerRpc
//				rpc.clicked(mouseDetails);
//			}
//		});

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

}
