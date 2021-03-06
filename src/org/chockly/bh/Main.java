package org.chockly.bh;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Curtis
 */
public class Main {
    
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Setup for the command line arguments
        Arguments arg = new Arguments();
        JCommander jcmnd = new JCommander(arg);
        
        try {
            // Parse the command line arguments
            jcmnd.parse(args);
            if (arg.helpRequested()) {
                // Print help and exit
                jcmnd.usage();
                return;
            }
            
            // Add the zones
            File inputFile = new File(arg.getFile());
            DNS dns;
            if (arg.isDnsmasqMode()) {
                dns = new DNSMasq(inputFile);
            } else {
                dns = new Named(inputFile, arg.getType(), arg.shouldNotify());
            }
            dns.setShouldSort(arg.shouldSort());
            dns.setUpComments(arg.getSectionIndicator(), arg.getCommentStart());
            dns.addDomains(arg.getDomains(), arg.getDestination(), arg.getSection());
        } catch (FileNotFoundException ex) {
            logger.log(Level.FINE, "Bind definition file not found", ex);
            System.err.println("Provided definition file doesn't exist!");
            jcmnd.usage();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Problem changing definition file", ex);
        } catch (ParameterException ex) {
            logger.log(Level.FINE, "Problem with command line arguments", ex);
            jcmnd.usage();
        } catch (InvalidSectionException ex) {
            logger.log(Level.FINE, "Invalid section provided", ex);
            System.err.println("Provided Section not found");
            jcmnd.usage("section");
        }
    }
}
