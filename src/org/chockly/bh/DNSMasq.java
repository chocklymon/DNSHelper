package org.chockly.bh;

import java.io.File;
import java.util.regex.Pattern;

/**
 *
 * @author Curtis
 */
public class DNSMasq extends AbstractDNS {
    
    public DNSMasq(File configFile) {
        super(configFile);
    }

    @Override
    protected Pattern getDomainPattern() {
        // address=/doubeclick.net/127.0.0.1
        return Pattern.compile("address=/([a-zA-Z0-9.\\-]+)/.*");
    }

    @Override
    protected String getDomainEntry(String domain, String destination) {
        StringBuilder entry = new StringBuilder();
        entry.append("address=/")
             .append(domain)
             .append("/")
             .append(destination);
        
        return entry.toString();
    }
}
