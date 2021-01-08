import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public abstract class URLIteration implements Iterator<String>
{
	//from [1,rangeTo]
	protected static int rangeTO;
	private static String urlBaseString=null;
	private  static BufferedReader reader;

	static {
		try
		{
			reader = new BufferedReader(
					new FileReader("./conf/url.txt"));
			String tmp = reader.readLine();
			while (tmp!=null){
				String oneLine=tmp;
				tmp=reader.readLine();
				//remove comment line
				if (oneLine.stripLeading().startsWith("#"))
					continue;
				if (urlBaseString==null) {
					urlBaseString=oneLine;
					continue;
				}
				rangeTO=Integer.parseInt(oneLine);
			}
			reader.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	protected static String constructURL(String index)
	{
		//for test
		return String.format(urlBaseString, index);
	}
}