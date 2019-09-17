
package javaapplication3;
import java.io.*;
import java.net.*;

public class MainClienteSimple {

    
    public static void main(String[] args){
        mensaje(args);
    }//Fin Main
    
    public static String validarIp(String args[]){
        String ip;
        try{
            if(args[0].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos... ");
                System.out.println("Sigue el formato: \"IP\" \"puerto\" \"Mensaje\" Ejemplo: \"127.0.0.1\" \"2001\" \"Hola\" ");
                System.exit(0);
            }else{
                ip = (args[0]);
                return ip;
            }
        }catch(Exception e){
            System.out.println("Error al ingresar datos... " + e.toString());
            System.out.println("Sigue el formato: \"IP\" \"puerto\" \"Mensaje\" Ejemplo: \"127.0.0.1\" \"2001\" \"Hola\" ");
            System.exit(0);
        }
        return "";
    }
    
    public static int validarPuerto(String args[]){
        int puerto;
        boolean band = false;
        try{
            if(args[1].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos... ");
                System.out.println("Sigue el formato: \"IP\" \"puerto\" \"Mensaje\" Ejemplo: \"127.0.0.1\" \"2001\" \"Hola\" ");
                System.exit(0);
            }else{
                band = true;
                puerto = Integer.valueOf(args[1]);
                return puerto;
            }
        }catch(Exception e){
            if(band == false){
                System.out.println("Error al ingresar datos...");
                System.out.println("Sigue el formato: \"IP\" \"puerto\" \"Mensaje\" Ejemplo: \"127.0.0.1\" \"2001\" \"Hola\" ");
            }else{
                System.out.println("Puerto no valido... ");
            }
            System.exit(1);
        }
        return 0;
    }
    
    public static Socket crearPuerto(String ip, int puerto){
        Socket socket;
        try{
            socket = new Socket(ip,puerto);
            return socket;
        }catch(IOException e){
            System.out.println("Error al crear el Socket... " + e.toString());
            System.exit(0);
        }
        return null;
    }
    
    public static PrintWriter enviarMensaje(Socket socket){
        PrintWriter escritor = null;
        try{
            escritor = new PrintWriter(
            socket.getOutputStream(), true);
            return escritor;
        }catch(IOException e){
            System.out.println("Error al mandar la info... " + e.toString());
            System.exit(2);
        }
        return null;
    }
    
    public static boolean validarMensaje(String args[]){
        boolean band = true;
        try{
            if(args[2].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos... ");
                System.out.println("Sigue el formato: \"IP\" \"puerto\" \"Mensaje\" Ejemplo: \"127.0.0.1\" \"2001\" \"Hola\" ");
                System.exit(0);
                return band = false;
            }
        }catch(Exception e){
            System.out.println("Error al ingresar datos...");
            System.out.println("Sigue el formato: \"IP\" \"puerto\" \"Mensaje\" Ejemplo: \"127.0.0.1\" \"2001\" \"Hola\" ");
            return band = false;
        }
        return band;
    }
    
    public static void cerrarSocket(Socket socket){
        try{
            socket.close();
        }catch(IOException e){
            System.out.println("Error al cerrar el socket... " + e.toString());
            System.exit(3);
        }
    }
    
    public static void mensaje(String args[]){
        Socket socket = null;
        PrintWriter escritor = null;
        String ip = null;
        int puerto = 0;

        ip = validarIp(args);
        puerto = validarPuerto(args);
        
        if(validarMensaje(args) == true){
            socket = crearPuerto(ip, puerto);
            escritor = enviarMensaje(socket);
            escritor.println(args[2]);            
        }else{
            System.exit(0);
        }
        
        cerrarSocket(socket);
    }
    
    
    
}//Fin Class
