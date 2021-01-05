import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class EvenUrlIteration extends URLIteration
{

	private int index = 0;

	@Override public boolean hasNext()
	{
		//as we use a circle
		return true;
	}

	@Override public String next()
	{
		if (index == URLIteration.rangeTO)
			index = 0;
		String tmp = URLIteration.constructURL(index++);
		return tmp;
	}
}

