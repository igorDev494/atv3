/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet rs;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto (ProdutosDTO produto){
        
       String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
        
       conn = new conectaDAO().connectDB();
       
       try {
        prep = conn.prepareStatement(sql);
        
        // 3. Passa os dados do DTO para as interrogações (?) do SQL
        prep.setString(1, produto.getNome());
        prep.setInt(2, produto.getValor());
        prep.setString(3, produto.getStatus());
        
        // 4. Linha que grava os dados no MySQL
        prep.execute(); 
        
        // 5. Fecha o prepareStatement
        prep.close();
        
    } catch (Exception erro) {
        System.out.println("Erro ao cadastrar produto no DAO: " + erro.getMessage());
    }
}
       
        
        
    
    
    public ArrayList<ProdutosDTO> listarProdutos(){        
        String sql = "SELECT * FROM produtos";
        
         conn = new conectaDAO().connectDB();
        
        try {
            prep = conn.prepareStatement(sql);
            rs = prep.executeQuery(); // executeQuery roda o SELECT no banco de dados
            
            // 3. O Java lê linha por linha do MySQL e joga na lista do NetBeans
            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                
                // ATENÇÃO: O texto entre aspas deve ser IGUAL aos nomes das colunas no MySQL
                produto.setId(rs.getInt("id")); 
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));
                
                listagem.add(produto); // Adiciona o produto na lista que vai pra JTable
            }
            
        } catch (Exception erro) {
            System.out.println("Erro ao rodar o SELECT no ProdutosDAO: " + erro.getMessage());
        } finally {
            // Fecha os recursos do banco por segurança
            try { if (rs != null) rs.close(); if (prep != null) prep.close(); } catch (Exception e) {}
        }
        
        return listagem;
    }
     public boolean venderProduto(int id) {
        
        String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
        
        try {
            
            conn = new conectaDAO().connectDB(); 
            prep = conn.prepareStatement(sql);
            
            
            prep.setInt(1, id);
            
           
            int linhasAfetadas = prep.executeUpdate();
            
            
            return linhasAfetadas > 0;
            
        } catch (Exception e) {
            System.out.println("Erro ao vender produto: " + e.getMessage());
            return false;
            
        } finally {
            
            try {
                if (prep != null) prep.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexões: " + e.getMessage());
            }
        }
     }
    
        


      public List<ProdutosDTO> listarProdutosVendidos() {
    String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
    
    List<ProdutosDTO> lista = new ArrayList<>();
    PreparedStatement prep = null;
    ResultSet resultset = null;
    
    try {
        // INICIALIZA A CONEXÃO PRIMEIRO (Evita o erro de "this.conn is null")
        conn = new conectaDAO().connectDB();
        
        // AGORA PREPARA O SQL USANDO A CONEXÃO VÁLIDA
        prep = conn.prepareStatement(sql);
        resultset = prep.executeQuery();
        
        while (resultset.next()) {
            ProdutosDTO produto = new ProdutosDTO();
            
            
            produto.setNome(resultset.getString("nome"));
           
            produto.setStatus(resultset.getString("status"));
            
            lista.add(produto);
        }
        
    } catch (Exception e) {
        System.out.println("Erro ao listar produtos vendidos: " + e.getMessage());
    } finally {
        try {
            if (resultset != null) resultset.close();
            if (prep != null) prep.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
    
    return lista;
}
}
