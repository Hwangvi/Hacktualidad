package com.Hacktualidad.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryStorageService {

    private final Cloudinary cloudinary;

    public CloudinaryStorageService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {


        System.out.println(">>> [Cloudinary Init] cloudName: " + (cloudName != null ? "OK" : "NULL"));
        System.out.println(">>> [Cloudinary Init] apiKey: " + (apiKey != null ? "OK" : "NULL"));
        System.out.println(">>> [Cloudinary Init] apiSecret: " + (apiSecret != null ? "OK" : "NULL"));

        if (cloudName == null || apiKey == null || apiSecret == null) {
            throw new IllegalArgumentException("ERROR: Las credenciales de Cloudinary no se han cargado correctamente desde las variables de entorno.");
        }

        try {
            this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName.trim(),
                    "api_key", apiKey.trim(),
                    "api_secret", apiSecret.trim()
            ));
            System.out.println(">>> [Cloudinary Init] ¡Servicio inicializado con éxito!");
        } catch (Exception e) {
            throw new RuntimeException("Error crítico al inicializar el objeto Cloudinary: " + e.getMessage(), e);
        }
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