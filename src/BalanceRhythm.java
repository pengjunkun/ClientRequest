import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BalanceRhythm extends RequestRhythm
{

	// the parameter is treated as the requesting times per second
	float timesPerSecond;

	private MyHttp myHttp;

	private double latencySum;
	private long sizeSum;
	private int requestCount;

	public BalanceRhythm(float rhythm_param)
	{
		timesPerSecond = rhythm_param;
		myHttp = new MyHttp();
		requestCount = 0;
		latencySum = 0;
		sizeSum = 0;
	}

	//do the request action
	public void execute()
	{
		//control the test time
		Timer durationTimer = new Timer();
		//set the threads of timer to be daemon threads, which will help theses requesting threads stop when duration arrives
		Timer timer = new Timer(true);
		//get the period(interval)
		long period = (long) (1000.0 / timesPerSecond);
		if (period <= 0)
		{
			System.out.println(
					"ERROR: please make sure the times per second is no more than 1000");
			System.exit(1);
		}

		//add all the tasks into Timer:
		timer.schedule(getRequestTask(), 0, period);

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
		}, RequestRhythm.get_duration());

	}

	//get the request task
	private TimerTask getRequestTask()
	{
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