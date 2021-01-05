import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class RequestRhythm
{
	//testing duration: millisecond
	private static int _duration;
	private static Iterator urlIteration;
	private MyHttp myHttp;

	private double latencySum;
	private long sizeSum;
	private int requestCount;

	public RequestRhythm()
	{
		myHttp=new MyHttp();
		requestCount = 0;
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
				WorkAgent.reportByPrint(get_duration(),requestCount,latencySum,sizeSum);

				System.exit(0);
			}
		}, get_duration());
	}

	public TimerTask getRequestTask(){
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
									.completedStage(System.currentTimeMillis()),
							//the two params are from two completedstages
							(pathHttpResponse, startTimeStamp) -> {
								if (pathHttpResponse == null)
								{
									System.out.println(
											"There is one request timeout!!!");
									//timeout is 5s
									latencySum+=5000;
									return;
								}
								HttpHeaders headers = pathHttpResponse
										.headers();
								int size = Integer.parseInt(
										headers.firstValue("Content-Length")
												.orElse(null));
								sizeSum += size;

								long latency = System.currentTimeMillis()
										- startTimeStamp;
								latencySum += latency;

								System.out.println(
										String.format("fileSize:%s", size));
								System.out.println("latency:" + latency);
							});

				}
			}
		};
		return task;
	}
}