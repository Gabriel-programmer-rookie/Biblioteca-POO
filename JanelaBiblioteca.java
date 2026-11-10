import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JanelaBiblioteca extends JFrame {
    private final BibliotecaManager manager;

    private final DefaultTableModel modeloAcervo = criarModelo("ID", "Tipo", "Título", "Ano", "Detalhes", "Disponível");
    private final DefaultTableModel modeloUsuarios = criarModelo("ID", "Tipo", "Nome", "Empréstimos", "Limite");
    private final DefaultTableModel modeloEmprestimos = criarModelo("ID Usuário", "Usuário", "ID Item", "Item");
    private final DefaultTableModel modeloPesquisa = criarModelo("ID", "Tipo", "Título", "Ano", "Detalhes", "Disponível");

    private final JComboBox<String> cbTipoItem = new JComboBox<>(new String[]{"Livro", "Revista", "DVD"});
    private final JTextField txtItemId = new JTextField(5);
    private final JTextField txtTitulo = new JTextField(16);
    private final JTextField txtAno = new JTextField(5);
    private final JLabel lblExtraA = new JLabel("Autor");
    private final JTextField txtExtraA = new JTextField(12);
    private final JLabel lblExtraB = new JLabel("Páginas");
    private final JTextField txtExtraB = new JTextField(5);

    private final JComboBox<String> cbTipoUsuario = new JComboBox<>(new String[]{"Aluno", "Professor", "Funcionário"});
    private final JTextField txtUsuarioId = new JTextField(5);
    private final JTextField txtNome = new JTextField(20);

    private final JTextField txtEmpUsuario = new JTextField(6);
    private final JTextField txtEmpItem = new JTextField(6);

    private final JComboBox<String> cbCriterio = new JComboBox<>(new String[]{"Título", "Tipo"});
    private final JTextField txtBusca = new JTextField(20);

    public JanelaBiblioteca(BibliotecaManager manager) {
        this.manager = manager;
        setTitle("Sistema de Gestão de Biblioteca Universitária");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 560);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Acervo", criarAbaAcervo());
        abas.addTab("Usuários", criarAbaUsuarios());
        abas.addTab("Empréstimos", criarAbaEmprestimos());
        abas.addTab("Pesquisa", criarAbaPesquisa());
        add(abas);

        cbTipoItem.addActionListener(e -> ajustarCamposItem());
        ajustarCamposItem();
        atualizarTudo();
    }

    private static DefaultTableModel criarModelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JScrollPane criarTabela(DefaultTableModel modelo) {
        JTable tabela = new JTable(modelo);
        tabela.setFillsViewportHeight(true);
        tabela.setRowHeight(24);
        return new JScrollPane(tabela);
    }

    private JPanel criarAbaAcervo() {
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.add(new JLabel("Tipo"));
        form.add(cbTipoItem);
        form.add(new JLabel("ID"));
        form.add(txtItemId);
        form.add(new JLabel("Título"));
        form.add(txtTitulo);
        form.add(new JLabel("Ano"));
        form.add(txtAno);
        form.add(lblExtraA);
        form.add(txtExtraA);
        form.add(lblExtraB);
        form.add(txtExtraB);
        JButton btn = new JButton("Adicionar item");
        btn.addActionListener(e -> adicionarItem());
        form.add(btn);

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(criarTabela(modeloAcervo), BorderLayout.CENTER);
        painel.add(form, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaUsuarios() {
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.add(new JLabel("Tipo"));
        form.add(cbTipoUsuario);
        form.add(new JLabel("ID"));
        form.add(txtUsuarioId);
        form.add(new JLabel("Nome"));
        form.add(txtNome);
        JButton btn = new JButton("Cadastrar usuário");
        btn.addActionListener(e -> cadastrarUsuario());
        form.add(btn);

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(criarTabela(modeloUsuarios), BorderLayout.CENTER);
        painel.add(form, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaEmprestimos() {
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.add(new JLabel("ID do usuário"));
        form.add(txtEmpUsuario);
        form.add(new JLabel("ID do item"));
        form.add(txtEmpItem);
        JButton btnEmprestar = new JButton("Emprestar");
        btnEmprestar.addActionListener(e -> executarMovimentacao(true));
        JButton btnDevolver = new JButton("Devolver");
        btnDevolver.addActionListener(e -> executarMovimentacao(false));
        form.add(btnEmprestar);
        form.add(btnDevolver);

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(form, BorderLayout.NORTH);
        painel.add(criarTabela(modeloEmprestimos), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarAbaPesquisa() {
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.add(new JLabel("Pesquisar por"));
        form.add(cbCriterio);
        form.add(txtBusca);
        JButton btn = new JButton("Pesquisar");
        btn.addActionListener(e -> pesquisar());
        form.add(btn);

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(form, BorderLayout.NORTH);
        painel.add(criarTabela(modeloPesquisa), BorderLayout.CENTER);
        return painel;
    }

    private void ajustarCamposItem() {
        String tipo = (String) cbTipoItem.getSelectedItem();
        boolean livro = "Livro".equals(tipo);
        if (livro) {
            lblExtraA.setText("Autor");
        } else if ("Revista".equals(tipo)) {
            lblExtraA.setText("Edição");
        } else {
            lblExtraA.setText("Duração (min)");
        }
        lblExtraB.setVisible(livro);
        txtExtraB.setVisible(livro);
        txtExtraA.setText("");
        txtExtraB.setText("");
    }

    private void adicionarItem() {
        try {
            int id = Integer.parseInt(txtItemId.getText().trim());
            String titulo = txtTitulo.getText().trim();
            int ano = Integer.parseInt(txtAno.getText().trim());
            String extraA = txtExtraA.getText().trim();

            if (titulo.isEmpty() || titulo.contains(";")) {
                mostrarErro("Informe um título válido, sem ponto e vírgula.");
                return;
            }
            if (manager.buscarItemPorId(id) != null) {
                mostrarErro("Já existe um item com esse ID.");
                return;
            }

            String tipo = (String) cbTipoItem.getSelectedItem();
            ItemBiblioteca item;
            if ("Livro".equals(tipo)) {
                if (extraA.isEmpty() || extraA.contains(";")) {
                    mostrarErro("Informe um autor válido, sem ponto e vírgula.");
                    return;
                }
                int paginas = Integer.parseInt(txtExtraB.getText().trim());
                item = new Livro(id, titulo, ano, extraA, paginas);
            } else if ("Revista".equals(tipo)) {
                item = new Revista(id, titulo, ano, Integer.parseInt(extraA));
            } else {
                item = new DVD(id, titulo, ano, Integer.parseInt(extraA));
            }

            manager.adicionarItem(item);
            txtItemId.setText("");
            txtTitulo.setText("");
            txtAno.setText("");
            txtExtraA.setText("");
            txtExtraB.setText("");
            atualizarTudo();
        } catch (NumberFormatException e) {
            mostrarErro("Preencha os campos numéricos com números inteiros.");
        }
    }

    private void cadastrarUsuario() {
        try {
            int id = Integer.parseInt(txtUsuarioId.getText().trim());
            String nome = txtNome.getText().trim();

            if (nome.isEmpty() || nome.contains(";")) {
                mostrarErro("Informe um nome válido, sem ponto e vírgula.");
                return;
            }
            if (manager.buscarUsuarioPorId(id) != null) {
                mostrarErro("Já existe um usuário com esse ID.");
                return;
            }

            String tipo = (String) cbTipoUsuario.getSelectedItem();
            Usuario usuario;
            if ("Aluno".equals(tipo)) {
                usuario = new Aluno(id, nome);
            } else if ("Professor".equals(tipo)) {
                usuario = new Professor(id, nome);
            } else {
                usuario = new Funcionario(id, nome);
            }

            manager.cadastrarUsuario(usuario);
            txtUsuarioId.setText("");
            txtNome.setText("");
            atualizarTudo();
        } catch (NumberFormatException e) {
            mostrarErro("O ID deve ser um número inteiro.");
        }
    }

    private void executarMovimentacao(boolean emprestar) {
        try {
            int idUsuario = Integer.parseInt(txtEmpUsuario.getText().trim());
            int idItem = Integer.parseInt(txtEmpItem.getText().trim());

            if (emprestar) {
                manager.realizarEmprestimo(idUsuario, idItem);
            } else {
                manager.realizarDevolucao(idUsuario, idItem);
            }

            txtEmpUsuario.setText("");
            txtEmpItem.setText("");
            atualizarTudo();
            JOptionPane.showMessageDialog(this, emprestar ? "Empréstimo realizado com sucesso." : "Devolução realizada com sucesso.");
        } catch (NumberFormatException e) {
            mostrarErro("Os IDs devem ser números inteiros.");
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    private void pesquisar() {
        String termo = txtBusca.getText().trim();
        List<ItemBiblioteca> resultado;
        if ("Título".equals(cbCriterio.getSelectedItem())) {
            resultado = manager.pesquisarPorTitulo(termo);
        } else {
            resultado = manager.pesquisarPorTipo(termo);
        }

        modeloPesquisa.setRowCount(0);
        for (ItemBiblioteca item : resultado) {
            modeloPesquisa.addRow(linhaItem(item));
        }
    }

    private Object[] linhaItem(ItemBiblioteca item) {
        return new Object[]{item.getId(), item.getTipoItem(), item.getTitulo(), item.getAno(), detalhes(item), item.isDisponivel() ? "Sim" : "Não"};
    }

    private String detalhes(ItemBiblioteca item) {
        if (item instanceof Livro) {
            Livro l = (Livro) item;
            return l.getAutor() + " - " + l.getNumPaginas() + " págs.";
        }
        if (item instanceof Revista) {
            return "Edição " + ((Revista) item).getEdicao();
        }
        if (item instanceof DVD) {
            return ((DVD) item).getDuracaoMinutos() + " min";
        }
        return "";
    }

    private void atualizarTudo() {
        modeloAcervo.setRowCount(0);
        for (ItemBiblioteca item : manager.getAcervo()) {
            modeloAcervo.addRow(linhaItem(item));
        }

        modeloUsuarios.setRowCount(0);
        for (Usuario u : manager.getUsuarios()) {
            modeloUsuarios.addRow(new Object[]{u.getId(), u.getTipoUsuario(), u.getNome(), u.getQtdEmprestados(), u.getLimiteEmprestimos()});
        }

        modeloEmprestimos.setRowCount(0);
        for (Emprestimo emp : manager.getEmprestimosAtivos()) {
            modeloEmprestimos.addRow(new Object[]{emp.getUsuario().getId(), emp.getUsuario().getNome(), emp.getItem().getId(), emp.getItem().getTitulo()});
        }
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}