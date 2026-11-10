import java.util.List;

public interface IPesquisavel {
    List<ItemBiblioteca> pesquisarPorTitulo(String titulo);
    List<ItemBiblioteca> pesquisarPorTipo(String tipo);
}