import java.util.Iterator;

public abstract class URLIteration implements Iterator<String>
{
	//from [1,rangeTo]
	protected static int rangeTO = 29;
	private static String urlBaseInt="http://gcp/2/seg-%d.m4s";
	private static String urlBaseString="http://gcp/2/seg-%s.m4s";

	protected static String constructURL(int index)
	{
		return String.format(urlBaseInt, index);
	}
	protected static String constructURL(String index)
	{

		//for test
		return String.format(urlBaseString, "1");
	}
}