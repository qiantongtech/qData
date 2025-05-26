package tech.qiantong.qdata.common.utils.ca;

import cn.hutool.crypto.SecureUtil;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sun.security.x509.*;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 生成自签名CA根证书并保存私钥为PEM文件
 * @author qdata
 */
public class CaGenerateRootCertificate {

    /**
     * 根据证书信息生成主体根证书
     * @param dnNameStr 证书信息
     * @return List<MultipartFile> 第一个是证书，第二个是私钥
     */
    public static List<MultipartFile> generateRootCertificate(String dnNameStr) {
        List<MultipartFile> files = new ArrayList<>();
        try {
            // 生成RSA密钥对
            KeyPair keyPair = SecureUtil.generateKeyPair("RSA", 2048);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 设定证书的有效期
            long currentTime = System.currentTimeMillis();
            Date startDate = new Date(currentTime);
            // 有效期30年
            Date endDate = new Date(currentTime + 365L * 30L * 24L * 60L * 60L * 1000L);

            // 设定证书信息
            X500Principal dnName = new X500Principal(dnNameStr);
            // 使用当前时间作为序列号
            BigInteger certSerialNumber = new BigInteger(Long.toString(currentTime));

            // 创建 X.509 证书对象
            X509CertInfo certInfo = new X509CertInfo();
            certInfo.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
            certInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(certSerialNumber));
            certInfo.set(X509CertInfo.SUBJECT, new X500Name(dnName.getName()));
            certInfo.set(X509CertInfo.ISSUER, new X500Name(dnName.getName()));
            certInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(startDate, endDate));
            certInfo.set(X509CertInfo.KEY, new CertificateX509Key(publicKey));
            certInfo.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(AlgorithmId.get("SHA256withRSA")));

            // 创建证书
            X509CertImpl certificate = new X509CertImpl(certInfo);
            certificate.sign(privateKey, "SHA256withRSA");

            // 保存证书为 .cer 文件
            String certFilePath = "rootCA.cer";
            ByteArrayOutputStream certOutputStream = new ByteArrayOutputStream();
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(certOutputStream, StandardCharsets.US_ASCII))) {
                writer.write("-----BEGIN CERTIFICATE-----\n");
                writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(certificate.getEncoded()));
                writer.write("\n-----END CERTIFICATE-----\n");
            }
            Files.write(Paths.get(certFilePath), certOutputStream.toByteArray());

            // 保存私钥为 PEM 文件
            String privateKeyFilePath = "privateKey.pem";
            ByteArrayOutputStream pemOutputStream = new ByteArrayOutputStream();
            try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(pemOutputStream, StandardCharsets.US_ASCII))) {
                PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
                pemWriter.writeObject(pemObject);
            }

            // 将生成的文件转换为 MultipartFile
            files.add(convertFileToMultipartFile(certFilePath, "rootCA.cer"));
            files.add(new MockMultipartFile(privateKeyFilePath, privateKeyFilePath, "application/x-pem-file", pemOutputStream.toByteArray()));

            // 删除原始文件
            deleteFile(certFilePath);
            deleteFile(privateKeyFilePath);

            System.out.println("根证书和私钥已生成、转换为 MultipartFile 对象并删除原始文件");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

    private static MultipartFile convertFileToMultipartFile(String filePath, String fileName) throws Exception {
        File file = new File(filePath);
        try (FileInputStream input = new FileInputStream(file)) {
            return new MockMultipartFile(fileName, fileName, "application/x-x509-ca-cert", input);
        }
    }

    private static void deleteFile(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
            System.out.println("文件已删除: " + filePath);
        } catch (Exception e) {
            System.out.println("文件删除失败: " + filePath);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 测试生成自签名证书
        String dnNameStr = "CN=YcgtRootCA, OU=IT, O=盐城市国有资产投资集团有限公司, L=Yancheng, ST=Yancheng, C=CN";
        List<MultipartFile> files = generateRootCertificate(dnNameStr);

        // 打印生成的文件名称
        for (MultipartFile file : files) {
            System.out.println("生成的文件: " + file.getOriginalFilename());
        }
    }
}
