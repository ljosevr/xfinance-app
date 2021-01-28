package com.gigti.xfinance.backend.services;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gigti.xfinance.backend.others.FileUtils;
import com.gigti.xfinance.backend.others.Response;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AmazonS3ImageService extends AmazonClientService {

    Logger logger = LoggerFactory.getLogger(CategoriaProductoServiceImpl.class);

    // Upload a List of Images to AWS S3.
//    public List<AmazonImage> insertImages(List<MultipartFile> images) {
//        List<AmazonImage> amazonImages = new ArrayList<>();
//        images.forEach(image -> amazonImages.add(uploadImageToAmazon(image)));
//        return amazonImages;
//    }

    // Upload image to AWS S3.
    public Response uploadImageToAmazon(MultipartFile multipartFile) {
        Response response = new Response();
        // Valid extensions array, like jpeg/jpg and png.
        List<String> validExtensions = Arrays.asList("jpeg", "jpg", "png", "pdf");

        // Get extension of MultipartFile
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!validExtensions.contains(extension)) {
            // If file have a invalid extension, call an Exception.
            logger.warn("Extension de Imagen Invenlida");
            response.setSuccess(false);
            response.setMessage("Extension Imagen Invalida");
            response.setCode("700");
        } else {
            // Upload file to Amazon.
            String url = uploadMultipartFile(multipartFile);
            if(url==null){
                response.setSuccess(false);
                response.setMessage("No se logro obtener Url de Imagen");
            } else {
                response.setObject(url);
                response.setSuccess(true);
                response.setMessage("Imagen OK al Repositorio");
            }

        }
        return response;
    }

    public boolean removeImageFromAmazon(String url) {
        try {
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            getClient().deleteObject(new DeleteObjectRequest(getBucketName(), fileName));
            return true;
        } catch(Exception e) {
            logger.error("Error al Eliminar Imagen: "+e.getMessage(), e);
            return false;
        }
    }

    // Make upload to Amazon.
    private String uploadMultipartFile(MultipartFile multipartFile) {
        String fileUrl;

        try {
            // Get the file from MultipartFile.
            File file = FileUtils.convertMultipartToFile(multipartFile);

            // Extract the file name.
            String fileName = FileUtils.generateFileName(multipartFile);

            // Upload file.
            uploadPublicFile(fileName, file);

            // Delete the file and get the File Url.
            file.delete();
            fileUrl = getUrl().concat(fileName);
        } catch (IOException e) {
            // If IOException on conversion or any file manipulation, call exception.
            logger.error("multipart.to.file.convert.except: "+e.getMessage(), e);
            fileUrl = null;
        }

        return fileUrl;
    }

    // Send image to AmazonS3, if have any problems here, the image fragments are removed from amazon.
    // Font: https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html#putObject%28com.amazonaws.services.s3.model.PutObjectRequest%29
    private void uploadPublicFile(String fileName, File file) {
        getClient().putObject(new PutObjectRequest(getBucketName(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

}
