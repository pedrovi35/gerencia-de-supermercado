import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GerenciadorEstoque {
    private List<Produto> produtos;
    private static final String NOME_ARQUIVO = "estoque.dat";

    public GerenciadorEstoque() {
        this.produtos = new ArrayList<>();
        carregarDados();
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        salvarDados();
    }

    public void alterarProduto(String codigo, Produto produtoAtualizado) {
        Optional<Produto> produtoExistente = buscarProdutoPorCodigo(codigo);
        produtoExistente.ifPresent(p -> {
            int index = produtos.indexOf(p);
            produtos.set(index, produtoAtualizado);
            salvarDados();
        });
    }

    public void excluirProduto(String codigo) {
        produtos.removeIf(p -> p.getCodigo().equals(codigo));
        salvarDados();
    }

    public Optional<Produto> buscarProdutoPorCodigo(String codigo) {
        return produtos.stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst();
    }

    public List<Produto> getListaProdutos() {
        return new ArrayList<>(produtos); // Retorna uma cópia para proteger a lista original
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO))) {
            produtos = (List<Produto>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de estoque não encontrado. Um novo será criado.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados do estoque: " + e.getMessage());
        }
    }

    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados do estoque: " + e.getMessage());
        }
    }
}