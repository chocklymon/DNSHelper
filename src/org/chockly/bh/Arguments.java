package org.chockly.bh;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Command line arguments for Bind Helper
 * @author Curtis
 */
@Parameters(commandDescription = "Add and remove definitions from a bind configuration file")
public class Arguments {
    
    @Parameter(description="The zones to add or remove")
    private List<String> zones = new ArrayList<>();
    
    @Parameter(names={"--file", "-f"}, description="The bind definitions file to modify")
    private String file = "/etc/bind/named.conf.local";
    
    @Parameter(names={"--help", "-h"}, description="Print out this help message", help=true)
    private boolean help = false;
    
    @Parameter(names={"--section-header", "-e"}, description="The string that indicates the start of a section")
    private String sectionIndicator = "// $";
    
    @Parameter(names={"--notify", "-n"}, description="If the provided zone should notify of changes")
    private boolean notify = false;
    
    @Parameter(names={"--zone-file", "-z"}, description="The zone's definition file", required=true)
    private String zoneFile;
    
    @Parameter(names={"--section", "-s"}, description="The section to place the new zones into. If not specified zones will be placed at the start of the file")
    private String section = "";
    
    @Parameter(names={"--sort", "-o"}, description="Sort the section after adding the zones")
    private boolean sort = false;
    
    @Parameter(names={"--type", "-t"}, description="The type of zone. Defaults to 'master'")
    private String type = "master";
    
    @Parameter(names={"--comment"}, description="Set the characters that define the start of a comment. Defaults to '//'", hidden=true)
    private String commentStart = "//";
    
    public Arguments() {
        
    }

    /**
     * @return the zones
     */
    public List<String> getZones() {
        return zones;
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
    public boolean isHelp() {
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
    public boolean isNotify() {
        return notify;
    }

    /**
     * @return the zoneFile
     */
    public String getZoneFile() {
        return zoneFile;
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
    public boolean isSort() {
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
}
