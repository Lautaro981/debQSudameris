package controllers;

/**
 * @author evalais
 */
public class TemplateBuilder {

    private String template;

    private String keyStart;

    private String keyEnd;

    public TemplateBuilder(String template) {
        keyStart = "\\[\\*\\.";
        keyEnd = "\\.\\*\\]";
        this.template = template;
    }

    public void replaceKey(String key, String value) {
        template = template.replaceAll(keyStart + key + keyEnd, value);
    }

    public String getTemplate() {
        return template;
    }
}
