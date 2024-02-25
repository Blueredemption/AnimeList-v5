package org.coopereisnor.utility;

import org.coopereisnor.Program;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UtilityMethods {

    public static Color[] medalColors = new Color[]{new Color(255, 215, 0), new Color(192, 192, 192), new Color(205, 127, 50)};

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bufferedImage;
    }

    public static void purge(File root){
        try{
            Files.walk(root.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }catch(IOException ex){
            Program.logger.error("IOException", ex);
        }
    }

    public static String getAsFormattedDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return date == null ? "?" : date.format(formatter);
    }

    public static String getAsCommaSeperatedString(String[] strings){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < strings.length; i++){
            builder.append(strings[i]);
            if(!(i + 1 == strings.length)){
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static String[] convertToStringArray(LinkedHashSet<String> set){
        ArrayList<String> list = new ArrayList<>(set);
        list.removeIf(s -> s.equals(""));
        Collections.sort(list);
        return list.toArray(new String[0]);
    }

    public static String getDatesString(LocalDate first, LocalDate second){
        if(!(first == null || second == null)) {
            if (first.equals(second)) {
                return UtilityMethods.getAsFormattedDate(first);
            }else{
                return UtilityMethods.getAsFormattedDate(first) + " to " + UtilityMethods.getAsFormattedDate(second);
            }
        } else {
            if(!(first == null && second == null)){
                return UtilityMethods.getAsFormattedDate(first) + " to " + UtilityMethods.getAsFormattedDate(second);
            }else{
                return  "";
            }
        }
    }

    public static String getFileAsString(Path filePath){
        String fileContent = "";

        try
        {
            byte[] bytes = Files.readAllBytes(filePath);
            fileContent = new String (bytes);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return fileContent;
    }

    public static String getStringOfColor(Color color){
        return "rgb(" +color.getRed() +", " +color.getGreen() +", " +color.getBlue() +")";
    }

    public static java.awt.Color convertColor(javafx.scene.paint.Color color){
        return new java.awt.Color((float) color.getRed(),
                                  (float) color.getGreen(),
                                  (float) color.getBlue(),
                                  (float) color.getOpacity());
    }

    public static javafx.scene.paint.Color convertColor(Color color){
        return javafx.scene.paint.Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0);
    }

    public static Path getLocalDataDirectory(){
        return Path.of(System.getProperty("user.dir"));
    }


    public static Color averageColor(ImageIcon imageIcon) {
        BufferedImage bi = toBufferedImage(imageIcon.getImage());
        int step = 5;
        int sampled = 0;
        long sumr = 0, sumg = 0, sumb = 0;
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (x % step == 0 && y % step == 0) {
                    Color pixel = new Color(bi.getRGB(x, y));
                    sumr += pixel.getRed();
                    sumg += pixel.getGreen();
                    sumb += pixel.getBlue();
                    sampled++;
                }
            }
        }
        return new Color(Math.round(sumr / sampled), Math.round(sumg / sampled), Math.round(sumb / sampled));
    }

    public static String capitalize(String str) {
        if(str == null || str.length()<=1) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
