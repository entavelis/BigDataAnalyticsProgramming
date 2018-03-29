import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Reads a directory of documents and constructs shingle representations for these documents.
 * 
 * @author Toon Van Craenendonck
 *
 */
public class TwitterReader {

	Shingler shingler;
	int maxDocs;
	int curDoc;
	String filePath;
	Scanner scanner;

	public TwitterReader(int maxDocs, Shingler shingler, String filePath){
		this.shingler = shingler;
		this.maxDocs = maxDocs;
		this.curDoc = 0;
		this.filePath = filePath;

		try {
			this.scanner = new Scanner(new File(filePath));
			scanner.useDelimiter("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Set<Integer> next(){
		if (this.curDoc < this.maxDocs) {
			String line = scanner.next();
			String[] cols = line.split("\t", -1);
			this.curDoc++;
			return this.shingler.shingle(cols[2]);
		}else{
			return null;
		}
	};

	public void reset(){
		try {
			this.scanner = new Scanner(new File(filePath));
			scanner.useDelimiter("\n");
			System.gc();
			this.curDoc = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	};

	public boolean hasNext(){
		return this.curDoc < this.maxDocs;
	};


}
