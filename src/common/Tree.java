package common;

import java.util.ArrayList;
import java.util.Iterator;

import model.AIGameState;
import javafx.scene.Parent;

public class Tree<T> {
    private Node<T> root;

    public Tree(T rootData) {
        root = new Node<T>(rootData, null, -1);
        root.children = new ArrayList<Node<T>>();
    }

    public static class Node<T> {
    	/// Ktorym dzieckiem swojego rodzica jest ten wezel
    	private int index;
    	/// Dane przechowywane w wezle
        private T data;
        /// Ojciec
        private Node<T> parent;
        /// Lista dzieci
        private ArrayList<Node<T>> children;
        /// Glebokosc wezla (korzen ma zero, jego dzieci 1, etc.)
        private int depth;
        
        public boolean checked = false;
        
        /**
         * Konstruktor ustawiajacy dane wezla, ojca i indeks
         * @param data dane
         * @param parent ojciec (null dla korzenia)
         * @param index ktore dziecko swego ojca
         */
        public Node(T data, Node<T> parent, int index) {
        	this.data = data;
        	this.parent = parent;
        	this.index = index;
        	this.children = new ArrayList<Node<T>>();
        	this.depth = parent == null? 0: parent.getDepth() + 1;
        }
        
        /**
         * Getter dla depth
         * @return glebokosc wezla (korzen ma zero, jego dzieci 1, etc.)
         */
        private int getDepth() {
			return depth;
		}

		/**
         * @return dane przechowywane w wezle
         */
		public T getData() {
			return data;
		}

		/**
		 * Getter dla children
		 * @return lista dzieci
		 */
		public ArrayList<Node<T>> getChildren() {
			return children;
		}

		/**
		 * @return czy wezel jest ostatnim dzieckiem swojego ojca?
		 */
		public boolean isLastChild() {
			return index == parent.countChildren() - 1;
		}
		
		/**
		 * Dodaje dziecko zadanego wêz³a (dodanie na koniec listy)
		 */
		private void addChild(Node<T> child) {
			children.add(child);
		}

		/**
		 * Dodaje nowy element o zadanej zawartoœci
		 * @param childContent zawartosc wezla do dodania
		 */
		public Node<T> addChild(T childContent) {
			Node<T> child = new Node<T>(childContent, this, this.countChildren());
			addChild(child);
			return child;
		}
		
		/**
		 * @return nastêpne dziecko tego samego rodzica
		 */
		private Node<T> nextChild() {
			if(isLastChild()) {
				throw new RuntimeException("Incorrect function call");
			}
			return parent.getChildren().get(index + 1);
		}

		/**
		 * @return liczba dzieci wezla
		 */
		private int countChildren() {
			return children.size();
		}

		/**
		 * @return ojciec wezla
		 */
		public Node<T> getParent() {
			return parent;
		}

		/**
		 * @return ktorym dzieckiem swego ojca jest ten wezel
		 */
		public int getIndex() {
			return index;
		}
		
	    /**
	     * Dla zadanego wêz³a, zwraca jego przodka n-tego poziomu, tj taki wêze³
	     * kóry znajduje siê o n poziomów wy¿ej.
	     * @param depth o ile poziomow w gore wejsc (dla depth=0 zwracamy this)
	     * @return przodek n-tego stopnia, jezeli nie istnieje to null
	     */
		private Node<T> getAncestorOnHeight(int depth) {
			if(depth > getDepth()) {
				throw new RuntimeException("Incorrect parameters passed to function call");
			}
			Node<T> currentNode = this;
			for(int i=0;i<depth;++i) {
				currentNode = currentNode.getParent();
			}
			return currentNode;
		}
		

	    /**
	     * Dla zadanego wezla X, zwraca pierwszy (w kolejnosci od lewej do prawej) 
	     * wezel Y, ktory spelnia warunek: Y znajduje sie o D poziomow nizej niz X.
	     * (X - wartosc zwracana, Y - parametr currentNode, D - parametr depth)
	     * @param level o ile poziomow mamy zejsc?
	     * @param currentNode dla ktorego wezla szukamy potomka
	     * @return pierwszy potomek o zadanej glebokosci wzgledem zadanego wezla 
	     * 		   (null jesli zaden nie istnieje)
	     */
		private Node<T> getFirstDescendantOnDepth(int depth) {
			if(depth == 0) {
				return this;
			}
			Node<T> currentNode = this;
			for(Node<T> child: currentNode.getChildren())
			{
				Node<T> result = child.getFirstDescendantOnDepth(depth-1);
				if(result != null)
				{
					return result;
				}
			}
			// nie znalezlismy nic
			return null;
		}

		public boolean isLeaf() {
			return countChildren() == 0;
		}

    }
    
    public static class TreeLevelIterator<T> implements Iterator<Node<T>> {
		Node<T> currentNode;
		
    	public TreeLevelIterator(Node<T> firstNode) {
			currentNode = firstNode;
		}
    	
		@Override
		public boolean hasNext() {
			return currentNode != null;
		}

		@Override
		public Node<T> next() {
			Node<T> oldNode = currentNode;
			for(int i=0; i < currentNode.getDepth(); ++i) {
				Node<T> ancestor = currentNode.getAncestorOnHeight(i);
				if(!ancestor.isLastChild()) {
					currentNode = ancestor.nextChild().getFirstDescendantOnDepth(i);
					return oldNode;
				}
			}
			currentNode = null;
			return oldNode;
		}

		@Override
		public void remove() {
			throw new RuntimeException("Unsupported operation");
		}
    	
    }

    public static class TreeLevel<T> implements Iterable<Node<T>> {
    	Node<T> currentNode;
    	
		public TreeLevel(Node<T> currentNode) {
			this.currentNode = currentNode;
		}
		
		@Override
		public Iterator<Node<T>> iterator() {
			return new TreeLevelIterator<T>(currentNode);
		}
    
    }
    
    public TreeLevel<T> getTreeLevel(int depth) {
		Node<T> firstNode = root.getFirstDescendantOnDepth(depth);
		return new TreeLevel<T>(firstNode);
    }

	public Node<T> getRoot() {
		return root;
	}

}