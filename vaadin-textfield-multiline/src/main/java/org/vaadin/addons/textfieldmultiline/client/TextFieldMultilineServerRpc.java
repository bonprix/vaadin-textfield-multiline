package org.vaadin.addons.textfieldmultiline.client;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

// ServerRpc is used to pass events from client to server
public interface TextFieldMultilineServerRpc extends ServerRpc {

	public void sendEnteredValues(String[] entered);

}
