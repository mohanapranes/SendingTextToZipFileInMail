package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CreateZip {
    void toZip(Path file,ZipOutputStream zos) throws IOException {
        try(FileInputStream fis = new FileInputStream(file.toFile())){
            ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            fis.close();
            zos.closeEntry();
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
    }

    public void filesToZip() throws IOException {
        String pathOfFolder = "folder";
        String zipName = "txtZip.zip";
        FileOutputStream fos = new FileOutputStream(zipName);
        ZipOutputStream zos = new ZipOutputStream(fos);
        Files.walk(Paths.get(pathOfFolder)).filter(Files::isRegularFile).filter(file->file.getFileName().toString().endsWith(".txt")).forEach(
                file-> {
                    try {
                        toZip(file,zos);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        zos.close();
        System.out.println();
        Files.walk(Paths.get(pathOfFolder)).filter(Files::isRegularFile).forEach(file->{
            file.toFile().delete();
        });
        }
}
