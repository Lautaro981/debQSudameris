package exceptions;

import utils.translations.TranslationKey;

public class ConectorException extends Exception{

    private TranslationKey translationKey;

    public ConectorException(TranslationKey translationKey) {
        this.translationKey = translationKey;
    }

    public TranslationKey getTranslationKey() {
        return translationKey;
    }

}
