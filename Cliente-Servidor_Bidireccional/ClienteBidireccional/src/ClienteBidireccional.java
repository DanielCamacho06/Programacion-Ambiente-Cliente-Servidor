import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClienteBidireccional {
    
    public static void main(String[] args){
        comunicacion(args);
        
    }//FIN MAIN
    
    public static String validarIp(String args[]){
        String ip;
        try{
            if(args[0].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos...");
                System.out.println("Sigue el formato: \"IP\" \"puerto\" Ejemplo: \"127.0.0.1\" \"2001\"");
                System.exit(0);
            }else{
                ip = (args[0]);
                return ip;
            }
        }catch(Exception e){
            System.out.println("Error al ingresar datos... " + e.toString());
            System.out.println("Sigue el formato: \"IP\" \"puerto\" Ejemplo: \"127.0.0.1\" \"2001\"");
            System.exit(0);
        }
        return "";
    }
    
    public static int validarPuerto(String args[]){
        int puerto;
        boolean band = false;
        try{
            if(args[1].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos...");
                System.out.println("Sigue el formato: \"IP\" \"puerto\" Ejemplo: \"127.0.0.1\" \"2001\"");
                System.exit(0);
            }else{
                band = true;
                puerto = Integer.valueOf(args[1]);
                return puerto;
            }
        }catch(Exception e){
            if(band == false){
                System.out.println("Error al ingresar datos... " + e.toString());
                System.out.println("Sigue el formato: \"IP\" \"puerto\" Ejemplo: \"127.0.0.1\" \"2001\"");
            }else{
                System.out.println("Puerto no valido... ");
            }
            System.exit(1);
        }
        return 0;
    }
    
    public static Socket crearSocket(String ip, int puerto){
        Socket socket;
        try{
            socket = new Socket(ip,puerto);
            return socket;
        }catch(IOException e){
            System.out.println("Error al crear el socket..." + e.toString());
            System.exit(0);
        }
        return null;
    }
    
    public static PrintWriter crearEscritor(Socket socket){
        PrintWriter escritor;
        try{
            escritor = new PrintWriter(
                socket.getOutputStream(),true
            );
            return escritor;
        }catch(IOException e){
            System.out.println("Error al crear el escritor..." + e.toString());
            System.exit(0);
        }   
        return null;
    }
    public static BufferedReader crearLector(Socket socket){
        BufferedReader lector = null;
        try{
            lector = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
            );
            return lector;
        }catch(IOException e){
            System.out.println("Error al crear el lector..." + e.toString());
        }
        return null;
    }
    
    public static void comunicacion(String[] args){
        String datos;
        String datosEntrada = "";
        Scanner scanner = new Scanner(System.in);
        
        String ip = null;
        int puerto = 0;
        Socket socket = null;
        BufferedReader lector = null;
        PrintWriter escritor = null;
        
        ip = validarIp(args);
        puerto = validarPuerto(args);        
        
        socket = crearSocket(ip, puerto);
        escritor = crearEscritor(socket);
        lector = crearLector(socket);
        
        System.out.println("---- Escribe fin para finalizar...");

        while(true){
            datos = scanner.nextLine();
            escritor.println(datos);
            
            if(datos.equalsIgnoreCase("fin")){
                try{
                    socket.close();
                    System.exit(0);
                }catch(IOException e){
                    System.out.println("Error al cerrar el socket... " + e.toString());
                    System.exit(0);
                }
            }else{
                try{
                    datosEntrada = lector.readLine();
                }catch(IOException e){
                    System.out.println("Error al recibir mensaje");
                    System.exit(0);
                }
                System.out.println("Servidor: " + datosEntrada);
                if(datosEntrada.equalsIgnoreCase("fin")){
                    System.out.println("El servidor ha sido cerrado... ");
                    try{
                        socket.close();
                        System.exit(0);
                    }catch(IOException e){
                        System.out.println("Error al cerrar el socket..." + e.toString());
                        System.exit(0);
                    }
                }   
            }            
        }
    }
    
    
}//FIN CLASS