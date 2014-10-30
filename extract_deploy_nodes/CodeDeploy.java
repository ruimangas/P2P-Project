import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
 
public class CodeDeploy {
 
	public static void main(String[] args) throws IOException{
 
		String file = args[0];
		String pasta = args[1];
		String nodes = "nodes.txt";
		BufferedReader br = new BufferedReader(new FileReader(nodes));
		String host;
		
		while ((host = br.readLine()) != null) {
   			String command = "scp -r " + file + " istple_seprs4@" + host + ":" + pasta;
			System.out.println(command);

			CodeDeploy obj = new CodeDeploy(); 
			
			String output = obj.executeCommand(command);
			System.out.println(output);
   		}
		br.close();
	}
 
	private String executeCommand(String command) {
 
		StringBuffer output = new StringBuffer();
 
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));
 
                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return output.toString();
 
	}
 
}