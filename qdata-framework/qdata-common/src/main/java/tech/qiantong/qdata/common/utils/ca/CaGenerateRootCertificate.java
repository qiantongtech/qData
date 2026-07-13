/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

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
 * Generate a self-signed CA root certificate and save the private key as a PEM file
 * @author qdata
 */
public class CaGenerateRootCertificate {

    /**
     * Generate subject root certificate based on certificate information
     * @param dnNameStr certificate information
     * @return List<MultipartFile> The first one is the certificate, the second one is the private key
     */
    public static List<MultipartFile> generateRootCertificate(String dnNameStr) {
        List<MultipartFile> files = new ArrayList<>();
        try {
            // Generate RSA key pair
            KeyPair keyPair = SecureUtil.generateKeyPair("RSA", 2048);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Set the validity period of the certificate
            long currentTime = System.currentTimeMillis();
            Date startDate = new Date(currentTime);
            // Valid for 30 years
            Date endDate = new Date(currentTime + 365L * 30L * 24L * 60L * 60L * 1000L);

            // Set certificate information
            X500Principal dnName = new X500Principal(dnNameStr);
            // Use current time as sequence number
            BigInteger certSerialNumber = new BigInteger(Long.toString(currentTime));

            // Create an X.509 certificate object
            X509CertInfo certInfo = new X509CertInfo();
            certInfo.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
            certInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(certSerialNumber));
            certInfo.set(X509CertInfo.SUBJECT, new X500Name(dnName.getName()));
            certInfo.set(X509CertInfo.ISSUER, new X500Name(dnName.getName()));
            certInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(startDate, endDate));
            certInfo.set(X509CertInfo.KEY, new CertificateX509Key(publicKey));
            certInfo.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(AlgorithmId.get("SHA256withRSA")));

            // Create certificate
            X509CertImpl certificate = new X509CertImpl(certInfo);
            certificate.sign(privateKey, "SHA256withRSA");

            // Save the certificate as a.cer file
            String certFilePath = "rootCA.cer";
            ByteArrayOutputStream certOutputStream = new ByteArrayOutputStream();
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(certOutputStream, StandardCharsets.US_ASCII))) {
                writer.write("-----BEGIN CERTIFICATE-----\n");
                writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(certificate.getEncoded()));
                writer.write("\n-----END CERTIFICATE-----\n");
            }
            Files.write(Paths.get(certFilePath), certOutputStream.toByteArray());

            // Save private key as PEM file
            String privateKeyFilePath = "privateKey.pem";
            ByteArrayOutputStream pemOutputStream = new ByteArrayOutputStream();
            try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(pemOutputStream, StandardCharsets.US_ASCII))) {
                PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
                pemWriter.writeObject(pemObject);
            }

            // Convert the generated file to MultipartFile
            files.add(convertFileToMultipartFile(certFilePath, "rootCA.cer"));
            files.add(new MockMultipartFile(privateKeyFilePath, privateKeyFilePath, "application/x-pem-file", pemOutputStream.toByteArray()));

            // Delete original files
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
        // Test generated self-signed certificate
        String dnNameStr = "CN=YcgtRootCA, OU=IT, O=盐城市国有资产投资集团有限公司, L=Yancheng, ST=Yancheng, C=CN";
        List<MultipartFile> files = generateRootCertificate(dnNameStr);

        // Print the generated file name
        for (MultipartFile file : files) {
            System.out.println("生成的文件: " + file.getOriginalFilename());
        }
    }
}
