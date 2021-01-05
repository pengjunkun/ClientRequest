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
	private static Iterator urlIteration;
	private MyHttp myHttp;

	protected double latencySum;
	protected long sizeSum;
	protected int requestCount;
	protected int responseCount;

	public RequestRhythm()
	{
		myHttp=new MyHttp();
		requestCount = 0;
		responseCount=0;
		latencySum = 0;
		sizeSum = 0;
	}

	public abstract Timer makeTimer();
	public void execute(){


		//control testing duration
		setDurationTimer(makeTimer());
	}

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

	public void setDurationTimer(Timer timer){

		//control the test time
		Timer durationTimer = new Timer();
		//use durationTimer to control the request duration
		durationTimer.schedule(new TimerTask()
		{
			@Override public void run()
			{
				//when duration arrives, cancel all the requests
				timer.cancel();

				//print the result
				WorkAgent.reportByPrint(get_duration(),requestCount,responseCount,latencySum,sizeSum);

				System.exit(0);
			}
		}, get_duration());
	}

	public TimerTask getRequestTask(BiConsumer action){
		TimerTask task = new TimerTask()
		{
			@Override public void run()
			{
				if (getUrlIteration().hasNext())
				{
					//get the url
					String url = getUrlIteration().next();
					System.out.println(requestCount+". request URL:" + url);

					//get the request which is just started in async way.
					CompletableFuture<HttpResponse<Path>> response = myHttp
							.get(url);
					//after send the request, count one more
					requestCount++;

					//the first parameter is used to record the start time of request
					//the second param is a callback which can help calculate the latency and size
					response.completeOnTimeout(null, 5, TimeUnit.SECONDS);
					response.thenAcceptBoth(CompletableFuture
									.completedStage(System.currentTimeMillis()),action);

				}
			}
		};
		return task;
	}
}