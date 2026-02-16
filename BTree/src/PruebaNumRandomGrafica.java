import java.util.Random;

public class PruebaNumRandomGrafica {


        public static void main(String[] args) {

            BTree arbol = new BTree(5);
            System.out.println("Insertando valores en el Árbol B de forma aleatoria...");

            Random random = new Random();
            int cantidad = 100; // cantidad de claves a insertar

            for (int i = 0; i < cantidad; i++) {
                int valor = random.nextInt(100); // números entre 0 y 99
                arbol.insert(valor);
                System.out.println("Insertado: " + valor);
            }

            System.out.println("\nRecorrido del Árbol B (debería estar ordenado):");
            arbol.traverse();
            arbol.exportarHTML("arbol_brandom.html");
        }
}
