package grpcholamundostream.servidor;

import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;

import java.util.Scanner;

import com.proto.saludo.SaludoServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ServidorImpl extends SaludoServiceGrpc.SaludoServiceImplBase {
    @Override
    public void saludo(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver) {
        // Se construye la respuesta a enviarle al cliente
        SaludoResponse respuesta = SaludoResponse.newBuilder().setResultado("Hola " + request.getNombre()).build();

        // En gRPC se utiliza onNext para envar la respuesta
        // En llamadas unitarias, solo se llama una vez
        responseObserver.onNext(respuesta);

        // Avisa que se ha terminado
        responseObserver.onCompleted();
    }


    @Override
    public void saludoStream(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver) {
        // Es la misma función de arriba, solo con este for
        // para simular el envío de múltiples chunks de datos
        for (int i = 0; i <= 10; i++) {
            // Se construye la respuesta a enviarle al cliente
            SaludoResponse respuesta = SaludoResponse.newBuilder()
                    .setResultado("Hola " + request.getNombre() + " por " + i + " vez.").build();

            // En gRPC se utiliza onNext para envar la respuesta
            // En llamadas unitarias, solo se llama una vez
            responseObserver.onNext(respuesta);
        }

        // Avisa que se ha terminado
        responseObserver.onCompleted();
    }

    public void saludoArchivoStream(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver) {
        // Obtenemos el nombre del archivo que quiere el cliente
        String archivoNombre = "/" + request.getNombre();
        System.out.println("Enviando el archivo: " + ServidorImpl.class.getResource(archivoNombre) + "...");

        // Abrir un glujo a un archivo grande para que tarde enviándolo
        try (Scanner scanner = new Scanner(ServidorImpl.class.getResourceAsStream(archivoNombre))){
            while (scanner.hasNextLine()) {
                // Se construye la respuesta a enviarle al cliente
                SaludoResponse respuesta = SaludoResponse.newBuilder().setResultado(scanner.nextLine()).build();
                System.out.print(".");

                // En gRPC se utiliza onNext para enviar la respuesta
                // En llamadas unarias, solo se llama una vez
                responseObserver.onNext(respuesta);
            }
        }

        System.out.println("\nEnvio de archivo completado.");
        responseObserver.onCompleted();
    }
}
