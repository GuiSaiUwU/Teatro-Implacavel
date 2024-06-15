package teatro_implacavel;

import java.util.LinkedHashMap;
import java.util.Map;

public class Sessao {
    protected final Map<String, Area> areas;
    public String nome;
    public Sessao(String nome) {
        this.nome = nome;
        this.areas = new LinkedHashMap<>();
        areas.put("Plateia A", new Area(25, 40.00));
        areas.put("Plateia B", new Area(100, 60.00));
        for (int i = 1; i <= 5; i++) {
            areas.put("Camarote " + i, new Area(10, 80.00));
        }
        for (int i = 1; i <= 6; i++) {
            areas.put("Frisa " + i, new Area(5, 120.00));
        }
        areas.put("Balcão Nobre", new Area(50, 250.00));
    }

    public Map<String, Area> getAreas() {
        return areas;
    }

    public int getTotalIngressosVendidos() {
        return areas.values().stream().mapToInt(Area::getTotalIngressosVendidos).sum();
    }

    public double getReceita() {
        return areas.values().stream().mapToDouble(Area::getReceita).sum();
    }
}
