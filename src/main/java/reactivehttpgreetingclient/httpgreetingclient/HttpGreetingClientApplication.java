package reactivehttpgreetingclient.httpgreetingclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class HttpGreetingClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpGreetingClientApplication.class, args);
	}

	@Bean
	WebClient webClient(WebClient.Builder builder) {
		return builder
				.baseUrl("http://localhost:8080")
				.build();
	}
}

@Log4j2
@Component
@RequiredArgsConstructor
class Client {
	private final WebClient client;

	@EventListener(ApplicationReadyEvent.class)
	public void read() {
		var name="Spring Developer";
		this.client
				.get()
				.uri("/greeting/{name}",name)
				.retrieve()
				.bodyToFlux(GreetingResponse.class)
				.subscribe(greetingResponse ->log.info(" Mono :"+greetingResponse.getMessage()));

		this.client
				.get()
				.uri("/greetings/{name}",name)
				.retrieve()
				.bodyToFlux(GreetingResponse.class)
				.subscribe(greetingResponse ->log.info(" Flux :"+greetingResponse.getMessage()));

	}

}
@Data
@NoArgsConstructor
@AllArgsConstructor
class GreetingRequest {
	private String name;

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class GreetingResponse {
	private String message;
}