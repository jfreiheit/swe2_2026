package htw.freiheit.studi.validation;

import jakarta.validation.groups.Default;

/**
 * Validierungsgruppe fuer Regeln, die nur beim Anlegen (POST) gelten.
 * Erweitert {@link Default}, damit beim Anlegen auch alle allgemeinen Regeln geprueft werden.
 */
public interface OnCreate extends Default {
}
