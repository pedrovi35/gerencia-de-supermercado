import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class SupermercadoGUI extends JFrame {

    private GerenciadorEstoque gerenciador;
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;

    // Campos do formulário
    private JTextField txtCodigo, txtNome, txtPreco, txtDataValidade, txtRisco, txtVolume;
    private JCheckBox chkAlcoolica;
    private JComboBox<String> comboTipo;
    private JButton btnSalvar, btnExcluir, btnLimpar;
    
    // Paineis para campos específicos
    private JPanel painelAlimenticio, painelLimpeza, painelBebida;

    public SupermercadoGUI() {
        gerenciador = new GerenciadorEstoque();

        setTitle("Sistema de Estoque de Supermercado");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Layout Principal ---
        setLayout(new BorderLayout(10, 10));

        // --- Tabela de Produtos ---
        String[] colunas = {"Código", "Nome", "Preço", "Tipo", "Detalhes"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna a tabela não editável
            }
        };
        tabelaProdutos = new JTable(tableModel);
        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);
        
        // Listener para seleção na tabela
        tabelaProdutos.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tabelaProdutos.getSelectedRow() != -1) {
                preencherFormularioComLinhaSelecionada();
            }
        });


        // --- Painel do Formulário ---
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new BorderLayout());
        
        // Formulário Principal
        JPanel formGrid = new JPanel(new GridLayout(0, 2, 5, 5));
        formGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formGrid.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        formGrid.add(txtCodigo);

        formGrid.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        formGrid.add(txtNome);

        formGrid.add(new JLabel("Preço:"));
        txtPreco = new JTextField();
        formGrid.add(txtPreco);

        formGrid.add(new JLabel("Tipo de Produto:"));
        String[] tipos = {"Alimentício", "Limpeza", "Bebida"};
        comboTipo = new JComboBox<>(tipos);
        formGrid.add(comboTipo);
        
        painelFormulario.add(formGrid, BorderLayout.NORTH);

        // --- Painéis Específicos (controlados pelo ComboBox) ---
        JPanel paineisEspecificos = new JPanel(new CardLayout());
        
        // Painel Alimentício
        painelAlimenticio = new JPanel(new GridLayout(0, 2, 5, 5));
        painelAlimenticio.add(new JLabel("Data de Validade:"));
        txtDataValidade = new JTextField();
        painelAlimenticio.add(txtDataValidade);
        paineisEspecificos.add(painelAlimenticio, "Alimentício");

        // Painel Limpeza
        painelLimpeza = new JPanel(new GridLayout(0, 2, 5, 5));
        painelLimpeza.add(new JLabel("Risco:"));
        txtRisco = new JTextField();
        painelLimpeza.add(txtRisco);
        paineisEspecificos.add(painelLimpeza, "Limpeza");

        // Painel Bebida
        painelBebida = new JPanel(new GridLayout(0, 2, 5, 5));
        painelBebida.add(new JLabel("Volume (ml):"));
        txtVolume = new JTextField();
        painelBebida.add(txtVolume);
        painelBebida.add(new JLabel("É Alcoólica?"));
        chkAlcoolica = new JCheckBox();
        painelBebida.add(chkAlcoolica);
        paineisEspecificos.add(painelBebida, "Bebida");
        
        painelFormulario.add(paineisEspecificos, BorderLayout.CENTER);
        
        comboTipo.addActionListener(e -> {
            CardLayout cl = (CardLayout)(paineisEspecificos.getLayout());
            cl.show(paineisEspecificos, (String)comboTipo.getSelectedItem());
        });
        
        // --- Painel de Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar Formulário");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        painelFormulario.add(painelBotoes, BorderLayout.SOUTH);

        add(painelFormulario, BorderLayout.SOUTH);

        // --- Ações dos Botões ---
        btnSalvar.addActionListener(e -> salvarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnLimpar.addActionListener(e -> limparFormulario());

        // Inicializa a Tabela e o formulário
        atualizarTabela();
        ((CardLayout)paineisEspecificos.getLayout()).show(paineisEspecificos, "Alimentício");
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) return;

        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        Optional<Produto> produtoOpt = gerenciador.buscarProdutoPorCodigo(codigo);

        produtoOpt.ifPresent(produto -> {
            limparFormulario();
            txtCodigo.setText(produto.getCodigo());
            txtNome.setText(produto.getNome());
            txtPreco.setText(String.valueOf(produto.getPreco()));
            
            // Trava o código para edição para evitar inconsistência
            txtCodigo.setEditable(false);

            if (produto instanceof ProdutoAlimenticio p) {
                comboTipo.setSelectedItem("Alimentício");
                txtDataValidade.setText(p.getDataValidade());
            } else if (produto instanceof ProdutoLimpeza p) {
                comboTipo.setSelectedItem("Limpeza");
                txtRisco.setText(p.getRisco());
            } else if (produto instanceof Bebida p) {
                comboTipo.setSelectedItem("Bebida");
                txtVolume.setText(String.valueOf(p.getVolumeMl()));
                chkAlcoolica.setSelected(p.isAlcoolica());
            }
        });
    }

    private void salvarProduto() {
        try {
            String codigo = txtCodigo.getText();
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            String tipo = (String) comboTipo.getSelectedItem();

            if (codigo.isBlank() || nome.isBlank()) {
                JOptionPane.showMessageDialog(this, "Código e Nome são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Produto produto = null;
            switch (tipo) {
                case "Alimentício":
                    produto = new ProdutoAlimenticio(codigo, nome, preco, txtDataValidade.getText());
                    break;
                case "Limpeza":
                    produto = new ProdutoLimpeza(codigo, nome, preco, txtRisco.getText());
                    break;
                case "Bebida":
                    double volume = Double.parseDouble(txtVolume.getText());
                    boolean alcoolica = chkAlcoolica.isSelected();
                    produto = new Bebida(codigo, nome, preco, volume, alcoolica);
                    break;
            }

            if (produto != null) {
                // Se o campo código não estiver editável, significa que estamos alterando um produto existente
                if (!txtCodigo.isEditable()) {
                    gerenciador.alterarProduto(codigo, produto);
                    JOptionPane.showMessageDialog(this, "Produto alterado com sucesso!");
                } else {
                    // Verifica se o código já existe para um novo produto
                    if (gerenciador.buscarProdutoPorCodigo(codigo).isPresent()) {
                        JOptionPane.showMessageDialog(this, "Já existe um produto com este código.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    gerenciador.adicionarProduto(produto);
                    JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
                }
            }

            limparFormulario();
            atualizarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço e Volume devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o produto " + codigo + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            gerenciador.excluirProduto(codigo);
            atualizarTabela();
            limparFormulario();
            JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
        }
    }
    
    private void limparFormulario() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtDataValidade.setText("");
        txtRisco.setText("");
        txtVolume.setText("");
        chkAlcoolica.setSelected(false);
        comboTipo.setSelectedIndex(0);
        tabelaProdutos.clearSelection();
        txtCodigo.setEditable(true); // Libera o campo código para novos cadastros
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        for (Produto p : gerenciador.getListaProdutos()) {
            Object[] row = {
                p.getCodigo(),
                p.getNome(),
                p.getPreco(),
                p.getTipo(), // Método polimórfico simples
                p.getDescricaoDetalhada() // **CHAMADA POLIMÓRFICA PRINCIPAL**
            };
            tableModel.addRow(row);
        }
    }
}