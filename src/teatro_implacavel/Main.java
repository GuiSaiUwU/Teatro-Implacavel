package teatro_implacavel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static java.awt.Color.*;

public class Main extends JFrame {
    private final Map<String, Cliente> clientes;
    private final Map<String, Peca> pecas;
    private final JComboBox<String> pecaComboBox;
    private final JComboBox<String> sessaoComboBox;
    private final JComboBox<String> areaComboBox;
    private final JPanel poltronasPanel;

    public Main() {
        clientes = new LinkedHashMap<>();
        pecas = new LinkedHashMap<>();

        pecas.put("Peça 1", new Peca("Peça 1"));
        pecas.put("Peça 2", new Peca("Peça 2"));
        pecas.put("Peça 3", new Peca("Peça 3"));

        setTitle("Teatro ABC - Sistema de Vendas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Tela cheia

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 1));

        JButton cancelarButton = new JButton("Cancelar Ingresso");
        cancelarButton.addActionListener(new CancelarIngressoListener());
        controlPanel.add(cancelarButton);

        JButton imprimirButton = new JButton("Checar Ingresso");
        imprimirButton.addActionListener(new ImprimirIngressoListener());
        controlPanel.add(imprimirButton);

        JButton estatisticaButton = new JButton("Estatística de Vendas");
        estatisticaButton.addActionListener(new EstatisticaVendasListener());
        controlPanel.add(estatisticaButton);

        add(controlPanel, BorderLayout.WEST);

        JPanel areasPanel = new JPanel();
        areasPanel.setLayout(new GridLayout(6, 2));
        areasPanel.add(new JLabel("Peça:"));
        pecaComboBox = new JComboBox<>(pecas.keySet().toArray(new String[0]));
        pecaComboBox.addActionListener(new AreaSelectionListener());
        areasPanel.add(pecaComboBox);

        areasPanel.add(new JLabel("Sessão:"));
        sessaoComboBox = new JComboBox<>(pecas.get(pecaComboBox.getSelectedItem()).getSessoes().keySet().toArray(new String[0]));
        sessaoComboBox.addActionListener(new AreaSelectionListener());
        areasPanel.add(sessaoComboBox);

        areasPanel.add(new JLabel("Área:"));
        areaComboBox = new JComboBox<>(pecas.get(pecaComboBox.getSelectedItem()).getSessoes().get(sessaoComboBox.getSelectedItem()).getAreas().keySet().toArray(new String[0]));
        areaComboBox.addActionListener(new AreaSelectionListener());
        areasPanel.add(areaComboBox);

        add(areasPanel, BorderLayout.CENTER);

        poltronasPanel = new JPanel();
        poltronasPanel.setLayout(new GridLayout(0, 5));
        updatePoltronasPanel();
        add(poltronasPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updatePoltronasPanel() {
        poltronasPanel.removeAll();
        String peca = (String) pecaComboBox.getSelectedItem();
        String sessao = (String) sessaoComboBox.getSelectedItem();
        String area = (String) areaComboBox.getSelectedItem();
        Area selectedArea = pecas.get(peca).getSessoes().get(sessao).getAreas().get(area);

        for (int i = 0; i < selectedArea.getTotalPoltronas(); i++) {
            JButton poltronaButton = new JButton(String.valueOf(i + 1));
            poltronaButton.setBackground(selectedArea.isPoltronaOcupada(i) ? RED : null);
            poltronaButton.setEnabled(!selectedArea.isPoltronaOcupada(i));
            poltronaButton.addActionListener(new PoltronaSelectionListener(i));
            poltronasPanel.add(poltronaButton);
        }

        poltronasPanel.revalidate();
        poltronasPanel.repaint();
    }

    private class AreaSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updatePoltronasPanel();
        }
    }

    private class PoltronaSelectionListener implements ActionListener {
        private final int poltrona;

        public PoltronaSelectionListener(int poltrona) {
            this.poltrona = poltrona;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cpf = JOptionPane.showInputDialog("Digite o CPF do cliente:");
            if (cpf == null || cpf.isEmpty()) {
                return;
            }

            Cliente cliente = clientes.computeIfAbsent(cpf, Cliente::new);
            String peca = (String) pecaComboBox.getSelectedItem();
            String sessao = (String) sessaoComboBox.getSelectedItem();
            String area = (String) areaComboBox.getSelectedItem();
            Area selectedArea = pecas.get(peca).getSessoes().get(sessao).getAreas().get(area);

            if (selectedArea.isPoltronaOcupada(poltrona)) {
                JOptionPane.showMessageDialog(null, "Poltrona já ocupada!");
                return;
            }

            Ingresso ingresso = new Ingresso(cpf, peca, sessao, area, poltrona, selectedArea.getPreco());
            cliente.adicionarIngresso(ingresso);
            selectedArea.comprarPoltrona(poltrona);
            JOptionPane.showMessageDialog(null, "Ingresso comprado com sucesso!");
            updatePoltronasPanel();
        }
    }
    public Cliente gerarClientePorCPF() {
        String cpf = JOptionPane.showInputDialog("Digite o CPF do cliente:");
        if (cpf == null || cpf.isEmpty()) {
            return null;
        }

        Cliente cliente = clientes.get(cpf);
        if (cliente == null || cliente.getIngressos().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado ou não possui ingressos.");
            return null;
        }

        return cliente;
    }
    private class CancelarIngressoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cliente cliente = gerarClientePorCPF();
            if (cliente == null) return;

            Ingresso ingresso = (Ingresso) JOptionPane.showInputDialog(null, "Selecione o ingresso a ser cancelado:",
                    "Cancelar Ingresso", JOptionPane.QUESTION_MESSAGE, null, cliente.getIngressos().toArray(), null);

            if (ingresso != null) {
                cliente.removerIngresso(ingresso);
                pecas.get(ingresso.getPeca()).getSessoes().get(ingresso.getSessao()).getAreas().get(ingresso.getArea()).cancelarPoltrona(ingresso.getPoltrona());
                JOptionPane.showMessageDialog(null, "Ingresso cancelado com sucesso!");
                updatePoltronasPanel();
            }
        }
    }

    private class ImprimirIngressoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cliente cliente = gerarClientePorCPF();

            StringBuilder ingressosStr = new StringBuilder("Ingressos do Cliente " + cliente.getCpf() + ":\n");
            for (Ingresso ingresso : cliente.getIngressos()) {
                ingressosStr.append(ingresso).append("\n");
            }
            JOptionPane.showMessageDialog(null, ingressosStr.toString());
        }
    }

    private class EstatisticaVendasListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            /*
            Estatística de Vendas: Essa funcionalidade permite visualizar:
            - Qual peça teve mais e menos ingressos vendidos;
            - Qual sessão teve maior e menor ocupação de poltronas;
            - Qual a peça/sessão mais lucrativa e menos lucrativa;
            - Lucro médio do teatro com todas as áreas por peça.
            */
            StringBuilder estatisticas = new StringBuilder("Estatísticas de Vendas:\n");

            double receitaTotal = 0;
            int ingressosTotal = 0;
            Peca pecaMaisIngressosVendidos = null;

            for (Map.Entry<String, Peca> entry : pecas.entrySet()) {
                Peca peca = entry.getValue();

                // Peça com mais ingressos vendidos
                if (pecaMaisIngressosVendidos == null) {
                    pecaMaisIngressosVendidos = peca;
                }
                if (peca.getReceitaTotal() > pecaMaisIngressosVendidos.getTotalIngressosVendidos()) {
                    pecaMaisIngressosVendidos = peca;
                }
                estatisticas.append("\nPeça: ").append(peca.getNome());

                Sessao sessaoMaisVendida = null;
                for (Map.Entry<String, Sessao> sessao_map : peca.getSessoes().entrySet()) {
                    estatisticas.append("\nSessão: ").append(sessao_map.getKey());
                    estatisticas.append("; Qtd. Poltronas: ").append(sessao_map.getValue().getTotalIngressosVendidos());
                    estatisticas.append("; Receita: ").append(sessao_map.getValue().getReceita());
                    if (sessaoMaisVendida == null) {
                        sessaoMaisVendida = sessao_map.getValue();
                    }
                    if (sessao_map.getValue().getReceita() > sessaoMaisVendida.getReceita()) {
                        sessaoMaisVendida = sessao_map.getValue();
                    }
                }
                estatisticas.append("\nSessão mais vendida: ").append(sessaoMaisVendida.nome).append("\n");


                ingressosTotal += peca.getTotalIngressosVendidos();
                receitaTotal += peca.getReceitaTotal();
            }

            estatisticas.append("\nPeça com maior número de ingressos vendidos: ").append(pecaMaisIngressosVendidos.getNome());
            estatisticas.append("\nQuantidade de Ingressos: ").append(pecaMaisIngressosVendidos.getTotalIngressosVendidos());
            estatisticas.append("\nReceita: ").append(pecaMaisIngressosVendidos.getReceitaTotal()).append("\n");

            estatisticas.append("\nTotal de ingressos vendidos: ").append(ingressosTotal);
            estatisticas.append("\nReceita total: ").append(receitaTotal).append("\n");

            JOptionPane.showMessageDialog(null, estatisticas.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}