/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LOCAL;

import java.util.HashSet;

/**
 *
 * @author DeadMansMarch
 */

//When using one computer to monitor clients, it gets pretty annoying to see the output.
public class Log {
    public static HashSet<Class<?>> NWRITEABLE = new HashSet<>();
    public static HashSet<Object> NOWRITEABLE = new HashSet<>();
    public static String Header = "";
    private static Class<?> Last;
    public static void Enable(Class<?> From){
        NWRITEABLE.remove(From);
    }
    
    public static void Disable(Class<?> From){
        NWRITEABLE.add(From);
    }
    
    public static boolean IsEnabled(Class<?> From){
        return !NWRITEABLE.contains(From);
    }
    
    public static void Enable(Object From){
        NOWRITEABLE.remove(From);
    }
    
    public static void Disable(Object From){
        NOWRITEABLE.add(From);
    }
    
    public static boolean IsEnabled(Object From){
        return !NOWRITEABLE.contains(From) || !NWRITEABLE.contains(From.getClass());
    }
    
    public static void Write(Class<?> From, Object K){
        
        if (IsEnabled(From)){
            System.out.print("[" + From.getSimpleName() + "] ");
            System.out.println(K);
        }
        Last = From;
    }
    
    public static void WriteL(Class<?> From, Object K){
        
        if (IsEnabled(From)){
            System.out.print("[" + From.getSimpleName() + "] ");
            System.out.print(K);
        }
        Last = From;
    }
    
    public static void Write(Object K){
        Write(Last,K);
    }
    
    public static void WriteL(Object K){
        Write(Last,K);
    }
    //
    public static void Write(Object From, Object K){
        if (IsEnabled(From)){
            Write(From.getClass(),K);
        }
    }
    
    public static void WriteL(Object From, Object K){
        if (IsEnabled(From)){
            WriteL(From.getClass(),K);
        }
    }
}
