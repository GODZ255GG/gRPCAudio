package grpcaudio.servidor;

import java.io.IOException;
import java.io.InputStream;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.ByteString;

import com.proto.audio.Audio.DataChunkResponse;
import com.proto.audio.Audio.DownloadFileRequest;
import com.proto.audio.AudioServiceGrpc;

public class ServidorImpl extends AudioServiceGrpc.AudioServiceImplBase{
    @Override
    public void downloadAudio(DownloadFileRequest request, StreamObserver<DataChunkResponse> responseObserver){
        String archivoNombre = "/" + request.getNombre();
        System.out.println("\n\nEnviando el archivo: " + request.getNombre());

        InputStream fileStream = ServidorImpl.class.getResourceAsStream(archivoNombre);

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int length;
        try{
            while((length = fileStream.read(buffer, 0, bufferSize)) != -1){
                DataChunkResponse respuesta = DataChunkResponse.newBuilder()
                        .setData(ByteString.copyFrom(buffer, 0, length))
                        .build();

                        System.out.print(".");
                responseObserver.onNext(respuesta);
            }
            fileStream.close();
        }catch(IOException e){
            System.out.println("No se pudo obtener el archivo " + archivoNombre);
        }
        responseObserver.onCompleted();

    }
}
