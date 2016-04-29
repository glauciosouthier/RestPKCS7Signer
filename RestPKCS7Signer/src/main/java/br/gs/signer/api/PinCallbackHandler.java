package br.gs.signer.api;

import java.awt.Font;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class PinCallbackHandler implements CallbackHandler {

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof PasswordCallback) {
                final PasswordCallback callback = (PasswordCallback) callbacks[i];
                JPasswordField passwordField = new JPasswordField();
                passwordField.setFont(new Font(SignerConfig.CONFIG_DIALOG_FONT.getValue(), SignerConfig.CONFIG_DIALOG_FONT_STYLE.getValueInt(), SignerConfig.CONFIG_DIALOG_FONT_SIZE.getValueInt()));
                passwordField.setEchoChar('*');
                Object[] obj = {"Por favor, digite o pin do Certificado Digital: \n", passwordField};
                Object stringArray[] = {"Confirmar", "Cancelar"};

                if (JOptionPane.showOptionDialog(null, obj, "Certificado Digital", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, obj) == JOptionPane.YES_OPTION) {
                    callback.setPassword(passwordField.getPassword());
                }
            }
        }
    }
}
