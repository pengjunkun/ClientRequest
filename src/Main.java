import java.time.Duration;

/**
 * Created by JackPeng(pengjunkun@gmail.com) on 2021/1/1.
 */
public class Main
{
	public static void main(String[] args)
	{
		Client client = new Client(0.1, Client.RHYTHM.BALANCE, 2, Client.POPULARITY.EVEN,
				0);
		client.run();
	}
}
