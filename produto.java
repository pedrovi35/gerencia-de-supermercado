import java.io.Serializable;

// Serializable é necessário para salvar o objeto em arquivo.
public abstract class Produto implements Serializable {
    private static final long serialVersionUID = 1L; // Controle de versão da serialização

    private String codigo;
    private String nome;
    private double preco;

    public Produto(String codigo, String nome, double preco) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
    }

    // Getters e Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    // **MÉTODO POLIMÓRFICO**
    // Cada subclasse deverá fornecer sua própria implementação.
    public abstract String getDescricaoDetalhada();
    
    // Método para identificar o tipo do produto na GUI
    public abstract String getTipo();
}