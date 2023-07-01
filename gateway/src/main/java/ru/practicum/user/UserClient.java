package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(UserDto user) {
        return post("", user);
    }

    @CachePut(cacheNames = "user")
    public ResponseEntity<Object> updateUser(UserDto user, long userId) {
        return patch("/" + userId, user);
    }

    @CacheEvict(cacheNames = "user")
    public void deleteUserById(long id) {
        delete("/" + id);
    }

    @Cacheable(cacheNames = "user")
    public ResponseEntity<Object> getById(long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> getAllUsers(int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", null, parameters);
    }
}
