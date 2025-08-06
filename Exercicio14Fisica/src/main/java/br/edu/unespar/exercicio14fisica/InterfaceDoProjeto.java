package br.edu.unespar.exercicio14fisica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.BorderFactory;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class InterfaceDoProjeto extends JFrame {

    // Componentes da interface
    private JLabel labelImagem;
    private ImageIcon imagemCircuito;
    private JPanel painelCentro;
    private JPanel painelSul;
    private JLabel labelDiferencaPotencial;
    private JLabel labelCapacitancia;
    private JLabel labelCircuito;
    private JTextArea textResultado;
    private JTextField textDiferencaPotencial;
    private JTextField textCapacitancia;
    private JButton botaoCalcular;
    private JButton botaoResetar;

    // Construtor da interface
    public InterfaceDoProjeto() {
        super("Calculando Cargas em Capacitores"); // Título da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new BorderLayout(10, 10)); // Layout principal da janela

        // Painel que contém a imagem do circuito
        JPanel painelCircuito = new JPanel(new BorderLayout(10, 10));
        painelCircuito.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margens internas

        labelCircuito = new JLabel("Imagem do circuito:");
        labelCircuito.setHorizontalAlignment(JLabel.CENTER); // Centraliza o texto
        painelCircuito.add(labelCircuito, BorderLayout.NORTH); // Adiciona texto acima da imagem

        // Carrega a imagem do circuito
        imagemCircuito = new ImageIcon(getClass().getResource("/imagem/Circuito2.png"));
        labelImagem = new JLabel(imagemCircuito);
        labelImagem.setHorizontalAlignment(JLabel.CENTER);
        painelCircuito.add(labelImagem, BorderLayout.CENTER); // Adiciona imagem ao centro

        add(painelCircuito, BorderLayout.NORTH); // Adiciona o painel ao topo da janela

        // Painel central com os campos de entrada
        painelCentro = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 linhas, 2 colunas
        painelCentro.setBorder(new EmptyBorder(20, 40, 20, 40)); // Margem interna

        labelDiferencaPotencial = new JLabel("Digite a diferença de potencial U (em Volts): ");
        painelCentro.add(labelDiferencaPotencial);

        textDiferencaPotencial = new JTextField(10); // Campo de texto para tensão
        painelCentro.add(textDiferencaPotencial);

        labelCapacitancia = new JLabel("Digite a capacitância C de cada capacitor (em µF): ");
        painelCentro.add(labelCapacitancia);

        textCapacitancia = new JTextField(10); // Campo de texto para capacitância
        painelCentro.add(textCapacitancia);

        add(painelCentro, BorderLayout.CENTER); // Adiciona o painel central à janela

        // Botão de calcular, com cores do fundo e do texto setadas
        botaoCalcular = new JButton("Calcular");
        painelCentro.add(botaoCalcular);
        botaoCalcular.setBackground(Color.BLUE);
        botaoCalcular.setForeground(Color.WHITE);

        // Botão de resetar, com cores do fundo e do texto setadas
        botaoResetar = new JButton("Resetar");
        botaoResetar.setBackground(Color.RED);
        botaoResetar.setForeground(Color.WHITE);
        painelCentro.add(botaoResetar);

        // Área de texto para exibir os resultados
        textResultado = new JTextArea();
        textResultado.setPreferredSize(new Dimension(300, 120));
        textResultado.setEditable(false); // O usuário não pode digitar
        textResultado.setFocusable(false);
        textResultado.setOpaque(false);
        textResultado.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Fonte com espaçamento fixo

        // Painel inferior com os resultados
        painelSul = new JPanel();
        painelSul.setBorder(new EmptyBorder(10, 20, 20, 20));
        painelSul.add(textResultado);
        add(painelSul, BorderLayout.SOUTH); // Adiciona painel inferior

        // Ação ao clicar em "Calcular"
        botaoCalcular.addActionListener(e -> calcularCarga());

        // Ação ao clicar em "Resetar"
        botaoResetar.addActionListener(e -> {
            textDiferencaPotencial.setText(""); // Limpa campo da tensão
            textCapacitancia.setText(""); // Limpa campo da capacitância
            textResultado.setText(""); // Limpa resultados
        });

        pack(); // Ajusta o tamanho automático da janela
        setVisible(true); // Torna a janela visível
        setLocationRelativeTo(null); // Centraliza na tela
    }

    // Método responsável pelos cálculos das cargas
    private void calcularCarga() {
        try {
            // Lê e converte os valores dos campos de texto
            double V = Double.parseDouble(textDiferencaPotencial.getText());
            double C_micro = Double.parseDouble(textCapacitancia.getText());

            // Verifica se os valores são válidos
            if (V <= 0 || C_micro <= 0) {
                JOptionPane.showMessageDialog(this, "Os valores de tensão e capacitância devem ser maiores que 0!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Converte a capacitância de microfarads (µF) para Farads (F)
            double C = C_micro * 1e-6;

            // Q1: Está diretamente sob a tensão V.
            double q1 = C * V;

            // C2 e C3 estão em série. A equivalente C23 = C/2.
            // C23 está em paralelo com C4. A equivalente C234 = C/2 + C = 1.5C.
            // C5 está em série com C234. A equivalente total do ramo Ceq = (3/5)C.
            
            // A carga total que passa pelo ramo inferior é a mesma que passa por C5.
            // Q_total_ramo = Ceq * V
            double q5 = (3.0 / 5.0) * C * V;

            //Partindo de q = Ceq3 * V = Ceq2 * V2 => V2 = (Ceq3 * V) / Ceq2
            // Substituindo os valores de Ceq3 e Ceq2 => V2 = V / 2.5
            double v_paralelo = V / 2.5;
            
            // Q4: Está sob a tensão V_paralelo.
            double q4 = C * v_paralelo;

            // A carga que passa pelo trecho C3-C4 (em série) é Q34 = C34 * V_paralelo
            // Como C3 e C4 estão em série, Q3 = Q4.
            double q2 = (C / 2) * v_paralelo;
            double q3 = q2;

            // Formatação dos resultados em notação científica com "×10^"
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setExponentSeparator("×10^");
            DecimalFormat formatador = new DecimalFormat("0.###E0", symbols);
            
            // Formata os resultados
            String q1Formatado = formatador.format(q1).replace("E", "");
            String q2Formatado = formatador.format(q2).replace("E", "");
            String q3Formatado = formatador.format(q3).replace("E", "");
            String q4Formatado = formatador.format(q4).replace("E", "");
            String q5Formatado = formatador.format(q5).replace("E", "");

            // Exibe os resultados formatados na área de texto
            textResultado.setText(
                "Resultados das Cargas (Q):\n" +
                "Q1 = " + q1Formatado + " C\n" +
                "Q2 = " + q2Formatado + " C\n" +
                "Q3 = " + q3Formatado + " C\n" +
                "Q4 = " + q4Formatado + " C\n" +
                "Q5 = " + q5Formatado + " C"
            );

        } catch (NumberFormatException e) {
            // Caso o usuário insira texto não numérico
            JOptionPane.showMessageDialog(this,
                "Por favor, insira valores numéricos válidos nos campos.",
                "Erro de Entrada",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
