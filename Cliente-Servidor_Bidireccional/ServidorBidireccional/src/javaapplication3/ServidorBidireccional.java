
package javaapplication3;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServidorBidireccional {


    public static void main(String[] args){
        comunicacion(args);
    }//FIN MAIN
    
    public static ServerSocket crearSocketServidor(int puerto){
        ServerSocket socketServidor = null;
        try{
            socketServidor = new ServerSocket(puerto);
            return socketServidor;
        }catch(IOException e){
            System.out.println("Erorr al crear el socket servidor" + e.toString());
            System.exit(1);
        }
        return null;
    }
    public static int validarPuerto(String args[]){
        int puerto = 0;
        boolean band = false;
        try{
            if(args[0].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos...");
                System.out.println("Sigue el formato: \"puerto\" Ejemplo: \"2001\" ");
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
    
    public static Socket crearSocket(ServerSocket socketServidor){
        Socket socket;
        try{
            socket = socketServidor.accept();
            return socket;
        }catch(IOException e){
            System.out.println("Error al crear el socket..." + e.toString());
            System.exit(2);
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
            System.exit(2);
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
            System.exit(2);
        }
        return null;
    }
    
    public static void comunicacion(String[] args){
        String entrada = "";
        Scanner scanner = new Scanner(System.in);
        String salida = "";
        int puerto = 0;
        ServerSocket socketServidor = null;
        Socket socket = null;
        BufferedReader lector = null;
        PrintWriter escritor = null;
        
        puerto = validarPuerto(args);
        socketServidor = crearSocketServidor(puerto);
        
        boolean band = false;
        boolean error = false;
        do{
            error = false;
            if(band == false){
            System.out.println("Esperando a un cliente...");
            socket = crearSocket(socketServidor);
            lector = crearLector(socket);
            escritor = crearEscritor(socket);
            
            System.out.println("---- Escribe fin para finalizar...");
            band = true;
            }
            try{
                entrada = lector.readLine();
                System.out.println("Cliente: " + entrada);
            }catch(IOException e){
                System.out.println("Error al leer la entrada de mensaje...");
                error = true;
            }
            
            if(error == false){
                if(entrada.equalsIgnoreCase("fin")){
                    band = false;
                    escritor.println("fin");
                }else{            
                    salida = scanner.nextLine();
                    escritor.println(salida);
                }                
            }else{
                band = false;
                try{
                    socket.close();
                }catch(IOException e){
                    System.out.println("Error al cerrar el socket cliente...");
                }
            }
        }while(!salida.equalsIgnoreCase("fin"));
        
        System.out.println("me voy");
        try{
            socketServidor.close();
        }catch(IOException e){
            System.out.println("Error al cerrar el socket servidor... ");
        }                
    }
    
}//FIN WHILE
