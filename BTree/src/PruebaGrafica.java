public class PruebaGrafica{
    public static void main(String[] args) {
        // Creamos un Árbol B con grado mínimo t = 3
        BTree arbol = new BTree(3);

        int[] valores = {10, 20, 5, 6, 12, 30, 7, 17, 25, 40, 50, 22, 23, 15, 60, 70, 80};

        System.out.println("Insertando valores... ");
        for (int valor : valores) {
            arbol.insert(valor);
        }

        System.out.println("Generando diagrama gráfico...");
        arbol.exportarHTML("arbol_b.html");
    }
}