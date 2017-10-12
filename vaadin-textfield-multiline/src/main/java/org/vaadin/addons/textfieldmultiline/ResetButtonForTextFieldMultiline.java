package org.vaadin.addons.textfieldmultiline;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.addons.textfieldmultiline.client.ResetButtonClickRpc;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;

public class ResetButtonForTextFieldMultiline extends AbstractExtension {
    private final List<ResetButtonClickListener> listeners = new ArrayList<>();

    private final ResetButtonClickRpc resetButtonClickRpc = new ResetButtonClickRpc() {
        @Override
        public void resetButtonClick() {
            for (ResetButtonClickListener listener : listeners) {
                listener.resetButtonClicked();
            }
        }
    };

    public static ResetButtonForTextFieldMultiline extend(TextFieldMultiline field) {
        ResetButtonForTextFieldMultiline resetButton = new ResetButtonForTextFieldMultiline();
        resetButton.extend((AbstractClientConnector) field);
        return resetButton;
    }

    public ResetButtonForTextFieldMultiline() {
        registerRpc(resetButtonClickRpc);
    }

    public void addResetButtonClickedListener(ResetButtonClickListener listener) {
        listeners.add(listener);
    }

    public void removeResetButtonClickListener(ResetButtonClickListener listener) {
        listeners.remove(listener);
    }
}