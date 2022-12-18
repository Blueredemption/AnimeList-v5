package org.coopereisnor;

import org.coopereisnor.animeApplication.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Program {
    public static final Logger logger = LoggerFactory.getLogger(Program.class);
    public static void main(String[] args){
        Application.main(args);
        System.exit(0); // there is a thread that doesn't close somewhere...? So when I close the program I need to tell the program to exit.
    }
}
