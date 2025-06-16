package com.example.google_cloud_speech_api.service;

import com.example.google_cloud_speech_api.dto.SpeechDto;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class SpeechService {

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

            return SpeechDto.builder()
                    .message(transcript.toString())
                    .build();
        }
    }
}