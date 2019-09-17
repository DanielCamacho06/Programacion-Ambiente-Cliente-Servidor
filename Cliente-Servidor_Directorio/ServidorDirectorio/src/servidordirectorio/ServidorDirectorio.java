package servidordirectorio;
import java.io.*;
import java.net.*;

public class ServidorDirectorio {

    public static void main(String[] args) {
        directorio(args);
    }//FIN MAIN
    
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
        ServerSocket socketServidor;
        try{
            socketServidor = new ServerSocket(puerto);
            return socketServidor;
        }catch(IOException e){
            System.out.println("Error al crear el socket servidor..." + e.toString());
            System.exit(0);
        }
        return null;
    }
  
    public static int validarPuerto(String args[]){
        int puerto = 0;
        try{
            if(args[0].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos... ");
                System.out.println("Sigue el formato: \"puerto\" Ejemplo: \"2001\"");
                System.exit(0);
            }else{
                puerto = Integer.valueOf(args[0]);
                return puerto;
            }
        }catch(Exception e){
            System.out.println("Error al ingresar datos... " + e.toString());
            System.out.println("Sigue el formato: \"puerto\" Ejemplo: \"2001\"");
            System.exit(1);
        }
        return 0;
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
    
    public static void directorio(String[] args){       
        String entrada = "";
        String salida = "";
        PrintWriter escritor = null;
        String[] elementos = null;
        int puerto = 0;
        ServerSocket socketServidor = null;
        Socket socket = null;
        BufferedReader lector = null;
        
        puerto = validarPuerto(args);
        socketServidor = crearSocketServidor(puerto);
        
        boolean error;
        do{
            error = false;
            System.out.println("Esperando a un cliente...");
            socket = crearSocket(socketServidor);
            lector = crearLector(socket);

            System.out.println("Esperando la ruta del directorio... ");

            try{
                escritor = new PrintWriter(socket.getOutputStream(),true);
            }catch(IOException e){
                System.out.println("Error al crear el escritor..." + e.toString());
                error = true;
            }
            
            if(error == false){
                try{
                    entrada = lector.readLine();
                }catch(IOException e){
                    System.out.println("Error al leer el lector...");
                    error = true;
                }
            }
            
            if(entrada.equalsIgnoreCase("fin")){
                escritor.println("fin");
                System.exit(0);
            }

            if(error == false){
                File directorio = new File(entrada);
                File[] tipo = directorio.listFiles();

                if(!directorio.exists()){
                    escritor.println("No existe");
                }else{
                    if(!directorio.canRead()){
                        escritor.println("No acceso");
                    }else{        
                        for (int x=0;x<tipo.length;x++){
                            File tipos = tipo[x];
                            if(tipos.isFile()){
                                escritor.println("Archivo: " + tipo[x].getName());
                            }else{
                                if(tipos.isDirectory()){
                                    escritor.println("Carpeta: " + tipo[x].getName());
                                }else{
                                    escritor.println(tipo[x].getName());
                                }
                            }
                        }
                        escritor.println("fin");
                    }
                }
            }
        }while(!entrada.equalsIgnoreCase("fin"));
        
        escritor.println("fin");
        
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
        System.exit(0);
    }
    
}//FIN CLASS
