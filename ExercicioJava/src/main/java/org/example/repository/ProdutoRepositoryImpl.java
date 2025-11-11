package org.example.repository;

import org.example.model.Produto;
import org.example.util.ConexaoBanco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositoryImpl implements ProdutoRepository {

    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
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
        final String query = "INSERT INTO produto (nome, preco, quantidade, categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidade());
            ps.setString(4, produto.getCategoria());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
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
        final String query = "SELECT id, nome, preco, quantidade, categoria FROM produto";

        try (Connection conn = ConexaoBanco.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                produtos.add(mapResultSetToProduto(rs));
            }
        }

        return produtos;
    }

    @Override
    public Produto findById(int id) throws SQLException {
        final String query = "SELECT id, nome, preco, quantidade, categoria FROM produto WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduto(rs);
                }
            }
        }

        return null;
    }

    @Override
    public Produto update(Produto produto) throws SQLException {
        final String query = "UPDATE produto SET nome = ?, preco = ?, quantidade = ?, categoria = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidade());
            ps.setString(4, produto.getCategoria());
            ps.setInt(5, produto.getId());

            int linhasAfetadas = ps.executeUpdate();

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
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}