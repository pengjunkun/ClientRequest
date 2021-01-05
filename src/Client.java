/*
	this class is used to create a new user who have some requests to send
 */
public class Client
{
	private WorkAgent agent;
	//describe the requesting rhythm which also can be seen as the requesting interval time
	public enum RHYTHM
	{
		BALANCE, POSSION,TRACE
	}

	//describe the requesting contents(urls) portion
	public enum POPULARITY
	{
		EVEN, ZIPF
	}

	//init a agent to do the detail execution.
	public Client(double duration, RHYTHM rhythm, String rhythm_param,
			POPULARITY popularity, double zipf_param)
	{
		agent=new WorkAgent();
		agent.setDuration(duration);
		agent.setRequestRhythm(rhythm,rhythm_param);
		agent.setPopularity(popularity,zipf_param);
	}

	//start
	public void run(){
		agent.doRequest();
	}

}