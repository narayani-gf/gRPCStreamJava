package grpcholamundostream.cliente;

import com.proto.saludo.SaludoServiceGrpc;
import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Cliente {
    public static void main(String[] args) {
        // Establecemos el servidor gRPC
        String host = "localhost";
        // Establecemos el puerto gRPC
        int puerto = 8080;

        // Creamos el canal de comunicación
        ManagedChannel ch = ManagedChannelBuilder
            .forAddress(host, puerto)
            .usePlaintext()
            .build();

        // Saludo una vez (unario)
        // saludarUnario(ch);

        // Saludo muchas veces (stream)
        // saludarStream(ch);

        // Leer archivo
        saludarArchivoStream(ch);

        // Terminamos la comunicación
        System.out.println("Apagando...");
        ch.shutdown();
    }

    // Saludo una vez (unario)
    public static void saludarUnario(ManagedChannel ch) {
        // Obtenemos la referencia al stub
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);
        // Coonstruimos la petición enviando un parámetro
        SaludoRequest peticion = SaludoRequest.newBuilder().setNombre("Nara").build();
        // Usando el stub, realizamos la llamada gRPC
        SaludoResponse respuesta = stub.saludo(peticion);

        // Imprimimos la respuesta de gRPC
        System.out.println("Respuesta RPC: " + respuesta.getResultado());
    }

    // Saludo muchas veces (stream)
    public static void saludarStream(ManagedChannel ch) {
        // Obtenemos la referencia al stub
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);
        // Coonstruimos la petición enviando un parámetro
        SaludoRequest peticion = SaludoRequest.newBuilder().setNombre("Nara").build();
        
        // Usando el stub, realizamos la llamada gRPC
        // Como es un stream, iteramoso hasta que no haya más datos
        stub.saludoStream(peticion).forEachRemaining(respuesta -> {
            // Imprimimos la respuesta de gRPC
            System.out.println("Respuesta RPC: " + respuesta.getResultado());
        });        
    }

    public static void saludarArchivoStream(ManagedChannel ch) {
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);
        SaludoRequest peticion = SaludoRequest.newBuilder().setNombre("archivote.csv").build();

        stub.saludoArchivoStream(peticion).forEachRemaining(respuesta -> {
            // Imprimimos la respuesta de gRPC
            System.out.println(respuesta.getResultado());
        }); 
    }
}
