package teatro_implacavel;

public class Ingresso {
    private final String cpf;
    private final String peca;
    private final String sessao;
    private final String area;
    private final int poltrona;
    private final double preco;

    public Ingresso(String cpf, String peca, String sessao, String area, int poltrona, double preco) {
        this.cpf = cpf;
        this.peca = peca;
        this.sessao = sessao;
        this.area = area;
        this.poltrona = poltrona;
        this.preco = preco;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPeca() {
        return peca;
    }

    public String getSessao() {
        return sessao;
    }

    public String getArea() {
        return area;
    }

    public int getPoltrona() {
        return poltrona;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return peca + " - " + sessao + " - " + area + " - Poltrona: " + poltrona + " - Preço: " + preco;
    }
}
