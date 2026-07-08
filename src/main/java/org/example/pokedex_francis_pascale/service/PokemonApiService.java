package org.example.pokedex_francis_pascale.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pokedex_francis_pascale.modele.Pokemon;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokemonApiService {

    private static final String URL = "https://pokeapi.co/api/v2/pokemon/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Pokemon recuperer(String id) throws Exception{
        HttpRequest req = HttpRequest.newBuilder(URI.create(URL+id)).GET().build();
        HttpResponse<String> res = client.send(req,HttpResponse.BodyHandlers.ofString());

        if(res.statusCode() != 200){
            throw new RuntimeException("API erreur");
        }

        JsonNode pokemon = mapper.readTree(res.body());

        System.out.println(pokemon);
        // pokemon ? ===> JSON ===> Dictionnaire
        Pokemon p = new Pokemon();

        p.id = pokemon.get("id").asInt();
        p.nom = pokemon.get("name").asText();

        JsonNode types = pokemon.get("types");
        p.type = types.get(0).get("type").get("name").asText();
        if (types.size()>1){
            p.type2 = types.get(1).get("type").get("name").asText();
        }

        JsonNode stats = pokemon.get("stats");
        p.hp = stats.get(0).get("base_stat").asInt();
        p.attack = stats.get(1).get("base_stat").asInt();
        p.defense = stats.get(2).get("base_stat").asInt();
        p.attackSp = stats.get(3).get("base_stat").asInt();
        p.defenseSp = stats.get(4).get("base_stat").asInt();
        p.vitesse = stats.get(5).get("base_stat").asInt();

        p.imageUrl = pokemon.get("sprites").get("front_default").asText();

        return p;
    }
}
