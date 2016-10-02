package com.mcnedward.app.ui.component;

/**
 * Created by Edward on 10/2/2016.
 */
public class StandardHtmlTextPane extends HtmlTextPane {

    private String mText;

    public StandardHtmlTextPane() {
        super();
    }

    public StandardHtmlTextPane(String text) {
        super();
        mText = text;
    }

    public void setHtml(String text) {
        mText = text;
        updateText();
    }

    @Override
    protected String getHtml() {
        return mText;
    }
}
