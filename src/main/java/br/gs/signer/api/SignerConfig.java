package br.gs.signer.api;

import java.applet.Applet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum SignerConfig {

    PARAM_APPLET_ACTION_EXECUTE("factory.applet.action"),
    PARAM_APPLET_JAVASCRIPT_POSTACTION_FAILURE("applet.javascript.postaction.failure"),
    LOOK_AND_FEEL("look.and.feel"),
    CONFIG_DIALOG_IS_MODAL("config.dialog.modal"),
    CONFIG_DIALOG_IS_VISIBLE("config.dialog.visible"),
    CONFIG_DIALOG_FONT("config.dialog.font"),
    CONFIG_DIALOG_FONT_STYLE("config.dialog.font.style"),
    CONFIG_DIALOG_FONT_SIZE("config.dialog.font.size"),
    CONFIG_DIALOG_DIMENSION_WIDTH("config.dialog.dimension.width"),
    CONFIG_DIALOG_DIMENSION_HEIGHT("config.dialog.dimension.height"),
    CONFIG_DIALOG_PIN_LABEL_X("config.dialog.pin-label.x"),
    CONFIG_DIALOG_PIN_LABEL_Y("config.dialog.pin-label.y"),
    CONFIG_DIALOG_PIN_LABEL_WIDTH("config.dialog.pin-label.width"),
    CONFIG_DIALOG_PIN_LABEL_HEIGHT("config.dialog.pin-label.height"),
    CONFIG_DIALOG_PIN_CODE_X("config.dialog.pin-code.x"),
    CONFIG_DIALOG_PIN_CODE_Y("config.dialog.pin-code.y"),
    CONFIG_DIALOG_PIN_CODE_WIDTH("config.dialog.pin-code.width"),
    CONFIG_DIALOG_PIN_CODE_HEIGHT("config.dialog.pin-code.height"),
    LABEL_DIALOG_TITLE("label.dialog.title"),
    LABEL_DIALOG_LABEL_PIN("label.dialog.label.pin"),
    LABEL_DIALOG_BUTTON_RUN("label.dialog.button.run"),
    LABEL_DIALOG_BUTTON_CANCEL("label.dialog.button.cancel"),
    LABEL_DIALOG_OPTION_PANE_TITLE("label.dialog.option_pane.title"),
    MESSAGE_ERROR_LOAD_TOKEN("message.error.load.driver"),
    MESSAGE_ERROR_UNEXPECTED("message.error.unexpected"),
    MESSAGE_ERROR_INVALID_PIN("message.error.invalid.pin"),
    MESSAGE_ERROR_PKCS11_NOT_FOUND("message.error.pkcs11.not.found"),
    MESSAGE_ERROR_DRIVER_NOT_AVAILABLE("message.error.driver.not.available"),
    MESSAGE_ERROR_DRIVER_INCOMPATIBLE("message.error.driver.incompatible"),
    CONFIG_DIALOG_LABEL_TABLE("label.dialog.label.table"),
    CONFIG_DIALOG_LABEL_POLICY("label.dialog.label.policy"),
    CONFIG_DIALOG_TABLE_CERTIFICATES_X("config.dialog.table.certificates.x"),
    CONFIG_DIALOG_TABLE_CERTIFICATES_Y("config.dialog.table.certificates.y"),
    CONFIG_DIALOG_TABLE_CERTIFICATES_WIDTH("config.dialog.table.certificates.width"),
    CONFIG_DIALOG_TABLE_CERTIFICATES_HEIGHT("config.dialog.table.certificates.height"),
    CONFIG_DIALOG_TABLE_CERTIFICATES_ROW_HEIGHT("config.dialog.table.certificates.row.heigth"),
    CONFIG_DIALOG_BUTTON_RUN_X("config.dialog.button-run.x"),
    CONFIG_DIALOG_BUTTON_RUN_Y("config.dialog.button-run.y"),
    CONFIG_DIALOG_BUTTON_RUN_WIDTH("config.dialog.button-run.width"),
    CONFIG_DIALOG_BUTTON_RUN_HEIGHT("config.dialog.button-run.height"),
    CONFIG_DIALOG_BUTTON_CANCEL_X("config.dialog.button-cancel.x"),
    CONFIG_DIALOG_BUTTON_CANCEL_Y("config.dialog.button-cancel.y"),
    CONFIG_DIALOG_BUTTON_CANCEL_WIDTH("config.dialog.button-cancel.width"),
    CONFIG_DIALOG_BUTTON_CANCEL_HEIGHT("config.dialog.button-cancel.height"),
    CONFIG_DIALOG_LABEL_POLICY_X("config.dialog.label.policy.x"),
    CONFIG_DIALOG_LABEL_POLICY_Y("config.dialog.label.policy.y"),
    CONFIG_DIALOG_LABEL_POLICY_WIDTH("config.dialog.label.policy.width"),
    CONFIG_DIALOG_LABEL_POLICY_HEIGHT("config.dialog.label.policy.height"),
    CONFIG_DIALOG_COMBO_POLICY_X("config.dialog.combo.policy.x"),
    CONFIG_DIALOG_COMBO_POLICY_Y("config.dialog.combo.policy.y"),
    CONFIG_DIALOG_COMBO_POLICY_WIDTH("config.dialog.combo.policy.width"),
    CONFIG_DIALOG_COMBO_POLICY_HEIGHT("config.dialog.combo.policy.height");

    private String key;
    private static ResourceBundle rb;
    private static Applet applet;

    /**
     * Construtor privado recebendo a chave do enum
     *
     * @param key
     */
    private SignerConfig(String key) {
        this.key = key;
    }

    /**
     * Retorna o valor de enum para uma determinada chave Primeiramente é
     * verificado se a chave foi informada como parametro do applet, se não, é
     * obtida a chave no resource bundle que pode ser da aplicacao ou caso nao
     * seja informado sera utilizado o resouce default do componente.
     *
     * @return
     */
    public String getValue() {
        try {
            String value = null;
            if (applet != null) {
                value = applet.getParameter(key);
            }
            if (value == null) {
                value = getResourceBundle().getString(key);
            }
            return value;
        } catch (MissingResourceException mre) {
            throw new RuntimeException("key '" + key + "' not found");
        }
    }

    /**
     * Retorna o valor de enum convertido para o tipo 'int' conforme sua
     * respectiva chave
     *
     * @return
     */
    public int getValueInt() {
        return Integer.valueOf(getValue());
    }

    /**
     * Retorna o valor de enum convertido para o tipo 'boolean' conforme sua
     * respectiva chave
     *
     * @return
     */
    public boolean getValueBoolean() {
        return Boolean.valueOf(getValue());
    }

    /**
     * Retorna o resouceBundle utilizado para obtencao
     *
     * @return
     */
    private ResourceBundle getResourceBundle() {
        if (rb != null) {
            return rb;
        }
        try {
            rb = getBundle("security-applet");
        } catch (MissingResourceException mre) {
            try {
                rb = getBundle("security-applet-default");
            } catch (MissingResourceException e) {
                throw new RuntimeException("key '" + key + "' not found for resource ''");
            }
        }
        return rb;
    }

    public ResourceBundle getBundle(String bundleName) {
        return ResourceBundle.getBundle(bundleName);
    }

    /**
     * Retorna a chave do enum
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    public static void setApplet(Applet _applet) {
        applet = _applet;
    }

}
