package starter_files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    public static boolean encode;
    public static String filenameFreq;
    public static String filenameMsg;
    public static ArrayList<String> characters;
    public static ArrayList<Integer> frequencies;
    public static String message;

	public static void main(String[] args) {
        parseArgs(args);
        test();
	}

	public static void parseArgs(String[] args) {
        if (args.length != 3) {
            System.out.println("Exactly 3 args must be provided.");
            System.exit(1);
        }

        if(args[0].equals("-en")){
            encode = true;
        }
        else if(args[0].equals("-de")){
            encode = false;
        }
        else{
            System.out.println("Unknown option: " + args[0] + "\n");
            //System.exit(1);
        }
        filenameFreq = args[1];
        readFrequencies();
        filenameMsg = args[2];
        readMessage();
    }

    public static void readFrequencies(){
        // Populate class fields 'characters' and 'frequencies' with content of filenameFreq
    	BufferedReader reader;
    	characters = new ArrayList<String>();
    	frequencies = new ArrayList<Integer>();
		try {
			reader = new BufferedReader(new FileReader(filenameFreq));
			String line = reader.readLine();
			while (line != null) {
				String data[] = line.split(" ");
				characters.add(data[0]);
				frequencies.add(Integer.valueOf(data[1]));
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static void readMessage(){
        // Populate class field 'message' with content of file filenameMsg
    	try {
            message = new String(Files.readAllBytes(Paths.get(filenameMsg)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test(){
        HuffmanTree huffman = new HuffmanTree();
        huffman.constructHuffmanTree(characters,frequencies);
        if(encode){
            System.out.println("The encoded message is:");
            System.out.println(huffman.encode(message));
        }
        else{
            System.out.println("The decoded message is:");
            System.out.println(huffman.decode(message));
        }
    }



}
