package xyz.dongxiaoxia.commons.utils.io;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sun.misc.BASE64Decoder;

public class Uploader {
    private String[] allowFiles;
    private int maxSize;
    private String uploadDirPath;

    private static final String[] defaultAllowFiles = new String[]{".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp"};
    private static final int defaultMaxSize = 10000;

    public Uploader(String uploadDirPath) {
        this(uploadDirPath, defaultMaxSize, defaultAllowFiles);
    }

    public Uploader(String uploadDirPath, int maxSize) {
        this(uploadDirPath, maxSize, defaultAllowFiles);
    }

    public Uploader(String uploadDirPath, String[] allowFiles) {
        this(uploadDirPath, defaultMaxSize, allowFiles);
    }

    public Uploader(String uploadDirPath, int maxSize, String[] allowFiles) {
        this.allowFiles = allowFiles;
        this.maxSize = maxSize;
        this.uploadDirPath = uploadDirPath;
    }

    public void upload(File file) throws Exception {
        InputStream ins = null;
        FileOutputStream os = null;
        try {
            ins = new FileInputStream(file);
            int fileSize = ins.available();
            if (fileSize > this.maxSize * 1024) {
                throw new Exception("最大支持" + this.maxSize + "(KB)");
            }
            String originalName = file.getName();
            if (this.checkFileType(originalName)) {
                String fileName = this.getName(originalName);
                String dirPath = getFolder(uploadDirPath);
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                os = new FileOutputStream(new File(dirPath, fileName));
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            } else {
                throw new Exception("不允许的文件格式");
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void upload(File[] files) throws Exception {
        for (File file : files) {
            upload(file);
        }
    }

    public void upload(HttpServletRequest request) throws Exception {
        // 判断enctype属性是否为multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            throw new Exception("未包含文件上传域");
        } else {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            String dirPath = getFolder(uploadDirPath);
            diskFileItemFactory.setRepository(new File(dirPath));
            try {
                ServletFileUpload sfu = new ServletFileUpload(diskFileItemFactory);
                sfu.setSizeMax((long) (this.maxSize * 1024));
                sfu.setHeaderEncoding("utf-8");
                List<FileItem> fileItems = sfu.parseRequest(request);
                for (FileItem fileItem : fileItems) {
                    if (!fileItem.isFormField()) {
                        String originalName = fileItem.getFieldName();
                        if (this.checkFileType(originalName)) {
                            File uploadedFile = new File(dirPath, this.getName(originalName));
                            fileItem.write(uploadedFile);
                        } else {
                            throw new Exception("不允许的文件格式");
                        }
                    }
                }
            } catch (SizeLimitExceededException var11) {
                throw new Exception("最大支持" + this.maxSize + "(KB)");
            } catch (FileUploadException var13) {
                throw new Exception("上传请求异常");
            }

        }
    }

    public static void uploadBase64(String base64Data, String filePath) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream ro = null;
        try {
            ro = new FileOutputStream(filePath);
            byte[] data = decoder.decodeBuffer(base64Data);
            for (int i = 0; i < data.length; ++i) {
                if (data[i] < 0) {
                    data[i] = (byte) (data[i] + 256);
                }
            }
            ro.write(data);
            ro.flush();
        } finally {
            if (ro != null) {
                try {
                    ro.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private boolean checkFileType(String fileName) {
        for (String ext : Arrays.asList(this.allowFiles)) {
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private String getName(String fileName) {
        return "" + new Random().nextInt(10000) + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
    }

    private String getFolder(String path) {
        path = path + File.separator + new SimpleDateFormat("yyyyMMdd").format(new Date());
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return path;
    }

    public static void main(String[] args) throws Exception {
        Uploader uploader = new Uploader("D:/test", new String[]{"abc"});
        List<File> files = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            File file = new File("D:/" + new Random().nextInt(10000) + ".abc");
            file.createNewFile();
            files.add(file);
        }
        uploader.upload(files.toArray(new File[files.size()]));
    }
}
