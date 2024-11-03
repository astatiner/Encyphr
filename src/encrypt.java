import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
public class encrypt {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";
    private static final int BUFFER_SIZE = 1024 * 1024; // 1MB buffer for reading/writing files

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128);
        return keyGen.generateKey();
    }

    public static void encryptFile(String inputFile, String outputFile, SecretKey key) throws Exception {
        // Generate a new IV
        byte[] iv = new byte[16]; // AES block size is 16 bytes
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile);
             BufferedInputStream bis = new BufferedInputStream(fis);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            // Write the IV to the output file first
            fos.write(iv);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    bos.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                bos.write(outputBytes);
            }
        }
    }

    public static void main(String inputFile, String encryptedFile) {
        try {
            // Generate a key
            SecretKey secretKey = generateKey();

            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Encryption Key (Base64 encoded): " + encodedKey);

            encryptFile(inputFile, encryptedFile, secretKey);
            System.out.println("File encrypted successfully.");
            String sysName = System.getProperty("user.name");
            File f = new File(inputFile);
            String basePath = deviceInfo.basePath;
            FileWriter fw = new FileWriter(basePath+"\\encyphrlogs.encp",true);
            fw.write(inputFile+"\n");
            fw.write(encodedKey+"\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        app appInstance = new app();
        try {
            appInstance.jListManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}