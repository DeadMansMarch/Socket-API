/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author DeadMansMarch
 */
public class Site {
    public String DEFAULT = "default.html";
    private HashMap<String,String> Caretaker = new HashMap<>();
    private HashMap<String,ArrayList<String>> Directories = new HashMap<>();
    
    public Site(){
    }
    
    public void addFile(String Path,String File){
        Caretaker.put(Path, File);
        
        String[] Split = Path.split("/");
        String CPath = "/";
        for (int i=1;i<Split.length;i++){
            String NCPath = CPath + Split[i];
            if (Directories.containsKey(CPath)){
                if (!Directories.containsKey(NCPath)){
                    Directories.get(CPath).add(Split[i]);
                }
            }else{
                Directories.putIfAbsent(CPath, new ArrayList<>(Arrays.asList(Split[i])));
            }
            CPath = NCPath;
        }
        
    }
    
    public String getFile(String Path){
        return Caretaker.get(Path);
    }
    
    public String[] List(String Path){
        if (Directories.get(Path) != null){
            return (String[]) Directories.get(Path).toArray(new String[Directories.get(Path).size()]);
        }
        return null;
    }
}
