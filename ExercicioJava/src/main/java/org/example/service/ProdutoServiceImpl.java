package org.example.service;

import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import org.example.repository.ProdutoRepositoryImpl;
import org.example.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProdutoServiceImpl implements ProdutoService{

    private final ProdutoRepository repository;

    public ProdutoServiceImpl() {
        this.repository = new ProdutoRepositoryImpl();
    }

    @Override
    public Produto cadastrarProduto(Produto produto) throws SQLException {
        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("Preço deve ser positivo.");
        }

        return repository.save(produto);
    }

    @Override
    public List<Produto> listarProdutos() throws SQLException {
        return repository.findAll();
    }

    @Override
    public Produto buscarPorId(int id) throws SQLException {
        return repository.findById(id);
    }

    @Override
    public Produto atualizarProduto(Produto produto, int id) throws SQLException {
        produto.setId(id);

        return repository.update(produto);
    }

    @Override
    public boolean excluirProduto(int id) throws SQLException {
        final String query = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            int linhasAfetadas = ps.executeUpdate();

            return linhasAfetadas > 0;
        }
    }

}