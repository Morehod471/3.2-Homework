package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService service;

    public AvatarController(AvatarService service) {
        this.service = service;
    }

    @PostMapping(value = "/{studentId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public void uploadAvatar(@PathVariable Long studentId,
                             @RequestParam MultipartFile avatar) throws IOException {
        service.uploadAvatar(studentId, avatar);
    }


    @GetMapping("/{studentid}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long studentid) {
        return service.downloadAvatar(studentid);
    }

    @GetMapping( "/{studentid}/avatar-from-file")
    public void downloadAvatarFromFile(@PathVariable Long studentid, HttpServletResponse response) throws IOException{
        service.downloadAvatarFromFile(studentid, response);
    }
}


