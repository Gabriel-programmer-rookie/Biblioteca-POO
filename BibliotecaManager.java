import java.io.*;
import java.util.*;

public class BibliotecaManager implements IPesquisavel {
    private final List<ItemBiblioteca> acervo = new ArrayList<>();
    private final Map<Integer, Usuario> usuarios = new HashMap<>();
    private final List<Emprestimo> emprestimosAtivos = new ArrayList<>();
    
    private final String ARQUIVO_ACERVO = "acervo.txt";
    private final String ARQUIVO_USUARIOS = "usuarios.txt";
    private final String ARQUIVO_EMPRESTIMOS = "emprestimos.txt";

    public BibliotecaManager() {
        carregarDados();
    }

    public void adicionarItem(ItemBiblioteca item) {
        acervo.add(item);
        salvarDados();
    }

    public void cadastrarUsuario(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
        salvarDados();
    }

    public ItemBiblioteca buscarItemPorId(int id) {
        for (ItemBiblioteca item : acervo) {
            if (item.getId() == id) return item;
        }
        return null;
    }

    public Usuario buscarUsuarioPorId(int id) {
        return usuarios.get(id);
    }

    public void realizarEmprestimo(int idUsuario, int idItem) throws LimiteExcedidoException, ItemIndisponivelException, Exception {
        Usuario u = usuarios.get(idUsuario);
        ItemBiblioteca item = buscarItemPorId(idItem);

        if (u == null) throw new Exception("Usuário não encontrado.");
        if (item == null) throw new Exception("Item não encontrado no acervo.");
        if (!item.isDisponivel()) throw new ItemIndisponivelException("O item já está emprestado.");
        if (u.getQtdEmprestados() >= u.getLimiteEmprestimos()) {
            throw new LimiteExcedidoException("O limite de empréstimos foi atingido.");
        }

        item.setDisponivel(false);
        u.incrementarEmprestimo();
        emprestimosAtivos.add(new Emprestimo(u, item));
        salvarDados();
    }

    public void realizarDevolucao(int idUsuario, int idItem) throws Exception {
        Usuario u = usuarios.get(idUsuario);
        ItemBiblioteca item = buscarItemPorId(idItem);

        if (u == null || item == null) throw new Exception("Dados inválidos para devolução.");
        if (item.isDisponivel()) throw new Exception("O item consta como disponível.");

        boolean emprestimoExiste = emprestimosAtivos.stream()
                .anyMatch(emp -> emp.getItem().getId() == idItem && emp.getUsuario().getId() == idUsuario);
        if (!emprestimoExiste) throw new Exception("Este usuário não possui empréstimo ativo deste item.");

        item.setDisponivel(true);
        u.decrementarEmprestimo();
        emprestimosAtivos.removeIf(emp -> emp.getItem().getId() == idItem && emp.getUsuario().getId() == idUsuario);
        salvarDados();
    }

    @Override
    public List<ItemBiblioteca> pesquisarPorTitulo(String titulo) {
        List<ItemBiblioteca> res = new ArrayList<>();
        for (ItemBiblioteca item : acervo) {
            if (item.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                res.add(item);
            }
        }
        return res;
    }

    @Override
    public List<ItemBiblioteca> pesquisarPorTipo(String tipo) {
        List<ItemBiblioteca> res = new ArrayList<>();
        for (ItemBiblioteca item : acervo) {
            if (item.getTipoItem().equalsIgnoreCase(tipo)) {
                res.add(item);
            }
        }
        return res;
    }

    public List<ItemBiblioteca> getAcervo() { return acervo; }
    public Collection<Usuario> getUsuarios() { return usuarios.values(); }
    public List<Emprestimo> getEmprestimosAtivos() { return emprestimosAtivos; }

    public final void salvarDados() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ACERVO))) {
            for (ItemBiblioteca item : acervo) {
                if (item instanceof Livro) {
                    Livro l = (Livro) item;
                    writer.println("LIVRO;" + l.getId() + ";" + l.getTitulo() + ";" + l.getAno() + ";" + l.isDisponivel() + ";" + l.getAutor() + ";" + l.getNumPaginas());
                } else if (item instanceof Revista) {
                    Revista r = (Revista) item;
                    writer.println("REVISTA;" + r.getId() + ";" + r.getTitulo() + ";" + r.getAno() + ";" + r.isDisponivel() + ";" + r.getEdicao());
                } else if (item instanceof DVD) {
                    DVD d = (DVD) item;
                    writer.println("DVD;" + d.getId() + ";" + d.getTitulo() + ";" + d.getAno() + ";" + d.isDisponivel() + ";" + d.getDuracaoMinutos());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            for (Usuario u : usuarios.values()) {
                writer.println(u.getTipoUsuario() + ";" + u.getId() + ";" + u.getNome() + ";" + u.getQtdEmprestados());
            }
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_EMPRESTIMOS))) {
            for (Emprestimo emp : emprestimosAtivos) {
                writer.println(emp.getUsuario().getId() + ";" + emp.getItem().getId());
            }
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    public final void carregarDados() {
        File fAcervo = new File(ARQUIVO_ACERVO);
        if (fAcervo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fAcervo))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] p = linha.split(";");
                    String tipo = p[0];
                    int id = Integer.parseInt(p[1]);
                    String titulo = p[2];
                    int ano = Integer.parseInt(p[3]);
                    boolean disp = Boolean.parseBoolean(p[4]);

                    if (tipo.equals("LIVRO")) {
                        Livro l = new Livro(id, titulo, ano, p[5], Integer.parseInt(p[6]));
                        l.setDisponivel(disp);
                        acervo.add(l);
                    } else if (tipo.equals("REVISTA")) {
                        Revista r = new Revista(id, titulo, ano, Integer.parseInt(p[5]));
                        r.setDisponivel(disp);
                        acervo.add(r);
                    } else if (tipo.equals("DVD")) {
                        DVD d = new DVD(id, titulo, ano, Integer.parseInt(p[5]));
                        d.setDisponivel(disp);
                        acervo.add(d);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }

        File fUsuarios = new File(ARQUIVO_USUARIOS);
        if (fUsuarios.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fUsuarios))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] p = linha.split(";");
                    String tipo = p[0];
                    int id = Integer.parseInt(p[1]);
                    String nome = p[2];
                    int qtdEmp = Integer.parseInt(p[3]);

                    Usuario u = null;
                    if (tipo.equals("ALUNO")) u = new Aluno(id, nome);
                    else if (tipo.equals("PROFESSOR")) u = new Professor(id, nome);
                    else if (tipo.equals("FUNCIONARIO")) u = new Funcionario(id, nome);

                    if (u != null) {
                        for (int i = 0; i < qtdEmp; i++) u.incrementarEmprestimo();
                        usuarios.put(u.getId(), u);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }

        File fEmprestimos = new File(ARQUIVO_EMPRESTIMOS);
        if (fEmprestimos.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fEmprestimos))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] p = linha.split(";");
                    Usuario u = usuarios.get(Integer.parseInt(p[0]));
                    ItemBiblioteca item = buscarItemPorId(Integer.parseInt(p[1]));
                    if (u != null && item != null) {
                        emprestimosAtivos.add(new Emprestimo(u, item));
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }
}