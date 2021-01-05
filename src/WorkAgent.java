import java.io.File;
import java.time.Duration;

/*
	main logic class
	use this class to control the whole request test process
 */
public class WorkAgent
{
	private RequestRhythm requestRhythm;
	private URLIteration urlIteration;

	//1st function, describe the testing duration
	public void setDuration(double duration)
	{
		RequestRhythm.setDuration(duration);
	}

	//2nd function, describe the request rhythm(interval time,unit: millisecond)
	public void setRequestRhythm(Client.RHYTHM rhythm, String rhythm_param)
	{
		switch (rhythm)
		{
		case BALANCE:
			//when use the rhythm of balance, the parameter is treated as the requesting count per second
			//the count can not be bigger than 1000, but it can be less than 1, which means several seconds once.
			requestRhythm = new BalanceRhythm(Integer.parseInt(rhythm_param));
			break;
		case POSSION:
			//when use the rhythm of possion, the parameter is treated as lambda of possion distribution
			requestRhythm = new PoissonRhythm(Integer.parseInt(rhythm_param));
			break;
		case TRACE:
			requestRhythm = new TraceRhythm(rhythm_param);
			break;
		}
		//set the urlIteration in the function of 'setPopularity', as this variable will be created there.
	}

	//3nd function, describe the request content popularity
	public void setPopularity(Client.POPULARITY popularity, double zipf_param)
	{
		switch (popularity)
		{
		case EVEN:
			//every url will be requested in the same frequency
			urlIteration = new EvenUrlIteration();
			break;
		case ZIPF:
			//Todo: implement the detail logics
			urlIteration = new ZipfUrlIteration(zipf_param);
			break;
		}
		//set the urlIteration in the function of 'setPopularity', as this variable will be created there.
		requestRhythm.setUrlIteration(urlIteration);
	}

	//start the testing process from this function
	public void doRequest()
	{
		requestRhythm.execute();
	}

	public static void reportByPrint(int duration, int requestCount,
			int responseCount, double latencySum, double sizeSum)
	{

		System.out.println("===============report==================");
		System.out.println(
				"In " + duration / 1000 + " seconds, sent " + requestCount
						+ " requests, received " + responseCount);
		//calculte the average
		System.out.printf("the average latency is: %.2f ms;\n",
				(latencySum / responseCount));
		System.out.printf("the average throughput is: %.2f KB/s.\n",
				(sizeSum / 1024) / Duration.ofMillis(duration).toSeconds());
		System.out.println("===============report==================");

	}
}