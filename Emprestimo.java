import java.time.LocalDate;

public class Emprestimo {
    private final Usuario usuario;
    private final ItemBiblioteca item;
    private final LocalDate dataEmprestimo;
    private final LocalDate dataDevolucaoPrevista;

    public Emprestimo(Usuario usuario, ItemBiblioteca item) {
        this.usuario = usuario;
        this.item = item;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = LocalDate.now().plusDays(14);
    }

    public Usuario getUsuario() { return usuario; }
    public ItemBiblioteca getItem() { return item; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }

    @Override
    public String toString() {
        return "Empréstimo -> Usuário: " + usuario.getNome() + 
               " | Item: " + item.getTitulo() + 
               " | Retirado em: " + dataEmprestimo + 
               " | Devolução Prevista: " + dataDevolucaoPrevista;
    }
}