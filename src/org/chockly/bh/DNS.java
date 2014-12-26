package org.chockly.bh;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Curtis
 */
public interface DNS {
    
    public void setShouldSort(boolean shouldSort);
    
    public void setUpComments(String sectionIndicator, String commentStart);
    
    public void addDomains(List<String> domains, String destination, String section) throws IOException, InvalidSectionException;
    
}
