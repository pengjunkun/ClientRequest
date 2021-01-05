import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class FixedPortionUrlIteration extends URLIteration {
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