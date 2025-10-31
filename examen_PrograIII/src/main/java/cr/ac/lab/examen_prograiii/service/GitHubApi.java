package cr.ac.lab.examen_prograiii.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import cr.ac.lab.examen_prograiii.model.Repo;
import cr.ac.lab.examen_prograiii.model.User;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class GitHubApi {

    private static final String BASE = "https://api.github.com/users/";
    private final HttpClient client;
    
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public GitHubApi() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    private HttpRequest.Builder request(String url) {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "java-github-client");

        String token = System.getenv("GITHUB_TOKEN");
        if (token != null && !token.isBlank())
            b.header("Authorization", "Bearer " + token);

        return b;
    }

    public User getUser(String username) throws Exception {
        HttpResponse<String> res = client.send(
                request(BASE + username).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        );

        int code = res.statusCode();
        if (code == 404) throw new Exception("❌ Usuario no encontrado (404)");
        if (code == 403) throw new Exception("⚠️ Límite de peticiones alcanzado (403)");
        if (code >= 500) throw new Exception(" Error del servidor de GitHub (" + code + ")");

        try {
            
            return mapper.readValue(res.body(), User.class);
        } catch (Exception e) {
            throw new Exception("⚠️ Respuesta JSON inválida del servidor\n" + e.getMessage());
        }
    }

    public List<Repo> getRepos(String username) throws Exception {
        HttpResponse<String> res = client.send(
                request(BASE + username + "/repos?per_page=100&sort=updated").GET().build(),
                HttpResponse.BodyHandlers.ofString()
        );

        int code = res.statusCode();
        if (code == 404) throw new Exception("❌ Repositorios no encontrados (404)");
        if (code == 403) throw new Exception("⚠️ Límite de peticiones alcanzado (403)");
        if (code >= 500) throw new Exception(" Error del servidor de GitHub (" + code + ")");

        try {
            
            
            Repo[] repos = mapper.readValue(res.body(), Repo[].class);
            return Arrays.stream(repos)
                    .sorted((a, b) -> {
                        if (b.getUpdated_at() == null || a.getUpdated_at() == null) return 0;
                        return b.getUpdated_at().compareTo(a.getUpdated_at());
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("⚠️ No se pudieron procesar los repositorios\n" + e.getMessage());
        }
    }
}
