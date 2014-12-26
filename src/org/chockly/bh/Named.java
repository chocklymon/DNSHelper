package org.chockly.bh;

import java.io.File;
import java.util.regex.Pattern;

/**
 *
 * @author Curtis
 */
public final class Named extends AbstractDNS {
    
    private final String type;
    private final boolean shouldNotify;

    public Named(File inputFile, String type, boolean shouldNotify) {
        super(inputFile);
        this.type = type;
        this.shouldNotify = shouldNotify;
    }

    @Override
    protected Pattern getDomainPattern() {
        return Pattern.compile("zone[ \t]+\"([a-zA-Z0-9.\\-]+)\"");
    }

    @Override
    protected String getDomainEntry(String domain, String destination) {
        StringBuilder entry = new StringBuilder();
        entry
            .append("zone \"").append(domain)
            .append("\" { type ").append(type).append("; ");
        if (!shouldNotify) {
            entry.append("notify no; ");
        }
        entry.append("file \"").append(destination).append("\"; };");
        
        return entry.toString();
    }
}
