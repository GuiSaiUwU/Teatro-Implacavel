package teatro_implacavel;

import java.util.LinkedHashMap;
import java.util.Map;

public class Peca {
    private final String nome;
    private final Map<String, Sessao> sessoes;

    public Peca(String nome) {
        this.nome = nome;
        this.sessoes = new LinkedHashMap<>();
        sessoes.put("Manhã", new Sessao("Manhã"));
        sessoes.put("Tarde", new Sessao("Tarde"));
        sessoes.put("Noite", new Sessao("Noite"));
    }

    public String getNome() {
        return nome;
    }

    public Map<String, Sessao> getSessoes() {
        return sessoes;
    }

    public int getTotalIngressosVendidos() {
        return sessoes.values().stream().mapToInt(Sessao::getTotalIngressosVendidos).sum();
    }

    public double getReceitaTotal() {
        return sessoes.values().stream().mapToDouble(Sessao::getReceita).sum();
    }

}
