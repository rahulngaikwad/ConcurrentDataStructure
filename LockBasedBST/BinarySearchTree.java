import java.util.concurrent.atomic.AtomicInteger;

public abstract class  BinarySearchTree<Type> {
	public AtomicInteger size;
	
	public abstract boolean add(Type value);
	public abstract boolean remove(Type value);
	public abstract boolean contains(Type value);
	
	public abstract int countExternalNodes();
	
	public abstract String printTree();
	
	public int getSize(){
		return size.get();
	}
}
