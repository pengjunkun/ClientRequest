import java.net.URL;
import java.time.Duration;
import java.util.Iterator;

public abstract class RequestRhythm
{
	//testing duration: millisecond
	private static int _duration;
	private static Iterator urlIteration;

	public abstract void execute();

	//caution:set duration in minutes, but save in millisecond
	public static void setDuration(double aDuration)
	{
		_duration = (int) Duration.ofSeconds((int)(aDuration*60)).toMillis();
	}

	public static int get_duration()
	{
		return _duration;
	}

	public static Iterator<String> getUrlIteration()
	{
		return urlIteration;
	}

	public static void setUrlIteration(Iterator iteration)
	{
		urlIteration=iteration;
	}
}