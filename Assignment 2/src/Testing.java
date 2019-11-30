import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Testing {

    public static void main(String... args){

        File testsDir = new File("tests/");
        String[] strs = testsDir.list();
        for(String testDirStr:strs){
            File testDir = new File(testsDir.getAbsolutePath()+"/"+testDirStr);
            if(testDir.isDirectory()){


                File original = new File(testDir.getAbsolutePath()+"/original.txt");

                if(original.exists()) {
                    System.out.println("Running: "+ testDir);

                    File encoded = new File(testDir.getAbsolutePath()+"/encoded.txt");
                    File decoded = new File(testDir.getAbsolutePath()+"/decoded.txt");

                    encodeAndDecode(original, encoded, decoded);
                }


            }
        }
    }
    private static void checkTree(HuffmanTree tree){
        System.out.println(tree);
        System.out.println(tree.getEncodingToCharacter());
    }
    private static boolean checkCorrectness(String original, String decoded){
        return original.equals(decoded);
    }

    private static void encodeAndDecode(File originalFile, File encodedFile, File decodedFile){

        try {
            byte[] data = Files.readAllBytes(Paths.get(originalFile.getAbsolutePath()));

            String originalString = new String(data, StandardCharsets.UTF_8);

            ArrayList<String> characters = new ArrayList<>();
            ArrayList<Integer> freq = new ArrayList<>();

            createFrequencyLists(originalString,characters,freq);

            HuffmanTree tree = new HuffmanTree();
            tree.constructHuffmanTree(characters,freq);
            String encodedString = tree.encode(originalString);
            String decodedString = tree.decode(encodedString);


            PrintWriter pwEncoded = new PrintWriter(encodedFile);
            pwEncoded.println(encodedString);
            pwEncoded.close();

            PrintWriter pwDecoded = new PrintWriter(decodedFile);
            pwDecoded.println(decodedString);
            pwDecoded.close();


            System.out.println(checkCorrectness(originalString,decodedString));
            checkTree(tree);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    private static void createFrequencyLists(String originalString, ArrayList<String> characters, ArrayList<Integer> freq){
        Map<Character,Integer> map = new HashMap<>();
        for(char c: originalString.toCharArray()){
            map.putIfAbsent(c,0);
            map.put(c,map.get(c)+1);
        }
        for(char c: map.keySet()){
            characters.add(String.valueOf(c));
            freq.add(map.get(c));
        }
    }

}
