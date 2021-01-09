import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public abstract class RequestRhythm
{
	//testing duration: millisecond
	private static int _duration;
	protected Iterator urlIteration;
	protected MyHttp myHttp;

	private boolean useTraceContent;

	protected double latencySum;
	protected long sizeSum;
	protected int requestCount;
	protected int responseCount;
	protected int granularity;

	public RequestRhythm()
	{
		myHttp = new MyHttp();
		requestCount = 0;
		responseCount = 0;
		latencySum = 0;
		sizeSum = 0;
		useTraceContent=false;
	}

	public boolean isUseTraceContent()
	{
		return useTraceContent;
	}

	public void setUseTraceContent(boolean useTraceContent)
	{
		this.useTraceContent = useTraceContent;
	}

	public abstract Timer makeTimer();

	public void execute()
	{

		//control testing duration
		setDurationTimer(makeTimer());
	}

	//caution:set duration in second, but save in millisecond
	public static void setDuration(int aDuration)
	{
		_duration = aDuration;
	}

	public static int get_duration()
	{
		return _duration;
	}

	public  Iterator<String> getUrlIteration()
	{
		return urlIteration;
	}

	public void setUrlIteration(Iterator iteration)
	{
		this.urlIteration = iteration;
	}

	public void setDurationTimer(Timer taskTimer)
	{

		//control the test time
		Timer durationTimer = new Timer();
		//control the report period
		Timer reportTimer = new Timer(true);
		reportTimer.schedule(new TimerTask()
		{
			@Override public void run()
			{

				WorkAgent.reportByPrint(granularity, responseCount,
						responseCount, latencySum, sizeSum);
				//reset
				resetReportVariables();
			}
		}, granularity*1000, granularity * 1000);

		//use durationTimer to control the request duration
		durationTimer.schedule(new TimerTask()
		{
			@Override public void run()
			{
				//when duration arrives, cancel all the requests
				taskTimer.cancel();
				reportTimer.cancel();
				System.exit(0);
			}
		}, get_duration() * 1000);
	}

	private void resetReportVariables()
	{
		requestCount = 0;
		responseCount = 0;
		latencySum = 0;
		sizeSum = 0;
	}

	public TimerTask getRequestTask(BiConsumer action)
	{
		TimerTask task = new TimerTask()
		{
			@Override public void run()
			{
				if (getUrlIteration().hasNext())
				{
					//get the url
					String url = getUrlIteration().next();
					System.out.println(requestCount + ". request URL:" + url);

					//get the request which is just started in async way.
					CompletableFuture<HttpResponse<Void>> response = myHttp
							.get(url);
					//after send the request, count one more
					requestCount++;

					//the first parameter is used to record the start time of request
					//the second param is a callback which can help calculate the latency and size
					response.completeOnTimeout(null, 5, TimeUnit.SECONDS);
					response.thenAcceptBoth(CompletableFuture
									.completedStage(System.currentTimeMillis()),
							action);

				}
			}
		};
		return task;
	}

	public void setGranularity(int granularity)
	{
		this.granularity = granularity;
	}
}