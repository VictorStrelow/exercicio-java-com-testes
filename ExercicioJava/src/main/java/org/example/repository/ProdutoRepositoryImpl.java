package org.example.repository;

import org.example.model.Produto;
import org.example.util.ConexaoBanco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositoryImpl implements ProdutoRepository{

    private Produto listaProduto(ResultSet rs) throws SQLException {
        Produto p = new Produto(
                rs.getString("nome"),
                rs.getDouble("preco"),
                rs.getInt("quantidade"),
                rs.getString("categoria")
        );

        p.setId(rs.getInt("id"));

        return p;
    }

    @Override
    public Produto save(Produto produto) throws SQLException {
        final String query = "INSERT INTO produto (nome, preco, quantidade, categoria) VALUE (?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conectar();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setString(4, produto.getCategoria());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                    return produto;
                }
            }
        }

        return null;
    }

    @Override
    public List<Produto> findAll() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
         final String query = "SELECT * FROM produto";

        try (Connection conn = ConexaoBanco.conectar();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(listaProduto(rs));
            }
        }

        return produtos;
    }

    @Override
    public Produto findById(int id) throws SQLException {
        final String query = "SELECT * FROM produto WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return listaProduto(rs);
                }

            }
        }

        return null;
    }

    @Override
    public Produto update(Produto produto) throws SQLException {
        final String query = "UPDATE produto SET nome = ?, preco = ?, quantidade = ?, categoria = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setString(4, produto.getCategoria());
            stmt.setInt(5, produto.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                return produto;
            }
        }

        return null;
    }

    @Override
    public void deleteById(int id) throws SQLException {
         final String query = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}