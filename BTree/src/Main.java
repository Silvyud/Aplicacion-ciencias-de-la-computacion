
    public static void main(String[] args) {
        // Creamos un Árbol B con grado mínimo t = 3
        // Esto significa: Mínimo 2 claves, Máximo 5 claves por nodo.
        BTree arbol = new BTree(3);

        int[] valores = {10, 20, 5, 6, 12, 30, 7, 17, 25, 40, 50, 22, 23, 15, 60, 70, 80};

        System.out.println("Insertando valores: ");
        for (int v : valores) System.out.print(v + " ");
        System.out.println("\n");

        for (int valor : valores) {
            arbol.insert(valor);
        }

        // 1. Mostrar que los datos están ordenados lógicamente
        System.out.println("Recorrido In-Order (Valores ordenados):");
        arbol.traverse();

        // 2. Mostrar la estructura jerárquica (Visualización del árbol)
        arbol.showTree();

        System.out.println("\nExplicación Visual:");
        System.out.println("- ├── y │ : Indican ramas intermedias.");
        System.out.println("- └──     : Indica el final de una rama (último hijo).");
        System.out.println("- El orden es de arriba hacia abajo (Raíz -> Hijos).");
        System.out.println("- Los hijos se muestran en orden: menor, medio, mayor.");
    }
