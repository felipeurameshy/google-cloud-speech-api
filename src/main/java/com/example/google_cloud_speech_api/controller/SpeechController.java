package com.example.google_cloud_speech_api.controller;

import com.example.google_cloud_speech_api.dto.SpeechDto;
import com.example.google_cloud_speech_api.service.SpeechService;
import org.springframework.beans.factory.annotation.Autowired;
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
}