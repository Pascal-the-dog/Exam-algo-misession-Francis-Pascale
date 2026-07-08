package org.example.pokedex_francis_pascale.modele;

import org.example.pokedex_francis_pascale.utils.Connexion;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class PokemonDAO {
    public void sauvegarder(Pokemon pokemon) throws SQLException {
        String sql =
                "INSERT INTO pokemon "
                +"(id,nom,type,type_2,hp,attaque,attaque_speciale,defense,defense_speciale,vitesse,image_url) "
                +"VALUES(?,?,?,?,?,?,?,?,?,?,?) "
                +"ON CONFLICT (id) DO UPDATE SET " +
                "nom = EXCLUDED.nom, " +
                "type = EXCLUDED.type, " +
                "type_2 = EXCLUDED.type_2, " +
                "hp = EXCLUDED.hp, " +
                "attaque = EXCLUDED.attaque, " +
                "attaque_speciale = EXCLUDED.attaque_speciale, " +
                "defense = EXCLUDED.defense, " +
                "defense_speciale = EXCLUDED.defense_speciale, " +
                "vitesse = EXCLUDED.vitesse, " +
                "image_url = EXCLUDED.image_url";;
            
        try(Connection connect = Connexion.getConnexion();
            PreparedStatement pre = connect.prepareStatement(sql)){

            pre.setInt(1, pokemon.id);
            pre.setString(2, pokemon.nom);
            pre.setString(3, pokemon.type);
            pre.setString(4, pokemon.type2);
            pre.setInt(5, pokemon.hp);
            pre.setInt(6, pokemon.attack);
            pre.setInt(7, pokemon.attackSp);
            pre.setInt(8, pokemon.defense);
            pre.setInt(9, pokemon.defenseSp);
            pre.setInt(10, pokemon.vitesse);
            pre.setString(11, pokemon.image_url);

            pre.executeUpdate();
        }
    }

    public List<Pokemon> lister() throws SQLException {
        String sql = "SELECT * FROM pokemon ORDER BY id DESC";
        List <Pokemon> tousEnsemble = new ArrayList<>();
        try(Connection connect = Connexion.getConnexion();
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            
            while(rs.next()){
                Pokemon pokemon = new Pokemon();
                pokemon.id = rs.getInt("id");
                pokemon.nom = rs.getString("nom");
                pokemon.type = rs.getString("type");
                pokemon.type2 = rs.getString("type_2");
                pokemon.hp = rs.getInt("hp");
                pokemon.attack = rs.getInt("attaque");
                pokemon.attackSp = rs.getInt("attaque_speciale");
                pokemon.defense = rs.getInt("defense");
                pokemon.defenseSp = rs.getInt("defense_speciale");
                pokemon.vitesse = rs.getInt("vitesse");
                pokemon.image_url = rs.getString("image_url");
                tousEnsemble.add(pokemon);
            }

        }
        return tousEnsemble;
    }
}
