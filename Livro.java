public class Livro extends ItemBiblioteca {
    private final String autor;
    private final int numPaginas;

    public Livro(int id, String titulo, int ano, String autor, int numPaginas) {
        super(id, titulo, ano);
        this.autor = autor;
        this.numPaginas = numPaginas;
    }

    @Override
    public String getTipoItem() { return "LIVRO"; }

    public String getAutor() { return autor; }
    public int getNumPaginas() { return numPaginas; }

    @Override
    public String toString() {
        return super.toString() + " | Autor: " + autor + " | Páginas: " + numPaginas;
    }
}