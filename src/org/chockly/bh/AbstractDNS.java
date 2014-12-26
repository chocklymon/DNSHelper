package org.chockly.bh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Curtis
 */
public abstract class AbstractDNS implements DNS {
    
    private final File configFile;
    private final Map<String, List<String>> sections;
    private final Set<String> allZones;
    private String sectionIndicator;
    private String commentStart;
    private boolean shouldSort;
    
    protected AbstractDNS(File configFile) {
        this.configFile = configFile;
        this.sections = new HashMap<>();
        this.allZones = new HashSet<>();
    }
    
    protected abstract Pattern getDomainPattern();
    
    protected abstract String getDomainEntry(String domain, String destination);
    
    @Override
    public void addDomains(List<String> domains, String destination, String section) throws IOException, InvalidSectionException {
        parseFile();

        List<String> contents = getSection(section);
        int sectionStartIndex = getStartIndex(contents);
        for (String zone : domains) {
            if (!allZones.contains(zone)) {
                contents.add(sectionStartIndex, getDomainEntry(zone, destination));
            }
        }

        if (shouldSort()) {
            sort(contents);
        }
        
        storeFile();
    }
    
    @Override
    public void setShouldSort(boolean shouldSort) {
        this.shouldSort = shouldSort;
    }
    
    public boolean shouldSort() {
        return shouldSort;
    }
    
    @Override
    public void setUpComments(String sectionIndicator, String commentStart) {
        this.sectionIndicator = sectionIndicator;
        this.commentStart = commentStart;
    }
    
    private void parseFile() throws FileNotFoundException, IOException {
        Pattern domainPattern = getDomainPattern();
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            
            Matcher m;
            String currentSectionName = "";
            List<String> currentSection = new ArrayList<>();
            sections.put(currentSectionName, currentSection);
            while ((line = reader.readLine()) != null) {
                m = domainPattern.matcher(line);
                if (m.find()) {
                    allZones.add(m.group(1));
                }
                if (line.startsWith(sectionIndicator)) {
                    currentSectionName = line.substring(sectionIndicator.length()).trim();
                    currentSection = new ArrayList<>();
                    sections.put(currentSectionName, currentSection);
                }
                currentSection.add(line);
            }
        }
    }

    private void storeFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            Iterator<List<String>> sectionIterator = sections.values().iterator();
            while (sectionIterator.hasNext()) {
                Iterator<String> lineIterator = sectionIterator.next().iterator();
                while (lineIterator.hasNext()) {
                    writer.write(lineIterator.next());
                    writer.newLine();
                }
            }
        }
    }

    private void sort(List<String> contents) {
        // We don't want to sort comments or new lines at the begining or end
        int startIndex = getStartIndex(contents);
        int endIndex = contents.size();
        for (int i=endIndex-1; i>startIndex; i--) {
            if (contents.get(i).startsWith(commentStart) || contents.get(i).trim().isEmpty()) {
                endIndex--;
            } else {
                break;
            }
        }
        
        // Modified from Collections.sort
        Object[] a = contents.subList(startIndex, endIndex).toArray();
        Arrays.sort(a);
        ListIterator<String> i = contents.listIterator(startIndex);
        for (int j=0; j<a.length; j++) {
            i.next();
            i.set((String) a[j]);
        }
    }

    private int getStartIndex(List<String> contents) {
        int startIndex = 0;
        for (String line : contents) {
            if (line.startsWith(commentStart) || line.trim().isEmpty()) {
                startIndex++;
            } else {
                break;
            }
        }
        return startIndex;
    }

    private List<String> getSection(String section) throws InvalidSectionException {
        List<String> contents = sections.get(section);
        if (contents == null) {
            throw new InvalidSectionException("Section '"+ section +"' not found");
        } else {
            return contents;
        }
    }
}
