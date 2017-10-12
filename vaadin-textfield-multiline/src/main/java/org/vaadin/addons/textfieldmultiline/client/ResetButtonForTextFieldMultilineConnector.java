package org.vaadin.addons.textfieldmultiline.client;

import org.vaadin.addons.textfieldmultiline.ResetButtonForTextFieldMultiline;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(ResetButtonForTextFieldMultiline.class)
public class ResetButtonForTextFieldMultilineConnector extends
        AbstractExtensionConnector implements KeyUpHandler,
        AttachEvent.Handler, StateChangeEvent.StateChangeHandler {

    private static final long serialVersionUID = -737765038361894693L;

    public static final String CLASSNAME = "resetbuttonfortextfieldmultiline";

    private VTextFieldMultiline textFieldMultiline;
    private TextFieldMultilineConnector textFieldMultilineConnector;

    private Element resetButtonElement;
    private final ResetButtonClickRpc resetButtonClickRpc = RpcProxy.create(
            ResetButtonClickRpc.class, this);

    @Override
    protected void extend(ServerConnector target) {
        target.addStateChangeHandler(new StateChangeEvent.StateChangeHandler() {
            private static final long serialVersionUID = -8439729365677484553L;

            @Override
            public void onStateChanged(StateChangeEvent stateChangeEvent) {
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    @Override
                    public void execute() {
                        updateResetButtonVisibility();
                    }
                });
            }
        });

        textFieldMultilineConnector = (TextFieldMultilineConnector) target;

        textFieldMultiline = (VTextFieldMultiline) ((ComponentConnector) target).getWidget();
        textFieldMultiline.addStyleName(CLASSNAME + "-textfield");

        resetButtonElement = DOM.createDiv();
        resetButtonElement.addClassName(CLASSNAME + "-resetbutton");

        textFieldMultiline.addAttachHandler(this);
        textFieldMultiline.addDomHandler(this, KeyUpEvent.getType());
    }

    public native void addResetButtonClickListener(Element el)
    /*-{
        var self = this;
        el.onclick = $entry(function () {
            self.@org.vaadin.addons.textfieldmultiline.client.ResetButtonForTextFieldMultilineConnector::clearTextField()();
        });
    }-*/;

    public native void removeResetButtonClickListener(Element el)
    /*-{
        el.onclick = null;
    }-*/;

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            textFieldMultiline.getElement().getParentElement()
                    .insertAfter(resetButtonElement, textFieldMultiline.getElement());
            updateResetButtonVisibility();
            addResetButtonClickListener(resetButtonElement);
        } else {
            Element parentElement = resetButtonElement.getParentElement();
            if (parentElement != null) {
                parentElement.removeChild(resetButtonElement);
            }
            removeResetButtonClickListener(resetButtonElement);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        updateResetButtonVisibility();
    }

    private void updateResetButtonVisibility() {
        if (textFieldMultiline.textArea.getValue().isEmpty()
            || textFieldMultiline.textArea.isReadOnly()
            || !textFieldMultiline.textArea.isEnabled()
            || textFieldMultiline.getStyleName().contains("v-textfield-prompt")) {
            resetButtonElement.getStyle().setDisplay(Display.NONE);
        } else {
            resetButtonElement.getStyle().clearDisplay();
        }
    }

    private void clearTextField() {
        resetButtonClickRpc.resetButtonClick();
        textFieldMultiline.textArea.setValue("", true);
        textFieldMultiline.textArea.valueChange(true);
        textFieldMultiline.textArea.updateFieldContent("");

        textFieldMultilineConnector.setValuesFromTextArea();
        textFieldMultilineConnector.setTextFieldString(true);
    }
}