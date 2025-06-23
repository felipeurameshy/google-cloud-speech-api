package com.example.google_cloud_speech_api.controller;

import com.example.google_cloud_speech_api.dto.SpeechDto;
import com.example.google_cloud_speech_api.service.ChatService;
import com.example.google_cloud_speech_api.service.SpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/speech")
public class SpeechController {

    @Autowired
    private SpeechService speechService;

    @PostMapping("/transcribe")
    public SpeechDto transcribeAudio(@RequestParam("file") MultipartFile file) throws Exception {
        return speechService.transcribe(file);
    }

    @PostMapping("/transcribe/chunk")
    public ResponseEntity<String> transcribe(@RequestParam("file") MultipartFile file) {
        try {
            // Aqui você pode converter o webm para wav se necessário,
            // e usar uma API como Google Speech-to-Text para transcrição.

            System.out.println("Recebido chunk: " + file.getOriginalFilename() + ", " + file.getSize() + " bytes");
            speechService.transcribeChunk(file);

            return ResponseEntity.ok("Chunk recebido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar áudio");
        }
    }

}