public class Bebida extends Produto {
    private static final long serialVersionUID = 1L;
    private double volumeMl;
    private boolean alcoolica;

    public Bebida(String codigo, String nome, double preco, double volumeMl, boolean alcoolica) {
        super(codigo, nome, preco);
        this.volumeMl = volumeMl;
        this.alcoolica = alcoolica;
    }
    
    public double getVolumeMl() { return volumeMl; }
    public void setVolumeMl(double volumeMl) { this.volumeMl = volumeMl; }
    public boolean isAlcoolica() { return alcoolica; }
    public void setAlcoolica(boolean alcoolica) { this.alcoolica = alcoolica; }

    @Override
    public String getDescricaoDetalhada() {
        return String.format("%.0fml, %s", this.volumeMl, this.alcoolica ? "Alcoólica" : "Não Alcoólica");
    }
    
    @Override
    public String getTipo() {
        return "Bebida";
    }
}