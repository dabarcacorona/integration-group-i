package cl.corona.integrationgroupi.service;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class intUpload {
    @Value("${sftpd.ip}")
    private String d_sftpip;

    @Value("${sftpd.prt}")
    private int d_sftpprt;

    @Value("${sftpd.usr}")
    private String d_sftpusr;

    @Value("${sftpd.pss}")
    private String d_sftppss;

    @Value("${sftpd.org}")
    private String d_sftporg;

    @Value("${sftpd.dst}")
    private String d_sftpdtn;

    @Value("${name.file}")
    private String d_namefile;

    @Value("${separador.carpetas}")
    private String separador;

    @Value("${largo.archivo}")
    private int largo_archivo;

    private static final Logger LOG = LoggerFactory.getLogger(intUpload.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    String strDir = System.getProperty("user.dir");

    public void UploadFile() throws IOException {

        FTPClient client = new FTPClient();

        try {

            client.connect(d_sftpip, d_sftpprt);
            client.login(d_sftpusr, d_sftppss);

            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            final String path = strDir + separador + d_sftporg;
            //final String path = sftporg;

            File directory = new File(path);
            File[] fList = directory.listFiles();

            for (File file : fList) {
                if (file.isFile()) {
                    String filename = file.getAbsolutePath();
                    LOG.info("Uploading PMM Reportes " + filename + " ---> " + d_sftpdtn);
                    InputStream in = new FileInputStream(file);
                    client.storeFile(file.getName(), in);
                    in.close();
                    file.delete();
                    LOG.info("{} : Upload Ok", dateTimeFormatter.format(LocalDateTime.now()));
                }
            }

            client.logout();
            client.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
