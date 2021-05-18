/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufes.sisgestaoOS.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

/**
 *
 * @author felip
 */
public final class ManipuladorArquivo {
    
    private ManipuladorArquivo() {
    }
    
    public static ManipuladorArquivo getInstance() {
        return ManipuladorArquivoHolder.INSTANCE;
    }
    
    private static class ManipuladorArquivoHolder {

        private static final ManipuladorArquivo INSTANCE = new ManipuladorArquivo();
    }
    
    public String leitor(String path) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(path));
		StringBuilder sb = new StringBuilder();
		String linha = "";
		while (true) {
			if (null != linha) {				
				sb.append(linha).append("\n");

			} else
				break;
			linha = buffRead.readLine();
		}
		buffRead.close();
		//System.out.println( sb.toString() );
		return sb.toString();
	}

	public void escritor(String path, String entrada) throws IOException {
		PrintWriter writer = new PrintWriter(path);
		writer.print(entrada);
		writer.close();
		/*
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
	//	String linha = "";
	//	Scanner in = new Scanner(System.in);
	//	System.out.println("Escreva algo: ");
	//	linha = in.nextLine();
		//buffWrite.flush();
		buffWrite.append(entrada + "\n");
		buffWrite.close();
		*/
	}
	
	public void criaArquivo(String pasta, String id, String fileName, String fileString, String inf) {
		try {
			System.out.println(fileString);
		
			String[] pathnames;
			
			File path = new File("assets//"+ pasta + "//" + id);
			path.mkdirs();	
			
	        pathnames = path.list(); 
	        
	        if(pathnames.length > 0) {
	        	for (String pathname : pathnames) {
	            	new File("assets//"+ pasta + "//" + id+"//"+pathname).delete();
	            }
	        }
			
		byte[] bytes = Base64.getDecoder().decode(fileString);	
		
        File file = new File(path+ "//"+fileName);        
        FileOutputStream fop;
		
			fop = new FileOutputStream(file);
		
        fop.write(bytes);
        fop.flush();
        fop.close();
        
        File info = new File(path+ "//info.txt");  
        fop = new FileOutputStream(info);

        fop.write(inf.getBytes());
        fop.flush();
        fop.close();
		} catch (IOException e) {
			System.out.println("Error: ");
			e.printStackTrace();
		}
	}
	
	public File pegaArquivo(String fileName) {
				
        File file = new File("assets//"+fileName);
        
        return file;
		
	}
	
	@SuppressWarnings("resource")
	public Object[] pegaArquivo(String pasta, String id) throws IOException {
		
		String[] pathnames;
		
		File path = new File("assets//"+ pasta + "//" + id);
		path.mkdirs();	
		
        pathnames = path.list(); 
        File file = null;
        String info = null;
        String nome = null;
        Object[] obj = null;
        if (pathnames.length > 0) {
        	if(null != pathnames[0]) {
        		for (String pathname : pathnames) {
        			if (pathname.equals("info.txt")) {
        				info = new BufferedReader(new FileReader("assets//"+ pasta + "//" + id+"//"+pathname)).readLine();
        			}else {
        				file = new File("assets//"+ pasta + "//" + id+"//"+pathname);
        				nome = pathname;
        			}
        		}
        	}

        	obj = new Object[] {
        			nome,
        			convertFileToString(file),
        			info
        	};
        }        
        return obj;
		
	}
	
	public ArrayList<File> pegaArquivos(String pasta, String id) {
		String[] pathnames;
		
		ArrayList<File> arquivos = new ArrayList<>();
		
		File f = new File("assets//"+ pasta + "//" + id);
		
        pathnames = f.list();       
        
        for (String pathname : pathnames) {
        	arquivos.add(new File("assets//"+ pasta + "//" + id+"//"+pathname));
        }
        
        return arquivos;
		
	}
	
	public String convertFileToString(File file) throws IOException{		
		byte[] fileContent = FileUtils.readFileToByteArray(file);

		String encodedString = Base64.getEncoder().encodeToString(fileContent);

        return encodedString;
    }
}
