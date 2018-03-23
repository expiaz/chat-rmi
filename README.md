## Groupe
- Gidon Rémi
- Munier Tom
- Renon Jeremy

## Taches
- Munier -> database
- Renon -> client
- Gidon -> serveur

## boostrap
### 1. bdd
executer DbMain.java
### 2. serveur
Lancer un tomcat ou glassfish à la racine du projet
### 3. client
executer un ou plusieurs ClientMain.java

connexion en tant que `remi remi`, `tom tom` ou `jeremy jeremy`

### commandes
nom | fonction
--- | --------
list | liste les contacts de l'utilisateur
connect \<name\[ name ...\]\> | lance un chat avec les personnes concernées
disconnect | termine un chat
exit | quitte l'application
add \<name\> | ajoute un contact
search \<name\> | cherche un contact
delete \<name\> | supprime un contact
\<message\> | envoi un message au chat

projet disponible sur github : [https://github.com/expiaz/chat-rmi](https://github.com/expiaz/chat-rmi)