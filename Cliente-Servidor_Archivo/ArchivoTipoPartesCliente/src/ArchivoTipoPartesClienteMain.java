
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;


public class ArchivoTipoPartesClienteMain {

    public static void main(String[] args) {
        copiaArchivo(args);
    }//FIN CLASS

    public static String validarIp(String args[]){
        String ip;
        try{
            if(args[0].equalsIgnoreCase("")){
            System.out.println("Error al ingresar datos... ");
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
                System.out.println("Error al ingresar datos... ");
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
    
    public String ruta(){
        URL ruta = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        return ruta.toString();
    }
    
    public static void copiaArchivo(String[] args){       
        Scanner scanner = new Scanner(System.in);
        
        String ip = null;
        ip = validarIp(args);
        
        int puerto = validarPuerto(args);
        
        Socket socket = crearSocket(ip, puerto);
        
        PrintWriter escritor = crearEscritor(socket);
        
        BufferedReader lector = crearLector(socket);
        
        ArchivoTipoPartesClienteMain a = new ArchivoTipoPartesClienteMain();
        String ruta = a.ruta();
        int dD = ruta.indexOf(":") + 2;
        int nC = ruta.indexOf("ArchivoTipoPartesCliente") + 24;
        String rF = ruta.substring(dD,nC);     
        
        System.out.println("Escribe la ruta completa del archivo a copiar... ");
        String enviarNombreArchivo;
        enviarNombreArchivo = scanner.nextLine();
        escritor.println(enviarNombreArchivo);  
        
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        DataInputStream dis = null;
        
        byte[] recibirDatos;
        int in;
        String archivoCopiado = null;
                
        try{
            //Recibimos el nombre del fichero
            archivoCopiado = lector.readLine();
        }catch(IOException e){
            System.out.println("El archivo no existe... ");
            System.exit(0);
        }
        
        long tamaño = 0;
        
        try{
            //Recibimos el tamaño del archivo a copiar
            tamaño = Long.parseLong(lector.readLine());
        }catch(IOException e){
            System.out.println("Error al recibir el tamaño del archivo a copiar... " + e.toString());
            System.exit(1);
        }
                
        System.out.println("Tamaño del archivo a copiar: " + tamaño);
        
        recibirDatos = new byte[1024];
        try{
            bis = new BufferedInputStream(socket.getInputStream());
        }catch(IOException e){
            System.out.println("Error al crear el BufferedInputStream... " + e.toString());
            System.exit(2);
        }
            
        try{
            dis = new DataInputStream(socket.getInputStream());
        }catch(IOException e){
            System.out.println("Error al crear el DataInputStream... " + e.toString());
            System.exit(3);
        }
        
        try{
            //Para guardar fichero recibido
            bos = new BufferedOutputStream(new FileOutputStream(rF + "/" + archivoCopiado));
        }catch(FileNotFoundException e){
            System.out.println("Archivo " + archivoCopiado + " no encontrado... " + e.toString());
            System.exit(4);
        }
        
        try{
            char[] animacion = new char[]{'|', '/', '-', '\\'};
            
            long tamañoPasado = 0;
            int i = 1;
            
            while ((in = bis.read(recibirDatos)) != -1){
                
                    bos.write(recibirDatos,0,in);
                    
                    tamañoPasado = tamañoPasado + in;
                    int  porcentaje = (int) ((tamañoPasado*100)/tamaño);
                    int barra = porcentaje / 5;
                    int espacios = porcentaje /5;
                    
                    System.out.print("[");
                    for(int x=0; x<barra;x++){
                        System.out.print("■");
                    }
                    
                    for(int x=0;x<20-espacios;x++){
                        System.out.print("-");
                    }
                    System.out.print("] Progreso: " + porcentaje + "% " + animacion[i % 4] + "\r");
                    i++;
            }
            System.out.println("\nArchivo Recibido!");
        }catch(IOException e){
            System.out.println("Erorr al recibir los datos del archivo copiandose del servidor... " + e.toString());
            System.exit(5);
        }

        try{
            bos.close();
        }catch(IOException e){
            System.out.println("Erorr al cerrar el BufferedOutputStream... " + e.toString());
            System.exit(6);
        }
        
        try{
            dis.close();
        }catch(IOException e){
            System.out.println("Erorr al cerrar el DataInputStream... " + e.toString());
            System.exit(7);
        }
        
        try{
            socket.close();
        }catch(IOException e){
            System.out.println("Error al obtener los datos del lector...");
            System.exit(8);
        }
    }
    
}//FIN MAIN
