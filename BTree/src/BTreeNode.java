class BTreeNode {
    int[] keys;           // Arreglo de claves
    int t;                // Grado mínimo (define el rango del número de claves)
    BTreeNode[] children; // Arreglo de punteros a los hijos
    int n;                // Número actual de claves
    boolean leaf;         // Verdadero si el nodo es una hoja

    public BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;

        // Un nodo puede contener como máximo 2*t - 1 claves
        this.keys = new int[2 * t - 1];

        // Un nodo puede tener como máximo 2*t hijos
        this.children = new BTreeNode[2 * t];

        this.n = 0;
    }

    // Método para buscar una clave en el subárbol enraizado en este nodo
    public BTreeNode search(int k) {
        int i = 0;

        // Encontrar la primera clave mayor o igual a k
        while (i < n && k > keys[i]) {
            i++;
        }

        // Si la clave encontrada es igual a k, devolvemos este nodo
        if (i < n && keys[i] == k) {
            return this;
        }

        // Si no se encuentra y es una hoja, la clave no está en el árbol
        if (leaf) {
            return null;
        }

        // Bajar al hijo correspondiente
        return children[i].search(k);
    }
    // Método para recorrer e imprimir todos los nodos en orden
    public void traverse() {
        int i;
        for (i = 0; i < n; i++) {
            // Si no es hoja, primero recorremos el hijo antes de imprimir la clave
            if (!leaf) {
                children[i].traverse(); // <--- Llamada recursiva al hijo izquierdo
            }
            System.out.print(" " + keys[i]); // <--- Imprime la clave
        }
        // Imprimir el último hijo (el que está a la derecha de la última clave)
        if (!leaf) {
            children[i].traverse();
        }
    }
    // Imprime el nodo y sus hijos usando caracteres ASCII para las ramas
    public void printStructure(String prefix, boolean isTail) {
        // 1. Construir la cadena de claves del nodo: [ 10 | 20 ]
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < n; i++) {
            sb.append(keys[i]).append(i < n - 1 ? " | " : " ");
        }
        sb.append("]");

        // 2. Imprimir el nodo actual con su conector
        // isTail verifica si es el último hijo para usar "└──" o "├──"
        System.out.println(prefix + (isTail ? "└── " : "├── ") + sb.toString());

        // 3. Imprimir los hijos recursivamente
        if (!leaf) {
            for (int i = 0; i <= n; i++) {
                boolean last = (i == n); // ¿Es el último hijo?
                // Si el nodo actual es el último (tail), sus hijos no necesitan la barra vertical "│" del padre
                String newPrefix = prefix + (isTail ? "    " : "│   ");

                if (children[i] != null) {
                    children[i].printStructure(newPrefix, last);
                }
            }
        }
    }

    // --- Convertir Nodo a JSON para la visualización Web ---
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        // Claves
        sb.append("\"keys\": [");
        for (int i = 0; i < n; i++) {
            sb.append(keys[i]).append(i < n - 1 ? "," : "");
        }
        sb.append("],");

        // Hijos
        sb.append("\"children\": [");
        if (!leaf) {
            for (int i = 0; i <= n; i++) {
                if (children[i] != null) {
                    sb.append(children[i].toJson());
                    if (i < n) sb.append(",");
                }
            }
        }
        sb.append("]");

        sb.append("}");
        return sb.toString();
    }


}