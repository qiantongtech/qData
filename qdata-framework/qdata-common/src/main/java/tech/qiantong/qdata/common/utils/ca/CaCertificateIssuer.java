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

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.security.x509.*;
import tech.qiantong.qdata.common.utils.MessageUtils;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Certificate issuance tool class provides methods to use root certificates and private keys to issue user certificates.
 * This tool class generates user certificates and returns the MultipartFile list by loading the root certificate and private key.
 * @author qdata
 */
public class CaCertificateIssuer {

    /**
     * Issues a user certificate and returns a MultipartFile list of certificates and private keys
     *
     * @param userName contains the user’s details
     * @param certUrl URL of the root certificate
     * @param privateKeyUrl URL of the private key
     * @param validity The validity period of the certificate (years)
     * @return List<MultipartFile> List of MultipartFiles containing user certificates and private keys
     * @throws Exception if an error occurs during the issuance process
     */
    public static List<MultipartFile> issueCertificate(String userName, String certUrl,
                                                       String privateKeyUrl, Long validity) throws Exception {
        X500Principal userDnName = new X500Principal(userName);
        List<MultipartFile> fileList = new ArrayList<>();

        // Load root certificate and private key
        X509Certificate rootCertificate = loadRootCertificate(certUrl);
        PrivateKey rootPrivateKey = loadRootPrivateKey(privateKeyUrl);

        // Obtain the subject information of the root certificate as the issuer information of the user certificate
        X500Principal rootDnName = rootCertificate.getSubjectX500Principal();
        X500Name rootX500Name = new X500Name(rootDnName.getName());

        // Generate the user's key pair (public and private keys)
        KeyPair userKeyPair = generateUserKeyPair();
        PublicKey userPublicKey = userKeyPair.getPublic();
        PrivateKey userPrivateKey = userKeyPair.getPrivate();

        // Define subject information for user certificates
        X500Name userX500Name = new X500Name(userDnName.getName());

        // Create an X.509 user certificate information object
        X509CertInfo userCertInfo = new X509CertInfo();
        userCertInfo.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        userCertInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(BigInteger.valueOf(System.currentTimeMillis())));
        userCertInfo.set(X509CertInfo.SUBJECT, userX500Name);
        userCertInfo.set(X509CertInfo.ISSUER, rootX500Name);
        userCertInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(new Date(), new Date(System.currentTimeMillis() + validity * 365L * 24L * 60L * 60L * 1000L)));
        userCertInfo.set(X509CertInfo.KEY, new CertificateX509Key(userPublicKey));
        userCertInfo.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(AlgorithmId.get("SHA256withRSA")));

        // Added subject extension fields (SAN) for browser https authentication
        String dnsName = ReUtil.get("CN=([^,]+)", userX500Name.getName(), 1);
        // Determine whether it is an IP address or domain name
        boolean isIpAddress = Validator.isIpv4(dnsName);
        // Determine whether it is a domain name
        boolean isDomain = ReUtil.isMatch("^(\\*\\.)?([\\w-]+\\.)+[a-zA-Z]{2,}$", dnsName);

        CertificateExtensions extensions = new CertificateExtensions();
        GeneralNames san = new GeneralNames();

        if (isIpAddress) {
            // If it is an IP address, add it to the SAN using the IPAddress type
            san.add(new GeneralName(new IPAddressName(dnsName)));
        } else if (isDomain) {
            // If it is a domain name, use the DNSName type to add it to the SAN
            san.add(new GeneralName(new DNSName(dnsName)));
        }

        if (isIpAddress || isDomain) {
            extensions.set(SubjectAlternativeNameExtension.NAME, new SubjectAlternativeNameExtension(san));
            // Add extension to certificate information
            userCertInfo.set(X509CertInfo.EXTENSIONS, extensions);
        }

        // Sign the user certificate using the root certificate's private key
        X509CertImpl userCertificate = new X509CertImpl(userCertInfo);
        userCertificate.sign(rootPrivateKey, "SHA256withRSA");

        // Convert user certificate to MultipartFile
        fileList.add(convertCertificateToMultipartFile(userCertificate, dnsName + "_certificate.cer"));

        // Save user private key as PEM file and convert to MultipartFile
        fileList.add(convertPrivateKeyToMultipartFile(userPrivateKey, dnsName + "_privateKey.pem"));

        return fileList;
    }

    /**
     * Generate RSA key pair
     *
     * @return generated RSA key pair
     * @throws Exception if key pair generation fails
     */
    private static KeyPair generateUserKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Convert X.509 certificate object to MultipartFile
     *
     * @param certificate X.509 certificate object
     * @param fileName file name
     * @return Certificate in the form of MultipartFile
     * @throws Exception if an error occurs during conversion
     */
    private static MultipartFile convertCertificateToMultipartFile(X509CertImpl certificate, String fileName) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.US_ASCII))) {
            writer.write("-----BEGIN CERTIFICATE-----\n");
            writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(certificate.getEncoded()));
            writer.write("\n-----END CERTIFICATE-----\n");
        }
        return new MockMultipartFile(fileName, fileName, "application/x-x509-ca-cert", outputStream.toByteArray());
    }

    /**
     * Convert private key object to MultipartFile
     *
     * @param privateKey private key object
     * @param fileName file name
     * @return Private key in the form of MultipartFile
     * @throws Exception if an error occurs during conversion
     */
    private static MultipartFile convertPrivateKeyToMultipartFile(PrivateKey privateKey, String fileName) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream, StandardCharsets.US_ASCII))) {
            PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
            pemWriter.writeObject(pemObject);
        }
        return new MockMultipartFile(fileName, fileName, "application/x-pem-file", outputStream.toByteArray());
    }

    /**
     * Loads the root certificate from the specified URL
     *
     * @param certUrl URL of the root certificate
     * @return the loaded X509Certificate object
     * @throws Exception if an error occurs during loading
     */
    public static X509Certificate loadRootCertificate(String certUrl) throws Exception {
        try (InputStream certStream = new URL(getServerIpAndPort() + certUrl).openStream()) {
            if (certStream == null) {
                throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                        "sys.error.ca.root.certificate.notfound", "Root certificate file was not found"));
            }
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certFactory.generateCertificate(certStream);
        }
    }

    /**
     * Load private key from specified URL
     *
     * @param privateKeyUrl URL of the private key
     * @return loaded PrivateKey object
     * @throws Exception if an error occurs during loading
     */
    public static PrivateKey loadRootPrivateKey(String privateKeyUrl) throws Exception {
        try (InputStream keyStream = new URL(getServerIpAndPort() + privateKeyUrl).openStream()) {
            if (keyStream == null) {
                throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                        "sys.error.ca.private.key.notfound", "Private key file was not found"));
            }
            PemReader pemReader = new PemReader(new InputStreamReader(keyStream));
            byte[] keyBytes = pemReader.readPemObject().getContent();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }


    /**
     * Get the IP and port of the current backend server
     *
     * @return the IP and port of the server, in the format "IP:port"
     */
    public static String getServerIpAndPort() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Get the server's IP and port
        int serverPort = request.getLocalPort();

        return "http://127.0.0.1" + ":" + serverPort;
    }


    public static void main(String[] args) throws Exception {
        // Define user information
        String userName = "CN=::www.wangming.xyz, OU=IT, O=盐城市国有资产投资集团有限公司, L=Yancheng, ST=Yancheng, C=CN";

        // URL that defines the root certificate and private key
        String certUrl = "http://127.0.0.1:8000/local-plus/66c1f165146fbf2cdaf53f55.cer";
        String privateKeyUrl = "http://127.0.0.1:8000/local-plus/66c1f166146fbf2cdaf53f56.pem";

        // Issue a certificate and get a list of MultipartFiles
        List<MultipartFile> files = issueCertificate(userName, certUrl, privateKeyUrl, 1L);

        // Print the generated file name
        for (MultipartFile file : files) {
            System.out.println("Generated file: " + file.getOriginalFilename());
        }
    }
}
