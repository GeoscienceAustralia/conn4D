package au.gov.ga.conn4d.utils;

public class FixedStack<T> {
	    private T[] stack;
	    private int size;
	    private int top;

	    @SuppressWarnings("unchecked")
		public FixedStack(int sz)
	    {
	        this.stack = (T[]) new Object[sz];
	        this.top = -1;
	        this.size = sz;
	    }

	    public void push(T obj)
	    {
	        if (top >= size) throw new IllegalArgumentException("Size = " + size);
	        stack[++top] = obj;
	    }

	    public T pop()
	    {
	        if (top < 0) throw new IndexOutOfBoundsException();
	        T obj = stack[top--];
	        stack[top + 1] = null;
	        return obj;
	    }

	    public int size()
	    {
	        return size;
	    }

	    public int elements()
	    {
	        return top + 1;
	    }
}
