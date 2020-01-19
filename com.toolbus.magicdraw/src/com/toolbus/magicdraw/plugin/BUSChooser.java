package com.toolbus.magicdraw.plugin;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.nomagic.magicdraw.core.Application;

public class BUSChooser
{
    public static File chooseFile()
    {
        JFileChooser file_chooser = new JFileChooser("./test-models");
        
        FileFilter filter = new FileNameExtensionFilter("TOOLBUS Files", "bus", "BUS");
        
        file_chooser.addChoosableFileFilter(filter);
        file_chooser.setAcceptAllFileFilterUsed(false);
        
        int result = file_chooser.showOpenDialog(Application.getInstance().getMainFrame());
        
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = file_chooser.getSelectedFile();

            System.err.println("File chosen: " + selectedFile.getName());
            	
            return selectedFile;
        }
        
        return null;
    }
}
