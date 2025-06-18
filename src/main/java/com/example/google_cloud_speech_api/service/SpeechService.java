package com.example.google_cloud_speech_api.service;

import com.example.google_cloud_speech_api.dto.SpeechDto;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class SpeechService {

    @Autowired
    private ChatService chatService;

    public SpeechDto transcribe(MultipartFile file) throws IOException {
        try (SpeechClient speechClient = SpeechClient.create()) {
            ByteString audioBytes = ByteString.readFrom(file.getInputStream());

            //O áudio precisa estar em formato WAV com 16KHz, mono e PCM (Linear16)

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("pt-BR")
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            StringBuilder transcript = new StringBuilder();

            for (SpeechRecognitionResult result : response.getResultsList()) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                transcript.append(alternative.getTranscript()).append("\n");
            }

            String message = transcript.toString();
            String medicalComplaint = getMedicalComplaint(message);
            String solution = getSolutions(medicalComplaint);

            return SpeechDto.builder()
                    .message(message)
                    .medicalComplaint(medicalComplaint)
                    .solution(solution)
                    .build();
        }
    }

    public String getMedicalComplaint(String message){
        String text = "Gerar um resumo somente sitomas de doenças desse texto: "+message;
        return chatService.chat(text);
    }

    public String getSolutions(String message){
        String text = "Como base no texto me retorne um sid medico: "+message;
        return chatService.chat(text);
    }

}