package com.tester.utils.executor;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Song Rui
 */
public class Executor {

    public final static String OS_WINDOWS = "windows";
    public final static String OS_MAC = "mac";
    public final static String OS_UNIX = "unix";
    protected static String OS;
    static ProcessBuilder pb = new ProcessBuilder();
    private static String PATH_AFFIX = "PATH";

    static {
        OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains(OS_WINDOWS)) {
            PATH_AFFIX = "Path";
        }
    }

    public void setDirectory(File file) {
        pb.directory(file);
    }

    public String exec(String cmd) {
        StringBuilder builder = new StringBuilder();
        Process process = null;
        InputStreamReader inputStr = null;
        BufferedReader bufferReader = null;
        try {
            if (OS.contains(OS_WINDOWS)) {
                pb.command("cmd.exe", "/c", cmd);
            } else {
                pb.command("bash", "-c", cmd);
            }
            process = pb.start();
            inputStr = new InputStreamReader(process.getInputStream());
            bufferReader = new BufferedReader(inputStr);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                builder.append("\n").append(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try{
                if (process != null) {
                    process.destroy();
                }
                if (inputStr != null) {
                    IOUtils.close(inputStr);
                }
                if (bufferReader != null) {
                    IOUtils.close(bufferReader);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

}
