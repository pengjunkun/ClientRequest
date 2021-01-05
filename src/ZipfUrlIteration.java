import org.apache.commons.math3.distribution.ZipfDistribution;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ZipfUrlIteration extends URLIteration
{

	private ZipfDistribution zipfDistribution;

	@Override public boolean hasNext()
	{
		return true;
	}

	@Override public String next()
	{
		return URLIteration.constructURL(zipfDistribution.sample());
	}

	public ZipfUrlIteration(double zipf_param)
	{
		zipfDistribution = new ZipfDistribution(URLIteration.rangeTO,
				zipf_param);
	}
}