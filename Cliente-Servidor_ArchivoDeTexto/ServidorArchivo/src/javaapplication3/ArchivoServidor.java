package javaapplication3;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ArchivoServidor {


    public static void main(String[] args){
        copiarArchivo(args);        
    }//FIN MAIN
    
    public static int validarPuerto(String args[]){
        int puerto = 0;
        boolean band = false;
        try{
            if(args[0].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos... ");
                System.out.println("Sigue el formato: \"puerto\" Ejemplo: \"2001\"");
                System.exit(0);
            }else{
                band = true;
                puerto = Integer.valueOf(args[0]);
                return puerto;
            }
        }catch(Exception e){
            if(band == false){
                System.out.println("Error al ingresar datos... " + e.toString());
                System.out.println("Sigue el formato: \"puerto\" Ejemplo: \"2001\"");
            }else{
                System.out.println("Puerto no valido... ");
            }
            System.exit(1);
        }
        return 0;
    }
    
    public static ServerSocket crearSocketServidor(int puerto){
        ServerSocket socketServidor;
        try{
            socketServidor = new ServerSocket(puerto);
            return socketServidor;
        }catch(IOException e){
            System.out.println("Error al crear el Socket Servidor...");
            System.exit(0);
        }
        return null;
    }

    public static Socket crearSocket(ServerSocket socketServidor){
        Socket socket;
        try{
            socket = socketServidor.accept();
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
            escritor = new PrintWriter(socket.getOutputStream(),true);
            return escritor;
        }catch(IOException e){
            System.out.println("Error al crear el escritor..." + e.toString());
            System.exit(0);
        }
        return null;
    }
    
    public static BufferedReader crearLector(Socket socket){
        BufferedReader lector;
        try{
            lector = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            return lector;
        }catch(IOException e){
            System.out.println("Error al crear el lector..." + e.toString());
            System.exit(0);
        }
        return null;
    }
    
    public static void cerrarSockets(Socket socket, ServerSocket socketServidor){
        try{
            socket.close();
        }catch(IOException e){
            System.out.println("Error al cerrar el socket " + e.toString());
            System.exit(0);
        }
        
        try{
             socketServidor.close();
         }catch(IOException e){
             System.out.println("Error al cerrar el socket " + e.toString());
             System.exit(0);
         }
    }
    
    public static void copiarArchivo(String[] args){       
        String entrada = "";
        String salida = "";
        Scanner scanner = null;
        PrintWriter escritor = null;
        int puerto = 0;
        ServerSocket socketServidor = null;
        Socket socket = null;
        BufferedReader lector = null;
        
        puerto = validarPuerto(args);
        socketServidor = crearSocketServidor(puerto);
        
        boolean error;
        do{
            error = false;
            System.out.println("Esperando un cliente...");
            socket = crearSocket(socketServidor);
            lector = crearLector(socket);
                        
            System.out.println("Esperando la ruta del archivo a copiar... ");
            
            try{
                entrada = lector.readLine();
            }catch(IOException e){
                System.out.println("Error al leer el lector...");
                error = true;
            }
            
            if(error == false){
                try{
                    escritor = new PrintWriter(socket.getOutputStream(),true);
                }catch(IOException e){
                    System.out.println("Error al crear el escritor..." + e.toString());
                    error = true;
                }
            }
            
            if(error == false){
                try{
                    scanner = new Scanner(new FileReader(entrada));
                }catch(FileNotFoundException e){
                    System.out.println("Archivo no encontrado en la ruta recibida...");
                    escritor.println("No existe");
                    error = true;
                }
            }
            
            if(error == false){
                while (scanner.hasNextLine()) {
                    salida = scanner.nextLine();
                    escritor.println(salida);
                }
                escritor.println("fin");
                System.out.println("Archivo copiado...");
            }
        }while(!entrada.equalsIgnoreCase("fin"));

        cerrarSockets(socket, socketServidor);
        System.exit(0);
    }
    
}//FIN CLASS
