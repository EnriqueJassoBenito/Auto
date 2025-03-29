package mx.edu.utez.automoviles.modules.car;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    private final Path rootLocation;
    private final String uploadDir;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadDir = uploadDir;
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        init();
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el directorio de almacenamiento", e);
        }
    }


    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("No se puede almacenar un archivo vacío");
            }

            // Generar nombre único para el archivo
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // Validar nombre del archivo
            if (filename.contains("..")) {
                throw new RuntimeException(
                        "Nombre de archivo contiene secuencia inválida: " + originalFilename);
            }

            // Copiar el archivo al directorio de destino
            try (InputStream inputStream = file.getInputStream()) {
                Path targetLocation = this.rootLocation.resolve(filename);
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                return filename;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al almacenar el archivo: " + file.getOriginalFilename(), e);
        }
    }

    /**
     * Carga un archivo como recurso
     * @param filename Nombre del archivo a cargar
     * @return Recurso del archivo
     */
    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("Archivo no encontrado: " + filename);
            }

            if (!resource.isReadable()) {
                throw new RuntimeException("Archivo no se puede leer: " + filename);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al cargar el archivo: " + filename, e);
        }
    }

    /**
     * Elimina un archivo del almacenamiento
     * @param filename Nombre del archivo a eliminar
     */
    public void deleteFile(String filename) {
        try {
            Path filePath = this.rootLocation.resolve(filename).normalize();
            if (!Files.exists(filePath)) {
                throw new RuntimeException("Archivo no existe: " + filename);
            }
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo: " + filename, e);
        }
    }

    /**
     * Lista todos los archivos en el directorio de almacenamiento
     * @return Stream de nombres de archivos
     */
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer archivos almacenados", e);
        }
    }

    /**
     * Obtiene la ruta completa del archivo
     * @param filename Nombre del archivo
     * @return Ruta completa como String
     */
    public String getFullPath(String filename) {
        return this.rootLocation.resolve(filename).toString();
    }

    /**
     * Obtiene el directorio de uploads configurado
     * @return Ruta del directorio
     */
    public String getUploadDir() {
        return this.uploadDir;
    }
}