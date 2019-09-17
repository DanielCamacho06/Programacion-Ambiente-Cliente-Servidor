import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ArchivoCliente {
    
    public static void main(String[] args){
        copiaArchivo(args);
    }//FIN MAIN
    
    public static String validarIp(String args[]){
        String ip;
        try{
            if(args[0].equalsIgnoreCase("")){
                System.out.println("Error al ingresar datos... ");
                System.out.println("Sigue el formato: \"IP\" \"puerto\" Ejemplo: \"127.0.0.1\" \"2001\"");
                System.exit(1);
            }else{
                ip = (args[0]);
                return ip;
            }
        }catch(Exception e){
            System.out.println("Error al ingresar datos... " + e.toString());
            System.out.println("Sigue el formato: \"IP\" \"puerto\" Ejemplo: \"127.0.0.1\" \"2001\"");
            System.exit(2);
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
                System.exit(1);
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
            System.exit(2);
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
            System.out.println("IP no valida, Puerto no valido o no hay conexión...");
            System.exit(1);
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
            System.exit(1);
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
            System.exit(1);
        }
        return null;
    }
    
    public String ruta(){
        URL ruta = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        return ruta.toString();
    }
        
    public static void copiaArchivo(String[] args){
        String archivo;
        String datosEntrada = null;
        FileWriter filewriter = null;
        PrintWriter printwriter = null;
        String ip = null;
        int puerto = 0;
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;
        
        Scanner scanner = new Scanner(System.in);
        
        ip = validarIp(args);
        puerto = validarPuerto(args);
        
        socket = crearSocket(ip, puerto);
        escritor = crearEscritor(socket);
        lector = crearLector(socket);
        
        System.out.println("Escribe la ruta completa del archivo a copiar... ");
        
        ArchivoCliente a = new ArchivoCliente();
        String ruta = a.ruta();
        int dD = ruta.indexOf(":") + 2;
        int nC = ruta.indexOf("ClienteArchivo") + 15;
        String rF = ruta.substring(dD,nC);     

        archivo = scanner.nextLine();
        escritor.println(archivo);  
        
        boolean band = false;
        boolean crearArchivo = false;
        do{
            try{
                datosEntrada=lector.readLine();
            }catch(IOException e){
                System.out.println("Error al obtener los datos del lector... " + e.toString());
                System.exit(1);
            }
            if(datosEntrada.equalsIgnoreCase("No existe")){
                System.out.println("El archivo no existe en esa ubicación... ");
                System.exit(2);
            }else{
                if(crearArchivo == false){
                    try{
                        filewriter = new FileWriter(rF + "copia.txt");
                    }catch(IOException e){
                        System.out.println("Error al crear el FileWriter... " + e.toString());
                        System.exit(3);
                    }
                    try{
                        printwriter = new PrintWriter(filewriter);
                    }catch(Exception e){
                        System.out.println("Error crear el PrintWriter... " + e.toString());
                        System.exit(4);            
                    }
                    crearArchivo = true;
                }    
                if(!datosEntrada.equalsIgnoreCase("fin")){
                    printwriter.println(datosEntrada);
                }else{
                    band = true;
                }   
            }
        }while(band == false);
        
        try{
            filewriter.close();
        }catch(IOException e){
            System.out.println("Error al obtener los datos del lector... " + e.toString());
            System.exit(5);
        }
        try{
            socket.close();
        }catch(IOException e){
            System.out.println("Error al cerrar el socket... " + e.toString());
            System.exit(6);
        }
    }
    
}//FIN CLASS