package me.Ult1;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {

    private static HashMap<String, JButton> buttons = new HashMap<>(80);

    public static HashMap<String, JButton> getButtons() {
        return buttons;
    }

    public static void main(String[] args) throws FileNotFoundException, Exception {
        GUI gui = new GUI();

        File[] folders = new File[args.length + 1];
        File myFolder = new File("D:\\Ult1\\videos_RandomVideos\\videos_edited_audio");
        if(myFolder.exists())
            folders[0] = myFolder;
        int i = myFolder.exists() ? 1 : 0;
        for(String arg : args){
            folders[i] = new File(arg);
            i ++;
        }

        for(File folder : folders)
        for(File file : Objects.requireNonNull(folder.listFiles())){
            if(file.exists())
                buttons.put(file.getName(), gui.addButton(file.getName(), file));
        }
        gui.finishGUI();



    }


    public static class FileTransferable implements Transferable {

        private List listOfFiles;

        public FileTransferable(List listOfFiles) {
            this.listOfFiles = listOfFiles;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.javaFileListFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.javaFileListFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            return listOfFiles;
        }
    }




//    public static boolean isSimilar(String str, String str2){
//        int mistakes = 0;
//        int i = 0;
//        String shorter = str.length() < str2.length() ? str : str2;
//        for(char a : ((shorter == str) ? str2 : str).toCharArray()){
//            char b;
//            if(i < shorter.length())
//                b = shorter.toCharArray()[i];
//            else continue;
//            if(a != b) mistakes ++;
//            if(mistakes < 2) return true;
//            i ++;
//        }
//        return false;
//    } // this feels like a java****** function, so it's awful and i hate it...
    // and because it's java******-like it doesn't work...
    // i don't really like JS
}
