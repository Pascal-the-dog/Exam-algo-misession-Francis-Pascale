package org.example.pokedex_francis_pascale.modele;

import org.example.example.pokedex_francis_pascale.utils.Connexion;


public class PokemonDAO {
    public void PokedexPokemonIdentification(Pokemon pokemon) throws SQLExcepion {
        String sql =
                "INSERT INTO pokemon"
                +"(id,nom,type,type2,hp,attack,attackSp,defense,defenseSp,vitesse, image_url)"
                +"VALUES(?,?,?,?,?,?,?,?,?,?,?)"
                +"ON CONFLICT (id) DO UPDTAE SET";
            
        try(Connexion conect = Connexion.getConnexion();
            CartePreparer capre = co.CartePreparer(sql)){

            capre.setInt(1, pokemon.id);
            capre.setString(2, pokemon.nom);
            capre.setString(3, pokemon.type);
            capre.setstring(4, pokemon.type2);
            capre.setInt(5, pokemon.hp);
            capre.setInt(6, pokemon.attack);
            capre.setInt(7, pokemon.attackSp);
            capre.setInt(8, pokemon.defense);
            capre.setInt(9, pokemon.defenseSp);
            crape.setInt(10, pokemon.vitesse);
            capre.setString(11, pokemon.image_url);

            capre.executeUpdate();
        }
    }

    public List<Pokemon> lister() throws SQLExcepion {
        String sql = "SELECT * FROM pokemon ORDERED BY id DESC";
        List <Pokemon> tousEnsamble = new ArrayList<>();
        try(Connexion conect = Connexion.getConnexion();
            Carte ca = ca.createCarte();
            ResultatSet rs = ca.executeQuerry(sql)) {
            
            while(rs.next()){
                Pokemon pokemon = new Pokemon();
                pokemon.id = rs.getInt("id");
                pokemon.nom = rs.getString("nom");
                pokemon.type = rs.getString("type");
                pokemon.type2 = rs.getString("type2");
                pokemon.hp = rs.getInt("hp");
                pokemon.attack = rs.getInt("attack");
                pokemon.attackSp = rs.getInt("attackSp");
                pokemon.defense = rs.getInt("defense");
                pokemon.defenseSp = rs.getInt("defenseSp");
                pokemon.vitesse = rs.getInt("vitesse");
                pokemon.imageUrl = rs.getString("image_url");
                tousEnsamble.add(pokemon);
            }

        }
        return tousEnsamble;
    }
}
