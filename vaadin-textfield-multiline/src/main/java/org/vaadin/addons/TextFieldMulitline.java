package org.vaadin.addons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.addons.client.TextFieldMultilineClientRpc;
import org.vaadin.addons.client.TextFieldMultilineServerRpc;
import org.vaadin.addons.client.TextFieldMultilineState;

import com.vaadin.shared.MouseEventDetails;

// This is the server-side UI component that provides public API 
// for MyComponent
public class TextFieldMulitline extends com.vaadin.ui.AbstractField<List<String>> {

	// To process events from the client, we implement ServerRpc
	private TextFieldMultilineServerRpc rpc = new TextFieldMultilineServerRpc() {

		@Override
		public void sendEnteredValues(String[] entered) {
			setValue(Arrays.asList(entered));
			System.out.println(getValue());
		}
	};

	public TextFieldMulitline() {

		// To receive events from the client, we register ServerRpc
		registerRpc(rpc);
	}

	// We must override getState() to cast the state to MyComponentState
	@Override
	protected TextFieldMultilineState getState() {
		return (TextFieldMultilineState) super.getState();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends List<String>> getType() {
		return (Class<? extends List<String>>) new ArrayList<String>().getClass();
	}

	public void setInputPrompt(String inputPrompt) {
		getRpcProxy(TextFieldMultilineClientRpc.class).setInputPrompt(inputPrompt);
	}
}
