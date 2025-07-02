public class ProdutoAlimenticio extends Produto {
    private static final long serialVersionUID = 1L;
    private String dataValidade;

    public ProdutoAlimenticio(String codigo, String nome, double preco, String dataValidade) {
        super(codigo, nome, preco);
        this.dataValidade = dataValidade;
    }

    public String getDataValidade() { return dataValidade; }
    public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }

    @Override
    public String getDescricaoDetalhada() {
        return "Validade: " + this.dataValidade;
    }
    
    @Override
    public String getTipo() {
        return "Alimentício";
    }
}