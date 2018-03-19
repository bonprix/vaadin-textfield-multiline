package org.vaadin.addons.textfieldmultiline.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addons.textfieldmultiline.TextFieldMultiline;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

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
            public void onBlur(final com.google.gwt.event.dom.client.BlurEvent event) {
                getWidget().textArea.setVisible(false);
                getWidget().textField.setVisible(true);

                setValuesFromTextArea();

                setTextFieldString(true);
            }
        });

        getWidget().resetButtonImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {

                getWidget().textArea.setValue("", true);

                setValuesFromTextArea();
                setTextFieldString(true);
            }
        });

    }

    private void setValuesFromTextArea() {
        String[] values = new String[0];

        final String enteredString = getWidget().textArea.getValue();
        if (!enteredString.isEmpty()) {
            final List<String> entered = new ArrayList<>(Arrays.asList(enteredString.split("\\n+|\\t+")));
            final Iterator<String> it = entered.iterator();
            while (it.hasNext()) {
                final String next = it.next();
                if (next.isEmpty()) {
                    it.remove();
                }
            }

            values = entered.toArray(new String[0]);
        }

        getWidget().values = values;
    }

    private void setTextFieldString(final boolean sendToServer) {
        final String[] values = getWidget().values;

        final StringBuilder sbTextArea = new StringBuilder();
        for (final String value : values) {
            sbTextArea.append(value + "\n");
        }
        getWidget().textArea.setValue(sbTextArea.toString());

        final StringBuilder sbTextField = new StringBuilder();
        if (values.length > 0) {
            sbTextField.append("(" + values.length + ") ");
            for (int i = 0; i < values.length; i++) {
                sbTextField.append(values[i]);
                if (i + 1 < values.length) {
                    sbTextField.append(", ");
                }
            }
        }
        getWidget().textField.setValue(sbTextField.toString());
        debug("textField: " + sbTextField.toString());

        if (sendToServer) {
            debug("entered: " + values);
            this.rpc.sendEnteredValues(values);
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
    public void onStateChanged(final StateChangeEvent stateChangeEvent) {
        // keep this here, because of later changes and easy accesibility
        super.onStateChanged(stateChangeEvent);
    }

    private void debug(final String string) {
        if (getWidget().enableDebug) {
            VConsole.error(string);
        }
    }

    @Override
    public void updateFromUIDL(final UIDL uidl, final ApplicationConnection client) {
        debug("updateFromUIDL");

        // Inverse logic here to make the default case (text input enabled)
        // work without additional UIDL messages
        final boolean isEnabled = uidl.hasAttribute(TextFieldMultilineConstants.ATTR_ENABLED)
                && uidl.getBooleanAttribute(TextFieldMultilineConstants.ATTR_ENABLED);
        getWidget().textField.setEnabled(isEnabled);
        getWidget().textField.setStyleName("v-disabled", !isEnabled);

        final boolean isReadonly = uidl.hasAttribute(TextFieldMultilineConstants.ATTR_READ_ONLY)
                && uidl.getBooleanAttribute(TextFieldMultilineConstants.ATTR_READ_ONLY);
        getWidget().textField.setReadOnly(isReadonly);

        if (uidl.hasAttribute(TextFieldMultilineConstants.ATTR_INPUTPROMPT)) {
            // input prompt changed from server
            getWidget().textField.setPlaceholder(uidl.getStringAttribute(TextFieldMultilineConstants.ATTR_INPUTPROMPT));
        }
        else {
            getWidget().textField.setPlaceholder("");
        }

        getWidget().textField.setValue(getWidget().textField.getValue());

        // Set the value from server on repaint
        final UIDL options = uidl.getChildUIDL(0);

        final List<String> values = new ArrayList<>();

        for (final Iterator<?> i = options.getChildIterator(); i.hasNext();) {
            final UIDL optionUidl = (UIDL) i.next();
            values.add(optionUidl.getStringAttribute("value"));
        }
        getWidget().values = values.toArray(new String[values.size()]);

        if (!values.isEmpty() && uidl.hasAttribute(TextFieldMultilineConstants.ATTR_RESET_BUTTON_ENABLED)) {
            getWidget().resetButtonImage.setVisible(true);
        }
        else {
            getWidget().resetButtonImage.setVisible(false);
        }

        setTextFieldString(false);
    }

}
