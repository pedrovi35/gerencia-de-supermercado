public class ProdutoLimpeza extends Produto {
    private static final long serialVersionUID = 1L;
    private String risco; // Ex: "Tóxico", "Inflamável", "Nenhum"

    public ProdutoLimpeza(String codigo, String nome, double preco, String risco) {
        super(codigo, nome, preco);
        this.risco = risco;
    }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }

    @Override
    public String getDescricaoDetalhada() {
        return "Risco: " + this.risco;
    }
    
    @Override
    public String getTipo() {
        return "Limpeza";
    }
}