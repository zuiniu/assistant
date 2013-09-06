/**
 * 
 */
package com.zuiniu.android.assistant.view;

/**
 * @author Administrator
 *
 */
public class SectionListItem {
    public Object item;
    public String section;

    public SectionListItem(final Object item, final String section) {
        super();
        this.item = item;
        this.section = section;
    }

    @Override
    public String toString() {
        return item.toString();
    }

}