package teatro_implacavel;

public class Area {
    private final boolean[] poltronas;
    private final double preco;

    public Area(int totalPoltronas, double preco) {
        this.poltronas = new boolean[totalPoltronas];
        this.preco = preco;
    }

    public int getTotalPoltronas() {
        return poltronas.length;
    }

    public double getPreco() {
        return preco;
    }

    public boolean isPoltronaOcupada(int index) {
        return poltronas[index];
    }

    public void comprarPoltrona(int index) {
        poltronas[index] = true;
    }

    public void cancelarPoltrona(int index) {
        poltronas[index] = false;
    }

    public int getTotalIngressosVendidos() {
        int total = 0;
        for (boolean poltrona : poltronas) {
            if (poltrona) {
                total++;
            }
        }
        return total;
    }

    public double getReceita() {
        return getTotalIngressosVendidos() * preco;
    }
}