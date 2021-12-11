/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.ricr.cw1.Utils;

import java.io.*;
import javax.json.*;

/**
 *
 * @author ricardorocha
 */
public class Logger {
    
    public void SaveLog(JsonObject json) {
        try {
            File logFile = new File("log.txt");
            
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            
            this.Writer(json);
            
        } catch (IOException e) {
            // ignore
        }
    }
    
    private void Writer(JsonObject json) {
        try {
            FileWriter file = new FileWriter("log.txt");
            file.append((CharSequence) json);
            file.close();
        } catch (IOException e) {
            // ignore
        }
    }
    
}
