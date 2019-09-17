import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ArchivoTipoPartesServidorMain {


    public static void main(String[] args) {
        copiarArchivo(args);
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
            System.out.println("Error al crear el Socket Servidor... " + e.toString());
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
            System.out.println("Error al crear el escritor... " + e.toString());
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
             System.exit(1);
         }
    }
    
    public static void copiarArchivo(String[] args){  
        
        int puerto;
        puerto = validarPuerto(args);

        
        do{
            ServerSocket socketServidor = null;
            socketServidor = crearSocketServidor(puerto);
            
            System.out.println("Esperando un cliente...");
            Socket socket = null;
            socket = crearSocket(socketServidor);
            
            BufferedReader lector = null; 
            lector = crearLector(socket);
                        
            System.out.println("Esperando la ruta del archivo a mandarte... ");
     
            String entrada = "";
            try{
                entrada = lector.readLine();
            }catch(IOException e){
                System.out.println("Error al leer la entrada de datos... " + e.toString());
                System.exit(1);
            }
            
            PrintWriter escritor = null;
            try{
                escritor = new PrintWriter(socket.getOutputStream(),true);
            }catch(IOException e){
                System.out.println("Error al crear el escritor..." + e.toString());
                System.exit(2);
            }
            
            String nombreArchivo = "";
            nombreArchivo = entrada;
            DataInputStream input;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int in;
            byte[] byteArray;
            
            //Fichero a transferir
            File localFile = new File(nombreArchivo);
            try{
                bis = new BufferedInputStream(new FileInputStream(localFile));
            }catch(FileNotFoundException e){
                System.out.println("Error al crear el BufferedInputStream... " + e.toString());
                escritor.println("El archivo no existe...");
                System.exit(3);
            }
            try{
                bos = new BufferedOutputStream(socket.getOutputStream());
            }catch(IOException e){
                System.out.println("Error al crear el BufferedOututStream... " + e.toString());
                System.exit(4);
            }
           
            try{
                //Enviamos el nombre del fichero
                DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
            }catch(IOException e){
                System.out.println("Error al crear el DataOutputStream... " + e.toString());
                System.exit(5);
            }
            
            escritor.println(localFile.getName());
            System.out.println("Nombre del archivo a copiar: " + localFile.getName());

            long tamaño;
            tamaño = localFile.length();
            escritor.println(tamaño);
            System.out.println("Tamaño del archivo a copiar: " + tamaño);
            
            try{
                //Enviamos el fichero
                byteArray = new byte[10000];
                while ((in = bis.read(byteArray)) != -1){
                    bos.write(byteArray,0,in);
                }
            }catch(IOException e){
                System.out.println("Error al enviar el fichero... " + e.toString());
                System.exit(6);
            }
            
            System.out.println("Archivo enviado!");
            
            try{
               bis.close();
            }catch(IOException e){
                System.out.println("Error al cerrar el BufferedInputStream... " + e.toString());
                System.exit(7);
            }
            try{
                bos.close();
            }catch(IOException e){
                System.out.println("Error al cerrar el BufferedOutputStream... " + e.toString());
                System.exit(8);
            }
            
            cerrarSockets(socket, socketServidor);
        }while(true);

    }
    
}//FIN CLASS
