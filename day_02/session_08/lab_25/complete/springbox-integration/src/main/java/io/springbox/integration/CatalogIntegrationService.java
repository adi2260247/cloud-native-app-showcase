package io.springbox.integration;

import io.springbox.integration.domain.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import rx.Observable;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;

@Service
public class CatalogIntegrationService {
	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "stubMovie")
	public Observable<Movie> getMovie(final String mlId) {
		return new ObservableResult<Movie>() {
			@Override
			public Movie invoke() {
				return restTemplate.getForObject(
						"http://springbox-catalog/movies/{mlId}",
						Movie.class, mlId);
			}
		};
	}

	private Movie stubMovie(final String mlId) {
		Movie stub = new Movie();
		stub.setMlId(mlId);
		stub.setTitle("Interesting...the wrong title. Sssshhhh!");
		return stub;
	}
}
