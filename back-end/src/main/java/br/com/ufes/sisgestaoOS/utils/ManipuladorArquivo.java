/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufes.sisgestaoOS.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

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
}
