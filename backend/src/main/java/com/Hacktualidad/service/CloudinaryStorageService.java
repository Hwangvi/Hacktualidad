package com.Hacktualidad.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryStorageService {

    private final Cloudinary cloudinary;

    public CloudinaryStorageService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dqkv5bmt1",
                "api_key", "311116265369515",
                "api_secret", "Kjmlt58WeO3x-He-mUi4jZt1IJ0"
        ));
    }

    public void deleteFile(String photoUrl) {
        if (photoUrl == null || photoUrl.isEmpty() || !photoUrl.contains("cloudinary.com")) {
            return;
        }

        try {
            String publicId = extractPublicId(photoUrl);

            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println(">>> Imagen eliminada de Cloudinary con éxito: " + publicId);

        } catch (IOException e) {
            System.err.println("Error al intentar borrar el archivo de Cloudinary: " + e.getMessage());
        }
    }

    private String extractPublicId(String photoUrl) {
        int lastSlashIndex = photoUrl.lastIndexOf("/");
        int lastDotIndex = photoUrl.lastIndexOf(".");

        if (lastSlashIndex != -1 && lastDotIndex != -1 && lastDotIndex > lastSlashIndex) {
            return photoUrl.substring(lastSlashIndex + 1, lastDotIndex);
        }
        return photoUrl;
    }
    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("El archivo está vacío.");
            }
            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo a Cloudinary: " + e.getMessage());
        }
    }
}