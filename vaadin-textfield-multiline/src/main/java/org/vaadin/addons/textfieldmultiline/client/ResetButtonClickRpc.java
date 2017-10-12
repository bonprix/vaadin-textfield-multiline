package org.vaadin.addons.textfieldmultiline.client;

import com.vaadin.shared.communication.ServerRpc;

public interface ResetButtonClickRpc extends ServerRpc {
    void resetButtonClick();
}