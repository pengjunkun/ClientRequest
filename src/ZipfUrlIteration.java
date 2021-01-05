import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ZipfUrlIteration extends URLIteration {
	public ZipfUrlIteration(double zipf_param)
	{
		super();
	}

	@Override public Iterator<String> iterator()
	{
		return null;
	}

	@Override public void forEach(Consumer<? super String> action)
	{

	}

	@Override public Spliterator<String> spliterator()
	{
		return null;
	}
}