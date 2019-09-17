import java.net.*;
import java.io.*;

public class MainServidorSimple {

    
    public static void main(String[] args){
        mensaje(args);
    }//FIN MAIN
    
    public static int validarPuerto(String args[]){
        int puerto = 0;
        boolean band = false;        
        try{
            if(args[0].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos...");
                System.out.println("Sigue el formato: \"puerto\" Ejemplo: \"2001\"");
                System.exit(0);
            }else{
                puerto = Integer.valueOf(args[0]);
                return puerto;
            }
        }catch(Exception e){
            if(band == false){
                System.out.println("Error al ingresar datos... " + e.toString());
                System.out.println("Sigue el formato: \"puerto\" Ejemplo: \"2001\"  ");
            }else{
                System.out.println("Puerto no valido... " + e.toString());
            }
            System.exit(1);
        }
        return 0;
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
    
    public static ServerSocket crearSocketServidor(int puerto){
        ServerSocket socketServidor = null;
        try{
            socketServidor = new ServerSocket(puerto);
            return socketServidor;
        }catch(IOException e){
            System.out.println("Error al crear el socket...");
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
            System.out.println("Error al crear el lector...");
            System.exit(0);
        }
        return null;
    }
    
    public static void mensaje(String[] args){
        Socket socket = null;
        BufferedReader lector = null;
        ServerSocket socketServidor = null;
        int puerto = 0;
        
        puerto = validarPuerto(args);
        socketServidor = crearSocketServidor(puerto);
        
        String entrada = "";
        boolean error = false;
        do{
            error = false;
            System.out.println("Esperando un cliente...");
            socket = crearSocket(socketServidor);
            lector = crearLector(socket);
            
            try{
                entrada = lector.readLine();
            }catch(IOException e){
                System.out.println("Error al leer el mensaje...");
                error = true;
            }
            
            if(error == false){
                System.out.println("Me dijeron " + entrada);
            }
        }while(!entrada.equalsIgnoreCase("fin"));
    }

}//FIN CLASS