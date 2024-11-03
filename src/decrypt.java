import java.io.*;
import java.nio.file.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class decrypt {

    private static final String AES_CIPHER = "AES/CBC/PKCS5Padding";
    static ArrayList<String> pathData;static ArrayList<String> cryptData;
    static Path decryptedPath;
    static String decryptedFilePath;
    public static void decryptFile(String keyBase64, String encryptedFilePath) throws Exception {
        // Decode the base64 key
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // Read the encrypted file
        Path encryptedPath = Paths.get(encryptedFilePath);
        byte[] encryptedData = Files.readAllBytes(encryptedPath);

        // Extract the IV from the beginning of the encrypted data
        byte[] iv = new byte[16];
        System.arraycopy(encryptedData, 0, iv, 0, 16);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Initialize the AES cipher
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // Decrypt the data
        byte[] decryptedData = cipher.doFinal(encryptedData, 16, encryptedData.length - 16);

        // Write the decrypted data to a new file
        int length = encryptedFilePath.length();
        decryptedFilePath = encryptedFilePath.toString().substring(0,length-5);
        decryptedPath = Paths.get(decryptedFilePath);
        Files.write(decryptedPath, decryptedData);

        System.out.println("File decrypted successfully: " + decryptedFilePath);

        // Delete the encrypted file
        Files.delete(encryptedPath);
        System.out.println("Encrypted file deleted: " + encryptedFilePath);
    }

    public static void main(String key, String filePath, ArrayList pathList, ArrayList cryptList) throws Exception {
        pathData = pathList;
        cryptData = cryptList;
        String keyBase64 = key;
        String encryptedFilePath = filePath;
        try {
            decryptFile(keyBase64, encryptedFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred during decryption and deletion process.");
        }
        fileDeletion(key,filePath);
    }
    public static void fileDeletion(String keyB64, String filePath) throws Exception {
            if(pathData.contains(filePath)&&cryptData.contains(keyB64)){
                int idx1 = pathData.indexOf(filePath);
                int idx2 = cryptData.indexOf(keyB64);
                pathData.remove(idx1);
                cryptData.remove(idx2);
            }
            String basePath = deviceInfo.basePath+"\\encyphrlogs.encp";
            File file = new File(basePath);
            FileWriter fw = new FileWriter(basePath);
            file.delete();
            file.createNewFile();
            System.out.println("encyphrlogs.encp created after successful deletion of .encp file");
            for(int i=0;i<pathData.size();i++){
                fw.write(pathData.get(i)+"\n");
                fw.write(cryptData.get(i)+"\n");
            }
            app appInstance = new app();
            appInstance.jListManager();
        }
    }