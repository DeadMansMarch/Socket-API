/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LOCAL;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.imageio.ImageIO;

/**
 *
 * @author DeadMansMarch
 */
public class FileSys {
    
    //NOT IN USE
    public static String EncodeBase64(String Encode){
        return Base64.encode(Encode.getBytes());
    }
    
    //Will stream an image directly out to an output stream from a relative file path.
    public static void streamImage(String Name,OutputStream OutStream) throws IOException{
        try{
            BufferedImage Image = ImageIO.read(new BufferedInputStream(new FileInputStream(new File(Name))));
            ByteArrayOutputStream OutputStream = new ByteArrayOutputStream();
            System.out.println(Image);
            ImageIO.write(Image, Name.substring(Name.lastIndexOf(".") + 1), OutputStream);
            OutStream.write(OutputStream.toByteArray());
            OutStream.flush();
            OutStream.close();
            System.out.println("Done");
        }catch(Exception E){
            System.out.println(E);
        }
    }
    
    //Will load a file given a relative file path.
    public static String load(String Name){
        try{
            String File = "";
            BufferedReader FileReader = new BufferedReader(new FileReader(Name));

            while(FileReader.ready()){
                File += FileReader.readLine();
            }
            return File;
        }catch(Exception E){
            System.out.println(E);
        }
        return "";
    }
    
    //Will save a file to a relative file path.
    public static void save(String Name,String File) {
        try (PrintWriter Saver = new PrintWriter(Name, "UTF-8")) {
            Saver.print(File);
        }catch(Exception E){
            System.out.println(E);
        }
    }
}
