package org.example.pokedex_francis_pascale.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pokedex_francis_pascale.exceptions.ApiConnexionException;
import org.example.pokedex_francis_pascale.exceptions.ApiErreurException;
import org.example.pokedex_francis_pascale.exceptions.ApiTimeoutException;
import org.example.pokedex_francis_pascale.exceptions.PokemonIntrouvableException;
import org.example.pokedex_francis_pascale.modele.Pokemon;
import org.example.pokedex_francis_pascale.utils.Types;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


public class PokemonApiService {

    private static final String URL = "https://pokeapi.co/api/v2/pokemon/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Pokemon> cache = new HashMap<>(); //Cache local -> string: parce quon peut chercher par nom ou id

    public Pokemon recuperer(String search) throws Exception{

        search = search.toLowerCase().trim();

        if(cache.containsKey(search)){ // cherche si le pokemon est dans le cache
            return cache.get(search);
        }

        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(URL + search))
                    .timeout(java.time.Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            int code = res.statusCode();

            if (code == 404) {
                throw new PokemonIntrouvableException();
            }
            if (code == 500 || code == 503) {
                throw new ApiErreurException();
            }
            if (code != 200) {
                throw new RuntimeException("API erreur : " + code);
            }

            JsonNode pokemon = mapper.readTree(res.body());

            // pokemon ? ===> JSON ===> Dictionnaire
            Pokemon p = new Pokemon();

            p.id = pokemon.get("id").asInt();
            p.nom = pokemon.get("name").asText();

            JsonNode types = pokemon.get("types");
            p.type = Types.TYPES.get(pokemon.get("types").get(0).get("type").get("name").asText());
            if (types.size() > 1) {
                p.type2 = Types.TYPES.get(types.get(1).get("type").get("name").asText());
            }

            JsonNode stats = pokemon.get("stats");
            p.hp = stats.get(0).get("base_stat").asInt();
            p.attack = stats.get(1).get("base_stat").asInt();
            p.defense = stats.get(2).get("base_stat").asInt();
            p.attackSp = stats.get(3).get("base_stat").asInt();
            p.defenseSp = stats.get(4).get("base_stat").asInt();
            p.vitesse = stats.get(5).get("base_stat").asInt();

            p.image_url = pokemon.get("sprites").get("front_default").asText();

            String cleanName = p.nom.toLowerCase().trim();
            p.cry_url = "https://play.pokemonshowdown.com/audio/cries/" + cleanName + ".mp3";

            cache.put(search, p); // insere dans le cache
            cache.put(String.valueOf(p.id), p); //insere dans le cache par ID aussi
            cache.put(p.nom.toLowerCase(), p);
            return p;

        } catch (java.net.http.HttpTimeoutException e) {
            throw new ApiTimeoutException();

        } catch (IOException e){
            throw new ApiConnexionException();
        }
    }
}
