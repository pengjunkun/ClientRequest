import java.util.Iterator;

public abstract class URLIteration implements Iterator<String>
{
	//from [1,rangeTo]
	protected static int rangeTO = 29;

	protected static String constructURL(int index)
	{
		return String.format("http://gcp/2/seg-%d.m4s", index);
	}
}