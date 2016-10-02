package com.mcnedward.app.ui.component;

/**
 * Created by Edward on 10/2/2016.
 */
public class StandardHtmlTextPane extends HtmlTextPane {

    private String mText;

    public StandardHtmlTextPane(String text) {
        super();
        mText = text;
    }

    @Override
    protected String getHtml() {
        return mText;
    }
}
