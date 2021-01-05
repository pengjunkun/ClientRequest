import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Created by JackPeng(pengjunkun@gmail.com) on 2021/1/2.
 */
public class MyHttp
{
	private  HttpClient client;
	private CompletableFuture<HttpResponse<Path>> response;

	public MyHttp()
	{
		this.client = HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_1_1)
			.connectTimeout(Duration.ofSeconds(20))
			.build();
	}

	//
	public CompletableFuture<HttpResponse<Void>> get(String uri) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();
		//use bodyhanlder to convert receiving bytes into a file
			CompletableFuture<HttpResponse<Void>> response=client.sendAsync(request,
					HttpResponse.BodyHandlers.discarding());
		return response;
	}

}
