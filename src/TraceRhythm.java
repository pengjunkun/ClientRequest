import java.io.*;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * Created by JackPeng(pengjunkun@gmail.com) on 2021/1/5.
 */
public class TraceRhythm extends RequestRhythm
{
	BufferedReader reader;
	private long currentBase;
	//this is the first trace timestamp
	private long traceBase;

	private BiConsumer<HttpResponse<Path>, Long> action;
	Timer timer;

	public TraceRhythm(String traceFileName)
	{
		timer = new Timer(true);
		initialAction();
		try
		{
			reader = new BufferedReader(
					new FileReader("./trace/" + traceFileName));
			Trace trace = getOnePairData();
			traceBase = trace.timeStamp;
			currentBase = System.currentTimeMillis();
			timer.schedule(getTraceTask(trace.content, action), 0);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

	}

	private void initialAction()
	{
		//this variable is used as a callback, first is response, second is requested url
		action = (pathHttpResponse, startTimeStamp) -> {
			if (pathHttpResponse == null)
			{
				System.out.println("There is one request timeout!!!");
				//timeout is 5s
				latencySum += 5000;
				return;
			}

			responseCount += 1;
			HttpHeaders headers = pathHttpResponse.headers();
			int size = Integer.parseInt(
					headers.firstValue("Content-Length").orElse(null));
			sizeSum += size;

			long latency = System.currentTimeMillis() - startTimeStamp;
			latencySum += latency;

			System.out.println(String.format("fileSize:%s", size));
			System.out.println("latency:" + latency);

			//add more tasks
			Trace trace = getOnePairData();
			if (trace == null)
			{
				System.out
						.println("----------------------------use all traces!");
				return;
			}
			timer.schedule(getTraceTask(trace.content, action),
					new Date(currentBase+(trace.timeStamp - traceBase) * 1000));
		};
	}

	@Override public Timer makeTimer()
	{
		//initial  tasks into Timer's taskQueen
		for (int i = 0; i < 300; i++)
		{
			Trace trace = getOnePairData();
			if (trace == null)
				break;

			timer.schedule(getTraceTask(trace.content, action),
					new Date(currentBase+(trace.timeStamp - traceBase) * 1000));
		}
		return timer;
	}

	//in trace model, build the Httprequest itself
	private TimerTask getTraceTask(String content, BiConsumer action)
	{
		TimerTask task = new TimerTask()
		{
			@Override public void run()
			{
				//get the url
				String url = URLIteration.constructURL(content);
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
						.completedStage(System.currentTimeMillis()), action);
			}
		};
		return task;

	}

	private Trace getOnePairData()
	{
		try
		{
			String tmp = reader.readLine();
			if (tmp == null)
			{
				System.out
						.println("----------------------------use all traces!");
				return null;
			}
			String[] oneLineArray = tmp.split(",");
			return new Trace(Long.parseLong(oneLineArray[1]), oneLineArray[4]);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;

	}

	private class Trace
	{
		long timeStamp;
		String content;

		public Trace(long timeStamp, String content)
		{
			this.timeStamp = timeStamp;
			this.content = content;
		}
	}
}
