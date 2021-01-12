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
	private float slowRatio;

	private BiConsumer<HttpResponse<Path>, Long> action;
	Timer timer;

	public TraceRhythm(String traceFileName)
	{

		timer = new Timer(true);
		initialAction();
		try
		{
			reader = new BufferedReader(new FileReader("./conf/slowRatio.txt"));
			slowRatio = Float.parseFloat(reader.readLine());
			reader.close();

			reader = new BufferedReader(
					//					new FileReader("./conf/" + traceFileName.substring(1,traceFileName.length()-1)));
					new FileReader("./conf/" + traceFileName));
			Trace trace = getOnePairData();
			traceBase = trace.timeStamp;
			currentBase = System.currentTimeMillis();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
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
				MyLog.logger.severe("There is one request timeout!!!");
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

			MyLog.logger.fine(String.format("fileSize:%s", size));
			MyLog.logger.fine("latency:" + latency);

			//add more tasks
			Trace trace = getOnePairData();
			if (trace == null)
			{
				MyLog.logger.info("----------------------------use all traces!");
				return;
			}
			timer.schedule(getTask(trace, action), new Date((long) (currentBase
					+ (trace.timeStamp - traceBase) * 1000 * slowRatio)));
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

			timer.schedule(getTask(trace, action), new Date((long) (currentBase
					+ (trace.timeStamp - traceBase) * 1000 * slowRatio)));
		}
		return timer;
	}

	//get task(combine traceTask&normalTask)
	private TimerTask getTask(Trace trace, BiConsumer action)
	{
		if (isUseTraceContent())
		{
			return getTraceTask(trace.content, action);
		} else
		{
			return getRequestTask(action);
		}
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
				MyLog.logger.fine(requestCount + ". request URL:" + url);

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
				MyLog.logger.info("----------------------------use all traces!");
				return null;
			}
			String[] oneLineArray = tmp.split(",");
			String timestamp = oneLineArray[1];
			String contentId =
					oneLineArray.length >= 5 ? null : oneLineArray[4];
			return new Trace(Long.parseLong(timestamp), contentId);
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
