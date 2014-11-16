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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Curtis
 */
public final class Named {
    
    private String commentStart;
    private String sectionIndicator;
    private Map<String, List<String>> sections;
    private File inputFile;
    private boolean shouldSort;
    private final HashSet<String> allZones;

    public Named(File inputFile, boolean shouldSort, String sectionIndicator, String commentStart) throws FileNotFoundException, IOException {
        this.inputFile = inputFile;
        this.shouldSort = shouldSort;
        this.sectionIndicator = sectionIndicator;
        this.commentStart = commentStart;
        sections = new LinkedHashMap<>();
        allZones = new HashSet<>();
        
        parseFile();
    }
    
    private void parseFile() throws FileNotFoundException, IOException {
        Pattern regex = Pattern.compile("zone[ \t]+\"([a-zA-Z0-9.\\-]+)\"");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            
            Matcher m;
            String currentSectionName = "";
            List<String> currentSection = new ArrayList<>();
            sections.put(currentSectionName, currentSection);
            while ((line = reader.readLine()) != null) {
                m = regex.matcher(line);
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

    public void storeFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
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
    
    public boolean addZones(List<String> zones, String section, String zoneFile, String type, boolean shouldNotify)
            throws InvalidSectionException
    {
        List<String> contents = sections.get(section);
        if (contents != null) {
            for (String zone : zones) {
                if (!allZones.contains(zone)) {
                    addZone(contents, zone, zoneFile, type, shouldNotify);
                }
            }

            if (shouldSort) {
                sort(contents);
            }
            return true;
        } else {
            throw new InvalidSectionException("Section '"+ section +"' not found");
        }
    }
    
    private void addZone(List<String> contents, String zone, String zoneFile, String type, boolean shouldNotify) {
        StringBuilder entry = new StringBuilder();
        entry
            .append("zone \"").append(zone)
            .append("\" { type ").append(type).append("; ");
        if (!shouldNotify) {
            entry.append("notify no; ");
        }
        entry.append("file \"").append(zoneFile).append("\"; };");

        contents.add(getStartIndex(contents), entry.toString());
    }

    private void sort(List<String> contents) {
        // We don't want to sort comments or new lines are the begining or end
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
}
