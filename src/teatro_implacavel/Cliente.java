package teatro_implacavel;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private final String cpf;
    private final List<Ingresso> ingressos;

    public Cliente(String cpf) {
        this.cpf = cpf;
        this.ingressos = new ArrayList<>();
    }

    public String getCpf() {
        return cpf;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public void adicionarIngresso(Ingresso ingresso) {
        ingressos.add(ingresso);
    }

    public void removerIngresso(Ingresso ingresso) {
        ingressos.remove(ingresso);
    }
}
