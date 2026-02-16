import java.io.FileWriter;
import java.io.IOException;

class BTree {
    BTreeNode root;
    int t; // Grado mínimo

    public BTree(int t) {
        this.root = null;
        this.t = t;
    }

    // Método principal de búsqueda
    public BTreeNode search(int k) {
        if (root == null) {
            return null;
        } else {
            return root.search(k);
        }
    }

    // Método principal de inserción
    public void insert(int k) {
        if (root == null) {
            // El árbol está vacío, creamos la raíz
            root = new BTreeNode(t, true);
            root.keys[0] = k;
            root.n = 1;
        } else {
            // Si la raíz está llena, el árbol crece en altura
            if (root.n == 2 * t - 1) {
                BTreeNode newRoot = new BTreeNode(t, false);
                newRoot.children[0] = root;

                // Dividir la antigua raíz y mover una clave a la nueva raíz
                splitChild(newRoot, 0, root);

                // Decidir cuál de los dos hijos tendrá la nueva clave
                int i = 0;
                if (newRoot.keys[0] < k) {
                    i++;
                }
                insertNonFull(newRoot.children[i], k);

                root = newRoot;
            } else {
                // Si la raíz no está llena, insertar de forma normal
                insertNonFull(root, k);
            }
        }
    }

    // Método auxiliar para insertar en un nodo que no está lleno
    private void insertNonFull(BTreeNode node, int k) {
        int i = node.n - 1;

        if (node.leaf) {
            // Mover todas las claves mayores un espacio hacia la derecha
            while (i >= 0 && node.keys[i] > k) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            // Insertar la nueva clave
            node.keys[i + 1] = k;
            node.n++;
        } else {
            // Encontrar el hijo que va a recibir la nueva clave
            while (i >= 0 && node.keys[i] > k) {
                i--;
            }
            i++;

            // Si el hijo está lleno, dividirlo
            if (node.children[i].n == 2 * t - 1) {
                splitChild(node, i, node.children[i]);
                if (node.keys[i] < k) {
                    i++;
                }
            }
            insertNonFull(node.children[i], k);
        }
    }

    // Método para dividir el hijo y (que está lleno) del nodo x
    private void splitChild(BTreeNode parent, int i, BTreeNode fullChild) {
        BTreeNode newNode = new BTreeNode(fullChild.t, fullChild.leaf);
        newNode.n = t - 1;

        // Copiar las últimas (t - 1) claves de fullChild a newNode
        if (t - 1 >= 0) System.arraycopy(fullChild.keys, 0 + t, newNode.keys, 0, t - 1);

        // Si no es hoja, copiar los últimos t hijos
        if (!fullChild.leaf) {
            if (t >= 0) System.arraycopy(fullChild.children, 0 + t, newNode.children, 0, t);
        }

        fullChild.n = t - 1;

        // Crear espacio para el nuevo hijo en el nodo padre
        for (int j = parent.n; j >= i + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[i + 1] = newNode;

        // Mover una clave de fullChild al padre
        for (int j = parent.n - 1; j >= i; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }
        parent.keys[i] = fullChild.keys[t - 1];
        parent.n++;
    }

    // Método para recorrer el árbol
    public void traverse() {
        if (root != null) {
            root.traverse(); // <--- Llama al método del nodo raíz
        }
        System.out.println(); // Un salto de línea al final para que se vea bonito en consola
    }
    public void showTree() {
        if (root != null) {
            System.out.println("\n--- ESTRUCTURA VISUAL DEL ÁRBOL B ---");
            // Iniciamos con prefijo vacío y asumiendo que la raíz es "cola" para cerrar el dibujo
            root.printStructure("", true);
            System.out.println("-------------------------------------");
        } else {
            System.out.println("El árbol está vacío.");
        }
    }
    // --- NUEVO: Generar archivo HTML con el diagrama ---
    public void exportarHTML(String fileName) {
        if (root == null) return;

        String jsonTree = root.toJson();
        String htmlContent = getHtmlTemplate(jsonTree);

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(htmlContent);
            System.out.println("✅ Archivo generado exitosamente: " + fileName);
            System.out.println("👉 Abre este archivo en tu navegador para ver el diagrama gráfico.");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    // Plantilla HTML/JS/CSS para dibujar el árbol estilo diagrama
    private String getHtmlTemplate(String jsonData) {

        String sb = "<!DOCTYPE html>\n" +
                "<html>\n<head>\n" +
                "<style>\n" +
                "  body { font-family: sans-serif; background: #f4f4f4; text-align: center; }\n" +
                "  h1 { color: #333; }\n" +
                "  .node { display: flex; border: 2px solid #333; background: #fff; border-radius: 4px; box-shadow: 2px 2px 5px rgba(0,0,0,0.2); position: absolute; }\n" +
                "  .key { padding: 8px 12px; border-right: 1px solid #ccc; font-weight: bold; color: #0044cc; }\n" +
                "  .key:last-child { border-right: none; }\n" +
                "  svg { position: absolute; top: 0; left: 0; width: 100%; height: 100%; z-index: -1; }\n" +
                "  path { fill: none; stroke: #666; stroke-width: 2px; }\n" +
                "  #canvas { position: relative; margin-top: 50px; display: inline-block; }\n" +
                "</style>\n</head>\n<body>\n" +
                "  <h1>Visualización de Árbol B (Grado t=" + t + ")</h1>\n" +
                "  <div id=\"canvas\"></div>\n" +
                "  <script>\n" +
                "    const treeData = " + jsonData + ";\n" +
                "    const canvas = document.getElementById('canvas');\n" +
                "    const NODE_HEIGHT = 40, LEVEL_GAP = 80, KEY_WIDTH = 40;\n" +
                "    function layoutTree(node, depth) {\n" +
                "      if (!node.children || node.children.length === 0) {\n" +
                "        node.width = node.keys.length * KEY_WIDTH;\n" +
                "        node.depth = depth;\n" +
                "        return node.width;\n" +
                "      }\n" +
                "      let totalWidth = 0;\n" +
                "      node.children.forEach(child => { totalWidth += layoutTree(child, depth + 1) + 20; });\n" +
                "      totalWidth -= 20;\n" +
                "      node.width = Math.max(node.keys.length * KEY_WIDTH, totalWidth);\n" +
                "      node.depth = depth;\n" +
                "      return node.width;\n" +
                "    }\n" +
                "    function assignCoords(node, startX, startY) {\n" +
                "      node.x = startX + (node.width - (node.keys.length * KEY_WIDTH)) / 2;\n" +
                "      node.y = startY;\n" +
                "      node.actualWidth = node.keys.length * KEY_WIDTH;\n" +
                "      let currentX = startX;\n" +
                "      if (node.width > (node.children ? node.children.reduce((acc, c) => acc + c.width + 20, 0) - 20 : 0)) {\n" +
                "         let childrenWidth = node.children.reduce((acc, c) => acc + c.width + 20, 0) - 20;\n" +
                "         currentX = startX + (node.width - childrenWidth) / 2;\n" +
                "      }\n" +
                "      if (node.children) {\n" +
                "        node.children.forEach(child => {\n" +
                "          assignCoords(child, currentX, startY + LEVEL_GAP);\n" +
                "          currentX += child.width + 20;\n" +
                "        });\n" +
                "      }\n" +
                "    }\n" +
                "    function draw(node, svg) {\n" +
                "      const div = document.createElement('div');\n" +
                "      div.className = 'node';\n" +
                "      div.style.left = node.x + 'px';\n" +
                "      div.style.top = node.y + 'px';\n" +
                "      div.style.width = node.actualWidth + 'px';\n" +
                "      node.keys.forEach(k => {\n" +
                "        const span = document.createElement('span');\n" +
                "        span.className = 'key';\n" +
                "        span.innerText = k;\n" +
                "        div.appendChild(span);\n" +
                "      });\n" +
                "      canvas.appendChild(div);\n" +
                "      if (node.children && node.children.length > 0) {\n" +
                "        node.children.forEach((child, index) => {\n" +
                "          let originX = node.x + (index * (node.actualWidth / node.children.length)) + (node.actualWidth / node.children.length)/2;\n" +
                "          let originY = node.y + NODE_HEIGHT;\n" +
                "          let destX = child.x + child.actualWidth / 2;\n" +
                "          let destY = child.y;\n" +
                "          const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');\n" +
                "          path.setAttribute('d', `M ${originX} ${originY} C ${originX} ${originY + 20}, ${destX} ${destY - 20}, ${destX} ${destY}`);\n" +
                "          svg.appendChild(path);\n" +
                "          draw(child, svg);\n" +
                "        });\n" +
                "      }\n" +
                "    }\n" +
                "    layoutTree(treeData, 0);\n" +
                "    assignCoords(treeData, 0, 50);\n" +
                "    const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n" +
                "    const maxY = (treeData.depth + 1) * LEVEL_GAP + 200;\n" +
                "    canvas.style.width = (treeData.width + 100) + 'px';\n" +
                "    canvas.style.height = maxY + 'px';\n" +
                "    canvas.appendChild(svg);\n" +
                "    draw(treeData, svg);\n" +
                "  </script>\n</body>\n</html>";

        return sb;
    }
}