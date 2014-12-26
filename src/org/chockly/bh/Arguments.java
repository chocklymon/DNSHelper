package org.chockly.bh;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.util.ArrayList;
import java.util.List;

/**
 * Command line arguments for Bind Helper
 * @author Curtis
 */
@Parameters(commandDescription = "Add and remove definitions from a bind9/dnsmasq configuration file")
public class Arguments {
    
    @Parameter(description="The domains to add or remove")
    private List<String> domains = new ArrayList<>();
    
    @Parameter(names={"-d", "--destination"}, description="The zone's definition file or IP address for dnsmasq")
    private String destination;
    
    @Parameter(names={"-e", "--section-header"}, description="The string that indicates the start of a section")
    private String sectionIndicator = "// $";
    
    @Parameter(names={"-f", "--file"}, description="The configuration file to modify")
    private String file = "/etc/bind/named.conf.local";
    
    @Parameter(names={"-h", "--help"}, description="Print out this help message", help=true)
    private boolean help = false;
    
    @Parameter(names={"-n", "--notify"}, description="If the provided domains should notify of changes. Bind9 only.")
    private boolean notify = false;
    
    @Parameter(names={"-m", "--dnsmasq"}, description="Set to dnsmasq mode")
    private boolean dnsmasqMode = false;
    
    @Parameter(names={"-o", "--sort"}, description="Sort the section after adding the domains")
    private boolean sort = false;
    
    @Parameter(names={"-s", "--section"}, description="The section to place the new domains into. If not specified domains will be placed at the start of the file")
    private String section = "";
    
    @Parameter(names={"-t", "--type"}, description="The type of zone. Defaults to 'master'. Bind9 only.")
    private String type = "master";
    
    @Parameter(names={"--comment"}, description="Set the characters that define the start of a comment. Defaults to '//'", hidden=true)
    private String commentStart = "//";
    
    public Arguments() {
        
    }

    /**
     * @return the zones
     */
    public List<String> getDomains() {
        return domains;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @return the help
     */
    public boolean helpRequested() {
        return help;
    }

    /**
     * @return the sectionIndicator
     */
    public String getSectionIndicator() {
        return sectionIndicator;
    }

    /**
     * @return the notify
     */
    public boolean shouldNotify() {
        return notify;
    }

    /**
     * @return the zoneFile
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @return the section
     */
    public String getSection() {
        return section;
    }

    /**
     * @return the sort
     */
    public boolean shouldSort() {
        return sort;
    }

    /**
     * @return the commentStart
     */
    public String getCommentStart() {
        return commentStart;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the dnsmasqMode
     */
    public boolean isDnsmasqMode() {
        return dnsmasqMode;
    }
}
